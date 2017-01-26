//package song;
//
//import java.util.Arrays;
//
//import elements.Phonetecizer;
//import elements.VowelPhoneme;
//
//public class WordPhonemeAlignment extends Alignment {
//
//    private String first;
//    private VowelPhoneme[] second;
//
//    public WordPhonemeAlignment(String string, VowelPhoneme[] stressedPhones, double[] scores) {
//        super(scores);
//        this.first = string;
//        this.second = stressedPhones;
//    }
//
//    @Override
//    public Object getFirst() {
//        return first;
//    }
//
//    @Override
//    public Object getSecond() {
//        return second;
//    }
//
//    public String toString() {
//        StringBuilder str = new StringBuilder();
//        for (char c : first.toCharArray()) {
//            str.append(c);
//            str.append("\t");
//        }
//        str.append("\n");
//        for (VowelPhoneme p : second) {
//            str.append(p == null? "null":Phonetecizer.intToString(p.phoneme));
//            str.append("\t");
//        }
//        str.append("\n");
//        for (double s : scores) {
//            str.append(s);
//            str.append("\t");
//        }
//        return str.toString();
//    }
//
//}