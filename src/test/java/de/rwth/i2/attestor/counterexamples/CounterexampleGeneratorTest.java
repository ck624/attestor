package de.rwth.i2.attestor.counterexamples;

import de.rwth.i2.attestor.MockupSceneObject;
import de.rwth.i2.attestor.exampleFactories.ExampleFactoryEmpty;
import de.rwth.i2.attestor.exampleFactories.ExampleFactorySLL;
import de.rwth.i2.attestor.grammar.languageInclusion.LanguageInclusionStrategy;
import de.rwth.i2.attestor.grammar.languageInclusion.LanguageInclusionStrategyBuilder;
import de.rwth.i2.attestor.graph.SelectorLabel;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.internal.ExampleHcImplFactory;
import de.rwth.i2.attestor.main.scene.SceneObject;
import de.rwth.i2.attestor.programState.defaultState.DefaultProgramState;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.AssignStmt;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Field;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Local;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.SemanticsCommand;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpaceGenerationAbortedException;
import de.rwth.i2.attestor.stateSpaceGeneration.impl.ProgramImpl;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.util.NotSufficientlyMaterializedException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class CounterexampleGeneratorTest {

    private SceneObject sceneObject;
    private ExampleHcImplFactory hcFactory;

    @Before
    public void setUp() {

        sceneObject = new MockupSceneObject();
        ExampleFactorySLL factorySLL = new ExampleFactorySLL(sceneObject);
        sceneObject.scene().strategies().setLenientCanonicalizationStrategy(factorySLL.getCanonicalization());


        LanguageInclusionStrategy languageInclusionStrategy = new LanguageInclusionStrategyBuilder()
                .setCanonicalizationStrategy(sceneObject.scene().strategies().getLenientCanonicalizationStrategy())
                .setSingleStepConcretizationStrategy(sceneObject.scene().strategies().getSingleStepConcretizationStrategy())
                .build();

        sceneObject.scene().strategies().setLanguageInclusionStrategy(languageInclusionStrategy);
        hcFactory = new ExampleHcImplFactory(sceneObject);
    }

    @Test
    public void testTrivial() {

        HeapConfiguration input = hcFactory.getList();
        ProgramState initialState = new DefaultProgramState(input.clone());
        Type type = sceneObject.scene().getType("List");
        ProgramImpl program = getSetNextProgram(type);

        ProgramState finalState = null;
        try {
            finalState = program.getStatement(0).computeSuccessors(initialState.clone()).iterator().next();
        } catch (NotSufficientlyMaterializedException | StateSpaceGenerationAbortedException e) {
            fail();
        }

        MockupTrace trace = new MockupTrace();
        trace.addState(initialState)
                .addState(finalState);

        ExampleFactoryEmpty factoryEmpty = new ExampleFactoryEmpty();
        CounterexampleGenerator generator = CounterexampleGenerator
                .builder()
                .setProgram(program)
                .setTrace(trace)
                .setCanonicalizationStrategy(factoryEmpty.getCanonicalization())
                .setAvailableMethods(Collections.emptySet())
                .setStateSubsumptionStrategy((subsumed, subsuming) -> subsuming.equals(subsumed))
                .setScopeExtractorFactory(method -> null)
                .setMaterializationStrategy(factoryEmpty.getMaterialization())
                .setStateRefinementStrategy(factoryEmpty.getStateRefinement())
                .build();

        ProgramState counterexampleInput = generator.generate();
        assertEquals(input, counterexampleInput.getHeap());
    }

    private ProgramImpl getSetNextProgram(Type type) {

        SelectorLabel next = sceneObject.scene().getSelectorLabel("next");
        return ProgramImpl.builder()
                .addStatement(
                        new AssignStmt(
                                sceneObject,
                                new Local(type, "x"),
                                new Field(type, new Local(type, "x"), next),
                                -1, Collections.emptySet()
                        )
                )
                .build();
    }

    @Test
    public void testWithMaterialization() {

        ExampleFactoryEmpty factoryEmpty = new ExampleFactoryEmpty();
        ExampleFactorySLL factorySLL = new ExampleFactorySLL(sceneObject);

        ProgramImpl program = getSetNextProgram(factorySLL.getNodeType());
        SemanticsCommand stmt = program.getStatement(0);
        ProgramState initialState = getInitialState();

        Collection<HeapConfiguration> mat = factorySLL
                .getMaterialization()
                .materialize(initialState.clone().getHeap(), stmt.getPotentialViolationPoints());
        HeapConfiguration materialized = null;
        for (HeapConfiguration s : mat) {
            if (!s.nonterminalEdges().isEmpty()) {
                materialized = s;
                break;
            }
        }
        assertNotNull(materialized);

        ProgramState finalState = null;
        try {
            finalState = stmt.computeSuccessors(
                    initialState.shallowCopyWithUpdateHeap(materialized.clone())
            ).iterator().next();
            finalState = finalState.shallowCopyWithUpdateHeap(
                    factorySLL.getCanonicalization().canonicalize(finalState.getHeap())
            );
        } catch (NotSufficientlyMaterializedException | StateSpaceGenerationAbortedException e) {
            fail();
        }

        MockupTrace trace = new MockupTrace();
        trace.addState(initialState)
                .addState(finalState);

        LanguageInclusionStrategy languageInclusionStrategy = new LanguageInclusionStrategyBuilder()
                .setCanonicalizationStrategy(sceneObject.scene().strategies().getLenientCanonicalizationStrategy())
                .setSingleStepConcretizationStrategy(sceneObject.scene().strategies().getSingleStepConcretizationStrategy())
                .build();


        CounterexampleGenerator generator = CounterexampleGenerator
                .builder()
                .setProgram(program)
                .setTrace(trace)
                .setCanonicalizationStrategy(factorySLL.getCanonicalization())
                .setMaterializationStrategy(factorySLL.getMaterialization())
                .setStateRefinementStrategy(factoryEmpty.getStateRefinement())
                .setAvailableMethods(Collections.emptySet())
                .setStateSubsumptionStrategy((subsumed, subsuming) -> languageInclusionStrategy.includes(subsumed.getHeap(), subsuming.getHeap()))
                .setScopeExtractorFactory(method -> null)
                .build();

        ProgramState counterexampleInput = generator.generate();
        HeapConfiguration expected = factorySLL.getListofLengthAtLeastOne();
        expected.builder()
                .addVariableEdge("x", expected.nodes().get(0))
                .build();

        ProgramState expectedState = sceneObject.scene().createProgramState(expected);

        assertEquals(expectedState.getHeap(), counterexampleInput.getHeap());
    }

    private ProgramState getInitialState() {

        ExampleFactorySLL factorySLL = new ExampleFactorySLL(sceneObject);
        ProgramState initialState = factorySLL.getInitialState();
        initialState.getHeap()
                .builder()
                .addVariableEdge("x", initialState.getHeap().nodes().get(0))
                .build();

        return initialState;
    }
}
