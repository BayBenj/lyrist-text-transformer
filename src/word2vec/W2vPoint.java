package word2vec;

import song.Word;

public class W2vPoint extends W2vResult {

    private double[] point;
    private long[] inputWordVocabPositions;
    private int number_of_input_words;
    private String word;

    public W2vPoint(double[] point, long[] inputWordVocabPositions, int number_of_input_words) {
        this.setPoint(point);
        this.setInputWordVocabPositions(inputWordVocabPositions);
        this.setNumber_of_input_words(number_of_input_words);
    }

    public long[] getInputWordVocabPositions() {
        return inputWordVocabPositions;
    }

    public void setInputWordVocabPositions(long[] inputWordVocabPositions) {
        this.inputWordVocabPositions = inputWordVocabPositions;
    }

    public int getNumber_of_input_words() {
        return number_of_input_words;
    }

    public void setNumber_of_input_words(int number_of_input_words) {
        this.number_of_input_words = number_of_input_words;
    }

    public double[] getPoint() {
        return point;
    }

    public void setPoint(double[] point) {
        this.point = point;
    }

    public String getString() {
        if (word == null || word.equals(""))
            return "NO_WORD_YET";
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
























































































