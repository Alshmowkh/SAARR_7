/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.annexed_genitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import saarr_5.Type;
import saarr_5.framework.Entity;
import saarr_5.framework.phraser.Phrase;
import saarr_5.framework.phraser.Phraser;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class DetectorIdafa {

    private Sentence sentence;

    public DetectorIdafa(Sentence sent) {
        sentence = sent;
    }

    public Entity getEntity() {
        if (sentence.getPhrases() == null) {
            sentence = Phraser.phrasing(sentence);

        }
        if (sentence.getPhrases() == null) {
            return null;
        }
        Entity entity = detector(sentence);
        if (entity != null) {
            sentence.AGentity(Arrays.asList(entity));
        }
        return entity;
    }

    public List getEntities() {
        if (sentence.getPhrases() == null) {
            sentence = Phraser.phrasing(sentence);

        }
        if (sentence.getPhrases() == null) {
            return null;
        }
        List entities = detectorAll(sentence);
        sentence.AGentity(entities);
        return entities;
    }

    public Entity getEntitiesESF() {
        if (sentence.getPhrases() == null) {
            sentence = Phraser.phrasing(sentence);

        }
        if (sentence.getPhrases() == null) {
            return null;
        }
        List entities = detectorAllESF(sentence);
        sentence.AGentity(entities);
        if (entities == null ||entities.isEmpty()) {
            return null;
        }
        Entity entity=(Entity) entities.get(0);
        
        return entity;
    }

    private Entity detector(Sentence sentence) {
        Word alpha;
        Word beta;
        Entity entity;
        AnnGenDetector detr;
        Phrase phrase;
        AnnGen ag;
        Iterator<Phrase> itr = sentence.getPhrases().iterator();
        while (itr.hasNext()) {
            phrase = itr.next();
//            pl(phrase);
            if (phrase.idafa()) {
                detr = new AnnGenDetector(phrase);
                ag = detr.getAnnGen();
                if (ag != null) {
                    alpha = ag.getAnnexed();
                    beta = ag.getGenitive();
                    entity = new Entity(alpha, beta, Type.ESF_AG);
                    return entity;
                }
            }

        }

        return null;
    }

    private List detectorAll(Sentence sentence) {
        Word alpha;
        Word beta;
        Entity entity;
        List entities = new ArrayList();
        AnnGenDetector detr;
        Phrase phrase;
        List<AnnGen> anngens = new ArrayList();
        AnnGen ag;
        Iterator<Phrase> itr = sentence.getPhrases().iterator();
        while (itr.hasNext()) {
            phrase = itr.next();
//            pl(phrase);
            if (phrase.idafa()) {
                detr = new AnnGenDetector(phrase);
                ag = detr.getAnnGen();
                if (ag != null) {
                    anngens.add(ag);
                }
            }

        }

        for (Iterator itr2 = anngens.iterator(); itr2.hasNext();) {
            ag = (AnnGen) itr2.next();
            alpha = ag.getAnnexed();
            beta = ag.getGenitive();
            if (alpha != null && beta != null) {
                entity = new Entity(alpha, beta);
                entity.setType(Type.ESF_AG);
                entities.add(entity);
            }

        }

        return entities;
    }

    private List detectorAllESF(Sentence sentence) {
        Word alpha;
        Word beta;
        Entity entity;
        List entities = new ArrayList();
        AnnGenDetector detr;
        Phrase phrase;
        List<AnnGen> anngens = new ArrayList();
        AnnGen ag;
        Iterator<Phrase> itr = sentence.getPhrases().iterator();
        while (itr.hasNext()) {
            phrase = itr.next();
//            pl(phrase);
            if (phrase.idafa()) {
                detr = new AnnGenDetector(phrase);
                ag = detr.getAnnGenESF();
                if (ag != null) {
                    anngens.add(ag);
                }
            }

        }

        for (Iterator itr2 = anngens.iterator(); itr2.hasNext();) {
            ag = (AnnGen) itr2.next();
            alpha = ag.getAnnexed();
            beta = ag.getGenitive();
            if (alpha != null && beta != null) {
                entity = new Entity(alpha, beta);
                entity.setType(Type.ESF_AG);
                entities.add(entity);
            }

        }

        return entities;
    }

}
