package rhyme;

import java.util.ArrayList;
import java.util.List;

public class SyllableGroup extends ArrayList<Syllable> {

    //Can represent the syllables of 1 or more filterWords

    public List<VowelPhoneme> getVowels() {
        List<VowelPhoneme> vowels = new ArrayList<>();
        for (Syllable syllable : this)
            vowels.add(syllable.getVowel());
        return vowels;
    }

    public List<ConsonantPhoneme> getConsonants() {
        List<ConsonantPhoneme> vowels = new ArrayList<>();
        for (Syllable syllable : this)
            vowels.addAll(syllable.getConsonants());
        return vowels;
    }

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







































































