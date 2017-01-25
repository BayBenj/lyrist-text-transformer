package filters;

public class FilterUtils {

    private static VocabList commonWords;
    private static VocabList bibleWords;
    private static VocabList modelWords;
    private static long startTime;
    private static long endTime;

    public static void startTimer() {
        startTime = System.nanoTime();
    }

    public static void stopTimer() {
        endTime = System.nanoTime();
    }

    public static String getTotalTime() {
        return ((endTime - startTime) / 1000000) + " milliseconds (" + ((endTime - startTime) / 1000000000) + " seconds).";
    }


    public static VocabList getCommonWords() {
        return commonWords;
    }

    public static VocabList getBibleWords() {
        return bibleWords;
    }

    public static void setCommonWords(VocabList commonWords) {
        FilterUtils.commonWords = commonWords;
    }

    public static void setBibleWords(VocabList bibleWords) {
        FilterUtils.bibleWords = bibleWords;
    }

    public static VocabList getModelWords() {
        return modelWords;
    }

    public static void setModelWords(VocabList modelWords) {
        FilterUtils.modelWords = modelWords;
    }
}



















































































