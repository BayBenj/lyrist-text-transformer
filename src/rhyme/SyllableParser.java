package rhyme;

import java.util.ArrayList;
import java.util.List;

public abstract class SyllableParser {

    public static List<Syllable> parse(Pronunciation stressedPhones) {
        int nSyllables = 0;
        for (StressedPhone stressedPhone : stressedPhones)
            if (stressedPhone.phone.isVowel())
                nSyllables++;

        List<Syllable> syllables = initialParse(stressedPhones, nSyllables);

        //Check correctness of syllables w/ phonotactic rules
        boolean allGood = checkSyllablesOfEnitreWord(syllables);

        //Fix syllables
        ready:
        while (!allGood) {
            for (int i = 0; i < syllables.size(); i++) {
                Syllable current = syllables.get(i);
                Syllable next;
                if (i < syllables.size() - 1)
                    next = syllables.get(i + 1);
                else
                    next = null;

                if (next != null && next.hasOnset() && current != null && current.hasCoda()) {
                    //Coda protection rule
                    if (    current.getCoda().size() == 1 &&
                            (next.getOnset().get(0) == Phoneme.NG ||
                                    next.getOnset().get(0) == Phoneme.ZH ||
                                    next.getOnset().get(0) == Phoneme.DH) )
                        break ready;

                    //Coda protection rule
                    if (!current.getCoda().get(0).isVoiced() &&
                            next.getOnset().get(0).isVoiced()) {
                        break ready;
                    }

                    //Coda protection rule
                    if ((current.getCoda().get(current.getCoda().size() - 1) == Phoneme.M ||
                            current.getCoda().get(current.getCoda().size() - 1) == Phoneme.NG) &&
                            Phoneme.getPlace(next.getOnset().get(0)) != Phoneme.getPlace(current.getCoda().get(current.getCoda().size() - 1))) {
                        break ready;
                    }
                }

                if (next != null && next.hasOnset() && current != null) {
                    //Coda protection rule
                    if (next.getOnset().get(0) == Phoneme.HH)
                        break ready;
                    Phoneme switched = next.getOnset().get(0);
                    next.getOnset().remove(switched);
                    current.getCoda().add(switched);
                }

                if (checkSyllablesOfEnitreWord(syllables))
                    break ready;
            }

            //Recheck correctness of syllables w/ phonotactic rules
            allGood = checkSyllablesOfEnitreWord(syllables);
        }

        return syllables;
    }

    private static List<Syllable> initialParse(Pronunciation stressedPhonemes, int nSyllables) {
        List<Syllable> syllables = new ArrayList<>();
        Syllable currentSyllable = new Syllable();

        //Inital syllable parse
        for (int i = 0; i < stressedPhonemes.size(); i++) {
            Phoneme phoneme = stressedPhonemes.get(i).phone;
            //if phoneme is a vowel
            if (phoneme.isVowel()) {
                currentSyllable.setStress(stressedPhonemes.get(i).stress);
                currentSyllable.setNucleus(phoneme);
                if (syllables.size() < nSyllables - 1) {
                    syllables.add(currentSyllable);
                    currentSyllable = new Syllable();
                }
            }
            //if phoneme is a consonant
            else {
                if (!currentSyllable.hasNucleus()) {
                    currentSyllable.getOnset().add(phoneme);
                }
                else {
                    currentSyllable.getCoda().add(phoneme);
                }
                if (i == stressedPhonemes.size() - 1) {
                    syllables.add(currentSyllable);
                    break;
                }
            }
        }
        return syllables;
    }

    private static boolean checkSyllablesOfEnitreWord(List<Syllable> syllables) {
        boolean allGood = true;
        for (Syllable syllable : syllables) {
            allGood = allGood && checkWithPhonotacticConstraints(syllable);
            if (!allGood)
                return false;
        }
        return allGood;
    }

