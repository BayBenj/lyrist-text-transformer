//package filters;
//
//import elements.Song;
//import elements.Word;
//import rhyme.LineRhymeScheme;
//import rhyme.Rhyme;
//import rhyme.WordsByRhyme;
//
//import java.util.List;
//
//public class RhymeSchemeFilter extends Filter {
//
//    private LineRhymeScheme lineRhymeScheme;
//
//    public RhymeSchemeFilter(LineRhymeScheme rhymeScheme) {
//        super(ReturnType.MATCHES);
//        this.setLineRhymeScheme(rhymeScheme);
//    }
//
//    public RhymeSchemeFilter(ReturnType returnType, LineRhymeScheme rhymeScheme) {
//        super(returnType);
//        this.setLineRhymeScheme(rhymeScheme);
//    }
//
//    public WordsByRhyme doFilter(Song song) {
//        if (song != null && !song.getAllWords().isEmpty()) {
//            List<Word> words = song.getAllWords();
//            WordsByRhyme wordsByRhyme = new WordsByRhyme();
//            for (int w = 0; w < words.size(); w++) {
//                if (super.getReturnType() == ReturnType.MATCHES && lineRhymeScheme.contains(w)) {
//                    Word word = words.get(w);
//                    Rhyme rhyme = lineRhymeScheme.getRhymeByIndex(w);
//                    wordsByRhyme.putWord(rhyme, word);
//                }
//            }
//            return wordsByRhyme;
//        }
//        return null;
//    }
//
//    public LineRhymeScheme getLineRhymeScheme() {
//        return lineRhymeScheme;
//    }
//
//    public void setLineRhymeScheme(LineRhymeScheme lineRhymeScheme) {
//        this.lineRhymeScheme = lineRhymeScheme;
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
