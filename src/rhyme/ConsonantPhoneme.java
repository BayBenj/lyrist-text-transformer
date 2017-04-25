package rhyme;

import utils.U;

import java.io.Serializable;

public class ConsonantPhoneme extends Phoneme implements Serializable {

    public ConsonantPhoneme(PhonemeEnum phonemeEnum) {
        super(phonemeEnum);
        if (super.phonemeEnum.isVowel())
            super.phonemeEnum = null;
    }

    public MannerOfArticulation getManner() {
        return phonemeEnum.getManner();
    }

    public PlaceOfArticulation getPlace() {
        return phonemeEnum.getPlace();
    }

    public static ConsonantPhoneme rnd() {
        return new ConsonantPhoneme(PhonemeEnum.rndConsonant());
    }


}
