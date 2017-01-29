package songtools;

import constraints.WordConstraint;
import word2vec.W2vCommander;
import java.util.Map;

public abstract class LyristReplacementInfo {

    private Map<Integer, WordConstraint> markingConstraints;
    private Map<Integer, WordConstraint> wordConstraints;
    private W2vCommander w2v = null;
    private double replacementFrequency;

    public LyristReplacementInfo(Map<Integer, WordConstraint> markingConstraints,
                                 Map<Integer, WordConstraint> wordConstraints,
                                 W2vCommander w2v,
                                 double replacementFrequency) {
        this.setMarkingConstraints(markingConstraints);
        this.setWordConstraints(wordConstraints);
        this.setW2v(w2v);
        this.setReplacementFrequency(replacementFrequency);
    }

    public Map<Integer, WordConstraint> getMarkingConstraints() {
        return markingConstraints;
    }

    public void setMarkingConstraints(Map<Integer, WordConstraint> markingConstraints) {
        this.markingConstraints = markingConstraints;
    }

    public Map<Integer, WordConstraint> getWordConstraints() {
        return wordConstraints;
    }

    public void setWordConstraints(Map<Integer, WordConstraint> wordConstraints) {
        this.wordConstraints = wordConstraints;
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





























































































