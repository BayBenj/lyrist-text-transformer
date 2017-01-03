package song;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import filters.*;
import rhyme.Phoneme;
import rhyme.Phoneticizer;
import rhyme.Pronunciation;
import rhyme.StressedPhone;
import stanford_nlp.StanfordNlp;
import utils.Utils;

import java.io.*;
import java.util.*;

import static com.sun.tools.classfile.Opcode.get;
import static edu.stanford.nlp.util.Timing.endTime;

public final class TemplateSongEngineer extends SongEngineer {

    //TODO if a Pop*-defined structure is passed in, find a template song using that structure. Otherwise, pick any template song.
    @Override
    public Song generateSong(
            //Inspiration inspiration
    ) {

        Utils.startTimer();

        //Read in original song lyrics, set up a SongWrapper and a Song object for the template.
        String rawTemplateText = this.readTemplate("sorrow-short.txt");
        StanfordNlp stanford = Utils.getStanfordNlp();
//        List<List<CoreLabel>> stanfordSentences = stanford.parseTextCompletelyByString(rawTemplateText);
//        ArrayList<Sentence> parsedSentences = this.stanfordSentencesToSentences(stanfordSentences);
//        stanford.parseTextCompletelyByPath("let-it-be.txt");

        List<Sentence> parsedSentences = stanford.parseTextToSentences(rawTemplateText);
        setPronunciationsForSentences(parsedSentences);
        SongWrapper templateSongWrapper = this.sentencesToSongWrapper(rawTemplateText, parsedSentences);
        Song templateSong = templateSongWrapper.getSong();

        //Filter out unsafe words so they can't be marked
        WordFilterEquation wordFilterEquation = new WordFilterEquation();
        wordFilterEquation.add(new FilterINTERSECTION());
        wordFilterEquation.add(new UnsafePosFilter(Direction.EXCLUDE_MATCH));
        List<Word> allMarkableWordsList = new ArrayList<>(wordFilterEquation.run(new HashSet(templateSong.getAllWords())));

        //Mark random markable words from template
        Set<Integer> allWordIndexes = new HashSet<Integer>();
        for (int i = 0; i < allMarkableWordsList.size(); i++)
            allWordIndexes.add(i);
        HashSet<Integer> markedIndexes = this.getRandomIndexes(allWordIndexes, 1);
        Set<Word> markedWords = new HashSet<Word>();
        for (int index : markedIndexes)
            markedWords.add(allMarkableWordsList.get(index));

        //Replace marked words in template w/ word2vec
        ReplacementJob replacementJob = new ReplacementJob();
        replacementJob.setnSuggestionsToPrint(10);
        WordReplacements wordReplacements = replacementJob.getAnalogousWords(markedWords,
                parsedSentences,
                replacementJob.getNormalStringFilters(),
                10,
                Utils.getW2vCommander());
        Song generatedSong = this.replaceWords(templateSong, wordReplacements);

        //Do my own fixes on the text
        this.manageSongText(generatedSong);

        //Print both songs out
        Utils.printSideBySide(templateSong, generatedSong);

        Utils.stopTimer();
        print("TOTAL RUNNING TIME: " + Utils.getTotalTime() + "\n");
        return generatedSong;
    }

    private void setPronunciationsForSentences(List<Sentence> sentences) {
        for (Sentence sentence : sentences) {
            for (Word word : sentence) {
                word.setPronunciation(getPronunciationForWord(word));
            }
        }
    }

    private Pronunciation getPronunciationForWord(String string) {
        if (string != null && string.length() > 0 && string.matches("\\w+")) {
            return Phoneticizer.getTopPronunciation(string.toUpperCase());
        }
        return null;
    }

    private Pronunciation getPronunciationForWord(Word word) {
        if (word.getClass() != Punctuation.class) {
            return this.getPronunciationForWord(word.getSpelling());
        }
        return null;
    }

    private void manageSongText(Song generatedSong) {
        // Fix indefinite articles
        this.fixAllIndefiniteArticles(generatedSong);

        // Capitalize first word of every line
        this.capitalizeFirstWordsInLines(generatedSong);
    }

    private ArrayList<Sentence> stanfordSentencesToSentences(List<List<CoreLabel>> stanfordSentences) {
        ArrayList<Sentence> resultSentences = new ArrayList<Sentence>();
        for (List<CoreLabel> sentence : stanfordSentences) {
            Sentence resultSentence = new Sentence();
            for (CoreLabel token : sentence) {
                String spelling = token.get(CoreAnnotations.TextAnnotation.class);
                Word resultWord = new Word(spelling);
                resultWord.setPos(Pos.valueOf(token.get(CoreAnnotations.PartOfSpeechAnnotation.class)));
                resultWord.setNe(NamedEntity.valueOf(token.get(CoreAnnotations.NamedEntityTagAnnotation.class)));
                resultSentence.add(resultWord);
            }
            resultSentences.add(resultSentence);
        }
        return resultSentences;
    }

