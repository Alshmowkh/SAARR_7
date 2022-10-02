/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.inchoative_predicate;

import java.util.ArrayList;
import java.util.List;
import saarr_5.framework.phraser.Phrase;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Predicate extends Phrase {

    private Sentence sentence;
    private Inchoative inchoative;
    private int offset;
    private boolean detected;
    private Predicate predicate;

    public Predicate(Sentence sent, Inchoative incho) {
        sentence = sent;
        inchoative = incho;
        offset = incho.ends();
        detected = false;
//    pl(offset);

    }

    public Predicate() {

    }

    public Predicate getPredicate() {
        Predicate pred = null;
        List<Phrase> predPhrs = null;
        if (sentence.getPhrases().size() > offset) {
            predPhrs = this.getPredicate(sentence.getPhrases(), offset);
        }
        if (predPhrs != null && !predPhrs.isEmpty()) {
            pred = new Predicate();
            for (Phrase p : predPhrs) {
                pred.addAll(p);
            }

//            this.begins(predPhr.get(0).index());
//            this.ends(predPhr.get(predPhr.size() - 1).index() + 1);
        }
        predicate = pred;
        return pred;
    }

    public Word getHead() {
        Word head = null;
        Predicate predic = null;
        if (this.predicate == null) {
            predic = this.getPredicate();
        }
        if (predic != null) {
            head = predicate.get(0);
        }
//        if (this.getPredicate() != null && !this.getPredicate().isEmpty()) {
//            head = getPredicate().get(0);
//        }
        return head;
    }

    private List<Phrase> getPredicate(List<Phrase> phrases, int index) {
//        pl(phrases.get(index).getTypeTB()+" ;; "+index);
        List<Phrase> predPhrs = new ArrayList();
        Phrase ph, ph1, ph2;
        ph = new Phrase();
        int count = phrases.size();
        if (index < count) {
//            pl(phrases.get(index));
            ph = readPredPhr(phrases, index);
        }

        if (!ph.isEmpty()) {
            predPhrs.add(ph);
        }
        index++;
        while (index < count && (ph1 = phrases.get(index)).isCP()) {

            ph2 = readPredPhr(phrases, index++);
            if (!ph2.isEmpty() && ph2.getType() == ph.getType()) {
                predPhrs.add(ph2);
            }
        }

        if (predPhrs.isEmpty()) {
            predPhrs = null;
        }
        return predPhrs;
    }

    private List<Phrase> singleType(List<Phrase> phrases, int index) {
        Phrase ph;

        Phrase ph2, ph1 = new Phrase();
        List<Phrase> temp, phrs = new ArrayList();
        int count = phrases.size();
        if (index < count) {
            ph = phrases.get(index);
            if (ph.isAJP() || ph.isNP()) {
                phrs.add(ph);
            }
        }
        index++;
        while (index < count && phrases.get(index).isCP()) {
            index++;
            temp = singleType(phrases, index);
            if (!temp.isEmpty()) {
                phrs.addAll(temp);
            }
        }
        return phrs;
    }

    private Phrase verbalType(List<Phrase> phrases, int index) {

        Phrase ph;
        Phrase phrase = new Phrase();
        int count = phrases.size();
        if (index < count) {
            ph = phrases.get(index);
            if (ph.isVP()) {
                if (ph.get(0).isPousdoVerb()) {
                    phrase = this.readPredPhr(phrases, index + 1);
                } else {
                    phrase = ph;
                }
            }
        }

        return phrase;
    }

    private Phrase placeTimeType(List<Phrase> phrases, int index) {

        Phrase ph;
        Phrase phrase = new Phrase();
        int count = phrases.size();
        if (index < count) {
            ph = phrases.get(index);
            if (ph.isPTP()) {
                phrase = ph;
            }
        }

        return phrase;
    }

    private Phrase nominalType(List<Phrase> phrases, int index) {
        Phrase ph;
        Phrase phrase = new Phrase();
        int count = phrases.size();
        if (index < count) {
            ph = phrases.get(index);
            if (ph.isDTNP()) {
                phrase = ph;
            }
        }

        return phrase;
    }

    private Phrase prepositionType(List<Phrase> phrases, int index) {
//        pl(phrases + " : index " + index);

        Phrase phrase = new Phrase();
        int count = phrases.size();
        int i = index;
        if (i >= count) {
            return phrase;
        }
        Phrase php = phrases.get(i);

        i++;
        Phrase php1, php2 = null, php3 = null;

        Word prep = php.get(0);
        if (i > count) {
            return php;
        }
        phrase.addAll(php);
        if (i < count) {
            php1 = phrases.get(i);
            phrase.addAll(php1);
        }
        i++;

        if (i < count) {
            php3 = phrases.get(i);
            while (!php3.isNP() && !php3.isAJP()) {
                phrase.addAll(php3);
                i++;
                php3 = null;
                if (count > i) {
                    php3 = phrases.get(i);
                } else {
                    break;
                }
            }
        }
//        pl(php3);
//        pl(phrase);
        if (php3 != null) {
            return php3;
        } else {
            return phrase;
        }
//        php2 = readPredPhr(phrases, i);
//        pl(phrases.get(i).getTypeTB());
//        if (php2 != null && !php2.isEmpty()) {
//            return php2;
//        } else {
//            return phrase;
//        }
    }

    private Phrase getPredicateRP(List<Phrase> phrases, int index) {
        RelativeClause rel = new RelativeClause(this.sentence, index);
        Phrase ph = rel.getPredicate();
        return ph;
    }

    public Phrase readPredPhr(List<Phrase> phrases, int index) {
        List<Phrase> phrs;//=new ArrayList();
        Phrase ph, ph1;
        int count = phrases.size();
        ph1 = new Phrase();

        if (index < count) {
            ph = phrases.get(index);

            if (ph.isNP() || ph.isAJP()) {
                phrs = singleType(phrases, index);
                ph1 = phrs.get(0);
            } else if (ph.isVP()) {
                ph1 = verbalType(phrases, index);
            } else if (ph.isPP()) {
                ph1 = prepositionType(phrases, index);
            } else if (ph.isPTP()) {
                ph1 = placeTimeType(phrases, index);
            } else if (ph.isRP()) {
                ph1 = getPredicateRP(phrases, index);
            } else if (ph.isDTNP()) {
                ph1 = nominalType(phrases, index);
            } else if (ph.isCP()) {
                ph1 = ph;
            } else if (ph.getType() == Type.Punc) {
                return ph1;
            } else {
                pl("Unknown predicate type of " + ph.getType());
            }
//            pl(ph1);
        }
        return ph1;
    }
}
