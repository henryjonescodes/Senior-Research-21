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
// import static Match3.Settings.*;

public class ViewContainer extends JPanel implements ActionListener, TreeSelectionListener{

  //Swing components
  private static JFrame frame;
  private static BoardMaker bm;
  private JScrollPane scroller;
  private DecisionTree dt;
  private DecisionTreeView dtv;
  private BorderLayout layout;
  private JFileChooser fileChooser;
  private GameIO io_manager;




  /**
  * ViewContainer constructor
  */
 public ViewContainer(){
    //Layout the container
    layout = new BorderLayout();
    fileChooser = new JFileChooser();
    this.setLayout(layout);

    //Initialize objects
    bm = new BoardMaker();
    dt = new DecisionTree(5);
    dt.generateBlankTree();
    dtv = new DecisionTreeView(dt);
    bm.updateState(dt.getInitialState(), 0);

    System.out.println(dt.toString());

    io_manager = new GameIO();


    dtv.addListener(this);

    // for(String s: dtv.stateNames()){
    //   System.out.print(s + ", ");
    // }
    // System.out.print("\n");

    scroller = new JScrollPane(dtv,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


    // bm2 = new BoardMaker();

    //Add components to frame and layout
    this.add(bm,BorderLayout.PAGE_START);
    this.add(scroller,BorderLayout.CENTER);
    this.add(makeToolbar(), BorderLayout.PAGE_END);

    this.setPreferredSize(new Dimension(1000,1000));
    // dtv.generateButtons();

    layout.layoutContainer(this);
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

  public void swapBoard(BoardState state){
    this.remove(bm);
    // bm = null;
    // bm = new BoardMaker(state);
    bm.updateState(state, 0);
    this.add(bm,BorderLayout.PAGE_START);
    update();
  }

  /**
  * protects the rest of the paint code from
  * irrevocable changes
  * @param g the Graphics object to project
  */
  public void paintComponent(Graphics g)
  {
      super.paintComponent(g);
  }

  private void loadFromImported(DecisionTree imported){
    this.remove(scroller);
    dt = imported;
    dtv = new DecisionTreeView(dt);
    dtv.addListener(this);
    bm.updateState(dt.getInitialState(), 0);
    scroller = new JScrollPane(dtv,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.add(scroller,BorderLayout.CENTER);
    System.out.println(dt.getGameStates().get(1).elementAt(0).toString());

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

  public JToolBar makeToolbar(){
    JToolBar toolBar = new JToolBar();
    JButton Save, Load;
    Save = new JButton("Save");
    Load = new JButton("Load");

    Save.addActionListener(this);
    Load.addActionListener(this);

    toolBar.addSeparator(new Dimension(30,10));
    toolBar.add(Save);
    toolBar.add(Load);
    toolBar.addSeparator(new Dimension(30,10));

    toolBar.setFloatable(false);

    return toolBar;
  }

  // handle button presses
  public void actionPerformed(ActionEvent e) {
      System.out.println(e.getActionCommand());
      switch(e.getActionCommand()) {
          case "Save":
              File saveLoc = getSaveLocation();
              if(saveLoc.toString() == ""){
                break;
              } else {
                // io_manager.exportState(dt, saveLoc);
                io_manager.writeToFile(dt.toString(), saveLoc);
                break;
              }
          case "Load":
              File loadLoc = getImportLocation();
              if(loadLoc.toString() == ""){
                break;
              } else {
                // DecisionTree importedTree = io_manager.importState(loadLoc);
                // loadFromImported(importedTree);
                // break;
                io_manager.readFromFile(loadLoc);
                break;
              }
          default:
              break;
      }
  }


  /**
  * displays view
  */
  public static void main(String[] args) {

      frame = new JFrame("BoardMaker");
      ViewContainer display = new ViewContainer();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(display);
      frame.pack();

      frame.setSize(500,700);

      frame.setResizable(false);
      frame.setVisible(true);
  }
}
