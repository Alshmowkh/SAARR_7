package saarr_5.morphalize;

import edu.columbia.ccls.madamira.configuration.MorphFeatureSet;
import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import saarr_5.Simile;
import saarr_5.framework.Entity;
import saarr_5.framework.phraser.Phrase;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.pl;

public class Sentence extends ArrayList<Word> {

    private int count;
    private int id;
    private OutSeg outseg;
    private List<Phrase> phrases;
    private Sentence tokens;
    private String rawSentence;
    private Entity IPentity;
    private List AGentity;
    private Entity CGentity;
    private Entity CIentity;
    private Simile simile;
    private boolean morphed;
    private Entity esf;

    public Sentence(OutSeg outs, String rawSent) {
        outseg = outs;
        rawSentence = rawSent;
        mapping();
        morphed = true;
    }

    public Sentence() {
        outseg = null;
    }

    public int count() {
        return this.size();
    }

    public int id() {
        return id;
    }

    public String rawSentence() {
        return this.rawSentence;
    }

    public void setRawSentence(String raw) {
        this.rawSentence = raw;
    }

    public OutSeg outseg() {
        return outseg;
    }

    public void outseg(OutSeg outsg) {
        outseg = outsg;
    }

    public void outseg(Object outsg) {
        outseg = (OutSeg) outsg;
    }

    public void mapping() {
        if (this.outseg == null) {
            return;
        }
//        pl(outseg.getSegmentInfo().getNer().getNe());
        Word clitic = null, word = null;
        List<edu.columbia.ccls.madamira.configuration.Word> morphs = outseg.getWordInfo().getWord();
        int index = 0;
        MorphFeatureSet wf = null;

        Iterator<edu.columbia.ccls.madamira.configuration.Word> itr = morphs.iterator();
        Iterator itr2;
        while (itr.hasNext()) {
            edu.columbia.ccls.madamira.configuration.Word w = itr.next();
            word = new Word();
            word.value(w.getWord());
//            try {
            itr2 = w.getAnalysis().iterator();
            if (itr2.hasNext()) {
                wf = w.getAnalysis().get(0).getMorphFeatureSet();
                word.diacValue(wf.getDiac() != null ? wf.getDiac() : word.value());
                word.setGloss(wf.getGloss());
                word.tag(wf.getPos());
                word.stem(wf.getStem());
                word.modifiedStem(word.stem());

                word.enc0(wf.getEnc0() != null && !"na".equals(wf.getEnc0()) && !"0".equals(wf.getEnc0()) && !wf.getEnc0().isEmpty() ? wf.getEnc0() : null);
                word.prc0(wf.getPrc0() != null && !"na".equals(wf.getPrc0()) && !"0".equals(wf.getPrc0()) && !wf.getPrc0().isEmpty() ? wf.getPrc0() : null);
                word.prc1(wf.getPrc1() != null && !"na".equals(wf.getPrc1()) && !"0".equals(wf.getPrc1()) && !wf.getPrc1().isEmpty() ? wf.getPrc1() : null);
                word.prc2(wf.getPrc2() != null && !"na".equals(wf.getPrc2()) && !"0".equals(wf.getPrc2()) && !wf.getPrc2().isEmpty() ? wf.getPrc2() : null);
                word.prc3(wf.getPrc3() != null && !"na".equals(wf.getPrc3()) && !"0".equals(wf.getPrc3()) && !wf.getPrc3().isEmpty() ? wf.getPrc3() : null);
                word.per(wf.getPer() != null && !"na".equals(wf.getPer()) && !"0".equals(wf.getPer()) && !wf.getPer().isEmpty() ? wf.getPer() : null);
                word.num(wf.getNum() != null && !"na".equals(wf.getNum()) && !"0".equals(wf.getNum()) && !wf.getNum().isEmpty() ? wf.getNum() : null);
                word.vox(wf.getVox() != null && !"na".equals(wf.getVox()) && !"u".equals(wf.getVox()) && !wf.getVox().isEmpty() ? wf.getVox() : null);
                word.asp(wf.getAsp() != null && !"na".equals(wf.getAsp()) && !wf.getAsp().isEmpty() ? wf.getAsp() : null);
                word.gen(wf.getGen() != null && !"na".equals(wf.getGen()) && !wf.getGen().isEmpty() ? wf.getGen() : null);
                word.cas(wf.getCas() != null && !"na".equals(wf.getCas()) && !"u".equals(wf.getCas()) && !wf.getCas().isEmpty() ? wf.getCas() : null);
                word.isLetter = false;

//                if (word.hasClitic()) {
                deClitic(word);
//                }
                word.modifyStem();
                word.tokens(this.tokenize(word));

                word.setIndex(index++);

//            } catch (Exception e) { }
            }
            if (word.diacValue() != null) {
                this.add(word);
                count++;
            }
        }
    }

