package rhyme;

import java.util.List;

public class LineRhymeScheme extends RhymeScheme {
    public LineRhymeScheme(String... rhymes) {
        super(rhymes);
    }

    public LineRhymeScheme(List<Rhyme> rhymes) {
        super(rhymes);
    }
}
