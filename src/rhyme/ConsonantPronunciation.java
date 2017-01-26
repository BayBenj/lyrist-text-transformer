package rhyme;

import java.util.ArrayList;

public class ConsonantPronunciation extends ArrayList<ConsonantPhoneme> {

    @Override
    public String toString() {
        String sb = "[";
        int i = 0;
        for (ConsonantPhoneme consonantPhoneme : this) {
            if (i != 0)
                sb+="-";
            sb += consonantPhoneme.toString();
            i++;
        }
        sb += "]";
        return sb;
    }
}
