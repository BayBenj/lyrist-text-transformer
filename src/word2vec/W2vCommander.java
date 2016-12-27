package word2vec;

import song.Word;
import utils.Utils;

import java.io.*;
import java.util.*;

import static word2vec.W2vOperations.stringsToPoint;

public class W2vCommander {
    private final int nOfDefaultSuggestions = 10;

    private W2vModel model;

    public W2vCommander(String fileName) {
        this.setupModel(fileName);
    }

    public void setupModel(String fileName) {
        File f = new File(Utils.rootPath + "local-data/w2v/models/sers/" + fileName + ".ser");
        if(f.exists() && !f.isDirectory())
            this.deserializeW2vModel(fileName);
        else
            this.buildModel(fileName);
        W2vOperations.setModel(this.model);
    }

    public void deserializeW2vModel(String fileName) {
        Utils.testPrint("Deserializing W2v Model");
        try {
            FileInputStream fileIn = new FileInputStream(Utils.rootPath + "local-data/w2v/models/sers/" + fileName + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.model = null;
            this.model = (W2vModel) in.readObject();
            in.close();
            fileIn.close();
        }
        catch(IOException i) {
            i.printStackTrace();
        }
        catch(ClassNotFoundException c) {
            System.out.println("W2vModel class not found");
            c.printStackTrace();
        }
    }

    public void serializeW2vModel(String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(Utils.rootPath + "local-data/w2v/models/sers/" + fileName + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.model);
            out.close();
            fileOut.close();
            System.out.println("Serialized W2v Model is saved in lyrist/local-data/w2v/models/sers/" + fileName + ".ser");
        }
        catch(IOException i) {
            i.printStackTrace();
        }
    }

    public void buildModel(String fileName) {
        this.model = W2vOperations.buildW2vModel(fileName);
        this.serializeW2vModel(fileName);
    }

    public Map<Double, String> findAnalogy(String oldSentiment, String newSentiment, String oldWord, int nOfSuggestions) {
        // newSentiment - oldSentiment + oldWord
        ArrayList<String> strings = new ArrayList<>();
        strings.add(oldSentiment);
        strings.add(newSentiment);
        strings.add(oldWord);
        W2vPoint point = stringsToPoint(OperationType.ANALOGY, strings);
        return new HashMap<>(W2vOperations.pointToStrings(point, nOfSuggestions));
    }

    public Map<Double, String> findSimilars(String string, int nOfSuggestions) {
        // uses chosen suggestion quantity
        ArrayList<String> oneString = new ArrayList<>();
        oneString.add(string);
        W2vPoint point = W2vOperations.stringsToPoint(OperationType.SINGLE, oneString);
        Map<Double, String> tree = new TreeMap<Double, String>(W2vOperations.pointToStrings(point, nOfSuggestions));
        if (tree.containsKey(point.getString()))
            tree.remove(point.getString());
        return new HashMap<>(tree);
    }

    public Map<Double, String> findSimilars(String string) {
        // uses default suggestion quantity
        return this.findSimilars(string, nOfDefaultSuggestions);
    }

    public Map<Double, String> findSum(HashSet<Word> words, int nOfSuggestions) {
        HashSet<String> strings = new HashSet<>();
        for (Word word : words)
            strings.add(word.getSpelling().toLowerCase());
        return this.findSum(strings, nOfSuggestions, true);
    }

    public Map<Double, String> findSum(HashSet<String> strings, int nOfSuggestions, boolean b) {
        W2vPoint sumPoint = stringsToPoint(OperationType.SUM, new ArrayList<String>(strings));
        return new HashMap<>(W2vOperations.pointToStrings(sumPoint, nOfSuggestions));
    }

    public Map<Double, String> findSum(HashSet<String> words) {
        return this.findSum(words, nOfDefaultSuggestions, true);
    }

    public Map<Double, String> findSentiment(HashSet<String> strings, int nOfSuggestions) {
        W2vPoint sumPoint = stringsToPoint(OperationType.AVERAGE, new ArrayList<String>(strings));
        return new HashMap<>(W2vOperations.pointToStrings(sumPoint, nOfSuggestions));
    }

}




























































































































































