/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.cognative;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import saarr_5.Type;
import saarr_5.framework.Entity;
import saarr_5.framework.cognative.verbroot.VerbRoot;
//import saarr_5.framework.cognative.CognaterEntity.Type;
import saarr_5.framework.cognative.verbroot.VerbRoot_v2;
import saarr_5.framework.phraser.Phrase;
import saarr_5.framework.phraser.Phraser;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import saarr_5.utiles.Utile;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.p;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Cognater {

    boolean candidateWord(String tag) {
        return tag.startsWith("noun") && !tag.endsWith("prop")
                || tag.equals("verb")
                || tag.equals("pron")
                || tag.startsWith("adj");

    }

    private static List gerundsRoot(String root) {

        String pathDB = "F:\\Master\\Thesis\\Implementations\\CognateIdentifer\\resources\\Gerunds02.database";
//        long c = -1;
        String line;
        List list = null;
//        pl(root);
        if (root.length() > 4 || root.length() < 3) {
            return null;
        }
        try {
//            long s, e;
//            s = System.nanoTime();
            Optional op = Files.lines(Paths.get(pathDB)).filter(i -> i.split("\\(")[0].trim().equals(root)).findFirst();
            if (op.isPresent()) {
                line = op.get().toString();
                line = line.substring(4);
                line = line.replaceAll(",", " ").replaceAll("\\(", " ").replaceAll("\\)", " ");

                list = Arrays.asList(line.split("\\s"));
            }
//            c = Files.lines(Paths.get(pathDB)).filter(i -> i.split("\\(")[0].trim().equals(root)).count();
//            pl(list);
//            e = System.nanoTime();
//            pl((e - s) / 1000000 + "\tmilis");            
        } catch (IOException ex) {
            Logger.getLogger(Cognater.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private Word scanGerund(Sentence sentence, String root, int index) {

        if (root == null) {
            return null;
        }

        Word gerund;
        String modRoot = root;
        boolean moatal = root.length() == 3 && root.charAt(1) == 'ا';
        modRoot = root.replaceAll("أ|إ", "ء");
        modRoot = moatal ? modRoot.replace("ا", "و") : modRoot;
//        Gerund gerunds = Gerund.gerundsRoot(root);
        List gerunds = gerundsRoot(modRoot);
//        pl(gerunds);
        if (gerunds == null && moatal) {
            modRoot = root.replace("ا", "ي");
            gerunds = gerundsRoot(modRoot);
        }
        if (gerunds == null) {
            return null;
        }
        gerund = scanGerund(sentence, gerunds, index);

        if (gerund == null && moatal) {
            modRoot = root.replace("ا", "ي");
            gerunds = gerundsRoot(modRoot);
            gerund = scanGerund(sentence, gerunds, index);
        }
//        pl(gerunds);
        return gerund;
    }

    boolean checkGerunds(List<String> list, String stem) {
        int c = 0;
        long count;
        list.stream().filter(i -> i.contains(stem)).forEach(System.out::println);
//        if (count.isPresent()) {
//            pl(count.get());
//            
//        }   
//        pl(count);
        return c > 0;
    }

    public Entity cognating2(Sentence sentence) {
        Word v, gerund;
        String root;
        long start, end;
//        VerbRoot roots = new VerbRoot();
        for (int i = 0; i < sentence.size(); i++) {
            v = sentence.get(i);
            if (v.isVerb()) {
//                pl(v);
//                start = System.nanoTime();
                root = VerbRoot.detect(deDiacritic(v.modifiedStem()));
//                end = System.nanoTime();
//                p((end - start) / 1000000 + "  Root , mils");
//                pl(root);
//                start = System.nanoTime();
                gerund = scanGerund(sentence, root, i + 1);
//                pl("Sent id=: " + sentence.id() + "::" + root + " -> " + gerund);
//                end = System.nanoTime();
//                p((end - start) / 1000000 + " Gerand, mils");
                if (gerund != null) {
                    Type type;
                    if (!gerund.isLast()) {

                        Word afterGerund = sentence.get(gerund.index() + 1);
                        type = setType(afterGerund);

                    } else {
                        type = Type.Cognater_EMPHASIS;
                    }

                    return new Entity(v, gerund, type);
                }
            }
        }
        return null;
    }

    public Entity cognating3(Sentence sentence) {
        Word v, gerund;
        String root;
        long start, end;
//        VerbRoot roots = new VerbRoot();
        for (int i = 0; i < sentence.size(); i++) {
            v = sentence.get(i);
            if (v.isVerb()) {

//                start = System.nanoTime();
                root = VerbRoot_v2.detect(deDiacritic(v.root()));
//                end = System.nanoTime();
//                p((end - start) / 1000000 + "  Root , mils");
//                pl(root);
//                start = System.nanoTime();
                gerund = scanGerund(sentence, root, i + 1);

//                end = System.nanoTime();
//                p((end - start) / 1000000 + " Gerund, milis");
//                pl("Sent id=: " + sentence.id() + "::" + root + " -> " + gerund);
                if (gerund != null) {
                    Type type;
                    if (!gerund.isLast()) {

                        Word afterGerund = sentence.get(gerund.index() + 1);
                        type = setType(afterGerund);

                    } else {
                        type = Type.Cognater_EMPHASIS;
                    }

                    return new Entity(v, gerund, type);
                }
            }
        }
        return null;
    }

    private Type setType(Word afterGerund) {
        if (afterGerund != null) {
            if (afterGerund.hasDT()) {
                return Type.Cognater_DESCRIPTIVE;
            } else {
                return Type.Cognater_NUMERICAL;

            }
        }
        return null;
    }

    private void foo() {

        try {
            Files.lines(Paths.get("F:\\Master\\Thesis\\Prototype\\Dataset\\dataset(verbRoots)2.txt")).forEach(i -> gerundsRoot(i));//.forEach(System.out::println);

//        List gerunds = gerundsRoot("كتب");
//        pl(gerunds);
        } catch (IOException ex) {
            Logger.getLogger(Cognater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Word scanGerund(Sentence sentence, List gerunds, int index) {
        if (gerunds == null) {
            return null;
        }
        Word gerundCandidate;
        String stem, modStem;
        for (int i = index; i < sentence.size(); i++) {
            gerundCandidate = sentence.get(i);
            if (gerundCandidate.isNoun()) {
                stem = gerundCandidate.modifiedStem().trim();
                stem = deDiacritic(stem).trim();
                modStem = stem.replaceAll("أ", "ا").replaceAll("إ", "ا");
                if (gerunds.contains(stem) || gerunds.contains(modStem) || gerunds.contains(gerundCandidate.value())) {
                    return gerundCandidate;
                }
            }
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Cognater cog = new Cognater();
//        cog.foo();
        gerundsRoot("ءكل");
    }

}
