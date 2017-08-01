package de.rwth.i2.attestor.indexedGrammars;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rwth.i2.attestor.grammar.Grammar;
import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.heap.*;
import de.rwth.i2.attestor.graph.heap.matching.AbstractMatchingChecker;
import de.rwth.i2.attestor.graph.heap.matching.EmbeddingChecker;
import de.rwth.i2.attestor.indexedGrammars.stack.*;
import de.rwth.i2.attestor.main.settings.Settings;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.ReturnValueStmt;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.ReturnVoidStmt;
import de.rwth.i2.attestor.stateSpaceGeneration.*;
import de.rwth.i2.attestor.util.DebugMode;
import de.rwth.i2.attestor.util.SingleElementUtil;
import gnu.trove.iterator.TIntIterator;

public class IndexedCanonicalizationStrategy implements CanonicalizationStrategy {
	private static final Logger logger = LogManager.getLogger( "IndexedCanonicalizationStrategy" );

	/**
	 * The grammar that guides abstraction.
	 */
	private Grammar grammar;

	/**
	 * A flag that determines whether the grammar is backward confluent.
	 * If this is the case, it suffices to consider a single sequence of inverse derivations.
	 */
	private final boolean isConfluent;

	/**
	 * A flag that prevents abstraction of program states whose corresponding program location
	 * has at most one successor.
	 */
	private boolean ignoreUniqueSuccessorStatements;

	private final StackCanonizationStrategy stackCanonizer;
	private final AnnotationsMaintainingStrategy annotationMaintainer;

	/**
	 * Initializes the strategy.
	 * @param grammar The grammar that guides abstraction.
	 * @param isConfluent True if and only if the grammar is backward confluent.
	 */
	public IndexedCanonicalizationStrategy(Grammar grammar, boolean isConfluent) {
		this.grammar = grammar;
		this.isConfluent = isConfluent;
		this.ignoreUniqueSuccessorStatements = false;
		this.stackCanonizer = new AVLStackCanonizationStrategy();
		this.annotationMaintainer = new AVLAnnotationMaintainingStrategy();
	}

	/**
	 * Sets a flag to prevent abstraction of program states with at most one successor.
	 * @param enabled True if and only if program states with at most one successor are not abstracted.
	 */
	public void setIgnoreUniqueSuccessorStatements(boolean enabled) {
		ignoreUniqueSuccessorStatements = enabled;
	}

	/**
	 * Performs the actual grammar based abstraction of a given program state.
	 * @param semantics The program statement executed prior to canonicalization.
	 * @param state The program state that should be abstracted.
	 * @return The set of abstracted program states.
	 */
	@Override
	public Set<ProgramState> canonicalize(Semantics semantics, ProgramState state) {

		IndexedState conf = ((IndexedState) state).clone();

		if( ignoreUniqueSuccessorStatements && !semantics.permitsCanonicalization() ) {

			return SingleElementUtil.createSet( conf );
		}
		annotationMaintainer.maintainAnnotations( conf );

		if( conf.getHeap().countNodes() > Settings.getInstance().options().getAggressiveAbstractionThreshold() ){
			if( DebugMode.ENABLED ){
				logger.trace( "Using aggressive canonization" );
			}
			return performCanonicalization( conf, true );
		}else if( Settings.getInstance().options().isAggressiveReturnAbstraction() 
				&& 
				(semantics instanceof ReturnValueStmt || semantics instanceof ReturnVoidStmt) ){
			return performCanonicalization( conf, true );
		}
		
		return performCanonicalization(conf, false);
	}

	

	private Set<ProgramState> performCanonicalization(IndexedState state, boolean strongCanonicalization) {

		Set<ProgramState> result = new HashSet<>();

		boolean checkNext = true;

		for( Nonterminal nt : grammar.getAllLeftHandSides() ) {

			IndexedNonterminal nonterminal = (IndexedNonterminal) nt;

			if(!checkNext) { break; }

			for(HeapConfiguration pattern : grammar.getRightHandSidesFor(nonterminal) ) {

				if( !checkNext ) { break; }

				stackCanonizer.canonizeStack( state.getHeap() );
				
				AbstractMatchingChecker checker;
				if( strongCanonicalization ){
					checker = new EmbeddingChecker(pattern, state.getHeap() );
				}else{
					checker = state.getHeap().getEmbeddingsOf(pattern);
				}
				
				while(checker.hasNext() && checkNext) {

					checkNext = !isConfluent; 

					IndexedState abstracted  = state;
					if( checkNext ) {
						abstracted = state.clone();
					}

					Matching embedding = checker.getNext();
					resetInstantiation(nonterminal);
					boolean indexMatch = checkIndexMatching(pattern, abstracted, embedding);
					if( indexMatch ){

						replaceEmbeddingBy(abstracted, embedding, nonterminal);
						result.addAll( performCanonicalization( abstracted, strongCanonicalization ) );
					}else{
						checkNext = true;
					}
				}

			}
		}

		if(result.isEmpty()) {	

			result.add(state);
		}

		return result;
	}
	
	private void resetInstantiation(IndexedNonterminal nonterminal) {
		StackSymbol lastSymb = nonterminal.getStack().getLastStackSymbol();
		if( lastSymb instanceof StackVariable ){
			( (StackVariable) lastSymb ).resetInstantiation();
		}
	}

	private void replaceEmbeddingBy(IndexedState abstracted, Matching embedding, IndexedNonterminal nonterminal) {
		HeapConfigurationBuilder builder = abstracted.getHeap().builder();
		builder.replaceMatching( embedding , nonterminal );

		TIntIterator iter = abstracted.getHeap().nonterminalEdges().iterator();
		while(iter.hasNext()) {
			int edge = iter.next();
			IndexedNonterminal label = (IndexedNonterminal) abstracted.getHeap().labelOf(edge);
			builder.replaceNonterminal(edge, label.getWithInstantiation() );
		}
		builder.build();
		stackCanonizer.canonizeStack( abstracted.getHeap() );
	}

	private boolean checkIndexMatching(HeapConfiguration pattern, IndexedState abstracted, Matching embedding) {
		boolean indexMatch = true;
		
		TIntIterator ntEdgeIter = pattern.nonterminalEdges().iterator();
		while(ntEdgeIter.hasNext()) {

			int edge = ntEdgeIter.next();

			IndexedNonterminal patternNt = (IndexedNonterminal) pattern.labelOf(edge);
			IndexedNonterminal targetNt =  (IndexedNonterminal) abstracted.getHeap().labelOf( embedding.match( edge ) );
			indexMatch = targetNt.getStack().matchStack(patternNt.getStack());
			if( ! indexMatch ){
				break;
			}	
		}
		return indexMatch;
	}

}
