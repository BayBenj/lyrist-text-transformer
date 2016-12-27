//package word2vec;
//
//import song.Word;
//
//import java.util.Comparator;
//
//public class W2vWordSuggestion implements Comparator<W2vWordSuggestion>, Comparable<W2vWordSuggestion> {
//
//    private Word word;
//    private Double cosineDistance;
//
//    public W2vWordSuggestion(Word word, Double cosineDistance) {
//        this.setWord(word);
//        this.setCosineDistance(cosineDistance);
//    }
//
//    public Word getString() {
//        return word;
//    }
//
//    public void setWord(Word word) {
//        this.word = word;
//    }
//
//    public Double getCosineDistance() {
//        return cosineDistance;
//    }
//
//    public void setCosineDistance(Double cosineDistance) {
//        this.cosineDistance = cosineDistance;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//        if (o == null || getClass() != o.getClass())
//            return false;
//        W2vWordSuggestion that = (W2vWordSuggestion) o;
//        if (this.getString() != null ? !this.getString().equals(that.getString()) : that.getString() != null)
//            return false;
//        return this.getCosineDistance() != null ? this.getCosineDistance().equals(that.getCosineDistance()) : that.getCosineDistance() == null;
//    }
//
//    @Override
//    public int hashCode() {
//        //TODO: Designed this way for hash structures to organize this by cosine distance. Is this right?
//        int result = this.getCosineDistance() != null ? this.getCosineDistance().hashCode() : 0;
//        return result;
//    }
//
//    @Override
//    public int compare(W2vWordSuggestion o1, W2vWordSuggestion o2) {
//        if (o1.getCosineDistance() < o2.getCosineDistance()) {
//            return -1;
//        }
//        if (o1.getCosineDistance() > o2.getCosineDistance()) {
//            return 1;
//        }
//        return (o1.getString().getSpelling().compareTo(o2.getString().getSpelling()));
//    }
//
//    @Override
//    public int compareTo(W2vWordSuggestion o) {
//        if (this.getCosineDistance() < o.getCosineDistance()) {
//            return 1;
//        }
//        if (this.getCosineDistance() > o.getCosineDistance()) {
//            return -1;
//        }
//        return (this.getString().getSpelling().compareTo(o.getString().getSpelling()));
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
