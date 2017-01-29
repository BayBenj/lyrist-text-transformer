//package filters;
//
//import java.util.*;
//
//public class DictionaryFilter extends VocabListFilter {
//
//    private static final String[] dictionaryArray = new String[]{"word1"};
//
//    private static final List<String> dictionaryList = new ArrayList(Arrays.asList(dictionaryArray));
//
//
//    public DictionaryFilter() {
//        super(new VocabList(dictionaryList, "dictionary"));
//    }
//
//    public DictionaryFilter(ReturnType returnType) {
//        super(returnType, new VocabList(dictionaryList, "dictionary"));
//    }
//
////    @Override
////    public FiltrationResults filterWords(Set<W2vWordSuggestion> w2vWordSuggestions) {
////        HashSet<W2vWordSuggestion> filteredIn = new HashSet<W2vWordSuggestion>();
////        HashSet<W2vWordSuggestion> filteredOut = new HashSet<W2vWordSuggestion>();
////        for (W2vWordSuggestion wordSuggestion : w2vWordSuggestions) {
////            Word word = wordSuggestion.getWord();
////            if (!dictionarySet.contains(word.getLowerSpelling())) {
//////                w2vWordSuggestions.remove(wordSuggestion);
////                filteredOut.add(new W2vWordSuggestion(word, wordSuggestion.getCosineDistance()));
////            }
////            else {
////                filteredIn.add(new W2vWordSuggestion(word, wordSuggestion.getCosineDistance()));
////            }
////        }
////        return new FiltrationResults(filteredIn, filteredOut);
////    }
//
////    public Set<Word> filterWords(Set<Word> w2vSuggestions, boolean b) {
////        for (Word w : w2vSuggestions) {
////            if (!dictionarySet.contains(w.getLowerSpelling()))
////                w2vSuggestions.remove(w);
////        }
////        return w2vSuggestions;
////    }
//
//    @Override
//    public Set<String> doFilter(Collection<String> originalStrings) {
//        for (String s : originalStrings) {
//            if (!super.vocabList.contains(s))
//                originalStrings.remove(s);
//        }
//        return (Set)originalStrings;
//    }
//
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
