/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import saarr_5.framework.ESFframework;
import saarr_5.framework.phraser.Phraser;
import saarr_5.morphalize.Morphalizer;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.SentenceFactory;
import saarr_5.utiles.Utile;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Document extends ArrayList<Sentence> {

    private String source;

    public Document(String rawSentence) {
        if (Utile.isFile(rawSentence)) {
            source = rawSentence;
        } else {
            this.add(new Sentence(null, rawSentence));
        }
    }

    public Document(String pathFile, int id) {
        if (Utile.isFile(pathFile)) {
            if (pathFile.endsWith("xml")) {
                SentenceFactory factory = new SentenceFactory();
                this.addAll(factory.sentenceFactory(pathFile, id));
            } else {
                this.addAll(rawSentences(pathFile, id));
            }
        }
    }

    public Document(List<Sentence> sentences) {
        this.addAll(sentences);
    }

    private List<Sentence> rawSentences(String pathFile, int id) {
        List<Sentence> sentences = new ArrayList();
        Utile ut = new Utile();
        ut.setFilePath(pathFile);
        Sentence sentence;
        if (id > 0) {
            String rawSent = ut.sentence(id);
            sentence = new Sentence(null, rawSent);
            sentence.setId(1);
            sentences.add(sentence);
            return sentences;
        } else {
            int i = 1;
            for (Iterator<String> itr = ut.sentences().iterator(); itr.hasNext();) {
                String rawSent = itr.next();
                sentence = new Sentence(null, rawSent);
                sentence.setId(i++);
                sentences.add(sentence);
            }
            return sentences;
        }
    }

    public Document morphlize() {
        if (this.size() == 1) {
            if (this.get(0).outseg() == null) {
                this.get(0).outseg(Morphalizer.outseg(this.get(0).rawSentence()));

            }
            this.get(0).mapping();
        } else if (this.size() > 1) {
            String[] rawSents = new String[this.size()];
            for (int i = 0; i < this.size(); i++) {
                rawSents[i] = this.get(i).rawSentence();
            }
            List outsegs = Morphalizer.outsegs(Arrays.asList(rawSents));
            for (int i = 0; i < this.size(); i++) {
                this.get(i).outseg(outsegs.get(i));
                this.get(i).mapping();
            }
        }
        return this;
    }
    ESFframework framework;

    public Document syntaxProcessing() {

        this.parallelStream().filter(s -> s.size() > 1).forEach(sentence -> {
            pl("Processing sentence: " + sentence.id());
//                pl(sent);
            framework = new ESFframework(sentence);
            framework.getCandidateSimileEnds();
        });
        return this;
    }

    public Document syntaxProcessing2() {

        for (Sentence sent : this) {

            if (sent.size() > 1) {
                pl("Processing sentence: " + sent.id());
//                pl(sent);
                framework = new ESFframework(sent);
                framework.getCandidateSimileEnds();
            }
        }

        return this;
    }

    public Document semanticProcessing() {
        if (this.framework == null) {
            System.out.println("Process syntax first!");
            return this;
        }
        this.parallelStream().forEach(sentence -> framework.getSimile(sentence));
//        for (Sentence sent : this) {
//            framework.getSimile(sent);
//        }

        return this;
    }

    public Document semanticProcessing2() {
        if (this.framework == null) {
            System.out.println("Process syntax first!");
            return this;
        }
        for (Sentence sent : this) {
            framework.getSimile(sent);
        }

        return this;
    }

    public List<Sentence> getSentences() {
        List<Sentence> list = new ArrayList();
        for (Sentence s : this) {
            list.add(s);
        }
        return list;
    }

    public Sentence getSentence(int i) {
        return this.get(i);
    }

    public Document processing() {
//        this.parallelStream().filter(s -> s.size() > 1).forEach(sentence -> {
        this.stream().filter(s -> s.size() > 1).forEach(sentence -> {
            pl("Processing sentence: " + sentence.id());
//                pl(sent);
            framework = new ESFframework(sentence);
            framework.getCandidateSimileEnds();
//            pl(framework.getCandidateSimileEnds());
            framework.getESF();
        });
        return this;
    }

    public Document load(int id, boolean continou) {
        SentenceFactory factory = new SentenceFactory();
        if (continou) {
            this.addAll(factory.sentenceFactory(source, id));

        }
        this.addAll(factory.sentenceFactory(source, id));
        return this;
    }

    public Document load() {
        return this.load(0, true);
    }

    public String getSourceFile() {
        return source;
    }

    public Document phrasing() {
        Document doc = this;
        doc.stream().forEach((sent) -> {
            sent.phrases(Phraser.phrasing(sent).getPhrases());
        });
        return doc;
    }

}
