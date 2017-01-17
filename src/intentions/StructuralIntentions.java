package intentions;

import main.Meter;
import rhyme.RhymeScheme;

public class StructuralIntentions {

    private RhymeScheme rhymeScheme;
    private Meter meter;

    public boolean hasNothing() {
        if (rhymeScheme == null && meter == null)
            return true;
        return false;
    }

    public RhymeScheme getRhymeScheme() {
        return rhymeScheme;
    }

    public void setRhymeScheme(RhymeScheme rhymeScheme) {
        this.rhymeScheme = rhymeScheme;
    }

    public Meter getMeter() {
        return meter;
    }

    public void setMeter(Meter meter) {
        this.meter = meter;
    }
}
