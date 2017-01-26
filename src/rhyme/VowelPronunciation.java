package rhyme;

import java.util.ArrayList;

public class VowelPronunciation extends ArrayList<VowelPhoneme> {

    @Override
    public String toString() {
        String sb = "[";
        int i = 0;
        for (VowelPhoneme vowelPhoneme : this) {
            if (i != 0)
                sb+="-";
            sb += vowelPhoneme.toString();
            i++;
        }
        sb += "]";
        return sb;
    }
}
