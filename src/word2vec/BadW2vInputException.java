package word2vec;

public class BadW2vInputException extends Exception {

    public BadW2vInputException(String badString) {
        super(badString);
    }
}
