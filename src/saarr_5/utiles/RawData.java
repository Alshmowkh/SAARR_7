package saarr_5.utiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class RawData {

    String source_corpora;

    public RawData(String corpora) {
        source_corpora = corpora;
//        testFolderPath()
    }

    public boolean RawSentencesToxml(String madaConfig, String xmlDoc) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(madaConfig));
            Element root = doc.getDocumentElement();

            File file = new File(source_corpora);

            File[] rawsfile = file.isFile() ? new File[]{file} : file.listFiles();
            for (File s : rawsfile) {
                Element rawdoc = doc.createElement("in_doc");
                rawdoc.setAttribute("id", s.getName());
                List<String> sentences = sentence_segmentor(s.getPath());
                for (int i = 0; i < sentences.size(); i++) {
                    String sent = sentences.get(i).trim() + ". ";
                    Element sentence = doc.createElement("in_seg");
                    sentence.setAttribute("id", "SENT" + (i + 1));
                    sentence.appendChild(doc.createTextNode(sent));

                    rawdoc.appendChild(sentence);
                }
                root.appendChild(rawdoc);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource dom = new DOMSource(doc);
//            System.out.println(xmlDoc + "\n" + madaConfig);
            StreamResult sr = new StreamResult(new File(xmlDoc));
            t.transform(dom, sr);

            if (new File(xmlDoc).exists()) {
                System.out.println("The xml_document has been done created in directory : " + xmlDoc);
            } else {
                System.out.println("There is error");
                return false;
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RawData.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("There is error");
            return false;
        } catch (IOException | SAXException | TransformerException ex) {
            Logger.getLogger(RawData.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("There is error");
            return false;
        }
        return true;
    }

    public List<String> sentence_segmentor(String textFile) throws IOException {

        String read_file = readFile(textFile);
        List<String> pureSent = new ArrayList<>();
        String[] sentences = read_file.split("\\.|:|\n|\\?");

        for (String sentence : sentences) {
            String sent = sentence.trim();
            if (!"".equals(sent)) {
                pureSent.add(sent);
            }
        }
        return pureSent;
    }

    public String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }
}
