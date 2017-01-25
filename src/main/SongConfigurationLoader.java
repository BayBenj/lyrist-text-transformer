package main;

//import globalstructure.StructureSource;
//import harmony.HarmonySource;
import intentions.InspirationSource;
import songtools.SongSource;
//import lyrics.LyricalSource;
//import pitch.PitchSource;
//import rhythm.RhythmSource;
//import substructure.SubstructureSource;

//TODO: rename this or my Song class
public class SongConfigurationLoader {

    public static SongConfiguration loadConfigurationFromFile() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public static SongConfiguration loadRandomConfiguration() {
        SongConfiguration config = new SongConfiguration();

        config.inspirationSource = InspirationSource.randomInspirationSource();
        config.songSource = SongSource.randomSongSource();
//        configCHECK.globalStructureSource = StructureSource.randomStructureSource();
//        configCHECK.substructureSource = SubstructureSource.randomSubstructureSource();
//        configCHECK.lyricSource = LyricalSource.randomLyricalSource();
//        configCHECK.harmonySource = HarmonySource.randomHarmonySource();
//        configCHECK.rhythmSource = RhythmSource.randomRhythmSource();
//        configCHECK.pitchSource = PitchSource.randomPitchSource();

        return config;
    }

    public static SongConfiguration loadConfigurationFromCommandline() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public static SongConfiguration loadSimpleConfiguration() {
        SongConfiguration config = new SongConfiguration();

        config.inspirationSource = InspirationSource.RANDOM;
        config.songSource = SongSource.RANDOM;
//        configCHECK.globalStructureSource = StructureSource.FIXED;
//        configCHECK.substructureSource = SubstructureSource.FIXED;
//        configCHECK.lyricSource = LyricalSource.TEMPLATE;
//        configCHECK.harmonySource = HarmonySource.MONOCHORD;
//        configCHECK.rhythmSource = RhythmSource.RANDOM;
//        configCHECK.pitchSource = PitchSource.RANDOM;

        return config;
    }

    public static SongConfiguration loadTestConfiguration() {
        SongConfiguration config = new SongConfiguration();

        config.inspirationSource = InspirationSource.RANDOM;
        config.songSource = SongSource.RANDOM;
//        configCHECK.globalStructureSource = StructureSource.TEST;
//        configCHECK.substructureSource = SubstructureSource.TEST;
//        configCHECK.lyricSource = LyricalSource.TEST;
//        configCHECK.harmonySource = HarmonySource.TEST;
//        configCHECK.rhythmSource = RhythmSource.TEST;
//        configCHECK.pitchSource = PitchSource.TEST;

        return config;
    }

    public static SongConfiguration loadDistributionalConfiguration() {
        SongConfiguration config = new SongConfiguration();

        config.inspirationSource = InspirationSource.RANDOM; // TODO
        config.songSource = SongSource.RANDOM;
//        configCHECK.globalStructureSource = StructureSource.DISTRIBUTION;
//        configCHECK.substructureSource = SubstructureSource.DISTRIBUTION;
//        configCHECK.lyricSource = LyricalSource.LYRICAL_NGRAM;
//        configCHECK.harmonySource = HarmonySource.SEGMENTSPECIFIC_HMM;
//        configCHECK.rhythmSource = RhythmSource.TEST; // TODO
//        configCHECK.pitchSource = PitchSource.TEST; // TODO

        return config;
    }
}





















































































































