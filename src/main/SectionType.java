package main;

public enum SectionType {
    //TODO get rid of non-lyric section types
    //TODO change name from "section". It's a bad name.
    //TODO add functionality for recognizing and using unique, unnamed section types
    INTRO, VERSE, PRECHORUS, CHORUS, BRIDGE, OUTRO, INTERLUDE;

    public static SectionType valueOf(Character key) {
        switch(key){
            case 'I':
                return INTRO;
            case 'V':
                return VERSE;
            case 'P':
                return PRECHORUS;
            case 'C':
                return CHORUS;
            case 'B':
                return BRIDGE;
            case 'O':
                return OUTRO;
            case 'N':
                return INTERLUDE;
            default:
                return null;
        }

    }

    public boolean hasLyrics() {
        if(this == INTRO || this == OUTRO || this == INTERLUDE) {
            return false;
        }
        return true;
    }
}



























