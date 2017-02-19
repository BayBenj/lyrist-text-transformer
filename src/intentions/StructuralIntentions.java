package intentions;

import rhyme.LineRhymeScheme;
import rhyme.Meter;
import rhyme.RhymeScheme;

public class StructuralIntentions {

    //If user wants to generate elements by replacement on template, the template should be loaded before these structural intentions are decided.

    private LineRhymeScheme rhymeScheme;
    private Meter meter;

    public boolean hasNothing() {
        if (rhymeScheme == null && meter == null)
            return true;
        return false;
    }

    public LineRhymeScheme getRhymeScheme() {
        return rhymeScheme;
    }

    public void setRhymeScheme(LineRhymeScheme rhymeScheme) {
        this.rhymeScheme = rhymeScheme;
    }

    public Meter getMeter() {
        return meter;
    }

    public void setMeter(Meter meter) {
        this.meter = meter;
    }
}




















































































