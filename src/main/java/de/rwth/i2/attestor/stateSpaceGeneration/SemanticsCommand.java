package de.rwth.i2.attestor.stateSpaceGeneration;

import de.rwth.i2.attestor.grammar.materialization.ViolationPoints;
import de.rwth.i2.attestor.util.NotSufficientlyMaterializedException;

import java.util.Collection;
import java.util.Set;

/**
 * An abstraction of abstract program semantics that is executed on objects of type {@link ProgramState}.
 *
 * @author Christoph
 */
public interface SemanticsCommand {

    /**
     * Executes a single step of the abstract program semantics on the given program state.
     * Since the abstract program semantics may be non-deterministic (for example if a conditional statement cannot
     * be evaluated), this results in a set of successor program states in general.
     *
     * @param programState The state on which the abstract program semantics shall be executed.
     * @param options      A collection of configuration parameters to influence to symbolic execution.
     * @return All states resulting from executing the program semantics on programState.
     * @throws NotSufficientlyMaterializedException This exception is thrown if the semantics cannot be executed on
     *                                              programState due to missing fields.
     */
    Collection<ProgramState> computeSuccessors(ProgramState programState, SymbolicExecutionObserver options)
            throws NotSufficientlyMaterializedException, StateSpaceGenerationAbortedException;

    /**
     * @return All potential violation points that may prevent execution of this statement.
     */
    ViolationPoints getPotentialViolationPoints();

    /**
     * @return The set of all program locations that are direct successors of this program statement in
     * the underlying control flow graph.
     */
    Set<Integer> getSuccessorPCs();

    /**
     * @return true, if the statement always requires canonicalization
     */
    boolean needsCanonicalization();

}
