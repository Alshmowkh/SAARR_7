/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.semantic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import saarr_5.Simile;
import saarr_5.framework.Entity;
import saarr_5.morphalize.Sentence;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class SemanticLayer {

    List candidates;
    List esfs;
    Sentence sentence;

    public SemanticLayer(List candid) {
        candidates = candid;
    }

    public SemanticLayer(Sentence sent) {
        sentence = sent;
    }

    public List getESF() {
        if (sentence != null) {
            candidates = sentence.IP_AG_CG_CI();
        }
        esfs = new ArrayList();
        Entity entity;
        AWN awn = new AWN();
        Simile simile;
//        pl(candidates);
        for (Iterator itr = candidates.iterator(); itr.hasNext();) {
            entity = (Entity) itr.next();
            if (entity != null) {
                if (awn.isESF(entity)) {
                    esfs.add(entity);
//                    simile = new Entity(entity.getAlpha(), entity.getBeta(),entity.getType());
                    sentence.setESF(entity);
                }
            }
        }
        return esfs;
    }

    public Simile getSimile() {
        if (esfs == null) {
            esfs = this.getESF();
        }
        if (esfs.isEmpty()) {
            return null;
        }
        Entity entity = (Entity) esfs.get(0);
        Simile simile = new Simile(entity.getAlpha(), entity.getBeta());
        sentence.setESF(entity);
        return simile;
    }
}
