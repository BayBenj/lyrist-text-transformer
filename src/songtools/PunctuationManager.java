package songtools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class PunctuationManager {

    private Character[] terminals = {
            '.',
            ';',
            '!',
            '?',
    };
    public final Set<Character> terminalSet = new HashSet<Character>(Arrays.asList(terminals));
}
