package songtools;

import constraints.WordConstraintManager;
import constraints.WordConstraintPrioritizer;
import elements.*;
import rhyme.*;
import utils.U;
import java.util.*;

public abstract class LyristReplacer {

    public static Song normalReplace(SongWrapper songWrapper, ReplacementByAnalogyInfo info) {
        List<Word> marked = markWithConstraints(songWrapper.getSong(), info.getStringMarkingFilters());
        WordReplacements wordReplacements = new WordReplacements();
        for (Sentence s : songWrapper.getSentences()) {
            for (int w = 0; w < s.size(); w++) {
                Word oldWord = s.get(w);
                if (oldWord instanceof Punctuation || !marked.contains(oldWord) || wordReplacements.containsKey(oldWord)) continue;
                Map<Double, String> cosineStrings = WordSource.w2vAnalogy(info.getW2v(), info.getOldAndNewThemes().getFirst(), info.getOldAndNewThemes().getSecond(), oldWord.getLowerSpelling(), 100);
                Set<Word> cosineWords = stringsToWords(cosineStrings, s, oldWord, w);
                Word chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(WordConstraintManager.getNormal(), cosineWords, oldWord);
                wordReplacements.put(oldWord, chosen);
            }
        }
        return SongMutator.replaceWords(songWrapper.getSong(), wordReplacements);
    }

    public static Song rhymeReplace(SongWrapper songWrapper, ReplacementByAnalogyInfo info) {
        WordsByRhyme oldWordsByRhyme = markRhyme(info.getRhymeSchemeFilter(), songWrapper.getSong());
        OldWordsToSuggestions oldWordsToSuggestions = new OldWordsToSuggestions();
        List<Word> marked = markWithConstraints(songWrapper.getSong(), info.getStringMarkingFilters());
        for (Sentence s : songWrapper.getSentences())
            for (int w = 0; w < s.size(); w++) {
                Word oldWord = s.get(w);
                if (oldWord instanceof Punctuation || !marked.contains(oldWord) || oldWordsToSuggestions.containsKey(oldWord)) continue;
                Map<Double, String> cosineStrings = WordSource.w2vAnalogy(info.getW2v(), info.getOldAndNewThemes().getFirst(), info.getOldAndNewThemes().getSecond(), oldWord.getLowerSpelling(), 100);
                cosineStrings = stringFilters(cosineStrings, info.getStringFilters());
                cosineStrings.putAll(WordSource.cmuRhymes(oldWord.getUpperSpelling()));
                Set<Word> cosineWords = stringsToWords(cosineStrings, s, oldWord, w);
                oldWordsToSuggestions.put(oldWord, cosineWords);
            }
        WordSuggestionsByRhyme suggestionsByRhyme = sortRhymeSuggestionsByRhyme(songWrapper.getSong().getAllWords(), oldWordsToSuggestions, oldWordsByRhyme);
        List<Word> rhymeModels = getRhymeModelWords(suggestionsByRhyme, info.getNonRhymeWordFilters(), .75);
        WordReplacements replacements = terminalWordfilters(oldWordsToSuggestions, info.getNonRhymeWordFilters());
        return SongMutator.replaceWords(songWrapper.getSong(), replacements);
    }

    private static WordSuggestionsByRhyme sortRhymeSuggestionsByRhyme(List<Word> orderedOldWords, OldWordsToSuggestions oldWordsToSuggestions, WordsByRhyme oldWordsByRhyme) {
        //TODO make sure this works
        WordSuggestionsByRhyme result = new WordSuggestionsByRhyme();
        int i = 0;
        for (Map.Entry<Rhyme, Set<Word>> rhymeClass : oldWordsByRhyme.entrySet()) {
            List<Word> oldWordsInRhymeClass = new ArrayList<>();
            for (Word oldWord : orderedOldWords) {
                if (!rhymeClass.getValue().contains(oldWord)) continue;
                oldWordsInRhymeClass.add(oldWord);
            }
            List<Set<Word>> suggestionsByRhymeInstance = new ArrayList<>();
            for (Word oldWord : oldWordsInRhymeClass)
                suggestionsByRhymeInstance.add(oldWordsToSuggestions.get(oldWord));
            result.put(rhymeClass.getKey(), suggestionsByRhymeInstance);
            i++;
        }
        return result;
    }

