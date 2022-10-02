package saarr_5.framework.inchoative_predicate;

import java.util.List;
import saarr_5.framework.phraser.Phrase;
import saarr_5.framework.phraser.Phraser;
import saarr_5.morphalize.Sentence;
import static saarr_5.utiles.Utile.pl;

public class RelativeClause {

    Sentence sentence;
    List<Phrase> phrases;
    List<Phrase> left;
    Phrase pronoun;
    List<Phrase> right;
    Sentence rights;
    Sentence lefts;
    int indexOfR;

    public RelativeClause(Sentence sent) {
        sentence = Phraser.phrasing(sent);
        phrases = sentence.getPhrases();
        factory();
    }

    public RelativeClause(Sentence sent, int index) {
        sentence = sent;
        indexOfR = index;
        phrases = sentence.getPhrases();

    }

    private void factory() {
        rights = new Sentence();
        lefts = new Sentence();

        int i = 0;
        int phs = phrases.size();
        while (phs > i && !phrases.get(i).isRP()) {
            rights.addAll(phrases.get(i));
            i++;
        }
        pronoun = phrases.get(i);
        i++;
        while (phs > i && !phrases.get(i).isRP()) {
            lefts.addAll(phrases.get(i));
            i++;
        }

    }

    public Sentence getRelSentence() {
//        pl(phrases);

        Sentence rs = new Sentence();

        int i = 0;
        int phs = phrases.size();
        while (phs > i && !phrases.get(i).isRP()) {

            rights.addPhrase(phrases.get(i));
            i++;
        }
        pronoun = phrases.get(i);
        i++;
        while (phs > i && !phrases.get(i).isRP()) {

            lefts.addPhrase(phrases.get(i));
            i++;
        }
        return rights;
    }

    public Sentence getRight() {
        return rights;
    }

    public Sentence getLeft() {
        return lefts;
    }

    public Phrase getPronoun() {
        return pronoun;
    }

    public Phrase getPredicate() {

        Phrase ph1, ph = null;
        if (phrases == null || phrases.isEmpty()) {
            return ph;
        }

        int i = indexOfR + 1;
        while (phrases.size() > i) {
            ph1 = phrases.get(i);
            if (ph1.isNP()) {
                ph = ph1;
                break;
            }
            i++;
        }
        if (ph == null) {
            ph = phrases.get(indexOfR);
        }
//        left = phrases.subList(0, pronoun.index());
//        right = phrases.subList(pronoun.index() + 1, phrases.size());

//        ph = getPhrase(right);
        return ph;
    }

    private Phrase getPhrase(List<Phrase> clause) {
        Phrase ph = null, phv, phdt;
        Phrase ph1, ph2, ph3, ph4;
        int count = clause.size();
        if (clause == null || clause.isEmpty()) {
            return null;
        }
        int i = 0;
        if (clause.get(i).isVP()) {
            phv = clause.get(i);
            i++;
        }
        while (count > i && (clause.get(i).isDTNP() || clause.get(i).isCP())) {
            phdt = clause.get(i);
            i++;
        }
//        pl(clause.get(i).get(0).tag());
        ph = new Predicate().readPredPhr(clause, i);
//        if (ph1 != null && !ph1.isEmpty()) {
//            ph = ph1;
//        }
        return ph.isEmpty() ? clause.get(0) : ph;
    }

}
