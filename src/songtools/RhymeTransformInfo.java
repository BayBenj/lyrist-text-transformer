package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintMaker;
import rhyme.LineRhymeScheme;
import utils.U;
import word2vec.W2vInterface;

import java.util.List;

public class RhymeTransformInfo extends TransformByAnalogyInfo {

    private LineRhymeScheme rhymeScheme;
    private List<WordConstraint> rhymeConstraints;

    public RhymeTransformInfo(List<WordConstraint> markingConstraints,
                              List<WordConstraint> normalConstraints,
                              List<WordConstraint> rhymeConstraints,
                              W2vInterface w2v, double replacementFrequency,
                              String oldTheme,
                              String newTheme,
                              LineRhymeScheme rhymeScheme) {
        super(markingConstraints, normalConstraints, w2v, replacementFrequency, oldTheme, newTheme);
        this.rhymeScheme = rhymeScheme;
        this.rhymeConstraints = rhymeConstraints;
    }

    public LineRhymeScheme getRhymeScheme() {
        return rhymeScheme;
    }

    public void setRhymeScheme(LineRhymeScheme rhymeScheme) {
        this.rhymeScheme = rhymeScheme;
    }

    public List<WordConstraint> getRhymeConstraints() {
        return rhymeConstraints;
    }

    public void setRhymeConstraints(List<WordConstraint> rhymeConstraints) {
        this.rhymeConstraints = rhymeConstraints;
    }

    public static RhymeTransformInfo getExample(String oldTheme, String newTheme, LineRhymeScheme rhymeScheme) {
        return new RhymeTransformInfo(
                WordConstraintMaker.getMarking(),
                WordConstraintMaker.getNormal(),
                WordConstraintMaker.getRhyme(),
                U.getW2VInterface(),
                1,
                oldTheme,
                newTheme,
                rhymeScheme);
    }

}
