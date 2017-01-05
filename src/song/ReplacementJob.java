package song;

import filters.*;
import main.ProgramArgs;
import rhyme.Phoneticizer;
import rhyme.RhymeScheme;
import rhyme.Rhymer;
import stanford_nlp.StanfordNlp;
import utils.Utils;
import word2vec.W2vCommander;

import java.util.*;

public class ReplacementJob {
//TODO eventually make children of this class that represent specifc W2v operation sequences, like [sentiment, similar, analogy, similar]
//TODO or [analogy, similar] or [similar], etc. Maybe, unless there's a smarter way.
// TODO: This class should simply execute a sequence of W2vOperations, interface with Filters, and return WordReplacements or new Words.

    private Word[] temporarySentimentArray = new Word[] {
            new Word("passive"),
            new Word("aggressive"),
            new Word("happy"),
            new Word("energetic"),
            new Word("slow"),
            new Word("fast"),
            new Word("light"),
            new Word("dark"),
            new Word("mysterious"),
            new Word("normal"),
            new Word("strange"),
            new Word("mean"),
            new Word("nice"),
            new Word("hot"),
            new Word("cold"),
            new Word("desert"),
            new Word("city"),
            new Word("crazy"),
            new Word("wild"),
            new Word("civilized"),
            new Word("stoic"),
            new Word("paranoid"),
            new Word("peaceful"),
            new Word("tired"),
            new Word("weary"),
            new Word("brave"),
            new Word("frightened"),
            new Word("popular"),
            new Word("lonely")
    };
    private List<Word> temporarySentimentList = new ArrayList(Arrays.asList(temporarySentimentArray));
    private int nSuggestionsToPrint;

