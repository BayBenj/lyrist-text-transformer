package song;

import java.util.ArrayList;

public class Line extends SongElement {

    @Override
    public Stanza getSuperElement() {
        if (super.getSuperElement() == null)
            return null;
        return (Stanza)super.getSuperElement();
        // TODO: Catch ClassCastException
    }

    @Override
    public void setSuperElement(SongElement stanza) {
        super.setSuperElement((Stanza)stanza);
        // TODO: Catch ClassCastException
    }

    @Override
    public ArrayList<SongElement> getSubElements() {
        if (super.getSubElements() == null)
            return null;
        ArrayList<SongElement> result = super.getSubElements();
        if (result == null)
            return null;
        for (SongElement se : result) {
            Word temp = (Word) se;
            // TODO: Catch ClassCastException
        }
        return result;
    }

    @Override
    public void setSubElements(ArrayList<SongElement> words) {
        for (SongElement se : words) {
            Word temp = (Word)se;
            // TODO: Catch ClassCastException
        }
        super.setSubElements(words);
    }

    public Stanza getStanza() {
        return this.getSuperElement();
    }

    public void setStanza(Stanza stanza) {
        this.setSuperElement(stanza);
        // TODO: Catch ClassCastException
    }

    public ArrayList<Word> getWords() {
        if (super.getSubElements() == null)
            return null;
        ArrayList<Word> words = new ArrayList<Word>();
        ArrayList<SongElement> elements = super.getSubElements();
        for (SongElement se : elements) {
            words.add((Word)se);
            // TODO: Catch ClassCastException
        }
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        ArrayList<SongElement> result = new ArrayList<SongElement>();
        for (Word w : words)
            result.add(w);
        this.setSubElements(result);
    }

    @Override
    public ArrayList<Word> getAllWords() {
        if (super.getSubElements() == null)
            return null;
        return this.getWords();
        //TODO: be sure this is okay
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        ArrayList<Word> words = this.getWords();
        for (int i = 0; i < words.size(); i++) {
            result.append(words.get(i).toString());
            if (i != words.size() - 1)
                result.append(" ");
        }
        return result.toString();
    }

}



































































