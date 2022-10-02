/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.inchoative_predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import saarr_5.framework.phraser.Phrase;
import saarr_5.framework.phraser.Phraser;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Inchoative extends Phrase {

    private Sentence sentence;
    private Phrase lastPhrase;
    private Inchoative inchoative;
    private Word head;
    private int begin;
    private int end;

    public Inchoative(Sentence sent) {
        sentence = sent;
//        inchoative=this.getInchoative();
//        head=this.getHead();
        if (sentence.getPhrases() == null) {
            sentence = Phraser.phrasing(sent);
        }

    }

    private Inchoative() {

    }

    public Inchoative getInchoative(ListIterator itr) {
        Phrase ph, ph1, ph2;
        Inchoative incho = new Inchoative();
        if (itr.hasNext()) {
            ph = (Phrase) itr.next();
            if (ph.isVP()) {
                return null;
            }
            if (ph.isNP()
                    || ph.isDTNP()
                    || ph.isDP()
                    || ph.isEP()
                    || ph.isPRP()
                    || ph.isQP()) {
                incho.addAll(ph);
                lastPhrase = ph;
            }

            if (itr.hasNext()) {
                ph1 = (Phrase) itr.next();
                if (ph1.isCP()) {
                    ph2 = getInchoative(itr);
                    if (ph2 != null && !ph2.isEmpty()) {
                        incho.addAll(ph1);
                        incho.addAll(ph2);
                        lastPhrase = ph2;
                    } else {
                        itr.previous();
                    }

                } else {
                    itr.previous();
                }
            }

            return incho;
        }
        return null;
    }

    public Inchoative getInchoative() {
        if (this.inchoative != null) {
            return this.inchoative;
        }
        Inchoative incho = null;

        if (!sentence.isNominal()) {
            return incho;
        }
        List<Phrase> phrases = sentence.getPhrases();
//        phrases = phrases.subList(this.trim(phrases), phrases.size());
        List<Phrase> inchPhrs = readInchoative(phrases, 0);
//        pl(inchPhrs);
        if (inchPhrs != null && !inchPhrs.isEmpty()) {
            incho = new Inchoative();
            this.begins(inchPhrs.get(trim(phrases)).index());
            this.ends(inchPhrs.size());
            for (Iterator<Phrase> itr = inchPhrs.iterator(); itr.hasNext();) {
                incho.addAll(itr.next());
            }
        }
        this.inchoative = incho;
        return incho;
    }

    @Override
    public void begins(int start) {
        begin = start;
    }

    @Override
    public void ends(int finish) {
        end = finish;
    }

    @Override
    public int begins() {
        return begin;
    }

    @Override
    public int ends() {
        return end;
    }

    private int trim(List<Phrase> sent) {
        int seek = 0;
        if (sent.size() < 1) {
            return seek;
        }
        if (sent.get(seek).get(0).isPousdoVerb()) {
            seek += 1;
            seek += trim(sent.subList(seek, sent.size()));
        }
        if (sent.get(seek).get(0).isConj()) {
            seek += 1;
            seek += trim(sent.subList(seek, sent.size()));
        }
        return seek;
    }

    public Word getHead() {
        Word heady = null;
        Inchoative incho = null;
        if (this.inchoative == null) {
            incho = this.getInchoative();
        }

        if (incho != null && !incho.isEmpty()) {
            heady = getInchoative().get(0);
        }
//        pl(heady);
        return heady;
    }

    public Phrase lastPhrase() {
        return lastPhrase;
    }

    private boolean shifter(Phrase ph) {

        return ph.isNP()
                || ph.isDTNP()
                || ph.isDP()
                || ph.isEP()
                || ph.isPRP()
                || ph.isQP()
                || ph.isCP();

    }

    private boolean noun3p(Phrase phr) {
        return phr.get(0).enc0() != null && phr.get(0).enc0().startsWith("3");

    }

    private List<Phrase> repositionPhrase(List<Phrase> reorderPhrs, Phrase phr) {
//        pl(reorderPhrs);
        if (reorderPhrs.size() < 1) {
            reorderPhrs.add(0, phr);
            return reorderPhrs;
        }
        int seek = trim(reorderPhrs);
        Phrase a = reorderPhrs.get(seek);
        reorderPhrs.set(seek, phr);
        reorderPhrs.add(a);
        return reorderPhrs;
    }

    private List<Phrase> readInchoative(List<Phrase> phrases, int index) {
        List<Phrase> inchPhrs = new ArrayList();
        List<Phrase> temp;// = new ArrayList();

        Phrase ph, ph1;
        ph = phrases.get(index);
//        pl(phrases);
        if (ph.isNP()
                || ph.isDTNP()
                || ph.get(0).isDTadj()
                || ph.isDP()
                || ph.isEP()
                || ph.isPRP()
                || ph.isQP()
                || ph.isCP()
                || ph.isRP()) {
            inchPhrs.add(ph);
        }
        index++;
        if (index < phrases.size()) {
            ph1 = phrases.get(index);
            if (ph.isCP() || ph1.isCP()) {
                inchPhrs.add(ph1);
                index++;
                temp = readInchoative(phrases, index);
                if (temp != null && !temp.isEmpty()) {
                    inchPhrs.addAll(temp);
                    index = temp.get(temp.size() - 1).index();
                }
            }
            if(ph1.isDTNP()){
                inchPhrs.add(ph1);
            }
        }

//pl(inchPhrs);
        if (index < phrases.size()) {
            ph1 = phrases.get(index);
            if (noun3p(ph1)) {
                ph1.get(0).encVal(null);
                inchPhrs = repositionPhrase(inchPhrs, ph1);
            }
        }

        return inchPhrs;
    }
}