    public Word getExistingSentiment(Set<Word> wordsToAverage, W2vCommander w2v) {
//        Random rand = new Random();
//        return temporarySentimentList.get(rand.nextInt(temporarySentimentList.size()));
        return new Word("sadness");
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

    public Word generateNewSentiment() {
        return new Word("joy");
//        int rnd = Utils.rand.nextInt(15);
//
//        if (rnd == 0)
//        return new Word("sex");
//
//        if (rnd == 1)
//            return new Word("penis");
//
//        if (rnd == 2)
//            return new Word("vagina");
//
//        if (rnd == 3)
//            return new Word("violence");
//
//        if (rnd == 4)
//            return new Word("evil");
//
//        if (rnd == 5)
//            return new Word("Satan");
//
//        if (rnd == 6)
//            return new Word("brimstone");
//
//        if (rnd == 7)
//            return new Word("death");
//
//        if (rnd == 8)
//            return new Word("sexism");
//
//        if (rnd == 9)
//            return new Word("racism");
//
//        if (rnd == 10)
//            return new Word("misogyny");
//
//        if (rnd == 11)
//            return new Word("polygamy");
//
//        if (rnd == 12)
//            return new Word("cult");
//
//        else
//            return new Word("secret");
//        Random rand = new Random();
//        return temporarySentimentList.get(rand.nextInt(temporarySentimentList.size()));
        //Word newSentiment = new Word("energetic");
        //return newSentiment;
        //SongElementStructurer structurer = new SongElementStructurer();
        //return structurer.structureWord(newSentiment, Structure.POS);
        //return StanfordNlp.parseTextCompletelyByString()
    }

    public RhymeScheme getABABRhymeScheme() {
        return new RhymeScheme("A","B","A","B");
    }

    public RhymeScheme getAABBRhymeScheme() {
        return new RhymeScheme("A","A","B","B");
    }

    public StringFilterEquation getNormalStringFilters() {
        StringFilterEquation stringFilters = new StringFilterEquation();

        stringFilters.add(new FilterINTERSECTION());
        stringFilters.add(new CommonStringFilter(Direction.EXCLUDE_MATCH));
        stringFilters.add(new FilterUNION());

//        List list = new ArrayList<Character>();
//        list.add('n');
//        stringFilters.add(new FirstLetterFilter(Direction.EXCLUDE_MATCH, new CharList(list, "x")));
//        stringFilters.add(new FilterUNION());

        stringFilters.add(new BadStringFilter(Direction.INCLUDE_MATCH));
        stringFilters.add(new DistastefulnessFilter(Direction.INCLUDE_MATCH));
        return stringFilters;
    }

    public WordFilterEquation getPosNeSylWordFilters(Word oldWord) {
        WordFilterEquation wordFilters = new WordFilterEquation();
        wordFilters.add(new FilterINTERSECTION());
        wordFilters.add(new PosMatchFilter(Direction.EXCLUDE_MATCH, oldWord));
        wordFilters.add(new FilterUNION());
        wordFilters.add(new NeMatchFilter(Direction.EXCLUDE_MATCH, oldWord));
        wordFilters.add(new PerfectRhymeFilter(Direction.EXCLUDE_MATCH, oldWord));
        return wordFilters;
    }

    public WordFilterEquation getPosNeWordFilters(Word oldWord) {
        WordFilterEquation wordFilters = new WordFilterEquation();
        wordFilters.add(new FilterINTERSECTION());
        wordFilters.add(new PosMatchFilter(Direction.EXCLUDE_MATCH, oldWord));
        wordFilters.add(new FilterUNION());
        wordFilters.add(new NeMatchFilter(Direction.EXCLUDE_MATCH, oldWord));
        return wordFilters;
    }

    public WordFilterEquation getPosWordFilter(Word oldWord) {
        WordFilterEquation wordFilters = new WordFilterEquation();
        wordFilters.add(new PosMatchFilter(Direction.EXCLUDE_MATCH, oldWord));
        return wordFilters;
    }

    public WordReplacements getAnalogousWords(Set<Word> wordsToReplace,
                                              List<Sentence> sentences,
                                              StringFilterEquation stringFilterEquation,
                                              int nOfSuggestions,
                                              W2vCommander w2v,
                                              String oldTheme,
                                              String newTheme
                                              //,WordFilterEquation wordFilterEquation
                                            ) {
        //TODO > make suggestion tagging use the original template sentence with the new word in the old word's place.
        //TODO > if the above technique lets too many wrong words through, change it.

        Utils.testPrintln("Entering getAnalogousWords");
        WordReplacements wordReplacements = new WordReplacements();
//        W2vCommander w2v = new W2vCommander("news2012");
//        W2vCommander w2v = new W2vCommander("GoogleNews-vectors-negative300.bin");
        for(Sentence sentence : sentences) {
            int oldWordIndex = 0;
            for(Word oldWord : sentence) {
                // Only replace words that are marked for replacement
                if (wordsToReplace.contains(oldWord) &&
                        !wordReplacements.containsKey(oldWord) &&
                        oldWord.getClass() != Punctuation.class) {
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
                    Set<String> badStrings = stringFilterEquation.run(new HashSet<>(stringSuggestionMap.values()));
                    stringSuggestionMap.values().removeAll(badStrings);

                    Utils.testPrintln("After string filtration: " + stringSuggestionMap.size() + " valid suggestions");

                    //If rhyming filter is included, add all this word's perfect rhymes to the stringSuggestionMap.
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
                        WordFilterEquation wordFilterEquation = this.getPosNeSylWordFilters(oldWord);
                        Set<Word> badWords = wordFilterEquation.run(new HashSet<>(wordSuggestionMap.values()));
                        wordSuggestionMap.values().removeAll(badWords);
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

                    //Choose the final replacement word
                    Word chosenWord;
                    try {
                        chosenWord = finalWordSuggestions.firstEntry().getValue();

                        //prevent the same suggestion from being used twice
                        while (wordReplacements.containsValue(chosenWord)) {
                            finalWordSuggestions.remove(finalWordSuggestions.firstKey());
                            chosenWord = finalWordSuggestions.firstEntry().getValue();
                        }
                    }
                    catch (NoSuchElementException | NullPointerException e) {
//                e.printStackTrace();
//                System.out.println("oldWord: " + oldWord.toString());
//                System.out.println("Pos: " + oldWord.getPos().toString());
//                System.out.println("filteredInTree size: " + filteredInTree.size());
                        if (ProgramArgs.isTesting()) {
                            chosenWord = new Word("[n/a]");
                            e.printStackTrace();
                        }
                        else
                            chosenWord = oldWord;
                    }

                    //Assign the word replacement
                    wordReplacements.put(oldWord, chosenWord);
                }
                oldWordIndex++;
            }
        }
        return wordReplacements;
    }

    public WordReplacements getAnalogousWords(Set<Word> wordsToReplace,
                                              List<Sentence> sentences,
                                              StringFilterEquation stringFilterEquation,
                                              int nOfSuggestions,
                                              W2vCommander w2v) {
        return this.getAnalogousWords(wordsToReplace,
                                        sentences,
                                        stringFilterEquation,
                                        nOfSuggestions,
                                        w2v,
                                        this.getExistingSentiment(null, null).getSpelling(),
                                        this.generateNewSentiment().getSpelling());
    }

    public int getnSuggestionsToPrint() {
        return nSuggestionsToPrint;
    }

    public void setnSuggestionsToPrint(int nSuggestionsToPrint) {
        this.nSuggestionsToPrint = nSuggestionsToPrint;
    }
}







/*
TODO > Decide between prereq and prereq
 */


/*
TODO > Make a SinglePointOutput subtype for W2vOperations
 */


































































