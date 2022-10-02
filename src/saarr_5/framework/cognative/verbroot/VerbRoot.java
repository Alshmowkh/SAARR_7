package saarr_5.framework.cognative.verbroot;

import java.util.Scanner;

/**
 *
 * @author bakee
 */
public class VerbRoot {

    private static Dictionary dic;

    public VerbRoot() {
        dic=new Dictionary();
    }

    public VerbRoot(String pathDoc) {
        dic = new Dictionary();
        dic.path = pathDoc;
    }

    public void setDBpath(String pa) {
        dic = new Dictionary();
        dic.path = pa;
    }

    public static String detect(String verb) {
        verb = verb.toLowerCase().trim();
        if (dic == null) {
//            System.out.println("You must set database path first...");
//            return null;
            dic = new Dictionary();

        }
        String root = dic.getRoot(verb);
        
        return root;
    }

    void processing() {
        String verb = "فاز";
        while (true) {
            System.out.print("Enter a verb: ");
            verb = new Scanner(System.in, "windows-1256").nextLine();
            if (verb.equals("")) {
                break;
            }
            System.out.println(detect(verb));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VerbRoot d = new VerbRoot();
        if (args.length > 0) {
            System.out.println(d.detect(args[0].trim()));
        } else {
            d.processing();
        }
    }

}
/*
مشكلات نظام اكتشاف الجذر
1- عدم القدرة على اكتشاف جذر افعال الجمل التالية  
    1- تمر في المثال تمر الايام مرور السحب 
    2- الفعل يكتب في  المثال يكتب محمد كتابة الطابعة
    3- الفعل سير في سرت سير السلحفاة
    4- الفعل يتلون
    5- الفعل المعتل صاح
    6- الفعل يعذب في الجمله يعذبه الله العذاب الاكبر
    7- الفعل سعى في الجملة وسعى لها سعيها
    
2- 
*/
