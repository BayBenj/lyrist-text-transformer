package songtools;

import utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LyristOperation extends TreeMap<Integer,Pair<List<Integer>, Operation>> {//operationIndex, <args, operation>

    //NORMAL REPLACEMENT
    // 0     -1    mark words
    // 1     0     loop the following for each word in 0
    // 2     1     getW2vSuggestions
    // 3     2     string filters
    // 4     3     strings to words
    // 5     4     word filters
    // 6     5     priority word filters
    // 7     0, 6     replace each old word with each new word from 5

    //RHYME REPLACEMENT
    // 0     -1    word marking filters
    // 1     0     loop the following for each word in 0
    // 2     1     getW2vSuggestions
    // 3     2     string filters
    // 4     3     add cmu to strings
    // 5     -2    mark words by rhyme scheme
    // 6     input    pick model rhyme words
    // 7     input    rhyme word filters
    // 8     prev     priority word filters

    public Object run(List<Object> externalInput) {
        //TODO test this
        for (Map.Entry<Integer,Pair<List<Integer>, Operation>> operationEntry : this.entrySet()) {
            List<Object> inputs = new ArrayList<>();
            for (int argIndex : operationEntry.getValue().getFirst()) {
                if (argIndex == -1)
                    inputs.addAll(externalInput);
                else
                    inputs.add(this.get(argIndex).getSecond().getOutput());
            }
            Object output = operationEntry.getValue().getSecond().run(inputs);
            operationEntry.getValue().getSecond().setOutput(output);
        }
        return this.lastEntry().getValue().getSecond().getOutput();
    }

}


















































