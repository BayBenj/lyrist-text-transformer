package rhyme;

import java.util.ArrayList;

public class WordSyllables extends ArrayList<Syllable> {

    @Override
    public String toString() {
        String sb = "";
        int i = 0;
        for (Syllable syllable : this) {
            if (i != 0)
                sb += " ";
            sb += syllable.toString();
            i++;
        }
        return sb;
    }
}
