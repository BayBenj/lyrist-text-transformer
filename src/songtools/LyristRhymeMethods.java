package songtools;

import constraints.*;
import elements.Punctuation;
import elements.Word;
import rhyme.*;
import utils.Pair;
import utils.U;

import java.util.*;

public abstract class LyristRhymeMethods {

    public static WordsToSuggestions addCmuRhymes(WordSuggestionsByRhyme wordSuggestionsByRhyme, WordsToSuggestions wordsToSuggestions, List<Word> words) {
        U.testPrint("Entered addCmuRhymes");
        for (final Word oldWord : words) {
            if (oldWord instanceof Punctuation || !wordSuggestionsByRhyme.containsOld(oldWord) || wordSuggestionsByRhyme.getRhymeByOldWord(oldWord).getModel() == null)
                continue;
            final Map<Double, String> cosineStrings = new HashMap<>();
            try {
                RhymeClass rhyme = wordSuggestionsByRhyme.getRhymeByOldWord(oldWord);
                cosineStrings.putAll(WordSource.perfectCmuRhymes(rhyme.getModel(), 100));
                final Set<Word> cosineWords = LyristTransformer.cosineStringsToWords(cosineStrings, oldWord);
                for (Word word : cosineWords)
                    word.setRhymeScore(rhyme.getModel().toString(),1.0);
                wordsToSuggestions.putWord(oldWord, cosineWords);
            } catch (NoRhymeFoundException e) {
                U.testPrint("No rhymes found for: " + oldWord.toString());
                e.printStackTrace();
                cosineStrings.clear();
            }
        }
        return wordsToSuggestions;
    }

    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

