package main;

import misc.Phoneme;

import java.util.ArrayList;

public final class Syllable {

    private ArrayList<Phoneme> onset;

    //rhyme
    private ArrayList<Phoneme> nucleus;
    private ArrayList<Phoneme> coda;

    @Override
    public String toString() {
        return onset.toString() + nucleus.toString() + coda.toString();
    }

    public String toString(Object instanceOfDesiredResultFormat) {
        return onset.toString() + nucleus.toString() + coda.toString();
    }


}
// extends ArrayList<Phoneme>?
// holds ArrayList<Phoneme>?






































































