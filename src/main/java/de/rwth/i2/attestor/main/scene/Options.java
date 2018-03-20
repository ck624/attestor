package de.rwth.i2.attestor.main.scene;

/**
 * Collects all options that customize the state space generation.
 *
 * @author Hannah Arndt, Christoph
 */
public class Options {

    protected Options() {}

    /**
     * The minimal distance between variables in a heap configuration and embeddings used for abstraction.
     * Increasing this number allows to use a less aggressive abstraction.
     */
    private int abstractionDistance = 0;
    /**
     * Indicates whether the abstraction distance for the null node is set to 0 (otherwise or the specified distance is chosen).
     */
    private boolean aggressiveNullAbstraction = true;
    /**
     * Enabling this option results in dead variables (variables that are not accessed before being rewritten in the
     * following) being deleted in order to enable more possible abstractions.
     */
    private boolean removeDeadVariables = true;
    /**
     * Enabled this option leads to using RefinedNonterminals in graph grammars.
     */
    private boolean grammarRefinementEnabled = false;
    /**
     * If true, unreachable parts of heap are regularly eliminated.
     */
    private boolean garbageCollectionEnabled = true;
    /**
     * Determines if post-processing is applied to generated state spaces.
     */
    private boolean postProcessingEnabled = false;
    /**
     * If enabled, external nodes of rules are collapsed before applying abstraction
     */
    private boolean ruleCollapsingEnabled = true;

    // -----------------------------------------------------------------------------------

    private boolean admissibleAbstractionEnabled = false;

    private boolean admissibleConstantsEnabled = false;

    private boolean admissibleMarkingsEnabled = false;

    private boolean admissibleFullEnabled = false;

    private boolean noChainAbstractionEnabled = false;

    private boolean noRuleCollapsingEnabled = false;

    /**
     * Enabling this option leads to using a program analysis based on indexed hyperedge replacement grammars.
     */
    private boolean indexedModeEnabled = false;

    private boolean canonicalEnabled = false;

    private int maxStateSpace = 5000;

    private int maxHeap = 50;

    // -----------------------------------------------------------------------------------

    /**
     * If enabled, we verify that a counterexample is not spurious (otherwise an invalid LTL formula is set to unknown).
     * This option is disabled by default, because counterexample verification requires a more elaborate state space
     * generation even if all specifications are valid.
     */
    private boolean verifyCounterexamples = false;




    /**
     * @return True if post-processing is applied to generated state spaces.
     */
    public boolean isPostprocessingEnabled() {

        return postProcessingEnabled;
    }

    /**
     * @param
     */
    public void setPostProcessingEnabled(boolean enabled) {

        this.postProcessingEnabled = enabled;
    }

    /**
     * @return The minimal distance between variables and nodes in an embedding before abstraction is performed.
     */
    public int getAbstractionDistance() {

        return abstractionDistance;
    }

    /**
     * @return True if and only if the set abstraction distance should be ignored for the null node (and instead set to 0).
     */
    public boolean getAggressiveNullAbstraction() {

        return aggressiveNullAbstraction;
    }

    /**
     * @return True if and only if dead variables are deleted from heap configurations whenever possible.
     */
    public boolean isRemoveDeadVariables() {

        return removeDeadVariables;
    }

    /**
     * @param removeDeadVariables True if and only if dead variables are deleted from
     *                            heap configurations whenever possible.
     */
    public void setRemoveDeadVariables(boolean removeDeadVariables) {

        this.removeDeadVariables = removeDeadVariables;
    }

    /**
     * @return True if and only if an indexed program analysis is performed.
     */
    public boolean isIndexedMode() {

        return indexedModeEnabled;
    }

    public boolean isGrammarRefinementEnabled() {

        return grammarRefinementEnabled;
    }

    public void setGrammarRefinementEnabled(boolean enabled) {

        grammarRefinementEnabled = enabled;
    }

    /**
     * @param enabled True if and only if the symbolic execution should perform garbage collection
     */
    public void setGarbageCollectionEnabled(boolean enabled) {

        this.garbageCollectionEnabled = enabled;
    }




    public boolean isRuleCollapsingEnabled() {

        return ruleCollapsingEnabled;
    }

    public void setRuleCollapsingEnabled(boolean enabled) {

        ruleCollapsingEnabled = enabled;
    }

    public void setVerifyCounterexamples(boolean verifyCounterexamples) {
        this.verifyCounterexamples = verifyCounterexamples;
    }

    public boolean isVerifyCounterexamples() {
        return verifyCounterexamples;
    }


    // ----------------------------------------------------------------------------------------------------------------



    public void setAdmissibleAbstractionEnabled(boolean admissibleAbstractionEnabled) {
        this.admissibleAbstractionEnabled = admissibleAbstractionEnabled;
    }


    public void setAdmissibleConstantsEnabled(boolean admissibleConstantsEnabled) {
        this.admissibleConstantsEnabled = admissibleConstantsEnabled;
    }


    public void setAdmissibleMarkingsEnabled(boolean admissibleMarkingsEnabled) {
        this.admissibleMarkingsEnabled = admissibleMarkingsEnabled;
    }


    public void setAdmissibleFullEnabled(boolean admissibleFullEnabled) {
        this.admissibleFullEnabled = admissibleFullEnabled;
    }


    public void setNoChainAbstractionEnabled(boolean noChainAbstractionEnabled) {
        this.noChainAbstractionEnabled = noChainAbstractionEnabled;
    }


    public void setNoRuleCollapsingEnabled(boolean noRuleCollapsingEnabled) {
        this.noRuleCollapsingEnabled = noRuleCollapsingEnabled;
    }

    public void setIndexedModeEnabled(boolean indexedModeEnabled) {
        this.indexedModeEnabled = indexedModeEnabled;
    }

    public boolean isCanonicalEnabled() {
        return canonicalEnabled;
    }

    public void setCanonicalEnabled(boolean canonicalEnabled) {
        this.canonicalEnabled = canonicalEnabled;
    }


    public void setMaxStateSpace(int maxStateSpace) {
        this.maxStateSpace = maxStateSpace;
    }


    public void setMaxHeap(int maxHeap) {
        this.maxHeap = maxHeap;
    }


    // ------------------------------------------------------------------------------------------------------------- //


    public int getMaxStateSpace() {
        return maxStateSpace;
    }

    public int getMaxHeap() {
        return maxHeap;
    }

    // ------------------------------------------------------------------------------------------------------------- //

    public boolean isNoRuleCollapsingEnabled() {
        return noRuleCollapsingEnabled;
    }

    public boolean isGarbageCollectionEnabled() {
        return garbageCollectionEnabled;
    }

    public boolean isNoChainAbstractionEnabled() {
        return noChainAbstractionEnabled;
    }

    public boolean isAdmissibleMarkingsEnabled() {
        return admissibleMarkingsEnabled;
    }

    public boolean isAdmissibleConstantsEnabled() {
        return admissibleConstantsEnabled;
    }

    public boolean isAdmissibleFullEnabled() {
        return admissibleFullEnabled;
    }

    public boolean isAdmissibleAbstractionEnabled() {
        return admissibleAbstractionEnabled;
    }

}
