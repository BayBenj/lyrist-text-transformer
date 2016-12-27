package song;

public abstract class SequencePair {

    protected static double MATCH_SCORE = 1;
    protected static double MISMATCH_SCORE = -1;
    protected static double GAP_OPEN_SCORE = -2;
    protected static double GAP_EXTEND_SCORE = 0;

    public abstract class AlignmentBuilder {

        double[] scores;
        int scoreCount = 0;

        public static final char INDEL_CHAR = '&';

        public AlignmentBuilder() {
            scores = new double[seq1length() + seq2length()];
        }

        public void appendScore(double score) {
            scores[scoreCount++] = score;
        }

        public abstract void appendCharSequence1(int i);

        public abstract void appendCharSequence2(int j);

        public abstract void appendIndelSequence1();

        public abstract void appendIndelSequence2();

        public void reverse() {
            double[] newScores = new double[scoreCount];
            for (int i = 0; i < scoreCount; i++) {
                newScores[i] = scores[scoreCount - i - 1];
            }
            scores = newScores;
        }

        public abstract Alignment renderAlignment();

    }

    public abstract AlignmentBuilder newAlignmentBuilder();

    public abstract double matchScore(int row_1, int i);

    public double leftGapCost(boolean forceExtend, int i) {
        return (forceExtend? GAP_EXTEND_SCORE : GAP_OPEN_SCORE);
    }

    public int nonGapCharCount(int row_1) {
        return 1;
    }

    public abstract int seq1length();

    public abstract int seq2length();

    /**
     *
     * @param match
     * @param mismatch
     * @param gap_open
     * @param gap_extend
     */
    public static void setCosts(double match, double mismatch, double gap_open, double gap_extend) {
        MATCH_SCORE = match;
        MISMATCH_SCORE = mismatch;
        GAP_OPEN_SCORE = gap_open;
        GAP_EXTEND_SCORE = gap_extend;
    }

}
