package rhyme;

public class StressedPhone {

    public Phoneme phone;
    public int stress;

    public StressedPhone(Phoneme phone, int stress) {
        this.phone = phone;
        this.stress = stress;
    }

    public String toString() {
        return phone.toString() + ":" + stress;
    }
}
