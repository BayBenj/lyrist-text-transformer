package song;

import java.util.ArrayList;
import java.util.List;

public abstract class SongElement {

    private SongElement superElement = null;
    private ArrayList<SongElement> subElements = null;

    public final void add(SongElement se) {
        if (this.getSubElements() == null)
            this.setSubElements(new ArrayList<SongElement>());
        this.getSubElements().add(se);
    }

    public final void add(ArrayList<SongElement> list) {
        list.forEach(this::add);
        //TODO: familiarize myself with the above syntax
    }

    public SongElement get(int index) {
        ArrayList<? extends SongElement> subelements = this.getSubElements();
        if (subelements != null && subelements.get(index) != null)
            return subelements.get(index);
        return null;
        //TODO throw error
    }

    public List<Word> getAllWords() {
        List<SongElement> allWordsAsElements = this.getSubElementsRecursively(new Word(""), new ArrayList<SongElement>());
        List<Word> allWords = new ArrayList<Word>();
        for (SongElement se : allWordsAsElements)
            allWords.add((Word)se);
        return allWords;
    }

    public List<SongElement> getAllSubElementsOfType(SongElement baseCase) {
        return this.getSubElementsRecursively(baseCase, new ArrayList<SongElement>());
    }

    private List<SongElement> getSubElementsRecursively(SongElement baseCase, ArrayList<SongElement> result) {
        if (this.getClass().equals(baseCase.getClass()))
            result.add(this);
        else
            for (int i = 0; i < this.getSubElements().size(); i++) {
                SongElement nextSubElement = this.getSubElements().get(i);
                nextSubElement.getSubElementsRecursively(baseCase, result);
            }
        return result;
    }

    public boolean hasCompleteStructure() {
        return (hasCompleteSpellingStructure() &&
                hasCompletePosStructure()      &&
                hasCompletePhonemeStructure()  &&
                hasCompleteSyllableStructure() &&
                hasCompleteStressStructure());
    }

    public boolean hasCompleteStructure(Structure structure) {
        //TODO: eventually move all this switch business down to the Word level
        switch(structure) {
            case SYLLABLE:
                return this.hasCompleteSyllableStructure();
            case STRESS:
                return this.hasCompleteStressStructure();
            case PHONEME:
                return this.hasCompletePhonemeStructure();
            case POS:
                return this.hasCompletePosStructure();
            case GRAMMAR:
                return false;
            default:
                return false;
        }
    }

    public boolean hasCompleteSpellingStructure() {
        ArrayList<SongElement> subElements = this.getSubElements();
        for (SongElement se : subElements) {
            if (!se.hasCompleteSpellingStructure())
                return false;
        }
        return true;
    }

    public boolean hasCompletePosStructure() {
        ArrayList<SongElement> subElements = this.getSubElements();
        for (SongElement se : subElements) {
            if (!se.hasCompletePosStructure())
                return false;
        }
        return true;
    }

    public boolean hasCompletePhonemeStructure() {
        ArrayList<SongElement> subElements = this.getSubElements();
        for (SongElement se : subElements) {
            if (!se.hasCompletePhonemeStructure())
                return false;
        }
        return true;
    }

    public boolean hasCompleteSyllableStructure() {
        ArrayList<SongElement> subElements = this.getSubElements();
        for (SongElement se : subElements) {
            if (!se.hasCompleteSyllableStructure())
                return false;
        }
        return true;
    }

    public boolean hasCompleteStressStructure() {
        ArrayList<SongElement> subElements = this.getSubElements();
        for (SongElement se : subElements) {
            if (!se.hasCompleteStressStructure())
                return false;
        }
        return true;
    }

    public int getSize() {
        return this.getSubElements().size();
    }

    protected boolean hasInsideSomething() {
        if (this.getSuperElement() == null)
            return false;
        return true;
    }

    protected boolean hasCarryingSomething() {
        if (this.getSubElements() == null)
            return false;
        return true;
    }

    protected SongElement getSuperElement() {
        return this.superElement;
    }

    protected void setSuperElement(SongElement superElement) {
        this.superElement = superElement;
    }

    protected ArrayList<SongElement> getSubElements() {
        return this.subElements;
    }

    protected void setSubElements(ArrayList<SongElement> subElements) {
        this.subElements = subElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SongElement songElement = (SongElement) o;

        ArrayList<SongElement> thisSubElements = this.getSubElements();
        ArrayList<SongElement> thatSubElements = songElement.getSubElements();
        for (int i = 0; i < thisSubElements.size(); i++) {
            if (!thisSubElements.get(i).equals(thatSubElements.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = this.getSubElements() != null ? getSubElements().hashCode() : 0;
        return result;
    }

}


























































/*
I need to be able to call getStructure(SongElement se, Structure... structures)
    And get back a SongElement of only the desired structures. Maybe.

I need to be able to call getAttributes(SongElement se, Attributes... attributes)
    And get back an object (or String) with all my requested attributes for that SongElement.

> Make new kind of Word for this? Probably not.

> Make interesting ways to output structure combinations.
 */




/*
public means other classes can use it
protected means Stanzas, Lines, and Words can use it
private means only stuff in this class can use it
 */



























































