package rhyme;

import utils.Pair;

import java.util.*;

public abstract class SyllableParser {

//    public static WordSyllables parse(Pronunciation stressedPhones) {
//        int nSyllables = 0;
//        for (StressedPhoneme stressedPhoneme : stressedPhones)
//            if (stressedPhoneme.phoneme.isVowel())
//                nSyllables++;
//
//        WordSyllables syllables = initialParse(stressedPhones, nSyllables);
//
//        //Check correctness of syllables w/ phonotactic rules
//        boolean allGood = checkSyllablesOfEntireWord(syllables);
//
//        //Fix syllables
//        ready:
//        while (!allGood) {
//            for (int i = 0; i < syllables.size(); i++) {
//                Syllable current = syllables.get(i);
//                Syllable next;
//                if (i < syllables.size() - 1)
//                    next = syllables.get(i + 1);
//                else
//                    next = null;
//
//                if (next != null && next.hasOnset() && current != null && current.hasCoda()) {
//                    //Coda protection rule
//                    if (    current.getCoda().size() == 1 &&
//                            (next.getOnset().get(0).phoneme == Phoneme.NG ||
//                                    next.getOnset().get(0).phoneme == Phoneme.ZH ||
//                                    next.getOnset().get(0).phoneme == Phoneme.DH) )
//                        break ready;
//
//                    //Coda protection rule
//                    if (!current.getCoda().get(0).phoneme.isVoiced() &&
//                            next.getOnset().get(0).phoneme.isVoiced()) {
//                        break ready;
//                    }
//
//                    //Coda protection rule
//                    if ((current.getCoda().get(current.getCoda().size() - 1).phoneme == Phoneme.M ||
//                            current.getCoda().get(current.getCoda().size() - 1).phoneme == Phoneme.NG) &&
//                            Phoneme.getPlace(next.getOnset().get(0).phoneme) != Phoneme.getPlace(current.getCoda().get(current.getCoda().size() - 1).phoneme)) {
//                        break ready;
//                    }
//                }
//
//                if (next != null && next.hasOnset() && current != null) {
//                    //Coda protection rule
//                    if (next.getOnset().get(0).phoneme == Phoneme.HH)
//                        break ready;
//                    Phoneme switched = next.getOnset().get(0).phoneme;
//                    next.getOnset().remove(switched);
//                    current.getCoda().add(switched);
//                }
//
//                if (checkSyllablesOfEntireWord(syllables))
//                    break ready;
//            }
//
//            //Recheck correctness of syllables w/ phonotactic rules
//            allGood = checkSyllablesOfEntireWord(syllables);
//        }
//
//        return syllables;
//    }

    private static WordSyllables initialParse(Pronunciation stressedPhonemes, int nSyllables) {
        WordSyllables syllables = new WordSyllables();
        Syllable currentSyllable = new Syllable();

        //Inital syllable parse
        for (int i = 0; i < stressedPhonemes.size(); i++) {
            StressedPhoneme stressedPhoneme = stressedPhonemes.get(i);
            //if phoneme is a vowel
            if (stressedPhoneme.isVowel()) {
                currentSyllable.setNucleus(stressedPhoneme);
                if (syllables.size() < nSyllables - 1) {
                    syllables.add(currentSyllable);
                    currentSyllable = new Syllable();
                }
            }
            //if phoneme is a consonant
            else {
                if (!currentSyllable.hasNucleus()) {
                    currentSyllable.getOnset().add(stressedPhoneme);
                }
                else {
                    currentSyllable.getCoda().add(stressedPhoneme);
                }
                if (i == stressedPhonemes.size() - 1) {
                    syllables.add(currentSyllable);
                    break;
                }
            }
        }
        return syllables;
    }

    public static WordSyllables algorithmicallyParse(Pronunciation stressedPhones) {
        int nSyllables = getNSyllables(stressedPhones);
        Map<Integer,SyllablePart> map = nucleusParse(stressedPhones, nSyllables);
        onsetParse(map, stressedPhones);
        codaParse(map, stressedPhones);
        return formSyllables(map, stressedPhones);
    }

    private static int getNSyllables(Pronunciation stressedPhonemes) {
        int nSyllables = 0;
        for (StressedPhoneme stressedPhoneme : stressedPhonemes)
            if (stressedPhoneme.phoneme.isVowel())
                nSyllables++;
        return nSyllables;
    }

