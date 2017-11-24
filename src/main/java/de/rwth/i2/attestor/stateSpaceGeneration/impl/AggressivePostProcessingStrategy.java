package de.rwth.i2.attestor.stateSpaceGeneration.impl;

import de.rwth.i2.attestor.semantics.AggressiveTerminalStatement;
import de.rwth.i2.attestor.stateSpaceGeneration.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AggressivePostProcessingStrategy implements PostProcessingStrategy {

    private CanonicalizationStrategy canonicalizationStrategy;

    public AggressivePostProcessingStrategy(CanonicalizationStrategy canonicalizationStrategy) {
        this.canonicalizationStrategy = canonicalizationStrategy;
    }

    @Override
    public void process(StateSpaceGenerator stateSpaceGenerator) {

        assert stateSpaceGenerator.getStateSpace().getClass() == InternalStateSpace.class;

        InternalStateSpace stateSpace = (InternalStateSpace) stateSpaceGenerator.getStateSpace();

        Set<ProgramState> finalStates = stateSpace.getFinalStates();

        Map<ProgramState,ProgramState> abstractedStates = new HashMap<>();
        Map<Integer, Integer> idMap = new HashMap<>();

        for(ProgramState state : finalStates) {
            ProgramState absState = canonicalizationStrategy.canonicalize(state);
            absState.setStateSpaceId(state.getStateSpaceId());
            ProgramState oldState = abstractedStates.put(absState,absState);
            if(oldState != null) {
                idMap.put(state.getStateSpaceId(), oldState.getStateSpaceId());
            } else {
                idMap.put(state.getStateSpaceId(), absState.getStateSpaceId());
            }
        }

        if(abstractedStates.size() < finalStates.size()) {
            stateSpace.updateFinalStates(abstractedStates.keySet(), idMap);
        }
    }
}
