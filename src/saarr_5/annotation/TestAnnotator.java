/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.annotation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import saarr_5.Document;
import saarr_5.framework.ESFframework;
import saarr_5.framework.Entity;
import saarr_5.framework.Objector;
import saarr_5.framework.Subjector;
import saarr_5.framework.annexed_genitive.DetectorIdafa;
import saarr_5.framework.circumstance.DetectorCircumstance;
import saarr_5.framework.cognative.DetectorCognate;
import saarr_5.framework.inchoative_predicate.DetectorIP;
import saarr_5.framework.inchoative_predicate.RelativeClause;
import saarr_5.framework.phraser.Phrase;
import saarr_5.morphalize.Morphalizer;
import saarr_5.framework.phraser.Phraser;
import saarr_5.framework.semantic.SemanticLayer;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.SentenceFactory;
import saarr_5.morphalize.Word;
import saarr_5.utiles.RawData;
import saarr_5.utiles.Utile;
import static saarr_5.utiles.Utile.next;
import static saarr_5.utiles.Utile.p;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class TestAnnotator {

    Document doc;

    void testIslastFunction() {
        //-------------------test islast fuction-----------
        doc = new Document(load(0, true));
        doc.stream().forEach(i -> {
            Sentence s = (Sentence) i;
            pl(s);
            s.stream().filter(w -> w.isLast()).forEach(System.out::println);
        });
    }

    void testOneSentence() {
        String rawSent = "وباستهدافه المدنيين سيتم محاسبته أمام القضاء.";
        Sentence morphed = Morphalizer.morphalize(rawSent);
//        doc = new Document("F:\\Master\\Thesis\\Prototype\\Dataset\\CircumstanceDataMada.xml", 0);

        Sentence sentence = morphed;
        new Utile().printSentence(sentence);

        (Phraser.phrasing((Sentence) sentence)).getPhrases().stream().forEach(i -> {
            Phrase phrase = (Phrase) i;

            p(phrase.begins());
            p(phrase);
            pl(phrase.ends());
//                next();
        });
        next();

    }

    void morphology() {
        String rawSent = "أحمد أسد.";
        List morphed;
        Sentence tokens = Morphalizer.morphalize(rawSent);
//        morphed = Morphalizer.morphalize(new File(Utile.corpus1));
//         tokens = Morphalizer.morphalize(Utile.corpus1, 32);

//        morphed.forEach(i -> {
//            pl(i);
//            next();
//        });
        pl(tokens);
// pl(((Word)tokens.get(2)).prc1()); pl(((Word)tokens.get(2)).prc0()); pl(((Word)tokens.get(2)).enc0());
    }

    void morphology(String rawSentence
    ) {

        List morphed;
        Sentence tokens = Morphalizer.morphalize(rawSentence);
//        morphed = Morphalizer.morphalize(new File(Utile.corpus1));
//         tokens = Morphalizer.morphalize(Utile.corpus1, 32);

//        morphed.forEach(i -> {
//            pl(i);
//            next();
//        });
        new Utile().printOutseg(tokens);
// pl(((Word)tokens.get(2)).prc1()); pl(((Word)tokens.get(2)).prc0()); pl(((Word)tokens.get(2)).enc0());
    }

    void phraser() {
        long start, end;
        start = System.nanoTime();
        String source = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.1.xml";
//        List tokens = Morphalizer.morphalize(Utile.corpus1, 31);
//        end = System.nanoTime();
//        pl((end - start) / 1000000 + "  millisecond");
//        pl(tokens);
//
////        tokens.forEach(i ->pl(((Word)i).diacValue()));
//        start = System.nanoTime();
//        Sentence phrases = Phraser.phrasing((Sentence) tokens);
//        end = System.nanoTime();
//        pl((end - start) / 1000000 + "  millisecond");
//        phrases.getPhrases().forEach(i -> {
//            Phrase phrase = (Phrase) i;
//            p(phrase.begins());
//            p(phrase);
//            pl(phrase.ends());
//
//        });

//        start = System.nanoTime();
//        List morphed = Morphalizer.morphalize(new File(Utile.corpus11), 0);
//        doc = new Document("F:\\Master\\Thesis\\Prototype\\Dataset\\CircumstanceDataMada.xml", 0);
//        doc = new Document(load(0, false));
        doc = new Document(source).load();
//        List phrases = new ArrayList();
//        start = System.nanoTime();

        doc = doc.phrasing();
//        end = System.nanoTime();
//        pl((end - start) / 1000000 + "  millisecond");
//        pl((doc.get(350)).getPhrases());
        new Annotator().phrases(doc);

    }

    void miss() {
        long start, end;

        String str = "lsk_df\bs_dfsdswwf";
        pl(str.lastIndexOf("_"));
        pl(str.substring(0, str.lastIndexOf("_") + 2));
//        start = System.nanoTime();
//        List tokens = Morphalizer.morphalize(Utile.corpus1, 31);
//        end = System.nanoTime();
//        pl((end - start) / 1000000 + "  millisecond");
//        pl(tokens);
//
////        tokens.forEach(i ->pl(((Word)i).diacValue()));
//        start = System.nanoTime();
//        Sentence phrases = Phraser.phrasing((Sentence) tokens);
//        end = System.nanoTime();
//        pl((end - start) / 1000000 + "  millisecond");
//        phrases.getPhrases().forEach(i -> {
//            Phrase phrase = (Phrase) i;
//            p(phrase.begins());
//            p(phrase);
//            pl(phrase.ends());
//
//        });

//        start = System.nanoTime();
////        List morphed = Morphalizer.morphalize(new File(Utile.corpus11), 0);
////        doc = new Document("F:\\Master\\Thesis\\Prototype\\Dataset\\CircumstanceDataMada.xml", 0);
//        doc = new Document(load(0, true));
//        end = System.nanoTime();
//        pl((end - start) / 1000000 + "  millisecond");
//
//        doc.stream().forEach(sent -> {
//            Sentence sentence = sent;
////            new Utile().printSentence(sentence);
//            pl("sentID=" + (sentence.id()));
//            sent.stream().forEach(i -> {
//                Word w = (Word) i;
////                p("\t"+w);
//                if (w.isPousdoVerb()) {
//                    p("  "+w);
//                    
//                }
//
////                next();
//            });
////        Morphalizer.morphalize(new File(Utile.corpus2), 0).stream().forEach(phrase -> {
////            (Phraser.phrasing(phrase)).stream().forEach(i -> pl(i));
////            next();
//        });
////      
//      
    }

    void ipDetector() {
//        String rawSent = "محمد فوق الشجرة.";
//        List<Sentence> morphed = new ArrayList();
//        tokens.add(Morphalizer.morphalize(rawSent));
//        tokens = Morphalizer.morphalize(new File(Utile.corpus1), 90);
//        morphed.add(Morphalizer.morphalize(Utile.corpus1, 0));
//        ESFframework framework = new ESFframework(tokens);
//        List ends = framework.getSimileEnds();
        doc = new Document(load(24, false));

        doc.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p )+" : "+((Phrase) p).getTypeTB()));
            Entity entity = new DetectorIP(i).getEntityESF();

            if (entity != null) {
                pl("SentID=" + i.id() + "-" + entity.getAlpha().index() + "-" + entity.getBeta().index()
                        + ":" + entity.getAlpha()
                        + "  " + entity.getBeta()
                        + " " + entity.getType().trim());
            }
