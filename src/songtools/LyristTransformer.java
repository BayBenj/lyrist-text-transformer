package songtools;

import constraints.*;
import elements.*;
import main.MultiProgramArgs;
import rhyme.*;
import stanford.StanfordNlp;
import utils.Pair;
import utils.U;
import word2vec.BadW2vInputException;
import java.util.*;

public abstract class LyristTransformer {

    public static InfoSong transform(InfoSong originalSong, NormalTransformInfo info, boolean rhyming) {

        //get word suggestions
        WordsToSuggestions suggestions = getSuggestions(originalSong, info, rhyming);

        //dummy variable
        WordsByRhyme oldWordsByRhyme = new WordsByRhyme();

        if (rhyming) {
            //Get old words by rhyme
            oldWordsByRhyme = SongScanner.getRhymeSchemeWords(originalSong, ((RhymeTransformInfo)info).getRhymeScheme());

            //Get word suggestions by rhyme
            WordSuggestionsByRhyme wordSuggestionsByRhyme = sortRhymeSuggestionsByRhyme(originalSong.words(), suggestions, oldWordsByRhyme);

            //Choose rhyme models, score suggestion word by rhyme models
            selectModelsAndScoreRhymes((RhymeTransformInfo)info, wordSuggestionsByRhyme);

            //For rhyme instances, add cmu rhymes
            suggestions = addCmuRhymes(wordSuggestionsByRhyme, suggestions, originalSong.words());
        }

        //use constraints on suggestions
        WordReplacements replacements = filterSuggestions(suggestions, info, rhyming, oldWordsByRhyme);

        //replace words
        InfoSong transformed = SongMutator.replaceWords(originalSong, replacements);
        transformed.setOldTheme(info.getOldTheme());
        transformed.setNewTheme(info.getNewTheme());

        return transformed;
    }

    public static WordsToSuggestions getSuggestions(InfoSong originalSong, NormalTransformInfo info, boolean rhyming) {
        try {
            final Set<Word> marked = WordConstraintPrioritizer.useConstraintsByWeakening(info.getMarkingConstraints(), originalSong.words());
            final WordsToSuggestions suggestions = new WordsToSuggestions();
            final List<Word> allOldWords = originalSong.words();
            WordsByRhyme oldWordsByRhyme = new WordsByRhyme();
            if (rhyming)
                oldWordsByRhyme = SongScanner.getRhymeSchemeWords(originalSong, ((RhymeTransformInfo)info).getRhymeScheme());

            //For each word in original song, get replacement suggestions
            for (final Word oldWord : allOldWords) {
                if (oldWord instanceof Punctuation || !marked.contains(oldWord) || suggestions.keySet().contains(oldWord))//TODO: make suggestions have a list rather than a set of oldWords?
                    continue;

                Map<Double, String> cosineStrings = new HashMap<>();
                //Add w2v analogy suggestions
                try {
                    if (rhyming && oldWordsByRhyme.contains(oldWord))
                        cosineStrings.putAll(WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), 100));
                    else
                        cosineStrings.putAll(WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), 30));
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

