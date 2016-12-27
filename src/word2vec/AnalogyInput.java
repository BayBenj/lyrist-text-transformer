package word2vec;

import song.Word;

import java.util.ArrayList;

public class AnalogyInput extends W2vInput {

    private String oldSentiment;
    private String newSentiment;
    private String oldWord;

    public AnalogyInput(String newSentiment, String oldWord) {
        // TODO only keep this if new sentiment is not discovered w/ word2vec
        this.setNewSentiment(newSentiment);
        this.setOldWord(oldWord);
    }

    public AnalogyInput(Word newSentiment, Word oldWord) {
        // TODO only keep this if new sentiment is not discovered w/ word2vec
        this.setNewSentiment(newSentiment.getSpelling());
        this.setOldWord(oldWord.getSpelling());
    }

    public AnalogyInput(String oldSentiment, String newSentiment, String oldWord) {
        this.setOldSentiment(oldSentiment);
        this.setNewSentiment(newSentiment);
        this.setOldWord(oldWord);
    }

    public AnalogyInput(Word oldSentiment, Word newSentiment, Word oldWord) {
        this.setOldSentiment(oldSentiment.getSpelling());
        this.setNewSentiment(newSentiment.getSpelling());
        this.setOldWord(oldWord.getSpelling());
    }

    public String getOldSentiment() {
        return oldSentiment;
    }

    public void setOldSentiment(String oldSentiment) {
        this.oldSentiment = oldSentiment;
    }

    public String getNewSentiment() {
        return newSentiment;
    }

    public void setNewSentiment(String newSentiment) {
        this.newSentiment = newSentiment;
    }

    public String getOldWord() {
        return oldWord;
    }

    public void setOldWord(String oldWord) {
        this.oldWord = oldWord;
    }

    @Override
    public boolean isComplete() {
        if (this.getOldSentiment() != null && this.getNewSentiment() != null && this.getOldWord() != null)
            return true;
        return false;
    }

}




















































































