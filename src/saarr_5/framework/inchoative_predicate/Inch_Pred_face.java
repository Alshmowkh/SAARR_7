/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.framework.inchoative_predicate;

import saarr_5.framework.phraser.Phrase;
import saarr_5.morphalize.Word;

/**
 *
 * @author bakee
 */
public interface Inch_Pred_face {

    Phrase getPhrase();

    int getBeginInedx();

    int getEndInedx();

    Word getHead();

}
