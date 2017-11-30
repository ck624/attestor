package de.rwth.i2.attestor.counterexampleGeneration;

import static org.junit.Assert.*;

import java.util.*;

import de.rwth.i2.attestor.MockupSceneObject;
import de.rwth.i2.attestor.main.environment.SceneObject;
import de.rwth.i2.attestor.types.GeneralType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.rwth.i2.attestor.UnitTestGlobalSettings;
import de.rwth.i2.attestor.exampleFactories.ExampleFactoryEmpty;
import de.rwth.i2.attestor.exampleFactories.ExampleFactorySLL;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.internal.ExampleHcImplFactory;
import de.rwth.i2.attestor.main.settings.Settings;
import de.rwth.i2.attestor.programState.defaultState.DefaultProgramState;
import de.rwth.i2.attestor.semantics.TerminalStatement;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.*;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.invoke.*;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Field;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values.Local;
import de.rwth.i2.attestor.stateSpaceGeneration.*;
import de.rwth.i2.attestor.stateSpaceGeneration.impl.NoPostProcessingStrategy;
import de.rwth.i2.attestor.stateSpaceGeneration.impl.NoStateLabelingStrategy;
import de.rwth.i2.attestor.stateSpaceGeneration.impl.NoStateRefinementStrategy;
import de.rwth.i2.attestor.stateSpaceGeneration.impl.StateSpaceBoundedAbortStrategy;
import de.rwth.i2.attestor.programState.defaultState.DefaultProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.impl.*;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.util.NotSufficientlyMaterializedException;
import de.rwth.i2.attestor.util.SingleElementUtil;

public class CounterexampleGeneratorTest {

    private SceneObject sceneObject;
    private ExampleHcImplFactory hcFactory;

    @BeforeClass
    public static void setupClass() {
        UnitTestGlobalSettings.reset();
    }

    @Before
    public void setUp() {
        sceneObject = new MockupSceneObject();
        hcFactory = new ExampleHcImplFactory(sceneObject);
    }

