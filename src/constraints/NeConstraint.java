package constraints;

import elements.Ne;
import elements.Word;
import filters.ReturnType;

import java.util.*;

public class NeConstraint extends ObjectConstraint {

    public NeConstraint(Collection<Ne> nes, ReturnType returnType) {
        super(nes, returnType);
    }

    public NeConstraint(Ne ne, ReturnType returnType) {
        super(ne, returnType);
    }

    public NeConstraint(ReturnType returnType) {
        super(returnType);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transformers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Object wordToSpecificObjectType(Word word) {
        return wordToNeObject(word);
    }

    @Override
    public Map<Word, Object> wordsToSpecificObjectType(Collection<Word> words) {
        return wordsToNeObjects(words);
    }

    protected static Object wordToNeObject(Word word) {
        return wordToNe(word);
    }

    protected static Map<Word,Object> wordsToNeObjects(Collection<Word> words) {
        Map<Word, Ne> var = wordsToNe(words);
        Map<Word,Object> result = new HashMap<>();
        for (Map.Entry<Word,Ne> entry : var.entrySet())
            result.put(entry.getKey(), entry.getKey().getNe());
        return result;
    }

    protected static Ne wordToNe(Word word) {
        return word.getNe();
    }

    protected static Map<Word,Ne> wordsToNe(Collection<Word> words) {
        Map<Word,Ne> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getNe());
        return result;
    }

}























































