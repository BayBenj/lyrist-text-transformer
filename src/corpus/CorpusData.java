package corpus;

import java.util.HashMap;

public class CorpusData extends HashMap<String,Integer> {

    public double freq(String s) {
        return this.get(s) / this.size();
    }

    public int ocur(String s) {
        return this.get(s);
    }

}
