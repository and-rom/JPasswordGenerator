package ru.androm.pwgen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Generator {

  private int wordsCount;
  private int digitsCount;
  private boolean upperCaseLetter;
  private int charactersCount;
  private int passwordsCount;
  private boolean transliterated = false;
  private ArrayList<HashMap<String, String>> passwords;
  private Connection connection;

  public Generator(
      int passwordsCount,
      int wordsCount,
      int digitsCount,
      int charactersCount,
      boolean upperCaseLetter,
      boolean transliterate) throws ClassNotFoundException, SQLException {
    this.setPasswordsCount(passwordsCount);
    this.setWordsCount(wordsCount);
    this.setDigitsCount(digitsCount);
    this.setCharactersCount(charactersCount);
    this.setUpperCaseLetter(upperCaseLetter);
    this.setTransliterate(transliterate);
    this.connect();
    this.passwords = new ArrayList<HashMap<String, String>>();
  }
  
  public Generator()  throws Error {
    this.connect();
    this.passwords = new ArrayList<HashMap<String, String>>();
  }

  public void setPasswordsCount(int passwordsCount) {
    this.passwordsCount = (passwordsCount >= 1 && passwordsCount <= 50 ? passwordsCount : 5 );
  }

  public void setWordsCount(int wordsCount) {
    this.wordsCount = (wordsCount >= 3 && wordsCount <= 5 ? wordsCount : 3 );
  }

  public void setDigitsCount(int digitsCount) {
    this.digitsCount = (digitsCount >= 0 && digitsCount <= 4 ? digitsCount : 0 );
  }

  public void setCharactersCount(int charactersCount) {
    this.charactersCount = (charactersCount >= 2 && charactersCount <= 4 ? charactersCount : 3 );
  }

  public void setUpperCaseLetter(boolean upperCaseLetter) {
    this.upperCaseLetter = upperCaseLetter;
  }

  public void setTransliterate(boolean transliterate) {
    this.transliterated = transliterate;
  }

  private void connect()  throws Error {
    this.connection = null;
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      throw new Error("Не найдена библиотека org.sqlite.");
    }
    try {
      
      this.connection = DriverManager.getConnection("jdbc:sqlite::resource:" + getClass().getResource("/pwgen.sqlite").toString());
      //this.connection = DriverManager.getConnection("jdbc:sqlite::resource:pwgen.sqlite");
      //this.connection = DriverManager.getConnection("jdbc:sqlite:pwgen.sqlite");
    } catch (SQLException e) {
      throw new Error("Не найден файл базы данных.");
    }
  }

  public void generate() throws Error {
    if (this.passwords.size() != 0) this.passwords.clear();
    try {
      this.generateSentences();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      throw new Error("Ошибка при запросе к базе даннх.");
    }
    if (this.transliterated) {this.transliterate();}
    this.generatePasswords();
  }

  private void generatePasswords() {
    for (int i = 0; i < this.passwords.size(); i++) {
      String[] words = ((String) this.passwords.get(i).get("sentence")).split(" ");
      ArrayList<String> password = new ArrayList<String>();
      for (String word : words) {
        if (Utils.pregMatch("^[0-9]+$", word)) {
          password.add(word);
        } else {
          String partToPassword = word.substring(0, this.charactersCount);
          if (this.transliterated) {
            password.add(partToPassword);
          } else {
            password.add(Utils.invertLayout(partToPassword));
          }
        }
      }
      this.passwords.get(i).put("password", Utils.implode(password));
    }
  }

  public void transliterate() {
    for (int i = 0; i < this.passwords.size(); i++) {
      this.passwords.get(i).put("sentence", Utils.translit((String) this.passwords.get(i).get("sentence")));
    }
  }

  private void generateSentences() throws SQLException {
    for (int i = 0; i < this.passwordsCount; i++) {
      int number = this.generateNumber();
      int ps_type = this.getPluralType(number);
      String[] words = {null, null, null, null, null, null};
      if (number > 1) {
        words[0] = Integer.toString(number);
      }
      HashMap<String,String> subject = this.getSubject(ps_type);
      words[2] = subject.get("word").toString();
      HashMap<String,String>  predicate = this.getPredicate(subject.get("ps"),(subject.get("ps").equals("p") ? "-" : subject.get("g")));
      words[3] = predicate.get("word").toString();
      HashMap<String,String>  object = this.getObject();
      words[5] = object.get("word").toString();
      if (this.wordsCount == 4 | this.wordsCount == 5) {
        HashMap<String,String>  attribute1 = this.getAttribute1(ps_type,(ps_type == 2 ? "-" : subject.get("g")));
        words[1] = attribute1.get("word").toString();
      }
      if (this.wordsCount == 5) {
        HashMap<String,String>  attribute2 = this.getAttribute2(object.get("ps"),object.get("g"),object.get("alt_case"));
        words[4] = attribute2.get("word").toString();
      }
      ArrayList<String> sentence = new ArrayList<String>();
      for(int j = 0; j <= words.length - 1; j++) {
        if (words[j] == null) continue;
        if (this.upperCaseLetter) { words[j] = words[j].substring(0, 1).toUpperCase() + words[j].substring(1);}
        sentence.add(words[j]);
      }
      HashMap<String,String> passw_sen = new HashMap<String,String>();
      passw_sen.put("sentence", Utils.implode(sentence," "));
      this.passwords.add(passw_sen);
    }
  }

  private HashMap<String, String> getSubject(int ps_type) throws SQLException {
    String query = "SELECT `word`,`g`,`ps` FROM `subject` WHERE `ps_type`='" + ps_type + "' ORDER BY RANDOM() LIMIT 0,1";
    Statement statement = this.connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    return this.dbQuery(resultSet);
}

  private HashMap<String, String> getPredicate(String ps, String g) throws SQLException {
    String query = "SELECT `word` FROM `predicate` WHERE `ps`='"+ps+"' AND `g`='"+g+"' ORDER BY RANDOM() LIMIT 0,1";
    Statement statement = this.connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    return this.dbQuery(resultSet);
  }

  private HashMap<String, String> getObject() throws SQLException {
    String query = "SELECT `word`,`g`,`ps`,`alt_case` FROM `object` ORDER BY RANDOM() LIMIT 0,1";
    Statement statement = this.connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    return this.dbQuery(resultSet);
  }

  private HashMap<String, String> getAttribute1(int ps_type, String g) throws SQLException {
    String query = "SELECT `word` FROM `attrib` WHERE `ps_type`="+ps_type+" AND `g`='"+g+"' AND `case`='nom' ORDER BY RANDOM() LIMIT 0,1";
    Statement statement = this.connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    return this.dbQuery(resultSet);
  }

  private HashMap<String, String> getAttribute2(String ps, String g, String ac) throws SQLException {
    String query = "SELECT `word` FROM `attrib` WHERE `ps`='"+ps+"' AND `g`='"+g+"' AND `case`='acc' AND `alt_case`='"+ac+"' ORDER BY RANDOM() LIMIT 0,1";
    Statement statement = this.connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    return this.dbQuery(resultSet);
  }

  private HashMap<String, String> dbQuery(ResultSet resultSet) throws SQLException {
    ResultSetMetaData md = resultSet.getMetaData();
    int columns = md.getColumnCount();
    HashMap<String, String> row = new HashMap<String, String>();
    for(int i=1; i<=columns; ++i) {
      row.put(md.getColumnName(i).toString(),resultSet.getObject(i).toString());
    }
    return row;
  }

  private int getPluralType(int number) {
    int ending;
    number = number % 100;
    if (number >= 11 && number <= 19) {
      ending = 2;
    } else {
      int i = number % 10;
      switch (i) {
      case (1): ending = 0; break;
      case (2):
      case (3):
      case (4): ending = 1; break;
      default: ending=2;
      }
    }
    return ending;
  }

  private int generateNumber() {
    int number;
    switch (this.digitsCount) {
    case 0: number = 1; break;
    case 1: number = (int)(Math.random() * ((9 - 2) + 1)) + 2; break;
    case 2:
    case 3:
    case 4: 
      int min = (int) Math.pow(10, this.digitsCount-1);
      int max = (int) Math.pow(10, this.digitsCount)-1;
      int range = (max - min) + 1;
      number = (int)(Math.random() * range) + min;
      break;
    default: number = 1;
    }
    return number;
  }

  public void escape () {
    for(int i = 0; i < this.passwords.size(); i++) {
      this.passwords.get(i).put("password", Utils.escapeHtml(this.passwords.get(i).get("password").toString()));
    }
  }

  public void highlight (String highlighter_s, String highlighter_f) {
    for (int i = 0; i < this.passwords.size(); i++) {
      String[] words = ((String) this.passwords.get(i).get("sentence")).split(" ");
      ArrayList<String> sentence = new ArrayList<String>();
      for (int j = 0; j < words.length; j++) {
        if (Utils.pregMatch("^[0-9]+$", words[j])) {
          words[j] = highlighter_s + words[j] + highlighter_f;
        } else {
          words[j] = highlighter_s + words[j].substring(0, this.charactersCount) + highlighter_f + words[j].substring(this.charactersCount);
        }
        sentence.add(words[j]);
      }
      this.passwords.get(i).put("sentence", Utils.implode(sentence, " "));
    }
  }

  public String print() {
    StringBuilder stringBuilder = new StringBuilder();
    for (HashMap<?, ?> pair : this.passwords){
      String password = (String) pair.get("password");
      String sentence = (String) pair.get("sentence");
      if (password != null) {
        stringBuilder.append(password + " ");
      }
      stringBuilder.append(sentence + "\n");
    }
    return stringBuilder.toString();
  }

  public String printHTML() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<pre style=\"white-space: pre-wrap;\">\n");
    for (HashMap<?, ?> pair : this.passwords){
      String password = (String) pair.get("password");
      String sentence = (String) pair.get("sentence");
      if (password != null) {
        stringBuilder.append(password + "&nbsp;&nbsp;&nbsp;&nbsp;");
      }
      stringBuilder.append(sentence + "\n");
    }
    stringBuilder.append("</pre>\n");
    return stringBuilder.toString();
  }

  public void finalize() {

  }

  /*
  public static void main(String[] args) {
    Generator generator = new Generator(10, 5, 1, 10, true, false);
    generator.generate();
    generator.escape();
    generator.highlight("","'");
    System.out.println(generator.printHTML());
  }
  */
}