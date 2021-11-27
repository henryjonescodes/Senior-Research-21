package Match3.View;
/*
 * @author Henry Jones
 */
import Match3.Listeners.*;
import Match3.Document.*;
import Match3.IO.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Match3Container extends JPanel implements ActionListener, GameOverListener{

  private JFileChooser fileChooser;
  private Match3 match3;
  private ScoreBoard sb;
  private JButton loadBtn,btn1,btn2,btn3,btn4,btn5;
  private JPanel loadPanel;
  private BorderLayout layout;
  private Vector<ExitListener> listeners;
  private boolean exitRequested = false;
  private GameIO io_manager;
  private Logger logger;
  private int logCounter, scoreTarget;
  private File logLocation;
  private boolean gameIsOver;
  private JPanel gameOverPanel;


  public Match3Container(){
    logCounter = 1;
    gameIsOver = false;
    io_manager = new GameIO();
    listeners = new Vector<ExitListener>();
    fileChooser = new JFileChooser();
    layout = new BorderLayout();
    loadBtn = new JButton("Load");
    btn1 = new JButton("1");
    btn2 = new JButton("2");
    btn3 = new JButton("3");
    btn4 = new JButton("4");
    btn5 = new JButton("5");
    btn1.addActionListener(this);
    btn2.addActionListener(this);
    btn3.addActionListener(this);
    btn4.addActionListener(this);
    btn5.addActionListener(this);

    loadBtn.addActionListener(this);
    loadPanel = new JPanel();
    loadPanel.setLayout(new FlowLayout());
    loadPanel.add(loadBtn);
    loadPanel.add(btn1);
    loadPanel.add(btn2);
    loadPanel.add(btn3);
    loadPanel.add(btn4);
    loadPanel.add(btn5);
    logLocation = getSaveLocation("");

    // try {
    //     BufferedImage myPicture = ImageIO.read(new File("src/main/java/Match3/Icons/icon1.png"));
    //     Image scaled = myPicture.getScaledInstance(100,100,Image.SCALE_SMOOTH);
    //     ImageIcon testIcon = new ImageIcon(scaled);
    //     loadBtn.setIcon(testIcon);
    // } catch (Exception ex) {
    //    System.out.println(ex);
    // }
    this.setLayout(layout);

    this.add(loadPanel, BorderLayout.PAGE_START);
    this.add(makeToolbar(), BorderLayout.PAGE_END);

    // this.setPreferredSize(new Dimension(1000,1000));

    layout.layoutContainer(this);
  }

  public File getSaveLocation(String extension){
    fileChooser.addChoosableFileFilter(new IO_Filter());
    fileChooser.setDialogTitle("Specify a filename and path");
    int userSelection = fileChooser.showSaveDialog(this);
    File fileToSave = new File("");

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      fileToSave = fileChooser.getSelectedFile();
    }

    File renamedFile = new File(fileToSave.getAbsolutePath() + extension);

    // System.out.println("Save as file: " + fileToSave.getAbsolutePath());
    return renamedFile;
  }

  public File getImportLocation(){
    fileChooser.addChoosableFileFilter(new IO_Filter());
    fileChooser.setDialogTitle("Specify a filename and path");
    int userSelection = fileChooser.showOpenDialog(this);
    File fileToSave = new File("");

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      fileToSave = fileChooser.getSelectedFile();
    }

    // System.out.println("Save as file: " + fileToSave.getAbsolutePath());
    return fileToSave;
  }

  private void loadFromImported(DecisionTree imported){
    this.remove(loadPanel);
    match3 = new Match3(imported);
    sb = new ScoreBoard();
    match3.addListener(sb);
    match3.addListener(this);
    this.add(sb,BorderLayout.PAGE_START);
    this.add(match3,BorderLayout.CENTER);
    // layout.layoutContainer(this);

    update();
    }

  /**
  * updates the view
  */
  public void update(){
    layout.layoutContainer(this);
    setVisible(true);
    revalidate();
    repaint();
  }

  public JToolBar makeToolbar(){
    JToolBar toolBar = new JToolBar();
    JButton exit,menu;
    menu = new JButton("Menu");
    exit = new JButton("Exit");

    menu.addActionListener(this);
    exit.addActionListener(this);

    toolBar.add(menu);
    toolBar.add(exit);
    toolBar.setFloatable(false);

    return toolBar;
  }

  // handle button presses
  public void actionPerformed(ActionEvent e) {
      System.out.println("(Match3Container)" + e.getActionCommand());
      File loadLoc;
      DecisionTree importedTree;
      switch(e.getActionCommand()) {
          case "Exit":
              exitRequested = true;
              // match3.removeListener(this);
              // match3 = null;
              // sb = null;
              notifyListeners();
              break;
          case "Menu":
              this.remove(match3);
              this.remove(sb);
              if(gameIsOver){
                this.remove(gameOverPanel);
              }
              gameIsOver = false;
              match3 = null;
              this.add(loadPanel, BorderLayout.PAGE_START);
              update();
              break;
          case "Load":
              loadLoc = getImportLocation();
              if(loadLoc.toString() == ""){
                break;
              } else {
                importedTree = io_manager.readFromFile(loadLoc);
                loadFromImported(importedTree);
                break;
              }
          case "1":
              loadLoc = new File("src/main/java/Match3/Scenarios/Scenario1.txt");
              scoreTarget = 0;
              importedTree = io_manager.readFromFile(loadLoc);
              loadFromImported(importedTree);
              break;
          case "2":
              loadLoc = new File("src/main/java/Match3/Scenarios/Scenario2.txt");
              scoreTarget = 12;
              importedTree = io_manager.readFromFile(loadLoc);
              loadFromImported(importedTree);
              break;
          case "3":
              loadLoc = new File("src/main/java/Match3/Scenarios/Scenario3.txt");
              scoreTarget = 14;
              importedTree = io_manager.readFromFile(loadLoc);
              loadFromImported(importedTree);
              break;
          case "4":
              loadLoc = new File("src/main/java/Match3/Scenarios/Scenario4.txt");
              scoreTarget = 12;
              importedTree = io_manager.readFromFile(loadLoc);
              loadFromImported(importedTree);
              break;
          case "5":
              loadLoc = new File("src/main/java/Match3/Scenarios/Scenario5.txt");
              scoreTarget = 12;
              importedTree = io_manager.readFromFile(loadLoc);
              loadFromImported(importedTree);
              break;
          default:
              System.out.println("(Match3Container)Invalid Command");
      }
  }

  public void gameOver(int score, Logger log){
    logger = log;
    String scoreString = "Points: " + String.valueOf(score);
    String targetString = "Target: " + String.valueOf(scoreTarget);

    JLabel message = new JLabel("GAME OVER");
    JLabel scoreTargetMessage = new JLabel(targetString);
    JLabel scoreMessage = new JLabel(scoreString);

    gameOverPanel = new JPanel();
    gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));

    scoreTargetMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
    message.setAlignmentX(Component.CENTER_ALIGNMENT);
    scoreMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

    message.setFont(new Font("Verdana", Font.PLAIN, 40));
    scoreMessage.setFont(new Font("Verdana", Font.PLAIN, 35));
    scoreTargetMessage.setFont(new Font("Verdana", Font.PLAIN, 20));

    if(score >= scoreTarget){
      scoreMessage.setForeground(Color.green);
    } else {
      scoreMessage.setForeground(Color.red);
    }

    gameOverPanel.add(message);
    gameOverPanel.add(scoreMessage);
    gameOverPanel.add(scoreTargetMessage);

    // gameOverPanel.add(message, BorderLayout.PAGE_START);
    // gameOverPanel.add(scoreMessage, BorderLayout.PAGE_END);
    match3.removeListener(this);
    this.remove(match3);
    this.remove(sb);
    this.add(gameOverPanel, BorderLayout.CENTER);
    gameIsOver = true;

    update();
    logStuff();

    // match3 = null;
    // sb = null;
  }

  public void logStuff(){
    File renamed = new File(logLocation.getAbsolutePath() + "_" + String.valueOf(logCounter) + ".csv");
    logCounter += 1;
    try {
      logger.write(renamed);
    } catch (IOException ex){
      System.out.println("Exception" + ex);
    }
  }

  public void logStuffManually(){
    logLocation = getSaveLocation(".csv");
    try {
      logger.write(logLocation);
    } catch (IOException ex){
      System.out.println("Exception" + ex);
    }
  }

  //=============================== Listeners ==================================

  public void addListener(ExitListener l)
  {
    if (! listeners.contains(l)) {
        listeners.add(l);
    }
  }

  /**
  * removes the specified ToolBarListener
  * @param l: the ToolBarListener to be removed
  */
  public void removeListener(ExitListener l)
  {
    listeners.remove(l);
  }

  //Notifies all interested listeners
  private void notifyListeners()
  {
    for (ExitListener l : listeners) {
      if(exitRequested){
        // match3.removeListener(this);
        // match3 = null;
        // sb = null;
        exitRequested = false;
        l.exit();
      }
    }
  }

}
