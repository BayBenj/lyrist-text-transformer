package intentions;

import main.ThemeManager;
import rhyme.Meter;
import rhyme.LineRhymeScheme;
import rhyme.RhymeSchemeManager;
import utils.U;
import java.util.ArrayList;
import java.util.List;

public abstract class IntentionManager {

    public static CompleteIntentions getSongIntentions(LineRhymeScheme rhymeScheme, String oldTheme, String newTheme, String culture) {
        StructuralIntentions structuralIntentions = new StructuralIntentions();
        structuralIntentions.setRhymeScheme(rhymeScheme);
        List<EmotionalIntention> emotionalIntentions = new ArrayList<>();
        emotionalIntentions.add(new EmotionalIntention(newTheme));
        List<CulturalIntention> culturalIntentions = new ArrayList<>();
        culturalIntentions.add(new CulturalIntention(culture));
        return new CompleteIntentions(structuralIntentions, oldTheme, emotionalIntentions, culturalIntentions);
    }

    public static CompleteIntentions getRandomSongIntentions(int lines) {
        return new CompleteIntentions(getRandomStructuralIntentions(lines), ThemeManager.getRndThemePair().getFirst(), getRandomEmotionalIntention(), getRandomCulturalIntention());
    }

    public static StructuralIntentions getRandomStructuralIntentions(int lines) {
        StructuralIntentions structuralIntentions = new StructuralIntentions();
        structuralIntentions.setRhymeScheme(getRandomRhymeScheme(lines));
        return structuralIntentions;
    }

    public static LineRhymeScheme getRandomRhymeScheme(int lines) {
//        int rnd = U.rand.nextInt(4);
        int rnd = 0;
        LineRhymeScheme rhymeScheme = null;
        switch (rnd) {
            case 0:
                rhymeScheme = RhymeSchemeManager.getAlternatingScheme(lines, U.rand.nextInt(lines - 1) + 2, 1);
                break;
            default:
                break;

        }
        return rhymeScheme;
    }

    public static Meter getRandomMeter() {
        return new Meter();
    }

    public static List<EmotionalIntention> getRandomEmotionalIntention() {
        List<EmotionalIntention> list = new ArrayList<>();
        list.add(new EmotionalIntention("excitement"));
        return list;
    }

    public static List<CulturalIntention> getRandomCulturalIntention() {
        List<CulturalIntention> list = new ArrayList<>();
        list.add(new CulturalIntention("English"));
        return list;
    }

}







































































































































