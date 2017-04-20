package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.*;

public class RhymeModelScoreConstraint extends RhymeScoreConstraint {

    public RhymeModelScoreConstraint(NonModelNum comparison) {
        super(comparison);
        this.oldWordSpecific = false;
    }

    public RhymeModelScoreConstraint(ModelNum comparison, double dbl, ReturnType returnType) {
        super(comparison, dbl, returnType);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transforming~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Map<Word, Double> wordsToSpecficDoubleType(Collection<Word> words) {
        return wordsToRhymeScores(words);
    }

    @Override
    public Double wordToSpecificDoubleType(Word word) {
        return word.getRhymeScore();
    }

    private static Map<Word,Double> wordsToRhymeScores(Collection<Word> words) {
        Map<Word,Double> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getRhymeScore());
        return result;
    }

    @Override
    public boolean weaken() {
        return this.weaken(.025);
    }

    @Override
    public String toString() {
        return "RhymeModelScoreConstraint";
    }

}


































































