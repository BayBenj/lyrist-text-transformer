package song;

public class Punctuation extends Word {
    public Punctuation(String s) {
        super(s);
        this.setPos(Pos.PUNCTUATION);
        this.setNe(NamedEntity.O);
    }
}
