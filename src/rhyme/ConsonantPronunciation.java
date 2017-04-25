package rhyme;

import utils.U;

import java.io.Serializable;
import java.util.ArrayList;

public class ConsonantPronunciation extends ArrayList<ConsonantPhoneme> implements Serializable {

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

    public static ConsonantPronunciation rnd(int len) {
        ConsonantPronunciation cp = new ConsonantPronunciation();
        for (int i = 0; i < len; i++) {
            cp.add(ConsonantPhoneme.rnd());
        }
        return cp;
    }

}
