package word2vec;

public class W2vPoint extends W2vResult {

    private double[] point;
    private int vocabPosition;
    private String word;

    public W2vPoint(double[] point) {
        this.setPoint(point);
    }

    public int getInputWordVocabPosition() {
        return vocabPosition;
    }

    public void setInputWordVocabPosition(int vocabPosition) {
        this.vocabPosition = vocabPosition;
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






















































































