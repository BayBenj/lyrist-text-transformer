package rhyme;

public class StressedPhoneme {

    public Phoneme phoneme;
    public int stress;

    public StressedPhoneme(Phoneme phoneme, int stress) {
        this.phoneme = phoneme;
        this.stress = stress;
    }

    public boolean isVowel() {
        return phoneme.isVowel();
    }

    public boolean isVoiced() {
        return phoneme.isVoiced();
    }

    public MannerOfArticulation getManner() {
        return phoneme.getManner();
    }

    public PlaceOfArticulation getPlace() {
        return phoneme.getPlace();
    }

    @Override
    public String toString() {
        return "" + phoneme.toString() + ":" + stress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StressedPhoneme that = (StressedPhoneme) o;

        if (stress != that.stress) return false;
        return phoneme == that.phoneme;
    }

    @Override
    public int hashCode() {
        int result = phoneme.hashCode();
        result = 31 * result + stress;
        return result;
    }
}














































































