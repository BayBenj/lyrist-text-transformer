package songtools;

import elements.Song;

import java.io.Serializable;

public class InfoSong extends Song implements Serializable {

    private String title;
    private String writer;
    private String programmer;
    private String genre;
    private String oldTheme;
    private String newTheme;

    public InfoSong(String title, String writer, String genre) {
        this.title = title;
        this.writer = writer;
        this.programmer = "n/a";
        this.genre = genre;
    }

    public InfoSong(String title) {
        this.title = title;
        this.writer = "Lyrist";
        this.programmer = "Ben Bay";
        this.genre = "postmodern computer pop";
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String writer() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String programmer() {
        return programmer;
    }

    public void setProgrammer(String programmer) {
        this.programmer = programmer;
    }

    public String genre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getOldTheme() {
        return oldTheme;
    }

    public void setOldTheme(String oldTheme) {
        this.oldTheme = oldTheme;
    }

    public String getNewTheme() {
        return newTheme;
    }

    public void setNewTheme(String newTheme) {
        this.newTheme = newTheme;
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

