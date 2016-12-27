package word2vec;

import java.io.Serializable;

import static java.lang.Math.toIntExact;

public class W2vModel implements Serializable {
//Container class for a W2vModel

    private final long numberOfWordsInVector;
    private final long numberOfDimensionsInVector ;
    private float[] M;
    private char[] vocab;

    public W2vModel(long numberOfWordsInVector,
                    long numberOfDimensionsInVector,
                    float[] M,
                    char[] vocab) {
        this.numberOfWordsInVector = numberOfWordsInVector;
        this.numberOfDimensionsInVector = numberOfDimensionsInVector;
        this.setM(M);
        this.setVocab(vocab);
    }

    public char getVocabAt(long index) {
        return vocab[toIntExact(index)];
    }

    public char getVocabAt(int index) {
        return vocab[index];
    }

    public float getMAt(long index) {
        return M[toIntExact(index)];
    }

    public float getMAt(int index) {
        return M[index];
    }

    public void setVocabAt_To_(long index, char ch) {
        vocab[toIntExact(index)] = ch;
    }

    public void setVocabAt_To_(int index, char ch) {
        vocab[index] = ch;
    }

    public void divideMAt_To_(long index, double d) {
        M[toIntExact(index)] /= (d);
    }

    public long getNumberOfWordsInVector() {
        return numberOfWordsInVector;
    }

    public long getNumberOfDimensionsInVector() {
        return numberOfDimensionsInVector;
    }

    public float[] getM() {
        return M;
    }

    public void setM(float[] m) {
        M = m;
    }

    public char[] getVocab() {
        return vocab;
    }

    public void setVocab(char[] vocab) {
        this.vocab = vocab;
    }
}












































































































