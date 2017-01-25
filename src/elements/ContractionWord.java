package elements;

import java.util.List;

public class ContractionWord extends Word {

    private List<Word> allWords = null;

    public ContractionWord(String s, List<Word> allWords) {
        super(s);
        this.setAllWords(allWords);
    }

    @Override
    public List<Word> getAllWords() {
        return allWords;
    }

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }
}
