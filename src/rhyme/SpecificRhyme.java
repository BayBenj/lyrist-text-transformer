package rhyme;

import java.util.List;

public class SpecificRhyme extends Rhyme {

    public List<Phoneme> rhyme;

    public SpecificRhyme(List<Phoneme> rhyme) {
        this.rhyme = rhyme;
    }
}