    String tokenize(Word word) {

        String toks = "";
        toks = word.prc3Val() != null ? word.prc3Val() + "-" : "";
        toks += word.prc2Val() != null ? word.prc2Val() + "-" : "";
        toks += word.prc1Val() != null ? word.prc1Val() + "-" : "";
        toks += word.prc0Val() != null ? word.prc0Val() + "-" : "";

        toks += word.modifiedStemDet();

        toks += word.encVal() != null ? "-" + word.encVal() : "";

        return toks;
    }

    void deClitic(Word word) {

        word.prc3Val(word.hasPrc3() ? word.getprc3() : null);
        word.prc2Val(word.hasPrc2() ? word.getprc2() : null);
        word.prc1Val(word.hasPrc1() ? word.getprc1() : null);
        word.prc0Val(word.hasPrc0() && !word.prc0().contains("det") ? word.getprc0() : null);
        word.tag(word.hasDT() ? word.tag() + "_det" : word.tag());
        word.encVal(word.hasEnc() ? word.encliticing() : null);
    }

    void cliticing(Sentence sent, Word word, int index) {

        Word clitic;
        if (word.hasPrc3()) {
            clitic = new Word();
            clitic.isLetter = true;
            clitic.value(deDiacritic(word.getprc3()));
            clitic.diacValue(word.getprc3());
            word.prc3Val(clitic.diacValue());
            clitic.tag("ques");
            clitic.setIndex(index++);
            count++;
            sent.add(clitic);
        }
        if (word.hasPrc2()) {
            clitic = new Word();
            clitic.isLetter = true;
            clitic.value(deDiacritic(word.getprc2()));
            clitic.diacValue(word.getprc2());
            word.prc2Val(clitic.diacValue());
            clitic.tag("conj");
            clitic.setIndex(index++);
            count++;
            sent.add(clitic);
        }
        if (word.hasPrc1()) {
            clitic = new Word();
            clitic.isLetter = true;
            clitic.value(deDiacritic(word.getprc1()));
            clitic.diacValue(word.getprc1());
            word.prc1Val(clitic.diacValue());
            clitic.tag(clitic.value().startsWith("و") || clitic.value().startsWith("ف") ? "conj"
                    : clitic.value().startsWith("س") ? "fut"
                            : clitic.value().startsWith("ي") ? "voc" : "prep");
            clitic.setIndex(index++);
            count++;
            sent.add(clitic);
        }
        if (word.hasPrc0() && !word.prc0().contains("det")) {
            clitic = new Word();
            clitic.isLetter = true;
            clitic.value(deDiacritic(word.getprc0()));
            clitic.diacValue(word.getprc0());
            word.prc0Val(clitic.diacValue());
            clitic.tag("part_neg");
            clitic.setIndex(index++);
            count++;
            sent.add(clitic);
        }
        if (word.hasDT()) {
            word.tag(word.tag() + "_det");
        }
        word.modifyStem();
        word.encVal(word.hasEnc() ? word.encliticing() : null);
    }

    /*
     phrasing
     */
    public void addPhrase(Phrase phrase) {
        if (phrases == null) {
            phrases = new ArrayList();
        }
        phrases.add(phrase);
//        this.addAll(phrase);
    }

    public List<Phrase> getPhrases() {
        return phrases;
    }

    public Phrase getPhrase(int inx) {

        for (Phrase ph : phrases) {
            for (Word w : ph) {
                if (w.index() == inx) {
                    return ph;
                }
            }
        }
        return null;
    }

    public boolean isNominal() {
        return !this.get(0).isVerb() || this.get(0).isPousdoVerb();
    }

