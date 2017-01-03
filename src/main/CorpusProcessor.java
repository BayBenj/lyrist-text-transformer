package main;

import edu.stanford.nlp.process.DocumentPreprocessor;

import java.io.*;
import java.util.Scanner;

public class CorpusProcessor {

    public static void main(String[] args) {
        File inFile = new File("/Users/Benjamin/Desktop/code/lyrist/local-data/w2v/corpora/news-lyrics-bom.txt");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc = null;
        FileWriter fw = null;
        try {
            sc = new Scanner(fis, "UTF-8");
            File outFile = new File("/Users/Benjamin/Desktop/code/lyrist/local-data/w2v/corpora/news-lyrics-bom-cleaned.txt");
            fw = new FileWriter(outFile);

            while (sc.hasNextLine()) {
                String string = sc.nextLine();
                if (!string.matches("(\\d|\\W|\\s|[\\d\\s]|[\\W\\s])+")) {
                    string = string.toLowerCase();
                    string = " " + string + " ";
                    string = string.replaceAll("\\s"," ");
                    string = string.replaceAll("\\d"," ");
                    string = string.replaceAll("[^\\w\\s]"," ");
                    string = string.replaceAll(" s "," is ");
                    string = string.replaceAll("n t "," not ");
                    string = string.replaceAll(" ve "," have ");
                    string = string.replaceAll(" d "," would ");
                    string = string.replaceAll(" ll "," will ");
                    string = string.replaceAll(" m "," am ");
                    string = string.replaceAll(" re "," are ");
                    string = string.replaceAll(" [^iao] "," ");
                    string = string.replaceAll(" [^iao] "," ");
                    string = string.replaceAll(" [^iao] "," ");
                    string = string.replaceAll(" +"," ");
                    if (string.length() > 0 && string.charAt(0) == ' ') {
                        string = string.substring(1);
                    }
                    if (string.length() > 0 && string.charAt(string.length() - 1) == ' ') {
                        string = string.substring(0,string.length() - 1);
                    }
                    fw.write(string + "\n");
                }
            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
            try {
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}































































































