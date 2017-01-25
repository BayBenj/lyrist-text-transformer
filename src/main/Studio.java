package main;
//
////import composition.Composition;
////import harmony.Harmony;
////import harmony.HarmonyEngineer;
//import compositionCHECK.Composition;
//import inspirationCHECK.Inspiration;
//import inspirationCHECK.InspirationEngineer;
////import lyrics.LyricalEngineer;
////import lyrics.Lyrics;
//import managerCHECK.Manager;
//import elements.Song;
//import songtools.SongEngineer;
////import structure.Structure;
////import structure.StructureEngineer;
//
//public class Studio {
//
//    public Composition generate() {
//
//        Composition newComposition = new Composition();
//
//        Manager manager = new Manager();
//
//        InspirationEngineer inspirationEngineer = manager.getInspirationEngineer();
//        Inspiration inspiration = inspirationEngineer.generateInspiration();
//        newComposition.setInspiration(inspiration);
//
//        SongEngineer songEngineer = manager.getSongEngineer();
//        Song elements = songEngineer.generateSong(inspiration);
//        newComposition.setSong(elements);
//
////        StructureEngineer structureEngineer = manager.getStructureEngineer();
////        Structure structure = structureEngineer.generateStructure();
////        newComposition.setStructure(structure);
////
////        LyricalEngineer lyircalEngineer = manager.getLyricalEngineer();
////        Lyrics lyrics = lyircalEngineer.generateLyrics(inspiration, structure);
////        newComposition.setLyrics(lyrics);
////
////        HarmonyEngineer harmonyEngineer = manager.getHarmonyEngineer();
////        Harmony harmony = harmonyEngineer.generateHarmony(inspiration, structure);
////        newComposition.setHarmony(harmony);
////
////		MelodyEngineer melodyEngineer = managerCHECK.getMelodyEngineer();
////		Melody melody = melodyEngineer.generateMelody(inspirationCHECK, structure, lyrics, harmony);
////		newComposition.setMelody(melody);
//
//        return newComposition;
//    }
//
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
