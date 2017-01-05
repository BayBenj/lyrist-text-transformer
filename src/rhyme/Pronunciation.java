package rhyme;

import java.util.ArrayList;

public class Pronunciation extends ArrayList<StressedPhoneme> {

    @Override
    public String toString() {
        String sb = "[";
        int i = 0;
        for (StressedPhoneme stressedPhoneme : this) {
            if (i != 0)
                sb+="-";
            sb += stressedPhoneme.toString();
            i++;
        }
        sb += "]";
        return sb;
    }
}
