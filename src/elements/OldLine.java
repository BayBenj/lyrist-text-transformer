//package elements;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OldLine extends SongElement implements Serializable {
//
//    private int minSyls = Integer.MIN_VALUE;
//    private int maxSyls = Integer.MAX_VALUE;
//
//    public int getMinSyls() {
//        return minSyls;
//    }
//
//    public void setMinSyls(int minSyls) {
//        this.minSyls = minSyls;
//    }
//
//    public int getMaxSyls() {
//        return maxSyls;
//    }
//
//    public void setMaxSyls(int maxSyls) {
//        this.maxSyls = maxSyls;
//    }
//
//    @Override
//    public OldStanza getSuperElement() {
//        if (super.getSuperElement() == null)
//            return null;
//        return (OldStanza)super.getSuperElement();
//        // TODO: Catch ClassCastException
//    }
//
//    @Override
//    public void setSuperElement(SongElement stanza) {
//        super.setSuperElement((OldStanza)stanza);
//        // TODO: Catch ClassCastException
//    }
//
//    @Override
//    public List<SongElement> getSubElements() {
//        if (super.getSubElements() == null)
//            return null;
//        List<SongElement> result = super.getSubElements();
//        if (result == null)
//            return null;
//        for (SongElement se : result) {
//            Word temp = (Word) se;
//            // TODO: Catch ClassCastException
//        }
//        return result;
//    }
//
//    @Override
//    public void setSubElements(List<SongElement> words) {
//        for (SongElement se : words) {
//            Word temp = (Word)se;
//            // TODO: Catch ClassCastException
//        }
//        super.setSubElements(words);
//    }
//
//    public OldStanza getOldStanza() {
//        return this.getSuperElement();
//    }
//
//    public void setOldStanza(OldStanza stanza) {
//        this.setSuperElement(stanza);
//        // TODO: Catch ClassCastException
//    }
//
//    public List<Word> getWords() {
//        if (super.getSubElements() == null)
//            return null;
//        List<Word> words = new ArrayList<>();
//        List<SongElement> elements = super.getSubElements();
//        for (SongElement se : elements) {
//            words.add((Word)se);
//            // TODO: Catch ClassCastException
//        }
//        return words;
//    }
//
//    public void setWords(List<Word> words) {
//        List<SongElement> result = new ArrayList<>();
//        for (Word w : words)
//            result.add(w);
//        this.setSubElements(result);
//    }
//
//    @Override
//    public List<Word> getAllWords() {
//        if (super.getSubElements() == null)
//            return null;
//        return this.getWords();
//        //TODO: be sure this is okay
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder result = new StringBuilder();
//        List<Word> words = this.getWords();
//        if (words == null)
//            return "";
//        for (int i = 0; i < words.size(); i++) {
//            result.append(words.get(i).toString());
//            if (i != words.size() - 1)
//                result.append(" ");
//        }
//        return result.toString();
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
