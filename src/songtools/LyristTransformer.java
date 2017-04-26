package songtools;

import constraints.*;
import elements.*;
import main.LyristDriver;
import main.MultiProgramArgs;
import rhyme.*;
import stanford.StanfordNlp;
import utils.Pair;
import utils.U;
import word2vec.BadW2vInputException;

import java.io.IOException;
import java.util.*;

public abstract class LyristTransformer {

    private static final int N_NORMAL = 10;
    private static final int N_RHYME = 1000;

    public static void main(String[] args) throws IOException {
        LyristDriver.standardSetup();

        WordSuggestionsByRhyme wordSuggestionsByRhyme = new WordSuggestionsByRhyme();
        Word w1 = new Word("blues");
        Word w2 = new Word("red");
        Word w3 = new Word("purples");
        w1.setPronunciations(Phoneticizer.getSyllables("blues"));
        w2.setPronunciations(Phoneticizer.getSyllables("red"));
        w3.setPronunciations(Phoneticizer.getSyllables("purples"));
        w1.setBase("blue");
        w2.setBase("red");
        w3.setBase("purples");
        w1.setPos(Pos.NNS);
        w2.setPos(Pos.JJ);
        w3.setPos(Pos.NNS);
        Set<Word> set1 = new HashSet<>();
        set1.add(w1);
        set1.add(w2);
        set1.add(w3);
        Word w4 = new Word("shoes");
        Word w5 = new Word("boots");
        Word w6 = new Word("kick");
        w4.setPronunciations(Phoneticizer.getSyllables("shoes"));
        w5.setPronunciations(Phoneticizer.getSyllables("boots"));
        w6.setPronunciations(Phoneticizer.getSyllables("kick"));
        w4.setBase("shoe");
        w5.setBase("boot");
        w6.setBase("kick");
        w4.setPos(Pos.NNS);
        w5.setPos(Pos.NNS);
        w6.setPos(Pos.VB);
        Set<Word> set2 = new HashSet<>();
        set2.add(w4);
        set2.add(w5);
        set2.add(w6);
        List<Pair<Word, Set<Word>>> list1 = new ArrayList();
        Word o1 = new Word("oranges");
        o1.setPronunciations(Phoneticizer.getSyllables("oranges"));
        o1.setBase("orange");
        o1.setPos(Pos.NNS);
        Word o2 = new Word("sandals");
        o2.setPronunciations(Phoneticizer.getSyllables("sandals"));
        o2.setBase("sandal");
        o2.setPos(Pos.NNS);
        list1.add(new Pair<>(o1, set1));
        list1.add(new Pair<>(o2, set2));
        wordSuggestionsByRhyme.put(new RhymeClass(0), new ArrayList<>(list1));

//        Set<Word> set3 = new HashSet<>();
//        Word w7 = new Word("bones");
//        Word w8 = new Word("skull");
//        Word w9 = new Word("guts");
//        w7.setPos(Pos.NNS);
//        w8.setPos(Pos.NN);
//        w9.setPos(Pos.NNS);
//        set3.add(w7);
//        set3.add(w8);
//        set3.add(w9);
//        Word w10 = new Word("buy");
//        Word w11 = new Word("sell");
//        Word w12 = new Word("loans");
//        w7.setPos(Pos.VB);
//        w8.setPos(Pos.VB);
//        w9.setPos(Pos.NNS);
//        Set<Word> set4 = new HashSet<>();
//        set4.add(w10);
//        set4.add(w11);
//        set4.add(w12);
//        List<Pair<Word, Set<Word>>> list2 = new ArrayList();
//        Word o3 = new Word("skeletons");
//        o1.setPos(Pos.NNS);
//        Word o4 = new Word("rents");
//        o2.setPos(Pos.NNS);
//        list2.add(new Pair<>(o3, set3));
//        list2.add(new Pair<>(o4, set4));
//        wordSuggestionsByRhyme.put(new Rhyme(1), new ArrayList<>(list2));

        LyristRhymeMethods.selectModels(wordSuggestionsByRhyme);
//        WordReplacements replacements = filterSuggestions(suggestions, info, rhyming, oldWordsByRhyme);

    }

