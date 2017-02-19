package rhyme;

import intentions.Intention;

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

    public RhymeScheme(int... rhymeIds) {
        intIdsToRhymes(rhymeIds);
    }

    public RhymeScheme(String... rhymeIds) {
        int[] intIds = stringIdsToIntIds(rhymeIds);
        intIdsToRhymes(intIds);
    }

    private int[] stringIdsToIntIds(String[] stringIds) {
        int[] rhymeIntIds = new int[stringIds.length];
        for (int i = 0; i < stringIds.length; i++) {
            String lineRhymeLetter = stringIds[i];
            rhymeIntIds[i] = lineRhymeLetter.hashCode();
        }
        return rhymeIntIds;
    }

    private void intIdsToRhymes(int[] intIds) {
        Set<Integer> alreadyAddedIndexes = new HashSet<>();
        int lineNum = 0;
        for (int lineRhymeId : intIds) {
            if (this.containsKey(new Rhyme(lineRhymeId))) {
                Set<Integer> indexes = this.get(new Rhyme(lineRhymeId));
                indexes.add(lineNum);
            }
            else {
                Set<Integer> indexes = new HashSet<>();
                indexes.add(lineNum);
                this.put(new Rhyme(lineRhymeId), indexes);
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

    public Set<Integer> getAllIndexes() {
        Set<Integer> result = new HashSet<>();
        for (Map.Entry<Rhyme,Set<Integer>> entry : this.entrySet())
            result.addAll(entry.getValue());
        return result;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : this.getAllIndexes())
            for (Map.Entry<Rhyme,Set<Integer>> entry : this.entrySet())
                if (entry.getValue().contains(i)) {
                    sb.append(entry.getKey().getRhymeId());
                    sb.append("-");
                }
        return sb.toString();
    }
}















































































