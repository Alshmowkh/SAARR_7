/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5;

/**
 *
 * @author bakee
 */
public enum Type {

    ESF_IP, ESF_AG, ESF_Cg, ESF_Cr,
    Cognater_EMPHASIS, Cognater_DESCRIPTIVE, Cognater_NUMERICAL;
    @Override
    public String toString(){
       return this.name();
    }
    public String trim(){
        return this.name().split("_")[1];
    }
}
