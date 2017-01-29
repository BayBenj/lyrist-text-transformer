package constraints;

import elements.Word;
import filters.ReturnType;
import java.util.*;

public class CosineDistanceConstraint extends DoubleConstraint {

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Field-dependent~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//    public Set<Word> equals(Collection<Word> words) {
//        return this.filterExactRhymeScore(words, dbl, returnType);
//    }

    public Set<Word> filterByGreaterOrEqual(Collection<Word> words) {
        return this.filterByGreaterOrEqual(words, dbl, returnType);
    }

//    public Set<Word> lessOrEqual(Collection<Word> words) {
//        return this.filterLessRhymeScore(words, dbl, returnType);
//    }

//    public Set<Word> filterByHighest(Collection<Word> words) {
//        return this.filterByHighest(words);
//    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Field-independent~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static Set<Word> filterByGreaterOrEqual(Collection<Word> words, Double score, ReturnType returnType) {
        Map<Word,Double> map = wordsToCosineDistances(words);
        map.values().retainAll(filter_goe(map.values(), score, returnType));
        return map.keySet();
    }

    public static Set<Word> filterByHighest(Collection<Word> words) {
        TreeMap<Word,Double> map = new TreeMap<>(wordsToCosineDistances(words));
        map.values().retainAll(filter_highest(map.values()));
        return map.keySet();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transformer~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static Map<Word,Double> wordsToCosineDistances(Collection<Word> words) {
        Map<Word,Double> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getCosineDistance());
        return result;
    }

}





































































