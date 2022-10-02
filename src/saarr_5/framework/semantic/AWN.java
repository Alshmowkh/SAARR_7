package saarr_5.framework.semantic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import saarr_5.Type;
import saarr_5.framework.Entity;
import saarr_5.morphalize.Word;
import saarr_5.transliteration.Transliterator;
import saarr_5.utiles.Utile;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.isDiacritic;
import static saarr_5.utiles.Utile.p;
import static saarr_5.utiles.Utile.pl;

public class AWN {
    
    private List list;
    private List entities;
    private PreparedStatement ps;
    private Connection conn;
    private ResultSet rs;
    private String wordTable;
    private String linkTable;
    
    public AWN() {
        
        setEntities();
//        connectDB();
        wordTable = "F:\\Master\\Thesis\\Prototype\\Dataset\\WordNet(word)2.txt";
        linkTable = "F:\\Master\\Thesis\\Prototype\\Dataset\\WordNet(link)2.txt";
    }
    
    private void setEntities() {
        entities = new ArrayList();
        entities.add("PERSON");
        entities.add("INSTRUMENT");
        entities.add("ORGANIZATION");
        entities.add("ANIMAL");
        entities.add("PLANT");
        entities.add("MEASURE");
        entities.add("PROCESS");
        entities.add("LOCATION");
        entities.add("GROUP");
        entities.add("NATURAL_OBJECT");
        entities.add("EVENT");
        entities.add("PSYCHOLOGICAL_FEATURE");
        entities.add("ARTIFACT");
    }
    
