package filters;

import song.Word;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UnsafeWordFilter extends WordFilter {
//TODO change this to extend EnumVocabFilter, similar to VocabFilters but holds a list of enums

    private final String[] unsafeStrArray = new String[] {
            "have",
            "had",
            "has",
            "hath",
            "hast",

            "be",
            "been",
            "am",
            "are",
            "was",
            "were",
            "is",

            "do",
            "does",
            "did",

            "and",
            "but",
            "or",
            "nor",
            "not",

            "in",
            "on",
            "upon",
            "at",
            "to",

            "amen",

            "then",

            "will",
            "wilt",
            "shall",
            "shalt",

            "the",
            "a",
            "an",

            "i",
            "you",
            "ye",
            "thou",
            "we",
            "they",
            "he",
            "she",
            "it",

            "me",
            "thee",
            "us",
            "them",
            "him",
            "her",

            "my",
            "your",
            "thy",
            "our",
            "their",
            "his",
            "her",
            "its",

            "myself",
            "yourself",
            "yourselves",
            "thyself",
            "ourselves",
            "themselves",
            "himself",
            "herself",
            "itself",

            "mine",
            "yours",
            "thine",
            "ours",
            "theirs",
            "hers"
    };
    private final Set<String> unsafeStrSet = new HashSet<>(Arrays.asList(unsafeStrArray));

    public UnsafeWordFilter() {}

    public UnsafeWordFilter(Direction direction) {
        super(direction);
    }

    @Override
    public Set<Word> doFilter(Set<Word> w2vSuggestions) {
        Set<Word> filteredIn = new HashSet<Word>();
        for (Word w : w2vSuggestions) {
            if (super.getDirection() == Direction.INCLUDE_MATCH && unsafeStrSet.contains(w.toString().toLowerCase()) ||
                    super.getDirection() == Direction.EXCLUDE_MATCH && !unsafeStrSet.contains(w.toString().toLowerCase()))
                filteredIn.add(w);
        }
        return filteredIn;
    }
}



















































































