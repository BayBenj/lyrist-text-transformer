package songtools;

import constraints.WordConstraint;
import rhyme.RhymeScheme;
import word2vec.W2vCommander;
import java.util.Map;

public class LyristReplacementInfo {

    private Map<Integer, WordConstraint> markingConstraints;
    private Map<Integer, WordConstraint> wordConstraints;
    private W2vCommander w2v = null;
    private double replacementFrequency;
    private RhymeScheme rhymeScheme;

    public LyristReplacementInfo(Map<Integer, WordConstraint> markingConstraints,
                                 Map<Integer, WordConstraint> wordConstraints,
                                 W2vCommander w2v,
                                 double replacementFrequency) {
        this.setMarkingConstraints(markingConstraints);
        this.setWordConstraints(wordConstraints);
        this.setW2v(w2v);
        this.setReplacementFrequency(replacementFrequency);
    }

    public LyristReplacementInfo(Map<Integer, WordConstraint> markingConstraints,
                                 Map<Integer, WordConstraint> wordConstraints,
                                 W2vCommander w2v,
                                 double replacementFrequency,
                                 RhymeScheme rhymeScheme) {
        this.setMarkingConstraints(markingConstraints);
        this.setWordConstraints(wordConstraints);
        this.setW2v(w2v);
        this.setReplacementFrequency(replacementFrequency);
        this.setRhymeScheme(rhymeScheme);
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

    public RhymeScheme getRhymeScheme() {
        return rhymeScheme;
    }

    public void setRhymeScheme(RhymeScheme rhymeScheme) {
        this.rhymeScheme = rhymeScheme;
    }
}




























































