    private void connectDB() {
        try {
            String DB = "F:\\Master\\Thesis\\Tools\\Ontology\\AWN\\Alshmowkh\\wordnety";
            conn = DriverManager.getConnection("jdbc:derby:" + DB);
//        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
//        Properties pr = System.getProperties();
//        pr.put("derby.storage.pageSize", "32000");
//        pr.put("derby.storage.pageCacheSize", "5000");
        } catch (SQLException ex) {
            Logger.getLogger(AWN.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String removeWNSuffix(String synsetid) {
//        String synsetname =synsetid;
        if (synsetid.length() > 0 && synsetid.contains("_")) {
            synsetid = synsetid.substring(0, synsetid.lastIndexOf("_"));
        }
        return synsetid;
    }
    
    public String getTranslation(String synsetid, String lang) {
        
        String result = null;
        try {
            if (lang.equals("Arabic")) {
                
                ps = conn.prepareStatement("SELECT link1 FROM link WHERE link2= ? AND type='equivalent'");
                ps.setString(1, synsetid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    result = rs.getString("link1");
                }
                
            } else if (lang.equals("English")) {
                
                ps = conn.prepareStatement("SELECT link2 FROM link WHERE link1= ? AND type='equivalent'");
                ps.setString(1, synsetid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    result = rs.getString("link2");
                }
            }
//            conn.close();
        } catch (SQLException ex) {
            
        }
        
        return result;
    }
    
    public String translation(String synsetid) {
        
        try {
            Optional op;
            if (synsetid.trim().endsWith("AR")) {
                
                op = Files.lines(Paths.get(linkTable)).filter((String line) -> line.split("\\s")[2].equals(synsetid) && line.split("\\s")[1].equals("equivalent")).map(line -> line.split("\\s")[4]).findFirst();
                if (op.isPresent()) {
                    return op.get().toString();
                }
            } else if (synsetid.trim().endsWith("EN")) {
                
                op = Files.lines(Paths.get(linkTable)).filter((String line) -> line.split("\\s")[4].equals(synsetid) && line.split("\\s")[1].equals("equivalent")).map(line -> line.split("\\s")[2]).findFirst();
                if (op.isPresent()) {
                    return op.get().toString();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(AWN.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public List getSynsets_diac(String word) {
//        connectDB();
        try {
            list = new ArrayList();
            if (isDiacritic(word)) {
                
                ps = conn.prepareStatement("SELECT synsetid FROM word WHERE value = ?");
                ps.setString(1, word);
                rs = ps.executeQuery();
                
            } else {
                System.out.println("The word " + word + " is not diacritic");
                return null;
            }
            while (rs.next()) {
                String ssid = rs.getString("synsetid");
                list.add(ssid);
            }
//            conn.close();
            return list;
            
        } catch (SQLException ex) {
            Logger.getLogger(AWN.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public List getAllSynsets(String word) {
//        connectDB();
        try {
            list = new ArrayList();
            
            ps = conn.prepareStatement("SELECT synsetid FROM word WHERE noDiacVal ='" + word + "'");
//            ps.setString(1, wordTable);
            rs = ps.executeQuery();
            while (rs.next()) {
                String ssid = rs.getString("synsetid");
                list.add(ssid);
            }
//            conn.close();
            return list;
            
        } catch (SQLException ex) {
            Logger.getLogger(AWN.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public List getSynsets_noDiac(String word) {
        try {
            String searchString = "";
            
            char[] wordChars = word.toCharArray();
            for (int i = 0; i < wordChars.length; i++) {
                searchString += wordChars[i] + "%";
            }
//        System.out.println(searchString);
            ps = conn.prepareStatement("SELECT synsetid FROM word WHERE value LIKE ?");
            ps.setString(1, searchString);
            rs = ps.executeQuery();
            
            list = new ArrayList();
            
            while (rs.next()) {
                String ssid = rs.getString("synsetid");
                list.add(ssid);
            }
            
            return list;
            
        } catch (SQLException ex) {
            Logger.getLogger(AWN.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List getSynsets_Eng(String word) {
//        connectDB();
        try {
            list = new ArrayList();
            
            ps = conn.prepareStatement("SELECT synsetid FROM word WHERE value = ?");
            ps.setString(1, word);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                String ssid = rs.getString("synsetid");
                list.add(ssid);
            }
//            conn.close();
            return list;
            
        } catch (SQLException ex) {
            Logger.getLogger(AWN.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public String getHypernym(String synsetid) {
        
        try {
            String result = "";
            
            ps = conn.prepareStatement("SELECT link2 FROM link where link1 = ?");
            ps.setString(1, synsetid);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getString("link2");
//                pl(result);
            }
            
            return result;
        } catch (SQLException ex) {
            
            return null;
        }
    }
    
    public List getHyperPath(String synsetid) {
        if (synsetid == null) {
            return null;
        }
        list = new ArrayList();
        String temp = synsetid.substring(synsetid.lastIndexOf("_") + 1);
        if (temp.startsWith("a") || temp.startsWith("s")) {
            list.add(synsetid);
            list.add("Adjective_0");
            return list;
        }
        String up = getTranslation(synsetid, "English");
        int over = 0;
        while (up != null && !up.isEmpty()) {
            over++;
            if (over > 50) {
                System.out.println("error in------------- KB inconsistency -----------" + "\n" + list);
                return null;
            }
            list.add(up);
//            pl(up);
            up = getHypernym(up);
            
        }
        
        return list;
    }
    
    public String hypernym(String synsetid) {
//        pl(synsetid);
        try {
            Optional result = Files.lines(Paths.get(linkTable)).filter((String line) -> line.split("\\s")[2].equals(synsetid)).map(line -> line.split("\\s")[4]).findFirst();
            
            if (result.isPresent()) {
                return result.get().toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public List hyperPath(String synsetid) {
        if (synsetid == null) {
            return null;
        }
        list = new ArrayList();
        String temp = synsetid.substring(synsetid.lastIndexOf("_") + 1);
        if (temp.startsWith("a") || temp.startsWith("s")) {
            list.add(synsetid);
            list.add("Adjective_0");
            return list;
        }
        String up = translation(synsetid);
        int over = 0;
        while (up != null && !up.isEmpty()) {
            over++;
            if (over > 50) {
                System.out.println("error in------------- KB inconsistency -----------" + "\n" + list);
                return null;
            }
            list.add(up);
//            pl(up);
            up = hypernym(up);
            
        }
        
        return list;
    }
    
    private String getMostCorrectSynset(List synsets, Word word) {
        String tempi;
        String stemDiac = word.root();
        String stem = deDiacritic(word.root());
        if (synsets != null && !synsets.isEmpty()) {
            for (int i = 0; i < synsets.size(); i++) {
                tempi = synsets.get(i).toString();
                if (tempi.substring(0, tempi.lastIndexOf("_") + 2).endsWith("n")) {
                    tempi = this.removeWNSuffix(tempi);
                    tempi = Transliterator.translitrate(tempi);
                    if (tempi.equals(stemDiac) || deDiacritic(tempi).equals(stem)) {
                        return synsets.get(i).toString();
                    }
                }
            }
        }
        System.out.println("------------Error in obtain the most correct synset for  \"" + word.root() + " \"item.");
        return null;
    }
    
    private String getMostCorrectSynset(List synsets, String word) {
        String tempi;
        String stemDiac = word;
        String stem = deDiacritic(word);
        if (synsets != null && !synsets.isEmpty()) {
            for (int i = 0; i < synsets.size(); i++) {
                tempi = synsets.get(i).toString();
                if (tempi.substring(0, tempi.lastIndexOf("_") + 2).endsWith("n")) {
                    tempi = this.removeWNSuffix(tempi);
                    tempi = Transliterator.translitrate(tempi);
                    if (tempi.equals(stemDiac) || deDiacritic(tempi).equals(stem)) {
                        return synsets.get(i).toString();
                    }
                }
            }
        }
        System.out.println("------------Error in obtain the most correct synset for  \"" + word + "\" item.");
        return null;
    }
    
    private List getSynsets(String word) {
        
        List synsets = null;
        if (isDiacritic(word)) {
            synsets = getSynsets_diac(word);
        }
        if (synsets != null && !synsets.isEmpty()) {
            return synsets;
        }
        synsets = getSynsets_noDiac(deDiacritic(word));
        
        if (synsets != null && !synsets.isEmpty()) {
            return synsets;
        }
        
        System.out.println("------------The \"" + word + " \"is not included in Knowledge Base ");
        return null;
    }
    
    private List getSynsets(Word word) {
        
        if (word == null) {
            return null;
        }
        if (word.isPunc()) {
            return null;
        }
        
        String stemDiac = word.root();
        String stem = deDiacritic(word.root());
        List synsets = null;
//        pl(stemDiac);
        if (isDiacritic(stemDiac)) {
            synsets = getSynsets_diac(stemDiac);
        }
        if (synsets != null && !synsets.isEmpty()) {
            return synsets;
        }
        
        synsets = getSynsets_noDiac(stem);
        
        if (synsets != null && !synsets.isEmpty()) {
            
            return synsets;
        }
        synsets = getSynsets_Eng(word.gloss().split(",")[0]);
        
        if (synsets != null && !synsets.isEmpty()) {
            return synsets;
        }
        System.out.println("------------The \"" + word.root() + " \"is not included in Knowledge Base ");
        return null;
        
    }
    
    public List synsets(String word) {
        
        try {
//            list = Files.lines(Paths.get(wordTable)).filter((String line) -> line.split("\\s")[2].equals(word)).map(line -> line.split("\\s")[3]).collect(Collectors.toList());
            list = Files.lines(Paths.get(wordTable)).filter((String line) -> line.split("\\s")[2].equals(word)).map(line -> line.split("\\s")[3]).filter(w -> w.trim().substring(w.lastIndexOf("_") + 1).startsWith("n")).collect(Collectors.toList());
            
        } catch (Exception ex) {
            
        }
        
        return list;
    }
    
    public List synsets(Word wrd) {
        String word = deDiacritic(wrd.root());
        try {
//            list = Files.lines(Paths.get(wordTable)).filter((String line) -> line.split("\\s")[2].equals(word)).map(line -> line.split("\\s")[3]).filter(w -> w.trim().endsWith("AR")).collect(Collectors.toList());
            list = Files.lines(Paths.get(wordTable)).filter((String line) -> line.split("\\s")[2].equals(word)).map(line -> line.split("\\s")[3]).filter(w -> w.trim().substring(w.lastIndexOf("_") + 1).startsWith("n")).collect(Collectors.toList());
            
        } catch (Exception ex) {
            
        }
        
        return list;
    }
    
    public List members(List synsets, String word) {
        
        try {
            list = Files.lines(Paths.get(wordTable)).filter((String line) -> synsets.contains(line.split("\\s")[3])).map(line -> line.split("\\s")[2]).collect(Collectors.toList());
        } catch (Exception ex) {
            
        }
        
        return list;
    }
    
    public List getOntoPath(Word word) {
//        pl(wordTable.tag());
        list = new ArrayList();
        if (word == null) {
            return null;
        }
        if (word.isPunc()) {
            return null;
        }
        if (word.isPnoun() || word.isPron()) {
            list.add("person");
            return list;
        }
//        pl(wordTable.modifiedStem());
        list = this.getSynsets(word);
        
        String correctSyn = this.getMostCorrectSynset(list, word);
//        pl(correctSyn);
        if (correctSyn != null) {
            list = getHyperPath(correctSyn);
            if (list != null && !list.isEmpty()) {
                return list;
            } else {
                System.out.println("---------The \"" + correctSyn + " \"has not Hyper-Path ");
                return null;
            }
        }
//        return correctSyn;//this.getHyperPath(correctSyn.get(2).toString());
//        String stem = deDiacritic(wordTable.modifiedStem());
////         System.out.println(stem);
//        List synsets = null;
////        synsets = getSynsets_diac(wordTable.modifiedStem());
////       
//        if (synsets != null && !synsets.isEmpty()) {
//          
//        }
//        synsets = getSynsets_noDiac(stem);
//        pl(synsets);
//        if (synsets != null && !synsets.isEmpty()) {
//            list = getHyperPath(synsets.get(2).toString());
//            return list;
//        }
//        synsets = getSynsets_Eng(wordTable.gloss().split(",")[0]);
//
//        if (synsets != null && !synsets.isEmpty()) {
//            list = getHyperPath(synsets.get(0).toString());
//            return list;
//        }
//        System.out.println("------------The \"" + wordTable + " \"is not included in Knowledge Base ");
        return null;
    }
    
    private boolean isInSelectEntities(List paths) {
        Iterator it = paths.iterator();
        while (it.hasNext()) {
            String normal = (String) it.next();
            normal = removeWNSuffix(normal).toUpperCase();
            if (entities.contains(normal)) {
                return true;
            }
        }
        return false;
    }
    
    public List ontoPath(Word word) {
//        pl(word.tag());
        list = new ArrayList();
        if (word == null) {
            return null;
        }
        if (word.isPunc()) {
            return null;
        }
        if (word.isPnoun() || word.isPron()) {
            list.add("person");
            return list;
        }
        List synsets = synsets(word);
        Iterator it = synsets.iterator();
        String correctSyn;// = this.getMostCorrectSynset(list, word);
        while (it.hasNext()) {
            correctSyn = (String) it.next();
            list = hyperPath(correctSyn);
            if (isInSelectEntities(list)) {
                return list;
            }
        }
        return list;
    }
    
    String getNED(Word word) {
        
        List<String> path = getOntoPath(word);
//        System.out.println("path :" + path);
        word.ontoPath(path);
        if (path != null) {
            String named;
            for (String n : path) {
                String normal = removeWNSuffix(n).toUpperCase();
                if (entities.contains(normal)) {
                    named = entities.get(entities.indexOf(normal)).toString();
                    return named;
                }
            }
        }
        return null;
    }
    
    String getNED(List<String> path) {

//        List<String> path = getOntoPath(wordTable);
//        System.out.println("path :" + path);
//        wordTable.ontoPath(path);
        if (path != null) {
            String named;
            for (String n : path) {
                String normal = removeWNSuffix(n).toUpperCase();
                if (entities.contains(normal)) {
                    named = entities.get(entities.indexOf(normal)).toString();
                    return named;
                }
            }
        }
        return null;
    }
    
    String NER(List paths) {
        
        Iterator it = paths.iterator();
        while (it.hasNext()) {
            String normal = (String) it.next();
            normal = removeWNSuffix(normal).toUpperCase();
            if (entities.contains(normal)) {
                return entities.get(entities.indexOf(normal)).toString();
            }
        }
        return null;
    }
    
    public boolean isESF(Entity entity) {
        if (entity.getType() == Type.ESF_AG) {
            return false;
        }
        try {
//            pl(entity);
            List alphaP;
//            alphaP= getOntoPath(entity.getAlpha());
            alphaP = ontoPath(entity.getAlpha());
            entity.setAlphaOntoPath(alphaP);
//            pl(alphaP);
            List betaP;
//            betaP= getOntoPath(entity.getBeta());
            betaP = ontoPath(entity.getBeta());
            entity.setBetaOntoPath(betaP);
//            pl(betaP);
            String entityX = null;
//            entityX= getNED(alphaP);
            entityX = NER(alphaP);
            entity.setAlphaEntity(entityX);
            String entityY = null;
//            entityY= getNED(betaP);
            entityY = NER(betaP);
            entity.setBetaEntity(entityY);
//            pl(entityY);
            Utile.printESF(entity);
            
            if (entityX == null || entityY == null) {
//            System.out.println("The entity of " + entity.getAlpha() + "or " + entity.getBeta() + "  is not recognized name");
                return false;
            }
            return !entityX.trim().toUpperCase().equals(entityY.trim().toUpperCase());
        } catch (Exception e) {
//            pl("error in " + entity);
            e.printStackTrace();
            return false;
        }
        
    }
    
    public static void main(String[] args) {
        AWN awn = new AWN();
        awn.test();
    }
    
    void test() {
        String w = "سحاب";
        long start, end;
        double val;
        start = System.nanoTime();
        list = this.synsets(w);
        end = System.nanoTime();
        pl((val = (end - start) / 1000000) + "  milis(" + (val / 1000) + ")");
        
        pl(list);
//        list = members(list, w);
//        pl(list);
//        w = this.getMostCorrectSynset(list, w);
//        w = "tadobiyr_n1AR";
//        pl(w.substring(w.lastIndexOf("_")+1));
        for (Object o : list) {
//            w = list.get(0).toString();
            list = this.hyperPath(o.toString());
            pl(list);
        }

//        String sysid=">asad_n1AR";
//       List path=this.getHyperPath(sysid);
//       pl(path);
//       pl(this.getHypernym(sysid));
    }
}
/* ملاحظات
 1- الكينونات المقارنة التي ستبنى عليها الانطلوجي هي 
 1-person
 2- instrument
 3- organization
 4- plant
 5- measure
 6- location
 7- animal
 8- group
 9- process
 10- natural_object

 */
