/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5;

import java.util.List;
import saarr_5.framework.ESFframework;
import saarr_5.morphalize.Morphalizer;
import saarr_5.morphalize.Sentence;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class SAARR {

    void ini() {

        String rawSent = "الدكتور خليل موسوعة.";
        Sentence morphed = Morphalizer.morphalize(rawSent);
        ESFframework framework=new ESFframework(morphed);
        List ends=framework.getCandidateSimileEnds();
        pl(ends);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SAARR saarr = new SAARR();
        saarr.ini();
        
    }

}
