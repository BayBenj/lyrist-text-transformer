package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CosineConstraint extends DoubleConstraint {

    public CosineConstraint(NonModelNum comparison) {
        super(comparison);
    }

    public CosineConstraint(ModelNum comparison, double dbl, ReturnType returnType) {
        super(comparison, dbl, returnType);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transforming~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Map<Word, Double> wordsToSpecficDoubleType(Collection<Word> words) {
        return wordsToRhymeScores(words);
    }

    private static Map<Word,Double> wordsToRhymeScores(Collection<Word> words) {
        Map<Word,Double> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getCosineDistance());
        return result;
    }

    @Override
    public Set<Word> useInstanceSpecific(Collection<Word> wordsToFilter, Word specificWord) {
        return null;
    }

    @Override
    public boolean weaken() {
        this.weaken(.1);
        return true;
    }

}
