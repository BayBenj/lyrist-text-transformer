package main;

import constraints.WordConstraintMaker;
import rhyme.Phoneticizer;
import rhyme.Rhymer;
import songtools.*;
import stanford.StanfordNlp;
import utils.U;
import word2vec.BadW2vInputException;
import word2vec.W2vInterface;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LyristDriver {

    //Generates a new elements inspired from a system-selected inspiring idea

    public static void main(String[] args) throws IOException {
        //Setup
        standardSetup();

        U.startMultiTimer();

        //Load arguments
        MultiProgramArgs.loadMultiProgramArgs(args);

        List<File> templates = new ArrayList<>();
        List<TextComposition> compositions = new ArrayList<>();
        for (File template : templates) {
            SingleTransformationArgs.templateName = template.getName();

            //Read in template song
            final InfoSong templateSong = SongScanner.getInfoSong(MultiProgramArgs.textInFormat, SingleTransformationArgs.templateName);

            //Generate and read in single program args
            String[] argz = TemplateSongEngineer.generateArgs(templateSong, template);
            SingleTransformationArgs.loadSingleTransformationArgs(argz);

            //Check that analogy args are in w2v vocab
            try {
                U.getW2VInterface().checkStringInput(SingleTransformationArgs.oldTheme);
                U.getW2VInterface().checkStringInput(SingleTransformationArgs.newTheme);
            }
            catch (BadW2vInputException e) {
                e.printStackTrace();
                return;
            }

            TextComposition tempComposition = TemplateSongEngineer.generateSongWithArgs(templateSong);
            U.print(tempComposition.toString());
            compositions.add(tempComposition);
        }

        U.stopMultiTimer();
        U.print("TOTAL RUNNING TIME FOR ALL SONGS: " + U.getTotalMultiTime() + "\n");
    }

    public static void standardSetup() {
        setupRootPath();
        setupCmuDict();
        setupStanfordNlp();
        setupW2vInterface();
        setupConstraints();
    }

    public static void setupRootPath() {
        //Set the root path of Lyrist in U
        final File currentDirFile = new File("");
        U.rootPath = currentDirFile.getAbsolutePath() + "/";
    }

    public static void setupCmuDict() {
        Phoneticizer.loadCMUDict();
        U.phoneticizer = new Phoneticizer();
//        Rhymer.deserializePerfRhymes();
    }

    public static void setupStanfordNlp() {
        final StanfordNlp stanfordNlp = new StanfordNlp();
        U.setStanfordNlp(stanfordNlp);
    }

    public static void setupW2vInterface() {
        final W2vInterface w2v  = new W2vInterface("now-91995-300");
        U.setW2VInterface(w2v);
    }

    public static void setupConstraints() {
        WordConstraintMaker.initializeFields();
    }

}































