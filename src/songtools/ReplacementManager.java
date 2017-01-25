package songtools;

import constraints.Constraint;
import constraints.ConstraintPrioritizer;
import elements.Song;
import filters.*;
import intentions.IndividualAction;
import intentions.MarkingIntention;
import intentions.ReplacementIntention;
import intentions.SourceEnum;
import main.ProgramArgs;
import rhyme.*;
import elements.Punctuation;
import elements.Sentence;
import elements.Word;
import stanford.StanfordNlp;
import utils.U;
import word2vec.W2vCommander;
import java.util.*;

import static intentions.IndividualAction.PICK_MODEL_RHYME_WORDS;

public abstract class ReplacementManager {
//TODO eventually make children of this class that represent specifc W2v operation sequences, like [sentiment, similar, analogy, similar]
//TODO or [analogy, similar] or [similar], etc. Maybe, unless there's a smarter way.
// TODO: This class should simply execute a sequence of W2vOperations, interface with Filters, and return WordReplacements or new Words.

    public static void normalReplace() {
        /*
        markingIntentions.add(MarkingIntention.MARK_ALL_BY_CONSTRAINTS);//need sentences and marking constraints
        individualActions.add(IndividualAction.W2V_ANALOGY);//need old and new theme, string words
        individualActions.add(IndividualAction.STRING_FILTERS);//need string filters and strings to filter
        individualActions.add(IndividualAction.STRINGS_TO_WORDS);//need cosined strings
        individualActions.add(IndividualAction.WORD_FILTERS);//need word filters and words to filter
        individualActions.add(IndividualAction.CHOOSE_BEST_SUGGESTION);//need constraint prioritization
         */
    }

    public static void rhymeReplace() {
        /*
        //find normal suggestions
        markingIntentions.add(MarkingIntention.MARK_ALL_BY_CONSTRAINTS);//need sentences and marking constraints
        individualActions.add(IndividualAction.W2V_ANALOGY);//need old and new theme, string words
        individualActions.add(IndividualAction.STRING_FILTERS);//need string filters and strings to filter
        individualActions.add(IndividualAction.ADD_CMU_RHYMES);
        individualActions.add(IndividualAction.STRINGS_TO_WORDS);//need cosined strings
        individualActions.add(IndividualAction.WORD_FILTERS);//need word filters and words to filter

        //then get suggestions for words that rhyme with model word
        markingIntentions.add(MarkingIntention.MARK_LAST_WORD_IN_LINES_BY_RHYME_SCHEME);//need sentences, song, and rhyme scheme
        individualActions.add(IndividualAction.PICK_MODEL_RHYME_WORDS);
        individualActions.add(IndividualAction.WORD_FILTERS);//need word filters specific to rhyming w/ model word and words to filter
        individualActions.add(IndividualAction.CHOOSE_BEST_SUGGESTION);//need constraint prioritization

        //replace old with new
        ReplacementIntention replacementIntention = ReplacementIntention.REPLACE_MARKED_WITH_NEW;//need old marked words, new words

         */
    }

    public static void replace(SongWrapper songWrapper, LyristOperationOld lyristOperationOld) {
        //Loop for each lyristOperationOld job:
        //  mark
        //  get suggestions for each mark
        //  choose one suggestion for each mark
        //return the new song

        List<Word> markedWords = marking(lyristOperationOld.getMarkingIntention());
        replacement(lyristOperationOld.getReplacementIntention());
        for () {
            for (IndividualAction intention : lyristOperationOld.getIndividualActions()) {
                insideReplacement(intention);
            }
        }
    }

