package songtools;

import elements.Word;
import filters.StringFilterEquation;
import filters.WordFilterEquation;
import intentions.IndividualAction;
import intentions.ReplacementIntention;
import word2vec.W2vCommander;
import java.util.List;
import java.util.Set;

public class LyristOperationOld {

    private Set<Word> wordsToFilter;

    private StringFilterEquation stringMarkingFilters;
    private WordFilterEquation wordMarkingFilters;
    private List<IndividualAction> individualActions;
    private StringFilterEquation stringFilters;
    private WordFilterEquation wordFilters;
    private W2vCommander w2v = null;
    ReplacementIntention replacementIntention;
    private double replacementFrequency;

    public LyristOperationOld(StringFilterEquation stringMarkingFilters,
                              WordFilterEquation wordMarkingFilters,
                              List<IndividualAction> individualActions,
                              StringFilterEquation stringFilters,
                              WordFilterEquation wordFilters,
                              W2vCommander w2v,
                              ReplacementIntention replacementIntention,
                              double replacementFrequency) {
        this.stringMarkingFilters = stringMarkingFilters;
        this.wordMarkingFilters = wordMarkingFilters;
        this.individualActions = individualActions;
        this.stringFilters = stringFilters;
        this.wordFilters = wordFilters;
        this.w2v = w2v;
        this.replacementIntention = replacementIntention;
        this.replacementFrequency = replacementFrequency;
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

    public WordFilterEquation getWordFilters() {
        return wordFilters;
    }

    public void setWordFilters(WordFilterEquation wordFilters) {
        this.wordFilters = wordFilters;
    }

    public W2vCommander getW2v() {
        return w2v;
    }

    public void setW2v(W2vCommander w2v) {
        this.w2v = w2v;
    }

    public ReplacementIntention getReplacementIntention() {
        return replacementIntention;
    }

    public void setReplacementIntention(ReplacementIntention replacementIntention) {
        this.replacementIntention = replacementIntention;
    }

    public double getReplacementFrequency() {
        return replacementFrequency;
    }

    public void setReplacementFrequency(double replacementFrequency) {
        this.replacementFrequency = replacementFrequency;
    }
}











































































































