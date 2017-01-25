package songtools;

import english.*;
import rhyme.Phoneticizer;
import elements.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SongMutator {

    public static void fixAllIndefiniteArticles(SongElement se) {
        List<Word> words = se.getAllWords();
        for (int i = 0; i < words.size(); i++) {
            if (            i < words.size() - 1 &&
                    (words.get(i).getSpelling().equals("a") ||
                            words.get(i).getSpelling().equals("an"))) {
                String correctArticle = DeterminerManager.getCorrectIndefiniteArticle(words.get(i + 1));
                if (!correctArticle.equalsIgnoreCase(words.get(i).getSpelling())) {
                    Word articleWord = words.get(i);
                    articleWord.setSpelling(correctArticle);
                    articleWord.setPos(Pos.DT);//TODO make sure this is right
                    articleWord.setNe(NamedEntity.O);
                    articleWord.setPhonemes(Phoneticizer.getPronunciationForWord(articleWord.getSpelling().toUpperCase()));
                    articleWord.setSyllables(Phoneticizer.getSyllables(articleWord.getSpelling().toUpperCase()));
                }
            }
        }
    }

    public static void capitalizeFirstWordsInLines(SongElement generatedSong) {
        List<SongElement> lines = generatedSong.getAllSubElementsOfType(new Line());
        for (SongElement songElement : lines) {
            Line line = (Line) songElement;
            Word firstWord = line.getWords().get(0);
            capitalizeWord(firstWord);
        }
    }

    private static void capitalizeWord(Word word) {
        //TODO move to CapitalizationManager?
        StringBuilder sb = new StringBuilder(word.getSpelling());
        char firstChar = sb.charAt(0);
        String upperStr = Character.toString(Character.toUpperCase(firstChar));
        sb.replace(0, 1, upperStr);
        word.setSpelling(sb.toString());
    }

    public static void lowercaseAllWords(Song generatedSong) {
        List<Word> words = generatedSong.getAllWords();
        for (Word word : words) {
            String spelling = word.getSpelling();
            word.setSpelling(spelling.toLowerCase());
            word.setCapitalized(false);
        }
    }

    public static Song replaceWords(Song templateSong, WordReplacements wordReplacements) {
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
                        //TODO: do a more complete copy here. Is that possible?
                        Word temp = new Word(currentWord.getSpelling());
                        temp.setPhonemes(currentWord.getPhonemes());
                        temp.setSyllables(currentWord.getSyllables());
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

    public static void hideAllPunctuation(SongElement se) {
        List<Word> words = se.getAllWords();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i) instanceof Punctuation) {
                ((Punctuation) words.get(i)).hide();
            }
        }
    }

    public static void revealAllPunctuation(SongElement se) {
        List<Word> words = se.getAllWords();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i) instanceof Punctuation) {
                ((Punctuation) words.get(i)).unhide();
            }
        }
    }

    public static void softenAllPunctuation(SongElement se) {
        /*
        terminals in the middle of a line turn to commas
        all other punctuation is hidden
         */
        //TODO implement
//        List<Word> words = se.getAllWords();
//        for (int i = 0; i < words.size(); i++) {
//            if (words.get(i) instanceof Punctuation) {
//                ((Punctuation) words.get(i)).unhide();
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
                rawString = rawString.replaceAll(string, expansion);//TODO ensure to only replace full words, not the letter sequence inside
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
}


















































































