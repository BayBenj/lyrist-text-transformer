package songtools;

import constraints.BaseConstraint;
import constraints.StringConstraint;
import constraints.WordConstraint;
import constraints.WordConstraintPrioritizer;
import elements.*;
import rhyme.*;
import stanford.StanfordNlp;

import java.util.*;

public abstract class LyristReplacer {

    public static Song normalReplace(SongWrapper songWrapper, NormalReplacementInfo info) {
        Song original = songWrapper.getSong();
        Set<Word> marked = WordConstraintPrioritizer.useConstraintsByWeakening(info.getMarkingConstraints(), original.getAllWords());
        WordReplacements wordReplacements = new WordReplacements();
        StringReplacements baseReplacements = new StringReplacements();
        for (Sentence s : songWrapper.getSentences()) {
            for (int w = 0; w < s.size(); w++) {
                Word oldWord = s.get(w);
                if (oldWord instanceof Punctuation || !marked.contains(oldWord) || wordReplacements.containsKey(oldWord))
                    continue;
                
                Map<Double, String> cosineStrings = WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), 100);
                Set<Word> cosineWords = cosineStringsToWords(cosineStrings, s, oldWord, w);

                Word chosen;
                WordConstraintPrioritizer.enableAllConstraints(info.getWordConstraints());
                if (baseReplacements.keySet().contains(oldWord.getBase()))
                    chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getBaseConstraints(baseReplacements.get(oldWord.getBase())), oldWord, cosineWords);
                
                else 
                    chosen = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getWordConstraints(), oldWord, cosineWords);

                //if no suggestion was successful, don't do a replacement
                if (chosen == null) continue;

                wordReplacements.put(oldWord, chosen);
                baseReplacements.put(oldWord.getBase(), chosen.getBase());
                ((StringConstraint)info.getWordConstraints().get(1)).getObjects().add(chosen.getLowerSpelling());
                ((BaseConstraint)info.getWordConstraints().get(2)).getObjects().add(chosen.getBase());
            }
        }
        return SongMutator.replaceWords(original, wordReplacements);
    }
    
    /*
    Lemma solution: for now, filter out suggestions that are of the wrong lemma.
        Later, build a tool that changes the base lemma into whatever the needed form is (just in case the correct form is not suggested).
     */

    public static Song rhymeReplace(SongWrapper songWrapper, RhymeReplacementInfo info) {
        Song original = songWrapper.getSong();
        WordsByRhyme oldWordsByRhyme = SongScanner.getRhymeSchemeWords(original, info.getRhymeScheme());
        WordsToSuggestions wordsToSuggestions = new WordsToSuggestions();
        Set<Word> marked = WordConstraintPrioritizer.useConstraintsByWeakening(info.getMarkingConstraints(), original.getAllWords());
        for (Sentence s : songWrapper.getSentences())
            for (int w = 0; w < s.size(); w++) {
                Word oldWord = s.get(w);
                if (oldWord instanceof Punctuation || !marked.contains(oldWord) || wordsToSuggestions.containsKey(oldWord)) continue;
                Map<Double, String> cosineStrings = WordSource.w2vAnalogy(info.getW2v(), info.getOldTheme(), info.getNewTheme(), oldWord.getLowerSpelling(), 100);
                cosineStrings.putAll(WordSource.cmuRhymes(oldWord.getUpperSpelling()));
                Set<Word> cosineWords = cosineStringsToWords(cosineStrings, s, oldWord, w);
                wordsToSuggestions.put(oldWord, cosineWords);
            }
        WordSuggestionsByRhyme suggestionsByRhyme = sortRhymeSuggestionsByRhyme(original.getAllWords(), wordsToSuggestions, oldWordsByRhyme);
        List<Word> rhymeModels = getRhymeModelWords(suggestionsByRhyme, info.getWordConstraints(), .75);
        WordReplacements replacements = WordConstraintPrioritizer.useConstraintsTo1ByWeakening(info.getWordConstraints(), wordsToSuggestions);
        return SongMutator.replaceWords(original, replacements);
    }

    public static Set<String> wordsToBases(Collection<Word> words) {
        Set<String> result = new HashSet<>();
        for (Word word : words)
            if (word.getBase() != null)
                result.add(word.getBase());
        return result;
    }
    
    public static Set<Word> cosineStringsToWords(Map<Double, String> stringMapSuggestions,
                                                 Sentence sentence,
                                                 Word oldWord,
                                                 int oldWordIndex) {
        //text, Pos and Ne for each word
        if (stringMapSuggestions == null || stringMapSuggestions.isEmpty()) return null;
        Map<Double, Word> wordMap = StanfordNlp.tagWordsWithSentenceContextWithDoubles(
                new TreeMap<>(stringMapSuggestions),
                sentence,
                oldWord,
                oldWordIndex
        );

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

    private static WordSuggestionsByRhyme sortRhymeSuggestionsByRhyme(List<Word> orderedOldWords, WordsToSuggestions wordsToSuggestions, WordsByRhyme oldWordsByRhyme) {
        //TODO make sure this works
        WordSuggestionsByRhyme result = new WordSuggestionsByRhyme();
        for (Map.Entry<Rhyme, Set<Word>> rhymeClass : oldWordsByRhyme.entrySet()) {
            List<Word> oldWordsInRhymeClass = new ArrayList<>();
            for (Word oldWord : orderedOldWords) {
                if (!rhymeClass.getValue().contains(oldWord)) continue;
                oldWordsInRhymeClass.add(oldWord);
            }
            List<Set<Word>> suggestionsByRhymeInstance = new ArrayList<>();
            for (Word oldWord : oldWordsInRhymeClass)
                suggestionsByRhymeInstance.add(wordsToSuggestions.get(oldWord));
            result.put(rhymeClass.getKey(), suggestionsByRhymeInstance);
        }
        return result;
    }

    private static List<Word> getRhymeModelWords(WordSuggestionsByRhyme wordsToRhyme, List<WordConstraint> nonRhymeFilters, double rhymeThreshold) {
        //get rhyme models for each rhyme class
        List<Word> result = new ArrayList<>();
        for (Map.Entry<Rhyme, List<Set<Word>>> rhymeClass : wordsToRhyme.entrySet()) {
            Word rhymeModel = null;
            while (rhymeModel == null)
                rhymeModel = chooseBestWordForRhyming(rhymeClass.getValue(), nonRhymeFilters, rhymeThreshold);
            result.add(rhymeModel);
        }
        return result;
    }

    private static Word chooseBestWordForRhyming(List<Set<Word>> suggestionSets, List<WordConstraint> nonRhymeFilters, double rhymeThreshold) {
        //Chooses the best word in its rhyme scheme (best = most rhymeable)
        //Attempts to optimize all constraints in choice of rhyming word

        Set<Word> bestNonRhymeWords = new HashSet<>();

        //Pick the best non-rhyming word from each set
        for (Set<Word> suggestionSet : suggestionSets)
            bestNonRhymeWords.add(WordConstraintPrioritizer.useConstraintsTo1ByWeakening(nonRhymeFilters, suggestionSet));

        //Of those filterWords, find the word with the most rhymes
        int wordNum = 0;
        Word bestRhymer = null;
        int mostRhymes = Integer.MIN_VALUE;
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

    private static int getNRhymingWords(Word rhymeModel, Set<Word> words, double rhymeScoreThreshold) {
        int n = 0;
        for (Word word : words)
            if (Rhymer.scoreRhyme(rhymeModel, word) >= rhymeScoreThreshold)
                n++;
        return n;
    }

}

































































