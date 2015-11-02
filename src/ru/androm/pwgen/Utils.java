package ru.androm.pwgen;

import java.util.ArrayList;
import java.util.HashMap;

public class Utils {
  private static final HashMap<String, String> layouts = new HashMap<String, String>();
  static {
    layouts.put("Й", "Q");
    layouts.put("Ц", "W");
    layouts.put("У", "E");
    layouts.put("К", "R");
    layouts.put("Е", "T");
    layouts.put("Н", "Y");
    layouts.put("Г", "U");
    layouts.put("Ш", "I");
    layouts.put("Щ", "O");
    layouts.put("З", "P");
    layouts.put("Х", "{");
    layouts.put("Ъ", "}");
    layouts.put("Ф", "A");
    layouts.put("Ы", "S");
    layouts.put("В", "D");
    layouts.put("А", "F");
    layouts.put("П", "G");
    layouts.put("Р", "H");
    layouts.put("О", "J");
    layouts.put("Л", "K");
    layouts.put("Д", "L");
    layouts.put("Ж", ":");
    layouts.put("Э", "\"");
    layouts.put("Я", "Z");
    layouts.put("Ч", "X");
    layouts.put("С", "C");
    layouts.put("М", "V");
    layouts.put("И", "B");
    layouts.put("Т", "N");
    layouts.put("Ь", "M");
    layouts.put("Б", "<");
    layouts.put("Ю", ">");
    layouts.put("й", "q");
    layouts.put("ц", "w");
    layouts.put("у", "e");
    layouts.put("к", "r");
    layouts.put("е", "t");
    layouts.put("н", "y");
    layouts.put("г", "u");
    layouts.put("ш", "i");
    layouts.put("щ", "o");
    layouts.put("з", "p");
    layouts.put("х", "[");
    layouts.put("ъ", "]");
    layouts.put("ф", "a");
    layouts.put("ы", "s");
    layouts.put("в", "d");
    layouts.put("а", "f");
    layouts.put("п", "g");
    layouts.put("р", "h");
    layouts.put("о", "j");
    layouts.put("л", "k");
    layouts.put("д", "l");
    layouts.put("ж", ";");
    layouts.put("э", "'");
    layouts.put("я", "z");
    layouts.put("ч", "x");
    layouts.put("с", "c");
    layouts.put("м", "v");
    layouts.put("и", "b");
    layouts.put("т", "n");
    layouts.put("ь", "m");
    layouts.put("б", ",");
    layouts.put("ю", ".");
  }

  private static final HashMap<String, String> letters = new HashMap<String, String>();
  static {
    letters.put("А", "A");
    letters.put("Б", "B");
    letters.put("В", "V");
    letters.put("Г", "G");
    letters.put("Д", "D");
    letters.put("Е", "E");
    letters.put("Ё", "E");
    letters.put("Ж", "Zh");
    letters.put("З", "Z");
    letters.put("И", "I");
    letters.put("Й", "I");
    letters.put("К", "K");
    letters.put("Л", "L");
    letters.put("М", "M");
    letters.put("Н", "N");
    letters.put("О", "O");
    letters.put("П", "P");
    letters.put("Р", "R");
    letters.put("С", "S");
    letters.put("Т", "T");
    letters.put("У", "U");
    letters.put("Ф", "F");
    letters.put("Х", "Kh");
    letters.put("Ц", "Tc");
    letters.put("Ч", "Ch");
    letters.put("Ш", "Sh");
    letters.put("Щ", "Shch");
    letters.put("Ъ", "");
    letters.put("Ы", "Y");
    letters.put("Ь", "");
    letters.put("Э", "E");
    letters.put("Ю", "Iu");
    letters.put("Я", "Ia");
    letters.put("а", "a");
    letters.put("б", "b");
    letters.put("в", "v");
    letters.put("г", "g");
    letters.put("д", "d");
    letters.put("е", "e");
    letters.put("ё", "e");
    letters.put("ж", "zh");
    letters.put("з", "z");
    letters.put("и", "i");
    letters.put("й", "i");
    letters.put("к", "k");
    letters.put("л", "l");
    letters.put("м", "m");
    letters.put("н", "n");
    letters.put("о", "o");
    letters.put("п", "p");
    letters.put("р", "r");
    letters.put("с", "s");
    letters.put("т", "t");
    letters.put("у", "u");
    letters.put("ф", "f");
    letters.put("х", "kh");
    letters.put("ц", "tc");
    letters.put("ч", "ch");
    letters.put("ш", "sh");
    letters.put("щ", "shch");
    letters.put("ъ", "");
    letters.put("ы", "y");
    letters.put("ь", "");
    letters.put("э", "e");
    letters.put("ю", "iu");
    letters.put("я", "ia");
  }

  private static String proceed(int action, String text) {
    HashMap<String, String> pairs = new HashMap<String, String>();
    if (action == 1) {
      pairs = layouts;
    } else {
      pairs = letters;
    }
    StringBuilder sb = new StringBuilder(text.length());
    for (int i = 0; i<text.length(); i++) {
      String l = text.substring(i, i+1);
      if (pairs.containsKey(l)) {
        sb.append(pairs.get(l));
      }
      else {
        sb.append(l);
      }
    }
    return sb.toString();
  }

  public static String translit(String text) {
    return proceed(2, text);
  }

  public static String invertLayout(String text) {
    return proceed(1, text);
  }

  public static String implode(ArrayList<?> elements, String delimiter) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Object element : elements) {
      stringBuilder.append(element);
      stringBuilder.append(delimiter);
    }
    stringBuilder.setLength(stringBuilder.length() - 1);
    return stringBuilder.toString(); 
  }

  public static String implode(ArrayList<?> elements) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Object element : elements) {
      stringBuilder.append(element);
    }
    return stringBuilder.toString(); 
  }

  public static boolean pregMatch(String pattern, String content) {
    return content.matches(pattern);
  }

  public static String escapeHtml(String string) {
    String escapedTxt = "";
    char tmp = ' ';
    for(int i = 0; i < string.length(); i++) {
      tmp = string.charAt(i);
      switch (tmp) {
      case '<':
        escapedTxt += "&lt;";
        break;
      case '>':
        escapedTxt += "&gt;";
        break;
      case '"':
        escapedTxt += "&quot;";
        break;
      default:
        escapedTxt += tmp;
      }
    }
    return escapedTxt;
  }

  /*
  public static void main(String[] args) {
    System.out.println(translit("Привет!"));
    System.out.println(invertLayout("Привет!"));
  }
  */
}