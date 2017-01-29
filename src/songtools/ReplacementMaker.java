//package songtools;
//
//import filters.FilterEquation;
//import filters.FilterManager;
//import intentions.IndividualAction;
//import intentions.MarkingIntention;
//import intentions.ReplacementIntention;
//import utils.Pair;
//import utils.U;
//import word2vec.W2vCommander;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class ReplacementMaker {
//
//    public static ReplacementByAnalogyInfo getNormalReplacement(String oldTheme, String newTheme) {
//        FilterEquation whatToReplace = FilterManager.wEq(FilterManager.getTaggingSafetyFilters());
//        double replacementFrequency = 1;
//        List<MarkingIntention> markingIntentions = new ArrayList<>();
//        List<IndividualAction> individualActions = new ArrayList<>();
//
//        //mark and filterByMultiple by constraints
//        markingIntentions.add(MarkingIntention.MARK_ALL_BY_CONSTRAINTS);//need sentences and marking constraints
//        individualActions.add(IndividualAction.W2V_ANALOGY);//need old and new theme, string words
//        individualActions.add(IndividualAction.STRING_FILTERS);//need string filters and strings to filterByMultiple
//        individualActions.add(IndividualAction.STRINGS_TO_WORDS);//need cosined strings
//        individualActions.add(IndividualAction.WORD_FILTERS);//need word filters and words to filterByMultiple
//        individualActions.add(IndividualAction.CHOOSE_BEST_SUGGESTION);//need constraint prioritization
//
//        //replace old with new
//        ReplacementIntention replacementIntention = ReplacementIntention.REPLACE_MARKED_WITH_NEW;//need old marked filterWords, new filterWords
//
//        FilterEquation suggestionFilters = FilterManager.wEq(FilterManager.getMirrorPosNeFilters());
//        W2vCommander w2v = U.getW2vCommander();
//        return new ReplacementByAnalogyInfo(whatToReplace, replacementFrequency, operationalIntentions, suggestionFilters, w2v, new Pair<String,String>(oldTheme, newTheme));
//    /*
//    Mark                FilterOperation
//    w2v analogy         Source
//    String filters      FilterOperation
//    Strings to Words    ?
//    Word filters        FilterOperation
//     */
//
//    }
//
//    public static ReplacementByAnalogyInfo getRhymeReplacement(String oldTheme, String newTheme) {
//        FilterEquation whatToReplace = FilterManager.wEq(FilterManager.getTaggingSafetyFilters());
//        double replacementFrequency = 1;
//        List<MarkingIntention> markingIntentions = new ArrayList<>();
//        List<IndividualAction> individualActions = new ArrayList<>();
//
//        //find normal suggestions
//        markingIntentions.add(MarkingIntention.MARK_ALL_BY_CONSTRAINTS);//need sentences and marking constraints
//        individualActions.add(IndividualAction.W2V_ANALOGY);//need old and new theme, string words
//        individualActions.add(IndividualAction.STRING_FILTERS);//need string filters and strings to filterByMultiple
//        individualActions.add(IndividualAction.ADD_CMU_RHYMES);
//        individualActions.add(IndividualAction.STRINGS_TO_WORDS);//need cosined strings
//        individualActions.add(IndividualAction.WORD_FILTERS);//need word filters and words to filterByMultiple
//
//        //then get suggestions for words that rhyme with model word
//        markingIntentions.add(MarkingIntention.MARK_LAST_WORD_IN_LINES_BY_RHYME_SCHEME);//need sentences, song, and rhyme scheme
//        individualActions.add(IndividualAction.PICK_MODEL_RHYME_WORDS);
//        individualActions.add(IndividualAction.WORD_FILTERS);//need word filters specific to rhyming w/ model word and words to filterByMultiple
//        individualActions.add(IndividualAction.CHOOSE_BEST_SUGGESTION);//need constraint prioritization
//
//        //replace old with new
//        ReplacementIntention replacementIntention = ReplacementIntention.REPLACE_MARKED_WITH_NEW;//need old marked filterWords, new filterWords
//
//        FilterEquation suggestionFilters = FilterManager.wEq(FilterManager.getMirrorPosNeFilters());
//        W2vCommander w2v = U.getW2vCommander();
//        return new ReplacementByAnalogyInfo(whatToReplace, replacementFrequency, replacementSources, suggestionFilters, w2v, new Pair<String,String>(oldTheme, newTheme));
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
