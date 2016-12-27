package misc;

public enum Phoneme {

    //Arpabet phonemes

    //Vowels
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

    //Consonants
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

    private boolean isVowel(Phoneme p) {
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

}


































































































