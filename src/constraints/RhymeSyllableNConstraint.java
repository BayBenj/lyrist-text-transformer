package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RhymeSyllableNConstraint extends IntConstraint {

    public RhymeSyllableNConstraint(NonModelNum comparison) {
        super(comparison);
        this.instanceSpecific = false;
    }

    public RhymeSyllableNConstraint(ModelNum comparison) {
        super(comparison);
        this.instanceSpecific = true;
    }

    public RhymeSyllableNConstraint(ModelNum comparison, int i, ReturnType returnType) {
        super(comparison, i, returnType);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transforming~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Map<Word, Integer> wordsToSpecficIntType(Collection<Word> words) {
        return wordsToSyllableN(words);
    }

    @Override
    public Integer wordToSpecficIntType(Word word) {
        return word.getFullRhyme().size();
    }

    private static Map<Word,Integer> wordsToSyllableN(Collection<Word> words) {
        Map<Word,Integer> result = new HashMap<>();
        for (Word w : words) {
            if (w.getFullRhyme() == null || w.getFullRhyme().isEmpty())
                result.put(w, 0);
            else
                result.put(w, w.getFullRhyme().size());
        }
        return result;
    }

    @Override
    public boolean weaken() {
        return false;
    }

}
