package songtools;

import constraints.Constraint;
import elements.Sentence;
import elements.Word;
import filters.Filter;
import filters.FilterObject;
import filters.StringFilterEquation;
import filters.WordFilterEquation;
import main.ProgramArgs;
import rhyme.Phoneticizer;
import stanford.StanfordNlp;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

public abstract class SuggestionHandler {

    private int defaultSuggestionNum = 100;
    private int defaultRhymeSuggestionNum = 1000;

    //takes in word suggestions, uses filters and constraints to return individual words

    //TODO make notOldWord and notRedundantWord into their own filters

    //Prioritize: closeness to analogy point, rhyme, pos, named entity, all other filters (dirty words, etc)
    //Prioritize: replacement on all by analogy, replacement on rhyme scheme by rhyme
    public static Word pickReplacementWord(TreeMap<Double, Word> wordSuggestions, WordReplacements wordReplacements, Word oldWord) {
        Word chosenWord;
        try {
            chosenWord = wordSuggestions.firstEntry().getValue();

            //prevent the same suggestion from being used twice TODO make this a prioritized filter, just like everything else
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

    public static Word pickReplacementWord(TreeMap<Integer, Constraint> constraints, Set<Word> words) {
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

    public static Map<Double, String> useStringFilters(Map<Double, String> strings, StringFilterEquation filters) {
        StringFilterEquation currentEquation = new StringFilterEquation();
        Set<String> currentResults = null;
        Set<String> previousResults = null;
        for (FilterObject filterObject : filters) {
            currentEquation.add(filterObject);
            if (filterObject instanceof Filter) {
                previousResults = currentResults;
                currentResults = currentEquation.run(strings.values());
            }
            if (currentResults == null || currentResults.size() == 0) {
                currentResults = previousResults;
                break;//TODO decide if one filter is too restrictive if it should be skipped and lower prioirity filters be used
            }
        }
        if (currentResults != null && currentResults.size() > 0) {
            strings.values().retainAll(currentResults);
            return strings;
        }
        //nothing got through the filters
        return null;
    }

    public static Map<Double, Word> useWordFilters(Map<Double, Word> words, WordFilterEquation filters) {
        WordFilterEquation currentEquation = new WordFilterEquation();
        Set<Word> currentResults = null;
        Set<Word> previousResults = null;
        for (FilterObject filterObject : filters) {
            currentEquation.add(filterObject);
            if (filterObject instanceof Filter) {
                previousResults = currentResults;
                currentResults = currentEquation.run(words.values());
            }
            if (currentResults == null || currentResults.size() == 0) {
                currentResults = previousResults;
                break;//TODO decide if one filter is too restrictive if it should be skipped and lower prioirity filters be used
            }
        }
        if (currentResults != null && currentResults.size() > 0) {
            words.values().retainAll(currentResults);
            return words;
        }
        //nothing got through the filters
        return null;
    }

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
























































































































