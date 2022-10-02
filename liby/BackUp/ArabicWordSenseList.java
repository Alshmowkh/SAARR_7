package saarr_5.framework.semantic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import saarr_5.transliteration.Transliterator;

public class ArabicWordSenseList {

    ArabicDBDataAccess dbaccess;

    ArrayList<String> list;

    public ArabicWordSenseList() {

        dbaccess = new ArabicDBDataAccess();

    }

    public String removeWNSuffix(String synsetid) {
        String synsetname = "";
        if (synsetid.length() > 0) {
            synsetname = synsetid.substring(0, synsetid.lastIndexOf("_"));
        }
        return synsetname;
    }

    public String convertToScript(String buckwalterIn) {
        return Transliterator.translitrate(buckwalterIn.replace("_", " "));
    }

    ArrayList<String> getFirstLevelNouns(ArrayList<String> firstlevel) throws SQLException {
        list = new ArrayList();
        for (String f : firstlevel) {
            String pos = String.valueOf(f.charAt(f.lastIndexOf("_") + 1));
            if ("n".equals(pos.toLowerCase().trim())) {
                String node = convertToScript(removeWNSuffix(dbaccess.getTopLevelSynsetsForTree(f)));
                list.add(node.replace("null", " "));
            }
        }
        return list;
    }

    public ArrayList<String> getFirstLevelNouns() throws SQLException {
        list = new ArrayList();
        ArrayList<String> firstlevel = dbaccess.getFirstLevelNodes();
        for (String f : firstlevel) {
            String pos = String.valueOf(f.charAt(f.lastIndexOf("_") + 1));
            if ("n".equals(pos.toLowerCase().trim())) {
                list.add(f);
            }
        }
        return list;
    }

    public ArrayList<String> getFirstLevelSynsetIDs(ArrayList<String> firstlevel) throws SQLException {
        list = new ArrayList();
        for (String f : firstlevel) {
            String pos = String.valueOf(f.charAt(f.lastIndexOf("_") + 1));
            if ("n".equals(pos.toLowerCase().trim())) {
                list.add(f);
            }
        }
        return list;
    }

    public ArrayList<String> getChildernSynsetID(String synsetid) throws SQLException {
        list = new ArrayList();
        HashMap<String, String> hyponyms = dbaccess.getHyponyms(synsetid);
        Iterator set = hyponyms.keySet().iterator();
        while (set.hasNext()) {
            list.add(set.next().toString());
        }
        return list;
    }

    ArrayList<String> recursionTree(String synsetid) throws SQLException {
        String trans = dbaccess.getTranslation(synsetid, "Arabic");
        String node = convertToScript(removeWNSuffix(trans));
        list = new ArrayList();
        list.add(node);
        HashMap<String, String> hyponyms = dbaccess.getHyponyms(synsetid);
        Iterator set = hyponyms.keySet().iterator();

        while (set.hasNext()) {
            list.addAll(this.getChildernSynsetID(set.next().toString()));
            list.add("\n");
        }
        return list;
    }

    private ArrayList<String> getHyperPathOld(String synsetid) throws SQLException {
        String up = synsetid;
        list = new ArrayList();
        while (!up.isEmpty()) {
            list.add(up);
            up = dbaccess.getHypernym(up);
        }
        return list;
    }

    public ArrayList<String> getHyperPath(String synsetid) {
        String up = dbaccess.getTranslation(synsetid, "English");
        list = new ArrayList();
        int over = 0;
        while (!up.isEmpty()) {
            over++;
            if (over > 50) {
                System.out.println("error in------------- KB inconsistency -----------" + "\n" + list);
                return null;
            }
            list.add(up);
            up = dbaccess.getHypernym(up);
        }

        return list;
    }

    public String getArabicTerm(String synsetid) throws SQLException {
        String trans = dbaccess.getTranslation(synsetid, "Arabic");
        String nosuffix = this.removeWNSuffix(trans);
        String script = this.convertToScript(nosuffix);
        return script;
    }

    ArrayList<String> getWordSenses(String input) throws SQLException {
        return dbaccess.readWordSenses(input);
    }

    public String deDiacritic(String diac) {
        char[] chars = diac.toCharArray();
        String res = "";
        for (Character ch : chars) {
            if (!dbaccess.diacriticList().contains(ch)) {
                res = res + ch;
            }
        }
        return res;
    }

