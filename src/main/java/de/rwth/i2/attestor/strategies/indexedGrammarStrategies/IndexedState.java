package de.rwth.i2.attestor.strategies.indexedGrammarStrategies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rwth.i2.attestor.graph.SelectorLabel;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.ConcreteValue;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.GeneralConcreteValue;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.strategies.GeneralJimpleProgramState;
import de.rwth.i2.attestor.types.Type;

public class IndexedState extends GeneralJimpleProgramState {
	private static final Logger logger = LogManager.getLogger( "IndexedState" );

	public IndexedState( HeapConfiguration heap ) {
		super( heap);
	}
	
	public IndexedState( HeapConfiguration heap, int scopeDepth ) {
		
		super( heap, scopeDepth);
	}
	
	private IndexedState(IndexedState state) {
		
		super( state );
	}
	

	@Override
	public ProgramState shallowCopy() {
		
		return new IndexedState(this);
	}



	public int hashCode() {

		int hash = programCounter;
		hash = (hash << 1) ^ scopeDepth;
		hash = (hash << 1) ^ heap.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {

		if(other == this) {
			return true;
		}

		if(other == null) {
			return false;
		}

		if(other.getClass() != IndexedState.class) {
			return false;
		}

		IndexedState state = (IndexedState) other;

		return programCounter == state.programCounter
			&& scopeDepth == state.scopeDepth
			&& atomicPropositions.equals(state.getAPs())
			&& heap.equals(state.getHeap());
	}

	@Override
	public GeneralConcreteValue getSelectorTarget(ConcreteValue from, String selectorName) {
			if(from instanceof GeneralConcreteValue) {
			
			GeneralConcreteValue dFrom = (GeneralConcreteValue) from;
			
			if(dFrom.isUndefined()) {
				logger.debug("getSelectorTarget: origin is undefined. Returning undefined.");
				return dFrom;
			}

			int node = dFrom.getNode();
			
			for( SelectorLabel label : getHeap().selectorLabelsOf(node) ){
				
				AnnotatedSelectorLabel sel = (AnnotatedSelectorLabel) label;
				
				if( sel.hasLabel(selectorName) ){
					int target = getHeap().selectorTargetOf(node, sel);
					Type type = getHeap().nodeTypeOf(target);
					return new GeneralConcreteValue( type, target );
					
				}
			}
			logger.debug("getSelectorTarget: source node did not have requested selector");

		} else {
			logger.warn("getSelectorTarget got invalid source");
		}
		
		return GeneralConcreteValue.getUndefined();
	}

	@Override
	public void setSelector(ConcreteValue from, String selectorName, ConcreteValue to) {
		if(from.isUndefined() || to.isUndefined()) {
			
			logger.warn("Specified edge has invalid source or target.");
			return;
		}
		
		if(from instanceof GeneralConcreteValue && to instanceof GeneralConcreteValue) {

			GeneralConcreteValue dFrom = (GeneralConcreteValue) from;
			GeneralConcreteValue dTo = (GeneralConcreteValue) to;
			
			int fromNode = dFrom.getNode();
			
			for( SelectorLabel label : getHeap().selectorLabelsOf(fromNode)  ){
				
				AnnotatedSelectorLabel sel = (AnnotatedSelectorLabel) label;
				
				if( sel.hasLabel(selectorName)){
					this.getHeap()
						.builder()
						.removeSelector(fromNode, sel)
						.build();
				}
			}
			
			AnnotatedSelectorLabel newSel = new AnnotatedSelectorLabel(selectorName, "");
			
			this.getHeap()
			.builder()
			.addSelector(fromNode, newSel, dTo.getNode())
			.build();
		}
		
	}

	@Override
	public ProgramState shallowCopyUpdatePC(int newPC) {
		IndexedState result = new IndexedState(this);
		result.setProgramCounter(newPC);
		return result;
	}

	@Override
	public IndexedState clone() {
		HeapConfiguration newHeap = heap.clone();
		IndexedState result = new IndexedState(newHeap);
		result.setProgramCounter( programCounter );
		result.scopeDepth = scopeDepth;
		return result;
	}
	
}
