package elements;

import rhyme.*;

public class Word extends SongElement implements Comparable<Word> {

    private String spelling = null;
    private VowelPronunciation phonemes = null;
    private WordSyllables syllables = null;
    private Pos pos = null;
    private NamedEntity ne = null;
    private boolean capitalized = false;

    private double cosineDistance = Double.MIN_VALUE;
    private double rhymeScore = Double.MIN_VALUE;

    public Word(String s) {
        this.setSpelling(s);
    }

    public SyllableGroup getFullRhyme() {
        return this.syllables.getWordRhymeFromStress();
    }

    public String getLowerSpelling() {
        return this.spelling.toLowerCase();
    }

    public String getUpperSpelling() {
        return this.spelling.toUpperCase();
    }

    public void setSpelling(String spelling) {
        if (spelling.length() > 0 && Character.isUpperCase(spelling.charAt(0)))
            this.capitalized = true;
        this.spelling = spelling.toLowerCase();
    }

    public VowelPronunciation getPhonemes() {
        return this.phonemes;
    }

    public WordSyllables getSyllables() {
        return this.syllables;
    }

    public void setSyllables(WordSyllables syllables) {
        this.syllables = syllables;
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

    public void setPhonemes(VowelPronunciation vowelPronunciation) {
        this.phonemes = vowelPronunciation;
    }

    public double getCosineDistance() {
        return cosineDistance;
    }

    public void setCosineDistance(double cosineDistance) {
        this.cosineDistance = cosineDistance;
    }

    public double getRhymeScore() {
        return rhymeScore;
    }

    public void setRhymeScore(double rhymeScore) {
        this.rhymeScore = rhymeScore;
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
        if (this.getLowerSpelling() == null || this.getLowerSpelling().equals(""))
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
    public String toString() {
        if (this.capitalized)
            return this.getLowerSpelling().substring(0, 1).toUpperCase() + this.getLowerSpelling().substring(1);
        return this.getLowerSpelling();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (getLowerSpelling() != null ? !getLowerSpelling().equals(word.getLowerSpelling()) : word.getLowerSpelling() != null)
            return false;
        if (getPhonemes() != null ? !getPhonemes().equals(word.getPhonemes()) : word.getPhonemes() != null)
            return false;
        if (getSyllables() != null ? !getSyllables().equals(word.getSyllables()) : word.getSyllables() != null)
            return false;
        return getPos() == word.getPos();
    }

    @Override
    public int hashCode() {
        int result = getLowerSpelling() != null ? getLowerSpelling().hashCode() : 0;
        result = 31 * result + (getPhonemes() != null ? getPhonemes().hashCode() : 0);
        result = 31 * result + (getSyllables() != null ? getSyllables().hashCode() : 0);
        result = 31 * result + (getPos() != null ? getPos().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Word o) {
        return this.getLowerSpelling().compareTo(o.getLowerSpelling());
    }
}



























































