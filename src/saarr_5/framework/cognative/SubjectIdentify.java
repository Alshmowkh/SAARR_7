/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.cognative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class SubjectIdentify {

    Sentence sentence;

    public SubjectIdentify() {

    }

    public SubjectIdentify(Sentence sent) {

        sentence = Phraser.phrasing(sent.removePousdo());

    }

    public Word subject() {
        if (!sentence.hasVerb()) {
            return null;
        }
        Phrase verbph = null;
        Word verb;
        Word subject = null;

        for (Phrase ph : sentence.getPhrases()) {
            if (ph.isVP()) {
                verbph = ph;
            }
            if (verbph != null) {
                break;
            }
        }

        if (verbph != null && !verbph.isEmpty()) {
            verb = verbph.get(0);
            if (verb.isPart()) {
                verb = verbph.get(1);
            }
        } else {
            return null;
        }
//        pl(verb);
        if (verb.vox() != null && verb.vox().equals("p")) {
//            System.out.println(verb + " is passive verb.");
            return subject;
        }
        if (verb.per() != null && verb.per().equals("2")) {

            return this.implicitSubject(verb);
        }
//        pl(verbph);

        Phrase candidate;
        if (verbph.index() == 0) {
            candidate = sentence.getPhrase(verbph.index() + 1);
            subject = VS(verb, candidate);
        } else {
            candidate = sentence.getPhrase(verbph.index() - 1);
            subject = SV(candidate, verb);
        }

        return subject;
    }

    public Word VS(Word verb, Phrase get) {
        if (get == null) {
            return null;
        }
        Word subject = null;
        String per = verb.per();
//        pl(per);
//        pl(verb.enc0());
        if ("1".equals(per)) {
            if (verb.value().endsWith("ت")) {
                subject = checkTaaf(verb, get);
                if (subject != null) {
                    return subject;
                }
            }

            if (get.isPRP()) {
                subject = get.get(0);
            } else {
                subject = implicitSubject(verb);
            }

        } else if ("3".equals(per)) {
            subject = guessSub(verb, get);
        }
        return subject;
    }

    private Word SV(Phrase before, Word verb) {
//pl(before);
        Word subject = null, resubject;
        int i = before.index();
        while (i >= 0) {
            if (before.isDTNP() || before.isNP() || before.isPRP() || before.isDP()) {
                subject = before.get(0);
                break;
            }
            if (before.get(0).isPunc()) {
                break;
            }
            before = sentence.getPhrase(i--);
        }
        subject = this.guessSub(before, verb);

        if (subject == null && sentence.hasPhrase(i + 2)) {
            resubject = this.VS(verb, sentence.getPhrases().get(i + 2));
            if (resubject != null) {
                return resubject;
            }
        }
        return subject;
    }

    public Word implicitSubject(Word verb) {
        Word prn = new Word();
        prn.tag("im_pron");

        String value;
        String per = verb.per() != null ? verb.per() : "";
        String gen = verb.gen() != null ? verb.gen() : "";
        String num = verb.num() != null ? verb.num() : "";
        String pron = per + gen + num;
        value = !"".equals(pron) ? implicitPron(pron) : "none";
        prn.value(value);
        prn.diacValue(value);
        prn.modifiedStem(value);
        prn.tokens(value);
        prn.root(value);

        return prn;
    }

    String implicitPron(String feat) {
        Map map = new HashMap();
        map.put("1", "أنا");
        map.put("2", "أنت");
        map.put("3", "هم");

        map.put("m", "أنا");
        map.put("f", "أنا");

        map.put("s", "أنا");
        map.put("d", "نحن");
        map.put("p", "هم");

        map.put("1s", "أنا");
        map.put("1p", "نحن");

        map.put("2m", "أنت");
        map.put("2f", "أنت");
        map.put("2s", "أنت");
        map.put("2d", "أنتما");
        map.put("2p", "أنتم");

        map.put("3m", "هو");
        map.put("3f", "هي");
        map.put("3s", "هو");
        map.put("3d", "هما");
        map.put("3p", "هم");

        map.put("1ms", "أنا");
        map.put("1fs", "أنا");
        map.put("1md", "نحن");
        map.put("1fd", "نحن");
        map.put("1mp", "نحن");
        map.put("1fp", "نحن");

        map.put("2ms", "أنت");
        map.put("2fs", "أنت");
        map.put("2md", "أنتما");
        map.put("2fd", "أنتن");
        map.put("2mp", "أنتم");
        map.put("2fp", "أنتن");

        map.put("3ms", "هو");
        map.put("3fs", "هي");
        map.put("3md", "هما");
        map.put("3fd", "هن");
        map.put("3mp", "هم");
        map.put("3fp", "هن");

        return map.get(feat).toString();
    }

    private Word guessSub(Word verb, Phrase phr) {
        Word noun = null;
        String genv = verb.gen();
        String numv = verb.num();

        if (phr.isNP() || phr.isDTNP() || phr.isDP()) {
            noun = phr.get(0);
            if (noun.gen().equals(genv)) {
                return noun;
            } else {
                noun = null;
            }
        }

        int i = phr.index();
        if (phr.isPP() || phr.isS()) {
            i++;
        }
        if (i < sentence.getPhrases().size() - 1) {
            phr = sentence.getPhrases().get(i + 1);
            return guessSub(verb, phr);
        }

        if (noun == null) {
            return this.implicitSubject(verb);
        }

        return noun;
    }

    private Word guessSub(Phrase phr, Word verb) {

        Word noun = null;
        String genv = verb.gen();
        String numv = verb.num();

        if (phr.isNP() || phr.isDTNP() || phr.isDP()) {
            noun = phr.get(0);
            if (noun.gen().equals(genv) && numv.equals(noun.num())) {
                return noun;
            }
        }

        int i = phr.index();

        if (sentence.hasPhrase(i - 1)) {
            phr = sentence.getPhrases().get(i - 1);
            return SV(phr, verb);
        }

        if (noun == null) {
            return this.implicitSubject(verb);
        }

        return noun;

    }

    private Word checkTaaf(Word verb, Phrase phr) {
        Word candid = null;
        if (phr.isNP() || phr.isDTNP() || phr.isPRP()) {

            if ("f".equals(phr.get(0).gen())) {
                candid = phr.get(0);
            }
        }
        if (sentence.hasPhrase(phr.index() - 2)) {
            phr = sentence.getPhrases().get(phr.index() - 2);
            if (phr.isNP() || phr.isDTNP() || phr.isPRP()) {
                if ("f".equals(phr.get(0).gen())) {
                    candid = phr.get(0);
                }
            }
        }
        return candid;
    }
}