    public static Map<Integer, Map<Double, String>> getWordSuggestions(SongWrapper songWrapper, LyristOperationOld lyristOperationOld) {
        Map<Double, String> strings = null;
        Map<Double, Word> words = null;
        int nOfSuggestions;
        Word oldWord = null;
        Sentence sentence = null;
        int oldWordIndex;
        List<Word> markedWords = null;
        Map<Rhyme,Set<Word>> wordsToRhyme = null;
        TreeMap<Integer, Constraint> nonRhymeConstraints = null;
        List<Set<Word>> suggestionsForRhymeClass = null;
        W2vCommander w2v = lyristOperationOld.getW2v();
        Word bestWordForRhymeClass = null;
        for (SourceEnum operationalIntention : lyristOperationOld.getOperationalIntentions()) {
            switch(operationalIntention) {
                case MARK:
                    if (lyristOperationOld.getMarkingFilters().has(RhymeSchemeFilter.class))
                        wordsToRhyme = markRhyme(((RhymeSchemeFilter) lyristOperationOld.getMarkingFilters().getFirst(RhymeSchemeFilter.class)), songWrapper.getSong());
                    else
                        markedWords = marking(songWrapper.getSong(), lyristOperationOld.getMarkingFilters());
                    break;

                case REPLACE_ALL_MARKED:
                    replaceAllMarked();
                    break;

                case RHYME_ALL_MARKED:
                    rhymeAllMarked();
                    break;

                case REPLACE_RND_MARKED:
                    replaceRndMarked();
                    break;

                case W2V_ANALOGY:
                    ReplacementByAnalogy rba = (ReplacementByAnalogy) lyristOperationOld;
                    strings = w2vAnalogy(w2v, rba.getOldAndNewThemes().getFirst(), rba.getOldAndNewThemes().getSecond(), oldWord.toString().toLowerCase(), nOfSuggestions);
                    break;

                case STRING_FILTERS:
                    strings = stringFilters(strings, lyristOperationOld.getStringFilters());
                    break;

                case STRINGS_TO_WORDS:
                    words = stringsToWords(strings, sentence, oldWord, oldWordIndex);
                    break;

                case WORD_FILTERS:
                    words = wordfilters(words, oldWord);
                    break;

                case PICK_MODEL_RHYME_WORDS:
                    bestWordForRhymeClass = pickBestRhymeWord(suggestionsForRhymeClass, nonRhymeConstraints, .75);
                    break;

                case CMU_RHYMES:
                    strings = cmuRhymes(oldWord.getSpelling().toUpperCase(), strings);
                    break;
            }
        }
    }

    private static void marking(MarkingIntention intention) {
        switch (intention) {
            case MARK_ALL_BY_CONSTRAINTS:
                markWithConstraints();
                break;
            case MARK_LAST_WORD_IN_LINES_BY_RHYME_SCHEME:
                markRhyme();
                break;
        }
    }

    private static void replacement(ReplacementIntention intention) {
        switch (intention) {
            case REPLACE_ALL_MARKED:
                normalReplace(1);
                break;
            case REPLACE_RND_MARKED:
                normalReplace(1);
                break;
            case RHYME_ALL_MARKED:
                normalReplace(1);
                rhymeReplace();
                break;
        }
    }

    private static void insideReplacement(IndividualAction intention) {
        switch (intention) {
            case W2V_ANALOGY:

                break;
            case STRING_FILTERS:

                break;
            case STRINGS_TO_WORDS:

                break;
            case WORD_FILTERS:

                break;
            case ADD_CMU_RHYMES:

                break;
            case PICK_MODEL_RHYME_WORDS:

                break;
        }

    }

    private static List<Word> markWithConstraints(Song songToMark, FilterEquation markingFilters) {
        List<Word> allMarkableWordsList = new ArrayList<>(songToMark.getAllWords());
        if (markingFilters instanceof WordFilterEquation) {
            WordFilterEquation wordsNotToMark = (WordFilterEquation) markingFilters;
            allMarkableWordsList.retainAll(wordsNotToMark.removeMatches(new HashSet(songToMark.getAllWords())));
        }
        return allMarkableWordsList;
    }

    private static Map<Rhyme,Set<Word>> markRhyme(RhymeSchemeFilter filter, Song song) {
        return filter.doFilter(song);
    }

