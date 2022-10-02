package saarr_5.framework.annexed_genitive;

import saarr_5.morphalize.Word;

public class AnnGen {

    public String annexed;
    public String genitive;
    private Word alpha;
    private Word beta;
//    public AnnGen(Word w) {
//        annexed = w.modifiedStemDet();
//        genitive = w.encVal();
//
//    }

    public AnnGen(Word w1, Word w2) {

        annexed = w1.modifiedStemDet();
        genitive = w2.modifiedStemDet();
        alpha = w1;
        beta = w2;

    }

    public Word getAnnexed() {
        return this.alpha;
    }

    public Word getGenitive() {
        return this.beta;
    }

    @Override
    public String toString() {
        return this.annexed + " : " + this.genitive;
    }
}
