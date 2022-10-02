package saarr_5.annotation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import saarr_5.Document;
import saarr_5.framework.phraser.Phrase;
import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;
import saarr_5.utiles.Utile;
import static saarr_5.utiles.Utile.pl;

/**
 *
 * @author ALshmowkh
 */
public class Statistics {

    /**
     *
     * @param sentence
     */
    public static void creator(Sentence sentence) {

        Statistics statis = new Statistics();
        File htmlFile = new File(Utile.createFile());
        try {

            OutputStream os = new FileOutputStream(htmlFile);
            PrintStream printhtml = new PrintStream(os);
            String layout = statis.drawLayout(sentence);
            printhtml.println(layout);
            //
            printhtml.close();
            os.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        statis.open(htmlFile);
    }

    public static void creator(Document doc) {
        File htmlFile = new File(Utile.createFile());
        Statistics statis = new Statistics();
        try {
            OutputStream os = new FileOutputStream(htmlFile);

            PrintStream printhtml = new PrintStream(os);
            String layout = statis.drawLayout(doc);
            printhtml.println(layout);
            //
            printhtml.close();
            os.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        statis.open(htmlFile);
    }

    private String drawLayout(List sentences) {

        String headTable = "<tr bgcolor='#fafaaf'>"
                + "<td> رقم الجملة</td>"
                + "<td style='white-space: pre-wrap;'> الجملة</td>"
                + "<td>عبارات الجملة </td> "
                + "<td>المبتدأ : الخبر</td>"
                + "<td> المضاف: المضاف إليه</td>"
                + "<td> المفعول المطلق</td>"
                + "<td> الحال</td>"
                + "<td> الشتبية البليغ</td>"
                + "</tr>";
//        String row="<tr><td>SENT1</td></tr>";
        String body = "<table border='3'  bgcolor='#f5f6fa' align='center'>\n"
                + headTable
                + getRows(sentences)
                + "</table>";

        String layout = "<!DOCTYPE html>\n"
                + "<html dir='RTL'>\n"
                + "<head>\n"
                + "<style>\n"
                + "header, footer {\n"
                + "    padding: 1em;\n"
                + "    color: white;\n"
                + "    background-color: #9a121b;\n"
                + "    clear: left;\n"
                + "    text-align: center;\n"
                + "}\n"
                + "</style>\n"
                + "<meta charset=\"utf-8\"/>"
                + "</head>\n"
                + "<body>\n"
                + "\n"
//                + "<div class=\"container\">\n"
                + "\n"
                + "<header>\n"
                + "<h1>" + "Semantic Annotator for Arabic Rhetorical Relations" + "</h1>"
                + "   <h1>نتائج التحليل</h1>\n" + "<br> Analysis Results"
                + "</header>\n"
//                + "<article>\n"
                + body
//                + "</article>\n"
                + "\n"
                + "<footer>Copyright &copy; Alshmowkh_Research</footer>\n"
                + "\n"
//                + "</div>\n"
                + "\n"
                + "</body>\n"
                + "</html>";
        return layout;
    }

    private String getRows(List sentences) {
        StringBuilder rows = new StringBuilder();
        String r = "<tr>", t = "<td>", rc = "</tr>", tc = "</td>";
        String table = "<table>";
        Sentence s;
//        pl(sentences.get(0));
        for (int i = 0; i < sentences.size(); i++) {
            
            s = (Sentence) sentences.get(i);

//            String sent = morphes(s);

            rows.append(r);
            rows.append(t).append(s.id()).append(tc);
            rows.append("<td style='white-space: pre-wrap;'>").append(s.rawSentence()).append(tc);
            rows.append(t);
            if (s.getPhrases() != null) {
//                rows.append(table);
                rows.append(table);
                for (Iterator<Phrase> itr = s.getPhrases().iterator(); itr.hasNext();)//phrases != null && !phrases.isEmpty()) {
                {
                    rows.append(r).append("<td style=\"border-bottom:thin solid;border-above:thin solid;\">");
                    String phrase = itr.next().text();
                    rows.append(phrase).append(tc).append(rc);
                }
                rows.append("</table>");
            }

            rows.append(tc);

            rows.append(t).append(s.IPentity()).append(tc);
            rows.append(t).append(s.AGentity()).append(tc);
            rows.append(t).append(s.CGentity()).append(tc);
            rows.append(t).append(s.CIentity()).append(tc);
            rows.append(t).append(s.getESF()).append(tc);

//        }
//                else {
//                rows.append("<td>").append("-").append(tc);
//                rows.append("<td>").append("-").append(tc);
//                rows.append("<td>").append("-").append(tc);
//                rows.append("<td>").append("-").append(tc);
//
//            }
            rows.append(rc);

        }

        return rows.toString();
    }

    public void open(File file) {
        try {
            Desktop.getDesktop().open(file);

        } catch (IOException ex) {
            Logger.getLogger(Statistics.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String morphes(Sentence s) {
        StringBuilder sb = new StringBuilder();
        for (Word w : s) {
            sb.append("&nbsp");
            sb.append("<span title='");
            
            sb.append("diac: ").append(w.diacValue()).append("\n");
            sb.append("pos: ").append(w.tag()).append("\n");
            sb.append("clitic: ").append(w.prc0Val()).append(",").append(w.prc1Val()).append(",")
                    .append(w.prc2Val()).append(",").append(w.prc3Val()).append(",").append(w.encVal()).append("\n");
            sb.append("gen: ").append(w.gen()).append("\n");
            sb.append("gloss: ").append(w.gloss()).append("\n");
            sb.append("'>");   
            sb.append(w.value());
            sb.append("</span>"); 
         
        }
        return sb.toString();
    }

}
