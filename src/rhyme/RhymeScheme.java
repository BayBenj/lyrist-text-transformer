package rhyme;

import java.util.*;

public abstract class RhymeScheme extends HashMap<Rhyme,Set<Integer>> {

//    public void specify(RhymeClass rhymeClass, SpecificRhyme specificRhyme) {
//        for (int i = 0; i < this.size(); i++) {
//            if (this.get(i) instanceof RhymeClass && rhymeClass.equals(this.get(i))) {
//                this.remove(i);
//                this.add(i,specificRhyme);
//            }
//        }
//    }

    public RhymeScheme(Map<Rhyme,Set<Integer>> rhymes) {
        this.setRhymes(rhymes);
    }

    public RhymeScheme(String... rhymes) {
        List<String> strings = Arrays.asList(rhymes);
        Set<Integer> alreadyAddedIndexes = new HashSet<>();
        int lineNum = 0;
        for (String lineRhymeLetter : strings) {
            if (this.containsKey(new Rhyme(lineRhymeLetter.hashCode()))) {
                Set<Integer> indexes = this.get(new Rhyme(lineRhymeLetter.hashCode()));
                indexes.add(lineNum);
            }
            else {
                Set<Integer> indexes = new HashSet<>();
                indexes.add(lineNum);
                this.put(new Rhyme(lineRhymeLetter.hashCode()), indexes);
            }
            alreadyAddedIndexes.add(lineNum);
            lineNum++;
        }
    }

    public HashMap<Rhyme,Set<Integer>> getRhymes() {
        return this;
    }

    public void setRhymes(Map<Rhyme,Set<Integer>> rhymes) {
        for (Map.Entry<Rhyme,Set<Integer>> entry : rhymes.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public Rhyme getRhymeByIndex(int i) {
        for (Map.Entry<Rhyme,Set<Integer>> entry : this.entrySet())
            if (entry.getValue().contains(i))
                return entry.getKey();
        return null;
    }

    public boolean contains(int index) {
        for (Set<Integer> set : this.values())
            if (set.contains(index))
                return true;
        return false;
    }

    public void putIndex(Rhyme rhyme, int index) {
        if (this.containsKey(rhyme) && this.get(rhyme) != null && !this.get(rhyme).isEmpty()) {
            Set<Integer> indexes = this.get(rhyme);
            indexes.add(index);
        }
        else
            this.put(rhyme, new HashSet<>());
    }

}













































































