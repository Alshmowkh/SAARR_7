/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.morphalize;

import saarr_5.morphalize.madamiray.Madamiray;
import saarr_5.utiles.Utile;
import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Morphalizer {

    public static Sentence morphalize(String sent) {

        if (sent == null) {
            return null;
        }

        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;

        mada = new Madamiray(sent);
        outseg = mada.getMorpholizing().get(0);
        sentence = new Sentence(outseg, sent);
        return sentence;
    }

    public static Sentence morphalize(String sent, boolean outsegPrint) {

        if (sent == null) {
            return null;
        }
        if (!outsegPrint) {
            return morphalize(sent);
        } else {
            Sentence sentence = morphalize(sent);
            new Utile().printSentence(sentence);
            return sentence;
        }
    }

    public static Sentence morphalize(String pathFile, int id) {

        Utile ut = new Utile();
        ut.setFilePath(pathFile);
        String rawSent = ut.sentence(id);
        return morphalize(rawSent);
    }

    public static Sentence morphalize(String pathFile, int id, boolean print) {

        Utile ut = new Utile();
        ut.setFilePath(pathFile);
        String rawSent = ut.sentence(id);
        return morphalize(rawSent, print);
    }

    public static List<Sentence> morphalize(File document) {

        Utile ut = new Utile();
        ut.setFilePath(document);
        List<String> rawSents = ut.sentences();
        return morphalize(rawSents);
    }

    public static List<Sentence> morphalize(File document, int seek) {

        Utile ut = new Utile();
        ut.setFilePath(document);
        List<String> rawSents = ut.sentences();
        return morphalize(rawSents.subList(seek, rawSents.size()));
    }

    private static List<Sentence> morphalize(List<String> rawSents) {
        List<Sentence> sentences = new ArrayList();
        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;
        {
            mada = new Madamiray(rawSents);
            for (int i = 0; i < rawSents.size(); i++) {
                outseg = mada.getMorpholizing().get(i);
                sentence = new Sentence(outseg, rawSents.get(i));
                sentences.add(sentence);
            }
        }

        return sentences;
    }

    public static Sentence morphalize(Sentence sent) {

        if (sent == null) {
            return null;
        }

        Madamiray mada;
        mada = new Madamiray(sent.rawSentence());
        sent.outseg(mada.getMorpholizing().get(0));
        sent.mapping();
        return sent;
    }

    public static OutSeg outseg(String sent) {
        if (sent == null) {
            return null;
        }

        Madamiray mada;
        OutSeg outseg;

        mada = new Madamiray(sent);
        outseg = mada.getMorpholizing().get(0);
        return outseg;
    }

    public static List<OutSeg> outsegs(List sents) {
        if (sents == null || sents.isEmpty()) {
            return null;
        }

        List outsegs = new ArrayList();
        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;
        {
            mada = new Madamiray(sents);
//            for (int i = 0; i < sents.size(); i++) {
            outsegs = mada.getMorpholizing();
//                outsegs.add(outseg);
//            }
        }
        return outsegs;
    }
}
