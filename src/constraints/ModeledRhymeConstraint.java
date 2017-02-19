package constraints;

import elements.Word;
import filters.ReturnType;
import rhyme.SyllableGroup;

import java.util.*;

public class ModeledRhymeConstraint extends ObjectConstraint {

    private RhymeScoreConstraint rhymeScoreConstraint;

    public ModeledRhymeConstraint(Set<Word> words, ReturnType returnType, double threshold) {
        super(returnType);
        this.objects = new HashSet<>(wordsToRhymeObjects(words).values());
        rhymeScoreConstraint.setDbl(threshold);
    }

    public ModeledRhymeConstraint(ReturnType returnType, double threshold) {
        super(returnType);
        this.instanceSpecific = true;
        rhymeScoreConstraint.setDbl(threshold);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Word transformers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Object wordToSpecificObjectType(Word word) {
        return wordToRhymeObject(word);
    }

    @Override
    public Map<Word, Object> wordsToSpecificObjectType(Collection<Word> words) {
        return wordsToRhymeObjects(words);
    }

    protected static Object wordToRhymeObject(Word word) {
        return wordToRhyme(word);
    }

    protected static Map<SyllableGroup,Object> rhymesToRhymeObjects(Collection<SyllableGroup> rhymes) {
        Map<SyllableGroup,Object> result = new HashMap<>();
        for (SyllableGroup sg : rhymes)
            result.put(sg, sg);
        return result;
    }

    protected static Map<Word,Object> wordsToRhymeObjects(Collection<Word> words) {
        Map<Word, SyllableGroup> var = wordsToRhymes(words);
        Map<Word,Object> result = new HashMap<>();
        for (Map.Entry<Word,SyllableGroup> entry : var.entrySet())
            result.put(entry.getKey(), entry.getKey().getLowerSpelling());
        return result;
    }

    protected static SyllableGroup wordToRhyme(Word word) {
        return word.getFullRhyme();
    }

    protected static Map<Word,SyllableGroup> wordsToRhymes(Collection<Word> words) {
        Map<Word,SyllableGroup> result = new HashMap<>();
        for (Word w : words)
            result.put(w, w.getFullRhyme());
        return result;
    }

}





































