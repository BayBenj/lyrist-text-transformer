package intentions;

public class CulturalIntention extends RecursiveIntention {

    private String cultureKeyword;

    public CulturalIntention(String cultureKeyword) {
        this.setCultureKeyword(cultureKeyword);
    }

    public static CulturalIntention combine(CulturalIntention... intentions) {
        return null;
    }

    public void combineWith(CulturalIntention... intentions) {

    }

    public String getCultureKeyword() {
        return cultureKeyword;
    }

    public void setCultureKeyword(String cultureKeyword) {
        this.cultureKeyword = cultureKeyword;
    }
}
/*

	Cultural intention—a corpus of elements to work within. No cultural intention means a broad, diverse corpus of elements to work within.
		Language
		Time (a year, or a range of years)
		Movement
		Group
		Individual: famous artist, musician, or poet


 */

/*
You should be able to combine cultural intentions.
 */

/*
CulturalIntentions may define other RecursiveIntentions (Structural, Emotional), and maybe eventually Operational intentions.
 */

/*
CulturalIntentions may be defined by other RecursiveIntentions (Structural, Emotional).
 */




































































































