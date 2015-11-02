package ru.androm.pwgen;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AppWindow implements ChangeListener,ActionListener {

  private Generator generator;
  private JFrame frame;
  private JTabbedPane tabbedPane;
  private JSpinner spinner;
  private JSpinner spinner_1;
  private JSpinner spinner_2;
  private JSpinner spinner_3;
  private JCheckBox checkBox;
  private JCheckBox checkBox_1;
  private JTextPane textPane;
  private JButton button;
  private JButton button_1;
  private JLabel label_4;

  public AppWindow() {
    try {
      generator = new Generator();
    } catch (Error err) {
      System.err.println(err.getMessage());
      System.exit(1);
    }
    initialize();
  }

  private void initialize() {
    frame = new JFrame("Генератор паролей");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setBounds(100, 100, 800, 260);
    frame.getContentPane().setLayout(new BorderLayout());

    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    frame.getContentPane().add(tabbedPane);

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    tabbedPane.addTab("Параметры генерации паролей", panel);

    spinner = new JSpinner();
    spinner.setModel(new SpinnerNumberModel(5, 1, 50, 1));
    spinner.addChangeListener(this);
    panel.add(spinner, new GridBagConstraints(0,GridBagConstraints.RELATIVE,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1),0,0));
    generator.setPasswordsCount(  (int) spinner.getValue()          );

    spinner_1 = new JSpinner();
    spinner_1.setModel(new SpinnerNumberModel(3, 3, 5, 1));
    spinner_1.addChangeListener(this);
    panel.add(spinner_1, new GridBagConstraints(0,GridBagConstraints.RELATIVE,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1),0,0));
    generator.setWordsCount((int) spinner_1.getValue());
    
    spinner_2 = new JSpinner();
    spinner_2.setModel(new SpinnerNumberModel(0, 0, 4, 1));
    spinner_2.addChangeListener(this);
    panel.add(spinner_2, new GridBagConstraints(0,GridBagConstraints.RELATIVE,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1),0,0));
    generator.setDigitsCount((int) spinner_2.getValue());
    
    spinner_3 = new JSpinner();
    spinner_3.setModel(new SpinnerNumberModel(3, 2, 4, 1));
    spinner_3.addChangeListener(this);
    panel.add(spinner_3, new GridBagConstraints(0,GridBagConstraints.RELATIVE,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1),0,0));
    generator.setCharactersCount((int) spinner_3.getValue());
    
    JLabel label = new JLabel("Количество создаваемых паролей");
    panel.add(label, new GridBagConstraints(1,GridBagConstraints.RELATIVE,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0));

    JLabel label_1 = new JLabel("Количество слов во фразе");
    panel.add(label_1, new GridBagConstraints(1,GridBagConstraints.RELATIVE,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0));

    JLabel label_2 = new JLabel("Количество цифр в начале фразы");
    panel.add(label_2, new GridBagConstraints(1,GridBagConstraints.RELATIVE,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0));

    JLabel label_3 = new JLabel("Количество букв из каждого слова");
    panel.add(label_3, new GridBagConstraints(1,GridBagConstraints.RELATIVE,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0));

    checkBox = new JCheckBox("Использовать заглавные буквы в начале слов");
    checkBox.addChangeListener(this);
    panel.add(checkBox, new GridBagConstraints(0,GridBagConstraints.RELATIVE,2,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0));
    generator.setUpperCaseLetter((boolean) checkBox.isSelected());
    
    checkBox_1 = new JCheckBox("Использовать транслит");
    checkBox_1.addChangeListener(this);
    panel.add(checkBox_1, new GridBagConstraints(0,GridBagConstraints.RELATIVE,2,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0));
    generator.setTransliterate((boolean) checkBox_1.isSelected());
    
    label_4 = new JLabel();
    this.count_symbols(label_4);
    panel.add(label_4, new GridBagConstraints(0,GridBagConstraints.RELATIVE,2,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0));

    button = new JButton ("Сгенерировать");
    button.addActionListener(this);
    panel.add(button, new GridBagConstraints(0,GridBagConstraints.RELATIVE,2,1,0.1,0.1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE, new Insets(1, 1, 1, 1),0,0));

    JPanel panel_1 = new JPanel();
    tabbedPane.addTab("Пароли", panel_1);
    panel_1.setLayout(new BorderLayout());

    textPane = new JTextPane();
    textPane.setEditable(false);
    textPane.setContentType("text/html");

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(textPane);
    panel_1.add(scrollPane, BorderLayout.CENTER);

    button_1 = new JButton ("Сгенерировать ещё");
    button_1.addActionListener(this);
    panel_1.add(button_1, BorderLayout.SOUTH);    
  }

  public static void main(String[] args) throws ClassNotFoundException,
                                                InstantiationException,
                                                IllegalAccessException,
                                                UnsupportedLookAndFeelException {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          AppWindow window = new AppWindow();
          //window.frame.pack();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void stateChanged(ChangeEvent e) {
    count_symbols(label_4);
         if (e.getSource().equals(spinner))    {generator.setPasswordsCount(  (int) spinner.getValue()          );}
    else if (e.getSource().equals(spinner_1))  {generator.setWordsCount(      (int) spinner_1.getValue()        );}
    else if (e.getSource().equals(spinner_2))  {generator.setDigitsCount(     (int) spinner_2.getValue()        );}
    else if (e.getSource().equals(spinner_3))  {generator.setCharactersCount( (int) spinner_3.getValue()        );}
    else if (e.getSource().equals(checkBox))   {generator.setUpperCaseLetter( (boolean) checkBox.isSelected()   );}
    else if (e.getSource().equals(checkBox_1)) {generator.setTransliterate(   (boolean) checkBox_1.isSelected() );}
  }

  private void count_symbols(JLabel label) {
    int sc = (int) spinner_3.getValue() * (int) spinner_1.getValue() + (int) spinner_2.getValue();
    label.setText("Длина пароля " + sc + " символов");
  }

  public void actionPerformed(ActionEvent e) {
    try {
      generator.generate();
      generator.escape();
      generator.highlight("<span style=\"color: red\">","</span>");
      textPane.setText(generator.printHTML());
      if (e.getSource().equals(button)) {
        tabbedPane.setSelectedIndex(1);
      }
    } catch (Error err) {
      JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}