package word2vec;

import java.util.HashMap;
import java.util.Map;

public class VocabMap extends HashMap<String, Long> {
    //Long = number of occurrences

    private String title;

    public VocabMap(Map<String, Long> map) {
        super(map);
        this.setTitle("unnamed");
    }

    public VocabMap(Map<String, Long> map, String title) {
        super(map);
        this.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
