package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.*;

public abstract class IntConstraint extends WordConstraint {

    protected ModelNum modelComparison;
    protected NonModelNum nonModelComparison;
    protected int i = -1;

    public IntConstraint(ModelNum comparison, int i, ReturnType returnType) {
        super(returnType);
        this.modelComparison = comparison;
        this.i = i;
    }

    public IntConstraint(ModelNum comparison) {
        super(ReturnType.MATCHES);
        this.modelComparison = comparison;
    }

    public IntConstraint(NonModelNum comparison) {
        super(null);
        this.nonModelComparison = comparison;
    }

    @Override
    public Set<Word> useWithPresetFields(Collection<Word> wordsToFilter) {
        if (modelComparison != null) {
            Set<Word> set = this.decideModelComparison(wordsToFilter);
            return set;
        }
        else if (nonModelComparison != null) {
            Set<Word> set = this.decideNonModelComparison(wordsToFilter);
            return set;
        }
        return null;
    }

    @Override
    public Set<Word> useOldWordSpecific(Collection<Word> wordsToFilter, Word oldWord) {
        this.i = (this.wordToSpecficIntType(oldWord));
        return this.useWithPresetFields(wordsToFilter);
    }

    private Set<Word> decideModelComparison(Collection<Word> wordsToFilter) {
        switch (modelComparison) {
            case GREATER_OR_EQUAL:
                return filterByGreaterOrEqual(wordsToFilter);
            case HIGHEST:
                Set<Word> set = filterByHighest(wordsToFilter);
                return set;
            case EQUAL:
                set = filterByEqual(wordsToFilter, i, returnType);
                return set;
            default:
                return null;
        }
    }

    private Set<Word> decideNonModelComparison(Collection<Word> wordsToFilter) {
        switch (nonModelComparison) {
            case HIGHEST:
                return filterByHighest(wordsToFilter);
            default:
                return null;
        }
    }

    public Set<Word> filterByGreaterOrEqual(Collection<Word> words) {
        return this.filterByGreaterOrEqual(words, i, returnType);
    }

    public Set<Word> filterByGreaterOrEqual(Collection<Word> words, Integer score, ReturnType returnType) {
        Map<Word,Integer> map = wordsToSpecficIntType(words);
        map.values().retainAll(filter_goe(map.values(), score, returnType));
        return map.keySet();
    }

    public Set<Word> filterByHighest(Collection<Word> words) {
        TreeMap<Word,Integer> map = new TreeMap<>(wordsToSpecficIntType(words));
        map.values().retainAll(filter_highest(map.values()));
        return map.keySet();
    }

    public Set<Word> filterByEqual(Collection<Word> words, Integer score, ReturnType returnType) {
        TreeMap<Word,Integer> map = new TreeMap<>(wordsToSpecficIntType(words));
        map.values().retainAll(filter_equal(map.values(), score, returnType));
        return map.keySet();
    }

//    public Set<Word> filterByEqual(Collection<Word> words) {
//        TreeMap<Word,Integer> map = new TreeMap<>(wordsToSpecficIntType(words));
//        map.values().retainAll(filter_equal(map.values()));
//        return map.keySet();
//    }
//
//    public Set<Word> filterByLessThan(Collection<Word> words) {
//        TreeMap<Word,Integer> map = new TreeMap<>(wordsToSpecficIntType(words));
//        map.values().retainAll(filter_less(map.values()));
//        return map.keySet();
//    }

    public abstract Map<Word,Integer> wordsToSpecficIntType(Collection<Word> words);

    public abstract Integer wordToSpecficIntType(Word word);

    @Override
    public abstract boolean weaken();

    public boolean weaken(int i) {
        if (this.i <= 0)
            return false;
        this.i -= i;
        return true;
    }

    public static List<Integer> filter_goe(Collection<Integer> ints, int i, ReturnType returnType) {
        List<Integer> filteredIn = new ArrayList<>();
        for (int d : ints)
            if (returnType == ReturnType.MATCHES && d >= i ||
                    returnType == ReturnType.NON_MATCHES && d < i)
                filteredIn.add(d);
        return filteredIn;
    }

    public static List<Integer> filter_highest(Collection<Integer> ints) {
        List<Integer> maxis = new ArrayList<>();
        int maxVal = Integer.MIN_VALUE;
        for (int d : ints)
            if (d > maxVal) {
                maxVal = d;
                maxis.clear();
                maxis.add(d);
            }
            else if (d == maxVal)
                maxis.add(d);
        return maxis;
    }

    public static List<Integer> filter_equal(Collection<Integer> ints, Integer score, ReturnType returnType) {
        List<Integer> result = new ArrayList<>();
        for (Integer i : ints)
            if (returnType == ReturnType.MATCHES && score == i ||
                    returnType == ReturnType.NON_MATCHES && score != i)
                result.add(i);
        return result;
    }

    public ModelNum getModelComparison() {
        return modelComparison;
    }

    public void setModelComparison(ModelNum modelComparison) {
        this.modelComparison = modelComparison;
    }

    public NonModelNum getNonModelComparison() {
        return nonModelComparison;
    }

    public void setNonModelComparison(NonModelNum nonModelComparison) {
        this.nonModelComparison = nonModelComparison;
    }

    public int geti() {
        return i;
    }

    public void seti(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "IntConstraint";
    }

}





















































