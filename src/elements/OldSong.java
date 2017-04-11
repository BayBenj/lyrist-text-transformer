//package elements;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OldSong extends SongElement implements Serializable {
//
//    @Override
//    public List<SongElement> getSubElements() {
//        if (super.getSubElements() == null)
//            return null;
//        List<SongElement> result = super.getSubElements();
//        for (SongElement se : super.getSubElements()) {
//            OldStanza temp = (OldStanza)se;
//        }
//        return result;
//    }
//
//    @Override
//    public void setSubElements(List<SongElement> stanzas) {
//        for (SongElement se : stanzas) {
//            OldStanza temp = (OldStanza)se;
//        }
//        super.setSubElements(stanzas);
//    }
//
//    public List<OldStanza> getOldStanzas() {
//        if (super.getSubElements() == null)
//            return null;
//        List<OldStanza> stanzas = new ArrayList<OldStanza>();
//        List<SongElement> elements = super.getSubElements();
//        for (SongElement se : elements) {
//            stanzas.add((OldStanza) se);
//        }
//        return stanzas;
//    }
//
//    public void setOldStanzas(List<OldStanza> stanzas) {
//        List<SongElement> result = new ArrayList<SongElement>();
//        for (OldStanza s : stanzas)
//            result.add(s);
//        this.setSubElements(result);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder result = new StringBuilder();
//        List<OldStanza> stanzas = this.getOldStanzas();
//        for (int i = 0; i < stanzas.size(); i++) {
//            result.append(stanzas.get(i).toString());
//            if (i != stanzas.size() - 1)
//                result.append("\n\n");
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
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
