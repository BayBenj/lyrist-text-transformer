package main;

import utils.U;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public abstract class VocabManager {

    public static VocabList readIn(String fileName) {
        String filePath = U.rootPath + "local-data/vocab-lists/" + fileName;
        File file = new File(filePath);
        Set<String> set = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null)
                set.add(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VocabList(set);
    }

}

























































































































































