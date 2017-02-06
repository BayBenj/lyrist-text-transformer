package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.*;

public class WithinStringConstraint extends ObjectConstraint {

    public WithinStringConstraint(Collection<char[]> chars, ReturnType returnType) {
        super(returnType);
        this.objects = new HashSet<>(charsToCharObjects(chars).values());
    }

    public WithinStringConstraint(Set<Word> words, ReturnType returnType) {
        super(returnType);
        this.objects = new HashSet<>(wordsToCharObjects(words).values());
    }

    public WithinStringConstraint(ReturnType returnType) {
        super(returnType);
        this.instanceSpecific = true;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transformers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Object wordToSpecificObjectType(Word word) {
        return wordToCharObject(word);
    }

    @Override
    public Map<Word, Object> wordsToSpecificObjectType(Collection<Word> words) {
        return wordsToCharObjects(words);
    }

    protected static Map<char[],Object> charsToCharObjects(Collection<char[]> words) {
        Map<char[],Object> result = new HashMap<>();
        for (char[] s : words)
            result.put(s, s);
        return result;
    }

    protected static Object wordToCharObject(Word word) {
        return wordToChar(word);
    }

    protected static Map<Word,Object> wordsToCharObjects(Collection<Word> words) {
        Map<Word, char[]> var = wordsToChars(words);
        Map<Word,Object> result = new HashMap<>();
        for (Map.Entry<Word,char[]> entry : var.entrySet())
            result.put(entry.getKey(), entry.getKey().getLowerSpelling().toCharArray());
        return result;
    }

    protected static char[] wordToChar(Word word) {
        return word.getLowerSpelling().toCharArray();
    }

    protected static Map<Word,char[]> wordsToChars(Collection<Word> words) {
        Map<Word,char[]> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getLowerSpelling().toCharArray());
        return result;
    }

}










































