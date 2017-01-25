//package song;
//
//import java.util.Arrays;
//
//import elements.Phonetecizer;
//import elements.StressedPhoneme;
//
//public class WordPhonemeAlignment extends Alignment {
//
//    private String first;
//    private StressedPhoneme[] second;
//
//    public WordPhonemeAlignment(String string, StressedPhoneme[] stressedPhones, double[] scores) {
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
//        for (StressedPhoneme p : second) {
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