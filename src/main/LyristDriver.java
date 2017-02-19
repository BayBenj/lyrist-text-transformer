package main;

import intentions.*;
import rhyme.LineRhymeScheme;
import rhyme.Phoneticizer;
import rhyme.RhymeSchemeManager;
import songtools.*;
import stanford.StanfordNlp;
import utils.Pair;
import utils.U;
import word2vec.W2vInterface;

import java.io.*;

public class LyristDriver {

    //Generates a new elements inspired from a system-selected inspiring idea

        public static void main(String[] args) throws IOException {

            //Set the root path of Lyrist in U
            final File currentDirFile = new File("");
            U.rootPath = currentDirFile.getAbsolutePath() + "/";

            //Load arguments
            final String templateFileName = args[0];
            final String debug = args[1];
            String oldTheme = args[2];
            String newTheme = args[3];
            final String rhymeSchemeInput = args[4];
            final String culture = args[5];
            final String paulFormat = args[6];

            ProgramArgs.loadProgramArgs(args); //TODO: right now it doesn't want any args

            //Set testing variable
            if (!debug.equals("debug"))
                ProgramArgs.setTesting(false);
            else
                ProgramArgs.setTesting(true);

            //Setup StanfordNlp
            final StanfordNlp stanfordNlp = new StanfordNlp();
            U.setStanfordNlp(stanfordNlp);

            //Setup W2vInterface
            final W2vInterface w2v  = new W2vInterface("vectors-phrase-681320-200");
//            W2vInterface w2v  = new W2vInterface("c");
            U.setW2VInterface(w2v);

            //Setup Phoneticizer
            U.phoneticizer = new Phoneticizer();

            //Get template elements, normal read format
            final InfoSong templateSong;
            if (!paulFormat.equals("true")) {
                templateSong = SongScanner.getInfoSong(templateFileName);
            }
            //Get template elements, Paul read format
            else {
                templateSong = SongScanner.getInfoSongPaulFormat(templateFileName);

            }

            //Get rnd themes
            if (oldTheme.equals("rnd") || newTheme.equals("rnd")) {
                Pair<String,String> themes = ThemeManager.getThemePair();
                oldTheme = themes.getFirst();
                newTheme = themes.getSecond();
            }

            //Get rnd rhyme scheme
            final LineRhymeScheme rhymeScheme;
            if (rhymeSchemeInput.equals("rnd")) {
                int nLines = SongScanner.getNLines(templateSong);
                rhymeScheme = RhymeSchemeManager.getRndAlternatingScheme(nLines);
                //TODO fix random rhyme schemes
            }
            else {
                //input would be like this: "a-b-a-b-c"
                rhymeScheme = new LineRhymeScheme(rhymeSchemeInput.split("-"));
            }

            //Get elements intentions from programmer input
            final CompleteIntentions completeIntentions = IntentionManager.getSongIntentions(
                    rhymeScheme, oldTheme, newTheme, culture);

            //Generate a new song
            final TemplateSongEngineer templateSongEngineer = new TemplateSongEngineer();
            final InfoSong newSong = templateSongEngineer.generateSong(completeIntentions, templateSong);

            //Make and print out composition
            final TextComposition composition = new TextComposition(templateSong, newSong, completeIntentions);
            U.print(composition.toString());
        }

}










































