package saarr_5.framework.cognative;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static saarr_5.utiles.Utile.pl;

public  class Gerund_v2 extends ArrayList<String> {

    private static String _root;
    private static List gerundLines;
//    public Gerund(String verbRoot) {
//        root = verbRoot.trim();
//        
//    }

    public static Gerund_v2 getGerunds(String root) {
        String line = null;
        Gerund_v2 gerund=new Gerund_v2();
        if (gerundLines == null) {
            String pathDB = "./resources/Gerunds02.database";
            pathDB = "F:\\Master\\Thesis\\Implementations\\CognateIdentifer\\resources\\Gerunds02.database";
            if (!new File(pathDB).exists()) {
                System.out.println("required database not found in path" + pathDB);
                return null;
            }

            if (root == null || root.isEmpty()) {
                System.out.println("give a root.");
                return null;
            }
            if (root.length() > 4 || root.length() < 3) {
                System.out.println("There is no root with more than 4 or less 3 characters.");
                return null;
            }
            _root = root;
            gerundLines = gerund.fullGerunds(pathDB);
        }
        line = gerund.getLineOfRoot2(root);
        if (line != null) {
            return gerund.gerundFactory(line);
        } else {
            System.out.println("No root detected in gerunds database.");
            return null;
        }

    }

     List fullGerunds(String db) {
        List lines = new ArrayList();
        try {

            File file = new File(db);
            FileReader fr = null;
            try {
                fr = new FileReader(file);
            } catch (FileNotFoundException ex) {
            }
            BufferedReader bfr = new BufferedReader(fr);
            String line, temp;
            while ((line = bfr.readLine()) != null) {
                lines.add(line);

            }

        } catch (IOException ex) {
        }
        return lines;
    }

    public  String getLineOfRoot2(String root) {

        if (gerundLines == null) {
            return null;
        }

        Object Line = gerundLines.parallelStream().filter(r -> r.toString().split("\\(")[0].trim().equals(root)).findAny().orElse(null);
        if (Line != null) {
            return Line.toString();
        }
        return null;
    }

    public String getLineOfRoot(String path) {

        String rootLine = null;
        try {

            File file = new File(path);
            FileReader fr = null;
            try {
                fr = new FileReader(file);
            } catch (FileNotFoundException ex) {
            }
            BufferedReader bfr = new BufferedReader(fr);
            String line, temp;
            while ((line = bfr.readLine()) != null) {
                temp = line.split("\\(")[0].trim();
                if (temp.equals(_root)) {
                    rootLine = line;
                    break;
                }
            }

        } catch (IOException ex) {
        }
        return rootLine;
    }

      Gerund_v2 gerundFactory(String line) {
//        List<String> finegerunds = refineGerunds(line);
        this.addAll(refineGerunds(line));

        return this;
    }

      List<String> refineGerunds(String line) {
        List<String> elements = new ArrayList();

        String regex = "\\([^()]*\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        String[] elms;
        while (matcher.find()) {
            elms = matcher.group().replaceAll("\\(", "").replaceAll("\\)", "").split(",");
            for (String s : elms) {
                elements.add(s);
            }
        }
        elements = noDuplicateAndRarely(elements);
        return elements;
    }

    private  List<String> noDuplicateAndRarely(List<String> elements) {
        List<String> finalList = new ArrayList();
        int count;
        for (String m : elements) {
//            count = containsCount(elements, m);
            if (!finalList.contains(m)) {
                finalList.add(m);
            }
        }
        return finalList;
    }

    private int containsCount(List<String> elements, String element) {
        int counter = 0;
        Iterator<String> itr = elements.iterator();
        String temp;
        while (itr.hasNext()) {
            if (element.equals(itr.next())) {
                counter++;
            }
        }
        return counter;
    }
}
