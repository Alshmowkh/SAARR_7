/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.temporary;

import saarr_5.utiles.Utile;

/**
 *
 * @author bakee
 */
public class Experiences {

    public static void main(String[] args) {
        Experiences ex = new Experiences();
        ex.exp1();
    }

    private void exp1() {
        int x = -1;
        Utile.pl(x < 0 ? "yes" : "no");
    }
}