    private static boolean checkWithPhonotacticConstraints(Syllable syllable) {
        List<Phoneme> onset = syllable.getOnset();
        Phoneme nucleus = syllable.getNucleus();
        List<Phoneme> coda = syllable.getCoda();

        //phonotactic constraints
        boolean ready = true;

        //All syllables have one nucleus that is a vowel
        if (nucleus == null || !nucleus.isVowel())
            return false;

        //The onset contains only consonants
        if (onset != null && onset.size() > 0)
            for (Phoneme phoneme : onset)
                if (phoneme.isVowel())
                    return false;

        //The coda contains only consonants
        if (coda != null && coda.size() > 0)
            for (Phoneme phoneme : coda)
                if (phoneme.isVowel())
                    return false;

        //No adjacent, duplicate phonemes in the onset.
        if (onset != null && onset.size() > 1)
            for (int i = 0; i < onset.size(); i++) {
                if (    (i > 0 && onset.get(i) == onset.get(i - 1)) ||
                        (i < onset.size() - 1 && onset.get(i) == onset.get(i + 1)))
                    return false;
            }

        //No adjacent, duplicate phonemes in the coda.
        if (coda != null && coda.size() > 1)
            for (int i = 0; i < coda.size(); i++) {
                if (    (i > 0 && coda.get(i) == coda.get(i - 1)) ||
                        (i < coda.size() - 1 && coda.get(i) == coda.get(i + 1)))
                    return false;
            }

        //No NG in onset
        if (onset != null && onset.contains(Phoneme.NG))
            return false;

        //No HH in coda
        if (coda != null && coda.contains(Phoneme.HH))
            return false;

        //No affricates in complex onsets
        if (onset != null && onset.size() > 1)
            for (Phoneme phoneme : onset)
                if (Phoneme.getManner(phoneme) == MannerOfArticulation.AFFRICATE)
                    return false;

        //The first consonant in a complex onset must be an obstruent
        if (onset != null && onset.size() > 1)
            if (Phoneme.getManner(onset.get(0)) != MannerOfArticulation.STOP &&
                    Phoneme.getManner(onset.get(0)) != MannerOfArticulation.FRICATIVE &&
                    Phoneme.getManner(onset.get(0)) != MannerOfArticulation.AFFRICATE)
                return false;

        //The second consonant in a complex onset must not be a voiced obstruent
        if (onset != null && onset.size() > 1)
            if (    Phoneme.getManner(onset.get(1)) == MannerOfArticulation.STOP ||
                    Phoneme.getManner(onset.get(1)) == MannerOfArticulation.FRICATIVE ||
                    Phoneme.getManner(onset.get(1)) == MannerOfArticulation.AFFRICATE &&
                            onset.get(1).isVoiced())
                return false;

        //If the first consonant in a complex onset is not an /s/, the second must be a liquid or a semivowel
        if (onset != null && onset.size() > 1)
            if (    onset.get(0) != Phoneme.S &&
                    Phoneme.getManner(onset.get(1)) != MannerOfArticulation.LIQUID &&
                    Phoneme.getManner(onset.get(1)) != MannerOfArticulation.SEMIVOWEL)
                return false;

        //No semivowels in coda
        if (coda != null && coda.size() > 1)
            for (Phoneme phoneme : coda)
                if (Phoneme.getManner(phoneme) == MannerOfArticulation.SEMIVOWEL)
                    return false;

        //If there is a complex coda, the second consonant must not be /ŋ/, /ʒ/, or /ð/
        if (coda != null && coda.size() > 1)
            if (coda.get(1) == Phoneme.NG || coda.get(1) == Phoneme.ZH || coda.get(1) == Phoneme.DH)
                return false;

        //If the second consonant in a complex coda is voiced, so is the first
        if (coda != null && coda.size() > 1)
            if (coda.get(1).isVoiced() && !coda.get(0).isVoiced())
                return false;

        //Two obstruents in the same coda must share voicing
        if (coda != null && coda.size() > 1) {
            Phoneme obstruent1 = null;
            Phoneme obstruent2 = null;
            for (Phoneme phoneme : coda) {
                if (Phoneme.getManner(phoneme) == MannerOfArticulation.STOP ||
                        Phoneme.getManner(phoneme) == MannerOfArticulation.FRICATIVE ||
                        Phoneme.getManner(phoneme) == MannerOfArticulation.AFFRICATE) {
                    if (obstruent1 == null) {
                        obstruent1 = phoneme;
                    }
                    else {
                        obstruent2 = phoneme;
                        break;
                    }
                }
            }
            if (obstruent1 != null && obstruent2 != null && obstruent1.isVoiced() != obstruent2.isVoiced()) {
                return false;
            }
        }

        //Non-alveolar nasals (M, NG) must be homorganic with the next phoneme
        if (onset != null && onset.size() > 1) {
            for (int i = 0; i < onset.size(); i++) {
                Phoneme phoneme = onset.get(i);
                if ((phoneme == Phoneme.M || phoneme == Phoneme.NG) && i < onset.size() - 1) {
                    if (Phoneme.getPlace(phoneme) != Phoneme.getPlace(onset.get(i + 1))) {
                        return false;
                    }
                }
            }
        }

        if (coda != null && coda.size() > 1) {
            for (int i = 0; i < coda.size(); i++) {
                Phoneme phoneme = coda.get(i);
                if ((phoneme == Phoneme.M || phoneme == Phoneme.NG) && i < coda.size() - 1) {
                    if (Phoneme.getPlace(phoneme) != Phoneme.getPlace(coda.get(i + 1))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}







/*
14 phonotactic constaints of English:
    @All syllables have a nucleus
    @No geminates (2 duplicate phonemes in a row. None of these in the onset or coda )
    @No onset /ŋ/ (NG)
    @No /h/ (HH) in the syllable coda
    @No affricates in complex onsets (more than 1 phoneme)
    @The first consonant in a complex onset must be a stop
    @The second consonant in a complex onset must not be a voiced stop
    @If the first consonant in a complex onset is not an /s/, the second must be a liquid or a semivowel
    #Every subsequence contained within a sequence of consonants must obey all the relevant phonotactic rules (the substring principle)[5]
    @No semivowels (semivowels) in codas
    @If there is a complex coda, the second consonant must not be /ŋ/, /ʒ/, or /ð/
    @If the second consonant in a complex coda is voiced, so is the first
    @Non-alveolar nasals (M, NG) must be homorganic with the next phoneme
        M - P, B
        NG - G, K
    @Two stops in the same coda must share voicing
 */


//sonnarent = non vowel that you can hold out: nasals, liquids

































































