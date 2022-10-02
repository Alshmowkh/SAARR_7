package saarr_5.framework.annexed_genitive;

import java.util.ArrayList;
import java.util.List;
import saarr_5.framework.phraser.Phrase;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.pl;

public class AnnGenDetector {

    Phrase phrase;
    List<AnnGen> AGs;

    public AnnGenDetector(Phrase wrds) {
        phrase = wrds;
    }

    AnnGen getAnnGen() {
        AGs = new ArrayList();
        AnnGen ag = null;
        Word w1, w2;
        for (int i = 0; i < phrase.size(); i++) {
            w1 = phrase.get(i);
            if (w1.hasEnc()) {
                w2 = w1.separateEnc();
                ag = new AnnGen(w1, w2);
                AGs.add(ag);
                i++;
//                pl(ag);
            }

            if (phrase.size() > i + 1) {
                w2 = phrase.get(i + 1);
                if (w1 != null && w2 != null) {
                    ag = new AnnGen(w1, w2);
                    AGs.add(ag);
                }

//                pl(ag);
            }
        }
        if (AGs.size() > 1) {
//            System.out.println("Phrase has more one idafa...");
        }
        return ag;
    }

    AnnGen getAnnGenESF() {
        AGs = new ArrayList();
        AnnGen ag = null;
        Word w1, w2;
        for (int i = 0; i < phrase.size(); i++) {
            w1 = phrase.get(i);
            if (w1.hasEnc() || w1.isAdj() || w1.isAdv()) {
                continue;
            }

            if (phrase.size() > i + 1) {
                w2 = phrase.get(i + 1);
                if (w1 != null && w2 != null) {
//                    if (w2.isAdj() || w2.isAdv() || w2.isDTadj()) {
//                        continue;
//                    }
                    if (w1.isNoun() && w2.isDTnoun()) {
                        ag = new AnnGen(w1, w2);
                        AGs.add(ag);
                    }

                }

//                pl(ag);
            }
        }
        if (AGs.size() > 1) {
//            System.out.println("Phrase has more one idafa...");
        }
        return ag;
    }

}
