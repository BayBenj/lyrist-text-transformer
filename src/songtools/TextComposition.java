package songtools;

import elements.Line;
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
        str.append(infoSong.title());
        str.append('\n');
        str.append("WRITER: ");
        str.append(infoSong.writer());
        str.append("\n\n");
        return str.toString();
    }

    public String printHeadersSideBySide(InfoSong infoSong1, InfoSong infoSong2, int longestLineLength) {
        StringBuilder str = new StringBuilder();

        str.append("TITLE: " + infoSong1.title());
        appendExtraSpace(str, (longestLineLength + 5) - 7 - infoSong1.title().length());
        str.append("TITLE: " + infoSong2.title());
        str.append('\n');

        str.append("WRITER: " + infoSong1.writer());
        appendExtraSpace(str, (longestLineLength + 5) - 8 - infoSong1.writer().length());
        str.append("WRITER: " + infoSong2.writer());
        str.append('\n');

        str.append("ORIGINAL THEME: " + infoSong2.getOldTheme());
        appendExtraSpace(str, (longestLineLength + 5) - 16 - infoSong2.getOldTheme().length());
        str.append("INSPIRATION: " + infoSong2.getNewTheme());

//        str.append(infoSong2.getOldTheme() + " -> " + infoSong2.getNewTheme());
//        appendExtraSpace(str, (longestLineLength + 5) - infoSong2.getOldTheme().length());
//        str.append(infoSong2.getNewTheme());

        str.append("\n\n");
        return str.toString();
    }

    public String printSongsSideBySide(InfoSong song1, InfoSong song2, boolean printHeaders) {
        StringBuilder str = new StringBuilder();

        List<Line> allLines1 = song1.lines();
        int longest_line_length = -1;
        for (Line line : allLines1)
            if (line.toString().length() > longest_line_length)
                longest_line_length = line.toString().length();

        if (printHeaders)
            str.append(printHeadersSideBySide(song1,song2, longest_line_length));

        for (int i = 0; i < song1.size(); i++) {
            Stanza stanza1 = song1.get(i);
            Stanza stanza2 = song2.get(i);

            if (printHeaders && stanza1.getType() != null && stanza2.getType() != null) {
                int extra_space = (longest_line_length + 5) - stanza1.getType().toString().length();
                str.append(stanza1.getType().toString());
                appendExtraSpace(str,extra_space);
                str.append(stanza2.getType().toString());
                str.append("\n");
            }

            for (int j = 0; j < stanza1.size(); j++) {
                Line line1 = stanza1.get(j);
                Line line2 = stanza2.get(j);
                int extra_space = (longest_line_length + 5) - line1.toString().length();
//                str.append("\t");
                str.append(line1.toString());
                appendExtraSpace(str,extra_space);
                str.append(line2.toString());
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























