package stanford;

import elements.Pos;
import elements.Word;

public abstract class LemmaManager {

    private static boolean sameBase(Word w1, Word w2) {
        return sameBase(w1.getBase(), w2.getBase());
    }

    private static boolean sameFam(Word w1, Word w2) {
        return w1.getPos().getFam() == w2.getPos().getFam();
    }

    private static boolean sameBase(String b1, String b2) {
        return b1.equals(b2);
    }

    public static boolean sameBaseAndFam(Word w1, Word w2) {
        Pos p1 = w1.getPos();
        Pos p2 = w2.getPos();
        return sameBase(w1, w2) && sameFam(w1, w2);
    }

//    public static String baseToInstance(String base) {
//
//    }

}
