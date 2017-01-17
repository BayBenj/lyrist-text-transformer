package song;

import filters.*;
import main.ProgramArgs;
import rhyme.Phoneticizer;
import rhyme.Rhymer;
import stanford_nlp.StanfordNlp;
import utils.Utils;
import word2vec.W2vCommander;

import java.util.*;

public class ReplacementJob {
//TODO eventually make children of this class that represent specifc W2v operation sequences, like [sentiment, similar, analogy, similar]
//TODO or [analogy, similar] or [similar], etc. Maybe, unless there's a smarter way.
// TODO: This class should simply execute a sequence of W2vOperations, interface with Filters, and return WordReplacements or new Words.

    private int nSuggestionsToPrint;

    public Word getExistingSentiment(Set<Word> wordsToAverage, W2vCommander w2v) {
//        Random rand = new Random();
//        return temporarySentimentList.get(rand.nextInt(temporarySentimentList.size()));
        return new Word("sorrow");
//        HashSet<String> stringsToAverage = new HashSet<String>();
//        for (Word w : wordsToAverage)
//            stringsToAverage.add(w.getSpelling().toLowerCase());
//        TreeSet<W2vSuggestion> suggestions = new TreeSet<W2vSuggestion>(w2v.findSentiment(stringsToAverage, 10));
//
//        Word oldSentiment = new Word(suggestions.first().getString());
//
//        SongElementStructurer structurer = new SongElementStructurer();
//        return structurer.structureWord(oldSentiment, Structure.POS);
    }

