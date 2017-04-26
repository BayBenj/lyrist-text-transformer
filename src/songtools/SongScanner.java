package songtools;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import globalstructure.SegmentType;
import rhyme.LineRhymeScheme;
import rhyme.Phoneticizer;
import elements.*;
import rhyme.RhymeClass;
import rhyme.WordsByRhyme;
import utils.U;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public abstract class SongScanner {

    public static InfoSong getTemplateSong(TextInFormat format, String fileString) {
        switch (format) {
            case NORMAL:
                return getTemplateSong(fileString);
            case PAUL:
                return getTemplateSongPaulFormat(fileString);
            default:
                return getTemplateSong(fileString);
        }
    }

    public static InfoSong getInfoSong(TextInFormat format, String fileString) {
        switch (format) {
            case NORMAL:
                return getInfoSong(fileString);
            case PAUL:
                return getInfoSongPaulFormat(fileString);
            default:
                return getInfoSong(fileString);
        }
    }

    public static InfoSong getTemplateSong(String fileString) {
        String rawTemplateText = readFileToText(fileString);
//        rawTemplateText = SongMutator.cleanText(rawTemplateText);
//        rawTemplateText = SongMutator.personToPerson(rawTemplateText, Person.FIRST, Person.SECOND);
//        rawTemplateText = SongMutator.stringToString(rawTemplateText, "my", "your");
        List<Sentence> parsedSentences = U.getStanfordNlp().parseTextToSentences(rawTemplateText);
        setSyllablesForSentences(parsedSentences);
        InfoSong templateInfoSong = sentencesToInfoSong(rawTemplateText, parsedSentences);
        return templateInfoSong;
    }

    public static InfoSong getTemplateSongPaulFormat(String fileString) {
        String rawTemplateText = readFileToText(fileString);
//        rawTemplateText = SongMutator.cleanText(rawTemplateText);
        InfoSong song = readPaulFormat(rawTemplateText);
        return song;
    }

    public static InfoSong readPaulFormat(String text) {
        String[] lines = text.split("\\n");
//        InfoSong song = new InfoSong("title", "writer", "genre");
        InfoSong result = null;
        Song stanzas = new Song();
        List<Integer> rhymes = new ArrayList<>();
        Stanza currentStanza = null;
        StringBuilder songText = new StringBuilder();
        String title = "";
        Stanza intro;
        Stanza outro;
        Stanza chorus;
        List<Stanza> verses = new ArrayList<>();
        List<Stanza> interludes = new ArrayList<>();
        List<Stanza> bridges = new ArrayList<>();

        for (String lineStr : lines) {
            if (lineStr.equals("") || lineStr.matches("\\s*[[buffer]]\\s*")) continue;

            if (lineStr.matches("TITLE: \\w+")) {
//                song.setTitle(lineStr.replace("TITLE: ", ""));
                title = lineStr.replace("TITLE: ", "");
                continue;
            }
            else if (lineStr.matches("INSPIRATION: \\w+")) {
                continue;
            }
            else if (lineStr.matches("INTRO")) {
                currentStanza = new Stanza(SegmentType.valOf(lineStr), stanzas);
                stanzas.add(currentStanza);
                intro = currentStanza;
                songText.append("\n");
                continue;
            }
            else if (lineStr.matches("OUTRO")) {
                currentStanza = new Stanza(SegmentType.valOf(lineStr), stanzas);
                stanzas.add(currentStanza);
                outro = currentStanza;
                songText.append("\n");
                continue;
            }
            else if (lineStr.matches("CHORUS")) {
                currentStanza = new Stanza(SegmentType.valOf(lineStr), stanzas);
                stanzas.add(currentStanza);
                chorus = currentStanza;
                songText.append("\n");
                continue;
            }
            else if (lineStr.matches("VERSE")) {
                currentStanza = new Stanza(SegmentType.valOf(lineStr), stanzas);
                stanzas.add(currentStanza);
                verses.add(currentStanza);
                songText.append("\n");
                continue;
            }
            else if (lineStr.matches("INTERLUDE")) {
//                currentStanza = new Stanza(SegmentType.valOf(lineStr), stanzas);
//                stanzas.add(currentStanza);
//                interludes.add(currentStanza);
//                songText.append("\n");
                continue;
            }
            else if (lineStr.matches("BRIDGE")) {
                currentStanza = new Stanza(SegmentType.valOf(lineStr), stanzas);
                stanzas.add(currentStanza);
                bridges.add(currentStanza);
                songText.append("\n");
                continue;
            }
            else if (lineStr.matches("\\d+\\t.+")) {
                int rhyme = Character.getNumericValue(lineStr.charAt(0));
                rhymes.add(rhyme);
                Line currentLine = new Line(currentStanza);
                lineStr = lineStr.substring(2);
                if (lineStr.charAt(lineStr.length() - 1) != '.')
                    lineStr += " .";

                String maxSyls = lineStr.replaceAll("-"," ");
                maxSyls = maxSyls.replaceAll("•"," ");
                maxSyls = maxSyls.replaceAll("[^\\w\\s\\d]","");
                maxSyls = maxSyls.replaceAll("\\s\\s"," ");
                String[] maxSyls_w = maxSyls.split("\\s");
                currentLine.setMaxSyls(maxSyls_w.length);

                String minSyls = maxSyls.replaceAll("\\[\\[buffer\\]\\]","");
                minSyls = minSyls.replaceAll("\\s\\s"," ");
                String[] minSyls_w = minSyls.split("\\s");
                currentLine.setMinSyls(minSyls_w.length);

                String noDashes = lineStr.replaceAll("\\[\\[buffer\\]\\]","");
                noDashes = noDashes.replaceAll("•\\s","");
                noDashes = noDashes.replaceAll("-","");
                noDashes = noDashes.replaceAll("•","");
                noDashes = noDashes.replaceAll("\\s\\s"," ");
                if (noDashes.matches("\\s+.")) continue;
                songText.append(noDashes);
                songText.append("\n");
                continue;
            }
            else {
                Line currentLine = new Line(currentStanza);
                if (lineStr.charAt(lineStr.length() - 1) != '.')
                    lineStr += " .";

                String maxSyls = lineStr.replaceAll("-"," ");
                maxSyls = maxSyls.replaceAll("•"," ");
                maxSyls = maxSyls.replaceAll("[^\\w\\s\\d]","");
                maxSyls = maxSyls.replaceAll("\\s\\s"," ");
                String[] maxSyls_w = maxSyls.split("\\s");
                currentLine.setMaxSyls(maxSyls_w.length);

                String minSyls = maxSyls.replaceAll("\\[\\[buffer\\]\\]","");
                minSyls = minSyls.replaceAll("\\s\\s"," ");
                String[] minSyls_w = minSyls.split("\\s");
                currentLine.setMinSyls(minSyls_w.length);

                String noDashes = lineStr.replaceAll("\\[\\[buffer\\]\\]","");
                noDashes = noDashes.replaceAll("•\\s","");
                noDashes = noDashes.replaceAll("-","");
                noDashes = noDashes.replaceAll("•","");
                noDashes = noDashes.replaceAll("\\s\\s"," ");
                if (noDashes.matches("\\s+.")) continue;
                songText.append(noDashes);
                songText.append("\n");
                continue;
            }
        }
        String clean = songText.toString();
        clean = clean.replaceAll("\n\n\n", "\n\n");
        if (clean.charAt(0) == '\n')
            clean = clean.substring(1);
        if (clean.charAt(clean.length() - 1) == '\n')
            clean = clean.substring(0, clean.length() - 1);
        clean = clean.replaceAll(" \\. \\."," .");
        List<Sentence> parsedSentences = U.getStanfordNlp().parseTextToSentences(clean);
        setSyllablesForSentences(parsedSentences);
        clean = clean.replace(",", " ,");
        clean = clean.replace("  ", " ");
        result = sentencesToInfoSong(clean, parsedSentences);
        result.setTitle(title);
        int s = 0;
        int i = 0;
        for (Stanza stanza : stanzas) {
            result.get(s).setType(stanza.getType());
            for (int l = 0; l < result.get(s).size(); l++) {
                result.get(s).get(l).setRhyme(new RhymeClass(rhymes.get(i)));
                U.print(result.get(s).get(l).getRhyme().getRhymeId());
                i++;
            }
            s++;
        }
        return result;
    }

    public static InfoSong getInfoSong(String fileString) {
        InfoSong templateInfoSong = getTemplateSong(fileString);

        String[] fileNameStrs = fileString.split("(@)|(.lyrics.txt)|( - )");
        if (fileNameStrs.length == 2) {
            String writer = fileNameStrs[0];
            String title = fileNameStrs[1];
            templateInfoSong.setTitle(title);
            templateInfoSong.setWriter(writer);
            templateInfoSong.setGenre("pop");
            templateInfoSong.setProgrammer("Ben Bay");
        }

        return templateInfoSong;
    }

    public static InfoSong getInfoSongPaulFormat(String fileString) {
        InfoSong templateInfoSong = getTemplateSongPaulFormat(fileString);

        String[] fileNameStrs = fileString.split("(@)|(.lyrics.txt)|( - )");
        if (fileNameStrs.length == 2) {
            String writer = fileNameStrs[0];
            String title = fileNameStrs[1];
            templateInfoSong.setTitle(title);
            templateInfoSong.setWriter(writer);
            templateInfoSong.setGenre("pop");
            templateInfoSong.setProgrammer("Ben Bay");
        }

        return templateInfoSong;
    }

    public static String readFileToText(String fileString) {
        U.testPrintln("Entering readSong");
        Song infoSong = new Song();
        try {
            File file = new File(U.rootPath + "data/songs/dev-template-songs/" + fileString);
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

    public static void setSyllablesForSentences(List<Sentence> sentences) {
        for (Sentence sentence : sentences) {
            for (Word word : sentence) {
                word.setPronunciations(Phoneticizer.getSyllablesForWord(word));
            }
        }
    }

    public static InfoSong sentencesToInfoSong(String rawSong, List<Sentence> parsedSentences) {
        //TODO > Include in InfoSong a CoreMap for each sentence.

        InfoSong tempInfoSong = new InfoSong("", "", "");

        ArrayList<String> rawStanzas = new ArrayList<>(Arrays.asList(rawSong.split("\\n\\n")));

        Sentence currentStringSentence = new Sentence();

        int currentSentenceIndex = 0;
        int sentenceWordIndex = 0;

        for (int s = 0; s < rawStanzas.size(); s++) {
            ArrayList<String> rawLines = new ArrayList<>(Arrays.asList(rawStanzas.get(s).split("\\n")));
            Stanza tempStanza = new Stanza(null, tempInfoSong);
            for (int l = 0; l < rawLines.size(); l++) {
                String rawLine = rawLines.get(l);
                //kill punctuation (for now)
                //TODO do something about appostrophes
//                rawLine = rawLine.replaceAll("[^\\w\\d\\s]", "");
                Line tempLine = new Line(tempStanza);
                ArrayList<String> rawWords = new ArrayList<>(Arrays.asList(rawLine.split("\\s")));
                for (int w = 0; w < rawWords.size(); w++) {
                    if (rawWords.get(w).equals("")) continue;

                    // add a word to the current sentence
                    if (parsedSentences.size() > currentSentenceIndex - 1) {
                        if (!parsedSentences.get(currentSentenceIndex).get(sentenceWordIndex).getLowerSpelling().equals(""))
                            tempLine.add(parsedSentences.get(currentSentenceIndex).get(sentenceWordIndex));
                        currentStringSentence.add(new Word(rawWords.get(w)));
                        sentenceWordIndex++;
                    }

                    // if current sentence is the oldWordSpecific as the parsed sentence
                    System.out.println(currentStringSentence.toString());
                    System.out.println(parsedSentences.get(currentSentenceIndex).toString());
                    if (currentStringSentence.toString().equals(parsedSentences.get(currentSentenceIndex).toString())) //.replaceAll("[^\\w\\d\\s]", "")))
                    {
                        currentStringSentence = new Sentence();
                        sentenceWordIndex = 0;
                        currentSentenceIndex++;
                    }
                }
                if (!tempLine.isEmpty())
                    tempStanza.add(tempLine);
            }
            if (!tempStanza.isEmpty())
                tempInfoSong.add(tempStanza);
        }

        //TODO: eventually make this internal
        return tempInfoSong;
    }

    public static ArrayList<Sentence> stanfordSentencesToSentences(List<List<CoreLabel>> stanfordSentences) {
        ArrayList<Sentence> resultSentences = new ArrayList<Sentence>();
        for (List<CoreLabel> sentence : stanfordSentences) {
            Sentence resultSentence = new Sentence();
            for (CoreLabel token : sentence) {
                String spelling = token.get(CoreAnnotations.TextAnnotation.class);
                Word resultWord = new Word(spelling);
                resultWord.setPos(Pos.valueOf(token.get(CoreAnnotations.PartOfSpeechAnnotation.class)));
                resultWord.setNe(Ne.valueOf(token.get(CoreAnnotations.NamedEntityTagAnnotation.class)));
                resultSentence.add(resultWord);
            }
            resultSentences.add(resultSentence);
        }
        return resultSentences;
    }

    public static Set<Integer> getRandomIndexes(Set<Integer> originalIndexes, double replacement_frequency) {
        U.testPrintln("Entering getRandomIndexes");

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
//            Pos wordsToPos = w.getParts();
//            String spelling = w.getLowerSpelling().toLowerCase();
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
//                    (       wordsToPos != Pos.CD &&
//                            wordsToPos != Pos.JJ &&
//                            wordsToPos != Pos.JJR &&
//                            wordsToPos != Pos.JJS &&
//                            wordsToPos != Pos.NN &&
//                            wordsToPos != Pos.NNS &&
//                            wordsToPos != Pos.NNP &&
//                            wordsToPos != Pos.NNPS &&
//                            wordsToPos != Pos.RB &&
//                            wordsToPos != Pos.RBR &&
//                            wordsToPos != Pos.RBS &&
//                            wordsToPos != Pos.UH
//
////                            &&
////                            wordsToPos != Pos.VB &&
////                            wordsToPos != Pos.VBD &&
////                            wordsToPos != Pos.VBG &&
////                            wordsToPos != Pos.VBN &&
////                            wordsToPos != Pos.VBP &&
////                            wordsToPos != Pos.VBZ
//
//                            // tricky parts of speech
////                    wordsToPos == Pos.TO ||
////                    wordsToPos == Pos.IN ||
////                    wordsToPos == Pos.PRP ||
////                    wordsToPos == Pos.WRB ||
////                    wordsToPos == Pos.CC ||
////                    wordsToPos == Pos.DT ||
////                    wordsToPos == Pos.EX ||
////                    wordsToPos == Pos.EX ||
////                    wordsToPos == Pos.PRP$ ||
////                    wordsToPos == Pos.UNKNOWN
//                    )
//                    )
//                whiteListIndexes.remove(i);
//        }
        //if it's 100% replacement, just return all non-blacklisted indexes
        if (replacement_frequency == 1 || replacement_frequency > 1)
            return originalIndexes;

        if (originalIndexes.size() < 1)
            return originalIndexes;

        HashSet<Integer> randomIndexes = new HashSet<>();
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

    public static Set<PositionedWord> getPositionedWords(Song infoSong) {
        Set<PositionedWord> positionedWords = new HashSet<>();
        for (int s = 0; s < infoSong.size(); s++) {
            Stanza stanza = infoSong.get(s);
            for (int l = 0; l < stanza.size(); l++) {
                Line line = stanza.get(l);
                for (int w = 0; w < line.size(); w++) {
                    Word word = line.get(w);
                    positionedWords.add(new PositionedWord(word, s, l , w));
                }

            }

        }
        return positionedWords;
    }

    public static WordsByRhyme getRhymeSchemeWords(Song infoSongToMark, LineRhymeScheme rhymeScheme) {
        if (infoSongToMark != null && !infoSongToMark.words().isEmpty()) {
            List<Line> lines = infoSongToMark.lines();
            WordsByRhyme wordsByRhyme = new WordsByRhyme();
            for (int l = 0; l < lines.size(); l++) {
                Line line = lines.get(l);
                if (rhymeScheme.contains(l) && rhymeScheme.getRhymeByIndex(l).getRhymeId() != 0 && rhymeScheme.get(rhymeScheme.getRhymeByIndex(l)).size() >= 2) {
                    Word word = line.get(line.size() - 1);
                    int i = 1;
                    while (word instanceof Punctuation) {
                        word = line.get(line.size() - 1 - i);
                        i++;
                    }
                    RhymeClass rhyme = rhymeScheme.getRhymeByIndex(l);
                    wordsByRhyme.putWord(rhyme, word);
                }
            }
            return wordsByRhyme;
        }
        return null;
    }

    public static int getNLines(Song infoSong) {
        List<Line> lines = infoSong.lines();
        return lines.size();
    }

}









































