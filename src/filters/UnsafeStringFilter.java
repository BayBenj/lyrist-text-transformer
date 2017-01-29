//package filters;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.Arrays;
//import java.util.Collection;
//
//public class UnsafeStringFilter extends StringFilter {
////TODO change this to extend EnumVocabFilter, similar to VocabFilters but holds a list of enums
//
//    private final String[] unsafeStrArray = new String[] {
//            "have",
//            "had",
//            "has",
//            "hath",
//            "hast",
//
//            "be",
//            "been",
//            "am",
//            "are",
//            "was",
//            "were",
//            "is",
//
//            "do",
//            "does",
//            "did",
//
//            "and",
//            "but",
//            "or",
//            "nor",
//            "not",
//
//            "in",
//            "on",
//            "upon",
//            "at",
//            "to",
//
//            "amen",
//
//            "then",
//
//            "will",
//            "wilt",
//            "shall",
//            "shalt",
//
//            "the",
//            "a",
//            "an",
//
//            "i",
//            "you",
//            "ye",
//            "thou",
//            "we",
//            "they",
//            "he",
//            "she",
//            "it",
//
//            "me",
//            "thee",
//            "us",
//            "them",
//            "him",
//            "her",
//
//            "my",
//            "your",
//            "thy",
//            "our",
//            "their",
//            "his",
//            "her",
//            "its",
//
//            "myself",
//            "yourself",
//            "yourselves",
//            "thyself",
//            "ourselves",
//            "themselves",
//            "himself",
//            "herself",
//            "itself",
//
//            "mine",
//            "yours",
//            "thine",
//            "ours",
//            "theirs",
//            "hers"
//    };
//    private final Set<String> unsafeStrSet = new HashSet<>(Arrays.asList(unsafeStrArray));
//
//    public UnsafeStringFilter() {}
//
//    public UnsafeStringFilter(ReturnType returnType) {
//        super(returnType);
//    }
//
//    @Override
//    public Set<String> doFilter(Collection<String> w2vSuggestions) {
//        Set<String> filteredIn = new HashSet<>();
//        for (String s : w2vSuggestions) {
//            if (super.getReturnType() == ReturnType.MATCHES && unsafeStrSet.contains(s.toString().toLowerCase()) ||
//                    super.getReturnType() == ReturnType.NON_MATCHES && !unsafeStrSet.contains(s.toString().toLowerCase()))
//                filteredIn.add(s);
//        }
//        return filteredIn;
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
