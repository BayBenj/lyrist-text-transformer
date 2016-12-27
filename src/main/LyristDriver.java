package main;

import song.TemplateSongEngineer;
import stanford_nlp.StanfordNlp;
import utils.Utils;
import word2vec.W2vCommander;

import java.io.File;

public class LyristDriver {

    //Generates a new song inspired from a system-selected inspiring idea

        public static void main(String[] args) {

            //Set the root path of Lyrist in Utils
            File currentDirFile = new File("");
            Utils.rootPath = currentDirFile.getAbsolutePath() + "/";

            //Load arguments
            ProgramArgs.loadProgramArgs(args); //TODO: right now it doesn't want any args

            //Set testing variable
            ProgramArgs.setTesting(true);

            //Setup StanfordNlp
            StanfordNlp stanfordNlp = new StanfordNlp();
            Utils.setStanfordNlp(stanfordNlp);

            //Setup W2vCommander
            W2vCommander w2v  = new W2vCommander("GoogleNews-3000000-300");
            Utils.setW2vCommander(w2v);

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























