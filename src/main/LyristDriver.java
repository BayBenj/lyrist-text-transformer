package main;

import bookofmormon.BookOfMormonMain;
import filters.FilterUtils;
import rhyme.Phoneticizer;
import song.TemplateSongEngineer;
import song.VocabList;
import stanford_nlp.StanfordNlp;
import utils.Utils;
import word2vec.W2vCommander;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class LyristDriver {

    //Generates a new song inspired from a system-selected inspiring idea

        public static void main(String[] args) throws IOException {

            //Set the root path of Lyrist in Utils
            File currentDirFile = new File("");
            Utils.rootPath = currentDirFile.getAbsolutePath() + "/";

            //Load arguments
            ProgramArgs.loadProgramArgs(args); //TODO: right now it doesn't want any args

            //Set testing variable
            ProgramArgs.setTesting(false);

            //Setup StanfordNlp
            StanfordNlp stanfordNlp = new StanfordNlp();
            Utils.setStanfordNlp(stanfordNlp);

            //Setup W2vCommander
            W2vCommander w2v  = new W2vCommander("news-lyrics-bom2");
            Utils.setW2vCommander(w2v);

            //Setup Phoneticizer
            Utils.phoneticizer = new Phoneticizer();

            //set common words
            FilterUtils.setCommonWords(readInCommonWords());


//            //Studio studio = new Studio();
//
//            //Composition newSong = studio.generate();
//
//            //Utils.print(newSong);
//
              //Generate a song
            TemplateSongEngineer templateSongEngineer = new TemplateSongEngineer();
            templateSongEngineer.generateSong();
//            Utils.print(generatedSong.toString());


        }

    private static VocabList readInCommonWords() {
        String filePath = Utils.rootPath + "local-data/vocab-lists/common-words.txt";
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



//    public static void main(String[] args) {
//		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
//		try {
//			BufferedReader br;
//			br = new BufferedReader(new FileReader("/Users/Benjamin/Documents/workspace/BibleWordThing/src/biblewords.txt"));
//
//		    String line = br.readLine();
//
//		    while (line != null) {
//		    	StringBuilder originalLine = new StringBuilder(line);
//		    	int i = Integer.parseInt(line.replaceAll("[^0-9]", ""));
//		    	map.put(i, originalLine.toString());
//
//		        line = br.readLine();
//		    }
//		    br.close();
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		for (Map.Entry<Integer, String> entry : map.entrySet()) {
//			System.out.print(entry.getKey() + ": ");
//			System.out.println(entry.getValue());
//
//		}
//
//	}

}

/*

//Generates a new song inspired from a system-selected inspiring idea

public class PopDriver {
	public static void main(String[] args)
	{
		ProgramArgs.loadProgramArgs(args);

		Studio studio = new Studio();

		Composition newSong = studio.generate();

		System.out.println(newSong);
	}
}
 */























