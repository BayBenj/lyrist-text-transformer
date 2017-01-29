package songtools;

import constraints.WordConstraint;
import constraints.WordConstraintManager;
import rhyme.LineRhymeScheme;
import rhyme.RhymeScheme;
import utils.U;
import word2vec.W2vCommander;
import java.util.Map;


public class ReplacementByAnalogyInfo extends LyristReplacementInfo {

    private String oldTheme;
    private String newTheme;

    public ReplacementByAnalogyInfo(Map<Integer, WordConstraint> markingConstraints,
                                    Map<Integer, WordConstraint> wordConstraints,
                                    W2vCommander w2v,
                                    double replacementFrequency,
                                    String oldTheme,
                                    String newTheme) {
        super(markingConstraints, wordConstraints, w2v, replacementFrequency);
        this.setOldTheme(oldTheme);
        this.setNewTheme(newTheme);
    }

    public ReplacementByAnalogyInfo(Map<Integer, WordConstraint> markingConstraints,
                                    Map<Integer, WordConstraint> wordConstraints,
                                    W2vCommander w2v,
                                    double replacementFrequency,
                                    RhymeScheme rhymeScheme,
                                    String oldTheme,
                                    String newTheme) {
        super(markingConstraints, wordConstraints, w2v, replacementFrequency, rhymeScheme);
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

    public static ReplacementByAnalogyInfo getRhyme() {
        return new ReplacementByAnalogyInfo(null,
                                            WordConstraintManager.getRhyme(),
                                            U.getW2vCommander(),
                                            1,
                                            new LineRhymeScheme("A","B","A","B"),
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


















































