package word2vec;

import elements.Word;
import utils.Pair;
import utils.U;

import java.io.*;
import java.util.*;

import static word2vec.W2vOperations.stringsToPoint;

public class W2vInterface {
    private final int nOfDefaultSuggestions = 100;

    private W2vModel model;

    public W2vInterface(String fileName) {
        this.setupModel(fileName);
    }

    public void setupModel(String fileName) {
        File f = new File(U.rootPath + "data/w2v/models/sers/" + fileName + ".ser");
//        if(f.exists() && !f.isDirectory())
//            this.deserializeW2vModel(fileName);
//        else
            this.buildModel(fileName);
        W2vOperations.setModel(this.model);
    }

//    public void deserializeW2vModel(String fileName) {
//        U.testPrint("Deserializing W2v Model");
//        try {
//            FileInputStream fileIn = new FileInputStream(U.rootPath + "data/w2v/models/sers/" + fileName + ".ser");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            this.model = null;
//            this.model = (W2vModel) in.readObject();
//            this.model.setContents(getContentsMap(fileName));
//            in.close();
//            fileIn.close();
//        }
//        catch(IOException i) {
//            i.printStackTrace();
//        }
//        catch(ClassNotFoundException c) {
//            System.out.println("W2vModel class not found");
//            c.printStackTrace();
//        }
//    }

//    public void serializeW2vModel(String fileName) {
//        try {
//            FileOutputStream fileOut = new FileOutputStream(U.rootPath + "data/w2v/models/sers/" + fileName + ".ser");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(this.model);
//            out.close();
//            fileOut.close();
//            System.out.println("Serialized W2v Model is saved in lyrist/data/w2v/models/sers/" + fileName + ".ser");
//        }
//        catch(IOException i) {
//            i.printStackTrace();
//        }
//    }

    public void buildModel(String fileName) {
        this.model = W2vOperations.buildW2vModel(fileName);
        this.model.setContents(getContentsMap(fileName));
//        this.serializeW2vModel(fileName);
    }

    private Map<String,Integer> getContentsMap(String fileName) {
        Map<String,Integer> result = new HashMap<>();

        File inFile = new File(U.rootPath + "data/w2v/models/vocab-lists/" + fileName + ".txt");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inFile);
            Scanner sc = null;
            sc = new Scanner(fis, "UTF-8");

            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String[] split = s.split(" ");
                result.put(split[0],Integer.parseInt(split[1]));
            }
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<Double, String> findAnalogy(String oldSentiment, String newSentiment, String oldWord, int nOfSuggestions) throws BadW2vInputException {
        checkStringInput(oldSentiment);
        checkStringInput(newSentiment);
        checkStringInput(oldWord);

        // newSentiment - oldSentiment + oldWord
        ArrayList<String> strings = new ArrayList<>();
        strings.add(oldSentiment);
        strings.add(newSentiment);
        strings.add(oldWord);
        Pair<W2vPoint,int[]> pair = stringsToPoint(OperationType.ANALOGY, strings);
        return new HashMap<>(W2vOperations.pointToStrings(pair.getFirst(), pair.getSecond(), nOfSuggestions));
    }

    public Map<Double, String> findSimilars(String string, int nOfSuggestions) throws BadW2vInputException {
        checkStringInput(string);

        // uses chosen suggestion quantity
        ArrayList<String> oneString = new ArrayList<>();
        oneString.add(string);
        Pair<W2vPoint,int[]> pair = W2vOperations.stringsToPoint(OperationType.SINGLE, oneString);
        Map<Double, String> tree = new TreeMap<>(W2vOperations.pointToStrings(pair.getFirst(), pair.getSecond(), nOfSuggestions));
        if (tree.containsValue(pair.getFirst().getString()))
            tree.remove(pair.getFirst().getString());
        return new HashMap<>(tree);
    }

    public Map<Double, String> findSimilars(String string) throws BadW2vInputException {
        checkStringInput(string);

        // uses default suggestion quantity
        return this.findSimilars(string, nOfDefaultSuggestions);
    }

    public Map<Double, String> findSum(Set<Word> words, int nOfSuggestions) throws BadW2vInputException {
        HashSet<String> strings = new HashSet<>();
        for (Word word : words) {
            strings.add(word.getLowerSpelling().toLowerCase());
        }
        for (String s : strings) {
            checkStringInput(s);
        }

        return this.findSum(strings, nOfSuggestions, true);
    }

    public Map<Double, String> findSum(Set<String> strings, int nOfSuggestions, boolean b) throws BadW2vInputException {
        for (String s : strings) {
            checkStringInput(s);
        }

        Pair<W2vPoint,int[]> pair = stringsToPoint(OperationType.SUM, new ArrayList<>(strings));
        return new HashMap<>(W2vOperations.pointToStrings(pair.getFirst(), pair.getSecond(), nOfSuggestions));
    }

    public Map<Double, String> findSum(Set<String> words) throws BadW2vInputException {
        return this.findSum(words, nOfDefaultSuggestions, true);
    }

    public Map<Double, String> findSentiment(Set<String> strings, int nOfSuggestions) throws BadW2vInputException {
        for (String s : strings) {
            checkStringInput(s);
        }

        Pair<W2vPoint,int[]> pair = stringsToPoint(OperationType.AVERAGE, new ArrayList<>(strings));
        return new HashMap<>(W2vOperations.pointToStrings(pair.getFirst(), pair.getSecond(), nOfSuggestions));
    }

    public Double findDistanceBetween(String string1, String string2) throws BadW2vInputException {
        checkStringInput(string1);
        checkStringInput(string2);

        //TODO implement this, especially for choosing the closest rhyming word from a set of distantly-related rhymes
        return null;
    }

    public void checkStringInput(String s) throws BadW2vInputException {
        if (!this.contains(s))
            throw new BadW2vInputException(s);
    }

    public boolean contains(String string) {
        if (model.getContents().containsKey(string))
            return true;
        return false;
    }

}
























































































































