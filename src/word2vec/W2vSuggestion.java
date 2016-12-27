//package word2vec;
//
//import java.util.Comparator;
//
//public class W2vSuggestion implements Comparator<W2vSuggestion>, Comparable<W2vSuggestion> {
//
//    private String string;
//    private Double cosineDistance;
//
//    public W2vSuggestion(String string, Double cosineDistance) {
//        this.setString(string);
//        this.setCosineDistance(cosineDistance);
//    }
//
//    public String getString() {
//        return string;
//    }
//
//    public void setString(String string) {
//        this.string = string;
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
//        W2vSuggestion that = (W2vSuggestion) o;
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
//    public int compare(W2vSuggestion o1, W2vSuggestion o2) {
//        if (o1.getCosineDistance() < o2.getCosineDistance()) {
//            return -1;
//        }
//        if (o1.getCosineDistance() > o2.getCosineDistance()) {
//            return 1;
//        }
//        return (o1.getString().compareTo(o2.getString()));
//    }
//
//    @Override
//    public int compareTo(W2vSuggestion o) {
//        if (this.getCosineDistance() < o.getCosineDistance()) {
//            return -1;
//        }
//        if (this.getCosineDistance() > o.getCosineDistance()) {
//            return 1;
//        }
//        return (this.getString().compareTo(o.getString()));
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
