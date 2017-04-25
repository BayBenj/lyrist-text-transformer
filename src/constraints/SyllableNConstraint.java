package constraints;

import elements.Word;
import filters.ReturnType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SyllableNConstraint extends IntConstraint {

    public SyllableNConstraint(NonModelNum comparison) {
        super(comparison);
        this.oldWordSpecific = false;
    }

    public SyllableNConstraint(ModelNum comparison) {
        super(comparison);
        this.oldWordSpecific = true;
    }

    public SyllableNConstraint(ModelNum comparison, int i, ReturnType returnType) {
        super(comparison, i, returnType);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transforming~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Map<Word, Integer> wordsToSpecficIntType(Collection<Word> words) {
        return wordsToSyllableN(words);
    }

    @Override
    public Integer wordToSpecficIntType(Word word) {
        return word.getSyllables().size();
    }

    private static Map<Word,Integer> wordsToSyllableN(Collection<Word> words) {
        Map<Word,Integer> result = new HashMap<>();
        for (Word w : words) {
            if (w.getSyllables() == null || w.getSyllables().isEmpty())
                result.put(w, 0);
            else
                result.put(w, w.getSyllables().size());
        }
        return result;
    }

    @Override
    public boolean weaken() {
        return false;
    }

    @Override
    public String toString() {
        return "SyllableNConstraint";
    }

}
