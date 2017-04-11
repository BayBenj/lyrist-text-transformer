//package elements;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OldStanza extends SongElement implements Serializable {
//
//    public void add(boolean b, ArrayList<String> strings) {
//        //called by the initial readTemplate()  method in TemplateOldSongEngineer
//        OldLine line = new OldLine();
//        for (String s : strings) {
//            Word w = new Word(s);
//            line.add(w);
//        }
//        super.add(line);
//    }
//
//    @Override
//    public OldSong getSuperElement() {
//        if (super.getSuperElement() == null)
//            return null;
//        return (OldSong)super.getSuperElement();
//        // TODO: Catch ClassCastException
//    }
//
//    @Override
//    public void setSuperElement(SongElement song) {
//        super.setSuperElement((OldSong)song);
//        // TODO: Catch ClassCastException
//    }
//
//    @Override
//    public List<SongElement> getSubElements() {
//        if (super.getSubElements() == null)
//            return null;
//        List<SongElement> result = super.getSubElements();
//        for (SongElement se : super.getSubElements()) {
//            OldLine temp = (OldLine)se;
//            // TODO: Catch ClassCastException
//        }
//        return result;
//    }
//
//    @Override
//    public void setSubElements(List<SongElement> lines) {
//        for (SongElement se : lines) {
//            OldLine temp = (OldLine)se;
//            // TODO: Catch ClassCastException
//        }
//        super.setSubElements(lines);
//    }
//
//    public OldSong getOldSong() {
//        return this.getSuperElement();
//    }
//
//    public void setOldSong(OldSong infoOldSong) {
//        this.setSuperElement(infoOldSong);
//        // TODO: Catch ClassCastException
//    }
//
//    public List<OldLine> getOldLines() {
//        if (super.getSubElements() == null)
//            return null;
//        List<OldLine> lines = new ArrayList<OldLine>();
//        List<SongElement> elements = super.getSubElements();
//        for (SongElement se : elements) {
//            lines.add((OldLine)se);
//            // TODO: Catch ClassCastException
//        }
//        return lines;
//    }
//
//    public void setOldLines(ArrayList<OldLine> lines) {
//        ArrayList<SongElement> result = new ArrayList<SongElement>();
//        for (OldLine l : lines)
//            result.add(l);
//        this.setSubElements(result);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder result = new StringBuilder();
//        List<OldLine> lines = this.getOldLines();
//        for (int i = 0; i < lines.size(); i++) {
//            result.append(lines.get(i).toString());
//            if (i != lines.size() - 1)
//                result.append("\n");
//        }
//        return result.toString();
//    }
//
//
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
