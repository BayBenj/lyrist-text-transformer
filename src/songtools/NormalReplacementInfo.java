package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintManager;
import utils.U;
import word2vec.W2vInterface;

import java.util.List;

public class NormalReplacementInfo extends ReplacementByAnalogyInfo {

    public NormalReplacementInfo(List<WordConstraint> markingConstraints, List<WordConstraint> wordConstraints, W2vInterface w2v, double replacementFrequency, String oldTheme, String newTheme) {
        super(markingConstraints, wordConstraints, w2v, replacementFrequency, oldTheme, newTheme);
    }

    public static NormalReplacementInfo getExample(String oldTheme, String newTheme) {
            return new NormalReplacementInfo(WordConstraintManager.getMarking(),
                                             WordConstraintManager.getNormal(),
                                             U.getW2VInterface(),
                                            1,
                                            oldTheme,
                                            newTheme);
    }

}
