package saarr_5.utiles;

import saarr_5.framework.semantic.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import saarr_5.Type;
import saarr_5.framework.Entity;
import saarr_5.morphalize.Word;
import saarr_5.transliteration.Transliterator;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.isDiacritic;
import static saarr_5.utiles.Utile.p;
import static saarr_5.utiles.Utile.pl;

public class AWN_Up {

    private List list;
    private List entities;
    private PreparedStatement ps;
    private Connection conn;
    private ResultSet rs;

    public AWN_Up() {

        setEntities();
        connectDB();

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
            Logger.getLogger(AWN_Up.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AWN_Up.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AWN_Up.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AWN_Up.class.getName()).log(Level.SEVERE, null, ex);
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

    public List getOntoPath(Word word) {
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
//        pl(word.modifiedStem());
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
//        String stem = deDiacritic(word.modifiedStem());
////         System.out.println(stem);
//        List synsets = null;
////        synsets = getSynsets_diac(word.modifiedStem());
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
//        synsets = getSynsets_Eng(word.gloss().split(",")[0]);
//
//        if (synsets != null && !synsets.isEmpty()) {
//            list = getHyperPath(synsets.get(0).toString());
//            return list;
//        }
//        System.out.println("------------The \"" + word + " \"is not included in Knowledge Base ");
        return null;
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

//        List<String> path = getOntoPath(word);
//        System.out.println("path :" + path);
//        word.ontoPath(path);
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

    public boolean isESF(Entity entity) {
        if (entity.getType() == Type.ESF_AG) {
            return false;
        }
        try {
            List alphaP = getOntoPath(entity.getAlpha());
            List betaP = getOntoPath(entity.getBeta());
            String entityX = getNED(alphaP);
            String entityY = getNED(betaP);
            p(entity.getAlpha().root());
            p(" -> " + entityX);
            p(" : " + entity.getBeta().root());
            pl("->" + entityY);

            if (entityX == null || entityY == null) {
//            System.out.println("The entity of " + entity.getAlpha() + "or " + entity.getBeta() + "  is not recognized name");
                return false;
            }
            return !entityX.trim().toUpperCase().equals(entityY.trim().toUpperCase());
        } catch (Exception e) {
            pl("error in " + entity);
            e.printStackTrace();
            return false;
        }

    }

    public static void main(String[] args) {
        AWN_Up awn = new AWN_Up();
        awn.test();
    }

    void test() {
        String w = "كلام";
        list = this.getSynsets(w);
        pl(list);
        w = this.getMostCorrectSynset(list, w);
        list = this.getHyperPath(w);
        pl(list);
//        list = this.getSynsets_noDiac("");
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