    ArrayList<String> getSynsetMembers(String synsetid) {
        ArrayList<String> members = this.dbaccess.readSynsetMembers(synsetid);
//        System.out.println(members);
        if (dbaccess.isdiacrited(wordOriginal)) {
            return members;
        } else {
            ArrayList<String> result = new ArrayList();

            for (String member : members) {
                String deDiac = this.deDiacritic(member);
                result.add(deDiac);
            }
            if (result.contains(wordOriginal)) {
                return members;
            }

        }
        return new ArrayList();
    }

    void p(Object str) {
        System.out.println(((ArrayList) str).size());
    }

    void test2() throws SQLException {
//        list = dbaccess.getSynsets("سِمَة");
//        list = dbaccess.getHyponymsList(dbaccess.getTranslation(list.get(0), "English"));
//        printList(list);
    }

//    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
//
//        ArabicWordSenseList cls = new ArabicWordSenseList();
//        cls.dbaccess = new ArabicDBDataAccess3();
//        ArabMorph aMorph = new ArabMorph();
//        ArrayList<String> morphs, synsets, members;
//        String input = "التفاحة";
//        morphs = aMorph.getWordMorphs(input);
//        String stemOrg = cls.deDiacritic(cls.b2a.transliterate(aMorph.getStem(input)));
//
//        System.out.println("morphs :" + morphs);
////
////        for (String mo : morphs) {
//        String script = cls.b2a.transliterate(morphs.get(0));
//        System.out.println(script);
//        synsets = cls.dbaccess.getSynsets_diac(script);
//        if (synsets.isEmpty()) {
//            synsets = cls.dbaccess.getSynsets_noDiac(script);
//        }
//        System.out.println("synsets : " + synsets);
//
//        for (String sy : synsets) {
//            members = cls.getSynsetMembers(sy, stemOrg);
////            for (String mr : members) {
//            System.out.println(members);
////            }
//        }
//
//        //00000000000000000000000000000000000000000000000000000000000000
////        ArrayList<String> firstlevel = cls.dbaccess.getFirstLevelNodes();
////        ArrayList<String> fnouns = cls.getFirstLevelSynsetIDs(firstlevel);
////        cls.printList(cls.dbaccess.getHyponymsList("person_n1EN"));
////        String id = cls.dbaccess.getHyponyms(cls.dbaccess.getHyponyms(cls.dbaccess.getHyponyms(fnouns.get(0)).keySet().iterator().next()).keySet().iterator().next()).keySet().iterator().next();
//        //000000000000000000000000000000000000000000000000000000000
////        String shakhs=cls.dbaccess.getHyponyms(id)
////        for (String f : firstlevel) {
////            String pos = String.valueOf(f.charAt(f.lastIndexOf("_") + 1));
////            if ("n".equals(pos.toLowerCase().trim())) {
////        HashMap<String, String> map = cls.dbaccess.getHyponyms(cls.dbaccess.getHyponyms(cls.dbaccess.getHyponyms(cls.dbaccess.getHyponyms(fnouns.get(0)).keySet().iterator().next()).keySet().iterator().next()).keySet().iterator().next());
////        String f2 = map.keySet().iterator().next();
////        System.out.println(id);
////        String trans = cls.dbaccess.getTranslation(f2, "Arabic");
////        String node = cls.convertToScript(cls.removeWNSuffix(trans));
////        String hyper=cls.dbaccess.getHypernym(cls.dbaccess.getHypernym(cls.dbaccess.getHypernym(cls.dbaccess.getHypernym(id))));
////        System.out.println(cls.dbaccess.getHypernym("unrestricted_s2EN"));
////        System.out.println(aMorph.getStem(input));
////        cls.printList(firstlevel);
////            }
////        }
//        //--------------------------------------------
////        cls.test2();
//    }
    void printList(ArrayList<String> list) throws SQLException {
//        System.out.println("List size is : " + list.size());
        for (String s : list) {
            System.out.println(s);
//            System.out.println((this.convertToScript(s)));
//                        System.out.println((this.convertToScript(removeWNSuffix(s))));
//            System.out.println(this.convertToScript(this.removeWNSuffix((this.dbaccess.getTranslation(s, "Arabic")))));
        }
        System.out.println();
    }
}
