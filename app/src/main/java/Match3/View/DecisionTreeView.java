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
// import static Match3.Settings.*;

public class DecisionTreeView extends JPanel implements ActionListener
{

  private static final int TREE_SPACING_Y = 40;
  private static final int TREE_SPACING_X = 1;
  private static final int BUTTON_WIDTH = 70;
  private static final int BUTTON_HEIGHT = 40;
  private static final Dimension BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

  private final Color DESELECTED_COLOR = Color.BLACK;
  private final Color SELECTED_COLOR = Color.RED;


  private SpringLayout layout;
  private JPanel buttonPanel;
  private JButton copyBtn;

  private boolean copyFlag = false;
  private boolean swapFlag = false;

  private DecisionTree dt;
  private int numMoves;
  private Map<Integer, Vector<String>> stateNames;
  private Map<Integer, Vector<JButton>> stateButtons;
  private Map<Integer, Vector<BoardState>> gameStates;

  private Vector<TreeSelectionListener> listeners;

  private BoardState stateToSwap;

  public DecisionTreeView(DecisionTree tree){
    listeners = new Vector<TreeSelectionListener>();

    layout = new SpringLayout();
    buttonPanel = new JPanel();
    buttonPanel.setLayout(layout);

    numMoves = tree.getNumMoves();
    this.setBorder(BorderFactory.createLineBorder(Color.black));

    // dt = new DecisionTree(numMoves);

    dt = tree;

    stateNames = dt.getStateNames();
    gameStates = dt.getGameStates();
    stateButtons = generateButtons(stateNames);

    layoutButtons(stateButtons, buttonPanel);

    this.setLayout(layout);
    this.setPreferredSize(new Dimension((TREE_SPACING_X + (int)BUTTON_SIZE.getWidth())
          *(stateButtons.get(numMoves).size() + 1), TREE_SPACING_Y*(numMoves + 1)));
  }

  public void update(){
    setVisible(true);
    revalidate();
    repaint();
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

  public void layoutButtons(Map<Integer, Vector<JButton>> buttons, JPanel panel) {
        for(int move: buttons.keySet()){
          Vector<JButton> buttonVec = buttons.get(move);
          if(move == 1){
              this.add(buttonVec.elementAt(0));
              layout.putConstraint(SpringLayout.NORTH, buttonVec.elementAt(0),
                     TREE_SPACING_Y,
                     SpringLayout.NORTH, this);
              layout.putConstraint(SpringLayout.WEST, buttonVec.elementAt(0),
                    (TREE_SPACING_X + BUTTON_WIDTH * (stateNames.get(numMoves).size()/2 - stateNames.get(move).size()/2)),
                    SpringLayout.WEST, this);
              copyBtn = new JButton("Copy");
              // copyBtn.setActionCommand("Copy");
              copyBtn.addActionListener(this);
              copyBtn.setPreferredSize(BUTTON_SIZE);
              this.add(copyBtn);
              layout.putConstraint(SpringLayout.NORTH, copyBtn,
                     TREE_SPACING_Y,
                     SpringLayout.NORTH, this);
              layout.putConstraint(SpringLayout.WEST, copyBtn,
                    TREE_SPACING_X,
                    SpringLayout.EAST, buttonVec.elementAt(0));
              layout.putConstraint(SpringLayout.EAST, copyBtn,
                    TREE_SPACING_X,
                    SpringLayout.EAST, this);
          } else {
            for(int i = 0; i < buttonVec.size(); i++){
              JButton thisBtn = buttonVec.elementAt(i);
              this.add(thisBtn);
              if(i == 0){
                //Attatch this one to the WEST side of the panel
                layout.putConstraint(SpringLayout.WEST, thisBtn,
                       (TREE_SPACING_X + BUTTON_WIDTH * (stateNames.get(numMoves).size()/2 - stateNames.get(move).size()/2)),
                       SpringLayout.WEST, this);
              } else if(i != buttonVec.size() - 1) {
                //Attatch this one to its neighbors
                layout.putConstraint(SpringLayout.WEST, thisBtn,
                       TREE_SPACING_X,
                       SpringLayout.EAST, buttonVec.elementAt(i-1));
              } else {
                //Attatch this one to the EAST side of the panel
                // layout.putConstraint(SpringLayout.WEST, thisBtn,
                //         (TREE_SPACING_X + BUTTON_WIDTH * (stateNames.get(numMoves).size()/2 - stateNames.get(move).size()/2)),
                //        SpringLayout.EAST, buttonVec.elementAt(i-1));
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
      System.out.println("(DecisisonTreeView)" + e.getActionCommand());
      switch(e.getActionCommand()) {
          case "Copy":
              copyFlag = !copyFlag;
              if(copyFlag){
                copyBtn.setForeground(SELECTED_COLOR);
              } else {
                copyBtn.setForeground(DESELECTED_COLOR);
              }
              // update();
              break;
          default:
              String[] cmd = e.getActionCommand().split(" ");
              int key = Integer.parseInt(cmd[0]);
              int index = Integer.parseInt(cmd[1]);
              stateToSwap = dt.getGameStates().get(key).elementAt(index);
              swapFlag = true;
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
      if(copyFlag){
        l.copyBoard(stateToSwap);
        copyBtn.setForeground(DESELECTED_COLOR);
        copyFlag = false;
      } else if(swapFlag){
        l.swapBoard(stateToSwap);
        swapFlag = false;
      }
    }
  }

}
