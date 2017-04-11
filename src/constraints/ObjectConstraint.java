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

        if (objects == null)
            return null;
        if (objects.isEmpty())
            if (returnType == ReturnType.MATCHES)
                return new HashSet<>();
            else
                return new HashSet<>(wordsToFilter);
        return this.filterByMultiple(wordsToFilter, objects, returnType);
    }

    @Override
    public Set<Word> useOldWordSpecific(Collection<Word> wordsToFilter, Word oldWord) {
        this.objects = new HashSet<>();
        this.objects.add(this.wordToSpecificObjectType(oldWord));
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

    public void setObjects(Collection<Object> objects) {
        this.objects = new HashSet<>(objects);
    }

    @Override
    public String toString() {
        return "ObjectConstraint";
    }

}




















































