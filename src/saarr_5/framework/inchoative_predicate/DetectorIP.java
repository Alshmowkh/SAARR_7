/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.inchoative_predicate;

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
public class DetectorIP {
    
    private Sentence sentence;
    private Sentence modSent;
    private boolean state;
    
    public DetectorIP(Sentence sent) {
        sentence = sent;
        state = checkPreliminary();
    }
    
    public Entity getEntity() {
        if (!state) {
            return null;
        }
        
        Entity entity = detector(modSent);
        if (entity != null) {
            sentence.IPentity(entity);
        }
//        pl(entity);
        return entity;
    }
    
    public Entity getEntityESF() {
        if (!state) {
            return null;
        }
        
        Entity entity = detectorESF(modSent);
//        pl(entity);
        sentence.IPentity(entity);
        
        return entity;
    }
    
    private Entity detector(Sentence sentence) {
        Word inchoHead;
        Word predHead;

//        pl(sentence.getPhrases());
        Entity entity = null;// = new Simile();
        Inchoative inchoative = new Inchoative(sentence);
        inchoHead = inchoative.getHead();
        
        if (inchoHead == null) {
            return null;
        }
        
        Predicate predicate = new Predicate(sentence, inchoative);

//        pl(inchoative.getInchoative());
//        pl(predicate.getPredicate());
        predHead = predicate.getHead();
        if (predHead == null) {
            return null;
        }
        
        entity = new Entity(inchoHead, predHead, Type.ESF_IP);
        return entity;
    }
    
    private Entity detectorESF(Sentence sent) {
        Word inchoHead;
        Word predHead;

//        pl(sent.getPhrases());
        Entity entity = null;// = new Simile();
        Inchoative inchoative = new Inchoative(sent);
      
        inchoHead = inchoative.getHead(); 
//        pl(inchoative.getInchoative());
//        pl(inchoHead);
        if (inchoHead == null) {
            return null;
        }  
       
        if (inchoHead.tag().equals("pron_dem")) {
            
        }
        Predicate predicate = new Predicate(sent, inchoative);

//        pl(inchoative.getInchoative());
//        pl(predicate.getPredicate());
        predHead = predicate.getHead();
//        pl(predHead);
        if (predHead == null) {
            return null;
        }
        if (!isESF(predHead)) {
            return null;
        }
//        pl(inchoHead + " : " + predHead);
        entity = new Entity(inchoHead, predHead, Type.ESF_IP);
        return entity;
    }
    
    private boolean checkPreliminary() {
        if (sentence == null || sentence.isEmpty()) {
            System.out.println("There is no sentene:" + sentence);
            return false;
        }
        modSent = sentence;
        modSent.removePousdo();
//        sentence.removeInsidePunc();
//        pl(sentence);
        Word fw = modSent.get(0);
        while (fw.isAdv() || fw.isPousdoVerb() || fw.isPunc() || fw.isPartNeg()) {
            modSent = modSent.removeWordAndReorder(0);
            fw = modSent.get(0);
        }
        
        modSent = Phraser.rePhrasing(modSent);
        if (modSent.getPhrases() == null) {
            return false;
        }
        return true;
    }
    
    private boolean isESF(Word pred) {
        return pred.isNoun();
    }
}
