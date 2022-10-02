/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.circumstance;

import saarr_5.framework.Subjector;
import java.util.ArrayList;
import java.util.List;
import saarr_5.Type;
import saarr_5.framework.Entity;
import saarr_5.framework.cognative.Described_Adjective;
import saarr_5.framework.cognative.verbroot.VerbRoot;
import saarr_5.framework.phraser.Phrase;
import saarr_5.framework.phraser.Phraser;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Circumstance {

    private Sentence sentence;
    private Word verb;
    private Word subject;
    private Word object1;
    private Word object2;
    private Word circ;

    Entity identify(Sentence sent) {
        sentence = sent;
//           pl(sentence.getPhrases());
        if (sentence.hasRelative()) {
            return null;
        }

        if (sentence.hasPausdoVerb()) {
            sentence.removePousdo();
        }
        
        if (sentence.getPhrases() == null) {
            sentence = Phraser.phrasing(sentence);
        }
//        pl(sentence.getPhrases());
        Entity cireny = detect();

        return cireny;
    }

    private Entity detect() {
        List initials = new ArrayList();
        List latets = new ArrayList();
        List candidates = new ArrayList();

        Phrase phr;
        Phrase vp;
// pl(sentence);
        int i = 0;
        int count = sentence.getPhrases().size();
        while (count > i && sentence.getPhrase(i) !=null && !sentence.getPhrase(i).isVP()) {
            initials.add(sentence.getPhrases().get(i));
            i++;
        }
        vp = sentence.getPhrase(i);

        while (count > i) {
            phr = sentence.getPhrases().get(i++);
//            pl(phr+"p"+phr.index());
            if (circumstance(phr)) {
                candidates.add(phr.get(0));
            } else {
                latets.add(phr);
            }
        }

        if (!candidates.isEmpty()) {
            circ = (Word) candidates.get(0);
            subject = new Subjector(sentence).subject();
        }
//        pl(initials);
//        pl(vp);
//        pl(candidates);
//        pl(subject);
        return circ != null && subject != null ? new Entity(subject, circ, Type.ESF_Cr) : null;
    }

    private Word ownerDetect(Word w) {
//        return new Described_Adjective(sentence).subject();
        return null;
    }

    public Entity circumstancing(Sentence sentence) {
        Word v, circum = null;
        int size = sentence.size();
        for (int i = 0; i < size; i++) {
            v = sentence.get(i);
            if (v.isVerb()) {

                while (i < size) {
                    return new Entity(v, circum, null);
                }
            }
        }
        return null;
    }

    private boolean circumstance(Phrase phr) {
        Phrase former = sentence.getPhrase(phr.index());
//        pl(sentence);
        return (phr.get(0).isIndef()
                || phr.get(0).isAdj())
                && (former ==null || !former.isPP())
                && (former ==null || !former.isVP())
                && phr.size() < 2;

    }
}
