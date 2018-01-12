package word2vec;

import utils.U;

import java.io.File;
import java.util.*;

public class Word2VecTester {

    private static String model;
    private static String nResults;
    private static int results;
    private static String operation;
    private static Scanner input = new Scanner(System.in);
    private static W2vInterface w2v;

    public static void main(String[] args) {
        //Set the root path of Lyrist in U
        File currentDirFile = new File("");
        U.rootPath = currentDirFile.getAbsolutePath() + "/";

        loop:
        while (true) {
//            //Choose word2vec model
//            System.out.println("word2vec model: ");
//            model = input.next();
//            if (model.equals("") || model.matches("\\s+")) {
//                model = "news-lyrics-bom";
//            }
//            w2v = new W2vInterface(model);
            w2v = new W2vInterface("news-lyrics-bom3");


//            //Choose number of results to return
//            System.out.println("# of results: ");
//            nResults = input.next();
//            if (nResults.matches("\\s+") || Integer.parseInt(nResults) < 1) {
//                nResults = "10";
//            }
//            results = Integer.parseInt(nResults);
            results = 50;

            //Choose word2vec operation
            System.out.print("Operation: ");
            operation = input.next();
            Map<Double, String> w2vResults = null;
            try {
                switch (operation) {
                    case "DONE":
                        break loop;
                    case "analogy":
                        w2vResults = analogy();
                        break;
                    case "sum":
                        w2vResults = sum();
                        break;
                    case "similar":
                        w2vResults = similar();
                        break;
                    case "average":
                        w2vResults = average();
                        break;
                    default:
                        w2vResults = analogy();
                        break;
                }
            }
            catch (BadW2vInputException e) {
                System.out.println("\t***Bad W2v Input!");
                e.printStackTrace();
            }
            printResults(w2vResults);
        }
    }

    private static Map<Double, String> sum() throws BadW2vInputException {
        String originalWords = "";
        String word = "";
        System.out.print("Original filterWords, type DONE when done: ");
        while (!word.equals("DONE")) {
            originalWords += " " + word;
            word = input.next();
        }
        String[] separatedWords = originalWords.split("\\s");
        Set<String> actualWords = new HashSet();
        for (String string : separatedWords) {
            if (!string.equals("") && !string.matches("\\s"))
                actualWords.add(string);
        }
        return w2v.findSum(actualWords, results, true);
    }

    private static Map<Double, String> similar() throws BadW2vInputException {
        String originalWord;
        System.out.print("Original word: ");
        originalWord = input.next();
        return w2v.findSimilars(originalWord, results);
    }

    private static Map<Double, String> average() throws BadW2vInputException {
        String originalWords = "";
        String word = "";
        System.out.print("Original filterWords, type DONE when done: ");
        while (!word.equals("DONE")) {
            originalWords += " " + word;
            word = input.next();
        }
        String[] separatedWords = originalWords.split("\\s");
        Set<String> actualWords = new HashSet();
        for (String string : separatedWords) {
            if (!string.equals("") && !string.matches("\\s"))
                actualWords.add(string);
        }
        return w2v.findSentiment(actualWords, results);
    }

    private static Map<Double, String> analogy() throws BadW2vInputException {
        System.out.print("___ is to ___ as ___ is to: ");
        String word1 = input.next();
        String word2 = input.next();
        String word3 = input.next();
        return w2v.findAnalogy(word1, word2, word3, results);
    }

    private static void printResults(Map<Double, String> map) {
        TreeMap<Double, String> treeMap = new TreeMap(map);
        for (Map.Entry<Double, String> entry : treeMap.entrySet()) {
            System.out.println(entry.getValue() + "\t" + entry.getKey());
        }
        System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
    }

}

/*
Should be able to
1) run every word2vec operation,
2) display the desired number of results, and
3) choose the word2vec model
all with console input variables.
 */


























