    private static void replaceAllMarked(List<Word> markedWords) {
        //get suggestions for each marked word
        //choose one suggestion as the replacement for each marked word
    }

    private static void rhymeAllMarked(RhymesAndTheirWordSuggestions wordsToRhyme, TreeMap<Integer,Constraint> nonRhymeConstraints, double rhymeThreshold) {
        //get rhyme models for each rhyme class
        List<Word> rhymeModels = getRhymeModelWords(wordsToRhyme, nonRhymeConstraints, rhymeThreshold);

        //get word replacement suggestions using rhyme models
        constrainSuggestions(rhymeModels, wordsToRhyme);
    }

    private static List<Word> getRhymeModelWords(RhymesAndTheirWordSuggestions wordsToRhyme, TreeMap<Integer,Constraint> nonRhymeConstraints, double rhymeThreshold) {
        //get rhyme models for each rhyme class
        List<Word> rhymeModels = new ArrayList<>();
        for (Map.Entry<Rhyme, Map<Integer,Set<Word>>> rhymeClass : wordsToRhyme.entrySet()) {
            List<Set<Word>> wordsPerRhymeInstance = new ArrayList<>();
            for (Map.Entry<Integer,Set<Word>> rhymeInstance : rhymeClass.getValue().entrySet())
                wordsPerRhymeInstance.add(rhymeInstance.getValue());
            Word rhymeModel = chooseBestWordForRhyming(wordsPerRhymeInstance, nonRhymeConstraints, rhymeThreshold);
            rhymeModels.add(rhymeModel);
        }
        return rhymeModels;
    }

    private static Map<Integer, Map<Double, String>> constrainSuggestions(List<Word> rhymeModels, RhymesAndTheirWordSuggestions suggestionsToFilter) {


    }

    private static void replaceRndMarked() {

    }

    private static Map<Double, String> stringFilters(Map<Double, String> stringSuggestionMap, StringFilterEquation stringFilterEquation) {
        stringSuggestionMap.values().retainAll(stringFilterEquation.removeMatches(new HashSet<>(stringSuggestionMap.values())));
        U.testPrintln("After string filtration: " + stringSuggestionMap.size() + " valid suggestions");
        return stringSuggestionMap;
    }

    private static Map<Double, Word> stringsToWords(Map<Double, String> stringSuggestionMap, Sentence sentence, Word oldWord, int oldWordIndex) {
        return SuggestionHandler.stringSuggestionsToWordSuggestions(stringSuggestionMap, sentence, oldWord, oldWordIndex);
    }

    private static Map<Double, Word> wordfilters(Map<Double, Word> wordSuggestionMap, Word oldWord) {
        WordFilterEquation wordFilterEquation = FilterManager.wEq(FilterManager.getMirrorPosNeFilters(oldWord));
        wordSuggestionMap.values().retainAll(wordFilterEquation.removeMatches(new HashSet<>(wordSuggestionMap.values())));//TODO BREAKS ON "It's", have Words know whether they are a contraction, and if so, what contraction they are. Allow Words to return their expansion of 2+ Words.
        U.testPrintln("After Word filtration: " + wordSuggestionMap.size() + " valid suggestions");
        return wordSuggestionMap;
    }

    private static Word pickBestRhymeWord(List<Set<Word>> suggestionSets, TreeMap<Integer, Constraint> nonRhymeConstraints, double rhymeThreshold) {
        //returns the model for rhyme class A, that all other words of rhyme class A will have to rhyme with
        //if rhyme priority > other word selection priorities

        //pick 1 suggestion that rhymes with the most / best other A words
        Word bestRhymingWord = chooseBestWordForRhyming(suggestionSets, nonRhymeConstraints, rhymeThreshold);

        //priority loop weakens constraints each time there is no result. This weakens other constraints at the price of optimizing rhyme.
        while (bestRhymingWord == null) {
            nonRhymeConstraints = ConstraintPrioritizer.weakenOrRemoveLastConstraint(nonRhymeConstraints);
            bestRhymingWord = chooseBestWordForRhyming(suggestionSets, nonRhymeConstraints, rhymeThreshold);
        }
        return bestRhymingWord;
    }


