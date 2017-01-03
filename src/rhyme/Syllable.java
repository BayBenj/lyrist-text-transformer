package rhyme;

import rhyme.Phoneme;

import java.util.ArrayList;
import java.util.List;

public final class Syllable {

    private List<Phoneme> onset = new ArrayList<>();

    //rhyme
    private Phoneme nucleus;
    private int stress;
    private List<Phoneme> coda = new ArrayList<>();

    public Syllable() {}

    public Syllable(List<Phoneme> onset, Phoneme nucleus, int stress, List<Phoneme> coda) {
        this.onset = onset;
        this.nucleus = nucleus;
        this.stress = stress;
        this.coda = coda;
    }

    public List<Phoneme> getOnset() {
        return onset;
    }

    public void setOnset(List<Phoneme> onset) {
        this.onset = onset;
    }

    public Phoneme getNucleus() {
        return nucleus;
    }

    public void setNucleus(Phoneme nucleus) {
        this.nucleus = nucleus;
    }

    public List<Phoneme> getCoda() {
        return coda;
    }

    public void setCoda(List<Phoneme> coda) {
        this.coda = coda;
    }

    public int getStress() {
        return stress;
    }

    public void setStress(int stress) {
        this.stress = stress;
    }

    public boolean hasOnset() {
        if (this.onset != null && this.onset.size() > 0)
            return true;
        return false;
    }

    public boolean hasNucleus() {
        if (this.nucleus != null)
            return true;
        return false;
    }

    public boolean hasCoda() {
        if (this.coda != null && this.coda.size() > 0)
            return true;
        return false;
    }

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


































































