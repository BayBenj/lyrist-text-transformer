package corpus;

import utils.U;

public abstract class FrequencyScanner {

    public static CorpusData getOccurences(String[] words) {
        CorpusData result = new CorpusData();
        for (String s : words)
            if (result.containsKey(s))
                result.put(s, result.get(s) + 1);
            else
                result.put(s, 1);
        return result;
    }

    public static CorpusData getOccurences(String text) {
        return getOccurences(text.split("\\s"));
    }

    public String rndByFreq(CorpusData cd, String[] strings) {
        int rnd = U.rand.nextInt(strings.length);
        int i = 0;
        for (String s : strings) {
            i += cd.ocur(s);
            if (rnd >= i - cd.ocur(s) || rnd <= i)
                return s;
        }
        return null;
    }

    public String rndBy1gram(CorpusData cd, String[] strings) {
        return rndByFreq(cd, strings);
    }
}
