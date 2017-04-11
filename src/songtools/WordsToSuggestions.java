package songtools;

import elements.Word;

import java.util.*;

public class WordsToSuggestions extends HashMap<Word,Set<Word>> {

    public void putWord(Word oldWord, Collection<Word> newWords) {
        if (this.containsKey(oldWord)) {
            this.get(oldWord).addAll(newWords);
        }
        else {
            this.put(oldWord, new TreeSet<>(newWords));
        }
    }

}



















































