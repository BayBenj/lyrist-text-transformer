package songtools;

import elements.Song;
import english.*;
import rhyme.Phoneticizer;
import elements.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SongMutator {

    public static void fixAllIndefiniteArticles(Song se) {
        List<Word> words = se.words();
        for (int i = 0; i < words.size(); i++) {
            if (            i < words.size() - 1 &&
                    (words.get(i).getLowerSpelling().equals("a") ||
                            words.get(i).getLowerSpelling().equals("an"))) {
                String correctArticle = DeterminerManager.getCorrectIndefiniteArticle(words.get(i + 1));
                if (!correctArticle.equalsIgnoreCase(words.get(i).getLowerSpelling())) {
                    Word articleWord = words.get(i);
                    articleWord.setSpelling(correctArticle);
                    articleWord.setPos(Pos.DT);//TODO make sure this is right
                    articleWord.setNe(Ne.O);
                    articleWord.setSyllables(Phoneticizer.getSyllables(articleWord.getLowerSpelling().toUpperCase()));
                }
            }
        }
    }

    public static void capitalizeFirstWordsInLines(Song generatedSong) {
        List<Line> lines = generatedSong.lines();
        for (Line line : lines) {
            if (line == null || line.size() == 0) continue;
            Word firstWord = line.get(0);
            capitalizeWord(firstWord);
        }
    }

    private static void capitalizeWord(Word word) {
        //TODO move to CapitalizationManager?
        StringBuilder sb = new StringBuilder(word.getLowerSpelling());
        char firstChar = sb.charAt(0);
        String upperStr = Character.toString(Character.toUpperCase(firstChar));
        sb.replace(0, 1, upperStr);
        word.setSpelling(sb.toString());
    }

    public static void lowercaseAllWords(Song generatedInfoSong) {
        List<Word> words = generatedInfoSong.words();
        for (Word word : words) {
            String spelling = word.getLowerSpelling();
            word.setSpelling(spelling.toLowerCase());
            word.setCapitalized(false);
        }
    }

    public static InfoSong replaceWords(Song templateInfoSong, WordReplacements wordReplacements) {
        //TODO: eventually change this to work with any SongSegment, not just entire Songs
        InfoSong generatedInfoSong = new InfoSong("NEW SONG");
        for (int i = 0; i < templateInfoSong.size(); i++) {
            Stanza currentStanza = templateInfoSong.get(i);
            Stanza newStanza = new Stanza(currentStanza.getType(), generatedInfoSong);
            newStanza.setType(currentStanza.getType());
            newStanza.setSong(generatedInfoSong);
            for (int j = 0; j < currentStanza.size(); j++) {
                Line currentLine = currentStanza.get(j);
                Line newLine = new Line(newStanza);
                newLine.setStanza(newStanza);
                for (int k = 0; k < currentLine.size(); k++) {
                    Word currentWord = currentLine.get(k);
                    if (wordReplacements.containsKey(currentWord))
                        newLine.add(wordReplacements.get(currentWord));
                    else {
                        Word temp = new Word(currentWord.getLowerSpelling());
                        temp.setBase(currentWord.getBase());
                        temp.setSyllables(currentWord.getSyllables());
                        temp.setPos(currentWord.getPos());
                        temp.setNe(currentWord.getNe());
                        temp.setCapitalized(currentWord.getCapitalized());
                        temp.setLine(newLine);
                        newLine.add(temp);
                    }
                }
                newStanza.add(newLine);
            }
            generatedInfoSong.add(newStanza);
        }
        return generatedInfoSong;
    }

//    public static Stanza replaceWords(Stanza templateStanza, WordReplacements wordReplacements) {
//        //TODO: eventually change this to work with any SongSement, not just entire Songs
//        //TODO: if Clonable becomes viable, skinny out this method and use Cloneable instead
//        Stanza result = new Stanza();
//        for (int l = 0; l < templateStanza.size(); l++) {
//            Line currentLine = templateStanza.get(l);
//            Line newLine = new Line();
//            for (int w = 0; w < currentLine.size(); w++) {
//                Word currentWord = currentLine.get(w);
//                if (wordReplacements.containsKey(currentWord))
//                    newLine.add(wordReplacements.get(currentWord));
//                else {
//                    Word temp = new Word(currentWord.getLowerSpelling());
//                    temp.setBase(currentWord.getBase());
//                    temp.setSyllables(currentWord.getSyllables());
//                    temp.setPos(currentWord.getPos());
//                    temp.setNe(currentWord.getNe());
//                    temp.setCapitalized(currentWord.getCapitalized());
//                    temp.setRhymeScore(currentWord.getRhymeScore());
//                    temp.setCosineDistance(currentWord.getCosineDistance());
//                    newLine.add(temp);
//                }
//            }
//            result.add(newLine);
//        }
//
//        return result;
//    }

    public static void hideAllPunctuation(Song se) {
        List<Word> words = se.words();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i) instanceof Punctuation) {
                ((Punctuation) words.get(i)).hide();
            }
        }
    }

    public static void revealAllPunctuation(Song se) {
        List<Word> words = se.words();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i) instanceof Punctuation) {
                ((Punctuation) words.get(i)).unhide();
            }
        }
    }

    public static void softenAllPunctuation(Song se) {
        /*
        terminals in the middle of a line turn to commas
        all other punctuation is hidden
         */
        //TODO implement
//        List<Word> filterWords = se.getAllWords();
//        for (int i = 0; i < filterWords.size(); i++) {
//            if (filterWords.get(i) instanceof Punctuation) {
//                ((Punctuation) filterWords.get(i)).unhide();
//            }
//        }
    }

    public static String stringToString(String rawString, String oldString, String newString) {
        if (oldString.equals(newString))
            return rawString;
        String[] strings = rawString.split("\\s");
        for (String string : strings) {
            if (string.toLowerCase().equalsIgnoreCase(oldString)) {
                rawString = rawString.replaceAll(string, newString);
            }
        }
        return rawString;
    }

    public static String expandAllContractions(String rawString) {
        String[] strings = rawString.split("\\s");
        Map<String, String[]> map = ContractionManager.getExpandMap();
        for (String string : strings) {
            if (ContractionManager.isContraction(string.toLowerCase())) {
                boolean capitalized = false;
                if (Character.isUpperCase(string.charAt(0)))
                    capitalized = true;
                String[] expansionStrings = map.get(string.toLowerCase());
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < expansionStrings.length; i++) {
                    builder.append(expansionStrings[i]);
                    if (i < expansionStrings.length - 1)
                        builder.append(" ");
                }
                String expansion = builder.toString();
                if (capitalized)
                    expansion = expansion.substring(0, 1).toUpperCase() + expansion.substring(1);
                rawString = rawString.replaceAll(string, expansion);//TODO ensure to only replace full filterWords, not the letter sequence inside
            }
        }
        return rawString;
    }

    public static String personToPerson(String rawString, Person oldPerson, Person newPerson) {
        String[] strings = rawString.split("\\s");
        Set<String> all = PronounManager.getAll();
        Set<String> alreadyReplaced = new HashSet<>();
        for (String string : strings) {
            if (!alreadyReplaced.contains(string)) {
                if (all.contains(string.toLowerCase()) && PronounManager.getPerson(string.toLowerCase()) == oldPerson) {
                    boolean capitalized = false;
                    if (Character.isUpperCase(string.charAt(0)))
                        capitalized = true;
                    String newPronoun = PronounManager.getPronoun(
                            PronounManager.getCase(string.toLowerCase()),
                            PronounManager.getGender(string.toLowerCase()),
                            PronounManager.getNumber(string.toLowerCase()),
                            newPerson);
                    if (capitalized)
                        newPronoun = newPronoun.substring(0, 1).toUpperCase() + newPronoun.substring(1);
                    rawString = rawString.replaceAll(string, newPronoun);
                    alreadyReplaced.add(string);
                }
//                else if (DeterminerManager.possessiveDeterminers.contains(string.toLowerCase()) && DeterminerManager.getPerson(string.toLowerCase()) == oldPerson) {
//                    boolean capitalized = false;
//                    if (Character.isUpperCase(string.charAt(0)))
//                        capitalized = true;
//                    String newPossessiveDeterminer = DeterminerManager.getPossessiveDeterminer(
//                            DeterminerManager.getCase(string.toLowerCase()),
//                            DeterminerManager.getGender(string.toLowerCase()),
//                            DeterminerManager.getNumber(string.toLowerCase()),
//                            newPerson);
//                    if (capitalized)
//                        newPossessiveDeterminer = newPossessiveDeterminer.substring(0, 1).toUpperCase() + newPossessiveDeterminer.substring(1);
//                    rawString = rawString.replaceAll(string, newPossessiveDeterminer);
//                    alreadyReplaced.add(string);
//                }
            }
        }
        return rawString;
    }

    public static String changeAllPronouns(String rawString, Case case_, Gender gender, english.Number number, Person person) {
        String[] strings = rawString.split("\\s");
        Set<String> all = PronounManager.getAll();
        Set<String> alreadyReplaced = new HashSet<>();
        for (String string : strings) {
            if (!alreadyReplaced.contains(string) && all.contains(string.toLowerCase())) {
                boolean capitalized = false;
                if (Character.isUpperCase(string.charAt(0)))
                    capitalized = true;
                String newPronoun = PronounManager.getPronoun(case_, gender, number, person);
                if (capitalized)
                    newPronoun = newPronoun.substring(0, 1).toUpperCase() + newPronoun.substring(1);
                rawString = rawString.replaceAll(string, newPronoun);
                alreadyReplaced.add(string);
            }
        }
        return rawString;
    }

    public static String splitAppostropheWords(String rawTemplateText) {
        String result = new String(rawTemplateText.replaceAll("'", " '"));
        return result;
    }

//    public static InfoSong combineAppostropheWords(InfoSong song) {
//        for (Stanza stanza : song.getStanzas()) {
//            for (Line line : stanza.getLines()) {
//                Word lastWord;
//                for (Word word : line.getWords()) {
//                    if ()
//                    lastWord = word;
//                }
//
//            }
//
//        }
//    }

    public static String fixAppostrophes(String rawTemplateText) {
        String result = new String(rawTemplateText.replaceAll("’", "'"));
        result = result.replaceAll("`", "'");
        return result;
    }

    public static String deleteGarbage(String rawTemplateText) {
        String result = new String(rawTemplateText.replaceAll("'", " '"));
        result = result.replaceAll("\\d", "");
        result = result.replaceAll("-", " ");
        result = result.replaceAll("[^\\w\\s\\d'.!?;:]", "");
        return result;
    }

    public static String cleanText(String text) {
        text = SongMutator.expandAllContractions(text);
        text = SongMutator.fixAppostrophes(text);
        text = SongMutator.splitAppostropheWords(text);
        text = SongMutator.deleteGarbage(text);
        return text;
    }

}





































































