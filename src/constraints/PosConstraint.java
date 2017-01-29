package constraints;

import elements.Pos;
import elements.Word;
import filters.ReturnType;

import java.util.*;

public class PosConstraint extends ObjectConstraint {

    public PosConstraint(Collection<Pos> parts, ReturnType returnType) {
        super(parts, returnType);
    }

    public PosConstraint(Pos part, ReturnType returnType) {
        super(part, returnType);
    }

    public PosConstraint(ReturnType returnType) {
        super(returnType);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transformers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Object wordToSpecificObjectType(Word word) {
        return wordToPosObject(word);
    }

    @Override
    public Map<Word, Object> wordsToSpecificObjectType(Collection<Word> words) {
        return wordsToPosObjects(words);
    }

    protected static Object wordToPosObject(Word word) {
        return wordToPos(word);
    }

    protected static Map<Word,Object> wordsToPosObjects(Collection<Word> words) {
        Map<Word,Pos> var = wordsToPos(words);
        Map<Word,Object> result = new HashMap<>();
        for (Map.Entry<Word,Pos> entry : var.entrySet())
            result.put(entry.getKey(), entry.getKey().getPos());
        return result;
    }

    protected static Pos wordToPos(Word word) {
        return word.getPos();
    }

    protected static Map<Word,Pos> wordsToPos(Collection<Word> words) {
        Map<Word,Pos> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getPos());
        return result;
    }




}











































