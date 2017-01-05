package rhyme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RhymeScheme extends ArrayList<Rhyme> {

    private List<Rhyme> rhymeTypes;

    public void specify(RhymeClass rhymeClass, SpecificRhyme specificRhyme) {
        for (int i = 0; i < rhymeTypes.size(); i++) {
            if (rhymeTypes.get(i) instanceof RhymeClass && rhymeClass.equals(rhymeTypes.get(i))) {
                rhymeTypes.remove(i);
                rhymeTypes.add(i,specificRhyme);
            }
        }
    }

    public RhymeScheme(List<Rhyme> rhymeTypes) {
        this.rhymeTypes = rhymeTypes;
    }

    public RhymeScheme(Rhyme... rhymeTypes) {
        this.rhymeTypes = Arrays.asList(rhymeTypes);
    }

    public RhymeScheme(String... rhymeTypes) {
        List<String> strings = Arrays.asList(rhymeTypes);
        for (String string : strings) {
            this.rhymeTypes.add(new RhymeClass(string));
        }
    }

    public List<Rhyme> getRhymeTypes() {
        return rhymeTypes;
    }

    public void setRhymeTypes(List<Rhyme> rhymeTypes) {
        this.rhymeTypes = rhymeTypes;
    }
}


















































































