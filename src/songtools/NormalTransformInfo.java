package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintManager;
import utils.U;
import word2vec.W2vInterface;

import java.util.List;

public class NormalTransformInfo extends TransformByAnalogyInfo {

    public NormalTransformInfo(List<WordConstraint> markingConstraints, List<WordConstraint> wordConstraints, W2vInterface w2v, double replacementFrequency, String oldTheme, String newTheme) {
        super(markingConstraints, wordConstraints, w2v, replacementFrequency, oldTheme, newTheme);
    }

    public static NormalTransformInfo getExample(String oldTheme, String newTheme) {
            return new NormalTransformInfo(WordConstraintManager.getMarking(),
                                             WordConstraintManager.getNormal(),
                                             U.getW2VInterface(),
                                            1,
                                            oldTheme,
                                            newTheme);
    }

}
