package songtools;

import constraints.WordConstraint;
import elements.Sentence;
import elements.Word;
//import filters.Filter;
//import filters.FilterObject;
//import filters.StringFilterEquation;
//import filters.WordFilterEquation;
import main.ProgramArgs;
import rhyme.Phoneticizer;
import stanford.StanfordNlp;

import java.util.*;

public abstract class SuggestionHandler {

    private int defaultSuggestionNum = 100;
    private int defaultRhymeSuggestionNum = 1000;

    //takes in word suggestions, uses filters and constraints to return individual filterWords

    //TODO make notOldWord and notRedundantWord into their own filters

    //Prioritize: closeness to analogy point, rhyme, wordsToPos, named entity, all other filters (dirty filterWords, etc)
    //Prioritize: replacement on all by analogy, replacement on rhyme scheme by rhyme
    public static Word pickReplacementWord(TreeMap<Double, Word> wordSuggestions, WordReplacements wordReplacements, Word oldWord) {
        Word chosenWord;
        try {
            chosenWord = wordSuggestions.firstEntry().getValue();

            //prevent the instanceSpecific suggestion from being used twice TODO make this a prioritized filterByMultiple, just like everything else
            while (wordReplacements.containsValue(chosenWord)) {
                wordSuggestions.remove(wordSuggestions.firstKey());
                chosenWord = wordSuggestions.firstEntry().getValue();
            }
        }
        catch (NoSuchElementException | NullPointerException e) {
            if (ProgramArgs.isTesting()) {
                e.printStackTrace();
                System.out.println("oldWord: " + oldWord.toString());
                System.out.println("Pos: " + oldWord.getPos().toString());
                System.out.println("finalWordSuggestions size: " + wordSuggestions.size());
                chosenWord = new Word("[n/a]");
                e.printStackTrace();
            }
            else
                chosenWord = oldWord;
        }
        return chosenWord;
    }

    public static Word pickReplacementWord(TreeMap<Integer, WordConstraint> constraints, Set<Word> words) {
        //TODO implement this
        return null;
    }

    public static Word pickClosest(TreeMap<Double, Word> wordSuggestions) {
        Word closestWord = null;
        try {
            closestWord = wordSuggestions.firstEntry().getValue();
        }
        catch (NoSuchElementException e) {
            if (ProgramArgs.isTesting()) {
                e.printStackTrace();
                closestWord = new Word("[n/a]");
            }
        }
        return closestWord;
    }

//    public static Map<Double, String> useStringFilters(Map<Double, String> strings, StringFilterEquation filters) {
//        StringFilterEquation currentEquation = new StringFilterEquation();
//        Set<String> currentResults = null;
//        Set<String> previousResults = null;
//        for (FilterObject filterObject : filters) {
//            currentEquation.add(filterObject);
//            if (filterObject instanceof Filter) {
//                previousResults = currentResults;
//                currentResults = currentEquation.run(strings.values());
//            }
//            if (currentResults == null || currentResults.size() == 0) {
//                currentResults = previousResults;
//                break;//TODO decide if one filterByMultiple is too restrictive if it should be skipped and lower prioirity filters be used
//            }
//        }
//        if (currentResults != null && currentResults.size() > 0) {
//            strings.values().retainAll(currentResults);
//            return strings;
//        }
//        //nothing got through the filters
//        return null;
//    }
//
//    public static Map<Double, Word> useWordFilters(Map<Double, Word> words, WordFilterEquation filters) {
//        WordFilterEquation currentEquation = new WordFilterEquation();
//        Set<Word> currentResults = null;
//        Set<Word> previousResults = null;
//        for (FilterObject filterObject : filters) {
//            currentEquation.add(filterObject);
//            if (filterObject instanceof Filter) {
//                previousResults = currentResults;
//                currentResults = currentEquation.run(words.values());
//            }
//            if (currentResults == null || currentResults.size() == 0) {
//                currentResults = previousResults;
//                break;//TODO decide if one filterByMultiple is too restrictive if it should be skipped and lower prioirity filters be used
//            }
//        }
//        if (currentResults != null && currentResults.size() > 0) {
//            words.values().retainAll(currentResults);
//            return words;
//        }
//        //nothing got through the filters
//        return null;
//    }

    public static Map<Double, Word> stringSuggestionsToWordSuggestions(Map<Double, String> stringMapSuggestions,
                                                                       Sentence sentence,
                                                                       Word oldWord,
                                                                       int oldWordIndex) {
        if (stringMapSuggestions.size() > 0) {
            Map<Double, Word> wordMap = StanfordNlp.tagWordsWithSentenceContextWithDoubles(
                    new TreeMap<>(stringMapSuggestions),
                    sentence,
                    oldWord,
                    oldWordIndex
            );

            //Assign phonemes on word2vec's suggestions
            for (Map.Entry<Double, Word> entry : wordMap.entrySet()) {
                entry.getValue().setPhonemes(Phoneticizer.getPronunciationForWord(entry.getValue()));
            }

            //Assign syllables on word2vec's suggestions
            for (Map.Entry<Double, Word> entry : wordMap.entrySet()) {
                entry.getValue().setSyllables(Phoneticizer.getSyllablesForWord(entry.getValue()));
            }
            return wordMap;
        }
        return null;//TODO return empty map instead?
    }

    public static Set<Word> stringSuggestionsToDataWordSuggestions(Map<Double, String> stringMapSuggestions,
                                                                       Sentence sentence,
                                                                       Word oldWord,
                                                                       int oldWordIndex) {
        Map<Double, Word> wordMap = stringSuggestionsToWordSuggestions(stringMapSuggestions, sentence, oldWord, oldWordIndex);
        Set<Word> dataWords = new HashSet<>();
        for (Map.Entry<Double, Word> entry : wordMap.entrySet()) {
            Word dw = ((Word)entry.getValue());
            dw.setCosineDistance(entry.getKey());
            dataWords.add(dw);
        }
        return dataWords;
    }


    public static Map<Double, String> wordSuggestionsToStringSuggestions(Map<Double, Word> wordMap) {
        if (wordMap.size() > 0) {
            Map<Double, String> stringMap = new TreeMap<>();
            for (Map.Entry<Double, Word> entry : wordMap.entrySet()) {
                stringMap.put(entry.getKey(), entry.getValue().toString());
            }
            return stringMap;
        }
        return null;//TODO return empty map instead?
    }

}























































































