    private static List<Word> markWithConstraints(Song songToMark, FilterEquation markingFilters) {
        List<Word> allMarkableWordsList = new ArrayList<>(songToMark.getAllWords());
        if (markingFilters instanceof StringFilterEquation) {
            StringFilterEquation wordsNotToMark = (StringFilterEquation) markingFilters;
            allMarkableWordsList.retainAll(wordsNotToMark.removeMatches(new HashSet(songToMark.getAllWords())));
        }
        else if (markingFilters instanceof WordFilterEquation) {
            WordFilterEquation wordsNotToMark = (WordFilterEquation) markingFilters;
            allMarkableWordsList.retainAll(wordsNotToMark.removeMatches(new HashSet(songToMark.getAllWords())));
        }
        return allMarkableWordsList;
    }

    private static WordsByRhyme markRhyme(RhymeSchemeFilter filter, Song song) {
        return filter.doFilter(song);
    }

    private static void replaceRndMarked() {

    }

    private static Map<Double, String> stringFilters(Map<Double, String> stringSuggestionMap, StringFilterEquation stringFilterEquation) {
        stringSuggestionMap.values().retainAll(stringFilterEquation.removeMatches(new HashSet<>(stringSuggestionMap.values())));
        U.testPrintln("After string filtration: " + stringSuggestionMap.size() + " valid suggestions");
        return stringSuggestionMap;
    }

//    private static Map<Double, Word> stringsToWords(Map<Double, String> stringSuggestionMap, Sentence sentence, Word oldWord, int oldWordIndex) {
//        return SuggestionHandler.stringSuggestionsToWordSuggestions(stringSuggestionMap, sentence, oldWord, oldWordIndex);
//    }

    private static Set<Word> stringsToWords(Map<Double, String> stringSuggestionMap, Sentence sentence, Word oldWord, int oldWordIndex) {
        return SuggestionHandler.stringSuggestionsToWordSuggestions(stringSuggestionMap, sentence, oldWord, oldWordIndex);
    }

    private static Map<Double, Word> wordfilters(Set<Word> wordSuggestions, WordFilterEquation wordFilterEquation) {
        wordSuggestions.values().retainAll(wordFilterEquation.removeMatches(new HashSet<>(wordSuggestions.values())));//TODO BREAKS ON "It's", have Words know whether they are a contraction, and if so, what contraction they are. Allow Words to return their expansion of 2+ Words.
        U.testPrintln("After Word filtration: " + wordSuggestions.size() + " valid suggestions");
        return wordSuggestions;
    }

    private static Word terminalWordfilters(Map<Double, Word> wordSuggestionMap, WordFilterEquation wordFilterEquation) {
        wordSuggestionMap.values().retainAll(wordFilterEquation.removeMatches(new HashSet<>(wordSuggestionMap.values())));//TODO BREAKS ON "It's", have Words know whether they are a contraction, and if so, what contraction they are. Allow Words to return their expansion of 2+ Words.
        U.testPrintln("After Word filtration: " + wordSuggestionMap.size() + " valid suggestions");
        return ((TreeMap<Double, Word>)wordSuggestionMap).firstEntry().getValue();
    }

    private static WordReplacements terminalWordfilters(OldWordsToSuggestions oldWordsToSuggestions, WordFilterEquation wordFilterEquation) {
        WordReplacements result = new WordReplacements();
        for (Map.Entry<Word,Set<Word>> entry : oldWordsToSuggestions.entrySet()) {
            Set<Word> suggestions = entry.getValue();
            suggestions.retainAll(wordFilterEquation.removeMatches(new HashSet<>(suggestions)));
            U.testPrintln("After terminal Word filtration: " + suggestions.size() + " valid suggestions");
            result.put(entry.getKey(), ((TreeSet<Word>)suggestions).first());
        }
        return result;
    }

