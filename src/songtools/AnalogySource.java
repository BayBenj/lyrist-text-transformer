package songtools;

public class AnalogySource extends Source {
    private String oldTheme;
    private String newTheme;
    private String oldWord;

    public AnalogySource(String oldTheme, String newTheme, String oldWord) {
        this.oldTheme = oldTheme;
        this.newTheme = newTheme;
        this.oldWord = oldWord;
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

    public String getOldWord() {
        return oldWord;
    }

    public void setOldWord(String oldWord) {
        this.oldWord = oldWord;
    }
}