    private static Word chooseBestWordForRhyming(List<Set<Word>> suggestionSets, TreeMap<Integer, Constraint> nonRhymeConstraints, double rhymeThreshold) {
        //Chooses the best word in its rhyme scheme (best = most rhymeable)
        //Attempts to optimize all constraints in choice of rhyming word

        //input all suggestions for rhyme A words
        Word bestRhymer = null;
        int mostRhymes = Integer.MIN_VALUE;
        Set<Word> bestNonRhymeWords = new HashSet<>();

        //Pick the non-rhyme best word from each set
        for (Set<Word> suggestionSet : suggestionSets)
            bestNonRhymeWords.add(SuggestionHandler.pickReplacementWord(nonRhymeConstraints, suggestionSet));

        //Of those words, find the word with the most rhymes
        int wordNum = 0;
        for (Word candidateBestWord : bestNonRhymeWords) {
            //Get all words from other suggestion sets, NOT including suggestionSet of chosen word
            Set<Word> allWordsFromOtherSets = new HashSet<>();
            for (int setNum = 0; setNum < suggestionSets.size(); setNum++)
                if (setNum != wordNum)
                    allWordsFromOtherSets.addAll(suggestionSets.get(wordNum));

            //See how many of those words rhyme with the candidate bestRhymer TODO later score according to whether the set's best Words rhyme?
            int nRhymes = getNRhymingWords(candidateBestWord, suggestionSets.get(wordNum), rhymeThreshold);
            if (nRhymes > mostRhymes) {
                mostRhymes = nRhymes;
                bestRhymer = candidateBestWord;
            }
            wordNum++;
        }
        return bestRhymer;
    }

    private static int scoreRhyme(Rhyme rhyme, Word word) {
        //TODO: implement this somewhere
        return -1;
    }

    private static int scoreRhyme(Word word1, Word word2) {
        //TODO: implement this somewhere
        return -1;
    }

    private static int getNRhymingWords(Word rhyme, Set<Word> words, double rhymeScoreThreshold) {
        int n = 0;
        for (Word word : words)
            if (scoreRhyme(rhyme, word) >= rhymeScoreThreshold)
                n++;

        return n;
    }

    public static WordReplacements getWordSuggestions(Set<Word> wordsToReplace,
                                                      Map<Rhyme,Set<Word>> rhymeWordsToReplace,
                                                      List<Sentence> sentences,
                                                      StringFilterEquation stringFilterEquation,
                                                      int nOfSuggestions,
                                                      W2vCommander w2v,
                                                      String oldTheme,
                                                      String newTheme,
                                                      Set<Word> wordsToRhyme
                                                      //,WordFilterEquation wordFilterEquation
                                            ) {
        //TODO > make suggestion tagging use the original template sentence with the new word in the old word's place.
        //TODO > if the above technique lets too many wrong words through, change it.

        Set<Word> allRhymeWords = new HashSet<>();
        for (Set<Word> set : rhymeWordsToReplace.values())
            allRhymeWords.addAll(set);

        U.testPrintln("Entering getWordSuggestions");
        WordReplacements wordReplacements = new WordReplacements();
        WordReplacements rhymeWordReplacements = new WordReplacements();
        for(Sentence sentence : sentences) {
            int oldWordIndex = 0;
            for(Word oldWord : sentence) {
                if (oldWord.getClass() != Punctuation.class) {
                    // Replace words that are marked for replacement
                    if (    wordsToReplace.contains(oldWord) &&
                            !wordReplacements.containsKey(oldWord)) {
                        Word chosenWord = this.getReplacementWordSuggestions(
                                wordReplacements,
                                sentence,
                                oldWordIndex,
                                stringFilterEquation,
                                nOfSuggestions,
                                w2v,
                                oldTheme,
                                newTheme,
                                oldWord);
                        //Assign the word replacement
                        wordReplacements.put(oldWord, chosenWord);
                    }
                    // Replace words that are on the rhyme scheme
                    if (allRhymeWords.contains(oldWord)) {
                        Word chosenWord = this.getReplacementRhymingWordSuggestions(
                                wordReplacements,
                                sentence,
                                oldWordIndex,
                                stringFilterEquation,
                                nOfSuggestions,
                                w2v,
                                oldTheme,
                                newTheme,
                                oldWord);
                        //Assign the rhyme word replacement
                        rhymeWordReplacements.put(oldWord, chosenWord);
                    }
                }
                oldWordIndex++;
            }
        }
        return wordReplacements;
    }

