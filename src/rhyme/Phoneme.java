package rhyme;

import twitter4j.Place;

public enum Phoneme {

    //Arpabet phonemes

    /* 15 Vowels
        Height - low, mid, high
        Frontness - front, central, back
        Tenseness - tense or lax
        Rounding - round or not
    */
    AA,	//			odd         AA D
    AE,	//			at			AE T
    AH,	//			hut			HH AH T
    AO,	//			ought		AO T
    AW,	//			cow			K AW
    AY,	//			hide		HH AY D
    EH,	//			Ed			EH D
    ER,	//			hurt		HH ER T
    EY,	//			ate			EY T
    IH,	//			it			IH T
    IY,	//			eat			IY T
    OW,	//			oat			OW T
    OY,	//			toy			T OY
    UH,	//			hood		HH UH D
    UW,	//			two			T UW

    /* 24 Consonants
        Manner of Articulation
        Place of Articulation
        Voicing
    */
    B,	//			be			B IY
    CH,	//			cheese		CH IY Z
    D,	//			dee			D IY
    DH,	//			thee		DH IY
    F,	//			fee			F IY
    G,	//			green		G R IY N
    HH,	//			he			HH IY
    JH,	//			gee			JH IY
    K,	//			key			K IY
    L,	//			lee			L IY
    M,	//			me			M IY
    N,	//			knee		N IY
    NG,	//			ping		P IH NG
    P,	//			pee			P IY
    R,	//			read		R IY D
    S,	//			sea			S IY
    SH,	//			she			SH IY
    T,	//			tea			T IY
    TH,	//			theta		TH EY T AH
    V,	//			vee			V IY
    W,	//			we			W IY
    Y,	//			yield		Y IY L D
    Z,	//			zee			Z IY
    ZH;	//		    seizure		S IY ZH ER

    public boolean isConsonant() {
        return !this.isVowel();
    }

    private boolean isConsonant(Phoneme p) {
        return !this.isVowel(p);
    }

    public boolean isVowel() {
        if (this.isVowel(this))
            return true;
        return false;
    }

    public static MannerOfArticulation getManner(Phoneme p) {
        return p.getManner();
    }

    public boolean isObstruent() {
        if (this.getManner() == MannerOfArticulation.STOP ||
                this.getManner() == MannerOfArticulation.FRICATIVE ||
                this.getManner() == MannerOfArticulation.AFFRICATE)
            return true;
        return false;
    }

    public static PlaceOfArticulation getPlace(Phoneme p) {
        return p.getPlace();
    }

    public MannerOfArticulation getManner() {
        if (this.isFricative(this))
            return MannerOfArticulation.FRICATIVE;

        else if (this.isStop(this))
            return MannerOfArticulation.STOP;

        else if (this.isNasal(this))
            return MannerOfArticulation.NASAL;

        else if (this.isAffricate(this))
            return MannerOfArticulation.AFFRICATE;

        else if (this.isLiquid(this))
            return MannerOfArticulation.LIQUID;

        else if (this.isSemivowel(this))
            return MannerOfArticulation.SEMIVOWEL;

        else if (this.isAspirate(this))
            return MannerOfArticulation.ASPIRATE;

        else return null;
    }

    public PlaceOfArticulation getPlace() {
        if (this.isAlveolar(this))
            return PlaceOfArticulation.ALVEOLAR;

        else if (this.isPalatal(this))
            return PlaceOfArticulation.PALATAL;

        else if (this.isBilabial(this))
            return PlaceOfArticulation.BILABIAL;

        else if (this.isVelar(this))
            return PlaceOfArticulation.VELAR;

        else if (this.isLabiodental(this))
            return PlaceOfArticulation.LABIODENTAL;

        else if (this.isInterdental(this))
            return PlaceOfArticulation.INTERDENTAL;

        else if (this.isGlottal(this))
            return PlaceOfArticulation.GLOTTAL;

        else return null;
    }

    private static boolean isAlveolar(Phoneme p) {
        if (    p == Phoneme.T ||
                p == Phoneme.D ||
                p == Phoneme.S ||
                p == Phoneme.Z ||
                p == Phoneme.L ||
                p == Phoneme.R ||
                p == Phoneme.N )
            return true;
        return false;
    }

