package rhyme;

import elements.Word;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordsByRhyme extends HashMap<Rhyme, Set<Word>> {

    public boolean contains(Word word) {
        for (Set<Word> set : this.values())
            if (set.contains(word))
                return true;
        return false;
    }

    public void putWord(Rhyme rhyme, Word word) {
        if (this.containsKey(rhyme) && this.get(rhyme) != null && !this.get(rhyme).isEmpty()) {
            Set<Word> words = this.get(rhyme);
            words.add(word);
        }
        else {
            Set temp = new HashSet();
            temp.add(word);
            this.put(rhyme, temp);
        }
    }

    public Rhyme getRhymeByWord(Word w) {
        for (Map.Entry<Rhyme,Set<Word>> entry : this.entrySet())
            if (entry.getValue().contains(w))
                return entry.getKey();
        return null;
    }

    public Set<Word> getAll() {
        Set<Word> words = new HashSet<>();
        for (Map.Entry<Rhyme, Set<Word>> entry : this.entrySet())
            words.addAll(entry.getValue());
        return words;
    }


}




















































