    public static Word getReplacementWordSuggestions(WordReplacements wordReplacements,
                                                     Sentence sentence,
                                                     int oldWordIndex,
                                                     StringFilterEquation stringFilterEquation,
                                                     int nOfSuggestions,
                                                     W2vCommander w2v,
                                                     String oldTheme,
                                                     String newTheme,
                                                     Word oldWord) {
        //Perform word2vec operation
        Map<Double, String> stringSuggestionMap = w2v.findAnalogy(
                oldTheme.toLowerCase(),
                newTheme.toLowerCase(),
                oldWord.toString().toLowerCase(),
                nOfSuggestions);
        U.testPrintln("After word2vec operation: " + stringSuggestionMap.size() + " suggestions");

        //Use string filters
        stringSuggestionMap.values().retainAll(stringFilterEquation.removeMatches(new HashSet<>(stringSuggestionMap.values())));
        U.testPrintln("After string filtration: " + stringSuggestionMap.size() + " valid suggestions");

        //w2v suggestions -> words
        Map<Double, Word> wordSuggestionMap = SuggestionHandler.stringSuggestionsToWordSuggestions(stringSuggestionMap, sentence, oldWord, oldWordIndex);

        //Use Word filters
        WordFilterEquation wordFilterEquation = FilterManager.wEq(FilterManager.getMirrorPosNeFilters(oldWord));
        wordSuggestionMap.values().retainAll(wordFilterEquation.removeMatches(new HashSet<>(wordSuggestionMap.values())));//TODO BREAKS ON "It's", have Words know whether they are a contraction, and if so, what contraction they are. Allow Words to return their expansion of 2+ Words.
//                    FiltrationResults filtrationResults = stringFilters.filterWordSuggestionsWithModel(wordSuggestions, oldWord);
//                    SimilarW2vResults w2vResults = new SimilarW2vResults();
//                    w2vResults.setFiltrationResults(filtrationResults);
        U.testPrintln("After Word filtration: " + wordSuggestionMap.size() + " valid suggestions");

        //Put suggestions into sorted TreeMap
        TreeMap<Double, Word> finalWordSuggestions = new TreeMap<>(wordSuggestionMap);

        //if testing, print word2vec's filtered suggestions
        if (ProgramArgs.isTesting()) {
            int i = 0;
            U.testPrintln("Post-filtration suggestions for " + oldWord.toString() + ":");
            for (Map.Entry<Double,Word> entry : finalWordSuggestions.entrySet()) {
                if (i > this.nSuggestionsToPrint)
                    break;
                U.testPrint("\t" + entry.getValue().toString());
                i++;
            }
        }

        return SuggestionHandler.pickReplacementWord(finalWordSuggestions, wordReplacements, oldWord);
    }

