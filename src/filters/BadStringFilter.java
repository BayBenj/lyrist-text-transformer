package filters;

import song.CharList;
import song.VocabList;

import java.util.*;

public class BadStringFilter extends CharFilter {

    private static final Character[] badCharacterArray = new Character[] {
            ' ',
            '\'',
            '_',
            ',',
            '.',
            '!',
            '@',
            '#',
            '$',
            '%',
            '^',
            '&',
            '*',
            '(',
            ')',
            '-',
            '+',
            '=',
            '`',
            '~',
            '<',
            '>',
            '/',
            '?',
            '\\',
            '[',
            ']',
            '{',
            '}',
            ':',
            ';',
            '\"',
            '|',
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9'
    };

    private static final List<Character> badCharacterList = new ArrayList<>(Arrays.asList(badCharacterArray));

    public BadStringFilter() {
        super(new CharList(badCharacterList, "bad-chars"));
    }

    public BadStringFilter(Direction direction) {
        super(direction, new CharList(badCharacterList, "bad-chars"));
    }

//    @Override
//    public FiltrationResults filter(Set<W2vWordSuggestion> w2vWordSuggestions) {
//        HashSet<W2vWordSuggestion> filteredIn = new HashSet<W2vWordSuggestion>();
//        HashSet<W2vWordSuggestion> filteredOut = new HashSet<W2vWordSuggestion>();
//        for (W2vWordSuggestion wordSuggestion : w2vWordSuggestions) {
//            Word word = wordSuggestion.getWord();
//            String spelling = word.getSpelling();
//            for (char c: spelling.toCharArray()) {
//                if (badCharacterSet.contains(Character.toString(c)) || spelling.equals("")) {
////                w2vWordSuggestions.remove(wordSuggestion);
//                    filteredOut.add(new W2vWordSuggestion(word, wordSuggestion.getCosineDistance()));
//                }
//                else {
//                    filteredIn.add(new W2vWordSuggestion(word, wordSuggestion.getCosineDistance()));
//                }
//            }
//        }
//        return new FiltrationResults(filteredIn, filteredOut);
//    }

//    public Set<Word> filter(Set<Word> w2vSuggestions, boolean b) {
//        for (Word w : w2vSuggestions) {
//            String spelling = w.getSpelling();
//            for (char c: spelling.toCharArray()) {
//                if (badCharacterSet.contains(c) || spelling.equals(""))
//                    w2vSuggestions.remove(w);
//            }
//        }
//        return w2vSuggestions;
//    }

    @Override
    public Set<String> doFilter(Set<String> originalStrings) {
        Set<String> result = new HashSet<>();
        for (String s : originalStrings) {
            for (char c: s.toCharArray()) {
                if (super.getDirection() == Direction.INCLUDE_MATCH && super.getCharList().contains(c)) {
                    result.add(s);
                    break;
                }
                else if (super.getDirection() == Direction.EXCLUDE_MATCH && super.getCharList().contains(c))
                    break;
            }
            if (super.getDirection() == Direction.EXCLUDE_MATCH)
                result.add(s);
        }
        return result;
    }
}











































































