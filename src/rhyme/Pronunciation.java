package rhyme;

import java.util.ArrayList;

public class Pronunciation extends ArrayList<Phoneme> {

    @Override
    public String toString() {
        String sb = "[";
        int i = 0;
        for (Phoneme phoneme : this) {
        if (i != 0)
        sb+="-";
        sb += phoneme.toString();
        i++;
        }
        sb += "]";
        return sb;
    }
}
