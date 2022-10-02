/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saarr_5.temporary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static saarr_5.utiles.Utile.deDiacritic;
import static saarr_5.utiles.Utile.p;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author bakee
 */
public class WordNetDB {

    private static Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    private static void connectDB() {

        try {
            String DB = "F:\\Master\\Thesis\\Tools\\Ontology\\AWN\\Alshmowkh\\wordnety";
            conn = DriverManager.getConnection("jdbc:derby:" + DB);
        } catch (SQLException ex) {
            Logger.getLogger(WordNetDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void dbModify() {
        connectDB();
        try {
            ps = conn.prepareStatement("alter table word add noDiacVal varchar(50)");
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(WordNetDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void addIndiacWord() {
        String value = "";
        String noDiacVal;
        String stmt;
        int authorStart = 152917;
        int authorEnd = 176397;
        int authorshipid = authorStart;

        try { 
            connectDB();
            while (authorshipid <= authorEnd) {
               
                stmt = "select value from  word where authorshipid=" + authorshipid++ + "";
                ps = conn.prepareStatement(stmt);
                rs = ps.executeQuery();

            while (rs.next()) {
                value = rs.getString("value");
            }
            noDiacVal = deDiacritic(value);
//            pl(" : "+value + " : " + noDiacVal);
            stmt = "update word set noDiacVal='" + noDiacVal + "' where authorshipid=" + authorshipid + "";
            ps = conn.prepareStatement(stmt);
            ps.execute();
            pl(authorshipid);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(WordNetDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void ini() {

    }

    public static void main(String[] args) {
        WordNetDB wn = new WordNetDB();
//        wn.dbModify();
        wn.addIndiacWord();
//        wn.ini();
    }
}
