package main;

import utils.Pair;
import utils.U;

public abstract class ThemeManager {

    public static String[] positives = {
            "colorful",
            "light",
            "shimmering",
            "exotic",
            "excitement",
            "crazy",
            "happiness"
    };

    public static String[] negatives = {
            "dull",
            "bleak",
            "dark",
            "boredom",
            "sadness"
    };

    public static Pair<String,String> getThemePair() {
        String t1;
        String t2;
        int i = U.rand.nextInt(5);
        switch (i) {
            case 0:
                t1 = "sad";
                t2 = "happy";
                break;
            case 1:
                t1 = "dull";
                t2 = "shimmering";
                break;
            case 2:
                t1 = "boring";
                t2 = "crazy";
                break;
            case 3:
                t1 = "light";
                t2 = "dark";
                break;
            case 4:
                t1 = "dark";
                t2 = "light";
                break;
            default:
                t1 = "good";
                t2 = "bad";
                break;
        }
        return new Pair<>(t1,t2);
    }

}