    public static InfoSong transform(InfoSong originalSong, TransformByAnalogyInfo info, boolean rhyming) {
        //dummy variable
        WordsByRhyme oldWordsByRhyme = new WordsByRhyme();

        if (rhyming) {
            //Get old words by rhyme
            oldWordsByRhyme = SongScanner.getRhymeSchemeWords(originalSong, ((RhymeTransformInfo)info).getRhymeScheme());
        }

        //get word suggestions
        WordsToSuggestions suggestions = getSuggestions(originalSong, info, rhyming, oldWordsByRhyme);

        if (rhyming) {
            //Get word suggestions by rhyme
            WordSuggestionsByRhyme wordSuggestionsByRhyme = LyristRhymeMethods.sortRhymeSuggestionsByRhyme(originalSong.words(), suggestions, oldWordsByRhyme);

            //Choose rhyme models, score suggestion word by rhyme models
            LyristRhymeMethods.selectModels(wordSuggestionsByRhyme);

            //assign rhyme scores to suggestions
            for (Map.Entry<RhymeClass, List<Pair<Word,Set<Word>>>> rhymeClass : wordSuggestionsByRhyme.entrySet()) {
                if (rhymeClass.getKey().getModel() != null)
                    LyristRhymeMethods.scoreSuggestionsByModel(rhymeClass.getKey().getModel(), rhymeClass.getValue());
                else
                    LyristRhymeMethods.clearSuggestionScores(rhymeClass.getValue());
            }

            //For each rhyme that doesn't have a model, remove it from rhyme scheme
            Set<RhymeClass> rhymeClasses = new HashSet<>(oldWordsByRhyme.keySet());
            for (RhymeClass rhymeClass : rhymeClasses) {
                if (rhymeClass.getModel() == null) {
                    oldWordsByRhyme.remove(rhymeClass);
                }
            }

            //For rhyme instances, add cmu rhymes
//            suggestions = LyristRhymeMethods.addCmuRhymes(wordSuggestionsByRhyme, suggestions, originalSong.words());
        }

        //use constraints on suggestions
        WordReplacements replacements = filterSuggestions(suggestions, info, rhyming, oldWordsByRhyme);

        //replace words
        InfoSong transformed = SongMutator.replaceWords(originalSong, replacements);
        transformed.setOldTheme(info.getOldTheme());
        transformed.setNewTheme(info.getNewTheme());

        return transformed;
    }

    public static WordsToSuggestions getSuggestions(InfoSong originalSong, TransformByAnalogyInfo info, boolean rhyming, WordsByRhyme oldWordsByRhyme) {
        try {
            final Set<Word> marked = WordConstraintRunner.useConstraintsByWeakening(info.getMarkingConstraints(), originalSong.words());
            final WordsToSuggestions suggestions = new WordsToSuggestions();
            final List<Word> allOldWords = originalSong.words();

            boolean analogy = true;
            //if new theme is [similar], then find similar instead of analogous words
            if (info.getNewTheme().equalsIgnoreCase("[similar]")) {
                analogy = false;
            }

            //For each word in original song, get replacement suggestions
            for (final Word oldWord : allOldWords) {
                if (oldWord instanceof Punctuation || (!marked.contains(oldWord) && !oldWordsByRhyme.contains(oldWord)) || suggestions.keySet().contains(oldWord))//TODO: make suggestions have a list rather than a set of oldWords?
                    continue;

                Map<Double, String> cosineStrings = new HashMap<>();
                //Add w2v analogy suggestions
                try {
                    if (analogy) {
                        if (rhyming && oldWordsByRhyme.contains(oldWord))
                            cosineStrings.putAll(WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), N_RHYME));
                        else
                            cosineStrings.putAll(WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), N_NORMAL));
                    }
                    else {
                        if (rhyming && oldWordsByRhyme.contains(oldWord))
                            cosineStrings.putAll(WordSource.w2vSimilar(info.getW2v(), oldWord.getLowerSpelling(), N_RHYME));
                        else
                            cosineStrings.putAll(WordSource.w2vSimilar(info.getW2v(), oldWord.getLowerSpelling(), N_NORMAL));
                    }
                }
                catch (BadW2vInputException e) {
                    System.out.print("\t***Bad w2v input: " + oldWord.toString());
                    System.out.println("\t <- this word will not be replaced");
                    e.printStackTrace();
                    continue;
                }

                final Set<Word> cosineWords = cosineStringsToWords(cosineStrings, oldWord);

                //if rhyming, add CMU perfect rhymes for each suggestion
