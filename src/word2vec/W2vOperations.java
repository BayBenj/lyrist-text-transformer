package word2vec;

import utils.Pair;
import utils.U;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.toIntExact;

public abstract class W2vOperations {

    private static W2vModel model;
    private static final long maxWordLength = 25;  // max length of vocabulary entries in vector. Important for w2v operations!

    public static W2vModel buildW2vModel(String fileName) {
        U.testPrint("Entering buildW2vModel");
        File file;

        //TODO: is this st1 correct?
        double len;
        long numberOfWordsInVector;
        long numberOfDimensionsInVector;
        long a, b;

        file = new File(fileName + ".bin");

        try {
            U.testPrint("Building W2v Model");
            DataInputStream data_in = new DataInputStream(new BufferedInputStream(new FileInputStream(U.rootPath + "local-data/w2v/models/bins/" + file)));

            //TODO: fix
//            numberOfWordsInVector = 681320;     //word-phrase
//            numberOfWordsInVector = 682454;   //news2012
//            numberOfWordsInVector = 38160;   //1600s-lotr
//            numberOfWordsInVector = 3000000;   //GoogleNews-3000000-300
            numberOfWordsInVector = 109478;   //news-lyrics-bom
//            numberOfWordsInVector = 288340;   //songtools 288340-500-5
            System.out.println("Words: " + numberOfWordsInVector);
            data_in.readLong();

            //TODO: fix
//            numberOfDimensionsInVector = 200;   //word-phrase
//            numberOfDimensionsInVector = 300; //news2012
//            numberOfDimensionsInVector = 1000; //1600s-lotr
//            numberOfDimensionsInVector = 300; //GoogleNews-3000000-300
            numberOfDimensionsInVector = 500; //news-lyrics-bom.bin
//            numberOfDimensionsInVector = 500;   //songtools 288340-500-5
            System.out.println("Size: " + numberOfDimensionsInVector);
            data_in.readLong();

            //TODO: fix
            float[] M = new float[(int) (numberOfDimensionsInVector + numberOfDimensionsInVector * numberOfWordsInVector + 1)];
            char[] vocab = new char[toIntExact(maxWordLength * numberOfWordsInVector + maxWordLength + 1)];

            for (b = 0; b < numberOfWordsInVector; b++) {
                a = 0;
                boolean eof = false;
                while (!eof) {
                    try {
                        vocab[toIntExact(b * maxWordLength + a)] = (char) data_in.readByte();
                        if (eof)
                            break;
                        if (vocab[toIntExact(b * maxWordLength + a)] == (int) ' ')
                            break;

                        if ((a < maxWordLength) && (vocab[toIntExact(b * maxWordLength + a)] != (int) '\n'))
                            a++;
                    } catch (EOFException e) {
                        eof = true;
                    }
                }
                vocab[toIntExact(b * maxWordLength + a)] = '0';

                for (a = 0; a < numberOfDimensionsInVector; a++) {
                    //TODO: understand how to do this
                    byte[] bytes = new byte[4];
                    data_in.read(bytes);
                    int asInt = (bytes[0] & 0xFF)
                            | ((bytes[1] & 0xFF) << 8)
                            | ((bytes[2] & 0xFF) << 16)
                            | ((bytes[3] & 0xFF) << 24);
                    float asFloat = Float.intBitsToFloat(asInt);
                    M[toIntExact(a + b * numberOfDimensionsInVector)] = asFloat;
                }

                len = 0;
                for (a = 0; a < numberOfDimensionsInVector; a++)
                    len += M[toIntExact(a + b * numberOfDimensionsInVector)] * M[toIntExact(a + b * numberOfDimensionsInVector)];
                len = (double) java.lang.Math.sqrt(len);
                for (a = 0; a < numberOfDimensionsInVector; a++) {
                    M[toIntExact((a + b * numberOfDimensionsInVector))] /= (len);
                }
            }
            data_in.close();
            W2vModel result = new W2vModel(numberOfWordsInVector, numberOfDimensionsInVector, M, vocab);
            model = result;
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static W2vPoint runAnalogy(W2vModel model, String oldSentiment, String newSentiment, String oldWord) {
//        // Grab data from word2vec model
//        long numberOfWordsInVector = model.getNumberOfWordsInVector();
//        long numberOfDimensionsInVector = model.getNumberOfDimensionsInVector();
//        float[] M = model.getM();
//        char[] vocab = model.getVocab();
//
//        // Initialize new variables
//        double len;
//        long a, b;
//        String[] inputWords = new String[] {oldSentiment, newSentiment, oldWord};
//        double[] point = new double[toIntExact(numberOfDimensionsInVector)];
//        long[] inputWordVocabPositions = new long[3];
//
//        U.testPrint("Performing W2v Analogy: [" + oldSentiment + "] is to [" + newSentiment + "] as [" + oldWord + "] is to ?");
//
//        int number_of_input_words = 3;
//        for (a = 0; a < number_of_input_words; a++) {
//            outerloop:
//            for (b = 0; b < numberOfWordsInVector; b++) {
//                //TODO: ensure this works
//                String temp = "";
//                int x = 0;
//                while (vocab[toIntExact(b * maxWordLength + x)] == inputWords[toIntExact(a)].charAt(x)) {
//                    temp += vocab[toIntExact(b * maxWordLength + x)];
//                    String tempString = inputWords[toIntExact(a)];
//                    if (temp.equals(tempString))
//                        break outerloop;
//                    x++;
//                }
//            }
//            if (b == numberOfWordsInVector)
//                b = 0;
//            inputWordVocabPositions[toIntExact(a)] = b;
//            if (b == 0) {
//                System.out.println("# Out of dictionary (input?) word! Whole analogy is broken!");
//                break;
//            }
//        }
//        //TODO: Remember that this is the place the actual analogy occurs
//        for (a = 0; a < numberOfDimensionsInVector; a++)
//            point[toIntExact(a)] =
//                    M[toIntExact(a + inputWordVocabPositions[1] * numberOfDimensionsInVector)]
//                            - M[toIntExact(a + inputWordVocabPositions[0] * numberOfDimensionsInVector)]
//                            + M[toIntExact(a + inputWordVocabPositions[2] * numberOfDimensionsInVector)];
//        len = 0;
//        for (a = 0; a < numberOfDimensionsInVector; a++)
//            len += point[toIntExact(a)] * point[toIntExact(a)];
//        len = java.lang.Math.sqrt(len);
//        for (a = 0; a < numberOfDimensionsInVector; a++)
//            point[toIntExact(a)] /= len;
//
//        String closest_word = getClosestWord(model, new W2vPoint(point, "[N/A]", inputWordVocabPositions, number_of_input_words) );
//
//        U.testPrint("\tThe best analogous word was: [" + closest_word + "]");
//
//        return new W2vPoint(point, closest_word, inputWordVocabPositions, number_of_input_words);
//    }

    public static Map<Double, String> pointToStrings(W2vPoint referencePoint,
                                                     int[] inputWordVocabPositions,
                                                     final int number_of_suggestions_to_show) {
        // Grab data from point in vector
        String word = referencePoint.getString();
        int number_of_input_words = inputWordVocabPositions.length;
        double[] point = referencePoint.getPoint();

        // Grab data from word2vec model
        long numberOfWordsInVector = model.getNumberOfWordsInVector();
        long numberOfDimensionsInVector = model.getNumberOfDimensionsInVector();
        float[] M = model.getM();
        char[] vocab = model.getVocab();

        // Initialize new variables
        String[] closest_words = new String[toIntExact(number_of_suggestions_to_show)];
        double[] closest_distances = new double[toIntExact(number_of_suggestions_to_show)];
        long a, b, c, d;

        if (number_of_suggestions_to_show > 1 && !word.equals("NO_WORD_YET"))
            U.testPrint("Finding word2vec suggestions similar to [" + word + "]...");

        // Fill words with blank values
        for (a = 0; a < number_of_suggestions_to_show; a++)
            closest_words[toIntExact(a)] = "";

        for (c = 0; c < numberOfWordsInVector; c++) {
            for (int l = 0; l < inputWordVocabPositions.length; l++)
                if (c == inputWordVocabPositions[l])
                    continue;
            a = 0;
            for (b = 0; b < number_of_input_words; b++)
                if (inputWordVocabPositions[toIntExact(b)] == c) a = 1;
            if (a == 1)
                continue;
            double dist = 0;
            for (a = 0; a < numberOfDimensionsInVector; a++)
                dist += point[toIntExact(a)] * M[toIntExact(a + c * numberOfDimensionsInVector)];
            for (a = 0; a < number_of_suggestions_to_show; a++) {
                if (dist > closest_distances[toIntExact(a)]) {
                    for (d = number_of_suggestions_to_show - 1; d > a; d--) {
                        closest_distances[toIntExact(d)] = closest_distances[toIntExact(d - 1)];

                        StringBuilder sb = new StringBuilder(closest_words[toIntExact(d - 1)]);
                        closest_words[toIntExact(d)] = sb.toString();

                    }
                    closest_distances[toIntExact(a)] = dist;
                    char not0 = vocab[toIntExact(c * maxWordLength)];
                    String temp = "";
                    int x = 1;
                    while (not0 != '0') {
                        temp += not0;
                        not0 = vocab[toIntExact(c * maxWordLength + x)];
                        x++;
                    }
                    closest_words[toIntExact(a)] = temp;
                    break;
                }
            }
        }

        // Put resulting suggestions into an object to return (distances are negative for sorting)
        Map<Double, String> suggestions = new HashMap<>();
        for (a = 0; a < number_of_suggestions_to_show; a++) {
            if (a == 0 && number_of_suggestions_to_show > 1)
                U.testPrint("\tword2vec found " + number_of_suggestions_to_show + " words.");
            suggestions.put(-1 * closest_distances[toIntExact(a)], closest_words[toIntExact(a)]);
        }
        return suggestions;
    }

//    public static W2vPoint average(
//            // W2vPoint... points
//    ) {
//        // W2vPoint sum = this.sum(points);
//        // int nOfPoints = points.size();
//        // W2vPoint centroid = sum / nOfPoints;
//        // return centroid;
//        // return centroid;
//        return null;
//    }
//
//    public static W2vPoint sum(W2vModel model, HashSet<W2vPoint> points
//            // W2vPoint... points
//    ) {
//        // Grab data from word2vec model
//        long numberOfWordsInVector = model.getNumberOfWordsInVector();
//        long numberOfDimensionsInVector = model.getNumberOfDimensionsInVector();
//        float[] M = model.getM();
//        char[] vocab = model.getVocab();
//
//        // Initialize new variables
//        int nOfPoints = points.size();
//        double len;
//        long a, b;
//        String[] inputWords = new String[nOfPoints];
//        double[] summedPoint = new double[toIntExact(numberOfDimensionsInVector)];
//        long[] inputWordVocabPositions = new long[nOfPoints];
//
//        U.testPrint("Summing W2v points: ");
//        int i = 0;
//        for (W2vPoint point : points) {
//            if (i >= nOfPoints - 1)
//                U.testPrint("[" + point.getClosestString() + "]");
//            else
//                U.testPrint("[" + point.getClosestString() + "], ");
//            i++;
//        }
//
//        for (a = 0; a < nOfPoints; a++) {
//            outerloop:
//            for (b = 0; b < numberOfWordsInVector; b++) {
//                //TODO: ensure this works
//                String temp = "";
//                int x = 0;
//                while (vocab[toIntExact(b * maxWordLength + x)] == inputWords[toIntExact(a)].charAt(x)) {
//                    temp += vocab[toIntExact(b * maxWordLength + x)];
//                    String tempString = inputWords[toIntExact(a)];
//                    if (temp.equals(tempString))
//                        break outerloop;
//                    x++;
//                }
//            }
//            if (b == numberOfWordsInVector)
//                b = 0;
//            inputWordVocabPositions[toIntExact(a)] = b;
//            if (b == 0) {
//                System.out.println("# Out of dictionary (input?) word! Whole sum is broken!");
//                break;
//            }
//        }
//
//        for (a = 0; a < numberOfDimensionsInVector; a++) {
//            double total = 0;
//            //right now it's doing average :)
//            for (i = 0; i < nOfPoints; i++)
//                total += M[toIntExact(a + inputWordVocabPositions[i] * numberOfDimensionsInVector)];
//            total /= nOfPoints;
//            summedPoint[toIntExact(a)] = total;
//        }
//        len = 0;
//        for (a = 0; a < numberOfDimensionsInVector; a++)
//            len += summedPoint[toIntExact(a)] * summedPoint[toIntExact(a)];
//        len = java.lang.Math.sqrt(len);
//        for (a = 0; a < numberOfDimensionsInVector; a++)
//            summedPoint[toIntExact(a)] /= len;
//
//        String closest_word = getClosestWord(model, new W2vPoint(summedPoint, "[N/A]", inputWordVocabPositions, nOfPoints) );
//
//        U.testPrint("\tThe summed words gave the word: [" + closest_word + "]");
//
//        return new W2vPoint(summedPoint, closest_word, inputWordVocabPositions, nOfPoints);
//    }
//
//    public static W2vPoint subtract(
//            // W2vPoint point1, W2vPoint point2
//    ) {
//        // subtract second input from first input
//        return null;
//    }
//
//    public static W2vPoint multiply(
//            // W2vPoint... points
//    ) {
//        // multiply all inputs
//        return null;
//    }
//
//    public static W2vPoint divide(
//            // W2vPoint point1, W2vPoint point2
//    ) {
//        // divide first input by second input
//        return null;
//    }

//    public static W2vSuggestion getClosestWord(W2vModel model, W2vPoint point) {
//        TreeSet<W2vSuggestion> tree = new TreeSet<W2vSuggestion>(pointToStrings(model, point, 1));
//        return tree.first();
//    }

//    public static W2vPoint getPointOfWord(W2vModel model, String word) {
//        ArrayList<W2vPoint> point = new ArrayList<W2vPoint>();
//        point.add(new W2vPoint(null, word, null, 1));
//        return stringsToPoint(OperationType.SINGLE, model, point);
////        // Grab data from word2vec model
////        long numberOfWordsInVector = model.getNumberOfWordsInVector();
////        long numberOfDimensionsInVector = model.getNumberOfDimensionsInVector();
////        float[] M = model.getM();
////        char[] vocab = model.getVocab();
////
////        // Initialize new variables
////        int max_input_string_size = word.length();
////        double len;
////        long a, b;
////        String[] inputWords = new String[] {word};
////        double[] point = new double[toIntExact(numberOfDimensionsInVector)];
////        long inputWordVocabPosition = -1;
////
////        U.testPrint("Finding point for " + word + "...");
////
////        int number_of_input_words = 1;
////        for (a = 0; a < number_of_input_words; a++) {
////            outerloop:
////            for (b = 0; b < numberOfWordsInVector; b++) {
////                //TODO: ensure this works
////                String temp = "";
////                int x = 0;
////                while (vocab[toIntExact(b * maxWordLength + x)] == inputWords[toIntExact(a)].charAt(x)) {
////                    temp += vocab[toIntExact(b * maxWordLength + x)];
////                    String tempString = inputWords[toIntExact(a)];
////                    if (temp.equals(tempString))
////                        break outerloop;
////                    x++;
////                }
////            }
////            if (b == numberOfWordsInVector)
////                b = 0;
////            inputWordVocabPosition = b;
////            if (b == 0) {
////                System.out.println("# Out of dictionary (input?) word! Whole analogy is broken!");
////                break;
////            }
////        }
////        //TODO: Remember that this is the place the actual analogy occurs
////        for (a = 0; a < numberOfDimensionsInVector; a++)
////            point[toIntExact(a)] = M[toIntExact(a + inputWordVocabPosition * numberOfDimensionsInVector)];
////        len = 0;
////        for (a = 0; a < numberOfDimensionsInVector; a++)
////            len += point[toIntExact(a)] * point[toIntExact(a)];
////        len = java.lang.Math.sqrt(len);
////        for (a = 0; a < numberOfDimensionsInVector; a++)
////            point[toIntExact(a)] /= len;
////
////        return new W2vPoint(point, word, new long[] {inputWordVocabPosition}, number_of_input_words);
    //}

    public static Pair<W2vPoint, int[]> stringsToPoint(OperationType type, List<String> strings) {
        // Grab data from word2vec model
        long numberOfWordsInVector = model.getNumberOfWordsInVector();
        long numberOfDimensionsInVector = model.getNumberOfDimensionsInVector();
        float[] M = model.getM();
        char[] vocab = model.getVocab();

        // Initialize new variables
        int nOfPoints = strings.size();
        double len;
        long a, b;
        String[] inputWords = new String[nOfPoints];
        int i = 0;
        for (String string : strings) {
            if (!string.equals("NO_WORD_YET")) {
                inputWords[i] = string;
                i++;
            }
            else {
                System.out.println("\t***BAD WORD IN stringsToPoint!!!!!***");
            }
        }
        double[] resultPoint = new double[toIntExact(numberOfDimensionsInVector)];
        int[] inputWordVocabPositions = new int[nOfPoints];

        prePrint(type, strings);

        //Ensure that word2vec model contains all inputted words
        for (a = 0; a < nOfPoints; a++) {
            outerloop:
            for (b = 0; b < numberOfWordsInVector; b++) {
                String temp = "";
                int x = 0;
                while (vocab[toIntExact(b * maxWordLength + x)] == inputWords[toIntExact(a)].charAt(x)) {
                    temp += vocab[toIntExact(b * maxWordLength + x)];
                    String tempString = inputWords[toIntExact(a)];
                    if (temp.equals(tempString))
                        break outerloop;
                    x++;
                }
            }
            if (b == numberOfWordsInVector)
                b = 0;
            inputWordVocabPositions[toIntExact(a)] = toIntExact(b);
            if (b == 0) {
                System.out.println("# Out of dictionary input word! Whole word2vec operation is broken!");
                break;
            }
        }

        vectorOperation(type, M, inputWordVocabPositions, numberOfDimensionsInVector, nOfPoints, resultPoint);

        // Should this happen for every single operation?
        len = 0;
        for (a = 0; a < numberOfDimensionsInVector; a++)
            len += resultPoint[toIntExact(a)] * resultPoint[toIntExact(a)];
        len = java.lang.Math.sqrt(len);
        for (a = 0; a < numberOfDimensionsInVector; a++)
            resultPoint[toIntExact(a)] /= len;

        //String closest_word = pointToStrings(new W2vPoint(resultPoint, inputWordVocabPositions, nOfPoints), 1).get(0).getString();

        //postPrint(type, closest_word);

        return new Pair<>(new W2vPoint(resultPoint), inputWordVocabPositions);
    }

    private static void prePrint(OperationType type, List<String> strings) {
        final int nOfPoints = strings.size();
        switch (type) {
            case SUM:
                U.testPrint("Summing W2v points: ");
                int i = 0;
                for (String string : strings) {
                    if (i >= nOfPoints - 1)
                        U.testPrint("[" + string + "]");
                    else
                        U.testPrint("[" + string + "], ");
                    i++;
                }
                break;
            case AVERAGE:
                U.testPrint("Averaging W2v points: ");
                i = 0;
                for (String string : strings) {
                    if (i >= nOfPoints - 1)
                        U.testPrint("[" + string + "]");
                    else
                        U.testPrint("[" + string + "], ");
                    i++;
                }
                break;
            case ANALOGY:
                U.testPrint("Performing W2v Analogy: [" + strings.get(0) + "] is to [" + strings.get(1) + "] as [" + strings.get(2) + "] is to ?");
                break;
            case SINGLE:
                U.testPrint("Finding point of : [" + strings.get(0) + "]...");
                break;
        }
    }

    private static void postPrint(OperationType type, String closest_word) {
        switch (type) {
            case SUM:
                U.testPrint("\tThe summed words added up to the word: [" + closest_word + "]");
                break;
            case AVERAGE:
                U.testPrint("\tThe averaged words gave the word: [" + closest_word + "]");
                break;
            case ANALOGY:
                U.testPrint("\tThe best analogous word was: [" + closest_word + "]");
                break;
            case SINGLE:
                U.testPrint("\tFound point for: [" + closest_word + "]...");
                break;
        }
    }

    private static void vectorOperation(OperationType type, float[] M, int[] inputWordVocabPositions, long numberOfDimensionsInVector, int nOfPoints, double[] resultPoint) {
        switch (type) {
            case SUM:
                for (int a = 0; a < numberOfDimensionsInVector; a++) {
                    double total = 0;
                    for (int i = 0; i < nOfPoints; i++)
                        total += M[toIntExact(a + inputWordVocabPositions[i] * numberOfDimensionsInVector)];
                    resultPoint[toIntExact(a)] = total;
                }
                break;
            case AVERAGE:
                for (int a = 0; a < numberOfDimensionsInVector; a++) {
                    double total = 0;
                    for (int i = 0; i < nOfPoints; i++)
                        total += M[toIntExact(a + inputWordVocabPositions[i] * numberOfDimensionsInVector)];
                    total /= nOfPoints;
                    resultPoint[toIntExact(a)] = total;
                }
                break;
            case ANALOGY:
                for (int a = 0; a < numberOfDimensionsInVector; a++)
                    resultPoint[toIntExact(a)] = 0
                            + M[toIntExact(a + inputWordVocabPositions[1] * numberOfDimensionsInVector)]
                            - M[toIntExact(a + inputWordVocabPositions[0] * numberOfDimensionsInVector)]
                            + M[toIntExact(a + inputWordVocabPositions[2] * numberOfDimensionsInVector)];
                break;
            case SINGLE:
                for (int a = 0; a < numberOfDimensionsInVector; a++)
                    resultPoint[toIntExact(a)] = M[toIntExact(a + inputWordVocabPositions[0] * numberOfDimensionsInVector)];
                break;
        }
    }

    public static void setModel(W2vModel model) {
        W2vOperations.model = model;
    }
}








/*
TODO > Get rid of inputWordVocabPositions (maybe not actually)

TODO > Rename iterating longs after their role, like dim for nOfDimension
 */
































/*

Input: Woman, Man, Queen
Output: Man - Woman + Queen = King

 */






































