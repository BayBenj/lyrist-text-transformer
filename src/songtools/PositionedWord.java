package songtools;

import elements.Word;

public class PositionedWord extends Word {

    private int stanzaIndex;
    private int lineIndex;
    private int wordIndex;

    public PositionedWord(Word word, int stanzaIndex, int lineIndex, int wordIndex) {
        super(word.getLowerSpelling());
        super.setPhonemes(word.getPhonemes());
        super.setSyllables(word.getSyllables());
        super.setPos(word.getPos());
        super.setNe(word.getNe());
        super.setCapitalized(word.getCapitalized());
        this.setStanzaIndex(stanzaIndex);
        this.setLineIndex(lineIndex);
        this.setWordIndex(wordIndex);
    }

    public int getStanzaIndex() {
        return stanzaIndex;
    }

    public void setStanzaIndex(int stanzaIndex) {
        this.stanzaIndex = stanzaIndex;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public int getWordIndex() {
        return wordIndex;
    }

    public void setWordIndex(int wordIndex) {
        this.wordIndex = wordIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PositionedWord that = (PositionedWord) o;

        if (getStanzaIndex() != that.getStanzaIndex()) return false;
        if (getLineIndex() != that.getLineIndex()) return false;
        return getWordIndex() == that.getWordIndex();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getStanzaIndex();
        result = 31 * result + getLineIndex();
        result = 31 * result + getWordIndex();
        return result;
    }
}



























































































