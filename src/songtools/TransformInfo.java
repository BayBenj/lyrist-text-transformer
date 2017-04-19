package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintManager;
import utils.U;
import word2vec.W2vInterface;

import java.util.List;

public class TransformInfo extends TransformByAnalogyInfo {

    public TransformInfo(List<WordConstraint> markingConstraints, List<WordConstraint> wordConstraints, W2vInterface w2v, double replacementFrequency, String oldTheme, String newTheme) {
        super(markingConstraints, wordConstraints, w2v, replacementFrequency, oldTheme, newTheme);
    }

    public static TransformInfo getExample(String oldTheme, String newTheme) {
            return new TransformInfo(WordConstraintManager.getMarking(),
                                             WordConstraintManager.getNormal(),
                                             U.getW2VInterface(),
                                            1,
                                            oldTheme,
                                            newTheme);
    }

}
