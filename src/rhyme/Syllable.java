package rhyme;

import rhyme.Phoneme;

import java.util.ArrayList;
import java.util.List;

public final class Syllable {

    private Pronunciation onset = new Pronunciation();

    //rhyme
    private StressedPhoneme nucleus;
    private Pronunciation coda = new Pronunciation();

    public Syllable() {}

    public Syllable(Pronunciation onset, StressedPhoneme nucleus, Pronunciation coda) {
        this.onset = onset;
        this.nucleus = nucleus;
        this.coda = coda;
    }

    public List<Phoneme> getRhyme() {
        List<Phoneme> rhyme = new ArrayList<>();
        if (nucleus != null && nucleus.phoneme != null)
            rhyme.add(nucleus.phoneme);
        if (coda != null && !coda.isEmpty())
            for (StressedPhoneme stressedPhoneme : coda)
                rhyme.add(stressedPhoneme.phoneme);
        return rhyme;
    }

    public List<StressedPhoneme> getStressedRhyme() {
        Pronunciation rhyme = new Pronunciation();
        if (nucleus != null)
            rhyme.add(nucleus);
        if (coda != null && !coda.isEmpty())
            rhyme.addAll(coda);
        return rhyme;
    }

    public List<Phoneme> getPhonemes() {
        List<Phoneme> all = new ArrayList<>();
        if (onset != null && !onset.isEmpty())
            for (StressedPhoneme stressedPhoneme : onset)
                all.add(stressedPhoneme.phoneme);
        if (nucleus != null && nucleus.phoneme != null)
            all.add(nucleus.phoneme);
        if (coda != null && !coda.isEmpty())
            for (StressedPhoneme stressedPhoneme : coda)
                all.add(stressedPhoneme.phoneme);
        return all;
    }

    public List<StressedPhoneme> getStressedPhonemes() {
        Pronunciation all = new Pronunciation();
        if (onset != null && !onset.isEmpty())
            all.addAll(onset);
        if (nucleus != null)
            all.add(nucleus);
        if (coda != null && !coda.isEmpty())
            all.addAll(coda);
        return all;
    }

    public void setRhyme(StressedPhoneme nucleus, Pronunciation coda) {
        this.setNucleus(nucleus);
        this.setCoda(coda);
    }

    public void setRhyme(List<StressedPhoneme> rhyme) {
        this.setNucleus(rhyme.remove(0));
        this.setCoda((Pronunciation)rhyme);
    }

    public List<StressedPhoneme> getOnset() {
        return onset;
    }

    public void setOnset(Pronunciation onset) {
        this.onset = onset;
    }

    public StressedPhoneme getNucleus() {
        return nucleus;
    }

    public void setNucleus(StressedPhoneme nucleus) {
        this.nucleus = nucleus;
    }

    public List<StressedPhoneme> getCoda() {
        return coda;
    }

    public void setCoda(Pronunciation coda) {
        this.coda = coda;
    }

    public int getStress() {
        return nucleus.stress;
    }

    public void setStress(int stress) {
        nucleus.stress = stress;
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
        return "" +  onset.toString() + nucleus.toString() + coda.toString();
    }

    public String toString(Object instanceOfDesiredResultFormat) {
        return "" + onset.toString() + nucleus.toString() + coda.toString();
    }


}
// extends ArrayList<Phoneme>?
// holds ArrayList<Phoneme>?




























