    /**
     * Labels the nuclei, first onset, and last coda of the word.
     */
    private static Map<Integer,SyllablePart> nucleusParse(Pronunciation stressedPhonemes, int nSyllables) {
        Map<Integer,SyllablePart> wordMap = new HashMap<>();
        int nucleusIndex = 0;
        for (int i = 0; i < stressedPhonemes.size(); i++) {
            //if phoneme is a vowel
            if (stressedPhonemes.get(i).phoneme.isVowel()) {
                wordMap.put(i,SyllablePart.NUCLEUS);
                nucleusIndex++;
            }
            //if phoneme is a consonant and comes before the first nucleus
            else if (nucleusIndex == 0) {
                wordMap.put(i,SyllablePart.ONSET);
            }
            //if phoneme is a consonant and comes after the last nucleus
            else if (nucleusIndex == nSyllables) {
                wordMap.put(i,SyllablePart.CODA);
            }
        }
        return wordMap;
    }

    /**
     * Builds the longest possible onset for each syllable, according to 13 phonotactic rules of English.
     */
    private static Map<Integer,SyllablePart> onsetParse(Map<Integer,SyllablePart> wordMap,
                                                        Pronunciation stressedPhonemes) {
        //Build nuclei map
        Map<Integer,SyllablePart> nuclei = new HashMap<>();
        for (Map.Entry<Integer,SyllablePart> entry : wordMap.entrySet()) {
            if (entry.getValue() == SyllablePart.NUCLEUS) {
                nuclei.put(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<Integer,SyllablePart> entry : nuclei.entrySet()) {
            int i = entry.getKey() - 1;
            if (i >= 0) {
                List<Phoneme> candidateOnset = new ArrayList<>();
                candidateOnset.add(stressedPhonemes.get(i).phoneme);
                while (!wordMap.keySet().contains(i) && followsOnsetRules(candidateOnset)) {
                    wordMap.putIfAbsent(i, SyllablePart.ONSET);
                    i--;
                    if (i >= 0 && i < stressedPhonemes.size())
                        candidateOnset.add(0, stressedPhonemes.get(i).phoneme);
                    else
                        break;
                }
            }
        }
        return wordMap;
    }

    /**
     * Labels all empty indexes as coda. //TODO this may need to be upgraded
     */
    private static Map<Integer,SyllablePart> codaParse(Map<Integer,SyllablePart> wordMap,
                                                        Pronunciation stressedPhonemes) {
        //Build nuclei map
        Map<Integer,SyllablePart> nuclei = new HashMap<>();
        for (Map.Entry<Integer,SyllablePart> entry : wordMap.entrySet()) {
            if (entry.getValue() == SyllablePart.NUCLEUS) {
                nuclei.put(entry.getKey(), entry.getValue());
            }
        }

        //Label all empty indexes as coda
        for (int i = 0; i < stressedPhonemes.size(); i++) {
            if (!wordMap.containsKey(i)) {
                wordMap.putIfAbsent(i, SyllablePart.CODA);
            }
        }

        //Build a nucleusNum->(coda,onset) map
        TreeMap<Integer,SyllablePart> treeMap = new TreeMap<>(wordMap);
        Map<Integer, Pair<List<Phoneme>,List<Phoneme>>> codas_onsets = new HashMap<>();
        List<Phoneme> currentCoda = new ArrayList<>();
        List<Phoneme> currentOnset = new ArrayList<>();
        int nucleusNum = -1;
        for (Map.Entry<Integer,SyllablePart> entry : treeMap.entrySet()) {
            if (entry.getValue() == SyllablePart.ONSET) {
                if (nucleusNum > -1) {
                    currentOnset.add(stressedPhonemes.get(entry.getKey()).phoneme);
                }
            }
            else if (entry.getValue() == SyllablePart.NUCLEUS) {
                nucleusNum++;
                if (nucleusNum != 0) {
                    codas_onsets.put(entry.getKey(),new Pair<>(currentCoda, currentOnset));
                    currentCoda = new ArrayList<>();
                    currentOnset = new ArrayList<>();
                }
            }
            else if (entry.getValue() == SyllablePart.CODA) {
                if (nucleusNum < nuclei.size() - 1) {
                    currentCoda.add(stressedPhonemes.get(entry.getKey()).phoneme);
                }
            }
        }

        //For each (coda,onset) pair, ensure coda passes rules. If not, take phonemes from onset until both coda and onset pass rules, or until there is no possible match.
        for (Map.Entry<Integer, Pair<List<Phoneme>,List<Phoneme>>> entry : codas_onsets.entrySet()) {
            if (!followsCodaRules(entry.getValue().getFirst())) {
                List<Phoneme> candidateCoda = new ArrayList<>(entry.getValue().getFirst());
                List<Phoneme> candidateOnset = new ArrayList<>(entry.getValue().getSecond());
                while (!candidateOnset.isEmpty()) {
                    //adding to coda, taking from onset
                    Phoneme switched = candidateOnset.remove(0);
                    candidateCoda.add(switched);
                    if (followsCodaRules(candidateCoda) && followsOnsetRules(candidateOnset)) {
                        codas_onsets.put(entry.getKey(),new Pair<>(candidateCoda,candidateOnset));

                        //update wordMap
                        for (Phoneme p : entry.getValue().getFirst()) {
                            for (Map.Entry<Integer,SyllablePart> entry2 : nuclei.entrySet()) {
                                if (entry2.getKey() == entry.getKey()) {
                                    int index = 0;
                                    for (Phoneme codaPhoneme : entry.getValue().getFirst()) {
                                        wordMap.put(index + entry.getKey(), SyllablePart.CODA);
                                        index++;
                                    }
                                    for (Phoneme onsetPhoneme : entry.getValue().getSecond()) {
                                        wordMap.put(index + entry.getKey(), SyllablePart.ONSET);
                                        index++;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return wordMap;
    }

    private static boolean followsOnsetRules(List<Phoneme> candidateOnset) {
        if (candidateOnset != null && candidateOnset.size() > 0) {
            //non-complex
            boolean passing = onsetRule1(candidateOnset);
            if (!passing)
                return false;

            //non-complex
            passing = onsetRule2(candidateOnset);
            if (!passing)
                return false;

            //non-complex?
            passing = onsetRule3(candidateOnset);
            if (!passing)
                return false;

            //Rules for complex onsets
            if (candidateOnset.size() > 1) {
                passing = onsetRule4(candidateOnset);
                if (!passing)
                    return false;

                passing = onsetRule5(candidateOnset);
                if (!passing)
                    return false;

                passing = onsetRule6(candidateOnset);
                if (!passing)
                    return false;

                passing = onsetRule7(candidateOnset);
                if (!passing)
                    return false;

                passing = onsetRule8(candidateOnset);
                if (!passing)
                    return false;

                passing = onsetRule9(candidateOnset);
                if (!passing)
                    return false;
            }
        }
        return true;
    }

    private static boolean onsetRule1(List<Phoneme> onset) {
        //The onset contains only consonants
        for (Phoneme phoneme : onset)
            if (phoneme.isVowel())
                return false;
        return true;
    }

    private static boolean onsetRule2(List<Phoneme> onset) {
        //No NG in onset
        if (onset.contains(Phoneme.NG))
            return false;
        return true;
    }

    private static boolean onsetRule3(List<Phoneme> onset) {
        //Non-alveolar nasals in onset (M, NG) must be homorganic with the next phoneme TODO next phoneme in onset or in the word???
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
        return true;
    }

    private static boolean onsetRule4(List<Phoneme> onset) {
        //No adjacent, duplicate phonemes in the onset.
        for (int i = 0; i < onset.size(); i++) {
            if (    (i > 0 && onset.get(i) == onset.get(i - 1)) ||
                    (i < onset.size() - 1 && onset.get(i) == onset.get(i + 1)))
                return false;
        }
        return true;
    }

    private static boolean onsetRule5(List<Phoneme> onset) {
        //No affricates in complex onsets
        for (Phoneme phoneme : onset)
            if (Phoneme.getManner(phoneme) == MannerOfArticulation.AFFRICATE)
                return false;
        return true;
    }

    private static boolean onsetRule6(List<Phoneme> onset) {
        //The first consonant in a complex onset must be an obstruent
        if (onset.get(0) == Phoneme.HH) {   //TODO this is an exception I made for "ex-hume"
            return true;
        }
        if (!onset.get(0).isObstruent())
            return false;
        return true;
    }

    private static boolean onsetRule7(List<Phoneme> onset) {
        //The second consonant in a complex onset must not be a voiced obstruent
        if (onset.get(1).isObstruent() && onset.get(1).isVoiced())
            return false;
        return true;
    }

    private static boolean onsetRule8(List<Phoneme> onset) {
        //If the first consonant in a complex onset is not an /s/, the second must be a liquid or a semivowel
        if (    onset.get(0) != Phoneme.S &&
                Phoneme.getManner(onset.get(1)) != MannerOfArticulation.LIQUID &&
                Phoneme.getManner(onset.get(1)) != MannerOfArticulation.SEMIVOWEL)
            return false;
        return true;
    }

    private static boolean onsetRule9(List<Phoneme> onset) {
        //No nasal in first position of a complex onset
        if (Phoneme.getManner(onset.get(0)) == MannerOfArticulation.NASAL)
            return false;
        return true;
    }

    private static boolean followsCodaRules(List<Phoneme> candidateCoda) {
        if (candidateCoda != null && candidateCoda.size() > 0) {
            //non-complex
            boolean passing = codaRule1(candidateCoda);
            if (!passing)
                return false;

            //non-complex
            passing = codaRule2(candidateCoda);
            if (!passing)
                return false;

            //non-complex
            passing = codaRule3(candidateCoda);
            if (!passing)
                return false;

            //Rules for complex coda
            if (candidateCoda.size() > 1) {
                passing = codaRule4(candidateCoda);
                if (!passing)
                    return false;

                passing = codaRule5(candidateCoda);
                if (!passing)
                    return false;

                passing = codaRule6(candidateCoda);
                if (!passing)
                    return false;

                passing = codaRule7(candidateCoda);
                if (!passing)
                    return false;

                passing = codaRule8(candidateCoda);
                if (!passing)
                    return false;
            }
        }
        return true;
    }

    private static boolean codaRule1(List<Phoneme> coda) {
        //The coda contains only consonants
        for (Phoneme phoneme : coda)
            if (phoneme.isVowel())
                return false;
        return true;
    }

    private static boolean codaRule2(List<Phoneme> coda) {
        //No HH in coda
        if (coda.contains(Phoneme.HH))
            return false;
        return true;
    }

    private static boolean codaRule3(List<Phoneme> coda) {
        //No semivowels in coda
        for (Phoneme phoneme : coda)
            if (Phoneme.getManner(phoneme) == MannerOfArticulation.SEMIVOWEL)
                return false;
        return true;
    }

    private static boolean codaRule4(List<Phoneme> coda) {
        //No adjacent, duplicate phonemes in the coda.
        for (int i = 0; i < coda.size(); i++)
            if (    (i > 0 && coda.get(i) == coda.get(i - 1)) ||
                    (i < coda.size() - 1 && coda.get(i) == coda.get(i + 1)))
                return false;
        return true;
    }

    private static boolean codaRule5(List<Phoneme> coda) {
        //If there is a complex coda, the second consonant must not be /ŋ/, /ʒ/, or /ð/
        if (coda.get(1) == Phoneme.NG || coda.get(1) == Phoneme.ZH || coda.get(1) == Phoneme.DH)
            return false;
        return true;
    }

    private static boolean codaRule6(List<Phoneme> coda) {
        //If the second consonant in a complex coda is voiced, so is the first
        if (coda.get(1).isVoiced() && !coda.get(0).isVoiced())
            return false;
        return true;
    }

    private static boolean codaRule7(List<Phoneme> coda) {
        //Two obstruents in the same coda must share voicing
        Phoneme obstruent1 = null;
        Phoneme obstruent2 = null;
        for (Phoneme phoneme : coda) {
            if (phoneme.isObstruent()) {
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
        return true;
    }

    private static boolean codaRule8(List<Phoneme> coda) {
        //Non-alveolar nasals (M, NG) must be homorganic with the next phoneme
        for (int i = 0; i < coda.size(); i++) {
            Phoneme phoneme = coda.get(i);
            if ((phoneme == Phoneme.M || phoneme == Phoneme.NG) && i < coda.size() - 1) {
                if (Phoneme.getPlace(phoneme) != Phoneme.getPlace(coda.get(i + 1))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static WordSyllables formSyllables(Map<Integer,SyllablePart> wordMap, Pronunciation stressedPhonemes) {
        WordSyllables result = new WordSyllables();
        Syllable currentSyllable = new Syllable();
        SyllablePart lastPart = null;
        int i = 0;
        for (Map.Entry<Integer,SyllablePart> entry : wordMap.entrySet()) {
            if (entry.getValue() == SyllablePart.ONSET) {
                if (lastPart == SyllablePart.CODA || lastPart == SyllablePart.NUCLEUS){
                    result.add(currentSyllable);
                    currentSyllable = new Syllable();
                }
                currentSyllable.getOnset().add(stressedPhonemes.get(i));
                lastPart = SyllablePart.ONSET;
            }
            else if (entry.getValue() == SyllablePart.NUCLEUS) {
                if (lastPart == SyllablePart.CODA || lastPart == SyllablePart.NUCLEUS){
                    result.add(currentSyllable);
                    currentSyllable = new Syllable();
                }
                currentSyllable.setNucleus(stressedPhonemes.get(i));
                lastPart = SyllablePart.NUCLEUS;
            }
            else if (entry.getValue() == SyllablePart.CODA) {
                currentSyllable.getCoda().add(stressedPhonemes.get(i));
                lastPart = SyllablePart.CODA;
            }
            i++;
        }
        if (lastPart == SyllablePart.CODA || lastPart == SyllablePart.NUCLEUS){
            result.add(currentSyllable);
        }
        return result;
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







































































