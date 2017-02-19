package rhyme;

import elements.Word;
import utils.Pair;
import java.util.*;

public class WordSuggestionsByRhyme extends HashMap<Rhyme, List<Pair<Word,Set<Word>>>> {
    //HashMap<rhyme class, List<Pair<oldWord instance,Set<word suggestion for this instance>>>>

    public void putWord(Word oldWord, Collection<Word> newWords, Rhyme rhyme) {
        List<Pair<Word,Set<Word>>> rhymeClass = this.get(rhyme);
        for (Pair<Word,Set<Word>> rhymeInstance : rhymeClass)
            if (oldWord.equals(rhymeInstance.getFirst()))
                rhymeInstance.getSecond().addAll(newWords);
    }

    public boolean containsOld(Word oldWord) {
        for (Map.Entry<Rhyme, List<Pair<Word,Set<Word>>>> rhymeClass : this.entrySet())
            for (Pair<Word,Set<Word>> rhymeInstance : rhymeClass.getValue())
                if (rhymeInstance.getFirst().equals(oldWord))
                    return true;
        return false;
    }
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
    public Rhyme getRhymeByOldWord(Word oldWord) {
        for (Map.Entry<Rhyme, List<Pair<Word,Set<Word>>>> rhymeClass : this.entrySet())
            for (Pair<Word,Set<Word>> rhymeInstance : rhymeClass.getValue())
                if (oldWord.equals(rhymeInstance.getFirst()))
                return rhymeClass.getKey();
        return null;
    }

}
































































































