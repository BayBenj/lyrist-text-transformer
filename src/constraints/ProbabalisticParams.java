package constraints;

public class ProbabalisticParams {

    private double positiveCosineSimilarity;
    private double positiveRhymeScore;

    public ProbabalisticParams(double positiveCosineSimilarity, double positiveRhymeScore) {
        this.positiveCosineSimilarity = positiveCosineSimilarity;
        this.positiveRhymeScore = positiveRhymeScore;
    }

    public double getScore(double normalizedCosine, double normalizedRhyme) {
        if (normalizedCosine == Double.MIN_VALUE)
            positiveCosineSimilarity = 0;
        if (normalizedRhyme == Double.MIN_VALUE)
            positiveRhymeScore = 0;
        return (positiveCosineSimilarity * normalizedCosine) + (Math.pow(normalizedRhyme, 100) * positiveRhymeScore);
    }

}