    public boolean hasVerb() {
        return this.stream().anyMatch((w) -> (w.isVerb()));
    }

    public boolean hasRelative() {
        return this.stream().anyMatch((w) -> (w.isRel()));
    }

    public String madamiraOut() {
        return outseg.toString();
    }

    public boolean hasPhrase(int phraseIndex) {
        try {
            this.getPhrases().get(phraseIndex);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public Sentence removePousdo() {
        Word w;
        int j;
        List<String> can = new Letter().abolishers();
        for (int i = 0; i < this.count(); i++) {
            w = this.get(i);
            if (can.contains(deDiacritic(w.value())) || w.tag().equals("part_verb") || w.isPousdoVerb()) {
                this.remove(i);
                if (w.hasPrc()) {
                    j = i - 1;
                    while (j >= 0 && (w = this.get(j)).isLetter) {
                        this.remove(j);
                    }
                }
            }

        }
        for (int i = 0; i < this.count(); i++) {
            this.get(i).setIndex(i);
        }
        return this;
    }

    /*
     full entites
     */
    public Entity IPentity() {
        return IPentity;
    }

    public List AGentity() {
        return AGentity;
    }

    public Entity CGentity() {
        return CGentity;
    }

    public Entity CIentity() {
        return CIentity;
    }

    public void IPentity(Entity ent) {
        IPentity = ent;
    }

    public void AGentity(List ents) {
        AGentity = ents;
    }

    public void CGentity(Entity ent) {
        CGentity = ent;
    }

    public void CIentity(Entity ent) {
        CIentity = ent;
    }

    public List IP_AG_CG_CI() {
        List list = new ArrayList();
        list.add(this.IPentity);
        if (this.AGentity != null && !this.AGentity.isEmpty()) {
            list.addAll(this.AGentity);
        } else {
            list.add(null);
        }
        list.add(this.CGentity);
        list.add(this.CIentity);

        return list;
    }
    /*
     Elequent Simile Figure
     */

    public Entity getESF() {
        return esf;
    }

    public void setESF(Entity es) {
        esf = es;
    }

    public boolean isMorphed() {
        return morphed;
    }

    public void setId(int i) {
        id = i;
    }
//    public boolean isEmpty(){
//        return this.isEmpty();
//    }

    public Sentence removeWordAndReorder(int index) {
        Word w;

        this.remove(index);
//        for (int i = 0; i < this.count(); i++) {
//            w = this.get(i);
//            if (can.contains(deDiacritic(w.value())) || w.tag().equals("part_verb") || w.isPousdoVerb()) {
//                this.remove(i);
//            }
//        }
        for (int i = 0; i < this.count(); i++) {
            this.get(i).setIndex(i);
        }
        return this;
    }

    public boolean hasPausdoVerb() {
        return this.stream().anyMatch((w) -> (w.isPousdoVerb()));
    }

    public void removeInsidePunc() {

        int i = 0;
        Word w = this.get(0);
        List paren = Arrays.asList("(", ")", "[", "]", "{", "}");
        for (i = 1; i < this.size(); i++) {
            if (w.isPunc()) {
                this.remove(w);
            }

            if (paren.contains(w.value())) {
                w = this.get(i++);
                while (!w.isLast() && !paren.contains(w)) {
                    this.remove(w);
                    w = this.get(i++);
                    pl(w);
                }
            }
            w = this.get(i);
        }
//        while (!(w = this.get(i)).isLast()) {
//
//            if (w.isPunc()) {
//                this.remove(w);
//                if (paren.contains(w.value())) {
//                    while (!(w = this.get(i)).isLast()) {
//                        pl(w);
//                        if (paren.contains(w)) {
//                            this.remove(w);
//                            break;
//                        }
//                        this.remove(w);
//                    }
//                }
//            }
//            i++;
//        }
        for (i = 0; i < this.count(); i++) {
            this.get(i).setIndex(i);
        }
    }

    public void dumpPhrases() {
        this.phrases = null;
    }

    public boolean hasCandidateESF() {
        return this.IP_AG_CG_CI() != null
                && !this.IP_AG_CG_CI().isEmpty();
    }

    public boolean isRetorical() {
        return this.esf != null;

    }

    public void phrases(List<Phrase> phrases) {
        this.phrases = phrases;
    }

}
