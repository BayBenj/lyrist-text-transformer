package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintManager;
import utils.U;
import word2vec.W2vInterface;

import java.util.List;


public class ReplacementByAnalogyInfo extends LyristReplacementInfo {

    private String oldTheme;
    private String newTheme;

    public ReplacementByAnalogyInfo(List<WordConstraint> markingConstraints,
                                    List<WordConstraint> wordConstraints,
                                    W2vInterface w2v,
                                    double replacementFrequency,
                                    String oldTheme,
                                    String newTheme) {
        super(markingConstraints, wordConstraints, w2v, replacementFrequency);
        this.setOldTheme(oldTheme);
        this.setNewTheme(newTheme);
    }

    public static ReplacementByAnalogyInfo getNormal(String oldTheme, String newTheme) {
        return new ReplacementByAnalogyInfo(null,
                                            WordConstraintManager.getNormal(),
                                            U.getW2VInterface(),
                                            1,
                                            oldTheme,
                                            newTheme);
    }

    public String getOldTheme() {
        return oldTheme;
    }

    public void setOldTheme(String oldTheme) {
        this.oldTheme = oldTheme;
    }

    public String getNewTheme() {
        return newTheme;
    }

    public void setNewTheme(String newTheme) {
        this.newTheme = newTheme;
    }
}




/*
                       StringFilterEquation stringMarkingFilters,
                       WordFilterEquation wordMarkingFilters,
                       List<IndividualAction> individualActions,
                       StringFilterEquation stringFilters,
                       WordFilterEquation wordFilters,
                       W2vInterface w2v,
                       ReplacementIntention replacementIntention,
                       double replacementFrequency

 */




















































