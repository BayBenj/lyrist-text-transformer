//package songtools;
//
//import elements.*;
//
//import java.util.*;
//
//public class SongWrapper {
//
//    //TODO: Right now the idea is that you make a elements completely, then make a SongWrapper w/ extra info from it. Its elements can never change.
//
//    private InfoSong infoSong;
//
////    private String rawSong;
////    private List<ArrayList<ArrayList<String>>> basic_lines;
////    private Set<Word> wordSet;
//
//    public SongWrapper() {
//
//    }
//
//    public SongWrapper(InfoSong infoSong) {
//        this.infoSong = infoSong;
//        this.wordSet = new HashSet<>();
//        this.basic_lines = new ArrayList<>();
//        this.setSongStats(this.infoSong);
//    }
//
//    public void setSongStats(Song infoSong) {
//        ArrayList<ArrayList<ArrayList<String>>> newSong = new ArrayList<ArrayList<ArrayList<String>>>();
//        for (int i = 0; i < infoSong.getSize(); i++) {
//            Stanza oldStanza = infoSong.getStanzas().get(i);
//            ArrayList<ArrayList<String>> newStanza = new ArrayList<ArrayList<String>>();
//            for (int j = 0; j < oldStanza.getSize(); j++) {
//                Line oldLine = oldStanza.getLines().get(j);
//                ArrayList<String> newLine = new ArrayList<String>();
//                for (int k = 0; k < oldLine.getSize(); k++) {
//                    Word oldWord = oldLine.getWords().get(k);
//                    newLine.add(oldWord.getLowerSpelling());
////                    this.wordSet.add(oldWord);
//                    //this.posMap.put();
//                }
//                newStanza.add(newLine);
//            }
//            newSong.add(newStanza);
//        }
////        this.basic_lines = newSong;
//    }
//
//    public InfoSong getInfoSong() {
//        return infoSong;
//    }
//
//    public void setInfoSong(InfoSong s) {
//        this.infoSong = s;
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
