package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintManager;
import utils.U;
import word2vec.W2vCommander;

import java.util.List;
import java.util.Map;


public class ReplacementByAnalogyInfo extends LyristReplacementInfo {

    private String oldTheme;
    private String newTheme;

    public ReplacementByAnalogyInfo(List<WordConstraint> markingConstraints,
                                    List<WordConstraint> wordConstraints,
                                    W2vCommander w2v,
                                    double replacementFrequency,
                                    String oldTheme,
                                    String newTheme) {
        super(markingConstraints, wordConstraints, w2v, replacementFrequency);
        this.setOldTheme(oldTheme);
        this.setNewTheme(newTheme);
    }

    public static ReplacementByAnalogyInfo getNormal() {
        return new ReplacementByAnalogyInfo(null,
                                            WordConstraintManager.getNormal(),
                                            U.getW2vCommander(),
                                            1,
                                            "depression",
                                            "happiness");
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
                       W2vCommander w2v,
                       ReplacementIntention replacementIntention,
                       double replacementFrequency

 */




















































