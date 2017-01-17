package rhyme;

import java.util.ArrayList;
import java.util.List;

public class WordSyllables extends ArrayList<Syllable> {

    public List<StressedPhoneme> getWordRhymeFromStress() {
        //Find the most stressed syllable
        int highestStress = Integer.MIN_VALUE;
        int i = 0;
        int mostStressedSyllableIndex = -1;
        for (Syllable syllable : this) {
            if (syllable.getNucleus().stress > highestStress) {
                highestStress = syllable.getNucleus().stress;
                mostStressedSyllableIndex = i;
            }
            i++;
        }

        //Concatenate all phonemes from the most stressed syllables's rhyme onward
        boolean reachedHighestStress = false;
        i = 0;
        List<StressedPhoneme> wordRhyme = new ArrayList<>();
        for (Syllable syllable : this) {
            if (reachedHighestStress)
                wordRhyme.addAll(syllable.getStressedPhonemes());

            if (i == mostStressedSyllableIndex) {
                reachedHighestStress = true;
                wordRhyme.addAll(syllable.getStressedRhyme());
            }
            i++;
        }
        return wordRhyme;
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















































































