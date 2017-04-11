package elements;

import globalstructure.SegmentType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Stanza extends ArrayList<Line> implements Serializable {

    private SegmentType type;
    private Song song;

    public Stanza(SegmentType type, Song song) {
        super();
        this.type = type;
        this.song = song;
    }

    public List<Word> words() {
        List<Word> result = new ArrayList<>();
        for (Line line : this)
            result.addAll(line);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            result.append(this.get(i).toString());
            if (i != this.size() - 1)
                result.append("\n");
        }
        return result.toString();
    }

    public String toStringHeadings() {
        StringBuilder result = new StringBuilder();
        if (this.type != null)
            result.append(this.type.toString());
        else
            result.append("NULL SEGMENT TYPE");
        result.append("\n");
        result.append(this.toString());
        return result.toString();
    }

    public SegmentType getType() {
        return type;
    }

    public void setType(SegmentType type) {
        this.type = type;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}










