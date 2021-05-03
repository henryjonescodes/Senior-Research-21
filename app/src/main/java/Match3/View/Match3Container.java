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
  private JButton loadBtn;
  private BorderLayout layout;
  private Vector<ExitListener> listeners;
  private boolean exitRequested = false;
  private GameIO io_manager;
  private Logger logger;


  public Match3Container(){
    io_manager = new GameIO();
    listeners = new Vector<ExitListener>();
    fileChooser = new JFileChooser();
    layout = new BorderLayout();
    loadBtn = new JButton("Load");
    loadBtn.addActionListener(this);
    try {
        BufferedImage myPicture = ImageIO.read(new File("src/main/java/Match3/Icons/icon1.png"));
        Image scaled = myPicture.getScaledInstance(100,100,Image.SCALE_SMOOTH);
        ImageIcon testIcon = new ImageIcon(scaled);
        loadBtn.setIcon(testIcon);
    } catch (Exception ex) {
       System.out.println(ex);
    }
    this.setLayout(layout);

    this.add(loadBtn, BorderLayout.PAGE_START);
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
    this.remove(loadBtn);
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
    JButton exit;
    exit = new JButton("Exit");

    exit.addActionListener(this);

    toolBar.add(exit);
    toolBar.setFloatable(false);

    return toolBar;
  }

  // handle button presses
  public void actionPerformed(ActionEvent e) {
      System.out.println("(Match3)" + e.getActionCommand());
      switch(e.getActionCommand()) {
          case "Exit":
              exitRequested = true;
              notifyListeners();
              break;
          case "Load":
              File loadLoc = getImportLocation();
              if(loadLoc.toString() == ""){
                break;
              } else {
                DecisionTree importedTree = io_manager.readFromFile(loadLoc);
                loadFromImported(importedTree);
                break;
              }
          default:
              System.out.println("(Match3)Invalid Command");
      }
  }

  public void gameOver(int score, Logger log){
    logger = log;
    JLabel message = new JLabel("GAME OVER");
    String scoreString = "Points: " + String.valueOf(score);
    JLabel scoreMessage = new JLabel(scoreString);
    JPanel gameOverPanel = new JPanel();
    // gameOverPanel.setLayout(new BorderLayout());
    gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
    message.setAlignmentX(Component.CENTER_ALIGNMENT);
    scoreMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

    message.setFont(new Font("Verdana", Font.PLAIN, 40));
    scoreMessage.setFont(new Font("Verdana", Font.PLAIN, 40));

    gameOverPanel.add(message);
    gameOverPanel.add(scoreMessage);

    // gameOverPanel.add(message, BorderLayout.PAGE_START);
    // gameOverPanel.add(scoreMessage, BorderLayout.PAGE_END);
    match3.removeListener(this);
    this.remove(match3);
    this.remove(sb);
    this.add(gameOverPanel, BorderLayout.CENTER);

    update();
    logStuff();

    // match3 = null;
    // sb = null;
  }

  public void logStuff(){
    File logLocation = getSaveLocation(".csv");
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
        match3.removeListener(this);
        match3 = null;
        sb = null;
        exitRequested = false;
        l.exit();
      }
    }
  }

}
