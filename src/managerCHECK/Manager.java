//package managerCHECK;
//
//import configCHECK.SongConfiguration;
//import configCHECK.SongConfigurationLoader;
//import inspirationCHECK.EnvironmentalInspirationEngineer;
//import inspirationCHECK.InspirationEngineer;
//import inspirationCHECK.RandomInspirationEngineer;
//import inspirationCHECK.UserInspirationEngineer;
//import song.TemplateSongEngineer;
//import mainCHECK.ProgramArgs;
//import song.SongEngineer;
//
//import static com.sun.imageio.plugins.jpeg.JPEG.TEM;
//import static mainCHECK.ProgramArgs.SongConfigSourceSetting.DISTRIBUTIONAL;
//import static mainCHECK.ProgramArgs.SongConfigSourceSetting.FROM_FILE;
//
//public class Manager {
//
//    private SongConfiguration config = null;
//
//    public Manager() {
//        switch (ProgramArgs.configurationSetting) {
//            case FROM_FILE:
//                config = SongConfigurationLoader.loadConfigurationFromFile();
//                break;
//            case RANDOM:
//                config = SongConfigurationLoader.loadRandomConfiguration();
//                break;
//            case FROM_COMMANDLINE:
//                config = SongConfigurationLoader.loadConfigurationFromCommandline();
//                break;
//            case SIMPLE:
//                config = SongConfigurationLoader.loadSimpleConfiguration();
//                break;
//            case TEST:
//                config = SongConfigurationLoader.loadTestConfiguration();
//                break;
//            case DISTRIBUTIONAL:
//                config = SongConfigurationLoader.loadDistributionalConfiguration();
//                break;
//            default:
//                throw new RuntimeException("Invalid configuration setting: ");// + config.globalStructureSource);
//        }
//    }
//
//    public InspirationEngineer getInspirationEngineer() {
//        InspirationEngineer engineer = null;
//
//        switch (config.inspirationSource) {
//            case RANDOM:
//                engineer = new RandomInspirationEngineer();
//                break;
//            case USER:
//                engineer = new UserInspirationEngineer();
//                break;
//            case ENVIRONMENT:
//                engineer = new EnvironmentalInspirationEngineer();
//                break;
//            default:
//                throw new RuntimeException("Invalid inspiration configuration: " + config.inspirationSource);
//        }
//
//        return engineer;
//    }
//
//    public SongEngineer getSongEngineer() {
//        SongEngineer engineer = null;
//
//        switch (config.songSource) {
//            case TEMPLATE:
//                engineer = new TemplateSongEngineer();
//                break;
//            case RANDOM:
//                //engineer = new RandomSongEngineer();
//                break;
//            case USER:
//                //engineer = new UserSongEngineer();
//                break;
//            case ENVIRONMENT:
//                //engineer = new EnvironmentalSongEngineer();
//                break;
//            default:
//                throw new RuntimeException("Invalid song configuration: " + config.inspirationSource);
//        }
//
//        return engineer;
//    }
//
//
////    public StructureEngineer getStructureEngineer() {
////        StructureEngineer engineer = new StructureEngineer();
////
////        GlobalStructureEngineer globalStructureEngineer = getGlobalStructureEngineer();
////        engineer.setGlobalStructureEngineer(globalStructureEngineer);
////
////        SubstructureEngineer substructureEngineer = getSubstructureEngineer();
////        engineer.setSubstructureEngineer(substructureEngineer);
////
////        return engineer;
////    }
//
////    public GlobalStructureEngineer getGlobalStructureEngineer() {
////        GlobalStructureEngineer engineer = null;
////
////        switch (configCHECK.globalStructureSource) {
////            case FIXED:
////                engineer = new FixedGlobalStructureEngineer();
////                break;
////            case DISTRIBUTION:
////                engineer = new DistributionalGlobalStructureEngineer();
////                break;
////            case MARKOV:
////                engineer = new MarkovGlobalStructureEngineer();
////                break;
////            case TEST:
////                engineer = new TestGlobalStructureEngineer();
////                break;
////            default:
////                throw new RuntimeException("Invalid structure configuration: " + configCHECK.globalStructureSource);
////        }
////
////        return engineer;
////    }
//
////    public SubstructureEngineer getSubstructureEngineer() {
////        SubstructureEngineer engineer = null;
////
////        switch (configCHECK.substructureSource) {
////            case FIXED:
////                engineer = new FixedSubstructureEngineer();
////                break;
////            case DISTRIBUTION:
////                engineer = new DistributionalSubstructureEngineer();
////                break;
////            case HIERARCHICAL:
////                engineer = new HierarchicalSubstructureEngineer();
////                break;
////            case TEST:
////                engineer = new TestSubstructureEngineer();
////                break;
////            default:
////                throw new RuntimeException("Invalid substructure configuration: " + configCHECK.substructureSource);
////        }
////
////        return engineer;
////    }
//
////    public LyricalEngineer getLyricalEngineer() {
////        LyricalEngineer engineer = null;
////
////        switch (configCHECK.lyricSource) {
////            case TEMPLATE:
////                engineer = new LyricTemplateEngineer();
////                break;
////            case LYRICAL_NGRAM:
////                engineer = new NGramLyricEngineer();
////                break;
////            case NON_LYRICAL_NGRAM:
////                engineer = new NGramNonLyricEngineer();
////                break;
////            case TEST:
////                engineer = new TestLyricEngineer();
////                break;
////            default:
////                throw new RuntimeException("Invalid lyric configuration: " + configCHECK.lyricSource);
////        }
////
////        return engineer;
////    }
//
////    public HarmonyEngineer getHarmonyEngineer() {
////        HarmonyEngineer engineer = null;
////
////        switch (configCHECK.harmonySource) {
////            case MONOCHORD:
////                engineer = new MonochordHarmonyEngineer();
////                break;
////            case PHRASE_DICT:
////                engineer = new PhraseDictHarmonyEngineer();
////                break;
////            case SEGMENTSPECIFIC_HMM:
////                engineer = new SegmentSpecificHarmonyEngineer();
////                break;
////            case TEST:
////                engineer = new TestHarmonyEngineer();
////                break;
////            default:
////                throw new RuntimeException("Invalid harmony configuration: " + configCHECK.harmonySource);
////        }
////
////        return engineer;
////    }
//
////    public MelodyEngineer getMelodyEngineer() {
////        MelodyEngineer engineer = new MelodyEngineer();
////
////        RhythmEngineer rhythmEngineer = getRhythmEngineer();
////        engineer.setRhythmEngineer(rhythmEngineer);
////
////        PitchEngineer pitchEngineer = getPitchEngineer();
////        engineer.setPitchEngineer(pitchEngineer);
////
////        return engineer;
////    }
//
////    public PitchEngineer getPitchEngineer() {
////        PitchEngineer pitchEngineer = null;
////
////        switch (configCHECK.pitchSource) {
////            case RANDOM:
////                pitchEngineer = new RandomPitchEngineer();
////                break;
////            case HMM:
////                pitchEngineer = new HMMPitchEngineer();
////                break;
////            case IDIOMS:
////                pitchEngineer = new IdiomaticPitchEngineer();
////                break;
////            case TEST:
////                pitchEngineer = new TestPitchEngineer();
////                break;
////            default:
////                throw new RuntimeException("Invalid harmony configuration: " + configCHECK.pitchSource);
////        }
////        return pitchEngineer;
////    }
//
////    public RhythmEngineer getRhythmEngineer() {
////        RhythmEngineer rhythmEngineer = null;
////
////        switch (configCHECK.rhythmSource) {
////            case RANDOM:
////                rhythmEngineer = new RandomRhythmEngineer();
////                break;
////            case PHRASE_DICT:
////                rhythmEngineer = new PhraseDictRhythmEngineer();
////                break;
////            case LYRICAL_STRESS_PATTERNS:
////                rhythmEngineer = new LyricalStressRhythmEngineer();
////                break;
////            case TEST:
////                rhythmEngineer = new TestRhythmEngineer();
////                break;
////            default:
////                throw new RuntimeException("Invalid harmony configuration: " + configCHECK.rhythmSource);
////        }
////        return rhythmEngineer;
////    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
