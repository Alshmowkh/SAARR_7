package saarr_5.zombie;

import saarr_5.morphalize.*;
import edu.columbia.ccls.madamira.configuration.MorphFeatureSet;
import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import saarr_5.framework.phraser.Phrase;
import static saarr_5.utiles.Utile.deDiacritic;

 class Sentence extends ArrayList<Word> {

    public int count;
    private String id;
    private final OutSeg outseg;
    private List<Phrase> phrases;

    public Sentence(OutSeg outs) {
        outseg = outs;
        wording();
    }

    public Sentence() {
        outseg = null;
    }

    public int count() {
        return this.size();
    }

    public String id() {
        return id;
    }

    private void wording() {
//        sentence = new Sentence();
        Word clitic = null, word = null;
        List<edu.columbia.ccls.madamira.configuration.Word> morphs = outseg.getWordInfo().getWord();
        int index = 0;;
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
                word.diacValue(wf.getDiac());
                word.setGloss(wf.getGloss());
                word.tag(wf.getPos());
                word.stem(wf.getStem());
                word.modifiedStem(wf.getStem());

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

                if (word.hasClitic()) {
                    cliticing(word, index);
                    index = this.size();
                }
                word.setIndex(index++);
                word.modifyStem();
//                word.modifyTag();
//            } catch (Exception e) { }
            }
            if (word.hasPos()) {

                this.add(word);
                count++;
            }

        }
        id = outseg.getId();

    }

    void cliticing(Word word, int index) {

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
            this.add(clitic);
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
            this.add(clitic);
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
            this.add(clitic);
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
            this.add(clitic);
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
    }

    public List<Phrase> getPhrases() {
        return phrases;
    }

//    public Phrase getPhrase(int inx) {
//
//        for (Phrase ph : phrases) {
//            for (Word w : ph) {
//                if (w.index() == inx) {
//                    return ph;
//                }
//            }
//        }
//        return null;
//    }

    public boolean isNominal() {
        return !this.get(0).isVerb() || this.get(0).isPousdoVerb();
    }

    public boolean hasVerb() {
        return this.stream().anyMatch((w) -> (w.isVerb()));
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

        List<String> can = new Letter().abolishers();
        for (int i = 0; i < this.count(); i++) {
            w = this.get(i);
            if (can.contains(deDiacritic(w.value())) || w.tag().equals("part_verb") || w.isPousdoVerb()) {
                this.remove(i);
            }
        }
        for (int i = 0; i < this.count(); i++) {
            this.get(i).setIndex(i);
        }
        return this;
    }

    public List tokens() {
        return this.stream().map(w -> w.modifiedStemDet()).collect(Collectors.toList());
    }

}
