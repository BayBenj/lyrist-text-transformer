package songtools;

import constraints.BaseConstraint;
import constraints.WordConstraint;
import filters.ReturnType;
import word2vec.W2vInterface;

import java.util.*;

public abstract class LyristReplacementInfo {

    private List<WordConstraint> markingConstraints;
    private List<WordConstraint> normalConstraints;
    private W2vInterface w2v = null;
    private double replacementFrequency;

    public LyristReplacementInfo(List<WordConstraint> markingConstraints,
                                 List<WordConstraint> normalConstraints,
                                 W2vInterface w2v,
                                 double replacementFrequency) {
        this.setMarkingConstraints(markingConstraints);
        this.setNormalConstraints(normalConstraints);
        this.setW2v(w2v);
        this.setReplacementFrequency(replacementFrequency);
    }

    public List<WordConstraint> getMarkingConstraints() {
        return markingConstraints;
    }

    public void setMarkingConstraints(List<WordConstraint> markingConstraints) {
        this.markingConstraints = markingConstraints;
    }

    public List<WordConstraint> getNormalConstraints() {
        return normalConstraints;
    }

    public void setNormalConstraints(List<WordConstraint> normalConstraints) {
        this.normalConstraints = normalConstraints;
    }

    public W2vInterface getW2v() {
        return w2v;
    }

    public void setW2v(W2vInterface w2v) {
        this.w2v = w2v;
    }

    public double getReplacementFrequency() {
        return replacementFrequency;
    }

    public void setReplacementFrequency(double replacementFrequency) {
        this.replacementFrequency = replacementFrequency;
    }

    public List<WordConstraint> getBaseConstraints(String base) {
        List<WordConstraint> result = new ArrayList<>(normalConstraints);
        result.remove(2);//remove the normal base constraint, change it and put it back in at the same position
        Set<String> set = new HashSet<>();
        set.add(base);
        result.add(2, new BaseConstraint(set, ReturnType.MATCHES));//(must be the same base as the other new base that was already added)
        //result.get(1).enforce();
        return result;
    }

}


























































































