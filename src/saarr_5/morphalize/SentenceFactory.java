package saarr_5.morphalize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import saarr_5.utiles.RawData;
import saarr_5.utiles.Utile;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.pl;

public class SentenceFactory {

    public List sentenceFactory(String madaFile) {

        File corpus = new File(madaFile);
        if (!corpus.exists()) {
            pl("Madamira File path error:" + madaFile);
            return null;
        }

        List sentDoc = null;
        try {
            DocumentBuilderFactory dbf;
            DocumentBuilder db;
            Document doc;
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(corpus);
            NodeList sentences = doc.getElementsByTagName("out_seg");
            sentDoc = sentenceNodes(sentences);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(SentenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            pl("Madamira File reading error:" + madaFile);
        }

        return sentDoc;
    }

    public List sentenceFactory(String madaFile, int id) {

        File corpus = new File(madaFile);
        if (!corpus.exists()) {
            pl("Madamira File path error:" + madaFile);
            return null;
        }

        List sentDoc = new ArrayList();
        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Sentence sentence;
        int i = id;
        try {
            DocumentBuilderFactory dbf;
            DocumentBuilder db;
            Document doc;
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(corpus);
            NodeList sentences = doc.getElementsByTagName("out_seg");
            while (i < sentences.getLength()) {
                sentence = (Sentence) es.submit(new SentenceCallable(sentences.item(i))).get();
                sentDoc.add(sentence);
                i++;
            }

//                sentDoc.add((Sentence) es.submit(new SentenceCallable(sentences.item(id))).get());
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(SentenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            pl("Madamira File reading error:" + madaFile);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(SentenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            pl("Full Sentence(" + i + ") error:" + ex);
        }
        es.shutdown();
        return sentDoc;
    }

    public List sentenceFactory2(String madaFile, int id) {

        File corpus = new File(madaFile);
        if (!corpus.exists()) {
            pl("Madamira File path error:" + madaFile);
            return null;
        }

        List sentDoc = new ArrayList();
        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            DocumentBuilderFactory dbf;
            DocumentBuilder db;
            Document doc;
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(corpus);
            NodeList sentences = doc.getElementsByTagName("out_seg");

            sentDoc.add((Sentence) es.submit(new SentenceCallable(sentences.item(id))).get());

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(SentenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            pl("Madamira File reading error:" + madaFile);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(SentenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            pl("Full Sentence(" + id + ") error:" + ex);
        }
        es.shutdown();
        return sentDoc;
    }

    public List sentenceFactoryDemo(String madaFile, int id) {

//        try {
        File corpus = new File(madaFile);
        if (!corpus.exists()) {
            pl("Madamira File path error:" + madaFile);
            return null;
        }

        List sentDoc = new ArrayList();
//        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Sentence sentence;
        int i = 0;
        try {
            DocumentBuilderFactory dbf;
            DocumentBuilder db;
            Document doc;
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(corpus);
            NodeList sentences = doc.getElementsByTagName("out_seg");
            sentences.getLength();
            if (id < 1) {
                while (i < sentences.getLength()) {
                    sentence = new SentenceCallable(sentences.item(i)).cally();
                    sentDoc.add(sentence);
                    i++;
                }
            } else {
                sentence = new SentenceCallable(sentences.item(id)).cally();

                sentDoc.add(sentence);
            }

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(SentenceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sentDoc;
    }

    private List sentenceNodes(NodeList sentences) {
        List<Sentence> list = new ArrayList();

        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Sentence sentence;
        int i = 0;
        try {

            while (i < sentences.getLength()) {
                sentence = (Sentence) es.submit(new SentenceCallable(sentences.item(i))).get();
                list.add(sentence);
//            pl(sentence);
                i++;

            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(SentenceFactory.class.getName()).log(Level.SEVERE, null, ex);
            pl("Full Sentence(" + i + ") error:" + ex);
        }
        es.shutdown();
        return list;

    }

    boolean convertDataToXML() {
        File file = new File(Utile.corpus9);
        RawData data = new RawData(file.getPath());

        data.RawSentencesToxml("./liby/madaConfig.xml",
                file.getParent() + "\\" + file.getName() + "XML.xml");
        return true;

    }

    class SentenceCallable implements Callable {

        Node node;

        SentenceCallable(Node n) {
            node = n;
        }

        @Override
        public Sentence call() {
            if (node == null) {
                return null;
            }
            Sentence sentence = new Sentence();

            String id = node.getAttributes().getNamedItem("id").getNodeValue();
            sentence.setId(Integer.parseInt(id.substring(4)));

            NodeList segments = node.getChildNodes();

            Node preprocess = segments.item(1).getChildNodes().item(1);
            sentence.setRawSentence(preprocess.getTextContent());

            NodeList words = segments.item(3).getChildNodes();
            int i = 0;
            int count = words.getLength();
            int index = 0;
            while (i < count) {
                if (!words.item(i).getNodeName().startsWith("#")) {
                    index = sentence.size();
                    sentence.addAll(wording(words.item(i), index));
                }
                i++;
            }
            sentence.get(sentence.size() - 1).last(true);
            sentence.add(period());
            return sentence;
        }

        private Word period() {
            Word p = new Word();
            p.value(".");
            p.modifiedStem(".");
            p.tokens(".");
            p.tag("punc");
            return p;
        }

        public List<Word> wording(Node wn, int index) {
            if (wn == null) {
                return null;
            }
            String v;
            Word word = new Word();
            List<Word> words = new ArrayList();

            Element element = (Element) wn;
            NodeList list = element.getElementsByTagName("analysis");
            if (list.getLength() < 1) {
                list = element.getElementsByTagName("svm_prediction");
            }
            Node method = list.item(0);
            String methodText = method.getNodeName();
            element = (Element) ((Element) method).getElementsByTagName("morph_feature_set").item(0);

            NamedNodeMap morphSet;// = checkNodeAnalysis(wn);
            morphSet = element.getAttributes();// wn.getChildNodes().item(3).getChildNodes().item(1).getAttributes();

            v = wn.getAttributes().getNamedItem("word").getNodeValue();
            word.value(v);
            v = morphSet.getNamedItem("diac").getNodeValue();
            word.diacValue(v);
            v = methodText.startsWith("analysis") ? morphSet.getNamedItem("gloss").getNodeValue() : null;
            word.setGloss(v);
            v = morphSet.getNamedItem("pos").getNodeValue();
            word.tag(!"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);

            if (word.tag() == null) {
                if (!word.tag().startsWith("punc") || !word.tag().startsWith("digit")) {
                    if (methodText.startsWith("analysis")) {
                        v = morphSet.getNamedItem("stem").getNodeValue();
                    } else {
                        v = word.value();
                    }
                } else {
                    v = null;
                }
            } else {
                v = null;
            }
            word.stem(v != null ? !"na".equals(v) && !"u".equals(v) && !v.isEmpty() ? v : word.value() : null);
            word.modifiedStem(word.stem());
            //---------------- end stem assing
            v = morphSet.getNamedItem("enc0").getNodeValue();
            word.enc0(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("prc0").getNodeValue();
            word.prc0(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("prc1").getNodeValue();
            word.prc1(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("prc2").getNodeValue();
            word.prc2(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("prc3").getNodeValue();
            word.prc3(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("per").getNodeValue();
            word.per(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("num").getNodeValue();
            word.num(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("asp").getNodeValue();
            word.asp(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("vox").getNodeValue();
            word.vox(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("gen").getNodeValue();
            word.gen(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("cas").getNodeValue();
            word.cas(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("tr") != null ? morphSet.getNamedItem("tr").getNodeValue() : null;
            word.tr(v != null && !"na".equals(v) && !"u".equals(v) && !v.equals("0") && !v.isEmpty() && !v.equals("0") ? v : null);
            v = morphSet.getNamedItem("root") != null ? morphSet.getNamedItem("root").getNodeValue() : null;
            word.root(v);

            word.isLetter = false;
            word.last(false);
//            deClitic(word);
//            word.modifyStem();
//            v = wn.getAttributes().getNamedItem("id").getNodeValue();
            word.setIndex(index);
            List<Word> clitics = cliticing(word);
            if (clitics != null && !clitics.isEmpty()) {
                words.addAll(clitics);
                index = index + clitics.size();
            }
            word.tokens(this.tokenize(word));
            word.setIndex(index);
            words.add(word);
            return words;
        }

        void deClitic(Word word) {

            word.prc3Val(word.hasPrc3() ? word.getprc3() : null);
            word.prc2Val(word.hasPrc2() ? word.getprc2() : null);
            word.prc1Val(word.hasPrc1() ? word.getprc1() : null);
            word.prc0Val(word.hasPrc0() && !word.prc0().contains("det") ? word.getprc0() : null);
            word.tag(word.hasDT() && !word.tag().endsWith("prop") ? word.tag() + "_det" : word.tag());
            word.encVal(word.hasEnc() ? word.encliticing() : null);
        }

        String tokenize(Word word) {

            String toks = "";
//            toks = word.prc3Val() != null ? word.prc3Val() + "-" : "";
//            toks += word.prc2Val() != null ? word.prc2Val() + "-" : "";
//            toks += word.prc1Val() != null ? word.prc1Val() + "-" : "";
//            toks += word.prc0Val() != null ? word.prc0Val() + "-" : "";

            toks += word.modifiedStemDet();

            toks += word.encVal() != null ? "-" + word.encVal() : "";

            return toks;
        }

        private List<Word> cliticing(Word word) {
            List<Word> clitics = new ArrayList();
            int index = word.index();
            Word clitic;
            String prc;
            if (word.hasPrc3()) {
                clitic = new Word();
                prc = word.getprc3();
                word.prc3Val(prc);
                clitic.isLetter = true;
                clitic.value(deDiacritic(prc));
                clitic.diacValue(prc);
                clitic.tokens(clitic.value());

                clitic.tag("ques");
                clitic.setIndex(index);
                index++;
                clitics.add(clitic);
            }
            if (word.hasPrc2()) {
                clitic = new Word();
                prc = word.getprc2();
                word.prc2Val(prc);
                clitic.isLetter = true;
                clitic.value(deDiacritic(prc));
                clitic.diacValue(prc);
                clitic.tokens(clitic.value());
                clitic.tag("conj");
                clitic.setIndex(index);
                index++;
                clitics.add(clitic);
            }
            if (word.hasPrc1()) {
                clitic = new Word();
                prc = word.getprc1();
                word.prc1Val(prc);
                clitic.isLetter = true;
                clitic.value(deDiacritic(prc));
                clitic.diacValue(prc);
                clitic.tag(clitic.value().startsWith("و") || clitic.value().startsWith("ف") ? "conj"
                        : clitic.value().startsWith("س") ? "fut"
                                : clitic.value().startsWith("ي") ? "voc" : "prep");
                clitic.tokens(clitic.value());
                clitic.setIndex(index);
                index++;
                clitics.add(clitic);
            }
            if (word.hasPrc0() && !word.prc0().contains("det")) {
                clitic = new Word();
                prc = word.getprc0();
                word.prc0Val(prc);
                clitic.isLetter = true;
                clitic.value(deDiacritic(prc));
                clitic.diacValue(prc);
                clitic.tokens(clitic.value());
                clitic.tag("part_neg");
                clitic.setIndex(index);
                index++;
                clitics.add(clitic);
            }
            word.tag(word.hasDT() && !word.tag().endsWith("prop") ? word.tag() + "_det" : word.tag());
            word.encVal(word.hasEnc() ? word.encliticing() : null);
            word.modifyStem();
            return clitics;
        }

        public Sentence cally() {
            if (node == null) {
                return null;
            }
            Sentence sentence = new Sentence();

            String id = node.getAttributes().getNamedItem("id").getNodeValue();
            sentence.setId(Integer.parseInt(id.substring(4)));

            NodeList segments = node.getChildNodes();

            Node preprocess = segments.item(1).getChildNodes().item(1);
            sentence.setRawSentence(preprocess.getTextContent());

            NodeList words = segments.item(3).getChildNodes();
            int i = 0;
            int count = words.getLength();
            int index = 0;
            while (i < count) {
                if (!words.item(i).getNodeName().startsWith("#")) {
                    index = sentence.size();
                    sentence.addAll(wording(words.item(i), index));
                }
                i++;
            }
            return sentence;
        }

    }

}
