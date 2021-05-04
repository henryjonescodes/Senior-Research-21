package Match3.Document;
/*
 * @author Henry Jones
 */

import Match3.Listeners.*;
import Match3.Document.*;
import Match3.IO.IO_Format;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;



// import static Match3.Settings.*;

public class DecisionTree implements Serializable
{

  // private final String BOARD_HEADER = "#B:";
  // private final String BOARD_FOOTER = "$B:";
  // private final String BOARD_SEPPARATOR = "\n~\n";
  // private final String TREE_HEADER = "#T:";
  // private final String TREE_FOOTER = "$T:";
  private final int NUM_ROWS = 7;
  private final int NUM_COLS = 7;

  private int numMoves;
  private Map<Integer, Vector<String>> stateNames;
  private Map<Integer, Vector<BoardState>> gameStates;
  private BoardState stateToSwap;


  public DecisionTree(int numMoves){
    // System.out.println("Constructor");
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

  public void copyBoard(BoardState copy, String paste){
    for(int move : gameStates.keySet()){
      for(int index = 0; index < gameStates.get(move).size(); index ++){
        if(gameStates.get(move).elementAt(index).getName() == paste){
          gameStates.get(move).set(index, new BoardState(copy,paste));
        }
      }
    }
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
        BoardState state = new BoardState(rows,cols,label,0,0);
        stateVec.add(state);
      }
      states.put(move, stateVec);
    }
    return states;
  }

  public void importFromMap(Map<Integer, Vector<BoardState>> states, Map<Integer, Vector<String>> names){
    gameStates = states;
    stateNames = names;
    // for(int move: names.keySet()){
    //   System.out.println("Move: " + move);
    //   for(int i = 0; i < names.get(move).size(); i++){
    //     System.out.println(names.get(move).elementAt(i));
    //   }
    // }

    // generateBlankTree();
  }

  public int getNumBoardsAtMove(int move){
    return gameStates.get(move).size();
  }

  public Map<Integer, Vector<BoardState>> getGameStates(){
    return gameStates;
  }

  public void importGameStates(Map<Integer, Vector<BoardState>> states){
    gameStates = states;
  }

  public int numStates(){
    int total = 0;
    for(int move: stateNames.keySet()){
      total += stateNames.get(move).size();
    }
    return total;
  }

  public int getNumRows(){
    return NUM_ROWS;
  }

  public int getNumCols(){
    return NUM_COLS;
  }

  public BoardState searchInMove(int move, String name){
    BoardState temp = new BoardState(7,7,name,0,0);
    for(BoardState bs : gameStates.get(move)){
      if(bs.getName().equals(name)){
        temp = bs;
      }
    }
    return temp;
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

  public String toString(){
    StringBuilder SB = new StringBuilder();
    //Output in format:
    //#T:
    //rows,cols,moves
    SB.append(IO_Format.TREE_HEADER + "\n" + NUM_ROWS + "," + NUM_COLS + "," + numMoves);
    // SB.append("\n" + IO_Format.MOVE_SEPPARATOR);
    for(int move: gameStates.keySet()){
      for(int index = 0; index < gameStates.get(move).size(); index++){
        SB.append("\n" + gameStates.get(move).elementAt(index).toString());
        // SB.append("\n" + IO_Format.BOARD_SEPPARATOR);
      }
      SB.append("\n" + IO_Format.MOVE_SEPPARATOR);
    }
    SB.append("\n" + IO_Format.TREE_FOOTER);
    return SB.toString();
  }
}
