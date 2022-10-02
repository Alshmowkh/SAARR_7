/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.annotation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import saarr_5.Document;
import saarr_5.framework.phraser.Phrase;

/**
 *
 * @author bakee
 */
public class Annotator {

    final String source = "F:\\Master\\Thesis\\Prototype\\Dataset\\corpus-6.1.xml";
    final String destination = "F:\\Master\\Thesis\\Prototype\\Experiments";

    boolean phrases(Document doc) {
        String line;
        String intened;
        try {
            BufferedReader br = new BufferedReader(new FileReader(doc.getSourceFile()));
            FileWriter fw = new FileWriter(destination + "\\Phrases1.xml");
            String sentID = "";
            while ((line = br.readLine()) != null) {
                fw.write(line + System.lineSeparator());
                if (line.trim().startsWith("<out_seg")) {
                    sentID = line.substring(line.indexOf("id=") + 8, line.indexOf(">") - 1);
                }
                if (line.trim().startsWith("</word_info>")) {
                    intened = line.split("<")[0];
                    fw.write(intened + "<syntactic_info>" + System.lineSeparator());
                    List<Phrase> phrases = doc.get(Integer.parseInt(sentID)).getPhrases();//getPhrasessentences.stream().filter(i-> i.toString().split("#")[0].trim()==sentID.trim()).collect(Collectors.toList());
                    for (int i = 0; i < phrases.size(); i++) {
                        line = "<phrase id=\"" + i + "\" begin=\"" + phrases.get(i).begins() + "\" end=\"" + phrases.get(i).ends() + "\" type=\"" + phrases.get(i).getTypeTB() + "\" content=\"" + phrases.get(i).text() + "\"/> ";
                        fw.write(intened + "\t" + line + System.lineSeparator());
                    }
                    fw.write(intened + "</syntactic_info>" + System.lineSeparator());
                }
            }
            br.close();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

}
