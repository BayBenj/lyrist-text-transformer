package word2vec;

import song.Word;

public class SimilarInput extends W2vInput {
//TODO: Delete this class becuase it only holds 1 Word object? Decide.

    private String word;

    public SimilarInput(String string) {
        this.setWord(string);
    }

    public SimilarInput(Word word) {
        this.setWord(word.getSpelling());
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean isComplete() {
        if (this.getWord() != null)
            return true;
        return false;
    }
}




















































































