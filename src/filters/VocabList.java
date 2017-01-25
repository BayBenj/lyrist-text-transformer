package filters;

import java.util.Collection;
import java.util.HashSet;

public class VocabList extends HashSet<String> {

    private String title;

    public VocabList(Collection<String> collection) {
        super(collection);
        this.setTitle("unnamed");
    }

    public VocabList(Collection<String> collection, String title) {
        super(collection);
        this.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
