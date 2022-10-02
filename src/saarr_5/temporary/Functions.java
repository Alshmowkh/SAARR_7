/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.temporary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import saarr_5.Document;
import saarr_5.framework.cognative.verbroot.VerbRoot_v2;

import saarr_5.morphalize.Letter;
import saarr_5.morphalize.SentenceFactory;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.next;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Functions {
    
    void sentenceCategory() {
        Document doc = new Document(load(0, true));
        doc.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
            int j = 0, N = 0, V = 0;
            Word fw = i.get(j);
            String tag = fw.tag();
            while (new Letter().abolishers().contains(deDiacritic(fw.value())) || tag.startsWith("adv")) {
                j++;
                fw = i.get(j);
                tag = fw.tag();
            }
            if (tag.startsWith("verb")) {
                V++;
            } else {
                N++;
            }
//            pl("Sent id=: " + i.id() + " :: " + fw + " --> " + tag);
            pl("Verbs= " + V + " , Nominal=" + N);
//            next();
        });
        
    }
    
    void reduceCorpus() {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-4.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-5.xml";
        String line, temp;
        String Lines = null;
        StringBuilder lines = new StringBuilder();
        String svm;
        List list = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(oldF));
//            br.close();
            int i = 0;
//            line=br.readLine();
//            list.add(line);
////            i++;
//            br.readLine();
//            list.add(line);
////            i++;
            line = br.readLine();
            list.add(line + System.lineSeparator());
            i++;
//            Lines = line;

            while ((line = br.readLine()) != null) {
//                pl(line);
//                lines.append(lines);
//                sent=br.readLine();
//                line = br.readLine();
                i++;
                svm = line;
                if (svm.trim().startsWith("<svm_prediction>")) {
                    String morph = br.readLine();
                    i++;
                    String svmC = br.readLine();
                    i++;
                    line = br.readLine();
                    i++;
                    if (!line.trim().startsWith("<analysis rank=")) {
                        list.add(svm + System.lineSeparator());
                        list.add(morph + System.lineSeparator());
                        list.add(svmC + System.lineSeparator());
                    }
                }
//                line = br.readLine();
//                i++;
                list.add(line + System.lineSeparator());
//                Lines += line + System.lineSeparator();
            }
            br.close();
        } catch (Exception ex) {
            
        }
        pl(list.size());
        try {
            FileWriter fw = new FileWriter(newF);
            fw.write(list.toString());
        } catch (IOException ex) {
            
        }
    }
    
    void addAttribute() {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-5.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.xml";
        String feature;
        String line;
        List list = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(oldF));
            line = br.readLine();
            list.add(line + System.lineSeparator());
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("<morph_feature_set")) {
                    line = line.substring(0, line.lastIndexOf("/"));
                    if (line.contains("stem=")) {
                        feature = line.split("stem=")[1];
                        feature = feature.substring(0, feature.lastIndexOf("\"") + 1);
                    } else {
                        feature = line.split("diac=")[1];
                        feature = feature.substring(0, feature.indexOf("lemma") - 1);
                    }
                    line = line + " root=" + feature + "/>";
                }
                list.add(line + System.lineSeparator());
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        pl(list.size());
        try {
            FileWriter fw = new FileWriter(newF);
            fw.write(list.toString());
            fw.close();
        } catch (IOException ex) {
            
        }
    }
    
    void readAllLines() {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-4.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-5.xml";
        List lines = null;
        try {
            lines = Files.readAllLines(Paths.get(oldF));
        } catch (IOException ex) {
            
        }
//        lines.
        pl(lines);
    }
    
    void addTaaMarboota() {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.1.xml";
        String feature;
        String line;
        List list = new ArrayList();
        String diac = "", stem = "", root = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(oldF));
            line = br.readLine();
            list.add(line + System.lineSeparator());
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("<morph_feature_set")) {
                    if (line.contains("stem=")) {
                        diac = line.substring(line.indexOf("diac") + 6, line.indexOf("lemma") - 2);
                        if (deDiacritic(diac).endsWith("ة")) {
                            root = diac;
                            if (root.startsWith("ال")) {
                                root = diac.substring(2);
                            }
                            line = line.substring(0, line.lastIndexOf("stem="));
                            line = line + "stem=\"" + root + "\" root=\"" + root + "\"/>";
                        }
                    }
                }
                list.add(line + System.lineSeparator());
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        pl(list.size());
        try {
            FileWriter fw = new FileWriter(newF);
            fw.write(list.toString());
            fw.close();
        } catch (IOException ex) {
            
        }
    }
    
    void getAlldatasetVerbs() {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.1.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Dataset\\Dataset(verbs).txt";
        String feature;
        String line;
        List list = new ArrayList();
        String diac = "", pos = "", root = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(oldF));
            line = br.readLine();
            list.add(line + System.lineSeparator());
            while ((line = br.readLine()) != null) {
                
                if (line.contains("pos=")) {
                    diac = line.substring(line.indexOf("diac") + 6, line.indexOf("lemma") - 2);
                    pos = line.substring(line.indexOf("pos=") + 5, line.indexOf("prc3=") - 2);
                    if (pos.trim().equals("verb")) {
//                        root = diac;
//                        if (root.startsWith("ال")) {
//                            root = diac.substring(2);
//                        }
//                        line = line.substring(0, line.lastIndexOf("stem="));
//                        line = line + "stem=\"" + root + "\" root=\"" + root + "\"/>";
                        pl(diac);
                    }
                    
                }
            }
            list.add(line + System.lineSeparator());
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        pl(list.size());
        try {
            FileWriter fw = new FileWriter(newF);
//            fw.write(list.toString());
            fw.close();
        } catch (IOException ex) {
            
        }
    }
    
    void getVerbsRoots() {
        String verbsFile = "F:\\Master\\Thesis\\Prototype\\Dataset\\dataset(verbs)3.txt";
        
        try {
            Files.lines(Paths.get(verbsFile)).map(i -> VerbRoot_v2.detect(i)).forEach(System.out::println);
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        Functions p = new Functions();
        p.getVerbsRoots();
//        p.getAlldatasetVerbs();
//        p.addTaaMarboota();
//        p.readAllLines();
//        p.sentenceCategory();
//        p.reduceCorpus();
//        p.addAttribute();
    }
    
    List load(int id, boolean continou) {
        SentenceFactory factory = new SentenceFactory();
        if (continou) {
            return factory.sentenceFactory("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-2.xml", id);
            
        }
        return factory.sentenceFactory2("F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-2.xml", id);
        
    }
    
}