    public static WordSuggestionsByRhyme sortRhymeSuggestionsByRhyme(List<Word> orderedOldWords, WordsToSuggestions wordsToSuggestions, WordsByRhyme oldWordsByRhyme) {
        final WordSuggestionsByRhyme result = new WordSuggestionsByRhyme();
        for (Map.Entry<RhymeClass, Set<Word>> rhymeClass : oldWordsByRhyme.entrySet()) {
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

    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

    public static void selectModels(WordSuggestionsByRhyme suggestionsByRhyme) {
        U.testPrint("Entered selectModels");


        //For each rhyme class, choose a model and score every instance according to that model
        for (Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass : suggestionsByRhyme.entrySet()) {

            //For each rhyme instance, filter all suggestions
            LyristRhymeMethods.prefilterRhymeClassSuggestions(rhymeClass);

            List<Pair<Word,Set<Word>>> rhymeInstances = rhymeClass.getValue();
//            U.testPrint("Started adding 10 rhymes in selectModels");
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
            U.testPrint("Started checking rhyme models in selectModels");
            //For each suggestion in the first rhyme instance, try rhyming it with words in other instances
            if (!checkClassAtAllThresholdLevels(rhymeClass, rhymeInstances)) {
                try {
                    throw new NoRhymeModelException();
                }
                catch (NoRhymeModelException e) {
                    U.testPrint("No rhyme model for rhyme " + rhymeClass.getKey().getRhymeId());
                    e.printStackTrace();
                }
            }
            else {
                U.testPrint("Rhyme model " + rhymeClass.getKey().getModel().toString() + " assigned to rhyme class " + rhymeClass.getKey().getRhymeId());
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
        }
    }

    private static boolean checkClassAtAllThresholdLevels(Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass, List<Pair<Word,Set<Word>>> rhymeInstances) {
        final double decrement = .025;
//        final double lowest_acceptable_thresh = 0.95;
        final double lowest_acceptable_thresh = 1.0;
        for (double temp_thresh = 1.0; temp_thresh >= lowest_acceptable_thresh; temp_thresh -= decrement) {
            if (checkAllCandidateModels(rhymeClass, rhymeInstances, temp_thresh)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkAllCandidateModels(Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass, List<Pair<Word,Set<Word>>> rhymeInstances, double thresh) {
        for (Word candidateModel : rhymeInstances.get(0).getSecond()) {
            if (U.isNullOrEmpty(candidateModel.getRhymeTail()))
                continue;

            if (checkAllInstancesWithCandidateModelAtThreshold(candidateModel, rhymeClass, thresh, rhymeInstances)) {
                legitimizeCandidateModel(candidateModel, rhymeClass, thresh);
                return true;
            }
            else {
                throwOutCandidateModel(candidateModel, rhymeClass);
                continue;
            }
        }
        return false;
    }

    private static void legitimizeCandidateModel(Word candidateModel, Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass, double thresh_used) {
        U.testPrint("Rhyme model set to " + candidateModel.toString() + " at score threshold " + thresh_used);
//                            rhymeClass.getKey().addInstance(candidateModel);//TODO is this correct?
        rhymeClass.getKey().setModel(candidateModel.getRhymeTail());
        candidateModel.setRhymeScore(candidateModel.getLowerSpelling(),2.0);
        scoreSuggestionsByModel(candidateModel.getRhymeTail(), rhymeClass.getValue());
    }

    private static void throwOutCandidateModel(Word candidateModel, Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass) {
        rhymeClass.getKey().setModel(null);
        candidateModel.clearRhymeScore();

        //reset all rhyme scores of suggestions in this rhyme class
        for (Pair<Word,Set<Word>> instance : rhymeClass.getValue()) {
            for (Word suggestion : instance.getSecond()) {
                suggestion.clearRhymeScore();
            }
        }
    }

    private static boolean checkAllInstancesWithCandidateModelAtThreshold(Word candidateModel, Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass, double thresh, List<Pair<Word,Set<Word>>> rhymeInstances) {
        U.testPrint("Candidate model: " + candidateModel.getLowerSpelling());
        U.testPrint("Threshold: " + thresh + "\n");

        rhymeClass.getKey().setInstances(new ArrayList<>());

        boolean candidateModelIsGood = true;
        for (int inst = 1; inst < rhymeInstances.size(); inst++) {
            Pair<Word,Set<Word>> instance = rhymeInstances.get(inst);
            Set<Word> instanceSuggestions = instance.getSecond();
            candidateModelIsGood = candidateModelIsGood && !testAllCandidateRhymes(candidateModel, thresh, rhymeClass, instanceSuggestions);
            if (!candidateModelIsGood) return false;
        }
        return true;
    }

    private static boolean testAllCandidateRhymes(Word candidateModel, double thresh, Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass, Set<Word> candidateRhymeInstances) {
        boolean bad = true;
        for (Word candidateRhymeInstance : candidateRhymeInstances) {
            bad = bad && scoreInstanceSuggestionsByCandidate(candidateModel, candidateRhymeInstance, thresh, rhymeClass);
            if (!bad) return false;
        }
        return true;
    }

    private static boolean scoreInstanceSuggestionsByCandidate(Word candidateModel, Word candidateRhymeInstance, double thresh, Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass) {
        boolean bad = true;
        double score;

        if (candidateModel.getLowerSpelling().equals(candidateRhymeInstance.getLowerSpelling())) return true;

//        if (Rhymer.perfectRhymes.containsKey(candidateModel.getRhymeTail()) && Rhymer.perfectRhymes.get(candidateModel.getRhymeTail()).contains(candidateRhymeInstance.getLowerSpelling()))
//            score = 1.0;
//        else
            score = Rhymer.score2Rhymes(candidateModel.getRhymeTail(), candidateRhymeInstance.getRhymeTail());

        if (score >= thresh) {
            U.testPrint("Candidate model [" + candidateModel.getLowerSpelling() + "] rhymes with instance suggestion [" + candidateRhymeInstance.getLowerSpelling() + "]");
            rhymeClass.getKey().addInstance(candidateRhymeInstance);
            bad = false;
        }
        return bad;
    }

    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

    public static void prefilterRhymeClassSuggestions(Map.Entry<RhymeClass,List<Pair<Word,Set<Word>>>> rhymeClass) {
        for (int inst4Model = 0; inst4Model < rhymeClass.getValue().size(); inst4Model++) {
            prefilterRhymeInstanceSuggestions(rhymeClass.getValue().get(inst4Model));
        }
    }

    private static void prefilterRhymeInstanceSuggestions(Pair<Word,Set<Word>> instanceForModel) {
        final Word oldWord = instanceForModel.getFirst();
        final Set<Word> unfilteredSuggestions = instanceForModel.getSecond();
        List<WordConstraint> constraints = WordConstraintMaker.getNormalCmuMulti();
//        WordConstraintRunner.disableUnenforcedConstraints(constraints);
        try {
            final Set<Word> filteredSuggestions = WordConstraintRunner.useConstraintsByWeakening(constraints, oldWord, unfilteredSuggestions);
            instanceForModel.setSecond(filteredSuggestions);
        } catch (EnforcedConstraintException e) {
            e.printStackTrace();
        }
    }

    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

    public static void scoreSuggestionsByModel(SyllableGroup rhymeModel, List<Pair<Word,Set<Word>>> instanceSuggestions) {
        for (Pair<Word,Set<Word>> instance : instanceSuggestions) {
            for (Word w : instance.getSecond()) {
                if (w.getRhymeScore() != 2.0) {
                    double score = Rhymer.score2Rhymes(rhymeModel, w.getRhymeTail());
                    w.setRhymeScore(rhymeModel.toString(), score);
                }
            }
        }
    }

    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

    public static void clearSuggestionScores(List<Pair<Word,Set<Word>>> instanceSuggestions) {
        for (Pair<Word,Set<Word>> instance : instanceSuggestions) {
            for (Word w : instance.getSecond()) {
                w.clearRhymeScore();
            }
        }
    }


//    public static InfoSong rhymeReplace(InfoSong originalSong, RhymeTransformInfo info) {
//        //Get suggestions
//        final WordsByRhyme oldWordsByRhyme = SongScanner.getRhymeSchemeWords(originalSong, info.getRhymeScheme());
//        WordsToSuggestions wordsToSuggestions = new WordsToSuggestions();
//        final Set<Word> marked;
//        try {
//            marked = WordConstraintPrioritizer.useConstraintsByWeakening(info.getMarkingConstraints(), originalSong.words());
//            marked.addAll(oldWordsByRhyme.getAll());
//            final List<Word> allOldWords = originalSong.words();
//            U.testPrint("Started finding word suggestions");
//            for (final Word oldWord : allOldWords) {
//                if (oldWord instanceof Punctuation || (!marked.contains(oldWord) && !oldWordsByRhyme.contains(oldWord)) || wordsToSuggestions.containsKey(oldWord))
//                    continue;
//                final Map<Double, String> cosineStrings;
//                try {
//                    if (oldWordsByRhyme.contains(oldWord))
//                        cosineStrings = WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), 100);
//                    else
//                        cosineStrings = WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), 10);
//                } catch (BadW2vInputException e) {
//                    System.out.println("\t***Bad w2v input: " + oldWord.toString());
//                    e.printStackTrace();
//                    continue;
//                }
//                final Set<Word> cosineWords = LyristTransformer.cosineStringsToWords(cosineStrings, oldWord);
//                wordsToSuggestions.put(oldWord, cosineWords);
//            }
//
//            //Get word suggestions by rhyme
//            WordSuggestionsByRhyme wordSuggestionsByRhyme = sortRhymeSuggestionsByRhyme(originalSong.words(), wordsToSuggestions, oldWordsByRhyme);
//
//            //Choose rhyme models, score suggestion word by rhyme models
//            selectModels(info, wordSuggestionsByRhyme);
//
//            //For rhyme instances, add cmu rhymes
//            wordsToSuggestions = addCmuRhymes(wordSuggestionsByRhyme, wordsToSuggestions, allOldWords);
//
//            U.testPrint("Started filtering suggestions");
//            //Filter suggestions
//            final WordReplacements replacements = new WordReplacements();
//            final StringReplacements baseReplacements = new StringReplacements();
//            for (Map.Entry<Word,Set<Word>> wordToSuggestions : wordsToSuggestions.entrySet()) {
//                final Word oldWord = wordToSuggestions.getKey();
//                final Set<Word> suggestions = wordToSuggestions.getValue();
//                Word chosen;
//                WordConstraintPrioritizer.enableAllConstraints(info.getNormalConstraints());
//                WordConstraintPrioritizer.enableAllConstraints(info.getRhymeConstraints());
//
//                try {
//                    //Use rhyme and normal filters
//                    if (oldWordsByRhyme.contains(wordToSuggestions.getKey())) {
//                            chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getRhymeConstraints(), oldWord, suggestions);
//
//                    }
//                    //Use normal filters
//                    else {
//                        WordConstraintPrioritizer.disableRhymeConstraints(info.getNormalConstraints());
//                        if (baseReplacements.keySet().contains(oldWord.getBase())) {
//                            WordConstraintPrioritizer.enableAllConstraints(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())));
//                            chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())), oldWord, suggestions);
//                        }
//                        else
//                            chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getNormalConstraints(), oldWord, suggestions);
//                    }
//
//                    //if no suggestion was successful, don't do a replacement
//                    if (chosen == null) {
//                        try {
//                            System.out.println("\t***No word replacement for old word: " + oldWord.toString());
//                            throw new NoWordReplacementException();
//                        } catch (NoWordReplacementException e) {
//                            e.printStackTrace();
//                            if (MultiProgramArgs.isDebugMode())
//                                chosen = new Word("[n/a]");
//                            else
//                                continue;
//                        }
//                    }
//                } catch (EnforcedConstraintException e) {
//                    e.printStackTrace();
//                    chosen = new Word("[n/a]");
//                }
//
//                replacements.put(oldWord, chosen);
//                baseReplacements.put(oldWord.getBase(), chosen.getBase());
//                ((StringConstraint)info.getNormalConstraints().get(1)).getObjects().add(chosen.getLowerSpelling());
//                ((BaseConstraint)info.getNormalConstraints().get(2)).getObjects().add(chosen.getBase());
//                ((StringConstraint)info.getRhymeConstraints().get(1)).getObjects().add(chosen.getLowerSpelling());
//                ((BaseConstraint)info.getRhymeConstraints().get(2)).getObjects().add(chosen.getBase());
//            }
//
//            //do replacement
//            return SongMutator.replaceWords(originalSong, replacements);
//        } catch (EnforcedConstraintReturnedZeroException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private static void selectModels(RhymeTransformInfo rInfo, WordSuggestionsByRhyme suggestionsByRhyme) {
//        U.testPrint("Entered selectModels");
//
//
//        //For each rhyme class, choose a model and score every instance according to that model
//        for (Map.Entry<Rhyme,List<Pair<Word,Set<Word>>>> rhymeClass : suggestionsByRhyme.entrySet()) {
//
////            For each rhyme instance, filter all suggestions
//            for (int inst4Model = 0; inst4Model < rhymeClass.getValue().size(); inst4Model++) {
//                final Pair<Word,Set<Word>> instanceForModel = rhymeClass.getValue().get(inst4Model);
//                final Word oldWord = instanceForModel.getFirst();
//                final Set<Word> unfilteredSuggestions = instanceForModel.getSecond();
//                List<WordConstraint> constraints = WordConstraintManager.getNormalCmuMulti();
//                WordConstraintPrioritizer.disableUnenforcedConstraints(constraints);
//                try {
//                    final Set<Word> filteredSuggestions = WordConstraintPrioritizer.useConstraintsByWeakening(constraints, oldWord, unfilteredSuggestions);
//                    instanceForModel.setSecond(filteredSuggestions);
//                } catch (EnforcedConstraintException e) {
//                    e.printStackTrace();
//                }
//            }
//            List<Pair<Word,Set<Word>>> rhymeInstances = rhymeClass.getValue();
//            boolean classHasModel = false;
//
////            U.testPrint("Started adding 10 rhymes in selectModels");
////            //For all but the first instance, add 10 rhymes for each word to the first instance
////            for (int inst = 1; inst < rhymeInstances.size(); inst++) {
////                final Pair<Word,Set<Word>> instance = rhymeInstances.get(inst);
////                for (Word rhymeTest : instance.getSecond()) {
////                    try {
////                        final Set<String> rhymes = Rhymer.getAllRhymesByThreshold(rhymeTest, 1.0, 10);
////                        final Map<Double, String> cosineRhymes = new HashMap<>();
////                        double i = 0.00000001;
////                        for (String rhyme : rhymes) {
////                            cosineRhymes.put(0.0 + i, rhyme);
////                            i += 0.00000001;
////                        }
////                        final Set<Word> rhymeWords = LyristNormalReplacer.cosineStringsToWords(cosineRhymes, instance.getFirst());
////                        rhymeInstances.get(0).getSecond().addAll(rhymeWords);
////                    } catch (NoRhymeFoundException e) {
////                        U.testPrint("No rhyme found for " + rhymeTest.toString());
////                    }
////                }
////            }
//
//            U.testPrint("Started checking rhyme models in selectModels");
//            //While rhyme class lacks a model and thresh is above 0, try candidates with a lowering rhyme score threshold
//            while (!classHasModel) {
//                //For each suggestion in the first rhyme instance, try rhyming it with words in other instances
//                for (Word candidateModel : rhymeInstances.get(0).getSecond()) {
//                    if (U.isNullOrEmpty(candidateModel.getRhymeTail()))
//                        continue;
//                    double thresh = 1.0;
//                    while (thresh > 0.5) {
//                        rhymeClass.getKey().setInstances(new ArrayList<>());
//                        boolean modelIsBad = false;
//
//                        //For each other rhyme instance, try rhyming its words with the candidate model
//                        for (int inst = 1; inst < rhymeInstances.size(); inst++) {
//                            boolean instanceIsBad = true;
//                            final Pair<Word,Set<Word>> instance = rhymeInstances.get(inst);
//                            double score = 0;
////                        while (score < thresh && thresh >= 1.0) {
//                            //For each word in this rhyme instance, score its rhyme with the candidate model
//                            for (Word rhymeTest : instance.getSecond()) {
//                                if (Rhymer.perfectRhymes.containsKey(candidateModel.getRhymeTail()) && Rhymer.perfectRhymes.get(candidateModel.getRhymeTail()).contains(rhymeTest.getLowerSpelling()))
//                                    score = 1.0;
//                                else
//                                    score = Rhymer.score2Rhymes(candidateModel.getRhymeTail(), rhymeTest.getRhymeTail());
//                                rhymeTest.setRhymeScore(null, score);
//                                if (score >= thresh) {
//                                    instanceIsBad = false;
//                                    rhymeClass.getKey().addInstance(rhymeTest);
//                                }
//                            }
////                        }
//                            if (instanceIsBad) {  //try new candidate model
//                                modelIsBad = true;
//                                break;
//                            }
//                        }
//                        if (!modelIsBad) {  //use this model and break
//                            U.testPrint("Rhyme model set to " + candidateModel.toString());
//                            rhymeClass.getKey().setModel(candidateModel.getRhymeTail());
//                            classHasModel = true;
//                            break;
//                        }
//                        thresh -= .025;
//                        U.testPrint("Threshold lowered to " + thresh);
//                    }
//                }
//            }
//
//
//
//
////                //Fill rhymeClassSuggestions
////            Set<Word> rhymeClassSuggestions = new HashSet<>();
////            for (Pair<Word,Set<Word>> instance : rhymeClass.getValue())
////                rhymeClassSuggestions.addAll(instance.getSecond());
////
////            //For each instance of this rhyme class
////            for (Pair<Word,Set<Word>> instanceForModel : rhymeClass.getValue()) {
////                //TODO run non-rhyme filters on this instance's suggestions to get list of candidate model words
////                Word oldWord = instanceForModel.getFirst();
////                Set<Word> candidateModels = instanceForModel.getSecond();
////                Set<Word> filteredSuggestions = WordConstraintPrioritizer.useConstraintByWeakening(WordConstraintManager.getNormalCmuMulti(), oldWord, candidateModels);
////
////                if (U.isNullOrEmpty(filteredSuggestions))
////                    continue;
////
////                int thresh = 1;
////                //For each candidate model from this instance
////                while (thresh > 0) {
////                    boolean modelIsGood = true;
////                    for (Word candidateModel : candidateModels) {
////                        //Score the rhyme between this candidate model and each instance in this rhyme class
////                        modelIsGood = true;
////                        //For each instance of this rhyme class
////                        for (Pair<Word,Set<Word>> instanceForModelTest : rhymeClass.getValue()) {
////                            Set<Word> rhymingSuggestions = instanceForModelTest.getSecond();
////                            boolean noRhymeInInstance = true;
////
////                            //For each suggestion
////                            for (Word rhymingSuggestion : rhymingSuggestions) {
////                                if (!Phoneticizer.cmuDictContains(rhymingSuggestion.getUpperSpelling()) ||
////                                        !Phoneticizer.cmuDictContains(candidateModel.getUpperSpelling()) ||
////                                        U.isNullOrEmpty(rhymingSuggestion.getRhymeTail()) ||
////                                        U.isNullOrEmpty(candidateModel.getRhymeTail())) continue;
//////                                if (candidateModel.getLowerSpelling().equals(rhymingSuggestion.getLowerSpelling())) {
//////                                    System.out.println("stop for testing");
//////                                }
////                                double score = Rhymer.score2Rhymes(candidateModel.getRhymeTail(), rhymingSuggestion.getRhymeTail());
////                                rhymingSuggestion.setRhymeScore(score);
////                                if (score >= thresh)
////                                    noRhymeInInstance = false;
////                            }
////                            if (noRhymeInInstance) {
////                                modelIsGood = false;
////                                break;
////                            }
////                        }
////                        if (modelIsGood) {
////                            rhymeClass.getKey().setModel(candidateModel);
////                            break;
////                        }
////                    }
////                    if (modelIsGood)
////                        break;
////                    thresh -= .1;
////                }
////
////                //TODO If all are successful, save this model and break to next rhyme class.
////                //TODO Else continue to next candidate model word.
////            }
////            Rhyme rhyme = rhymeClass.getKey();
////            List<Pair<Word,Set<Word>>> instances = rhymeClass.getValue();
////            //For each rhyme instance
////            for (Pair<Word,Set<Word>> instance : instances) {
////                Word model = instance.getFirst();
////                Set<Word> instanceSuggestions = instance.getSecond();
////                //Score each instance suggestion by rhyme w/ the model
////                for (Word suggestion : instanceSuggestions) {
////                    double score = Rhymer.score2Rhymes(model.getRhymeTail(), suggestion.getRhymeTail());
////                    suggestion.setRhymeScore(score);
////                }
////                //Filter suggestions
////                Word chosenModel = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(rInfo.getNormalConstraints(), instance.getFirst(), instance.getSecond());
////                if (chosenModel == null || chosenModel.getRhymeScore() < .75)
////                    continue;
////                rhyme.setModel(chosenModel);
////                break;
////            }
//            if (rhymeClass.getKey().getModel() == null) {
//                try {
//                    throw new NoRhymeModelException();
//                } catch (NoRhymeModelException e) {
//                    U.testPrint("No rhyme model for rhyme " + rhymeClass.getKey().getRhymeId());
//                    e.printStackTrace();
//                }
//            }
//        }
//        /*
//        Each rhyme class needs a model word.
//        > Make a java class rhyme that takes a Word in its constructor
//        > Make a Constraint type that constrains a collection of words based on their scoring with the ModeledRhyme's Word
//
//        > Make the first word in the Rhyme Class the model. Run constraints. If all pass, return these wordReplacements. If not, iterate to the second word in the Rhyme Class, etc.
//         */
//    }
//
//
//    /*
//    Lemma solution: for now, filter out suggestions that are of the wrong lemma.
//        Later, build a tool that changes the base lemma into whatever the needed form is (just in case the correct form is not suggested).
//     */
//
//    /*
//    Search a corpus of song lyrics for the oldWord's frequency. If it's high, filter out suggestions below a certain frequency. If it's low, filter out suggestions above a certain frequency.
//     */
//
//
//    private static WordsToSuggestions addCmuRhymes(WordSuggestionsByRhyme wordSuggestionsByRhyme, WordsToSuggestions wordsToSuggestions, List<Word> words) {
//        U.testPrint("Entered addCmuRhymes");
//        for (final Word oldWord : words) {
//            if (oldWord instanceof Punctuation || !wordSuggestionsByRhyme.containsOld(oldWord) || wordSuggestionsByRhyme.getRhymeByOldWord(oldWord).getModel() == null)
//                continue;
//            final Map<Double, String> cosineStrings = new HashMap<>();
//            try {
//                Rhyme rhyme = wordSuggestionsByRhyme.getRhymeByOldWord(oldWord);
//                cosineStrings.putAll(WordSource.imperfectCmuRhymes(rhyme.getModel(), 100));
//                final Set<Word> cosineWords = LyristTransformer.cosineStringsToWords(cosineStrings, oldWord);
//                wordsToSuggestions.putWord(oldWord, cosineWords);
//            } catch (NoRhymeFoundException e) {
//                U.testPrint("No rhymes found for: " + oldWord.toString());
//                e.printStackTrace();
//                cosineStrings.clear();
//            }
//        }
//        return wordsToSuggestions;
//    }
//
//    private static WordSuggestionsByRhyme sortRhymeSuggestionsByRhyme(List<Word> orderedOldWords, WordsToSuggestions wordsToSuggestions, WordsByRhyme oldWordsByRhyme) {
//        final WordSuggestionsByRhyme result = new WordSuggestionsByRhyme();
//        for (Map.Entry<Rhyme, Set<Word>> rhymeClass : oldWordsByRhyme.entrySet()) {
//            final List<Word> oldWordsInRhymeClass = new ArrayList<>();
//            for (Word oldWord : orderedOldWords) {
//                if (!rhymeClass.getValue().contains(oldWord)) continue;
//                oldWordsInRhymeClass.add(oldWord);
//            }
//            final List<Pair<Word,Set<Word>>> suggestionsByRhymeInstance = new ArrayList<>();
//            for (Word oldWord : oldWordsInRhymeClass)
//                suggestionsByRhymeInstance.add(new Pair(oldWord, wordsToSuggestions.get(oldWord)));
//            result.put(rhymeClass.getKey(), suggestionsByRhymeInstance);
//        }
//        return result;
//    }

}














































































































































