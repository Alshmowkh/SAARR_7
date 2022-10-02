package saarr_5.transliteration;

import java.util.*;

public class ArabicToBuckwalter extends HashMap<String, String> {
    public ArabicToBuckwalter () {
	this.put("\u0621", new String("'"));
	this.put("\u0622", new String("|"));
	this.put("\u0623", new String(">"));
	this.put("\u0624", new String("&"));
	this.put("\u0625", new String("<"));
	this.put("\u0626", new String("}"));
	this.put("\u0627", new String("A"));
	this.put("\u0628", new String("b"));
	this.put("\u0629", new String("p"));
	this.put("\u062A", new String("t"));
	this.put("\u062B", new String("v"));
	this.put("\u062C", new String("j"));
	this.put("\u062D", new String("H"));
	this.put("\u062E", new String("x"));
	this.put("\u062F", new String("d"));
	this.put("\u0630", new String("*"));
	this.put("\u0631", new String("r"));
	this.put("\u0632", new String("z"));
	this.put("\u0633", new String("s"));
	this.put("\u0634", new String("$"));
	this.put("\u0635", new String("S"));
	this.put("\u0636", new String("D"));
	this.put("\u0637", new String("T"));
	this.put("\u0638", new String("Z"));
	this.put("\u0639", new String("E"));
	this.put("\u063A", new String("g"));
	this.put("\u0640", new String("_"));
	this.put("\u0641", new String("f"));
	this.put("\u0642", new String("q"));
	this.put("\u0643", new String("k"));
	this.put("\u0644", new String("l"));
	this.put("\u0645", new String("m"));
	this.put("\u0646", new String("n"));
	this.put("\u0647", new String("h"));
	this.put("\u0648", new String("w"));
	this.put("\u0649", new String("Y"));
	this.put("\u064A", new String("y"));
	this.put("\u064B", new String("F"));
	this.put("\u064C", new String("N"));
	this.put("\u064D", new String("K"));
	this.put("\u064E", new String("a"));
	this.put("\u064F", new String("u"));
	this.put("\u0650", new String("i"));
	this.put("\u0651", new String("~"));
	this.put("\u0652", new String("o"));
	this.put("\u0670", new String("`"));
	this.put("\u0652", new String("{"));
    }

  public String transliterate(String s) {
    String ret = "";
    for (int i=0;i<s.length();i++) {
      ret +=this.get(""+s.charAt(i));
    }
    return ret;
  }
}
