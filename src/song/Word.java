package song;

import rhyme.*;

import java.util.ArrayList;
import java.util.List;

public class Word extends SongElement implements Comparable<Word> {

    private String spelling = null;
    private Pronunciation phonemes = null;
    private WordSyllables syllables = null;
    private List<Integer> stresses = null;
    private Pos pos = null;
    private NamedEntity ne = null;
    private boolean capitalized = false;

    public Word(String s) {
        this.setSpelling(s);
    }

    public String getSpelling() {
        return this.spelling.toLowerCase();
    }

    public void setSpelling(String spelling) {
        if (spelling.length() > 0 && Character.isUpperCase(spelling.charAt(0)))
            this.capitalized = true;
        this.spelling = spelling.toLowerCase();
    }

    public Pronunciation getPhonemes() {
        return this.phonemes;
    }

    public WordSyllables getSyllables() {
        return this.syllables;
    }

    public void setSyllables(WordSyllables syllables) {
        this.syllables = syllables;
    }

    public List<Integer> getStresses() {
        return this.stresses;
    }

    private void setStresses(List<Integer> stresses) {
        this.stresses = stresses;
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public NamedEntity getNe() {
        return ne;
    }

    public void setNe(NamedEntity ne) {
        this.ne = ne;
    }

    public boolean getCapitalized() {
        return capitalized;
    }

    public void setCapitalized(boolean b) {
        this.capitalized = b;
    }

    public void setPhonemes(Pronunciation pronunciation) {
        this.phonemes = pronunciation;
        List<Integer> tempStresses = new ArrayList<>();
        if (pronunciation != null) {
            for (StressedPhoneme sp : pronunciation) {
                tempStresses.add(sp.stress);
            }
            this.setStresses(tempStresses);
        }
    }

    @Override
    public Line getSuperElement() {
        return (Line)super.getSuperElement();
        // TODO: Catch ClassCastException
    }

    @Override
    public void setSuperElement(SongElement line) {
        super.setSuperElement((Line)line);
        // TODO: Catch ClassCastException
    }

    public Line getLine() {
        return this.getSuperElement();
    }

    public void setLine(Line line) {
        this.setSuperElement(line);
        // TODO: Catch ClassCastException
    }

    @Override
    public boolean hasCompleteSpellingStructure() {
        if (this.getSpelling() == null || this.getSpelling().equals(""))
            return false;
        return true;
    }

    @Override
    public boolean hasCompletePosStructure() {
        if (this.getPos() == null)
            return false;
        return true;
    }

    @Override
    public boolean hasCompletePhonemeStructure() {
        if (this.getPhonemes() == null || this.getPhonemes().isEmpty())
            return false;
        return true;
    }

    @Override
    public boolean hasCompleteSyllableStructure() {
        if (this.getSyllables() == null || this.getSyllables().isEmpty())
            return false;
        return true;
    }

    @Override
    public boolean hasCompleteStressStructure() {
        if (this.getStresses() == null || this.getStresses().isEmpty())
            return false;
        return true;
    }

    @Override
    public String toString() {
        if (this.capitalized)
            return this.getSpelling().substring(0, 1).toUpperCase() + this.getSpelling().substring(1);
        return this.getSpelling();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (getSpelling() != null ? !getSpelling().equals(word.getSpelling()) : word.getSpelling() != null)
            return false;
        if (getPhonemes() != null ? !getPhonemes().equals(word.getPhonemes()) : word.getPhonemes() != null)
            return false;
        if (getSyllables() != null ? !getSyllables().equals(word.getSyllables()) : word.getSyllables() != null)
            return false;
        if (getStresses() != null ? !getStresses().equals(word.getStresses()) : word.getStresses() != null)
            return false;
        return getPos() == word.getPos();
    }

    @Override
    public int hashCode() {
        int result = getSpelling() != null ? getSpelling().hashCode() : 0;
        result = 31 * result + (getPhonemes() != null ? getPhonemes().hashCode() : 0);
        result = 31 * result + (getSyllables() != null ? getSyllables().hashCode() : 0);
        result = 31 * result + (getStresses() != null ? getStresses().hashCode() : 0);
        result = 31 * result + (getPos() != null ? getPos().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Word o) {
        return this.getSpelling().compareTo(o.getSpelling());
    }
}



































































