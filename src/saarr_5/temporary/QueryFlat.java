/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.temporary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import static saarr_5.utiles.Utile.isArabic;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class QueryFlat {

    private String pathFile;

    void performance() {
        long start, end;
        start = System.nanoTime();
        end = System.nanoTime();
        pl((end - start) / 1000000 + "  millisecond");
    }

    void readLines() {
        List lines = new ArrayList();
        long start, end;
        double val;
        try {
            start = System.nanoTime();

            lines = Files.readAllLines(Paths.get(pathFile));

            end = System.nanoTime();
            pl((val = (end - start) / 1000000) + "  milli(" + (val / 1000) + ")");
        } catch (IOException ex) {
            Logger.getLogger(QueryFlat.class.getName()).log(Level.SEVERE, null, ex);
        }
        pl(lines.size());
    }

    void readLinesContain(String str) {
        Stream lines = Stream.empty();
        long start, end;
        double val;
        try {
            start = System.nanoTime();

//            lines = Files.readAllLines(Paths.get(pathFile));
            lines = Files.lines(Paths.get(pathFile)).filter(i -> ((String) i).split("\\s")[0].contains(str));

            end = System.nanoTime();
            pl((val = (end - start) / 1000000) + "  milli(" + (val / 1000) + ")");
        } catch (IOException ex) {
            Logger.getLogger(QueryFlat.class.getName()).log(Level.SEVERE, null, ex);
        }
        pl(lines.count());
    }

    void readMaxLinesContain(String str) {
        Stream lines = Stream.empty();
        long start, end;
        double val;
        try {
            start = System.nanoTime();

//            lines = Files.readAllLines(Paths.get(pathFile));
            lines = Files.lines(Paths.get(pathFile)).filter(i -> ((String) i).split("\\s")[1].endsWith(str));

            end = System.nanoTime();
            pl((val = (end - start) / 1000000) + "  milli(" + (val / 1000) + ")");
        } catch (IOException ex) {
            Logger.getLogger(QueryFlat.class.getName()).log(Level.SEVERE, null, ex);
        }
        pl(lines.count());
    }

    void modifyLine() {
        Stream lines = Stream.empty();
        long start, end;
        double val;
        try {
            start = System.nanoTime();

//            lines = Files.readAllLines(Paths.get(pathFile));
            lines = Files.lines(Paths.get(pathFile)).map((String line) -> {
                String[] slices = line.split("\\s");
                int k = 0;
                String temp = slices[k++].trim();
                String value = temp;
                while (isArabic(temp) || temp.isEmpty()) {
                    temp = slices[k++].trim();
                    value += "_" + temp;
                }
                line = value + " " + line.substring(value.length() + 1, line.length());
//                pl(line);
                return line;

            });

            end = System.nanoTime();
            pl((val = (end - start) / 1000000) + "  milli(" + (val / 1000) + ")");
        } catch (IOException ex) {
            Logger.getLogger(QueryFlat.class.getName()).log(Level.SEVERE, null, ex);
        }
        pl(lines.count());
    }

    public static void main(String[] args) {
        QueryFlat qf = new QueryFlat();
        qf.pathFile = "F:\\Master\\Thesis\\Prototype\\Dataset\\WordNet(word).txt";
//        qf.readLines();
//        qf.readLinesContain("صنعاء");
//        qf.readMaxLinesContain("AR");
        qf.modifyLine();
    }
}
