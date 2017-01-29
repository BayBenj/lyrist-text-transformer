package constraints;

import elements.Word;
import filters.ReturnType;

import java.util.*;

public abstract class ObjectConstraint extends WordConstraint {

    protected Set<Object> objects;

    public ObjectConstraint(Set<Object> objects, ReturnType returnType) {
        super(returnType);
        this.objects = objects;
    }

    public ObjectConstraint(Object object, ReturnType returnType) {
        super(returnType);
        this.objects = new HashSet<>();
        this.objects.add(object);
    }

    public ObjectConstraint(ReturnType returnType) {
        super(returnType);
    }

    @Override
    public Set<Word> useWithPresetFields(Collection<Word> wordsToFilter) {
        if (objects != null && !objects.isEmpty())
            return this.filterByMultiple(wordsToFilter, objects, returnType);
        return null;
    }

    @Override
    public Set<Word> useInstanceSpecific(Collection<Word> wordsToFilter, Word specificWord) {
        this.objects = new HashSet<>();
        this.objects.add(this.wordToSpecificObjectType(specificWord));
        return this.useWithPresetFields(wordsToFilter);
    }

    public Set<Word> filterByMultiple(Collection<Word> words, Set<Object> modelObjects, ReturnType returnType) {
        Map<Word, Object> map = wordsToSpecificObjectType(words);
        map.values().retainAll(getContained(modelObjects, map.values(), returnType));
        return map.keySet();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~Weaken~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean weaken() {
        return false;
    }

    public abstract Map<Word, Object> wordsToSpecificObjectType(Collection<Word> words);

    public abstract Object wordToSpecificObjectType(Word word);

    public Set<Object> getObjects() {
        return objects;
    }

    public void setObjects(Set<Object> objects) {
        this.objects = objects;
    }

}





















































