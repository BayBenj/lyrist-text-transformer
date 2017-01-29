//package songtools;
//
//import elements.Word;
//import filters.FilterEquation;
//import filters.WordFilterEquation;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class FilterOperation extends Operation {
//
//    //can mark
//    //can narrow suggestions down
//
//    private Set toBeFiltered;//Strings, Words, Cosine Strings, Cosine Words
//    private FilterEquation filters;
//
//    public FilterOperation(Set wordstoBeFiltered, WordFilterEquation filters) {
//        this.toBeFiltered = wordstoBeFiltered;
//        this.filters = filters;
//    }
//
//    public Set<Word> retain() {
//        Set<Word> result = new HashSet<>(toBeFiltered);
//        result.retainAll(toBeFiltered);
//        return result;
//    }
//
//    public Set<Word> remove() {
//        Set<Word> result = new HashSet<>(toBeFiltered);
//        result.removeAll(toBeFiltered);
//        return result;
//    }
//
//    public Set<Word> add() {
//        Set<Word> result = new HashSet<>(toBeFiltered);
//        result.addAll(toBeFiltered);
//        return result;
//    }
//
//    public Set<Word> getWordstoBeFiltered() {
//        return toBeFiltered;
//    }
//
//    public void setWordstoBeFiltered(Set wordstoBeFiltered) {
//        this.toBeFiltered = wordstoBeFiltered;
//    }
//
//    public FilterEquation getFilters() {
//        return filters;
//    }
//
//    public void setFilters(WordFilterEquation filters) {
//        this.filters = filters;
//    }
//}
