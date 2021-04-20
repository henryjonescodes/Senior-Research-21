package Match3.View;
/*
 * @author Henry Jones
 */

import Match3.Listeners.*;
import Match3.Document.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import static Match3.Settings.*;

public class DecisionTree extends JPanel implements ActionListener
{

  private static final int TREE_SPACING_Y = 40;
  private static final int TREE_SPACING_X = 15;
  private static final Dimension BUTTON_SIZE = new Dimension(70, 40);


  private SpringLayout layout;
  private JPanel buttonPanel;
  private int numMoves;
  private Map<Integer, Vector<String>> stateNames;
  private Map<Integer, Vector<JButton>> stateButtons;
  private Map<Integer, Vector<BoardState>> gameStates;

  private Vector<TreeSelectionListener> listeners;

  private BoardState stateToSwap;


  // private JPanel treePanel;

  public DecisionTree(int numMoves){
    listeners = new Vector<TreeSelectionListener>();

    layout = new SpringLayout();
    buttonPanel = new JPanel();
    buttonPanel.setLayout(layout);

    this.numMoves = numMoves;
    this.setBorder(BorderFactory.createLineBorder(Color.black));

    stateNames = generateTree();
    stateButtons = generateButtons(stateNames);
    gameStates = generateGameStates(stateNames, NUM_ROWS,NUM_COLS);
    layoutButtons(stateButtons, buttonPanel);
    // stateToSwap = gameStates.get(1).elementAt(0);


    this.setLayout(layout);
    // this.setPreferredSize(new Dimension(500,10));
    this.setPreferredSize(new Dimension((TREE_SPACING_X + (int)BUTTON_SIZE.getWidth())
          *(stateButtons.get(numMoves).size() + 1), TREE_SPACING_Y*(numMoves + 1)));
    // this.setPreferredSize(new Dimension(1000, TREE_SPACING_Y*(numMoves + 1)));



    // this.setPreferredSize(new Dimension(500,400));
    // this.add(buttonPanel);
    // notifyListeners();

  }

  public Map<Integer, Vector<String>> generateTree(){
    Map<Integer, Vector<String>> tempMap = new HashMap<Integer, Vector<String>>();
    Vector<String> firstMove = new Vector<String>();
    firstMove.add("1");
    tempMap.put(1, firstMove);
    for(int move = 2; move <= numMoves; move++){
      Vector<String> tempVec = new Vector<String>();
      Vector<String> previousMoves = tempMap.get(move-1);

      for(int i = 0; i < tempMap.get(move-1).size(); i++){
        String previous = tempMap.get(move-1).get(i);
        String moveA = String.valueOf(previous) + ".1";
        String moveB = String.valueOf(previous) + ".0";

        tempVec.add(moveA);
        tempVec.add(moveB);
      }
      tempMap.put(move, tempVec);
    }

    // //Just printing stuff for testing
    // for(int i : tempMap.keySet()){
    //   System.out.print("\n" + i + "-->");
    //   for(int j = 0; j < tempMap.get(i).size(); j++){
    //     System.out.print(tempMap.get(i).get(j));
    //     if(j<tempMap.get(i).size() - 1){
    //       System.out.print(", ");
    //     }
    //   }
    // }
    return tempMap;
  }

  public Map<Integer, Vector<JButton>> generateButtons(Map<Integer, Vector<String>> tree){
    Map<Integer, Vector<JButton>> buttonMap = new HashMap<Integer, Vector<JButton>>();
    for(int move: tree.keySet()){
      Vector<JButton> buttonVec = new Vector<JButton>();
      for(String label: tree.get(move)){
        JButton btn = new JButton(label);
        btn.setActionCommand(String.valueOf(move) + " " + String.valueOf(tree.get(move).indexOf(label)));
        btn.addActionListener(this);
        btn.setPreferredSize(BUTTON_SIZE);
        buttonVec.add(btn);
        // this.add(btn);
      }
      buttonMap.put(move, buttonVec);
    }
    return buttonMap;
  }

