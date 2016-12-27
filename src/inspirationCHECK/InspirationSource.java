package inspirationCHECK;

import java.util.List;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public enum InspirationSource {
    UNSET, RANDOM, USER, ENVIRONMENT;

    private static final List<InspirationSource> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RAND = new Random();

    public static InspirationSource randomInspirationSource() {
        return VALUES.get(RAND.nextInt(SIZE));
    }

}















































