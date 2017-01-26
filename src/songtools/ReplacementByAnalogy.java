package songtools;

import filters.StringFilterEquation;
import filters.WordFilterEquation;
import intentions.IndividualAction;
import utils.Pair;
import word2vec.W2vCommander;

import java.util.List;


public class ReplacementByAnalogy extends LyristReplacement {

    private Pair<String,String> oldAndNewThemes;

    public ReplacementByAnalogy(StringFilterEquation stringMarkingFilters,
                                WordFilterEquation wordMarkingFilters,
                                List<IndividualAction> individualActions,
                                StringFilterEquation stringFilters,
                                WordFilterEquation nonRhymeWordFilters,
                                WordFilterEquation rhymeWordFilters,
                                W2vCommander w2v,
                                double replacementFrequency,
                                Pair<String, String> oldAndNewThemes) {
        super(stringMarkingFilters, wordMarkingFilters, individualActions, stringFilters, nonRhymeWordFilters, rhymeWordFilters, w2v, replacementFrequency);
        this.setOldAndNewThemes(oldAndNewThemes);
    }

    public Pair<String, String> getOldAndNewThemes() {
        return oldAndNewThemes;
    }

    public void setOldAndNewThemes(Pair<String, String> oldAndNewThemes) {
        this.oldAndNewThemes = oldAndNewThemes;
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

























































