package rhyme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class RhymeScheme extends ArrayList<Rhyme> {

    public void specify(RhymeClass rhymeClass, SpecificRhyme specificRhyme) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) instanceof RhymeClass && rhymeClass.equals(this.get(i))) {
                this.remove(i);
                this.add(i,specificRhyme);
            }
        }
    }

    public RhymeScheme(List<Rhyme> rhymes) {
        this.setRhymes(rhymes);
    }

//    public RhymeScheme(Rhyme... rhymes) {
//        this.rhymes = Arrays.asList(rhymes);
//    }

    public RhymeScheme(String... rhymes) {
        List<String> strings = Arrays.asList(rhymes);
        for (String string : strings)
            this.add(new RhymeClass(string));
    }

    public List<Rhyme> getRhymes() {
        return this;
    }

    public void setRhymes(List<Rhyme> rhymes) {
        for (Rhyme rhyme : rhymes)
            this.add(rhyme);
    }
}




















































































