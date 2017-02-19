package main;

import songtools.TextInFormat;

import java.util.Arrays;

public abstract class MultiProgramArgs {

    public static boolean debugMode;
    public static TextInFormat textInFormat;
    public static String templateDirectory;
    public static IntentionSelectionMode oldThemeSelectionMode;
    public static IntentionSelectionMode newThemeSelectionMode;
    public static IntentionSelectionMode cultureSelectionMode;
    public static IntentionSelectionMode rhymeSchemeSelectionMode;

    public enum UISetting {
        UNSET, COMMANDLINE, GUI
    }

    public enum SongConfigSourceSetting {
        FROM_FILE, RANDOM, FROM_COMMANDLINE, UNSET, SIMPLE, TEST, DISTRIBUTIONAL
    }

    public static SongConfigSourceSetting configurationSetting = SongConfigSourceSetting.UNSET;
    public static UISetting userInterfaceSetting = UISetting.UNSET;

    public static void loadMultiProgramArgs(String[] args) {
        if (args.length != 7)
            throw new UnsupportedOperationException("Illegal arguments passed to program: " + Arrays.toString(args));

        templateDirectory(args[0]);
        debugMode(args[1]);
        oldThemeSelectionMode(args[2]);
        newThemeSelectionMode(args[3]);
        cultureSelectionMode(args[4]);
        rhymeSchemeSelectionMode(args[5]);
        textInFormat(args[6]);
    }

    private static void loadDefaultProgramArgs() {
//		configurationSetting = SongConfigSourceSetting.TEST;
        configurationSetting = SongConfigSourceSetting.DISTRIBUTIONAL;
        userInterfaceSetting = UISetting.COMMANDLINE;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }

    public static void setDebugMode(boolean testing_in) {
        debugMode = testing_in;
    }

    private static void debugMode(String debug) {
        if (debug.equalsIgnoreCase("debug"))
            debugMode = true;
        else
            debugMode = false;
    }

    private static void textInFormat(String format) {
        if (format.equalsIgnoreCase("paul"))
            textInFormat = TextInFormat.PAUL;
        else
            textInFormat = TextInFormat.NORMAL;
    }

    private static void templateDirectory(String dir) {
        templateDirectory = dir;
    }

    private static void oldThemeSelectionMode(String mode) {
        if (mode.equalsIgnoreCase("rnd"))
            oldThemeSelectionMode = IntentionSelectionMode.RND;
        else if (mode.equalsIgnoreCase("input"))
            oldThemeSelectionMode = IntentionSelectionMode.INPUT;
        else if (mode.equalsIgnoreCase("scan"))
            oldThemeSelectionMode = IntentionSelectionMode.SCAN;
        else
            oldThemeSelectionMode = IntentionSelectionMode.DEFAULT;
    }

    private static void newThemeSelectionMode(String mode) {
        if (mode.equalsIgnoreCase("rnd"))
            newThemeSelectionMode = IntentionSelectionMode.RND;
        else if (mode.equalsIgnoreCase("input"))
            newThemeSelectionMode = IntentionSelectionMode.INPUT;
        else if (mode.equalsIgnoreCase("scan"))
            newThemeSelectionMode = IntentionSelectionMode.SCAN;
        else
            newThemeSelectionMode = IntentionSelectionMode.DEFAULT;
    }

    private static void cultureSelectionMode(String mode) {
        if (mode.equalsIgnoreCase("rnd"))
            cultureSelectionMode = IntentionSelectionMode.RND;
        else if (mode.equalsIgnoreCase("input"))
            cultureSelectionMode = IntentionSelectionMode.INPUT;
        else if (mode.equalsIgnoreCase("scan"))
            cultureSelectionMode = IntentionSelectionMode.SCAN;
        else
            cultureSelectionMode = IntentionSelectionMode.DEFAULT;
    }

    private static void rhymeSchemeSelectionMode(String mode) {
        if (mode.equalsIgnoreCase("rnd"))
            rhymeSchemeSelectionMode = IntentionSelectionMode.RND;
        else if (mode.equalsIgnoreCase("input"))
            rhymeSchemeSelectionMode = IntentionSelectionMode.INPUT;
        else if (mode.equalsIgnoreCase("scan"))
            rhymeSchemeSelectionMode = IntentionSelectionMode.SCAN;
        else
            rhymeSchemeSelectionMode = IntentionSelectionMode.DEFAULT;
    }


}
























//TODO: oooh, enums within a class?
























































