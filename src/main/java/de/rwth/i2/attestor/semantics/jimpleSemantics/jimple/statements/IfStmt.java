package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements;

import de.rwth.i2.attestor.main.settings.Settings;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.VariablesUtil;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.ConcreteValue;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.NullPointerDereferenceException;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Value;
import de.rwth.i2.attestor.semantics.util.Constants;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.SemanticsObserver;
import de.rwth.i2.attestor.stateSpaceGeneration.ViolationPoints;
import de.rwth.i2.attestor.util.NotSufficientlyMaterializedException;
import de.rwth.i2.attestor.util.SingleElementUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * IfStmt models statements like if condition goto pc
 * 
 * @author Hannah Arndt
 *
 */
public class IfStmt extends Statement {

	private static final Logger logger = LogManager.getLogger( "IfStmt" );

	/**
	 * the condition on which the successor state is determined
	 */
	private final Value conditionValue;
	/**
	 * the program counter of the successor state in case the condition
	 * evaluates to true
	 */
	private final int truePC;
	/**
	 * the program counter of the successor state in case the condition
	 * evaluates to false
	 */
	private final int falsePC;
	
	private final Set<String> liveVariableNames;

	public IfStmt( Value condition, int truePC, int falsePC, Set<String> liveVariableNames){

		this.conditionValue = condition;
		this.truePC = truePC;
		this.falsePC = falsePC;
		this.liveVariableNames = liveVariableNames;
	}

	/**
	 * evaluates the condition on the input heap. Returns the resulting heap
	 * (side effects of the condition will last) together with the appropriate
	 * program counter. <br>
	 * In case the condition evaluates to undefined, the result will contain
	 * both program counters.<br>
	 * 
	 * If any of the variables in the condition are not live after this statement,
	 * it will be removed from the heap to enable abstraction.
	 */
	@Override
	public Set<ProgramState> computeSuccessors(ProgramState programState, SemanticsObserver options)
			throws NotSufficientlyMaterializedException{

		options.update(this, programState);

		Set<ProgramState> defaultRes = new HashSet<>();
		defaultRes.add(programState.shallowCopyUpdatePC(truePC));
		defaultRes.add(programState.shallowCopyUpdatePC(falsePC));

		programState = programState.clone();
		ConcreteValue trueValue = programState.getConstant(Constants.TRUE);
		ConcreteValue falseValue = programState.getConstant(Constants.FALSE);

		ConcreteValue concreteCondition;
		try {
			concreteCondition = conditionValue.evaluateOn( programState );
		} catch (NullPointerDereferenceException e) {
			logger.error(e.getErrorMessage(this));
			concreteCondition = programState.getUndefined();
		}
		
		if( concreteCondition.isUndefined() ){
			return defaultRes;
		}
		if( !concreteCondition.type().equals( Settings.getInstance().factory().getType( "int" ) ) ){
			logger.debug( "concreteCondition is not of type int, but " + concreteCondition.type() );
		}

		if(options.isDeadVariableEliminationEnabled()) {
			VariablesUtil.removeDeadVariables(conditionValue.toString(), programState, liveVariableNames);
		}

		if( concreteCondition.equals( trueValue ) ){
			return Collections.singleton(programState.shallowCopyUpdatePC(truePC));
		}else if( concreteCondition.equals( falseValue )){
			return Collections.singleton(programState.shallowCopyUpdatePC(falsePC));
		}else{
			return defaultRes;
		}
	}

	@Override
	public boolean needsMaterialization( ProgramState programState ){
		return conditionValue.needsMaterialization( programState );
	}


	public String toString(){
		return "if( " + conditionValue + ") goto " + truePC + " else goto " + falsePC;
	}

	@Override
	public ViolationPoints getPotentialViolationPoints() {
		
		return conditionValue.getPotentialViolationPoints();
	}
	
	@Override
	public Set<Integer> getSuccessorPCs() {
		
		Set<Integer> res = SingleElementUtil.createSet(truePC);
		res.add(falsePC);
		return res;
	}
}
