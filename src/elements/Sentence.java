package elements;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

public class Sentence extends ArrayList<Word> {

    private CoreMap coreMap;
    private Map<Integer,Character> punctuationPositions = new TreeMap<Integer, Character>();//gives punctuation positions for the CoreMap

    public CoreMap getCoreMap() {
        return coreMap;
    }

    public void setCoreMap(CoreMap coreMap) {
        this.coreMap = coreMap;
        int index = 0;
        for (CoreLabel token : this.coreMap.get(CoreAnnotations.TokensAnnotation.class)) {
            String spelling = token.get(CoreAnnotations.TextAnnotation.class);
//            if (spelling.length() <= 2 && spelling.matches("[^\\w\\d\\s]"))
            if (spelling.length() <= 2 && !Character.isLetterOrDigit(spelling.charAt(0)) && !Character.isWhitespace(spelling.charAt(0)))
                punctuationPositions.put(index,spelling.charAt(0));
            index++;
        }
    }

    public int getWordIndexIncludingPunctuation(int wordIndex) {
        int wordcount = 0;
        int prePunctuationCount = 0;
        int coreMapSize = this.coreMap.get(CoreAnnotations.TokensAnnotation.class).size();
        for (int i = 0; i < coreMapSize; i++) {
            if (!punctuationPositions.entrySet().contains(i))
                wordcount++;
            else
                prePunctuationCount++;
            if (wordcount == wordIndex + 1)
                return prePunctuationCount + wordIndex;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Sentence words = (Sentence) o;

        if (getCoreMap() != null ? !getCoreMap().equals(words.getCoreMap()) : words.getCoreMap() != null) return false;
        return punctuationPositions != null ? punctuationPositions.equals(words.punctuationPositions) : words.punctuationPositions == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getCoreMap() != null ? getCoreMap().hashCode() : 0);
        result = 31 * result + (punctuationPositions != null ? punctuationPositions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            if (i != 0 && this.get(i).getClass() != Punctuation.class)
                sb.append(" ");
            sb.append(this.get(i).toString());
        }
        return sb.toString();
    }

    public String toStringOnlyWords() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getClass() != Punctuation.class) {
                if (i != 0)
                    sb.append(" ");
                sb.append(this.get(i).toString());
            }
        }
        return sb.toString();
    }
}






































































