//            pl(new Inchoative(i).getHead());
//            pl(new RelativeClause(i).getPredicate());
//            next();
        });
    }

    void idafa() {
//        List<Sentence> morphed = new ArrayList();
//        tokens.add(Morphalizer.morphalize(rawSent));
//        morphed = Morphalizer.morphalize(new File(Utile.corpus1), 0);
//        tokens.add(Morphalizer.morphalize(Utile.corpus4, 1));
//        ESFframework framework = new ESFframework(tokens);
//        List ends = framework.getSimileEnds();
        doc = new Document(load(0, false));
        doc.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
//            Entity entity=new DetectorIdafa(i).getEntity();
            Entity entity = new DetectorIdafa(i).getEntitiesESF();
            if (entity != null) {
                pl("SentID=" + i.id() + "-" + entity.getAlpha().index() + "-" + entity.getBeta().index()
                        + ":" + entity.getAlpha()
                        + "  " + entity.getBeta()
                        + " " + entity.getType().trim());
            }
//            next();
        });
    }

    void cognate() {
//        List<Sentence> morphed = new ArrayList();
//        tokens.add(Morphalizer.morphalize(rawSent));
//        morphed = Morphalizer.morphalize(new File(Utile.corpus6), 0);
//        morphed.add(Morphalizer.morphalize(Utile.corpus6, 5));
//        ESFframework framework = new ESFframework(tokens);
//        List ends = framework.getSimileEnds();

        doc = new Document(load(325, false));
        doc.stream().forEach(i -> {
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
            Entity entity = new DetectorCognate(i).getEntityESF();
            if (entity != null) {
                pl("SentID=" + i.id() + "-" + entity.getAlpha().index() + "-" + entity.getBeta().index()
                        + ":" + entity.getAlpha()
                        + "  " + entity.getBeta()
                        + " " + entity.getType().trim());
            }
//            if (entity != null) {
//                pl("Sent id=: " + i.id() + " :: " + entity.getBeta().hasDT());
//            }
//            next();
        });
    }

    void cognateExtend() {
//        List<Sentence> morphed = new ArrayList();
//        tokens.add(Morphalizer.morphalize(rawSent));
//        morphed = Morphalizer.morphalize(new File(Utile.corpus6), 0);
//        morphed.add(Morphalizer.morphalize(Utile.corpus6, 5));
//        ESFframework framework = new ESFframework(tokens);
//        List ends = framework.getSimileEnds();
        doc = new Document(load(280, true));
        doc.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
            Entity entity = new DetectorCognate(i).getEntityESF();

            if (entity != null) {
                pl("SentID=" + i.id() + "-" + entity.getAlpha().index() + "-" + entity.getBeta().index()
                        + ":" + entity.getAlpha()
                        + "  " + entity.getBeta()
                        + " " + entity.getType().trim());
            }

            next();
        });
    }

    void subject() {
//        List<Sentence> morphed = new ArrayList();
//        String rawSent
//                = "أحمد يأكل تفاح.";
////        tokens.add(Morphalizer.morphalize(rawSent));
//        morphed = Morphalizer.morphalize(new File(Utile.corpus6), 0);
////        morphed.add(Morphalizer.morphalize(Utile.corpus6, 2,true));
////        ESFframework framework = new ESFframework(tokens);
////        List ends = framework.getSimileEnds();
//        morphed.stream().forEach(i -> {
////            new Utile().printSentence(i);
////            pl(i);
////            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
//            pl(new SubjectIdentify(i).subject());
//            next();
//        });
        doc = new Document(load(0, true));
        doc.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
//            pl("----------------------------");
            Word object = new Subjector(i).subject();
            pl("Sent id=: " + i.id() + " :: " + object);

//            next();
        });
    }

    void object() {
        doc = new Document(load(601, false));
        doc.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
//            pl("----------------------------");
            Word object = new Objector(i).object();
            pl("Sent id=: " + i.id() + " :: " + object);

//            next();
        });
    }

    void circumstance() {
        /*
         تركت الكتاب مفتوحا / مفتوحا يعتبر حال وفي نفس الوقت صفه
         */
//        List<Sentence> morphed = new ArrayList();
//        tokens.add(Morphalizer.morphalize(rawSent));
//        morphed = Morphalizer.morphalize(new File(Utile.corpus7), 1);
//        morphed.add(Morphalizer.morphalize(Utile.corpus7, 1));
//        ESFframework framework = new ESFframework(tokens);
//        List ends = framework.getSimileEnds();
        doc = new Document(load(0, true));
        doc.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
//            pl("----------------------------");
            Entity entity = new DetectorCircumstance(i).getEntity();
            if (entity != null) {
                pl("SentID=" + i.id() + "-" + entity.getAlpha().index() + "-" + entity.getBeta().index()
                        + ":" + entity.getAlpha()
                        + "  " + entity.getBeta()
                        + " " + entity.getType().trim());
            }

//            next();
        });
    }

    void syntaxLayer() {
//        List entities = new ArrayList();

        String rawSent = "محمد أسد.";
        List<Sentence> morphed = new ArrayList();
//        morphed.add(Morphalizer.morphalize(rawSent));
        morphed = Morphalizer.morphalize(new File(Utile.corpus8), 0);
//        morphed.add(Morphalizer.morphalize(Utile.corpus8, 2));
//      
//       
        morphed.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
            ESFframework framework = new ESFframework(i);
            List ends = framework.getCandidateSimileEnds();
            SemanticLayer semantic = new SemanticLayer(ends);

            ends = semantic.getESF();
            pl(ends);
            next();
        });

    }

    void semanticLayer() {
//        List entities = new ArrayList();

//        String rawSent = "محمد كسلان.";
//        List<Sentence> morphed = new ArrayList();
//        morphed.add(Morphalizer.morphalize(rawSent));
//        morphed = Morphalizer.morphalize(new File(Utile.corpus8), 0);
//        morphed.add(Morphalizer.morphalize(Utile.corpus8, 1));
//      
        doc = new Document(load(0, true));
        doc.parallelStream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
            ESFframework framework = new ESFframework(i);
            List ends = framework.getCandidateSimileEnds();
//            SemanticLayer semantic = new SemanticLayer(ends);
//pl(ends);
//            ends = semantic.getESF();
//             Statistics.creator(i);
            if (ends != null) {
                pl("Sent id=: " + i.id() + " :: " + ends);
            }

//            next();
        });
    }

    void awn() {
        String str = "الطبيب حلاق.";
        str = "نور قمر.";
//        List<Sentence> morphed = new ArrayList();
//        morphed.add(Morphalizer.morphalize(str));
//        Word w1 = morphed.get(0).get(0);
//        Word w2 = morphed.get(0).get(1);

//        pl(word.tag()+"\t:"+word.gloss());
        doc = new Document(load(279, true));
        doc.stream().forEach(i -> {

            Sentence sent = (Sentence) i;
            pl("Sent" + sent.id() + ":");
            ESFframework framework = new ESFframework(sent);
            List ends = framework.getCandidateSimileEnds();
            framework.getESF();
//            SemanticLayer semantic = new SemanticLayer(ends);
//
//            semantic.getESF();
//
//            pl(sent.rawSentence());
//            pl(sent.IP_AG_CG_CI());
//            if (entity != null) {
            Entity entity;// = framework.getESF();
            entity = null;
            if (entity != null) {
                pl("SentID=" + sent.id() + ":" + entity.getAlpha().root() + "==" + entity.getAlphaOntoPath()
                );

                pl("\t\t:" + entity.getBeta().root()
                        + "==" + entity.getBetaOntoPath()
                );
            }

////            }
//            Utile.printESF(entity);
//            pl("Sent" + sent.id() + ":" + sent.isRetorical());
////            next();
        });
//        AWN awn = new AWN();
////        pl(w1 + "\t" + w2);
//        List levels = awn.getOntoPath(w1);
//        pl(levels);
//        levels = awn.getOntoPath(w2);
//
////        for (Object s : levels) {
////            str = awn.getTranslation(s.toString(), "Arabic");
////           pl(Transliterator.translitrate(str.substring(0, str.lastIndexOf("_"))));
////        }
//        pl(levels);
////        pl(awn.getOntoPath(w2));
    }

    void integration() {
//        Document doc; //new Document(Utile.corpus10, 0);
//        doc = new Document(load(352,false));
//        doc = doc.processing();
//        doc = doc.syntaxProcessing();

//        doc = doc.morphlize();
//        new Utile().printSentence(doc.getSentence(0));
//        doc = doc.syntaxProcessing();
//        doc = doc.semanticProcessing();
//        doc = doc.processing();
//        List entities = new ArrayList();
//        String rawSent = "محمد كسلان.";
//        Sentence morphed = doc.getSentence(0);
//        pl(doc.size());
////        morphed.add(Morphalizer.morphalize(rawSent));
////        morphed = Morphalizer.morphalize(new File(Utile.corpus8), 0);
//        morphed.add(Morphalizer.morphalize(Utile.corpus8, 1));
        doc = new Document(load(32, false));
//        doc = doc.processing();
        doc.stream().forEach(i -> {
            Sentence sent = (Sentence) i;
////            new Utile().printSentence(i);
////            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
            ESFframework framework = new ESFframework(sent);
            List ends = framework.getCandidateSimileEnds();
//            framework.getESF();
//            SemanticLayer semantic = new SemanticLayer(ends);
//
//            semantic.getESF();
//
//            pl(sent.rawSentence());
//            pl(sent.IP_AG_CG_CI());
//            if (entity != null) {
            Entity entity = framework.getESF();

//            pl("SentID=" + sent.id() + ":" + entity.getAlpha().modifiedStem() + "==" + entity.getAlpha().ontoPath()
//                    + "  ::  " + entity.getBeta().modifiedStem()
//                    + "==" + entity.getBeta().ontoPath()
//            );
////            }
//            Utile.printESF(entity);
            pl("Sent" + sent.id() + ":" + sent.isRetorical());
//////            next();
        });
//        Statistics.creator(doc);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestAnnotator test = new TestAnnotator();
//        test.testIslastFunction();
//        File_XML xml=new File_XML("F:\\Master\\Thesis\\Prototype\\Dataset\\Corpus-3.1.xml","F:\\Master\\Thesis\\Prototype\\Dataset\\Corpus-3.1.0.xml");
//        xml.insertClausesToMadaFile();
//        test.miss();

        test.testOneSentence();
//        test.relativeClause();
//        test.load(17);
//        test.sentencesFromMada();
//        test.convertDataToXML();
//        test.integration();
//        test.awn();
//        test.semanticLayer();
//        test.syntaxLayer();
//        test.morphology("ستستمتعون في الرحلة.");
//        test.phraser();
//        test.ipDetector();
//        test.idafa();
//        test.cognate();
//        test.cognateExtend();
//        test.subject();
//        test.object();
//        test.circumstance();
    }

    void relativeClause() {
        Sentence sentence = new Document(load(18, false)).get(0);
        RelativeClause rc = new RelativeClause(sentence);
        pl(rc.getRight());
        pl(rc.getPronoun());

        pl(rc.getLeft());

    }

    boolean convertDataToXML() {
        File file = new File(Utile.corpus11);
        RawData data = new RawData(file.getPath());

        data.RawSentencesToxml("./liby/madaConfig.xml",
                file.getParent() + "\\" + file.getName() + "XML.xml");
        return true;
    }

    void sentencesFromMada() {
        SentenceFactory factory = new SentenceFactory();
        factory.sentenceFactory("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-1.xml");
    }

    List load(int id, boolean continou) {
        SentenceFactory factory = new SentenceFactory();
        if (continou) {
            return factory.sentenceFactory("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.1.xml", id);

        }
        return factory.sentenceFactory2("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.1.xml", id);

    }

    List loadDemo(int id, boolean continou) {
        SentenceFactory factory = new SentenceFactory();
        if (continou) {
            return factory.sentenceFactoryDemo("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.1.xml", id);

        }
        return factory.sentenceFactoryDemo("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.1.xml", id);

    }
}
