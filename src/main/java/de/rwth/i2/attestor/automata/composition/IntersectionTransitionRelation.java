package de.rwth.i2.attestor.automata.composition;

import de.rwth.i2.attestor.automata.AutomatonState;
import de.rwth.i2.attestor.automata.TransitionRelation;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Transition relation of a heap automaton that are the intersection of two other heap automata.
 *
 * @author Christoph
 */
public class IntersectionTransitionRelation implements TransitionRelation {

    private TransitionRelation firstRelation;
    private TransitionRelation secondRelation;

    public IntersectionTransitionRelation(TransitionRelation firstRelation, TransitionRelation secondRelation) {

        this.firstRelation = firstRelation;
        this.secondRelation = secondRelation;
    }

    @Override
    public AutomatonState move(List<AutomatonState> ntAssignment, HeapConfiguration heapConfiguration) {

        List<AutomatonState> firstAssignment = new ArrayList<>(ntAssignment.size());
        List<AutomatonState> secondAssignment = new ArrayList<>(ntAssignment.size());
        for(AutomatonState state : ntAssignment) {
            if(state instanceof IntersectionAutomatonState) {
                IntersectionAutomatonState iState = (IntersectionAutomatonState) state;
                firstAssignment.add(iState.first());
                secondAssignment.add(iState.second());
            } else {
                throw new IllegalArgumentException("Invalid state assigned to nonterminal hyperedge.");
            }
        }
        return new IntersectionAutomatonState(
                firstRelation.move(firstAssignment, heapConfiguration),
                secondRelation.move(secondAssignment, heapConfiguration)
        );
    }

}