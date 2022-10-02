package saarr_5.utiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import saarr_5.framework.Entity;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;

public class Utile {

    String filePath;
    public static final String corpus1 = "F:\\Master\\Thesis\\Implementations\\IP_Detector\\IP\\simple_corpus_NS.txt";
    public static final String corpus2 = "F:\\Master\\Thesis\\Implementations\\PronParsing\\Shom_lib\\simple_corpus.txt";
    public static final String corpus3 = "F:\\Master\\Thesis\\Implementations\\SAARR_4\\Rhe_lib\\Corpus\\Corpus 08\\raw data\\Samsung news.txt";
    public static final String corpus4 = "F:\\Master\\Thesis\\Implementations\\Annexed_Genitive\\Shom_lib\\simple_corpus.txt";
    public static final String corpus5 = "F:\\Master\\Thesis\\Prototype\\Papers\\Indentification of Arabic Cognate (or Unrestarined Verb)\\Subject ID corpus.txt";
    public static final String corpus6 = "F:\\Master\\Thesis\\Prototype\\Papers\\Indentification of Arabic Cognate (or Unrestarined Verb)\\Simple congnator corpus.txt";
    public static final String corpus7 = "F:\\Master\\Thesis\\Prototype\\Papers\\Circumstance Detecting in Arabic Sentence\\Circumstance corpus.txt";
    public static final String corpus8 = "F:\\Master\\Thesis\\Implementations\\Rhe_Processes\\Rhe_lib\\Corpora 02\\ESF_Examples.txt";
    public static final String corpus9 = "F:\\Master\\Thesis\\Prototype\\Dataset\\Dataset_v_02";
    public static final String corpus10 = "F:\\Master\\Thesis\\Prototype\\Dataset\\Dataset_v_003.txt";
    public static final String corpus11 = "F:\\Master\\Thesis\\Prototype\\Dataset\\CircumstanceData.txt";

    public static boolean isFile(String path) {
//        if (path.charAt(path.length() - 4) == '.' || path.charAt(path.length() - 3) == '.' || path.charAt(path.length() - 2) == '.') {
//            return true;
//        }
//        return false;
        return new File(path).isFile();
    }

    public static String createFile() {
        String path = "liby/statistics/statistic";
        String newPath = path;
        int _new = 0;
        while (new File(newPath + ".html").exists()) {
            newPath = path + _new++;
        }
        return newPath + ".html";
    }

    public void setFilePath(Object path) {
        if (path instanceof String) {
            filePath = (String) path;
        } else if (path instanceof File) {
            filePath = ((File) path).getAbsolutePath();
        }
    }

    public String sentence(int num) {
        if (filePath == null) {
            pl("You must set file path befor...");
            return null;
        }
        BufferedReader reader;
        String sentence = null;
        try {

            reader = new BufferedReader(new FileReader(filePath));

            int line = 0;

            while ((sentence = reader.readLine()) != null) {
                line++;
                if (num == line) {
                    reader.close();
                    break;
                }
            }
        } catch (Exception e) {

        }
        return sentence;
    }

