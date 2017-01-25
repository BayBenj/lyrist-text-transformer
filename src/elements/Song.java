package elements;

import java.util.ArrayList;
import java.util.List;

public class Song extends SongElement {

    @Override
    public List<SongElement> getSubElements() {
        if (super.getSubElements() == null)
            return null;
        List<SongElement> result = super.getSubElements();
        for (SongElement se : super.getSubElements()) {
            Stanza temp = (Stanza)se;
        }
        return result;
    }

    @Override
    public void setSubElements(List<SongElement> stanzas) {
        for (SongElement se : stanzas) {
            Stanza temp = (Stanza)se;
        }
        super.setSubElements(stanzas);
    }

    public List<Stanza> getStanzas() {
        if (super.getSubElements() == null)
            return null;
        List<Stanza> stanzas = new ArrayList<Stanza>();
        List<SongElement> elements = super.getSubElements();
        for (SongElement se : elements) {
            stanzas.add((Stanza) se);
        }
        return stanzas;
    }

    public void setStanzas(List<Stanza> stanzas) {
        List<SongElement> result = new ArrayList<SongElement>();
        for (Stanza s : stanzas)
            result.add(s);
        this.setSubElements(result);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        List<Stanza> stanzas = this.getStanzas();
        for (int i = 0; i < stanzas.size(); i++) {
            result.append(stanzas.get(i).toString());
            if (i != stanzas.size() - 1)
                result.append("\n\n");
        }
        return result.toString();
    }

}


































































































