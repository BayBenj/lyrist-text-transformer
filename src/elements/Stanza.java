package elements;

import java.util.ArrayList;
import java.util.List;

public class Stanza extends SongElement {

    public void add(boolean b, ArrayList<String> strings) {
        //called by the initial readTemplate()  method in TemplateSongEngineer
        Line line = new Line();
        for (String s : strings) {
            Word w = new Word(s);
            line.add(w);
        }
        super.add(line);
    }

    @Override
    public Song getSuperElement() {
        if (super.getSuperElement() == null)
            return null;
        return (Song)super.getSuperElement();
        // TODO: Catch ClassCastException
    }

    @Override
    public void setSuperElement(SongElement song) {
        super.setSuperElement((Song)song);
        // TODO: Catch ClassCastException
    }

    @Override
    public List<SongElement> getSubElements() {
        if (super.getSubElements() == null)
            return null;
        List<SongElement> result = super.getSubElements();
        for (SongElement se : super.getSubElements()) {
            Line temp = (Line)se;
            // TODO: Catch ClassCastException
        }
        return result;
    }

    @Override
    public void setSubElements(List<SongElement> lines) {
        for (SongElement se : lines) {
            Line temp = (Line)se;
            // TODO: Catch ClassCastException
        }
        super.setSubElements(lines);
    }

    public Song getSong() {
        return this.getSuperElement();
    }

    public void setSong(Song song) {
        this.setSuperElement(song);
        // TODO: Catch ClassCastException
    }

    public List<Line> getLines() {
        if (super.getSubElements() == null)
            return null;
        List<Line> lines = new ArrayList<Line>();
        List<SongElement> elements = super.getSubElements();
        for (SongElement se : elements) {
            lines.add((Line)se);
            // TODO: Catch ClassCastException
        }
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        ArrayList<SongElement> result = new ArrayList<SongElement>();
        for (Line l : lines)
            result.add(l);
        this.setSubElements(result);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        List<Line> lines = this.getLines();
        for (int i = 0; i < lines.size(); i++) {
            result.append(lines.get(i).toString());
            if (i != lines.size() - 1)
                result.append("\n");
        }
        return result.toString();
    }



}








































































