    public List<String> sentences() {
        StringBuilder stringBuilder = new StringBuilder();
        if (filePath == null) {
            pl("You must set file path before...");
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = null;
            String ls = System.getProperty("line.separator");

            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

            } finally {
                reader.close();
            }
        } catch (Exception e) {

        }
        List<String> pureSent = new ArrayList<>();
        String[] sentences = stringBuilder.toString().split("\\.|:|\n|\\?");
        for (String sentence : sentences) {
            String sent = sentence.trim();
            if (!"".equals(sent)) {
                pureSent.add(sent + ".");
            }
        }
        return pureSent;
    }

    public static List<Character> diacriticList() {
        Character[] diacritics = new Character[]{'َ', 'ِ', 'ُ', 'ْ', 'ّ', 'ً', 'ٍ', 'ٌ'};
        return Arrays.asList(diacritics);

    }

    public static boolean isDiacritic(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (diacriticList().contains(word.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String deDiacritic(String term) {
        String res = "";
        if (term != null) {
            char[] chars = term.toCharArray();

            for (Character ch : chars) {
                if (diacriticList().contains(ch)) {
                    continue;
                } else if (ch.equals('ٱ')) {
                    ch = 'ا';
                }
                res = res + ch;
            }
        }

        return res;
    }

    public static void pl(Object o) {
        System.out.println(o);
    }

    public static void p(Object o) {
        System.out.print(o);
    }

    void pt(Object o) {
        System.out.print(o + "\t");
    }

    public static void next() {
        try {
            char c = (char) System.in.read();
            if (c == 'q' || c == '0') {
                System.exit(1);
            }
        } catch (Exception e) {

        }

    }

    public void printSentence(Sentence sentence) {
        Word w;
        Iterator<Word> itr = sentence.iterator();
        while (itr.hasNext()) {
            w = itr.next();
            p("  value :" + w.value());
            p("  diac :" + w.diacValue());
            p("  stem :" + w.stem());
            p("  modstem :" + w.modifiedStem());
            p(" tag:" + w.tag());
            p(" per:" + w.per());
            p(" num:" + w.num());
            p(" gen:" + w.gen());
            p(" vox:" + w.vox());
            p(" cas:" + w.cas());
            p(" asp:" + w.asp());

            p(" clitics[" + (w.prc3Val() != null ? w.prc3Val() : w.prc3() != null ? w.prc3() : "") + ","
                    + (w.prc2Val() != null ? w.prc2Val() : w.prc2() != null ? w.prc2() : "") + ","
                    + (w.prc1Val() != null ? w.prc1Val() : w.prc1() != null ? w.prc1() : "") + ","
                    + (w.encVal() != null ? w.encVal() : w.enc0() != null ? w.enc0() : "") + "]");
            pl("");
        }
        pl("---------------------------------------------");
    }

    public void printOutseg(Sentence sentence) {
        pl(sentence.madamiraOut());
    }

    public static void printList(List list) {
        for (Object o : list) {
            pl(o);
        }
    }

    public static double f_score(double p, double r) {

        return 2 * (p * r) / (p + r);
    }

    public static boolean isArabic(String character) {
        if (character.trim().isEmpty()) {
            return false;
        }
        Map map = new HashMap();
        map.put("\u0621", "'");
        map.put("\u0622", "|");
        map.put("\u0623", ">");
        map.put("\u0624", "&");
        map.put("\u0625", "<");
        map.put("\u0626", "}");
        map.put("\u0627", "A");
        map.put("\u0628", "b");
        map.put("\u0629", "p");
        map.put("\u062A", "t");
        map.put("\u062B", "v");
        map.put("\u062C", "j");
        map.put("\u062D", "H");
        map.put("\u062E", "x");
        map.put("\u062F", "d");
        map.put("\u0630", "*");
        map.put("\u0631", "r");
        map.put("\u0632", "z");
        map.put("\u0633", "s");
        map.put("\u0634", "$");
        map.put("\u0635", "S");
        map.put("\u0636", "D");
        map.put("\u0637", "T");
        map.put("\u0638", "Z");
        map.put("\u0639", "E");
        map.put("\u063A", "g");
        map.put("\u0640", "_");
        map.put("\u0641", "f");
        map.put("\u0642", "q");
        map.put("\u0643", "k");
        map.put("\u0644", "l");
        map.put("\u0645", "m");
        map.put("\u0646", "n");
        map.put("\u0647", "h");
        map.put("\u0648", "w");
        map.put("\u0649", "Y");
        map.put("\u064A", "y");
        map.put("\u064B", "F");
        map.put("\u064C", "N");
        map.put("\u064D", "K");
        map.put("\u064E", "a");
        map.put("\u064F", "u");
        map.put("\u0650", "i");
        map.put("\u0651", "~");
        map.put("\u0652", "o");
        map.put("\u0670", "`");
        map.put("\u0652", "{");

        return map.containsKey(character.charAt(0));
    }

    public static void printESF(Entity entity) {
        if (entity != null) {
            p(entity.getAlpha().root());
            p(" -> " + entity.getAlphaEntity());
            p(" : " + entity.getBeta().root());
            pl("->" + entity.getBetaEntity());
        }
    }

}
