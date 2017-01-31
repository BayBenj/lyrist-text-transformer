package songtools;

import constraints.BaseConstraint;
import constraints.WordConstraint;
import filters.ReturnType;
import word2vec.W2vCommander;

import java.util.*;

public abstract class LyristReplacementInfo {

    private List<WordConstraint> markingConstraints;
    private List<WordConstraint> wordConstraints;
    private W2vCommander w2v = null;
    private double replacementFrequency;

    public LyristReplacementInfo(List<WordConstraint> markingConstraints,
                                 List<WordConstraint> wordConstraints,
                                 W2vCommander w2v,
                                 double replacementFrequency) {
        this.setMarkingConstraints(markingConstraints);
        this.setWordConstraints(wordConstraints);
        this.setW2v(w2v);
        this.setReplacementFrequency(replacementFrequency);
    }

    public List<WordConstraint> getMarkingConstraints() {
        return markingConstraints;
    }

    public void setMarkingConstraints(List<WordConstraint> markingConstraints) {
        this.markingConstraints = markingConstraints;
    }

    public List<WordConstraint> getWordConstraints() {
        return wordConstraints;
    }

    public void setWordConstraints(List<WordConstraint> wordConstraints) {
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

    public List<WordConstraint> getBaseConstraints(String base) {
        List<WordConstraint> result = new ArrayList<>(wordConstraints);
        result.remove(2);
        Set<String> set = new HashSet<>();
        set.add(base);
        result.add(1, new BaseConstraint(set, ReturnType.MATCHES));//(must be the same base as the other new base that was already added)
        //result.get(1).enforce();
        return result;
    }

}


























































































