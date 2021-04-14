package Match3;
/*
 * @author Henry Jones
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import static Match3.Settings.*;

class DecisionTree extends JPanel
{
  private SpringLayout layout;
  private JPanel buttonPanel;
  private int numMoves;
  // private JPanel treePanel;

  public DecisionTree(int numMoves){
    // layout = new SpringLayout();
    this.numMoves = numMoves;
    this.setBorder(BorderFactory.createLineBorder(Color.black));
    // treePanel.setLayout(layout);
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

    //Just printing stuff for testing
    for(int i : tempMap.keySet()){
      System.out.print("\n" + i + "-->");
      for(int j = 0; j < tempMap.get(i).size(); j++){
        System.out.print(tempMap.get(i).get(j));
        if(j<tempMap.get(i).size() - 1){
          System.out.print(", ");
        }
      }
    }
    return tempMap;
  }

  public Map<Integer, Vector<JButton>> generateButtons(){
    Map<Integer, Vector<JButton>> buttonMap = new HashMap<Integer, Vector<JButton>>();
    Map<Integer, Vector<String>> tree = generateTree();
    for(int move: tree.keySet()){
      Vector<JButton> buttonVec = new Vector<JButton>();
      for(String label: tree.get(move)){
        JButton btn = new JButton(label);
        btn.setPreferredSize(new Dimension(40, 40));
        buttonVec.add(btn);
        this.add(btn);
      }
      buttonMap.put(move, buttonVec);
    }
    return buttonMap;
  }

  // public void layoutButtons(Map<Integer, Vector<JButton>> buttons) {
  //       for(int move: buttons.keySet()){
  //         Vector<JButton> buttonVec = buttons.get(move);
  //         if(move == 1){
  //           this.add(buttonVec[0]);
  //         } else {
  //           for(JButton jb: buttonVec){
  //             if(jb)
  //           }
  //         }
  //       }
  // }

  /**
  * protects the rest of the paint code from
  * irrevocable changes
  * @param g the Graphics object to project
  */
  public void paintComponent(Graphics g)
  {
      super.paintComponent(g);
  }

}
