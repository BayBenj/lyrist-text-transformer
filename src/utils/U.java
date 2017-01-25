package utils;

import main.ProgramArgs;
import rhyme.Phoneticizer;
import elements.Line;
import elements.Song;
import elements.SongElement;
import elements.Stanza;
import stanford.StanfordNlp;
import word2vec.W2vCommander;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.Map.Entry;

public class U {//Utils

    public static String rootPath;

    private static StanfordNlp stanfordNlp;
    private static W2vCommander w2vCommander;
    public static Phoneticizer phoneticizer;

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

    public static Random rand = new Random();

    public static StanfordNlp getStanfordNlp() {
        return stanfordNlp;
    }

    public static void setStanfordNlp(StanfordNlp posTagger_in) {
        stanfordNlp = posTagger_in;
    }

    public static W2vCommander getW2vCommander() {
        return w2vCommander;
    }

    public static void setW2vCommander(W2vCommander w2vCommander_in) {
        w2vCommander = w2vCommander_in;
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public static void print(Object o) {
        System.out.println(o);
    }

    public static void print(Object o1, Object o2) {
        print(o1);
        print(o2);
    }

    public static void printSideBySide(Song s1, Song s2) {
        List<Stanza> s1Stanzas = s1.getStanzas();
        List<Stanza> s2Stanzas = s2.getStanzas();

        List<SongElement> allLines1 = s1.getAllSubElementsOfType(new Line());
        int longest_line_length = -1;
        for (SongElement line : allLines1)
            if (line.toString().length() > longest_line_length)
                longest_line_length = line.toString().length();

        for (int i = 0; i < s1Stanzas.size(); i++) {
            List<Line> s1Lines = s1Stanzas.get(i).getLines();
            List<Line> s2Lines = s2Stanzas.get(i).getLines();
            for (int j = 0; j < s1Lines.size(); j++) {
                int extra_space1 = (longest_line_length + 5) - s1Lines.get(j).toString().length();
                System.out.print(s1Lines.get(j).toString());
                for (int k = 0; k < extra_space1; k++) {
                    System.out.print(" ");
                }
                print(s2Lines.get(j).toString());
            }
            print("");
        }
    }


    public static void testPrintln(Object o1) {
        if (ProgramArgs.isTesting())
            print("\n" + o1);
    }

    public static void testPrint(Object o1) {
        if (ProgramArgs.isTesting())
            print(o1);
    }

    //TODO add handy timer methods here?

    /*
     * Credit to http://javatechniques.com/blog/faster-deep-copies-of-java-objects/
     */
    public static Object deepCopy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    public static <T> String join(List<T> line, String delimiter) {
        StringBuilder str = new StringBuilder();
        boolean first = true;
        for (T object : line) {
            if(!first)
                str.append(delimiter);
            else
                first = false;
            str.append(object);
        }

        return str.toString();
    }

    public static String getPositionString(int i) {
        String posStr = "the ";
        if (i == -1)
        {
            posStr += "LAST";
        }
        else if (i == 0)
        {
            posStr += "FIRST";
        }
        else if (i == 1)
        {
            posStr += "SECOND";
        }
        else if (i == 2)
        {
            posStr += "THIRD";
        }
        else
        {
            posStr += (i+1) + "TH";
        }

        return posStr + " position";
    }

    public static <T extends Comparable<T>> Map<T, List<Integer>> sortByListSize(Map<T, List<Integer>> map, final boolean order) {
        List<Entry<T, List<Integer>>> list = new LinkedList<Entry<T, List<Integer>>>(map.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<T, List<Integer>>>()
        {
            public int compare(Entry<T, List<Integer>> o1,
                               Entry<T, List<Integer>> o2) {
                if (order) {
                    return o1.getValue().size() - o2.getValue().size();
                }
                else {
                    return o2.getValue().size() - o1.getValue().size();

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<T, List<Integer>> sortedMap = new LinkedHashMap<T, List<Integer>>();
        for (Entry<T, List<Integer>> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}




































































































































