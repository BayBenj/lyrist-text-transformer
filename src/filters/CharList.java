package filters;

import java.util.Collection;
import java.util.HashSet;

public class CharList extends HashSet<Character> {

    private String title;

    public CharList(Collection<Character> collection, String title) {
        super(collection);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
