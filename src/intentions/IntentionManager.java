package intentions;

import rhyme.Meter;
import rhyme.LineRhymeScheme;
import rhyme.RhymeScheme;
import rhyme.RhymeSchemeManager;
import utils.U;
import java.util.ArrayList;
import java.util.List;

public abstract class IntentionManager {

    public static SongIntentions getSongIntentions(LineRhymeScheme rhymeScheme, String emotion, String culture) {
        StructuralIntentions structuralIntentions = new StructuralIntentions();
        structuralIntentions.setRhymeScheme(rhymeScheme);
        List<EmotionalIntention> emotionalIntentions = new ArrayList<>();
        emotionalIntentions.add(new EmotionalIntention(emotion));
        List<CulturalIntention> culturalIntentions = new ArrayList<>();
        culturalIntentions.add(new CulturalIntention(culture));
        return new SongIntentions(structuralIntentions, emotionalIntentions, culturalIntentions);
    }

    public static SongIntentions getRandomSongIntentions(int lines) {
        return new SongIntentions(getRandomStructuralIntentions(lines), getRandomEmotionalIntention(), getRandomCulturalIntention());
    }

    public static StructuralIntentions getRandomStructuralIntentions(int lines) {
        StructuralIntentions structuralIntentions = new StructuralIntentions();
        structuralIntentions.setRhymeScheme(getRandomRhymeScheme(lines));
        return structuralIntentions;
    }

    public static RhymeScheme getRandomRhymeScheme(int lines) {
//        int rnd = U.rand.nextInt(4);
        int rnd = 0;
        RhymeScheme rhymeScheme = null;
        switch (rnd) {
            case 0:
                RhymeSchemeManager.getAlternatingScheme(lines, U.rand.nextInt(lines - 1) + 2, 1);
                rhymeScheme = new LineRhymeScheme();
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






































































































































