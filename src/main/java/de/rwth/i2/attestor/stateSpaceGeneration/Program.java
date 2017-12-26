package de.rwth.i2.attestor.stateSpaceGeneration;

import de.rwth.i2.attestor.semantics.TerminalStatement;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.AssignInvoke;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.InvokeStmt;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.ReturnValueStmt;
import de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.statements.ReturnVoidStmt;
import gnu.trove.list.array.TIntArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Abstraction of a program that is symbolically executed to generate a state space.
 * In particular, this class provides access to program statements located at a requested position.
 *
 * @author Christoph
 */
public class Program {

    /**
     * The internal representation of the program as a list of program statements.
     * The position in the lost corresponds to the value of the program counter.
     */
    private final List<SemanticsCommand> program;

    /**
     * Initialize this program.
     *
     * @param program The list of statements that make up this program.
     */
    public Program(List<SemanticsCommand> program) {

        this.program = program;

        updateCanonicalizationPermission();
    }

    private Program() {

        program = new ArrayList<>();
    }

    public static ProgramBuilder builder() {

        return new ProgramBuilder();
    }

    /**
     * @return The first statement upon execution of the program.
     */
    public SemanticsCommand getEnterPoint() {

        return program.get(0);
    }

    /**
     * @return The initial value of the program counter upon execution of the program.
     */
    public int getEnterPC() {

        return 0;
    }

    /**
     * @param programCounter A program counter.
     * @return The programs statement corresponding to the given program counter.
     */
    public SemanticsCommand getStatement(int programCounter) {

        if (isExit(programCounter)) {
            return new TerminalStatement();
        }
        return program.get(programCounter);
    }

    /**
     * Determines the program locations at which canonicalization should be performed.
     * These locations correspond to locations that have at least two ingoing edges
     * in the underlying control flow graph.
     */
    private void updateCanonicalizationPermission() {

        TIntArrayList incoming = new TIntArrayList(program.size());
        for (int i = 0; i < program.size(); i++) {
            incoming.add(0);
        }

        for (SemanticsCommand aProgram : program) {

            Set<Integer> out = aProgram.getSuccessorPCs();
            for (Integer pc : out) {

                if (pc >= 0) {
                    int inc = incoming.get(pc) + 1;
                    incoming.set(pc, inc);
                }
            }
        }

        for (int i = 0; i < program.size(); i++) {

            SemanticsCommand s = program.get(i);

            boolean isReturn = s instanceof ReturnValueStmt || s instanceof ReturnVoidStmt;
            boolean isInvoke = s instanceof AssignInvoke || s instanceof InvokeStmt;
            program.get(i).setPermitCanonicalization((incoming.get(i) > 1) || isReturn || isInvoke);
        }

    }

    /**
     * Checks whether the given program location leads to termination of the program.
     *
     * @param programCounter A value of the program counter.
     * @return True if and only of the program location corresponding to programCounter does not correspond to
     * another statement of the program, i.e. we terminate.
     */
    private boolean isExit(int programCounter) {

        return programCounter >= program.size() || programCounter < 0;
    }

    public final static class ProgramBuilder {

        private Program program = new Program();

        protected ProgramBuilder() {

        }

        public Program build() {

            assert !program.program.isEmpty();
            Program result = program;
            program = null;
            return result;
        }

        public ProgramBuilder addStatement(SemanticsCommand semanticsCommand) {

            program.program.add(semanticsCommand);
            return this;
        }
    }
}
