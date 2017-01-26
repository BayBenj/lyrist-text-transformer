package rhyme;

public enum PhonemeEnum {

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

    private boolean isConsonant(PhonemeEnum p) {
        return !this.isVowel(p);
    }

    public boolean isVowel() {
        if (this.isVowel(this))
            return true;
        return false;
    }

    public static MannerOfArticulation getManner(PhonemeEnum p) {
        return p.getManner();
    }

    public boolean isObstruent() {
        if (this.getManner() == MannerOfArticulation.STOP ||
                this.getManner() == MannerOfArticulation.FRICATIVE ||
                this.getManner() == MannerOfArticulation.AFFRICATE)
            return true;
        return false;
    }

    public static PlaceOfArticulation getPlace(PhonemeEnum p) {
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

    private static boolean isAlveolar(PhonemeEnum p) {
        if (    p == PhonemeEnum.T ||
                p == PhonemeEnum.D ||
                p == PhonemeEnum.S ||
                p == PhonemeEnum.Z ||
                p == PhonemeEnum.L ||
                p == PhonemeEnum.R ||
                p == PhonemeEnum.N )
            return true;
        return false;
    }

    private static boolean isPalatal(PhonemeEnum p) {
        if (    p == PhonemeEnum.SH ||
                p == PhonemeEnum.ZH ||
                p == PhonemeEnum.CH ||
                p == PhonemeEnum.JH ||
                p == PhonemeEnum.Y )
            return true;
        return false;
    }

    private static boolean isBilabial(PhonemeEnum p) {
        if (    p == PhonemeEnum.B ||
                p == PhonemeEnum.P ||
                p == PhonemeEnum.W ||
                p == PhonemeEnum.M )
            return true;
        return false;
    }

    private static boolean isVelar(PhonemeEnum p) {
        if (    p == PhonemeEnum.K ||
                p == PhonemeEnum.G ||
                p == PhonemeEnum.NG )
            return true;
        return false;
    }

    private static boolean isLabiodental(PhonemeEnum p) {
        if (    p == PhonemeEnum.F ||
                p == PhonemeEnum.V )
            return true;
        return false;
    }

    private static boolean isInterdental(PhonemeEnum p) {
        if (    p == PhonemeEnum.TH ||
                p == PhonemeEnum.DH )
            return true;
        return false;
    }

    private static boolean isGlottal(PhonemeEnum p) {
        if (    p == PhonemeEnum.HH )
            return true;
        return false;
    }

    public boolean isVoiced() {
        return isVoiced(this);
    }

    private static boolean isVoiced(PhonemeEnum p) {
        if (    p.isVowel() ||
                p == PhonemeEnum.B ||
                p == PhonemeEnum.V ||
                p == PhonemeEnum.D ||
                p == PhonemeEnum.M ||
                p == PhonemeEnum.G ||
                p == PhonemeEnum.N ||
                p == PhonemeEnum.Z ||
                p == PhonemeEnum.L ||
                p == PhonemeEnum.R ||
                p == PhonemeEnum.NG ||
                p == PhonemeEnum.ZH ||
                p == PhonemeEnum.DH ||
                p == PhonemeEnum.JH )
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

    private static boolean isVowel(PhonemeEnum p) {
        if (    p == PhonemeEnum.AA ||
                p == PhonemeEnum.AE ||
                p == PhonemeEnum.AH ||
                p == PhonemeEnum.AO ||
                p == PhonemeEnum.AW ||
                p == PhonemeEnum.AY ||
                p == PhonemeEnum.EH ||
                p == PhonemeEnum.ER ||
                p == PhonemeEnum.EY ||
                p == PhonemeEnum.IH ||
                p == PhonemeEnum.IY ||
                p == PhonemeEnum.OW ||
                p == PhonemeEnum.OY ||
                p == PhonemeEnum.UH ||
                p == PhonemeEnum.UW
                )
            return true;
        return false;
    }

    private static boolean isFricative(PhonemeEnum p) {
        if (    p == PhonemeEnum.DH ||
                p == PhonemeEnum.F ||
                p == PhonemeEnum.S ||
                p == PhonemeEnum.SH ||
                p == PhonemeEnum.TH ||
                p == PhonemeEnum.V ||
                p == PhonemeEnum.Z ||
                p == PhonemeEnum.ZH )
            return true;
        return false;
    }

    private static boolean isStop(PhonemeEnum p) {
        if (    p == PhonemeEnum.B ||
                p == PhonemeEnum.D ||
                p == PhonemeEnum.G ||
                p == PhonemeEnum.K ||
                p == PhonemeEnum.P ||
                p == PhonemeEnum.T )
            return true;
        return false;
    }

    private static boolean isNasal(PhonemeEnum p) {
        if (    p == PhonemeEnum.M ||
                p == PhonemeEnum.N ||
                p == PhonemeEnum.NG )
            return true;
        return false;
    }

    private static boolean isAffricate(PhonemeEnum p) {
        if (    p == PhonemeEnum.CH ||
                p == PhonemeEnum.JH )
            return true;
        return false;
    }

    private static boolean isLiquid(PhonemeEnum p) {
        if (    p == PhonemeEnum.L ||
                p == PhonemeEnum.R )
            return true;
        return false;
    }

    private static boolean isSemivowel(PhonemeEnum p) {
        if (    p == PhonemeEnum.W ||
                p == PhonemeEnum.Y )
            return true;
        return false;
    }

    private static boolean isAspirate(PhonemeEnum p) {
        if (    p == PhonemeEnum.HH )
            return true;
        return false;
    }

}









































































































