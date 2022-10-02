package saarr_5.transliteration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */





/**
 *
 * @author bakee
 */
public class Transliterator {

    public static String translitrate(String str) {
        String ret = "";
        ArabicToBuckwalter atb = new ArabicToBuckwalter();
        BuckwalterToArabic bta = new BuckwalterToArabic();
           

        if (atb.containsKey(str.trim().charAt(0) + "")) 
        {
            ret = atb.transliterate(str);
        } else if (bta.containsKey(str.trim().charAt(0) + "")) {
            ret = bta.transliterate(str);
        }
        ret = ret.replaceAll("null", " ");
        return ret;
    }

//    public static void main(String[] args) {
//        if (args.length > 0) {
//            System.out.println(translitrate(args[0]));
//        }
//        while (true) {
//            System.out.println("Enter text or entery key to exit :");
//            String input = new Scanner(System.in,"windows-1256").nextLine().trim();
//            if (input.isEmpty()) {
//                break;
//            }
//            System.out.println(translitrate(input));
//        }
//    }
}
