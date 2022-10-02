/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.temporary;

import saarr_5.morphalize.Word;

/**
 *
 * @author bakee
 */
public class Verb extends Word {

    private int verb_id;
    private String diac;
    private String nodiac;
    private boolean active;
    private char tense;
    private String pronoun;
    private int root_id;
    private int conjugation_no;
    private char transitive;

    public void verbID(int id) {
        verb_id = id;
    }

    public int verbID() {
        return verb_id;
    }


    public void rootID(int id) {
        root_id = id;
    }

    public int rootID() {
        return root_id;
    }

    public void conjugationNo(int no) {
        conjugation_no = no;
    }

    public int conjugationNo() {
        return conjugation_no;
    }

    public void diac(String diacritic) {
        diac = diacritic;
    }

    public String diac() {
        return diac;
    }

    public void nodiac(String nodiacritic) {
        nodiac = nodiacritic;
    }

    public String nodiac() {
        return nodiac;
    }

    public void pronoun(String prc) {
        pronoun = prc;
    }

    public String pronoun() {
        return pronoun;
    }

    public void tense(char tns) {
        tense = tns;
    }

    public char tense() {
        return tense;
    }

    public void transitive(char trans) {
        transitive = trans;
    }

    public char transitive() {
        return transitive;
    }
}