  public Map<Integer, Vector<BoardState>> generateGameStates(Map<Integer, Vector<String>> tree, int rows, int cols){
    Map<Integer, Vector<BoardState>> states = new HashMap<Integer, Vector<BoardState>>();
    for(int move: tree.keySet()){
      Vector<BoardState> stateVec = new Vector<BoardState>();
      for(String label: tree.get(move)){
        BoardState state = new BoardState(rows,cols,label);
        stateVec.add(state);
      }
      states.put(move, stateVec);
    }
    return states;
  }

  public void layoutButtons(Map<Integer, Vector<JButton>> buttons, JPanel panel) {
        for(int move: buttons.keySet()){
          Vector<JButton> buttonVec = buttons.get(move);
          if(move == 1){
              this.add(buttonVec.elementAt(0));
              layout.putConstraint(SpringLayout.NORTH, buttonVec.elementAt(0),
                     TREE_SPACING_Y,
                     SpringLayout.NORTH, this);
              layout.putConstraint(SpringLayout.WEST, buttonVec.elementAt(0),
                    TREE_SPACING_X,
                    SpringLayout.WEST, this);
          } else {
            for(int i = 0; i < buttonVec.size(); i++){
              JButton thisBtn = buttonVec.elementAt(i);
              this.add(thisBtn);
              if(i == 0){
                //Attatch this one to the WEST side of the panel
                layout.putConstraint(SpringLayout.WEST, thisBtn,
                       TREE_SPACING_X,
                       SpringLayout.WEST, this);
              } else if(i != buttonVec.size() - 1) {
                //Attatch this one to its neighbors
                layout.putConstraint(SpringLayout.WEST, thisBtn,
                       TREE_SPACING_X,
                       SpringLayout.EAST, buttonVec.elementAt(i-1));
              } else {
                //Attatch this one to the EAST side of the panel
                layout.putConstraint(SpringLayout.WEST, thisBtn,
                       TREE_SPACING_X,
                       SpringLayout.EAST, buttonVec.elementAt(i-1));
                // layout.putConstraint(SpringLayout.EAST, thisBtn,
                //        TREE_SPACING_X,
                //        SpringLayout.EAST, this);
              }
              //Get the last row of moves and constrain to each (NORTH/SOUTH)
              Vector<JButton> prevButtonVec = buttons.get(move-1);
              for(int j = 0; j < prevButtonVec.size(); j++){
                layout.putConstraint(SpringLayout.NORTH, thisBtn,
                       TREE_SPACING_Y,
                       SpringLayout.NORTH, prevButtonVec.elementAt(j));
              }
            }
          }
        }
  }

  // handle button presses
  public void actionPerformed(ActionEvent e) {
      switch(e.getActionCommand()) {
          default:
              System.out.println(e.getActionCommand());
              String[] cmd = e.getActionCommand().split(" ");
              int key = Integer.parseInt(cmd[0]);
              int index = Integer.parseInt(cmd[1]);
              stateToSwap = gameStates.get(key).elementAt(index);
              notifyListeners();
              break;
      }
  }

  public int numStates(){
    int total = 0;
    for(int move: stateNames.keySet()){
      total += stateNames.get(move).size();
    }
    return total;
  }

  public String[] stateNames(){
    String[] names = new String[numStates()];
    int index = 0;
    for(int move: stateNames.keySet()){
      for(int state = 0; state < stateNames.get(move).size(); state++){
        names[index] = stateNames.get(move).elementAt(state);
        index += 1;
      }
    }
    return names;
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

  public void addListener(TreeSelectionListener l)
  {
    if (! listeners.contains(l)) {
        listeners.add(l);
    }
  }

  public void removeListener(TreeSelectionListener l)
  {
    listeners.remove(l);
  }

  //Notifies all interested listeners
  private void notifyListeners()
  {
    for (TreeSelectionListener l : listeners) {
      l.swapBoard(stateToSwap);
    }
  }

}
