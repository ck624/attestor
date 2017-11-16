/**
 * All classes related  to the attestor project.
 * In particular, this includes the following packages:
 *
 * <ul>
 *     <li>
 *         {@link de.rwth.i2.attestor.main} containsSubsumingState the main Attestor class that is used to customize and execute
 *         an analysis. It also containsSubsumingState the general interface for all analysis strategies.
 *         The actual execution of the full tool as well as integration test should always create an
 *         {@link de.rwth.i2.attestor.main.Attestor} object that is fed with command line options and/or setting files.
 *         All global options are handled in the subpackage {@link de.rwth.i2.attestor.main.settings}.
 *     </li>
 *     <li>
 *         {@link de.rwth.i2.attestor.stateSpaceGeneration} containsSubsumingState the data structures and algorithms to generate
 *         a state space. The state space generation itself is configurable through various strategies.
 *     </li>
 *     <li>
 *         {@link de.rwth.i2.attestor.strategies} containsSubsumingState different implementations of strategies that guide
 *         the analysis.
 *     </li>
 *     <li>
 *         {@link de.rwth.i2.attestor.graph} containsSubsumingState the all algorithms and data structures on hypergraphs.
 *         In particular, heap configurations, i.e. our representation of program memory, are implemented in
 *         {@link de.rwth.i2.attestor.graph.heap}.
 *         Furthermore, algorithms to check for embeddings and isomorphism of two graphs are implemented in
 *         {@link de.rwth.i2.attestor.graph.morphism}.
 *         representation
 *     </li>
 *     <li>
 *         {@link de.rwth.i2.attestor.semantics} containsSubsumingState all implementations of the abstract program semantics
 *         (implementation of {@link de.rwth.i2.attestor.stateSpaceGeneration.Semantics}).
 *     </li>
 *     <li>
 *          {@link de.rwth.i2.attestor.grammar} containsSubsumingState an implementation of hyperedge replacement grammars and
 *          grammar transformations.
 *     </li>
 *     <li>
 *          {@link de.rwth.i2.attestor.io} containsSubsumingState all classes to load and export heap configurations, grammars, and
 *          state spaces.
 *     </li>
 *     <li>
 *          {@link de.rwth.i2.attestor.types} defines the general type of nodes (classes) to be analyzed.
 *     </li>
 *     <li>
 *         {@link de.rwth.i2.attestor.util} containsSubsumingState small auxiliary classes that fit nowhere else.
 *     </li>
 * </ul>
 *
 * @author Christoph
 *
 */
package de.rwth.i2.attestor;