/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5;

import saarr_5.framework.Entity;
import saarr_5.morphalize.Word;

/**
 *
 * @author bakee
 */
public class Simile  {

    private Word likened;
    private Word likenedTo;

    public Simile() {

    }

    public Simile(Word lik, Word likTo) {
        likened = lik;
        likenedTo = likTo;
    }

    public void setLikened(Word lik) {
        likened = lik;
    }

    public void setLikenedTo(Word likto) {
        likenedTo = likto;
    }

    public Word getLikened() {
        return likened;
    }

    public Word getLikenedTo() {
        return likenedTo;
    }

    @Override
    public String toString() {
        return likened + " : " + likenedTo;
    }
}
