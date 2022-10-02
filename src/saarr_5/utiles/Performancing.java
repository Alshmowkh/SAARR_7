/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.utiles;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import saarr_5.Document;
import saarr_5.Document2;
import saarr_5.framework.Entity;
import saarr_5.framework.cognative.DetectorCognate;
import saarr_5.morphalize.SentenceFactory;

import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Performancing {

    String path;
    Document doc;

    void ini() {
        //initialization step
        long start, end, sum;

        start = System.nanoTime();

//        doc = new Document(Utile.corpus9, 0);
        doc = new Document(load(0, false));

        end = System.nanoTime();
        pl((end - start) / 1000000 + "  millisecond");

        start = System.nanoTime();

//        doc = doc.syntaxProcessing();
        cognate();
        end = System.nanoTime();
        pl((end - start) / 1000000 + "  millisecond");

        pl(doc.size());
    }

    void cognate() {

        doc = new Document(load(288, false));
        doc.stream().forEach(i -> {
            Entity entity = new DetectorCognate(i).getEntity();

            pl("Sent id=: " + i.id() + " :: " + entity);

//            next();
        });
    }

    public static void main(String[] args) {
        Performancing test = new Performancing();
//        test.cognate();
        test.ini();
    }

    List load() {
        SentenceFactory factory = new SentenceFactory();
        return factory.sentenceFactory("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-1.xml");
    }

    List load(int id, boolean continou) {
        SentenceFactory factory = new SentenceFactory();
        if (continou) {
            return factory.sentenceFactory("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-2.xml", id);

        }
        return factory.sentenceFactory2("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-2.xml", id);

    }
}
