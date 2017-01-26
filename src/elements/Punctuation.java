package elements;

public class Punctuation extends Word {

    private boolean hidden = false;

    public Punctuation(String s) {
        super(s);
        this.setPos(Pos.PUNCTUATION);
        this.setNe(NamedEntity.O);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void hide() {
        this.setHidden(true);
    }

    public void unhide() {
        this.setHidden(false);
    }

    @Override
    public String toString() {
        if (hidden)
            return "";
        return super.getLowerSpelling();
    }
}
