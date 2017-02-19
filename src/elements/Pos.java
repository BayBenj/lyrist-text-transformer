package elements;

import english.Number;

import java.io.Serializable;

public enum Pos implements Serializable {

    CC,
    CD,
    DT,
    EX,
    FW,
    IN,
    JJ,
    JJR,
    JJS,
    LS,
    MD,
    NN,
    NNS,
    NNP,
    NNPS,
    PDT,
    POS,
    PRP,
    PRP$,
    RB,
    RBR,
    RBS,
    RP,
    SYM,
    TO,
    UH,
    VB,
    VBD,
    VBG,
    VBN,
    VBP,
    VBZ,
    WDT,
    WP,
    WP$,
    WRB,

    PUNCTUATION,
    UNKNOWN,
    CONTRACTION_WORD;

    public PosFam getFam() {
        if (this == NN || this == NNS) 
            return PosFam.NOUN;
        
        else if (this == JJ || this == JJR || this == JJS)
            return PosFam.ADJ;

        else if (this == NNP || this == NNPS)
            return PosFam.P_NOUN;

        else if (this == RB || this == RBR || this == RBS)
            return PosFam.ADV;

        else if (this == VB || this == VBD || this == VBG || this == VBN || this == VBP || this == VBZ)
            return PosFam.VERB;
        
        return null;
    }

    public Number getNumber() {
        if (this == NNS || this == NNPS)
            return Number.PLURAL;

        else if (this == NN || this == NNP)
            return Number.SINGULAR;

        return null;
    }
    
}








































































