//package rhyme;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.SortedMap;
//
//import harmony.Chord;
//import pitch.Pitch;
//
//public class CompletedTab implements Serializable {
//
//    /**
//     *
//     */
//    private static final long serialVersionUID = 1L;
//    public int[] keys;
//    public List<String> words;
//    public List<SortedMap<Integer, Chord>> chords;
//    public int[] scheme;
//    public char[] structure;
//    public String tabURL;
//
//    public CompletedTab(int key, List<String> words, List<SortedMap<Integer, Chord>> chords, int[] scheme, char[] structure, String tabURL) {
//        this.words = words;
//        this.chords = chords;
//        this.scheme = scheme;
//        this.structure = structure;
//        this.tabURL = tabURL;
//        this.keys = new int[scheme.length];
//        for (int i = 0; i < keys.length; i++) {
//            keys[i] = key;
//        }
//    }
//
//    public int length() {
//        return scheme.length;
//    }
//
//    public char segmentLabelAt(int i) {
//        return structure[i];
//    }
//
//    public int rhymeSchemeAt(int i) {
//        return scheme[i];
//    }
//
//    public String toString() {
//        StringBuilder str = new StringBuilder();
//
//        str.append("URL: ");
//        str.append(tabURL);
//        str.append("\n");
//        str.append("Key signature: ");
//        int prevKey = -1;
//        int key;
//        for (int i = 0; i < keys.length; i++) {
//            key = keys[i];
//            if (key != prevKey) {
//                if (i != 0) str.append("/");
//                str.append(Pitch.getPitchName(key));
//            }
//            prevKey = key;
//        }
//
//        str.append("\n");
//
//        prevKey = -1;
//        for (int i = 0; i < scheme.length; i++) {
//            str.append("\n");
//            key = keys[i];
//            if (key != prevKey) {
//                str.append("" + i + "\t" + Pitch.getPitchName(key) + "\t\t" + chords.get(i)); // We label the key signature once on the line in which it first begins.
//            } else {
//                str.append("" + i + "\t\t\t" + chords.get(i));
//            }
//            prevKey = key;
//
//            str.append("\n");
//            str.append("" + i + "\t" + structure[i] + "\t" + scheme[i] + "\t" + words.get(i));
//        }
//
//        return str.toString();
//    }
//}
