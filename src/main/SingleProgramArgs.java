package main;

import java.util.Arrays;

public abstract class SingleProgramArgs extends MultiProgramArgs {

    public static String oldTheme;
    public static String newTheme;
    public static String culture;
    public static String rhymeScheme;
    public static String templateName;

    public static void loadSingleProgramArgs(String[] args) {
        if (args.length != 5)
            throw new UnsupportedOperationException("Illegal arguments passed to program: " + Arrays.toString(args));

        oldTheme = args[0].toLowerCase();
        newTheme = args[1].toLowerCase();
        culture = args[2];
        rhymeScheme = args[3];
        templateName = args[4];
    }

    public static void clearSingleProgramArgs() {
        oldTheme = null;
        newTheme = null;
        culture = null;
        rhymeScheme = null;
        templateName = null;
    }

}



































































