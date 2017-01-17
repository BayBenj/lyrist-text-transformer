package intentions;

import main.Meter;
import misc.CulturalIntention;
import misc.EmotionalIntention;
import rhyme.LineRhymeScheme;
import rhyme.RhymeScheme;
import rhyme.RhymeSchemeManager;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class IntentionManager {

    public static SongIntentions getRandomSongIntentions(int lines) {
        return new SongIntentions(getRandomStructuralIntentions(lines), getRandomEmotionalIntention(), getRandomCulturalIntention());
    }

    public static StructuralIntentions getRandomStructuralIntentions(int lines) {
        StructuralIntentions structuralIntentions = new StructuralIntentions();
        structuralIntentions.setRhymeScheme(getRandomRhymeScheme(lines));
        return structuralIntentions;
    }

    public static RhymeScheme getRandomRhymeScheme(int lines) {
//        int rnd = Utils.rand.nextInt(4);
        int rnd = 0;
        RhymeScheme rhymeScheme = null;
        switch (rnd) {
            case 0:
                RhymeSchemeManager.getAlternatingScheme(lines, Utils.rand.nextInt(lines - 1) + 2, 1);
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
        list.add(new EmotionalIntention());
        return list;
    }

    public static List<CulturalIntention> getRandomCulturalIntention() {
        List<CulturalIntention> list = new ArrayList<>();
        list.add(new CulturalIntention());
        return list;
    }

}








































































































































