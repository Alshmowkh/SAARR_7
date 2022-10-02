/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.temporary;

import java.util.ArrayList;
import java.util.List;
import saarr_5.framework.cognative.SubjectIdentify;
import saarr_5.framework.phraser.Phraser;
import saarr_5.morphalize.Morphalizer;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class Parser {

    String rawSent = "F:\\Master\\Thesis\\Implementations\\Rhe_Processes\\Rhe_lib\\Corpora 03\\raw data\\Document 02.txt";

    void ini() {
        List<Sentence> morphed = new ArrayList();
        String rawSent
                = "أحمد يأكل تفاح.";
//        tokens.add(Morphalizer.morphalize(rawSent));
//        morphed = Morphalizer.morphalize(new File(Utile.corpus6), 0);
        morphed.add(Morphalizer.morphalize(rawSent));
//        ESFframework framework = new ESFframework(tokens);
//        List ends = framework.getSimileEnds();
        morphed.stream().forEach(i -> {
//            new Utile().printSentence(i);
//            pl(i);
//            Phraser.phrasing(i).getPhrases().forEach(p -> pl(((Phrase) p)));
            for (int j = 0; j < i.count(); j++) {
                if (i.get(j).isVerb()) {
                    Word[] pattern = parsing(i, i.get(j));
                    pl(pattern[0] + " " + pattern[1] + " " + pattern[2]);
                }
            }

//            next();
        });
    }

    Word[] parsing(Sentence sentence, Word verb) {
        sentence = Phraser.phrasing(sentence);
        Word v = verb, s = null, o = null;
        Verb v2 = (Verb) v;
        v = v2;
        Word[] pattern = new Word[]{v, s, o};
        SubjectIdentify subId=new SubjectIdentify();
        if (verb.vox() != null && verb.vox().equals("p")) {
            return pattern;
        }
        if (VSO(sentence, verb)) {
            s=subId.VS(verb, sentence.getPhrase(verb.index()));
        }
        if (verb.index() == 0 || (verb.index() == 1 && sentence.get(verb.index() - 1).isPart())) {

        }

        return pattern;
    }

    private boolean VSO(Sentence sentence, Word verb) {
        return sentence.getPhrase(verb.index()).index() == 0;
    }

    public static void main(String[] args) {
        Parser p = new Parser();
        p.ini();
    }

}
