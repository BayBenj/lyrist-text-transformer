package songtools;

import constraints.*;
import elements.*;
import main.MultiProgramArgs;
import rhyme.*;
import stanford.StanfordNlp;
import utils.U;
import word2vec.BadW2vInputException;
import java.util.*;

public abstract class LyristTransformer {

    public static InfoSong transform(InfoSong originalSong, NormalReplacementInfo info, boolean rhyming) {

        //get word suggestions
        WordsToSuggestions suggestions = getSuggestions(originalSong, info, rhyming);

        //use constraints on suggestions
        WordReplacements replacements = filterSuggestions(suggestions, info);

        //replace words
        InfoSong transformed = SongMutator.replaceWords(originalSong, replacements);
        transformed.setOldTheme(info.getOldTheme());
        transformed.setNewTheme(info.getNewTheme());

        return transformed;
    }

    public static WordsToSuggestions getSuggestions(InfoSong originalSong, NormalReplacementInfo info, boolean rhyming) {
        try {
            final Set<Word> marked = WordConstraintPrioritizer.useConstraintsByWeakening(info.getMarkingConstraints(), originalSong.words());
            final WordsToSuggestions suggestions = new WordsToSuggestions();
            final List<Word> allOldWords = originalSong.words();

            //For each word in original song, get replacement suggestions
            for (final Word oldWord : allOldWords) {
                if (oldWord instanceof Punctuation || !marked.contains(oldWord) || suggestions.keySet().contains(oldWord))//TODO: make suggestions have a list rather than a set of oldWords?
                    continue;

                Map<Double, String> cosineStrings = new HashMap<>();
                //Add w2v analogy suggestions
                try {
                    cosineStrings.putAll(WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), 50));
                }
                catch (BadW2vInputException e) {
                    System.out.print("\t***Bad w2v input: " + oldWord.toString());
                    System.out.println("\t <- this word will not be replaced");
                    e.printStackTrace();
                    continue;
                }

                final Set<Word> cosineWords = cosineStringsToWords(cosineStrings, oldWord);

                //if rhyming, add CMU perfect rhymes for each suggestion
                if (rhyming) {
                    Map<Double, String> rhymeStrings = new HashMap<>();
                    for (Word w : cosineWords) {
                        try {
                            rhymeStrings.putAll(WordSource.perfectCmuRhymes(w.getRhymeTail(), 10));
                        }
                        catch (NoRhymeFoundException e) {
                            System.out.println("\t***No rhyme found for suggestion: " + w.toString());
                            e.printStackTrace();
                        }
                    }
                    cosineWords.addAll(cosineStringsToWords(rhymeStrings, oldWord));
                }
                suggestions.putWord(oldWord, cosineWords);
////                Word chosen = null;
//                try {
//                    WordConstraintPrioritizer.enableAllConstraints(info.getNormalConstraints());
//                    if (baseReplacements.keySet().contains(oldWord.getBase())) {//if this oldWord has the same base as an already replaced oldWord
//                        WordConstraintPrioritizer.enableAllConstraints(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())));//TODO should this be fixed?
//                        chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())), oldWord, cosineWords);
//                    }
//                    else
//                        chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getNormalConstraints(), oldWord, cosineWords);
//                }
//                catch (EnforcedConstraintException e) {
//                    e.printStackTrace();
//                }
//
//                //if no suggestion was successful, don't do a replacement
//                if (chosen == null) {
//                    try {
//                        throw new NoWordReplacementException();
//                    }
//                    catch (NoWordReplacementException e) {
//                        System.out.println("\t***No word replacement for old word " + oldWord.toString());
//                        e.printStackTrace();
//                        if (MultiProgramArgs.isDebugMode())
//                            chosen = new Word("[n/a]");
//                        else
//                            continue;
//                    }
//                }

//                wordReplacements.put(oldWord, chosen);
//                baseReplacements.put(oldWord.getBase(), chosen.getBase());
//                ((StringConstraint) info.getNormalConstraints().get(3)).getObjects().add(chosen.getLowerSpelling());//add this spelling to alreadyUsed
//                ((BaseConstraint) info.getNormalConstraints().get(2)).getObjects().add(chosen.getBase());//add this base to alreadyUsed
            }
            return suggestions;
        }
        catch (EnforcedConstraintException e) {
            e.printStackTrace();
            return null;
        }
    }

