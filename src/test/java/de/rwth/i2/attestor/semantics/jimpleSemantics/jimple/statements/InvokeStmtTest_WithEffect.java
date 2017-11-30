package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements;

import static org.junit.Assert.*;

import java.util.*;

import de.rwth.i2.attestor.MockupSceneObject;
import de.rwth.i2.attestor.main.environment.SceneObject;
import de.rwth.i2.attestor.semantics.util.Constants;
import org.junit.*;

import de.rwth.i2.attestor.UnitTestGlobalSettings;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.internal.ExampleHcImplFactory;
import de.rwth.i2.attestor.ipa.IpaAbstractMethod;
import de.rwth.i2.attestor.main.settings.Settings;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.invoke.AbstractMethod;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.invoke.StaticInvokeHelper;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Field;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Local;
import de.rwth.i2.attestor.stateSpaceGeneration.*;
import de.rwth.i2.attestor.programState.defaultState.DefaultProgramState;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.util.NotSufficientlyMaterializedException;
import de.rwth.i2.attestor.util.SingleElementUtil;

public class InvokeStmtTest_WithEffect {


	private SceneObject sceneObject;
	private ExampleHcImplFactory hcFactory;

	private DefaultProgramState testInput;
	private HeapConfiguration expectedHeap;
	private InvokeStmt stmt;


	@BeforeClass
	public static void init()
	{
		UnitTestGlobalSettings.reset();
	}

	@Before
	public void setUp() throws Exception {

		sceneObject = new MockupSceneObject();
		hcFactory = new ExampleHcImplFactory(sceneObject);

		testInput = new DefaultProgramState( hcFactory.getInput_InvokeWithEffect() );
		testInput.prepareHeap();
		
		DefaultProgramState expectedState = new DefaultProgramState( hcFactory.getExpectedResult_InvokeWithEffect() );
		expectedState.prepareHeap();
		expectedHeap = expectedState.getHeap();
		
		Type type = sceneObject.scene().getType("List");
		type.addSelectorLabel("next", Constants.NULL);
		Local varX = new Local(type, "x");
		Local varY = new Local(type, "y");
		Field nextOfX = new Field(type, varX, "next");
		Field nextOfY = new Field(type, varY, "next");
		
		AbstractMethod method = new IpaAbstractMethod("method");
		List<Semantics> methodBody = new ArrayList<>();
		methodBody.add( new IdentityStmt(1, varY, "@parameter0:"));
		
		HashSet<String> liveVariables = new HashSet<>();	
		methodBody.add( new AssignStmt(nextOfY, varY, 2, liveVariables));
		methodBody.add( new ReturnValueStmt(varY, type) );
		method.setControlFlow( methodBody );
		
		StaticInvokeHelper invokeHelper = new StaticInvokeHelper(SingleElementUtil.createList(nextOfX));
		stmt = new InvokeStmt(method, invokeHelper, 1);
		
	}

	@Test
	public void testComputeSuccessors() {
		try {
			Set<ProgramState> resStates = stmt.computeSuccessors( testInput, new MockupSymbolicExecutionObserver() );
			assertEquals( 1, resStates.size() );
			assertEquals( expectedHeap, resStates.iterator().next().getHeap() );
		} catch (NotSufficientlyMaterializedException | StateSpaceGenerationAbortedException e) {
			e.printStackTrace();
			fail( "unexpected exception");
		}
	}

	@Test
	public void testNeedsMaterialization() {

		assertTrue( stmt.needsMaterialization(testInput) );
	}

}
