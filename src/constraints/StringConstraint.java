package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.*;

public class StringConstraint extends ObjectConstraint {

    public StringConstraint(Collection<String> strings, ReturnType returnType) {
        super(returnType);
        this.objects = new HashSet<>(stringsToStringObjects(strings).values());
    }

    public StringConstraint(Set<Word> words, ReturnType returnType) {
        super(returnType);
        this.objects = new HashSet<>(wordsToStringObjects(words).values());
    }

    public StringConstraint(ReturnType returnType) {
        super(returnType);
        this.instanceSpecific = true;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transformers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Object wordToSpecificObjectType(Word word) {
        return wordToStringObject(word);
    }

    @Override
    public Map<Word, Object> wordsToSpecificObjectType(Collection<Word> words) {
        return wordsToStringObjects(words);
    }

    protected static Map<String,Object> stringsToStringObjects(Collection<String> words) {
        Map<String,Object> result = new HashMap<>();
        for (String s : words)
            result.put(s, s);
        return result;
    }

    protected static Object wordToStringObject(Word word) {
        return wordToString(word);
    }

    protected static Map<Word,Object> wordsToStringObjects(Collection<Word> words) {
        Map<Word, String> var = wordsToString(words);
        Map<Word,Object> result = new HashMap<>();
        for (Map.Entry<Word,String> entry : var.entrySet())
            result.put(entry.getKey(), entry.getKey().getLowerSpelling());
        return result;
    }

    protected static String wordToString(Word word) {
        return word.getLowerSpelling();
    }

    protected static Map<Word,String> wordsToString(Collection<Word> words) {
        Map<Word,String> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getLowerSpelling());
        return result;
    }

}










































