/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import saarr_5.Document;
import saarr_5.Simile;
import saarr_5.framework.annexed_genitive.DetectorIdafa;
import saarr_5.framework.circumstance.DetectorCircumstance;
import saarr_5.framework.cognative.DetectorCognate;
import saarr_5.framework.inchoative_predicate.DetectorIP;
import saarr_5.framework.phraser.Phraser;
import saarr_5.framework.semantic.AWN;
import saarr_5.framework.semantic.SemanticLayer;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class ESFframework {

    private Word candidateAlpha;
    private Word candidateBeta;
    private Sentence sentence;

    List candidates;

    public ESFframework(Sentence morphed) {
        sentence = Phraser.phrasing(morphed);
    }

    public List getCandidates() {
        candidates = new ArrayList();

        candidates.add(new DetectorIP(sentence).getEntity());
//        candidates.addAll(new DetectorIdafa(sentence).getEntities());
//        candidates.add(new DetectorCognate(sentence).getEntityESF());
//        candidates.add(new DetectorCircumstance(sentence).getEntity());

        return candidates;
    }

    public List getCandidateSimileEnds() {
        candidates = new ArrayList();

        candidates.add(new DetectorCognate(sentence).getEntityESF());
        if (sentence.CGentity() != null) {
            return candidates;
        }
        candidates.add(new DetectorIP(sentence).getEntityESF());
        candidates.add(new DetectorIdafa(sentence).getEntitiesESF());
        candidates.add(new DetectorCircumstance(sentence).getEntity());

        return candidates;

    }

    public Simile getSimile() {
        if (candidates == null) {
            candidates = this.getCandidateSimileEnds();
        }
        SemanticLayer semantic = new SemanticLayer(candidates);
        Simile simile = semantic.getSimile();
        return simile;
    }

    public Simile getSimile(Sentence sent) {
        if (!sent.hasCandidateESF()) {
            return null;
        }
        SemanticLayer semantic = new SemanticLayer(sent);
        Simile simile = semantic.getSimile();
        return simile;
    }

    public Entity getESF() {
        if (!sentence.hasCandidateESF()) {
            return null;
        }
//        List esfs = new ArrayList();
        
        Entity entity;
        AWN awn = new AWN();

        for (Iterator itr = candidates.iterator(); itr.hasNext();) {
            entity = (Entity) itr.next();

            if (entity != null) {
                if (entity.getAlpha().tag().equals("pron_dem") || entity.getBeta().tag().equals("pron_dem")) {
//                    pl("Determine_pronoun error");
                    return null;
                }
                if (awn.isESF(entity)) {
//                    esfs.add(entity);
//                    simile = new Entity(entity.getAlpha(), entity.getBeta(),entity.getType());
                    sentence.setESF(entity);
                    return entity;
                }
            }
        }
        return null;
    }

}
