package songtools;

import elements.Song;

import java.io.Serializable;

public class InfoSong extends Song implements Serializable {

    private String title;
    private String writer;
    private String programmer;
    private String genre;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getProgrammer() {
        return programmer;
    }

    public void setProgrammer(String programmer) {
        this.programmer = programmer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
