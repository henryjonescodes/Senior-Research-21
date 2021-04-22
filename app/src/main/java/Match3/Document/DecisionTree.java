package Match3.Document;
/*
 * @author Henry Jones
 */

import Match3.Listeners.*;
import Match3.Document.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
// import static Match3.Settings.*;

public class DecisionTree implements Serializable
{
  private final int NUM_ROWS = 7;
  private final int NUM_COLS = 7;
  private int numMoves;
  private Map<Integer, Vector<String>> stateNames;
  private Map<Integer, Vector<BoardState>> gameStates;
  private BoardState stateToSwap;


  public DecisionTree(int numMoves){
    System.out.println("Constructor");
    this.numMoves = numMoves;
  }

  public void generateBlankTree(){
    stateNames = generateTree();
    gameStates = generateGameStates(stateNames, NUM_ROWS,NUM_COLS);
  }

  public int getNumMoves(){
    return numMoves;
  }

  public BoardState getInitialState(){
    return gameStates.get(1).elementAt(0);
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
    return tempMap;
  }

  public Map<Integer, Vector<String>> getStateNames(){
    return stateNames;
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

  public Map<Integer, Vector<BoardState>> getGameStates(){
    return gameStates;
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
}
