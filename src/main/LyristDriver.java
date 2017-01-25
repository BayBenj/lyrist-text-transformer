package main;

import intentions.*;
import rhyme.LineRhymeScheme;
import rhyme.Phoneticizer;
import songtools.SongScanner;
import songtools.SongWrapper;
import songtools.TemplateSongEngineer;
import stanford.StanfordNlp;
import utils.U;
import word2vec.W2vCommander;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LyristDriver {

    //Generates a new elements inspired from a system-selected inspiring idea

        public static void main(String[] args) throws IOException {

            //Set the root path of Lyrist in U
            File currentDirFile = new File("");
            U.rootPath = currentDirFile.getAbsolutePath() + "/";

            //Load arguments
            ProgramArgs.loadProgramArgs(args); //TODO: right now it doesn't want any args

            //Set testing variable
            ProgramArgs.setTesting(false);

            //Setup StanfordNlp
            StanfordNlp stanfordNlp = new StanfordNlp();
            U.setStanfordNlp(stanfordNlp);

            //Setup W2vCommander
            W2vCommander w2v  = new W2vCommander("news-lyrics-bom2");
            U.setW2vCommander(w2v);

            //Setup Phoneticizer
            U.phoneticizer = new Phoneticizer();

            //read in vocab lists TODO move this to after I know I will need the filter for it
            VocabManager.readInVocab();

            //Get template elements
            SongWrapper templateSong = SongScanner.getTemplateSong("sorrow.txt");

            //Get elements intentions from programmer input
            SongIntentions songIntentions = IntentionManager.getSongIntentions(
                    new LineRhymeScheme("A","B","A","B"), "strength", "English");

            //Generate a new song
            TemplateSongEngineer templateSongEngineer = new TemplateSongEngineer();
            templateSongEngineer.generateSong(songIntentions, templateSong);
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

//Generates a new elements inspired from a system-selected inspiring idea

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






















































