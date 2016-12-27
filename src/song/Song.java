package song;

import java.util.ArrayList;

public class Song extends SongElement {

    @Override
    public ArrayList<SongElement> getSubElements() {
        if (super.getSubElements() == null)
            return null;
        ArrayList<SongElement> result = super.getSubElements();
        for (SongElement se : super.getSubElements()) {
            Stanza temp = (Stanza)se;
        }
        return result;
    }

    @Override
    public void setSubElements(ArrayList<SongElement> stanzas) {
        for (SongElement se : stanzas) {
            Stanza temp = (Stanza)se;
        }
        super.setSubElements(stanzas);
    }

    public ArrayList<Stanza> getStanzas() {
        if (super.getSubElements() == null)
            return null;
        ArrayList<Stanza> stanzas = new ArrayList<Stanza>();
        ArrayList<SongElement> elements = super.getSubElements();
        for (SongElement se : elements) {
            stanzas.add((Stanza) se);
        }
        return stanzas;
    }

    public void setStanzas(ArrayList<Stanza> stanzas) {
        ArrayList<SongElement> result = new ArrayList<SongElement>();
        for (Stanza s : stanzas)
            result.add(s);
        this.setSubElements(result);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        ArrayList<Stanza> stanzas = this.getStanzas();
        for (int i = 0; i < stanzas.size(); i++) {
            result.append(stanzas.get(i).toString());
            if (i != stanzas.size() - 1)
                result.append("\n\n");
        }
        return result.toString();
    }

}


































































