    @Test
    public void testTrivial() {

        HeapConfiguration input = hcFactory.getList();
        ProgramState initialState = new DefaultProgramState(input.clone());
        Type type = sceneObject.scene().getType("List");
        Program program = getSetNextProgram(type);

        ProgramState finalState = null;
        try {
            finalState = program.getStatement(0)
                    .computeSuccessors(initialState.clone(), new MockupSymbolicExecutionObserver()).iterator().next();
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
                .setMaterializationStrategy(factoryEmpty.getMaterialization())
                .setStateRefinementStrategy(factoryEmpty.getStateRefinement())
                .build();

        HeapConfiguration counterexampleInput = generator.generate();
        assertEquals(input, counterexampleInput);
    }

    private Program getSetNextProgram(Type type) {
        return Program.builder()
                .addStatement(
                        new AssignStmt(
                                new Local(type, "x"),
                                new Field(type, new Local(type, "x"), "next"),
                                -1, Collections.emptySet()
                        )
                )
                .build();
    }

    @Test
    public void testWithMaterialization() {

        ExampleFactoryEmpty factoryEmpty = new ExampleFactoryEmpty();
        ExampleFactorySLL factorySLL = new ExampleFactorySLL(sceneObject);

        Program program = getSetNextProgram(factorySLL.getNodeType());
        Semantics stmt = program.getStatement(0);
        ProgramState initialState = getInitialState();

        List<ProgramState> mat = factorySLL
                .getMaterialization()
                .materialize(initialState.clone(), stmt.getPotentialViolationPoints());
        ProgramState materialized = null;
        for(ProgramState s : mat) {
            if(!s.getHeap().nonterminalEdges().isEmpty()) {
                materialized = s;
                break;
            }
        }
        assertNotNull(materialized);

        ProgramState finalState = null;
        try {
            finalState = stmt.computeSuccessors(
                    materialized.clone(), factoryEmpty.getSemanticsOptionsSupplier().get(null)
            ).iterator().next();
            finalState = factorySLL.getCanonicalization().canonicalize(finalState);
        } catch (NotSufficientlyMaterializedException | StateSpaceGenerationAbortedException e) {
            fail();
        }

        MockupTrace trace = new MockupTrace();
        trace.addState(initialState)
                .addState(finalState);

        CounterexampleGenerator generator = CounterexampleGenerator
                .builder()
                .setProgram(program)
                .setTrace(trace)
                .setCanonicalizationStrategy(factorySLL.getCanonicalization())
                .setMaterializationStrategy(factorySLL.getMaterialization())
                .setStateRefinementStrategy(factoryEmpty.getStateRefinement())
                .build();

        HeapConfiguration counterexampleInput = generator.generate();
        HeapConfiguration expected = factorySLL.getListofLengthAtLeastOne();
        expected.builder()
                .addVariableEdge("x", expected.nodes().get(0))
                .build();
        assertEquals(expected, counterexampleInput);
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

    @Test
    public void testWithProcedures() {

        ExampleFactoryEmpty factoryEmpty = new ExampleFactoryEmpty();
        ExampleFactorySLL factorySLL = new ExampleFactorySLL(sceneObject);

        AssignInvoke invokeStmt = getProcedure();
        Program program = Program.builder()
                .addStatement(invokeStmt)
                .addStatement(new Skip(2))
                .addStatement(new TerminalStatement())
                .build();

        ProgramState initialState = getInitialState();

        ProgramState finalState = null;
        try {
            Set<ProgramState> successors = invokeStmt.computeSuccessors(initialState.clone(),
                    new SymbolicExecutionObserver() {
                        @Override
                        public void update(Object handler, ProgramState input) {

                        }

                        @Override
                        public StateSpace generateStateSpace(Program program, ProgramState input) throws StateSpaceGenerationAbortedException {
                            ProgramState initialState = new DefaultProgramState(input.getHeap());
                            initialState.setProgramCounter(0);
                            return StateSpaceGenerator.builder()
                                    .addInitialState(initialState)
                                    .setProgram(program)
                                    .setStateRefinementStrategy(new NoStateRefinementStrategy())
                                    .setAbortStrategy(new StateSpaceBoundedAbortStrategy(500, 50))
                                    .setStateLabelingStrategy(new NoStateLabelingStrategy())
                                    .setMaterializationStrategy(factorySLL.getMaterialization())
                                    .setCanonizationStrategy(factorySLL.getCanonicalization())
                                    .setStateCounter( s -> {} )
                                    .setExplorationStrategy((s,sp) -> true)
                                    .setStateSpaceSupplier(() -> new InternalStateSpace(100))
                                    .setSemanticsOptionsSupplier(s -> this)
                                    .setPostProcessingStrategy(new NoPostProcessingStrategy())
                                    .build()
                                    .generate();
                        }

                        @Override
                        public boolean isDeadVariableEliminationEnabled() {
                            return false;
                        }
                    }
            );
            assertEquals(2, successors.size());
            for(ProgramState s : successors) {
                if(s.getHeap().countNonterminalEdges() == 2) {
                    finalState = s;
                    break;
                }
            }
        } catch (NotSufficientlyMaterializedException | StateSpaceGenerationAbortedException e) {
            fail();
        }

        MockupTrace trace = new MockupTrace();
        assert finalState != null;
        trace.addState(initialState)
                .addState(finalState)
                .addState(finalState.shallowCopyUpdatePC(2))
                .addState(finalState.shallowCopyUpdatePC(-1));

        CounterexampleGenerator generator = CounterexampleGenerator
                .builder()
                .setProgram(program)
                .setTrace(trace)
                .setCanonicalizationStrategy(factorySLL.getCanonicalization())
                .setMaterializationStrategy(factorySLL.getMaterialization())
                .setStateRefinementStrategy(factoryEmpty.getStateRefinement())
                .build();

        HeapConfiguration counterexampleInput = generator.generate();

        HeapConfiguration expected = factorySLL
                .getListofLengthAtLeastOne()
                .builder()
                .addVariableEdge("x", 0)
                .build();

        assertEquals(expected, counterexampleInput);
    }

    private AssignInvoke getProcedure() {

        AbstractMethod procedure = new SimpleAbstractMethod("method");

        ExampleFactorySLL factorySLL = new ExampleFactorySLL(sceneObject);

        Local varY = new Local(factorySLL.getNodeType(), "y");
        Field fieldN = new Field(factorySLL.getNodeType(), varY, factorySLL.getNextSel().getLabel());

        List<Semantics> controlFlow = new ArrayList<>();
        controlFlow.add( new IdentityStmt(1, varY, "@parameter0:"));

        controlFlow.add( new AssignStmt(varY, fieldN, 2, Collections.emptySet()));
        controlFlow.add( new ReturnValueStmt(varY, factorySLL.getNodeType()) );
        procedure.setControlFlow( controlFlow );

        Local varX = new Local(factorySLL.getNodeType(), "x");
        StaticInvokeHelper invokeHelper = new StaticInvokeHelper(SingleElementUtil.createList(varX));
        return new AssignInvoke(varX, procedure, invokeHelper, 1);
    }
}
