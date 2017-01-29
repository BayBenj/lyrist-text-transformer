//package filters;
//
//import elements.Word;
//
//public abstract class ModelScoreFilter extends WordFilter {
//
//    private double score;
//
//    public ModelScoreFilter(double score) {
//        this.setModel(score);
//    }
//
//    public ModelScoreFilter(ReturnType returnType, double score) {
//        super(returnType);
//        this.score = score;
//    }
//
//    protected double getModel() {
//        return score;
//    }
//
//    private void setModel(double score) {
//        this.score = score;
//    }
//}
