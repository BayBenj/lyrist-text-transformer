package rhyme;

import elements.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class WordSuggestionsByRhymeModels extends HashMap<Word, List<Set<Word>>> {
    //HashMap<rhyme class's model word, Map<instance n of this rhyme class,Set<word suggestion for this instance>>>

//    public boolean contains(Word word) {
//        for (Set<Word> set : this.values())
//            if (set.contains(word))
//                return true;
//        return false;
//    }
//
//    public void putWord(Rhyme rhyme, Word word) {
//        if (this.containsKey(rhyme) && this.get(rhyme) != null && !this.get(rhyme).isEmpty()) {
//            Set<Word> filterWords = this.get(rhyme);
//            filterWords.add(word);
//        }
//        else
//            this.put(rhyme, new HashSet<>());
//    }
//
//    public Rhyme getRhymeByWord(Word w) {
//        for (Map.Entry<Rhyme,Set<Word>> entry : this.entrySet())
//            if (entry.getValue().contains(w))
//                return entry.getKey();
//        return null;
//    }

}






























































































