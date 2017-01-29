package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintManager;
import rhyme.LineRhymeScheme;
import utils.U;
import word2vec.W2vCommander;

import java.util.Map;

public class RhymeReplacementInfo extends ReplacementByAnalogyInfo {

    private LineRhymeScheme rhymeScheme;

    public RhymeReplacementInfo(Map<Integer,
                                WordConstraint> markingConstraints,
                                Map<Integer, WordConstraint> wordConstraints,
                                W2vCommander w2v, double replacementFrequency,
                                String oldTheme,
                                String newTheme,
                                LineRhymeScheme rhymeScheme) {
        super(markingConstraints, wordConstraints, w2v, replacementFrequency, oldTheme, newTheme);
        this.rhymeScheme = rhymeScheme;
    }

    public LineRhymeScheme getRhymeScheme() {
        return rhymeScheme;
    }

    public void setRhymeScheme(LineRhymeScheme rhymeScheme) {
        this.rhymeScheme = rhymeScheme;
    }

    public static RhymeReplacementInfo getExample() {
        return new RhymeReplacementInfo(null,
                WordConstraintManager.getRhyme(),
                U.getW2vCommander(),
                1,
                "depression",
                "happiness",
                new LineRhymeScheme("A","B","A","B"));
    }

}
