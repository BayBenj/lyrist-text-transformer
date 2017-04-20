package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.*;

public abstract class DoubleConstraint extends WordConstraint {

    protected ModelNum modelComparison;
    protected NonModelNum nonModelComparison;
    protected double dbl = -1;

    public DoubleConstraint(ModelNum comparison, double dbl, ReturnType returnType) {
        super(returnType);
        this.modelComparison = comparison;
        this.dbl = dbl;
    }

    public DoubleConstraint(ModelNum comparison) {
        super(null);
        this.modelComparison = comparison;
    }

    public DoubleConstraint(NonModelNum comparison) {
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
        this.dbl = (this.wordToSpecificDoubleType(oldWord));
        return this.useWithPresetFields(wordsToFilter);
    }

    private Set<Word> decideModelComparison(Collection<Word> wordsToFilter) {
        switch (modelComparison) {
            case GREATER_OR_EQUAL:
                return filterByGreaterOrEqual(wordsToFilter);
            case HIGHEST:
                Set<Word> set = filterByHighest(wordsToFilter);
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
        return this.filterByGreaterOrEqual(words, dbl, returnType);
    }

    public Set<Word> filterByGreaterOrEqual(Collection<Word> words, Double score, ReturnType returnType) {
        Map<Word,Double> map = wordsToSpecficDoubleType(words);
        map.values().retainAll(filter_goe(map.values(), score, returnType));
        return map.keySet();
    }

    public Set<Word> filterByHighest(Collection<Word> words) {
        TreeMap<Word,Double> map = new TreeMap<>(wordsToSpecficDoubleType(words));
        map.values().retainAll(filter_highest(map.values()));
        return map.keySet();
    }

//    public Set<Word> filterByEqual(Collection<Word> words) {
//        TreeMap<Word,Double> map = new TreeMap<>(wordsToSpecficIntType(words));
//        map.values().retainAll(filter_equal(map.values()));
//        return map.keySet();
//    }
//
//    public Set<Word> filterByLessThan(Collection<Word> words) {
//        TreeMap<Word,Double> map = new TreeMap<>(wordsToSpecficIntType(words));
//        map.values().retainAll(filter_less(map.values()));
//        return map.keySet();
//    }

    public abstract Map<Word,Double> wordsToSpecficDoubleType(Collection<Word> words);

    public abstract Double wordToSpecificDoubleType(Word word);

    @Override
    public abstract boolean weaken();

    public boolean weaken(double d) {
        if (dbl <= 0)
            return false;
        dbl -= d;
        return true;
    }

    public static List<Double> filter_goe(Collection<Double> doubles, double dbl, ReturnType returnType) {
        List<Double> filteredIn = new ArrayList<>();
        for (double d : doubles)
            if (returnType == ReturnType.MATCHES && d >= dbl ||
                    returnType == ReturnType.NON_MATCHES && d < dbl)
                filteredIn.add(d);
        return filteredIn;
    }

    public static List<Double> filter_highest(Collection<Double> doubles) {
        List<Double> maxDbls = new ArrayList<>();
        double maxVal = Double.MIN_VALUE;
        for (double d : doubles)
            if (d > maxVal) {
                maxVal = d;
                maxDbls.clear();
                maxDbls.add(d);
            }
            else if (d == maxVal)
                maxDbls.add(d);
        return maxDbls;
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

    public double getDbl() {
        return dbl;
    }

    public void setDbl(double dbl) {
        this.dbl = dbl;
    }

    @Override
    public String toString() {
        return "DoubleConstraint";
    }

}


















