    public static Word getReplacementRhymingWordSuggestions(WordReplacements wordReplacements,
                                                            Sentence sentence,
                                                            int oldWordIndex,
                                                            StringFilterEquation stringFilterEquation,
                                                            int nOfSuggestions,
                                                            W2vCommander w2v,
                                                            String oldTheme,
                                                            String newTheme,
                                                            Word oldWord) {
        //Perform word2vec operation
        Map<Double, String> stringSuggestionMap = w2v.findAnalogy(
                oldTheme.toLowerCase(),
                newTheme.toLowerCase(),
                oldWord.toString().toLowerCase(),
                nOfSuggestions);

        //Put suggestions into sorted TreeMap
        TreeMap<Double, String> testStringSuggestionMap = new TreeMap<>(stringSuggestionMap);

        U.testPrintln("After word2vec operation: " + stringSuggestionMap.size() + " suggestions");

        //Use string filters
        stringSuggestionMap.values().retainAll(stringFilterEquation.removeMatches(new HashSet<>(stringSuggestionMap.values())));

        U.testPrintln("After string filtration: " + stringSuggestionMap.size() + " valid suggestions");

        //Add all this word's perfect rhymes to the stringSuggestionMap. TODO use the actual distance from the rhyming word to the point of analogy.
        Set<String> allRhymes = Rhymer.getPerfectRhymes(oldWord.getSpelling(), 1);
        double extra = 0.0001;
        for (String rhyme : allRhymes) {
            stringSuggestionMap.put(1.0 + extra, rhyme.toLowerCase());
            extra += 0.0001;
        }

        //Tag pos and ne on word2vec's suggestions
        Map<Double, Word> wordSuggestionMap = new TreeMap<>();
        if (stringSuggestionMap.size() > 0) {
            wordSuggestionMap = StanfordNlp.tagWordsWithSentenceContextWithDoubles(
                    new TreeMap<>(stringSuggestionMap),
                    sentence,
                    oldWord,
                    oldWordIndex
            );

            //Assign phonemes on word2vec's suggestions
            for (Map.Entry<Double, Word> entry : wordSuggestionMap.entrySet()) {
                entry.getValue().setPhonemes(Phoneticizer.getPronunciationForWord(entry.getValue()));
            }

            //Assign syllables on word2vec's suggestions
            for (Map.Entry<Double, Word> entry : wordSuggestionMap.entrySet()) {
                entry.getValue().setSyllables(Phoneticizer.getSyllablesForWord(entry.getValue()));
            }

            //Use Word filters
            WordFilterEquation wordFilterEquation = FilterManager.wEq(FilterManager.getMirrorPosNeFilters(oldWord));
            wordSuggestionMap.values().retainAll(wordFilterEquation.removeMatches(new HashSet<>(wordSuggestionMap.values())));
//                    FiltrationResults filtrationResults = stringFilters.filterWordSuggestionsWithModel(wordSuggestions, oldWord);
//                    SimilarW2vResults w2vResults = new SimilarW2vResults();
//                    w2vResults.setFiltrationResults(filtrationResults);


            U.testPrintln("After Word filtration: " + wordSuggestionMap.size() + " valid suggestions");
        }


        //Put suggestions into sorted TreeMap
        TreeMap<Double, Word> finalWordSuggestions = new TreeMap<>(wordSuggestionMap);

        //if testing, print word2vec's filtered suggestions
        if (ProgramArgs.isTesting()) {
            int i = 0;
            U.testPrintln("Post-filtration suggestions for " + oldWord.toString() + ":");
            for (Map.Entry<Double,Word> entry : finalWordSuggestions.entrySet()) {
                if (i > this.nSuggestionsToPrint)
                    break;
                U.testPrint("\t" + entry.getValue().toString());
                i++;
            }
        }

        //Handle empty filtration results
        // TODO: Handle empty FiltrationResults here, for cases when the filters were too strict or the model was not adequate.
        // TODO > In test mode, print [n/a]
        // TODO > In presentation mode, print original word

        return SuggestionHandler.pickReplacementWord(finalWordSuggestions, wordReplacements, oldWord);
    }

}


























































































