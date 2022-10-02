/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import saarr_5.morphalize.Morphalizer;
import saarr_5.morphalize.Sentence;

/**
 *
 * @author bakee
 */
public class Document2 implements Callable<Document> {

    Document doc;
//    private String path;

    public Document2(Document rawSentence) {
        doc = rawSentence;
    }

    @Override
    public Document call() {
        if (doc.size() == 1) {
            if (doc.get(0).outseg() == null) {
                doc.get(0).outseg(Morphalizer.outseg(doc.get(0).rawSentence()));
            }
            doc.get(0).mapping();
        } else if (doc.size() > 1) {
            String[] rawSents = new String[doc.size()];
            for (int i = 0; i < doc.size(); i++) {
                rawSents[i] = doc.get(i).rawSentence();
            }
            List outsegs = Morphalizer.outsegs(Arrays.asList(rawSents));
            for (int i = 0; i < doc.size(); i++) {
                doc.get(i).outseg(outsegs.get(i));
                doc.get(i).mapping();
            }
        }
        return doc;
    }

}
