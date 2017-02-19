package rhyme;

import utils.U;

import java.util.ArrayList;
import java.util.List;

public abstract class RhymeSchemeManager {

    private static String[] alphabet = new String[] {
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "Y",
            //X is excluded because it means a unique rhyme that nothing else can rhyme with
            "Z"
    };

    public static LineRhymeScheme getRndAlternatingScheme(int nLines) {
        int nRhymeClasses = (int)(Math.pow(U.rand.nextDouble(), 2) * nLines);
        int nLinesPerRhyme = (int)(Math.pow(U.rand.nextDouble(), 2) * 3) + 1;
        return getAlternatingScheme(nLines, nRhymeClasses, nLinesPerRhyme);
    }

    public static LineRhymeScheme getAlternatingScheme(int nLines, int nRhymeClasses, int nLinesPerRhyme) {
        if (nRhymeClasses < 1)
            return getRhymelessScheme(nLines);

        else if (nRhymeClasses == 1)
            return getMonorhyme(nLines);

        else if (nRhymeClasses > nLines)
            nRhymeClasses = nLines;

        while (nLinesPerRhyme * (nRhymeClasses - 1) + 1 > nLines)
            nLinesPerRhyme--;

        String[] uniqueRhymes = new String[nRhymeClasses];
        for (int rhyme = 0; rhyme < nRhymeClasses; rhyme++) {
            uniqueRhymes[rhyme] = alphabet[rhyme];
        }

        String[] lineRhymes = new String[nLines];
        int nSameRhymesInARow = 0;
        int currentRhyme = 0;
        for (int line = 0; line < nLines; line++) {
            if (nSameRhymesInARow > nLinesPerRhyme) {
                currentRhyme++;
                if (currentRhyme >= nRhymeClasses)
                    currentRhyme = 0;
            }
            lineRhymes[line] = uniqueRhymes[currentRhyme];
            nSameRhymesInARow++;
        }
        return new LineRhymeScheme(lineRhymes);
    }

    public static LineRhymeScheme getRhymelessScheme(int nLines) {
        String[] lineRhymes = new String[nLines];
        for (int line = 0; line < nLines; line++)
            lineRhymes[line] = "X";
        return new LineRhymeScheme(lineRhymes);
    }

    public static LineRhymeScheme getMonorhyme(int nLines) {
        return getAlternatingScheme(nLines, 1, nLines);
    }

//    public static LineRhymeScheme merge(LineRhymeScheme... rhymeSchemes) {
//        List<Rhyme> lineRhymes = new ArrayList<>();
//        for (LineRhymeScheme lineRhymeScheme : rhymeSchemes)
//            lineRhymes.addAll(lineRhymeScheme);
//        return new LineRhymeScheme(lineRhymes);
//    }
//
//    public static LineRhymeScheme getReflection(LineRhymeScheme rhymeScheme) {
//        List<Rhyme> lineRhymes = new ArrayList<>();
//        for (int i = rhymeScheme.size() - 1; i > 0; i--) {
//            lineRhymes.add(rhymeScheme.get(i));
//        }
//        return new LineRhymeScheme(lineRhymes);
//    }

//    public static LineRhymeScheme getChiasmus(LineRhymeScheme rhymeScheme) {
//        rhymeScheme.putAll(getReflection(rhymeScheme));
//        return rhymeScheme;
//    }

    public static LineRhymeScheme getLimerick() {
        return new LineRhymeScheme("A","A","B","B","A");
    }

    public static LineRhymeScheme getBallade() {
        return new LineRhymeScheme(
                "A","B","A","B","B","C","B","C",
                "A","B","A","B","B","C","B","C",
                "A","B","A","B","B","C","B","C",
                "B","C","B","C");
    }

    /*
Couplet: It contains two line stanzas with “A, A,” rhyme scheme that often appears as “A,A, B,B, C,C and D,D…”
Triplet: It often repeats like a couplet, uses rhyme scheme of “AAA.”
Enclosed rhyme: It uses rhyme scheme of “ABBA”
Terza rima rhyme scheme: It uses tercets, three lines stanzas. Its interlocking pattern on end words follow: Aba bcb cdc ded and so on…
Keats Odes rhyme scheme: In his famous odes, Keats has used a specific rhyme scheme, which is “ABABCDECDE.”
Limerick: A poem uses five lines with rhyme scheme of “AABBA.”
Villanelle: A nineteen-line poem consisting of five tercets and a final quatrain is villanelle and uses rhyme scheme of “A1bA2, abA1, abA2, abA1, abA2, abA1A2.”
     */

}



/*

line
line
line
line
line


5, 2, 1 -> ABABA
5, 2, 2 -> AABBA
5, 2, 3 -> AAABB
5, 2, 4 -> AAAAB
5, 2, 5 -> AAAAA bad

5, 3, 1 -> ABCAB
5, 3, 2 -> AABBC
5, 3, 3 -> AAABB


if width * (n of types - 1) + 1 > total, then FAIL
if 3 * 2 + 1 > 5
if 7 > 5 FAIL

if 2 * 2 + 1 > 5
if 5 > 5 PASS

 */



















































































































































