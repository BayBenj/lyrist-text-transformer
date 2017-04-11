package elements;

import rhyme.Rhyme;

import java.io.Serializable;
import java.util.ArrayList;

public class Line extends ArrayList<Word> implements Serializable {

    private int minSyls = Integer.MIN_VALUE;
    private int maxSyls = Integer.MAX_VALUE;
    private Stanza stanza;
    private Rhyme rhyme;

    public Line(Stanza stanza) {
        super();
        this.stanza = stanza;
    }

    public int getMinSyls() {
        return minSyls;
    }

    public void setMinSyls(int minSyls) {
        this.minSyls = minSyls;
    }

    public int getMaxSyls() {
        return maxSyls;
    }

    public void setMaxSyls(int maxSyls) {
        this.maxSyls = maxSyls;
    }

    public Stanza getStanza() {
        return stanza;
    }

    public void setStanza(Stanza stanza) {
        this.stanza = stanza;
    }

    public Rhyme getRhyme() {
        return rhyme;
    }

    public void setRhyme(Rhyme rhyme) {
        this.rhyme = rhyme;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            result.append(this.get(i).toString());
            if (i != this.size() - 1)
                result.append(" ");
        }
        return result.toString();
    }

}