//    private static void chooseModelsAssignRhymeScores(List<Word> orderedOldWords, WordsToSuggestions wordsToSuggestions, WordsByRhyme oldWordsByRhyme, List<WordConstraint> constraints) {
//        WordSuggestionsByRhyme suggestionsByRhyme = sortRhymeSuggestionsByRhyme(orderedOldWords, wordsToSuggestions, oldWordsByRhyme);
//        Map<Rhyme,Word> rhymeModels = getRhymeModelWords(suggestionsByRhyme, constraints, .7);
////        WordSuggestionsByRhymeModels result = new WordSuggestionsByRhymeModels();
//        for (Map.Entry<Rhyme,List<Pair<Word,Set<Word>>>> entry : suggestionsByRhyme.entrySet()) {
//            Rhyme rhyme = entry.getKey();
//            Word model = rhymeModels.get(rhyme);
//            List<Pair<Word,Set<Word>>> instances = entry.getValue();
//            for (Pair<Word,Set<Word>> pair : instances)
//                for (Word suggestion : pair.getSecond()) {
//                    double score = Rhymer.score2Rhymes(model.getRhymeTail(), suggestion.getRhymeTail());
//                    suggestion.setRhymeScore(score);
//                }
////            result.put(model, instances);
//        }
//    }
//
//    public static Set<String> wordsToBases(Collection<Word> words) {
//        Set<String> result = new HashSet<>();
//        for (Word word : words)
//            if (word.getBase() != null)
//                result.add(word.getBase());
//        return result;
//    }
    
    public static Set<Word> cosineStringsToWords(Map<Double, String> stringMapSuggestions, Word oldWord) {
        U.testPrint("Entered cosineStringsToWords");
        //text, Pos and Ne for each word
        if (stringMapSuggestions == null || stringMapSuggestions.isEmpty()) return null;
        Map<Double, Word> wordMap = StanfordNlp.tagWordsWithSentenceContextWithDoubles(new TreeMap<>(stringMapSuggestions), oldWord);

        //Assign syllables on word2vec's suggestions
        for (Map.Entry<Double, Word> entry : wordMap.entrySet()) {
            entry.getValue().setSyllables(Phoneticizer.getSyllablesForWord(entry.getValue()));
        }

        //Put cosine distances into result
        Set<Word> result = new HashSet<>();
        for (Map.Entry<Double, Word> entry : wordMap.entrySet()) {
            Word temp = entry.getValue();
            temp.setCosineDistance(entry.getKey());
            result.add(temp);
        }
        return result;
    }

    public static WordReplacements filterSuggestions(final WordsToSuggestions wordsToSuggestions, final NormalReplacementInfo info) {//TODO get this to work
        U.testPrint("Started filtering suggestions");
        //Filter suggestions
        final WordReplacements replacements = new WordReplacements();
        final StringReplacements baseReplacements = new StringReplacements();
        for (Map.Entry<Word, Set<Word>> wordToSuggestions : wordsToSuggestions.entrySet()) {
            final Word oldWord = wordToSuggestions.getKey();
            final Set<Word> suggestions = wordToSuggestions.getValue();
            Word chosen;
            WordConstraintPrioritizer.enableAllConstraints(info.getNormalConstraints());

            try {//Use normal filters
                WordConstraintPrioritizer.disableRhymeConstraints(info.getNormalConstraints());
                if (baseReplacements.keySet().contains(oldWord.getBase())) {
                    WordConstraintPrioritizer.enableAllConstraints(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())));
                    chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())), oldWord, suggestions);
                }
                else
                    chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getNormalConstraints(), oldWord, suggestions);

                //if no suggestion was successful, don't do a replacement
                if (chosen == null) {
                    try {
                        System.out.println("\t***No word replacement for old word: " + oldWord.toString());
                        throw new NoWordReplacementException();
                    } catch (NoWordReplacementException e) {
                        e.printStackTrace();
                        if (MultiProgramArgs.isDebugMode())
                            chosen = new Word("[n/a]");
                        else
                            continue;
                    }
                }
            } catch (EnforcedConstraintException e) {
                e.printStackTrace();
                chosen = new Word("[n/a]");
            }

            replacements.put(oldWord, chosen);
            baseReplacements.put(oldWord.getBase(), chosen.getBase());
            ((StringConstraint) info.getNormalConstraints().get(3)).getObjects().add(chosen.getLowerSpelling());
            ((BaseConstraint) info.getNormalConstraints().get(2)).getObjects().add(chosen.getBase());
        }

        return replacements;
    }

