package constraints;

import elements.Ne;
import elements.Word;
import filters.ReturnType;

import java.util.*;

public class NeConstraint extends ObjectConstraint {

    public NeConstraint(Collection<Ne> nes, ReturnType returnType) {
        super(returnType);
        this.objects = new HashSet<>(neToNeObjects(nes).values());
    }

    public NeConstraint(Ne ne, ReturnType returnType) {
        super(returnType);
        Set<Object> set = new HashSet<>();
        set.add(ne);
        this.objects = set;
    }

    public NeConstraint(ReturnType returnType) {
        super(returnType);
        this.oldWordSpecific = true;
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

    protected static Map<Ne,Object> neToNeObjects(Collection<Ne> ne) {
        Map<Ne,Object> result = new HashMap<>();
        for (Ne n : ne)
            result.put(n, n);
        return result;
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

    @Override
    public String toString() {
        return "NeConstraint";
    }
}























