    private static boolean isPalatal(Phoneme p) {
        if (    p == Phoneme.SH ||
                p == Phoneme.ZH ||
                p == Phoneme.CH ||
                p == Phoneme.JH ||
                p == Phoneme.Y )
            return true;
        return false;
    }

    private static boolean isBilabial(Phoneme p) {
        if (    p == Phoneme.B ||
                p == Phoneme.P ||
                p == Phoneme.W ||
                p == Phoneme.M )
            return true;
        return false;
    }

    private static boolean isVelar(Phoneme p) {
        if (    p == Phoneme.K ||
                p == Phoneme.G ||
                p == Phoneme.NG )
            return true;
        return false;
    }

    private static boolean isLabiodental(Phoneme p) {
        if (    p == Phoneme.F ||
                p == Phoneme.V )
            return true;
        return false;
    }

    private static boolean isInterdental(Phoneme p) {
        if (    p == Phoneme.TH ||
                p == Phoneme.DH )
            return true;
        return false;
    }

    private static boolean isGlottal(Phoneme p) {
        if (    p == Phoneme.HH )
            return true;
        return false;
    }

    public boolean isVoiced() {
        return isVoiced(this);
    }

    private static boolean isVoiced(Phoneme p) {
        if (    p == Phoneme.B ||
                p == Phoneme.V ||
                p == Phoneme.D ||
                p == Phoneme.M ||
                p == Phoneme.G ||
                p == Phoneme.N ||
                p == Phoneme.Z ||
                p == Phoneme.L ||
                p == Phoneme.R ||
                p == Phoneme.NG ||
                p == Phoneme.ZH ||
                p == Phoneme.DH ||
                p == Phoneme.JH )
            return true;
        return false;
    }

    /*
    Vowels have 4 features:
    Height - low, mid, high
    Frontness - front, central, back
    Tenseness - tense or lax
    Rounding - round or not
     */

    private static boolean isVowel(Phoneme p) {
        if (    p == Phoneme.AA ||
                p == Phoneme.AE ||
                p == Phoneme.AH ||
                p == Phoneme.AO ||
                p == Phoneme.AW ||
                p == Phoneme.AY ||
                p == Phoneme.EH ||
                p == Phoneme.ER ||
                p == Phoneme.EY ||
                p == Phoneme.IH ||
                p == Phoneme.IY ||
                p == Phoneme.OW ||
                p == Phoneme.OY ||
                p == Phoneme.UH ||
                p == Phoneme.UW
                )
            return true;
        return false;
    }

    private static boolean isFricative(Phoneme p) {
        if (    p == Phoneme.DH ||
                p == Phoneme.F ||
                p == Phoneme.S ||
                p == Phoneme.SH ||
                p == Phoneme.TH ||
                p == Phoneme.V ||
                p == Phoneme.Z ||
                p == Phoneme.ZH )
            return true;
        return false;
    }

    private static boolean isStop(Phoneme p) {
        if (    p == Phoneme.B ||
                p == Phoneme.D ||
                p == Phoneme.G ||
                p == Phoneme.K ||
                p == Phoneme.P ||
                p == Phoneme.T )
            return true;
        return false;
    }

    private static boolean isNasal(Phoneme p) {
        if (    p == Phoneme.M ||
                p == Phoneme.N ||
                p == Phoneme.NG )
            return true;
        return false;
    }

    private static boolean isAffricate(Phoneme p) {
        if (    p == Phoneme.CH ||
                p == Phoneme.JH )
            return true;
        return false;
    }

    private static boolean isLiquid(Phoneme p) {
        if (    p == Phoneme.L ||
                p == Phoneme.R )
            return true;
        return false;
    }

    private static boolean isSemivowel(Phoneme p) {
        if (    p == Phoneme.W ||
                p == Phoneme.Y )
            return true;
        return false;
    }

    private static boolean isAspirate(Phoneme p) {
        if (    p == Phoneme.HH )
            return true;
        return false;
    }

}











































































































