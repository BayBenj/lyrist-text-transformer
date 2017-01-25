package intentions;

import java.util.List;

public class SongIntentions {

    private StructuralIntentions structuralIntentions;
    private List<EmotionalIntention> emotionalIntentions;
    private List<CulturalIntention> culturalIntentions;

    public SongIntentions(StructuralIntentions structuralIntentions,
                          List<EmotionalIntention> emotionalIntentions,
                          List<CulturalIntention> culturalIntentions) {
        this.structuralIntentions = structuralIntentions;
        this.emotionalIntentions = emotionalIntentions;
        this.culturalIntentions = culturalIntentions;
    }

    public StructuralIntentions getStructuralIntentions() {
        return structuralIntentions;
    }

    public void setStructuralIntentions(StructuralIntentions structuralIntentions) {
        this.structuralIntentions = structuralIntentions;
    }

    public List<EmotionalIntention> getEmotionalIntentions() {
        return emotionalIntentions;
    }

    public void setEmotionalIntentions(List<EmotionalIntention> emotionalIntentions) {
        this.emotionalIntentions = emotionalIntentions;
    }

    public List<CulturalIntention> getCulturalIntentions() {
        return culturalIntentions;
    }

    public void setCulturalIntentions(List<CulturalIntention> culturalIntentions) {
        this.culturalIntentions = culturalIntentions;
    }
}












































































































