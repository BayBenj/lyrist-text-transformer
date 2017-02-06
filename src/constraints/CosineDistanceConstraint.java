package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CosineDistanceConstraint extends DoubleConstraint {

    public CosineDistanceConstraint(NonModelNum comparison) {
        super(comparison);
        this.instanceSpecific = true;
    }

    public CosineDistanceConstraint(ModelNum comparison) {
        super(comparison);
    }

    public CosineDistanceConstraint(ModelNum comparison, double dbl, ReturnType returnType) {
        super(comparison, dbl, returnType);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transforming~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Map<Word, Double> wordsToSpecficDoubleType(Collection<Word> words) {
        return wordsToRhymeScores(words);
    }

    @Override
    public Double wordToSpecficDoubleType(Word word) {
        return word.getCosineDistance();
    }

    private static Map<Word,Double> wordsToRhymeScores(Collection<Word> words) {
        Map<Word,Double> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getCosineDistance());
        return result;
    }

    @Override
    public boolean weaken() {
        this.weaken(.1);
        return true;
    }

}