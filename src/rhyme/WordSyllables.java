package rhyme;

import java.io.Serializable;

public class WordSyllables extends SyllableGroup implements Serializable {

    //Pertains specifically to syllables in 1 word

    public SyllableGroup getWordRhymeFromStress() {
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
        SyllableGroup wordRhyme = new SyllableGroup();
        for (Syllable syllable : this) {
            if (reachedHighestStress)
                wordRhyme.add(syllable);

            if (i == mostStressedSyllableIndex) {
                reachedHighestStress = true;
                wordRhyme.add(new Syllable(new ConsonantPronunciation(), syllable.getNucleus(), syllable.getCoda()));
            }
            i++;
        }
        return wordRhyme;
    }

    public Pronunciation getPhonemes() {
        return this.getPronunciation();
    }

    public Pronunciation getPronunciation() {
        Pronunciation result = new Pronunciation();
        for (Syllable s : this)
            result.addAll(s.getPronunciation());
        return result;
    }

}







































































