package song;

import java.util.List;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public enum SongSource {
    UNSET, RANDOM, USER, ENVIRONMENT, TEMPLATE;

    private static final List<SongSource> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RAND = new Random();

    public static SongSource randomSongSource() {
        return VALUES.get(RAND.nextInt(SIZE));
    }

}

















































