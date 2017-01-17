package rhyme;

import java.util.List;

public class FullRhyme extends SpecificRhyme {

    public List<StressedPhoneme> perfectRhyme;

    public FullRhyme(List<StressedPhoneme> perfectRhyme) {
        this.perfectRhyme = perfectRhyme;
    }

}
