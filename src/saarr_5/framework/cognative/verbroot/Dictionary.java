package saarr_5.framework.cognative.verbroot;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.pl;

public class Dictionary {

    String path;
    private static List<String> Lines;
    private Stream<String> linesStrm;

    public Dictionary() {
//        path = "./resources/Verbs02.database";
        path = "F:\\Master\\Thesis\\Implementations\\verbRoot\\resources\\Verbs02.database";
        if (Lines == null) {
//            getAllLines();
        }
    }

    private void setDBpath(String pa) {
        path = pa;
    }

    private List<String> getAllLines() {

        try {
            List<String> lines = new ArrayList();

            File file = new File(path);
            FileReader fr = null;
            try {
                fr = new FileReader(file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader bfr = new BufferedReader(fr);
            String line;
            int i = 0;
            while ((line = bfr.readLine()) != null) {
                lines.add(line);
//                Linesdediac.add(uts.deDiacritic(line));
            }
            Lines = lines;
            return lines;
        } catch (IOException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Stream<String> getAllLines2() {

        Stream<String> lines;
        File file = new File(path);
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader bfr = new BufferedReader(fr);
        lines = bfr.lines();
//        Lines = lines.collect(Collectors.toList());
        return lines;
    }

    int maxOccuresLine(String verb) {
        if (Lines == null) {
            Lines = this.getAllLines();
//            System.out.println(LocalTime.now().getSecond());
        }
        verb = elaChecker(verb);
        int temp = 0, max = 1;
        int i = -1;
        int lineNo = i;
        String line;
        List<String> lineElements;
        Iterator<String> itr = Lines.iterator();
//        System.out.println(LocalTime.now().getSecond());
        while (itr.hasNext()) {
            i++;
            line = itr.next();

            lineElements = this.getLineElements(line);
//        System.out.println(LocalTime.now().getSecond());

            temp = itemsCount(lineElements, verb);
            if (temp > max) {
                max = temp;
                lineNo = i;
            }
            if (lineNo >= 10) {
                break;
            }

        }
//        System.out.println(LocalTime.now().getSecond());

        return lineNo;
    }

    int maxOccuresLine2(String verb) {
        if (linesStrm == null) {
            linesStrm = this.getAllLines2();
//            System.out.println(LocalTime.now().getSecond());
        }
        verb = elaChecker(verb);
        int temp = 0, max = 1;
        int i = -1;
        int lineNo = i;
        String line;
        List<String> lineElements;
        if (Lines == null) {
            Lines = this.getAllLines();
        }

        Iterator<String> itr = Lines.iterator();
        System.out.println(LocalTime.now().getSecond());
        while (itr.hasNext()) {
            i++;
            line = itr.next();

            lineElements = this.getLineElements(line);
//        System.out.println(LocalTime.now().getSecond());

            temp = itemsCount(lineElements, verb);
            if (temp > max) {
                max = temp;
                lineNo = i;
            }
            if (lineNo >= 10) {
                break;
            }

        }
        System.out.println(LocalTime.now().getSecond());

        return lineNo;
    }

    String getLine(int i) {
        if (Lines == null) {
            Lines = this.getAllLines();
        }
        return Lines.get(i);
    }

    String getRoot(String verb) {
        int lineroot = this.maxOccuresLine(verb);
        if (lineroot < 1) {
            System.out.println("فعل غير معروف.");
            return null;
        }
        String line = this.Lines.get(lineroot);
        line = line.split("\\(")[0];
        return line;
    }

    String getRoot2(String verb) {
        Stream<String> lines;
        try {
            lines = Files.lines(Paths.get(path));
        } catch (IOException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
        verb = this.elaChecker(verb);
//        line=lines.filter(null)
        int lineroot = this.maxOccuresLine2(verb);
        if (lineroot < 1) {
            return "فعل غير معروف.";
        }
        String line = this.Lines.get(lineroot);
        line = line.split("\\(")[0];
        return line;
    }

    private String elaChecker(String verb) {

        if (verb.length() == 3) {
            char middelElla = verb.charAt(1);
            if (middelElla == 'ا') {
                verb = verb.replace(middelElla, 'و');
            }

        } else if (verb.length() == 2) {
            verb = verb + verb.charAt(1);
        }
        return verb;
    }

    public List<String> getLineElements(String line) {
        List<String> lineElements = new ArrayList();
        String regex = "\\([^()]*\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        String[] elms;
        while (matcher.find()) {
            elms = matcher.group().replaceAll("\\(", "").replaceAll("\\)", "").split(",");
            for (String s : elms) {
                if (!s.isEmpty() && !s.equals("0")) {
                    lineElements.add(s);
                }
            }
        }
        return lineElements;
    }

    public int itemsCount(List<String> lineElements, String verb) {
        int counter = 0;
//        for (String s : lineElements) {
//            if (s.trim().matches(verb)) {
//                counter++;
//            }
//        }
//        while (lineElements.contains(verb)) {
//            counter++;
//            lineElements.remove(verb);
//        }

        for (Iterator<String> itr = lineElements.iterator(); itr.hasNext();) {
            if (itr.next().trim().equals(verb)) {
                counter++;
            }
        }
        return counter;
    }

    String getRoot3(String verb) {
        String root = null;
        long count = -1;
        Stream stream = Stream.empty();
        Optional op = null;
        long start = 0, end = 0;
        try {
            start = System.nanoTime();
//            stream = Files.lines(Paths.get(path));
            op = Files.lines(Paths.get(path)).parallel().filter(i -> i.contains(verb)).max((i, j) -> Integer.compare(i.split(verb).length, j.split(verb).length));//.forEach(i->pl(i));
            end = System.nanoTime();
        } catch (IOException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (op != null && op.isPresent()) {
            root = (String) op.get();
            root = root.split("\\(")[0];
            pl((end - start)/1000000 +"\tmilisecond");
        }

        return root;
    }

    void AllrootsOfverbs() {
        Path file = Paths.get("F:\\Master\\Thesis\\Prototype\\Dataset\\dataset(verbs)3.txt");
        List verbs;
        Stream stream = Stream.empty();
        try {
//            verbs = Files.readAllLines(file);
            Files.lines(file).parallel().map(i -> this.getRoot3(i)).forEach(System.out::println);
//            pl(stream.count());
        } catch (IOException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        Dictionary dic = new Dictionary();
        dic.getRoot3("يكتب");
//                dic.getRoot3("يرفس");

//        dic.AllrootsOfverbs();
    }
}
