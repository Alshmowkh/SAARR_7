/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.cognative;

import java.util.Iterator;
import java.util.List;
import saarr_5.Type;
import saarr_5.framework.Entity;
import saarr_5.framework.Subjector;
//import saarr_5.framework.cognative.CognaterEntity.Type;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class DetectorCognate {

    private final Sentence sentence;

    public DetectorCognate(Sentence sent) {
        sentence = sent;
    }

    public Entity getEntity() {

        if (!sentence.hasVerb()) {
//            System.out.println("No verb.");
            return null;
        }
        Entity entity = new Cognater().cognating2(sentence);
//        if (entity != null) {
//            entity = getSubject(entity);
//        }
//        if (entity != null) {
//            sentence.CIentity(entity);
//        }
        return entity;
    }

    public Entity getEntityESF() {

        if (!sentence.hasVerb()) {
//            System.out.println("No verb.");
            return null;
        }
        Entity entity = new Cognater().cognating3(sentence);// detector2(sentence);

        if (entity != null && entity.getType() == Type.Cognater_DESCRIPTIVE) {
            if (entity.getBeta() != null && (entity.getBeta().isNoun() || entity.getBeta().isDTnoun())) {

                entity.setBeta(sentence.get(entity.getBeta().index() + 1));

            } else {
                return null;
            }
            Word subject;
//            subject= new Described_Adjective(sentence, entity).getSubject();
//            pl(subject);
            subject = new Subjector(sentence).subject();
//            pl(subject);
            entity.setAlpha(subject);
        }
        sentence.CIentity(entity);
        return entity;
    }
}