//                if (rhyming) {
//                    Map<Double, String> rhymeStrings = new HashMap<>();
//                    for (Word w : cosineWords) {
//                        try {
//                            rhymeStrings.putAll(WordSource.perfectCmuRhymes(w.getRhymeTail(), 10));
//                        }
//                        catch (NoRhymeFoundException e) {
//                            System.out.println("\t***No rhyme found for suggestion: " + w.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                    cosineWords.addAll(cosineStringsToWords(rhymeStrings, oldWord));
//                }
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
//        U.testPrint("Entered cosineStringsToWords");
        //text, Pos and Ne for each word
        if (stringMapSuggestions == null || stringMapSuggestions.isEmpty()) return null;
        Map<Double, Word> wordMap = StanfordNlp.tagWordsWithSentenceContextWithDoubles(new TreeMap<>(stringMapSuggestions), oldWord);

        //Assign syllables on word2vec's suggestions
        for (Map.Entry<Double, Word> entry : wordMap.entrySet()) {
            entry.getValue().setPronunciations(Phoneticizer.getSyllablesForWord(entry.getValue()));
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

    public static WordReplacements filterSuggestions(final WordsToSuggestions wordsToSuggestions, final TransformByAnalogyInfo info, boolean rhyming, WordsByRhyme oldWordsByRhyme) {//TODO get this to work
        U.testPrint("Started filtering suggestions");
        //Filter suggestions
        final WordReplacements replacements = new WordReplacements();
        final StringReplacements baseReplacements = new StringReplacements();
        for (Map.Entry<Word, Set<Word>> wordToSuggestions : wordsToSuggestions.entrySet()) {
            final Word oldWord = wordToSuggestions.getKey();
            final Set<Word> suggestions = wordToSuggestions.getValue();
            Word chosen;
            WordConstraintRunner.enableAllConstraints(info.getNormalConstraints());

            try {
                //Use rhyme filters
                if (rhyming && oldWordsByRhyme.contains(wordToSuggestions.getKey())) {
                    chosen = WordConstraintRunner.useConstraintsTo1ByWeakening(((RhymeTransformInfo)info).getRhymeConstraints(), oldWord, suggestions);
                }
                else {
                    //Use normal filters
                    WordConstraintRunner.disableRhymeConstraints(info.getNormalConstraints());
                    if (baseReplacements.keySet().contains(oldWord.getBase())) {
                        WordConstraintRunner.enableAllConstraints(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())));
                        chosen = WordConstraintRunner.useConstraintsTo1ByWeakening(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())), oldWord, suggestions);
                    }
                    else
                        chosen = WordConstraintRunner.useConstraintsTo1ByWeakening(info.getNormalConstraints(), oldWord, suggestions);
                }

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
//
//}
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




/*
Fix rhyming functionality before the 20th:
    > Read rhyme IDs of Paul's rhyme format correctly
    @> Ensure rhyme model choice is streamlined
    > Ensure the rest of rhyming is streamlined
    > Break methods down into multiple smaller methods where appropriate
    > Add W2v distance-finding functionality
    > Make words tagged to be rhymes not be connected to the non-rhyme same word in the song; they should be replaced separately.
    > Let words tagged to be rhymes use a different set of filters
    > Maybe mark non-rhymes and rhymes separately, and rhymes should include every Pos, even difficult ones like pronoun.
    > For rhymes, allow pronouns to be replaced by nouns. Figure out other good Pos expansion rules.
 */




























































