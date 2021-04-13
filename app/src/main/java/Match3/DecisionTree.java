package Match3;
/*
 * @author Henry Jones
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import static Match3.Settings.*;

class DecisionTree
{
  private SpringLayout layout;
  private int numMoves;
  // private JPanel treePanel;

  public DecisionTree(int numMoves){
    // layout = new SpringLayout();
    this.numMoves = numMoves;
    // treePanel.setLayout(layout);


  }

  public Map<Integer, Vector<Integer>> generateTree(){
    Map<Integer, Vector<Integer>> tempMap = new HashMap<>();
    int stateNum = 0;
    for(int move = 1; move <= numMoves; move++){
      Vector<Integer> tempVec = new Vector<>();
      for(int state = stateNum; state < Math.pow(2,  move); state++){
        tempVec.add(state);
        stateNum ++;
      }
      tempMap.put(move, tempVec);
    }

    for(int i : tempMap.keySet()){
      System.out.print("\n" + i + "-->");
      for(int j = 0; j < tempMap.get(i).size(); j++){
        System.out.print(tempMap.get(i).get(j));
      }
    }

    return tempMap;
  }
}