    private void lowercaseAllWords(Song generatedSong) {
        List<Word> words = generatedSong.getAllWords();
        for (Word word : words) {
            String spelling = word.getSpelling();
            word.setSpelling(spelling.toLowerCase());
        }
    }

    private void capitalizeFirstWordsInLines(Song generatedSong) {
        List<SongElement> lines = generatedSong.getAllSubElementsOfType(new Line());
        for (SongElement songElement : lines) {
            Line line = (Line) songElement;
            Word firstWord = line.getWords().get(0);
            this.capitalizeWord(firstWord);
        }
    }

//    //@Override
//    private TemplateSong getTemplateLyrics(Inspiration inspiration) {
//        // SegmentKey is the type of stanza you're generating
//        // segmentSubstructures are the constraints that you wanna satisfy
//        // inspirationCHECK is the mood of the stanza
//
//        return readInLyrics("terrible-love-short.txt");
//
//        //SparseSingleOrderMarkovModel<Lyric> segmentSpecificMM = mModel.get(segmentKey);
//    }

//    //TODO > having a Song have lyrics instead of Strings is a pain in the neck
//    private static void tryRhymes(LyricSegment lyrics) {
//        //ArrayList<ArrayList<String>> segmentString = new ArrayList<ArrayList<String>>();
//        ArrayList<int[]> rhymeSchemes = new ArrayList<int[]>();
//        for (int i = 0; i < lyrics.getLines().size(); i++) {
//            List<Lyric> line = lyrics.getLine(i);
//            ArrayList<String> lineString = new ArrayList<String>();
//            for (int j = 0; j < lyrics.getLine(i).size(); j++) {
//                Lyric lyric = line.get(j);
//                String lyricString = lyric.toString();
//                lineString.add(lyricString);
//            }
//            int[] lineRhymeScheme = RhymeStructureAnalyzer.extractRhymeScheme(lineString);
//            rhymeSchemes.add(lineRhymeScheme);
//        }
//
//    }

//    private static void setReplacementFrequency(int n) {
//        replacementFrequency = n;
//    }

//    private static TemplateSong readInLyrics(String songFile) {
//        ArrayList<ArrayList<String>> strings = new ArrayList<ArrayList<String>>();
//        try {
//            BufferedReader br;
////            br = new BufferedReader(new FileReader("../../../songs/"));
//            //TODO: fix this so it works on any computer
//            br = new BufferedReader(new FileReader("/Users/Benjamin/Desktop/development/lyrist/songs/" + songFile));
//            String line = br.readLine();
//
//            while (line != null) {
//                String[] splitLine = line.split("\\s");
//                ArrayList<String> tempLine = new ArrayList<String>();
//                //TODO sloppy: change this somehow
//                for (int i = 0; i < splitLine.length; i++) {
//                    tempLine.add(new String(splitLine[i]));
//                }
//                strings.add(tempLine);
//                line = br.readLine();
//            }
//            br.close();
//            return new TemplateSong(strings);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void fixAllIndefiniteArticles(SongElement se) {
        List<Word> words = se.getAllWords();
        for (int i = 0; i < words.size(); i++) {
            if (            i < words.size() - 1 &&
                            (words.get(i).getSpelling().equals("a") ||
                            words.get(i).getSpelling().equals("an"))) {
                this.mutateIndefiniteArticle(words.get(i), words.get(i + 1));
            }
        }
    }

    public void mutateIndefiniteArticle(Word article, Word following) {
        List<Phoneme> phonemes = following.getPhonemes();
        String spelling = following.getSpelling();
        if (phonemes != null && phonemes.size() > 0) {
            Phoneme first = phonemes.get(0);
            if (first.isVowel())
                article.setSpelling("an");
            else
                article.setSpelling("a");
        }
        else if (spelling != null && spelling.length() > 0) {
            char firstChar = spelling.charAt(0);
            if (    firstChar == 'a' ||
                    firstChar == 'e' ||
                    firstChar == 'i' ||
                    firstChar == 'o' ||
                    firstChar == 'u' )
                article.setSpelling("an");
            else
                article.setSpelling("a");
        }
    }

    public void capitalizeWord(Word word) {
        StringBuilder sb = new StringBuilder(word.getSpelling());
        char firstChar = sb.charAt(0);
        String upperStr = Character.toString(Character.toUpperCase(firstChar));
        sb.replace(0, 1, upperStr);
        word.setSpelling(sb.toString());
    }

    public HashSet<Integer> getRandomIndexes(Set<Integer> originalIndexes, double replacement_frequency) {
        Utils.testPrintln("Entering getRandomIndexes");

//        //Make whiteListIndexes
//        List<Integer> whiteListIndexes = new ArrayList<Integer>();
//        for (int i = 0; i < originalIndexes.size(); i++)
//            whiteListIndexes.add(i);
//
//        //Make resultIndexes
//        HashSet<Integer> resultIndexes = new HashSet<Integer>();
//
//        //Remove blacklisted indexes from whiteListIndexes
//        for (int i = 0; i < originalIndexes.size(); i++) {
//            Word w = originalIndexes.get(i);
//            Pos pos = w.getPos();
//            String spelling = w.getSpelling().toLowerCase();
//            if (
////                    spelling.equals("it's") ||
////                    spelling.equals("i'm") ||
////                    spelling.equals("i've") ||
////                    spelling.equals("i'll") ||
////                    spelling.equals("you're") ||
////                    spelling.equals("you've") ||
////                    spelling.equals("you'll") ||
////                    spelling.equals("we're") ||
////                    spelling.equals("we've") ||
////                    spelling.equals("we'll") ||
////                    spelling.equals("they're") ||
////                    spelling.equals("they'll") ||
////                    spelling.equals("they've") ||
////                    spelling.equals("he's") ||
////                    spelling.equals("he'll") ||
////                    spelling.equals("she's") ||
////                    spelling.equals("she'll") ||
////                    spelling.equals("don't") ||
////                    spelling.equals("won't") ||
////                    spelling.equals("doesn't") ||
////                    spelling.equals("hasn't") ||
////                    spelling.equals("haven't") ||
//
//                    // manageable parts of speech
//                    (       pos != Pos.CD &&
//                            pos != Pos.JJ &&
//                            pos != Pos.JJR &&
//                            pos != Pos.JJS &&
//                            pos != Pos.NN &&
//                            pos != Pos.NNS &&
//                            pos != Pos.NNP &&
//                            pos != Pos.NNPS &&
//                            pos != Pos.RB &&
//                            pos != Pos.RBR &&
//                            pos != Pos.RBS &&
//                            pos != Pos.UH
//
////                            &&
////                            pos != Pos.VB &&
////                            pos != Pos.VBD &&
////                            pos != Pos.VBG &&
////                            pos != Pos.VBN &&
////                            pos != Pos.VBP &&
////                            pos != Pos.VBZ
//
//                            // tricky parts of speech
////                    pos == Pos.TO ||
////                    pos == Pos.IN ||
////                    pos == Pos.PRP ||
////                    pos == Pos.WRB ||
////                    pos == Pos.CC ||
////                    pos == Pos.DT ||
////                    pos == Pos.EX ||
////                    pos == Pos.EX ||
////                    pos == Pos.PRP$ ||
////                    pos == Pos.UNKNOWN
//                    )
//                    )
//                whiteListIndexes.remove(i);
//        }
        //if it's 100% replacement, just return all non-blacklisted indexes
        if (replacement_frequency == 1)
            return (HashSet<Integer>) originalIndexes;

        if (originalIndexes.size() < 1)
            return (HashSet<Integer>) originalIndexes;

        HashSet<Integer> randomIndexes = new HashSet<Integer>();
        int nOfOriginalIndexes = originalIndexes.size();
        int num_to_replace = (int)(replacement_frequency * nOfOriginalIndexes); //TODO decide which way to round
        Random rand = new Random();
        int index_to_add = rand.nextInt(nOfOriginalIndexes);
        while (num_to_replace > 0) {
            while(randomIndexes.contains(index_to_add))
                index_to_add = rand.nextInt(nOfOriginalIndexes);
            randomIndexes.add(index_to_add);
            num_to_replace--;
        }
        return randomIndexes;
    }

    private Song replaceWords(Song templateSong, WordReplacements wordReplacements) {
        //TODO: eventually change this to work with any SongSement, not just entire Songs
        //TODO: if Clonable becomes viable, skinny out this method and use Cloneable instead
        Song generatedSong = new Song();
        for (int i = 0; i < templateSong.getSize(); i++) {
            Stanza currentStanza = templateSong.getStanzas().get(i);
            Stanza newStanza = new Stanza();
            for (int j = 0; j < currentStanza.getSize(); j++) {
                Line currentLine = currentStanza.getLines().get(j);
                Line newLine = new Line();
                for (int k = 0; k < currentLine.getSize(); k++) {
                    Word currentWord = currentLine.getWords().get(k);
                    if (wordReplacements.containsKey(currentWord))
                        newLine.add(wordReplacements.get(currentWord));
                    else {
                        //TODO: do a more complete copy here
                        Word temp = new Word(currentWord.getSpelling());
                        temp.setPos(currentWord.getPos());
                        temp.setNe(currentWord.getNe());
                        temp.setCapitalized(currentWord.getCapitalized());
                        newLine.add(temp);
                    }
                }
                newStanza.add(newLine);
            }
            generatedSong.add(newStanza);
        }
        return generatedSong;
    }

    private static void print(String s) {
        System.out.println(s);
    }

    private SongWrapper sentencesToSongWrapper(String rawSong, List<Sentence> parsedSentences) {
        //TODO > Include in SongWrapper a CoreMap for each sentence.

        Song tempSong = new Song();

        ArrayList<String> rawStanzas = new ArrayList<String>(Arrays.asList(rawSong.split("\\n\\n")));

        SongWrapper wrapper = new SongWrapper();
        ArrayList<ArrayList<char[]>> punctuation = new ArrayList<ArrayList<char[]>>();
        Sentence currentStringSentence = new Sentence();

        int currentSentenceIndex = 0;

        for (int i = 0; i < rawStanzas.size(); i++) {
            ArrayList<String> rawLines = new ArrayList<>(Arrays.asList(rawStanzas.get(i).split("\\n")));
            Stanza tempStanza = new Stanza();
            ArrayList<char[]> punctStanza = new ArrayList<>();
            int sentenceWordIndex = 0;
            for (int j = 0; j < rawLines.size(); j++) {
                String rawLine = rawLines.get(j);
                //kill punctuation (for now)
                //TODO do something about appostrophes
                rawLine = rawLine.replaceAll("[^\\w\\d\\s]", "");
                Line tempLine = new Line();
                ArrayList<String> rawWords = new ArrayList<>(Arrays.asList(rawLine.split("\\s")));
                for (int k = 0; k < rawWords.size(); k++) {

                    // if current sentence is the same as the parsed sentence
                    if (currentStringSentence.toString().equals(parsedSentences.get(currentSentenceIndex).toString().replaceAll("[^\\w\\d\\s]", ""))) {
                        currentStringSentence = new Sentence();
                        sentenceWordIndex = 0;
                        currentSentenceIndex++;
                    }

                    // add a word to the current sentence
                    if (parsedSentences.size() > currentSentenceIndex - 1) {
                        tempLine.add(parsedSentences.get(currentSentenceIndex).get(sentenceWordIndex));
                        currentStringSentence.add(new Word(rawWords.get(k)));
                        sentenceWordIndex++;

                    }

                }
                tempStanza.add(tempLine);
            }
            tempSong.add(tempStanza);
        }

        //TODO: eventually make this internal
        wrapper.setRawSong(rawSong);
        wrapper.setSentences(parsedSentences);
        wrapper.setPunctuation(punctuation);
        wrapper.setSong(tempSong);
        wrapper.setWordSet(new HashSet<>());
        wrapper.setBasic_lines(new ArrayList<>());
        wrapper.setSongStats(tempSong);
        return wrapper;
    }

    private String readTemplate(String songFile) {
        Utils.testPrintln("Entering readTemplate");
        Song song = new Song();
        try {
            File file = new File(Utils.rootPath + "local-data/dev-template-songs/" + songFile);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            return new String(data, "UTF-8");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}



/*
TODO: If the top word is a noun but the pos is plural noun, don't cut it, just pluralize the noun! Same with comparative and superlative adverbs!

TODO: Tag pos in w2v suggestions within the context of the whole song (or at least the sentence it's in).

TODO: Figure out verbs: reflexive, transitive, person, etc.

TODO: Make a named entity recognition filterWords

TODO: Recognize compound nouns, replace them with nouns or compound nouns

TODO: use coreferences to get gender right

TODO: use a serialized Stanford NLP pipeline object
 */


/*
Safe parts of speech:
CD
JJ
JJR
JJS
NN
NNS
NNP
NNPS
RB
RBR
RBS
UH
VB
VBD
VBG
VBN
VBP
VBZ
 */

/*
A sentence may go over multiple lines:
I will see that crazy man
When he rides a crazy lamb.

Or 1 line can have multiple sentences:
Let it be. Let it be.

Lines are defined by the \n character. Sentences are defined by punctuation.
 */































































































