//    private static Map<Rhyme,Word> getRhymeModelWords(WordSuggestionsByRhyme wordsToRhyme, List<WordConstraint> nonRhymeFilters, double rhymeThreshold) {
//        //get rhyme models for each rhyme class
//        Map<Rhyme,Word> result = new HashMap<>();
//        for (Map.Entry<Rhyme, List<Pair<Word,Set<Word>>>> rhymeClass : wordsToRhyme.entrySet()) {
//            Word rhymeModel = null;
//            while (rhymeModel == null)
//                rhymeModel = chooseBestWordForRhyming(rhymeClass.getValue(), nonRhymeFilters, rhymeThreshold);
//            result.put(rhymeClass.getKey(), rhymeModel);
//        }
//        return result;
//    }
//
//    private static Word chooseBestWordForRhyming(List<Pair<Word,Set<Word>>> suggestionSets, List<WordConstraint> nonRhymeFilters, double rhymeThreshold) {
//        //Chooses the best word in its rhyme scheme (best = most rhymeable)
//        //Attempts to optimize all constraints in choice of rhyming word
//
//        Set<Word> bestNonRhymeWords = new HashSet<>();
//
//        WordConstraintPrioritizer.disableRhymeConstraints(nonRhymeFilters);
//
//        //Pick the best non-rhyming word from each set
//        for (Pair<Word,Set<Word>> suggestionSet : suggestionSets)
//            bestNonRhymeWords.add(WordConstraintPrioritizer.useConstraintsTo1ByWeakening(WordConstraintManager.getNormal(), suggestionSet.getFirst(), suggestionSet.getSecond()));
//
//        //Of those filterWords, find the word with the most rhymes
//        int wordNum = 0;
//        Word bestRhymer = null;
//        int mostRhymes = Integer.MIN_VALUE;
//        for (Word candidateBestWord : bestNonRhymeWords) {
//            //Get all filterWords from other suggestion sets, NOT including suggestionSet of chosen word
//            Set<Word> allWordsFromOtherSets = new HashSet<>();
//            for (int setNum = 0; setNum < suggestionSets.size(); setNum++)
//                if (setNum != wordNum)
//                    allWordsFromOtherSets.addAll(suggestionSets.get(wordNum).getSecond());
//
//            //See how many of those filterWords rhyme with the candidate bestRhymer TODO later dbl according to whether the set's best Words rhyme?
//            int nRhymes = getNRhymingWords(candidateBestWord, suggestionSets.get(wordNum).getSecond(), rhymeThreshold);
//            if (nRhymes > mostRhymes) {
//                mostRhymes = nRhymes;
//                bestRhymer = candidateBestWord;
//            }
//            wordNum++;
//        }
//        return bestRhymer;
//    }
//
//    private static int getNRhymingWords(Word rhymeModel, Set<Word> words, double rhymeScoreThreshold) {
//        int n = 0;
//        for (Word word : words)
//            if (Rhymer.score2Rhymes(rhymeModel.getRhymeTail(), word.getRhymeTail()) >= rhymeScoreThreshold)
//                n++;
//        return n;
//    }

}




/*
> Add a bad string filter to normal filters, so that non-alphabetic characters stop getting through

> Fix up rhyme scoring algorithm to choose better rhymes
    him - skit
    1) Align the syllables starting at the end of (the rhyme of?) each word. Each syllable insertion/deletion costs ?.
    2) Align the inside of each syllable
    Charge points for every substitution (depending on type of substitution). Insertion/deletions may be more expensive.

> Make a list of bad rhymes and good rhymes to better do the above.

> Make the score2Phonemes function.
 */

















































































































