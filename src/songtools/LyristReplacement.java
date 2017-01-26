package songtools;

import filters.RhymeSchemeFilter;
import filters.RhymeScoreFilter;
import filters.StringFilterEquation;
import filters.WordFilterEquation;
import intentions.IndividualAction;
import word2vec.W2vCommander;
import java.util.List;

public class LyristReplacement {

    private StringFilterEquation stringMarkingFilters;
    private WordFilterEquation wordMarkingFilters;
    private List<IndividualAction> individualActions;
    private StringFilterEquation stringFilters;
    private WordFilterEquation nonRhymeWordFilters;
    private RhymeScoreFilter rhymeWordFilter;
    private RhymeSchemeFilter rhymeSchemeFilter;
    private W2vCommander w2v = null;
    private double replacementFrequency;

    public LyristReplacement(StringFilterEquation stringMarkingFilters,
                             WordFilterEquation wordMarkingFilters,
                             List<IndividualAction> individualActions,
                             StringFilterEquation stringFilters,
                             WordFilterEquation nonRhymeWordFilters,
                             RhymeScoreFilter rhymeWordFilter,
                             W2vCommander w2v,
                             double replacementFrequency) {
        this.stringMarkingFilters = stringMarkingFilters;
        this.wordMarkingFilters = wordMarkingFilters;
        this.individualActions = individualActions;
        this.stringFilters = stringFilters;
        this.nonRhymeWordFilters = nonRhymeWordFilters;
        this.rhymeWordFilter = rhymeWordFilter;
        this.w2v = w2v;
        this.replacementFrequency = replacementFrequency;
    }

    public RhymeScoreFilter getRhymeWordFilter() {
        return rhymeWordFilter;
    }

    public void setRhymeWordFilter(RhymeScoreFilter rhymeWordFilter) {
        this.rhymeWordFilter = rhymeWordFilter;
    }

    public RhymeSchemeFilter getRhymeSchemeFilter() {
        return rhymeSchemeFilter;
    }

    public void setRhymeSchemeFilter(RhymeSchemeFilter rhymeSchemeFilter) {
        this.rhymeSchemeFilter = rhymeSchemeFilter;
    }

    public StringFilterEquation getStringMarkingFilters() {
        return stringMarkingFilters;
    }

    public void setStringMarkingFilters(StringFilterEquation stringMarkingFilters) {
        this.stringMarkingFilters = stringMarkingFilters;
    }

    public WordFilterEquation getWordMarkingFilters() {
        return wordMarkingFilters;
    }

    public void setWordMarkingFilters(WordFilterEquation wordMarkingFilters) {
        this.wordMarkingFilters = wordMarkingFilters;
    }

    public List<IndividualAction> getIndividualActions() {
        return individualActions;
    }

    public void setIndividualActions(List<IndividualAction> individualActions) {
        this.individualActions = individualActions;
    }

    public StringFilterEquation getStringFilters() {
        return stringFilters;
    }

    public void setStringFilters(StringFilterEquation stringFilters) {
        this.stringFilters = stringFilters;
    }

    public WordFilterEquation getNonRhymeWordFilters() {
        return nonRhymeWordFilters;
    }

    public void setNonRhymeWordFilters(WordFilterEquation nonRhymeWordFilters) {
        this.nonRhymeWordFilters = nonRhymeWordFilters;
    }

    public W2vCommander getW2v() {
        return w2v;
    }

    public void setW2v(W2vCommander w2v) {
        this.w2v = w2v;
    }

    public double getReplacementFrequency() {
        return replacementFrequency;
    }

    public void setReplacementFrequency(double replacementFrequency) {
        this.replacementFrequency = replacementFrequency;
    }
}

































































































