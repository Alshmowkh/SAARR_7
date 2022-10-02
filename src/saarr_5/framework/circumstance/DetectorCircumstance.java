/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.circumstance;

import java.util.List;
import saarr_5.Type;
import saarr_5.framework.Entity;
//import saarr_5.framework.Entity.Type;
import saarr_5.framework.phraser.Phraser;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class DetectorCircumstance {

    private final Sentence sentence;

    public DetectorCircumstance(Sentence sent) {
        sentence = sent;
    }

    public Entity getEntity() {

        if (!sentence.hasVerb()) {
//            System.out.println("No verb.");
            return null;
        }
        Entity entity = detector(sentence);

        sentence.CIentity(entity);

        return entity;

    }

    private Entity detector(Sentence sentence) {

//        Entity entity = null;
        Circumstance circum = new Circumstance();

        Entity cirenty = circum.identify(sentence);
//        pl(cirenty);

        if (cirenty != null) {
            return new Entity(cirenty.getAlpha(), cirenty.getBeta(), cirenty.getType());
        }

        return null;
    }

}
