package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintManager;
import rhyme.LineRhymeScheme;
import utils.U;
import word2vec.W2vInterface;

import java.util.List;

public class RhymeReplacementInfo extends ReplacementByAnalogyInfo {

    private LineRhymeScheme rhymeScheme;
    private List<WordConstraint> rhymeConstraints;

    public RhymeReplacementInfo(List<WordConstraint> markingConstraints,
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

    public static RhymeReplacementInfo getExample(String oldTheme, String newTheme, LineRhymeScheme rhymeScheme) {
        return new RhymeReplacementInfo(
                WordConstraintManager.getMarking(),
                WordConstraintManager.getNormal(),
                WordConstraintManager.getRhyme(),
                U.getW2VInterface(),
                1,
                oldTheme,
                newTheme,
                rhymeScheme);
    }

}
