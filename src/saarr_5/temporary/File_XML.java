package saarr_5.temporary;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JOptionPane;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//import static saarr_5.utiles.Utile.pl;
//
public class File_XML {
//    
//    String InputFile;
//    String OutputFile;
////    MadamiraReader madaRead;
//
//    public List<List<List<Node>>> clauses;
//    List<List<Node>> clause;
//// List<String> word;
////    Node clauses;
////    Node clause;
//    List<Node> word;
//    
//    public File_XML(String input, String output) {// throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException {
//        InputFile = input;
//        OutputFile = output;
////        madaRead = new MadamiraReader(InputFile);
////        getNomClauses();
////        insertClausesToMadaFile();
//    }
//    
//    Node getNounFeat(Node noun) throws XPathExpressionException {
//        Map morphemes = madaRead.getMorphemesMap(noun);
//        
//        NodeList toks = madaRead.getD3TokensScheme(noun);
//        String Stem = this.getCorrectStem(toks);
//        String pos = morphemes.get("pos").toString();
//        String prc0 = morphemes.get("prc0").toString();
//        Boolean prc0B = prc0.equals("0") | prc0.equals("na");
//        String prc1 = morphemes.get("prc1").toString();
//        Boolean prc1B = prc1.equals("0") | prc1.equals("na");
//        
//        String id = noun.getAttributes().getNamedItem("id").getNodeValue();
//        String enc0 = morphemes.get("enc0").toString();
//        Boolean enc0B = enc0.equals("0") | enc0.equals("na");
//        
//        if (prc0B && prc1B && enc0B) {
//            return noun;
//        } else {
//            if (!prc0B) {
//                return noun;
//            } else if (!enc0B) {
//                return noun;
//            }
//            
//        }
//        return noun;
//    }
//    
//    void getWordFeat(Node noun) throws XPathExpressionException {
//        Map morphemes = madaRead.getMorphemesMap(noun);
//        String diac = morphemes.get("diac").toString();
//        String pos = morphemes.get("pos").toString();
//        String id = noun.getAttributes().getNamedItem("id").getNodeValue();
//        
//        Boolean prc1B = morphemes.get("prc1").equals("0") | morphemes.get("prc1").equals("na");
//        Boolean prc2B = morphemes.get("prc2").equals("0") | morphemes.get("prc2").equals("na");
//        Boolean per3 = morphemes.get("per").equals("3");
//        
//        if (pos.equals("noun_prop")) {
//            word.add(noun);
//        } else if (pos.equals("pron") && !per3) {
//            word.add(noun);
//        } else if (pos.startsWith("adj")) {
//            word.add(noun);
//        } else if (pos.equals("noun")) {
//            if (!prc1B | !prc2B) {                       //An noun has prepostion bi (بـ)  Or has conjunction as و
//                if (word.size() > 1) {
//                    clause.add(word);
//                }
//                word = new ArrayList();
//                word.add(this.getNounFeat(noun));
//                
//            } else {
//                word.add(this.getNounFeat(noun));
//            }
//        } else {
//            if (word.size() > 1) {
//                clause.add(word);
//            }
//            word = new ArrayList();
//        }
//    }
//    
//    private void getNomClauses() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
//        
//        NodeList docs = madaRead.getDocuments();
//        clauses = new ArrayList();
//        for (int cod = 0; cod < docs.getLength(); cod++) {
//            NodeList sents = madaRead.getSentences(docs.item(cod));
//            for (int cos = 0; cos < sents.getLength(); cos++) {
//                clause = new ArrayList();
//                word = new ArrayList();
//                NodeList words = madaRead.getWordsOfSentence(docs.item(cod), sents.item(cos));
//                for (int cow = 0; cow < words.getLength(); cow++) {
//                    getWordFeat(words.item(cow));
//                }
//                clauses.add(clause);
//            }
//        }
//    }
//    
//    public void getXMLClauses() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, TransformerConfigurationException, TransformerException {
//        
//        String destination = OutputFile;
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document doc = db.newDocument();
//        Element root = doc.createElement("corpus");
//        
//        NodeList docs = madaRead.getDocuments();
//        for (int cod = 0; cod < docs.getLength(); cod++) {
//            Element docNode = doc.createElement(docs.item(cod).getNodeName());
//            docNode.setAttribute("id", docs.item(cod).getAttributes().getNamedItem("id").getNodeValue());
//            
//            NodeList sents = madaRead.getSentences(docs.item(cod));
//            for (int cos = 0; cos < sents.getLength(); cos++) {
//                Element sentNode = doc.createElement(sents.item(cod).getNodeName());
//                sentNode.setAttribute("id", sents.item(cos).getAttributes().getNamedItem("id").getNodeValue());
//                Element clausesSent = doc.createElement("clauses");
//                clause = new ArrayList();
//                word = new ArrayList();
//                NodeList words = madaRead.getWordsOfSentence(docs.item(cod), sents.item(cos));
//                for (int cow = 0; cow < words.getLength(); cow++) {
//                    getWordFeat(words.item(cow));
//                }
//                
//                int i = 0;
//                int from, to = 0;
//                for (List<Node> L : clause) {
//                    Element clauseNode = doc.createElement("clause");
//                    clauseNode.setAttribute("id", ++i + "");
//                    String sequ = "";
//                    from = L.size() - 1;
//                    for (Node n : L) {
//                        sequ = sequ + n.getAttributes().getNamedItem("word").getNodeValue() + " ";
//                        to = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
//                    }
//                    clauseNode.setTextContent(sequ);
//                    clauseNode.setAttribute("from", "" + (to - from));
//                    clauseNode.setAttribute("to", "" + to);
//                    
//                    clausesSent.appendChild(clauseNode);
//                    
//                }
//                sentNode.appendChild(clausesSent);
//                docNode.appendChild(sentNode);
//            }
//            root.appendChild(docNode);
//        }
//        doc.appendChild(root);
//        doc.setXmlStandalone(true);
//        //save xml doc
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer t = tf.newTransformer();
//        
//        t.setOutputProperty(OutputKeys.INDENT, "yes");
//        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//        
//        DOMSource dom = new DOMSource(doc);
//        StreamResult sr = new StreamResult(new File(destination));
//        t.transform(dom, sr);
//        
//    }
//    
//    public void createClausesFile() {
//        // throws SAXException, IOException, ParserConfigurationException, TransformerException, XPathExpressionException {
//        try {
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            Document sourceDoc = db.parse(new File(InputFile));
////        Document destDoc = db.parse(new File("Rhe_lib\\corpora 03\\clausesDoc.xml"));
//
//            NodeList sentences = sourceDoc.getElementsByTagName("out_seg");
//            Node sentence = sentences.item(0);
////            sentences.item(0).removeChild(sentences.item(0).cloneNode(true));
////            pl(sentences..getChildNodes().item(1).getNodeName());
////            XPath xpath = XPathFactory.newInstance().newXPath();
////        for (int i = 0; i < sentences.getLength(); i++) {
////            String specificDoc = sentences.item(0).getAttributes().getNamedItem("id").getNodeValue();
////            String query = "//out_doc[@ id='" + specificDoc + "']/out_seg";
////            NodeList sentences = (NodeList) xpath.evaluate(query, sourceDoc.getDocumentElement(), XPathConstants.NODESET);
//
////            for (int j = 0; j < sentences.getLength(); j++) {
////                String specificSent = sentences.item(j).getAttributes().getNamedItem("id").getNodeValue();
////                NodeList phrase = (NodeList) xpath.evaluate(query + "[@ id='" + specificSent + "']/clauses", destDoc.getDocumentElement(), XPathConstants.NODESET);
////                Node newNode = phrase.item(0).cloneNode(true);
////                sourceDoc.adoptNode(newNode);
////                sentences.item(j).appendChild(newNode);
////            }
////        }
////        //-------------------------------------------------
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer t = tf.newTransformer();
//            t.setOutputProperty(OutputKeys.INDENT, "yes");
//            DOMSource dom = new DOMSource(sourceDoc);
//            
//            StreamResult sr = new StreamResult(new File(OutputFile));
////            t.transform(dom, sr);
//        } catch (SAXException | IOException | ParserConfigurationException | TransformerException ex) {
//            Logger.getLogger(File_XML.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    public void insertClausesToMadaFile() {
//        try {
//            //throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException, XPathExpressionException {
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            Document doc = db.parse(new File(InputFile));
//
////        for (List<List<Node>> LL : clauses) {
//            Node clausesNode = doc.createElement("clauses");
//            int id = 0;
////            for (List<Node> L : LL) {
//            
//            Element clauseNode = doc.createElement("clause");
//            String sequ = "";
//            
//            String from = "1";// L.get(0).getAttributes().getNamedItem("id").getNodeValue();
//            String to = "10";// L.get(L.size() - 1).getAttributes().getNamedItem("id").getNodeValue();
////                for (Node n : L) {
////                    sequ = sequ + n.getAttributes().getNamedItem("word").getNodeValue() + " ";
////                }
//            clauseNode.setTextContent("بيت زبطان");
//            clauseNode.setAttribute("id", id++ + "");
//            clauseNode.setAttribute("from", from);
//            clauseNode.setAttribute("to", to);
//            clausesNode.appendChild(clauseNode);
////            }
//
////            if (clausesNode.hasChildNodes()) {
////                
//                Element sentNode =(Element)doc.getElementsByTagName("out_seg").item(0);// madaRead.getNodeParents(LL.get(0).get(0)).get(1);
//               sentNode.appendChild(clausesNode);
//               sentNode.getElementsByTagName("");
////                String docID = madaRead.getDocumentOfSentence(sentNode);
////                String sentID = sentNode.getAttributes().getNamedItem("id").getNodeValue();
////                XPath xpath = XPathFactory.newInstance().newXPath();
////                NodeList sentences = (NodeList) xpath.evaluate("//out_doc[@ id='" + docID + "']/out_seg[@ id='" + sentID + "']", doc.getDocumentElement(), XPathConstants.NODESET);
////                Node sentence = sentences.item(0);
////                  
//            
////            }
////        }
//
//            //-------------------------------------------------
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer t = tf.newTransformer();
//            
//            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            t.setOutputProperty(OutputKeys.VERSION, "1.0");
//            t.setOutputProperty(OutputKeys.INDENT, "yes");
//            
//            DOMSource dom = new DOMSource(doc);
//            StreamResult sr = new StreamResult(new File(OutputFile));
//            t.transform(dom, sr);
//        } catch (ParserConfigurationException | SAXException | IOException ex) {
//            Logger.getLogger(File_XML.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TransformerConfigurationException ex) {
//            Logger.getLogger(File_XML.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (TransformerException ex) {
//            Logger.getLogger(File_XML.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    String getCorrectStem(NodeList toks) {
//        String tok = toks.item(0).getAttributes().getNamedItem("form0").getNodeValue();
//        for (int i = 0; i < toks.getLength(); i++) {
//            String temp = toks.item(i).getAttributes().getNamedItem("form0").getNodeValue();
//            if (!temp.contains("+")) {
//                return temp;
//            }
//        }
//        return tok;
//    }
//    
//    public void printClauses() {
//        
//        for (List<List<Node>> LL : clauses) {
//            for (List<Node> L : LL) {
//                for (Node s : L) {
//                    System.out.print(s.getAttributes().getNamedItem("word").getNodeValue() + " ");
//                    //s.getAttributes().getNamedItem("word").getNodeValue() + " ");
//                }
//                System.out.print("\n");
//            }
//            
//            System.out.print("\n\n");
//        }
//    }
}
