package de.rwth.i2.attestor.grammar;

import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Immutable data-object storing all the rules (lhs &#8594; rhs).
 * For construction use a {@link GrammarBuilder}
 *
 * @author Hannah
 */
public class Grammar {

    final Map<Nonterminal, Set<HeapConfiguration>> rules;

    public Grammar(Map<Nonterminal, Set<HeapConfiguration>> rules) {

        this.rules = rules;
    }

    public static GrammarBuilder builder() {

        return new GrammarBuilder();
    }

    /**
     * Gets all rule graphs of rules with the specified left hand side.
     *
     * @param nonterminal The left hand side
     * @return an unmodifiable view of the rules' set
     */
    public Set<HeapConfiguration> getRightHandSidesFor(Nonterminal nonterminal) {

        if (rules.containsKey(nonterminal)) {
            return Collections.unmodifiableSet(rules.get(nonterminal));
        } else {
            return new HashSet<>();
        }
    }


    /**
     * @return an unmodifiable view of the set of left hand sides
     */
    public Set<Nonterminal> getAllLeftHandSides() {

        return Collections.unmodifiableSet(rules.keySet());
    }


}
