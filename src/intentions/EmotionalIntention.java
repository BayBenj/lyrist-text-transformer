package intentions;

public class EmotionalIntention extends RecursiveIntention {

    private String emotionKeyword;

    public EmotionalIntention(String emotionKeyword) {
        this.setEmotionKeyword(emotionKeyword);
    }

    public static EmotionalIntention combine(EmotionalIntention... intentions) {
        return null;
    }

    public String getEmotionKeyword() {
        return emotionKeyword;
    }

    public void setEmotionKeyword(String emotionKeyword) {
        this.emotionKeyword = emotionKeyword;
    }



}
/*

	Emotional intention—a sentiment to work within. No emotional intention means no intentional directing of emotional sentiments (any sentiment must be distant from any sort of emotion?).
		Unifying emotion (a sentiment)
		Emotional flow (a sequence of sentiments)


 */

/*
You should be able to combine emotional intentions
 */

/*
EmotionalIntentions may define Cultural, Structural, and maybe eventually Operational intentions.
 */







































































