    private static List<Word> getRhymeModelWords(WordSuggestionsByRhyme wordsToRhyme, WordFilterEquation nonRhymeFilters, double rhymeThreshold) {
        //get rhyme models for each rhyme class
        List<Word> result = new ArrayList<>();
        for (Map.Entry<Rhyme, List<Set<Word>>> rhymeClass : wordsToRhyme.entrySet()) {
            Word rhymeModel = weakenFiltersUntilBestRhymingWord(rhymeClass.getValue(), nonRhymeFilters, rhymeThreshold);
            result.add(rhymeModel);
        }
        return result;
    }

    private static Word weakenFiltersUntilBestRhymingWord(List<Set<Word>> suggestionSets, WordFilterEquation nonRhymeFilters, double rhymeThreshold) {
        //returns the model for rhyme class A, that all other filterWords of rhyme class A will have to rhyme with
        //if rhyme priority > other word selection priorities

        //pick 1 suggestion that rhymes with the most / best other A filterWords
        Word bestRhymingWord = chooseBestWordForRhyming(suggestionSets, nonRhymeFilters, rhymeThreshold);

        //priority loop weakens constraints each time there is no result. This weakens other constraints at the price of optimizing rhyme.
        while (bestRhymingWord == null) {
            nonRhymeFilters = WordConstraintPrioritizer.weakenOrRemoveLastConstraint(nonRhymeFilters);
            bestRhymingWord = chooseBestWordForRhyming(suggestionSets, nonRhymeFilters, rhymeThreshold);
        }
        return bestRhymingWord;
    }

    private static Word chooseBestWordForRhyming(List<Set<Word>> suggestionSets, WordFilterEquation nonRhymeFilters, double rhymeThreshold) {
        //Chooses the best word in its rhyme scheme (best = most rhymeable)
        //Attempts to optimize all constraints in choice of rhyming word

        //input all suggestions for rhyme A filterWords
        Word bestRhymer = null;
        int mostRhymes = Integer.MIN_VALUE;
        Set<Word> bestNonRhymeWords = new HashSet<>();

        //Pick the non-rhyme best word from each set
        for (Set<Word> suggestionSet : suggestionSets)
            bestNonRhymeWords.add(terminalWordfilters(nonRhymeFilters, suggestionSet));

        //Of those filterWords, find the word with the most rhymes
        int wordNum = 0;
        for (Word candidateBestWord : bestNonRhymeWords) {
            //Get all filterWords from other suggestion sets, NOT including suggestionSet of chosen word
            Set<Word> allWordsFromOtherSets = new HashSet<>();
            for (int setNum = 0; setNum < suggestionSets.size(); setNum++)
                if (setNum != wordNum)
                    allWordsFromOtherSets.addAll(suggestionSets.get(wordNum));

            //See how many of those filterWords rhyme with the candidate bestRhymer TODO later dbl according to whether the set's best Words rhyme?
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

    /*
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
        //TODO > if the above technique lets too many wrong filterWords through, change it.

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
                    // Replace filterWords that are marked for replacement
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
                    // Replace filterWords that are on the rhyme scheme
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

        //w2v suggestions -> filterWords
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
        Set<String> allRhymes = Rhymer.getPerfectRhymes(oldWord.getLowerSpelling(), 1);
        double extra = 0.0001;
        for (String rhyme : allRhymes) {
            stringSuggestionMap.put(1.0 + extra, rhyme.toLowerCase());
            extra += 0.0001;
        }

        //Tag wordsToPos and filterNe on word2vec's suggestions
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
*/

}


/*
Fix:
    Filters
    Constraints (make these into enums? Something that can call a method for its specific filterByMultiple)
make functions as modular as possible,
get normal replacement and rhyme replacement to work fully.

> Rename "Filters" to "Constraints"; Filters are the methods inside FilterMethods.
Constraints can access certain methods in FilterMethods.
Constraints can return their enum?
Constraints can weaken themselves, return bool
Constraints can strengthen themselves, return bool
 */














































