    public WordReplacements getAnalogousWords(Set<Word> wordsToReplace,
                                              List<Sentence> sentences,
                                              StringFilterEquation stringFilterEquation,
                                              int nOfSuggestions,
                                              W2vCommander w2v,
                                              String oldTheme,
                                              String newTheme,
                                              Set<Word> wordsToRhyme
                                              //,WordFilterEquation wordFilterEquation
                                            )
    {
        //TODO > make suggestion tagging use the original template sentence with the new word in the old word's place.
        //TODO > if the above technique lets too many wrong words through, change it.

        Utils.testPrintln("Entering getAnalogousWords");
        WordReplacements wordReplacements = new WordReplacements();
        for(Sentence sentence : sentences) {
            int oldWordIndex = 0;
            for(Word oldWord : sentence) {
                // Only replace words that are marked for replacement
                if (    wordsToReplace.contains(oldWord) &&
                        !wordReplacements.containsKey(oldWord) &&
                        oldWord.getClass() != Punctuation.class)
                {
                    Word chosenWord = this.getReplacementWord(wordReplacements,
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
                oldWordIndex++;
            }
        }
        return wordReplacements;
    }

    private Word getReplacementWord(WordReplacements wordReplacements,
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
        Utils.testPrintln("After word2vec operation: " + stringSuggestionMap.size() + " suggestions");

        //Use string filters
        stringSuggestionMap.values().retainAll(stringFilterEquation.removeMatches(new HashSet<>(stringSuggestionMap.values())));
        Utils.testPrintln("After string filtration: " + stringSuggestionMap.size() + " valid suggestions");

        //w2v suggestions -> words
        Map<Double, Word> wordSuggestionMap = stringsToWords(stringSuggestionMap, sentence, oldWord, oldWordIndex);

        //Use Word filters
        WordFilterEquation wordFilterEquation = FilterManager.wEq(FilterManager.getMirrorPosNeFilters(oldWord));
        wordSuggestionMap.values().retainAll(wordFilterEquation.removeMatches(new HashSet<>(wordSuggestionMap.values())));
//                    FiltrationResults filtrationResults = stringFilters.filterWordSuggestionsWithModel(wordSuggestions, oldWord);
//                    SimilarW2vResults w2vResults = new SimilarW2vResults();
//                    w2vResults.setFiltrationResults(filtrationResults);
        Utils.testPrintln("After Word filtration: " + wordSuggestionMap.size() + " valid suggestions");

        //Put suggestions into sorted TreeMap
        TreeMap<Double, Word> finalWordSuggestions = new TreeMap<>(wordSuggestionMap);

        //if testing, print word2vec's filtered suggestions
        if (ProgramArgs.isTesting()) {
            int i = 0;
            Utils.testPrintln("Post-filtration suggestions for " + oldWord.toString() + ":");
            for (Map.Entry<Double,Word> entry : finalWordSuggestions.entrySet()) {
                if (i > this.nSuggestionsToPrint)
                    break;
                Utils.testPrint("\t" + entry.getValue().toString());
                i++;
            }
        }

        return pickReplacementWord(finalWordSuggestions, wordReplacements, oldWord);
    }

    private Word getReplacementRhymingWord(WordReplacements wordReplacements,
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

        Utils.testPrintln("After word2vec operation: " + stringSuggestionMap.size() + " suggestions");

        //Use string filters
        stringSuggestionMap.values().retainAll(stringFilterEquation.removeMatches(new HashSet<>(stringSuggestionMap.values())));

        Utils.testPrintln("After string filtration: " + stringSuggestionMap.size() + " valid suggestions");

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


            Utils.testPrintln("After Word filtration: " + wordSuggestionMap.size() + " valid suggestions");
        }


        //Put suggestions into sorted TreeMap
        TreeMap<Double, Word> finalWordSuggestions = new TreeMap<>(wordSuggestionMap);

        //if testing, print word2vec's filtered suggestions
        if (ProgramArgs.isTesting()) {
            int i = 0;
            Utils.testPrintln("Post-filtration suggestions for " + oldWord.toString() + ":");
            for (Map.Entry<Double,Word> entry : finalWordSuggestions.entrySet()) {
                if (i > this.nSuggestionsToPrint)
                    break;
                Utils.testPrint("\t" + entry.getValue().toString());
                i++;
            }
        }

        //Handle empty filtration results
        // TODO: Handle empty FiltrationResults here, for cases when the filters were too strict or the model was not adequate.
        // TODO > In test mode, print [n/a]
        // TODO > In presentation mode, print original word

        return pickReplacementWord(finalWordSuggestions, wordReplacements, oldWord);
    }

    private Map<Double, String> useStringFilters(Map<Double, String> strings, StringFilterEquation filters) {
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

    private Map<Double, Word> useWordFilters(Map<Double, Word> words, WordFilterEquation filters) {
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

    //Prioritize: closeness to analogy point, rhyme, pos, named entity, all other filters (dirty words, etc)
    //Prioritize: replacement on all by analogy, replacement on rhyme scheme by rhyme
    private Word pickReplacementWord(TreeMap<Double, Word> finalWordSuggestions, WordReplacements wordReplacements, Word oldWord) {
        Word chosenWord;
        try {
            chosenWord = finalWordSuggestions.firstEntry().getValue();

            //prevent the same suggestion from being used twice TODO make this a prioritized filter, just like everything else
            while (wordReplacements.containsValue(chosenWord)) {
                finalWordSuggestions.remove(finalWordSuggestions.firstKey());
                chosenWord = finalWordSuggestions.firstEntry().getValue();
            }
        }
        catch (NoSuchElementException | NullPointerException e) {
            if (ProgramArgs.isTesting()) {
                e.printStackTrace();
                System.out.println("oldWord: " + oldWord.toString());
                System.out.println("Pos: " + oldWord.getPos().toString());
                System.out.println("finalWordSuggestions size: " + finalWordSuggestions.size());
                chosenWord = new Word("[n/a]");
                e.printStackTrace();
            }
            else
                chosenWord = oldWord;
        }
        return chosenWord;
    }

    private Map<Double, Word> stringsToWords(Map<Double, String> stringMap,
                                             Sentence sentence,
                                             Word oldWord,
                                             int oldWordIndex) {
        if (stringMap.size() > 0) {
            Map<Double, Word> wordMap = StanfordNlp.tagWordsWithSentenceContextWithDoubles(
                    new TreeMap<>(stringMap),
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


    private Map<Double, String> wordsToStrings(Map<Double, Word> wordMap) {
        if (wordMap.size() > 0) {
            Map<Double, String> stringMap = new TreeMap<>();
            for (Map.Entry<Double, Word> entry : wordMap.entrySet()) {
                stringMap.put(entry.getKey(), entry.getValue().toString());
            }
            return stringMap;
        }
        return null;//TODO return empty map instead?
    }

    public int getnSuggestionsToPrint() {
        return nSuggestionsToPrint;
    }

    public void setnSuggestionsToPrint(int nSuggestionsToPrint) {
        this.nSuggestionsToPrint = nSuggestionsToPrint;
    }
}

























































