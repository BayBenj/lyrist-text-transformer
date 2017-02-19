package songtools;

import elements.Line;
import elements.SongElement;
import elements.Stanza;
import intentions.CompleteIntentions;

import java.util.List;

public class TextComposition {

    private InfoSong original = null;
    private CompleteIntentions completeIntentions;
    private InfoSong modified = null;

    public TextComposition(InfoSong original, InfoSong modified, CompleteIntentions completeIntentions) {
        this.setOriginal(original);
        this.setModified(modified);
        this.setCompleteIntentions(completeIntentions);
    }

    public String toString() {
        return print(true, true, true, true);
    }

    public InfoSong getOriginal() {
        return original;
    }

    public void setOriginal(InfoSong original) {
        this.original = original;
    }

    public CompleteIntentions getCompleteIntentions() {
        return completeIntentions;
    }

    public void setCompleteIntentions(CompleteIntentions completeIntentions) {
        this.completeIntentions = completeIntentions;
    }

    public InfoSong getModified() {
        return modified;
    }

    public void setModified(InfoSong modified) {
        this.modified = modified;
    }

    public String print(boolean printOriginal, boolean printModified, boolean printHeaders, boolean sideBySide) {
        if (!(printOriginal || printModified))
            return "";

        if (printOriginal && printModified) {
            if (sideBySide)
                return printSongsSideBySide(original, modified, printHeaders);

            StringBuilder str = new StringBuilder();
            str.append(printSingleSong(original, printHeaders));
            str.append("\n");
            str.append(printSingleSong(modified, printHeaders));
            return str.toString();
        }

        else if (printOriginal)
            return printSingleSong(original, printHeaders);

        else if (printModified)
            return printSingleSong(modified, printHeaders);

        return null;
    }

    public String printSingleSong(InfoSong infoSong, boolean printHeader) {
        StringBuilder str = new StringBuilder();
        if (printHeader)
            str.append(printSingleHeader(infoSong));
        str.append(infoSong.toString());
        return str.toString();
    }

    public String printSingleHeader(InfoSong infoSong) {
        StringBuilder str = new StringBuilder();
        str.append("TITLE: ");
        str.append(infoSong.getTitle());
        str.append('\n');
        str.append("WRITER: ");
        str.append(infoSong.getWriter());
        str.append("\n\n");
        return str.toString();
    }

    public String printHeadersSideBySide(InfoSong infoSong1, InfoSong infoSong2, int longestLineLength) {
        StringBuilder str = new StringBuilder();

        str.append("TITLE:");
        appendExtraSpace(str, (longestLineLength + 5) - 6);
        str.append("TITLE:");

        str.append(infoSong1.getTitle());
        appendExtraSpace(str, (longestLineLength + 5) - infoSong1.getTitle().length());
        str.append(infoSong1.getTitle());

        str.append('\n');

        str.append("WRITER:");
        appendExtraSpace(str, (longestLineLength + 5) - 7);
        str.append("WRITER:");

        str.append(infoSong1.getWriter());
        appendExtraSpace(str, (longestLineLength + 5) - infoSong1.getWriter().length());
        str.append(infoSong1.getWriter());

        str.append("\n\n");
        return str.toString();
    }

    public String printSongsSideBySide(InfoSong s1, InfoSong s2, boolean printHeaders) {
        StringBuilder str = new StringBuilder();

        List<Stanza> s1Stanzas = s1.getStanzas();
        List<Stanza> s2Stanzas = s2.getStanzas();

        List<SongElement> allLines1 = s1.getAllSubElementsOfType(new Line());
        int longest_line_length = -1;
        for (SongElement line : allLines1)
            if (line.toString().length() > longest_line_length)
                longest_line_length = line.toString().length();

        if (printHeaders)
            str.append(printHeadersSideBySide(s1,s2, longest_line_length));

        for (int i = 0; i < s1Stanzas.size(); i++) {
            List<Line> s1Lines = s1Stanzas.get(i).getLines();
            List<Line> s2Lines = s2Stanzas.get(i).getLines();
            for (int j = 0; j < s1Lines.size(); j++) {
                int extra_space = (longest_line_length + 5) - s1Lines.get(j).toString().length();
                str.append(s1Lines.get(j).toString());
                appendExtraSpace(str,extra_space);
                str.append(s2Lines.get(j).toString());
                str.append("\n");
            }
            str.append("\n");
        }
        return str.toString();
    }

    private void appendExtraSpace(StringBuilder sb, int spaces) {
        for (int k = 0; k < spaces; k++)
            sb.append(" ");
    }

}