    public static WordReplacements filterSuggestions(final WordsToSuggestions wordsToSuggestions, final NormalTransformInfo info, boolean rhyming, WordsByRhyme oldWordsByRhyme) {//TODO get this to work
        U.testPrint("Started filtering suggestions");
        //Filter suggestions
        final WordReplacements replacements = new WordReplacements();
        final StringReplacements baseReplacements = new StringReplacements();
        for (Map.Entry<Word, Set<Word>> wordToSuggestions : wordsToSuggestions.entrySet()) {
            final Word oldWord = wordToSuggestions.getKey();
            final Set<Word> suggestions = wordToSuggestions.getValue();
            Word chosen;
            WordConstraintPrioritizer.enableAllConstraints(info.getNormalConstraints());

            try {
                //Use rhyme filters
                if (rhyming && oldWordsByRhyme.contains(wordToSuggestions.getKey())) {
                    chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(((RhymeTransformInfo)info).getRhymeConstraints(), oldWord, suggestions);

                }
                else {
                    //Use normal filters
                    WordConstraintPrioritizer.disableRhymeConstraints(info.getNormalConstraints());
                    if (baseReplacements.keySet().contains(oldWord.getBase())) {
                        WordConstraintPrioritizer.enableAllConstraints(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())));
                        chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())), oldWord, suggestions);
                    }
                    else
                        chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getNormalConstraints(), oldWord, suggestions);
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

    private static WordSuggestionsByRhyme sortRhymeSuggestionsByRhyme(List<Word> orderedOldWords, WordsToSuggestions wordsToSuggestions, WordsByRhyme oldWordsByRhyme) {
        final WordSuggestionsByRhyme result = new WordSuggestionsByRhyme();
        for (Map.Entry<Rhyme, Set<Word>> rhymeClass : oldWordsByRhyme.entrySet()) {
            final List<Word> oldWordsInRhymeClass = new ArrayList<>();
            for (Word oldWord : orderedOldWords) {
                if (!rhymeClass.getValue().contains(oldWord)) continue;
                oldWordsInRhymeClass.add(oldWord);
            }
            final List<Pair<Word,Set<Word>>> suggestionsByRhymeInstance = new ArrayList<>();
            for (Word oldWord : oldWordsInRhymeClass)
                suggestionsByRhymeInstance.add(new Pair(oldWord, wordsToSuggestions.get(oldWord)));
            result.put(rhymeClass.getKey(), suggestionsByRhymeInstance);
        }
        return result;
    }

    private static void selectModelsAndScoreRhymes(RhymeTransformInfo rInfo, WordSuggestionsByRhyme suggestionsByRhyme) {
        U.testPrint("Entered selectModelsAndScoreRhymes");


        //For each rhyme class, choose a model and score every instance according to that model
        for (Map.Entry<Rhyme,List<Pair<Word,Set<Word>>>> rhymeClass : suggestionsByRhyme.entrySet()) {

//            For each rhyme instance, filter all suggestions
            for (int inst4Model = 0; inst4Model < rhymeClass.getValue().size(); inst4Model++) {
                final Pair<Word,Set<Word>> instanceForModel = rhymeClass.getValue().get(inst4Model);
                final Word oldWord = instanceForModel.getFirst();
                final Set<Word> unfilteredSuggestions = instanceForModel.getSecond();
                List<WordConstraint> constraints = WordConstraintManager.getNormalCmuMulti();
                WordConstraintPrioritizer.disableUnenforcedConstraints(constraints);
                try {
                    final Set<Word> filteredSuggestions = WordConstraintPrioritizer.useConstraintsByWeakening(constraints, oldWord, unfilteredSuggestions);
                    instanceForModel.setSecond(filteredSuggestions);
                } catch (EnforcedConstraintException e) {
                    e.printStackTrace();
                }
            }
            List<Pair<Word,Set<Word>>> rhymeInstances = rhymeClass.getValue();
            boolean classHasModel = false;

//            U.testPrint("Started adding 10 rhymes in selectModelsAndScoreRhymes");
//            //For all but the first instance, add 10 rhymes for each word to the first instance
//            for (int inst = 1; inst < rhymeInstances.size(); inst++) {
//                final Pair<Word,Set<Word>> instance = rhymeInstances.get(inst);
//                for (Word rhymeTest : instance.getSecond()) {
//                    try {
//                        final Set<String> rhymes = Rhymer.getAllRhymesByThreshold(rhymeTest, 1.0, 10);
//                        final Map<Double, String> cosineRhymes = new HashMap<>();
//                        double i = 0.00000001;
//                        for (String rhyme : rhymes) {
//                            cosineRhymes.put(0.0 + i, rhyme);
//                            i += 0.00000001;
//                        }
//                        final Set<Word> rhymeWords = LyristNormalReplacer.cosineStringsToWords(cosineRhymes, instance.getFirst());
//                        rhymeInstances.get(0).getSecond().addAll(rhymeWords);
//                    } catch (NoRhymeFoundException e) {
//                        U.testPrint("No rhyme found for " + rhymeTest.toString());
//                    }
//                }
//            }

            U.testPrint("Started checking rhyme models in selectModelsAndScoreRhymes");
            //While rhyme class lacks a model and thresh is above 0, try candidates with a lowering rhyme score threshold
            while (!classHasModel) {
                //For each suggestion in the first rhyme instance, try rhyming it with words in other instances
                for (Word candidateModel : rhymeInstances.get(0).getSecond()) {
                    if (U.isNullOrEmpty(candidateModel.getRhymeTail()))
                        continue;
                    double thresh = 1.0;
                    while (thresh > 0.5) {
                        rhymeClass.getKey().setInstances(new ArrayList<>());
                        boolean modelIsBad = false;

                        //For each other rhyme instance, try rhyming its words with the candidate model
                        for (int inst = 1; inst < rhymeInstances.size(); inst++) {
                            boolean instanceIsBad = true;
                            final Pair<Word,Set<Word>> instance = rhymeInstances.get(inst);
                            double score = 0;
//                        while (score < thresh && thresh >= 1.0) {
                            //For each word in this rhyme instance, score its rhyme with the candidate model
                            for (Word rhymeTest : instance.getSecond()) {
                                if (Rhymer.perfectRhymes.containsKey(candidateModel.getRhymeTail()) && Rhymer.perfectRhymes.get(candidateModel.getRhymeTail()).contains(rhymeTest.getLowerSpelling()))
                                    score = 1.0;
                                else
                                    score = Rhymer.score2Rhymes(candidateModel.getRhymeTail(), rhymeTest.getRhymeTail());
                                rhymeTest.setRhymeScore(score);
                                if (score >= thresh) {
                                    instanceIsBad = false;
                                    rhymeClass.getKey().addInstance(rhymeTest);
                                }
                            }
//                        }
                            if (instanceIsBad) {  //try new candidate model
                                modelIsBad = true;
                                break;
                            }
                        }
                        if (!modelIsBad) {  //use this model and break
                            U.testPrint("Rhyme model set to " + candidateModel.toString());
                            rhymeClass.getKey().setModel(candidateModel.getRhymeTail());
                            classHasModel = true;
                            break;
                        }
                        thresh -= .025;
                        U.testPrint("Threshold lowered to " + thresh);
                    }
                }
            }




//                //Fill rhymeClassSuggestions
//            Set<Word> rhymeClassSuggestions = new HashSet<>();
//            for (Pair<Word,Set<Word>> instance : rhymeClass.getValue())
//                rhymeClassSuggestions.addAll(instance.getSecond());
//
//            //For each instance of this rhyme class
//            for (Pair<Word,Set<Word>> instanceForModel : rhymeClass.getValue()) {
//                //TODO run non-rhyme filters on this instance's suggestions to get list of candidate model words
//                Word oldWord = instanceForModel.getFirst();
//                Set<Word> candidateModels = instanceForModel.getSecond();
//                Set<Word> filteredSuggestions = WordConstraintPrioritizer.useConstraintByWeakening(WordConstraintManager.getNormalCmuMulti(), oldWord, candidateModels);
//
//                if (U.isNullOrEmpty(filteredSuggestions))
//                    continue;
//
//                int thresh = 1;
//                //For each candidate model from this instance
//                while (thresh > 0) {
//                    boolean modelIsGood = true;
//                    for (Word candidateModel : candidateModels) {
//                        //Score the rhyme between this candidate model and each instance in this rhyme class
//                        modelIsGood = true;
//                        //For each instance of this rhyme class
//                        for (Pair<Word,Set<Word>> instanceForModelTest : rhymeClass.getValue()) {
//                            Set<Word> rhymingSuggestions = instanceForModelTest.getSecond();
//                            boolean noRhymeInInstance = true;
//
//                            //For each suggestion
//                            for (Word rhymingSuggestion : rhymingSuggestions) {
//                                if (!Phoneticizer.cmuDictContains(rhymingSuggestion.getUpperSpelling()) ||
//                                        !Phoneticizer.cmuDictContains(candidateModel.getUpperSpelling()) ||
//                                        U.isNullOrEmpty(rhymingSuggestion.getRhymeTail()) ||
//                                        U.isNullOrEmpty(candidateModel.getRhymeTail())) continue;
////                                if (candidateModel.getLowerSpelling().equals(rhymingSuggestion.getLowerSpelling())) {
////                                    System.out.println("stop for testing");
////                                }
//                                double score = Rhymer.score2Rhymes(candidateModel.getRhymeTail(), rhymingSuggestion.getRhymeTail());
//                                rhymingSuggestion.setRhymeScore(score);
//                                if (score >= thresh)
//                                    noRhymeInInstance = false;
//                            }
//                            if (noRhymeInInstance) {
//                                modelIsGood = false;
//                                break;
//                            }
//                        }
//                        if (modelIsGood) {
//                            rhymeClass.getKey().setModel(candidateModel);
//                            break;
//                        }
//                    }
//                    if (modelIsGood)
//                        break;
//                    thresh -= .1;
//                }
//
//                //TODO If all are successful, save this model and break to next rhyme class.
//                //TODO Else continue to next candidate model word.
//            }
//            Rhyme rhyme = rhymeClass.getKey();
//            List<Pair<Word,Set<Word>>> instances = rhymeClass.getValue();
//            //For each rhyme instance
//            for (Pair<Word,Set<Word>> instance : instances) {
//                Word model = instance.getFirst();
//                Set<Word> instanceSuggestions = instance.getSecond();
//                //Score each instance suggestion by rhyme w/ the model
//                for (Word suggestion : instanceSuggestions) {
//                    double score = Rhymer.score2Rhymes(model.getRhymeTail(), suggestion.getRhymeTail());
//                    suggestion.setRhymeScore(score);
//                }
//                //Filter suggestions
//                Word chosenModel = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(rInfo.getNormalConstraints(), instance.getFirst(), instance.getSecond());
//                if (chosenModel == null || chosenModel.getRhymeScore() < .75)
//                    continue;
//                rhyme.setModel(chosenModel);
//                break;
//            }
            if (rhymeClass.getKey().getModel() == null) {
                try {
                    throw new NoRhymeModelException();
                } catch (NoRhymeModelException e) {
                    U.testPrint("No rhyme model for rhyme " + rhymeClass.getKey().getRhymeId());
                    e.printStackTrace();
                }
            }
        }
        /*
        Each rhyme class needs a model word.
        > Make a java class rhyme that takes a Word in its constructor
        > Make a Constraint type that constrains a collection of words based on their scoring with the ModeledRhyme's Word

        > Make the first word in the Rhyme Class the model. Run constraints. If all pass, return these wordReplacements. If not, iterate to the second word in the Rhyme Class, etc.
         */
    }

    private static WordsToSuggestions addCmuRhymes(WordSuggestionsByRhyme wordSuggestionsByRhyme, WordsToSuggestions wordsToSuggestions, List<Word> words) {
        U.testPrint("Entered addCmuRhymes");
        for (final Word oldWord : words) {
            if (oldWord instanceof Punctuation || !wordSuggestionsByRhyme.containsOld(oldWord) || wordSuggestionsByRhyme.getRhymeByOldWord(oldWord).getModel() == null)
                continue;
            final Map<Double, String> cosineStrings = new HashMap<>();
            try {
                Rhyme rhyme = wordSuggestionsByRhyme.getRhymeByOldWord(oldWord);
                cosineStrings.putAll(WordSource.imperfectCmuRhymes(rhyme.getModel(), 100));
                final Set<Word> cosineWords = LyristTransformer.cosineStringsToWords(cosineStrings, oldWord);
                wordsToSuggestions.putWord(oldWord, cosineWords);
            } catch (NoRhymeFoundException e) {
                U.testPrint("No rhymes found for: " + oldWord.toString());
                e.printStackTrace();
                cosineStrings.clear();
            }
        }
        return wordsToSuggestions;
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




/*
Fix rhyming functionality before the 20th:
    > Read rhyme IDs of Paul's rhyme format correctly
    > Ensure rhyme model choice is streamlined
    > Ensure the rest of rhyming is streamlined
    > Break methods down into multiple smaller methods where appropriate
    > Add W2v distance-finding functionality
    > Make words tagged to be rhymes not be connected to the non-rhyme same word in the song; they should be replaced separately.
    > Let words tagged to be rhymes use a different set of filters
    > Maybe mark non-rhymes and rhymes separately, and rhymes should include every Pos, even difficult ones like pronoun.
    > For rhymes, allow pronouns to be replaced by nouns. Figure out other good Pos expansion rules.
 */

































































































