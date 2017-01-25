package filters;

import elements.Song;
import elements.Word;
import rhyme.LineRhymeScheme;
import rhyme.Rhyme;
import rhyme.RhymesAndTheirWords;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RhymeSchemeFilter extends Filter {

    private LineRhymeScheme lineRhymeScheme;

    public RhymeSchemeFilter(LineRhymeScheme rhymeScheme) {
        super(ReturnType.MATCHES);
        this.setLineRhymeScheme(rhymeScheme);
    }

    public RhymeSchemeFilter(ReturnType returnType, LineRhymeScheme rhymeScheme) {
        super(returnType);
        this.setLineRhymeScheme(rhymeScheme);
    }

    public Map<Rhyme,Set<Word>> doFilter(Song song) {
        if (song != null && !song.getAllWords().isEmpty()) {
            List<Word> words = song.getAllWords();
            RhymesAndTheirWords rhymesAndTheirWords = new RhymesAndTheirWords();
            for (int w = 0; w < words.size(); w++) {
                if (super.getReturnType() == ReturnType.MATCHES && lineRhymeScheme.contains(w)) {
                    Word word = words.get(w);
                    Rhyme rhyme = lineRhymeScheme.getRhymeByIndex(w);
                    rhymesAndTheirWords.putWord(rhyme, word);
                }
            }
            return rhymesAndTheirWords;
        }
        return null;
    }

    public LineRhymeScheme getLineRhymeScheme() {
        return lineRhymeScheme;
    }

    public void setLineRhymeScheme(LineRhymeScheme lineRhymeScheme) {
        this.lineRhymeScheme = lineRhymeScheme;
    }
}

























































































