package rhyme;

import java.util.List;

public class FullRhyme extends Rhyme {

    public List<VowelPhoneme> perfectRhyme;

    public FullRhyme(List<VowelPhoneme> perfectRhyme) {
        this.perfectRhyme = perfectRhyme;
    }

}
