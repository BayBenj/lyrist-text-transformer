package elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Song extends ArrayList<Stanza> implements Serializable {

    public List<Word> words() {
        List<Word> result = new ArrayList<>();
        for (Stanza stanza : this)
            result.addAll(stanza.words());
        return result;
    }

    public List<Line> lines() {
        List<Line> result = new ArrayList<>();
        for (Stanza stanza : this)
            result.addAll(stanza);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            result.append(this.get(i).toString());
            if (i != this.size() - 1)
                result.append("\n\n");
        }
        return result.toString();
    }

}
