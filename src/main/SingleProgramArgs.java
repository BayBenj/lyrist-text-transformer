package main;

import java.util.Arrays;

public abstract class SingleProgramArgs extends MultiProgramArgs {

    public static String oldTheme;
    public static String newTheme;
    public static String culture;
    public static String rhymeScheme;
    public static String templateName;

    public static void loadSingleProgramArgs(String[] args) {
        if (args.length != 7)
            throw new UnsupportedOperationException("Illegal arguments passed to program: " + Arrays.toString(args));

        oldTheme = args[0].toLowerCase();
        newTheme = args[1].toLowerCase();
        culture = args[2];
        rhymeScheme = args[3];
        templateName = args[4];
    }

    public static void loadExtraProgramArgs(String... extraArgs) {
        if (extraArgs.length != 2)
            throw new UnsupportedOperationException("Illegal arguments passed to program: " + Arrays.toString(extraArgs));

        debugMode(extraArgs[0]);
        textInFormat(extraArgs[1]);
    }

    public static void clearSingleProgramArgs() {
        oldTheme = null;
        newTheme = null;
        culture = null;
        rhymeScheme = null;
        templateName = null;
    }

}





































































