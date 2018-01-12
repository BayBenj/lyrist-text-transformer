package elements;

import rhyme.*;

import java.io.Serializable;
import java.util.List;

public class Word implements Comparable<Word>, Serializable {

    private String spelling = null;
    private String base = null;
    private List<WordSyllables> pronunciations = null;
    private int chosenPronunciationIndex = 0;//TODO change this to use the optimal pronunciation instead of just the first
    private Pos pos = null;
    private Ne ne = null;
    private boolean capitalized = false;

    private double cosineSimilarity = Double.MIN_VALUE;
    private double rhymeScore = Double.MIN_VALUE;
    private String rhymeWord = "[uninitialized]";

    private Sentence sentence;
    private int sentenceIndex;
    private Line line;

    public Word(String s) {
        this.setSpelling(s);
    }

    public String getRhymeWord() {
        return rhymeWord;
    }

    public void setRhymeWord(String rhymeWord, double score) {
        this.rhymeWord = rhymeWord;
        this.rhymeScore = score;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public SyllableGroup getRhymeTail() {
        if (this.pronunciations == null)
            return null;
        return this.pronunciations.get(chosenPronunciationIndex).getRhymeTailFromStress();
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

    public Pronunciation getPhonemes() {
        return this.pronunciations.get(chosenPronunciationIndex).getPronunciation();
    }

    public List<WordSyllables> getPronunciations() {
        return this.pronunciations;
    }

    public void setPronunciations(List<WordSyllables> pronunciations) {
        this.pronunciations = pronunciations;
    }

    public WordSyllables getPronunciation() {
        return this.pronunciations.get(chosenPronunciationIndex);
    }

    public WordSyllables getSyllables() {
        return this.getPronunciation();
    }

    public void setPronunciation(int i) {
        this.chosenPronunciationIndex = i;
    }

    public void setSyllables(int i) {
        this.setPronunciation(i);
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public Ne getNe() {
        return ne;
    }

    public void setNe(Ne ne) {
        this.ne = ne;
    }

    public boolean getCapitalized() {
        return capitalized;
    }

    public void setCapitalized(boolean b) {
        this.capitalized = b;
    }

    public double getCosineSimilarity() {
        return cosineSimilarity;
    }

    public void setCosineSimilarity(double cosineSimilarity) {
        this.cosineSimilarity = cosineSimilarity;
    }

    public double getRhymeScore() {
        return rhymeScore;
    }

    public void setRhymeScore(String rhymeWord, double rhymeScore) {
        this.rhymeScore = rhymeScore;
        this.rhymeWord = rhymeWord;
    }

    public void clearRhymeScore() {
        this.rhymeScore = Double.MIN_VALUE;
        this.rhymeWord = "[uninitialized]";
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public int getSentenceIndex() {
        return sentenceIndex;
    }

    public void setSentenceIndex(int sentenceIndex) {
        this.sentenceIndex = sentenceIndex;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    //    @Override
//    public Line getSuperElement() {
//        return (Line)super.getSuperElement();
//        // TODO: Catch ClassCastException
//    }
//
//    @Override
//    public void setSuperElement(SongElement line) {
//        super.setSuperElement((Line)line);
//        // TODO: Catch ClassCastException
//    }


//    @Override
//    public boolean hasCompleteSpellingStructure() {
//        if (this.getLowerSpelling() == null || this.getLowerSpelling().equals(""))
//            return false;
//        return true;
//    }
//
//    @Override
//    public boolean hasCompletePosStructure() {
//        if (this.getPos() == null)
//            return false;
//        return true;
//    }
//
//    @Override
//    public boolean hasCompletePhonemeStructure() {
//        if (this.getPhonemes() == null || this.getPhonemes().isEmpty())
//            return false;
//        return true;
//    }
//
//    @Override
//    public boolean hasCompleteSyllableStructure() {
//        if (this.getPronunciations() == null || this.getPronunciations().isEmpty())
//            return false;
//        return true;
//    }

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
        if (getPronunciations() != null ? !getPronunciations().equals(word.getPronunciations()) : word.getPronunciations() != null)
            return false;
        return true;
//        return getPos() == word.getPos();
    }

    @Override
    public int hashCode() {
        int result = getLowerSpelling() != null ? getLowerSpelling().hashCode() : 0;
        result = 31 * result + (getPronunciations() != null ? getPronunciations().hashCode() : 0);
//        result = 31 * result + (getPos() != null ? getPos().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Word o) {
        return this.getLowerSpelling().compareTo(o.getLowerSpelling());
    }
}



























































