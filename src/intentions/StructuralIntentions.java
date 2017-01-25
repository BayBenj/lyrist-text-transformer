package intentions;

import rhyme.Meter;
import rhyme.RhymeScheme;

public class StructuralIntentions {

    //If user wants to generate elements by replacement on template, the template should be loaded before these structural intentions are decided.

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





















































































