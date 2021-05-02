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

public class Match3Container extends JPanel implements ActionListener{

  private JFileChooser fileChooser;
  private Match3 match3;
  private JButton loadBtn;
  private BorderLayout layout;
  private Vector<ExitListener> listeners;
  private boolean exitRequested = false;
  private GameIO io_manager;


  public Match3Container(){
    io_manager = new GameIO();
    listeners = new Vector<ExitListener>();
    fileChooser = new JFileChooser();
    layout = new BorderLayout();
    loadBtn = new JButton("Load");
    loadBtn.addActionListener(this);
    this.setLayout(layout);

    this.add(loadBtn, BorderLayout.PAGE_START);
    this.add(makeToolbar(), BorderLayout.PAGE_END);

    // this.setPreferredSize(new Dimension(1000,1000));

    layout.layoutContainer(this);
  }

  public File getSaveLocation(){
    fileChooser.addChoosableFileFilter(new IO_Filter());
    fileChooser.setDialogTitle("Specify a filename and path");
    int userSelection = fileChooser.showSaveDialog(this);
    File fileToSave = new File("");

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      fileToSave = fileChooser.getSelectedFile();
    }

    File renamedFile = new File(fileToSave.getAbsolutePath() + ".txt");

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
        exitRequested = false;
        l.exit();
      }
    }
  }

}
