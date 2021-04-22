package Match3.Document;

import Match3.View.*;
import Match3.Listeners.*;
import java.util.*;
import java.io.*;
// import static Match3.Settings.*;

// class Board
// underlying game engine for Match3
public class BoardState implements Serializable
{

  final boolean verbose = false;
  private Vector<BoardStateListener> listeners;
  int numrows,numcols;
  int numRandomRows;
  int[][] board;
  private String name;
  private int[] agentA = new int[4];
  private int[] agentB = new int[4];;


  private Vector<BoardState> cascades;

  public BoardState(int numrows, int numcols, String name)
  {
     // super(numrows, numcols);
     listeners = new Vector<BoardStateListener>();
     this.numrows = numrows;
     this.numcols = numcols;
     this.name = name;
     numRandomRows = 0;
     cascades = new Vector<BoardState>();
     board = new int[numrows][numcols];
     for(int i=0;i < numrows; i++) {
       for(int j=0;j < numcols; j++) {
           board[i][j] = BoardMaker.CELL_EMPTY;
       }
     }
  }

  // construct a copy of an existing board
  public BoardState(BoardState oldboard, String name) {
      // super(oldboard);
      listeners = new Vector<BoardStateListener>();
      numrows = oldboard.getNumRows();
      numcols = oldboard.getNumCols();
      this.name = name;
      cascades = new Vector<BoardState>();
      board = new int[numrows][numcols];
      for(int i=0;i < numrows; i++) {
          for(int j=0;j < numcols; j++) {
              board[i][j] = oldboard.board[i][j];
          }
      }
  }

  public int getValueAt(int row, int col) {
      return board[row][col];
  }

  public void setValueAt(int row, int col, int value) {
    board[row][col] = value;
  }

  public String getName(){
    return name;
  }

  public int getNumRows() {
      return numrows;
  }

  public int getNumCols() {
      return numcols;
  }

  public boolean updateAgentSelection(int agentID, int[] position){
    printSelection(position);
    if(agentID != 0 && agentID != 1){
      System.out.println("Agent ID Invalid");
      return false;
    }
    else if(position.length != 4) {
      System.out.println("Position Invalid");
      return false;
    }
    System.out.println("Updating Agent: " + agentID);
    if(agentID == 0){
      agentA = position;
    } else if(agentID == 1){
      agentB = position;
    }
    printSelection(position);
    notifyListeners();
    return true;
  }

  public int isHighlighted(int row, int col){
    if(searchInSelection(row, col, agentA)){
      System.out.println("returning 0");
      return 0;
    } else if(searchInSelection(row, col, agentB)){
      return 1;
    } else {
      return -1;
    }
  }

  public boolean searchInSelection(int row, int col, int[] selection){
    // printSelection(selection);
    int upperBound, lowerBound;
    if(selection[0] == -1 || selection[1] == -1 || selection[2] == -1 || selection[3] == -1 ){
      // System.out.println("no selection");
      return false;
    }
    else if(selection[0] == selection[2]){
      // System.out.println("Same Row");
      upperBound = Math.max(selection[1], selection[3]);
      lowerBound = Math.min(selection[1], selection[3]);
      for(int i = lowerBound; i <= upperBound; i++){
        if(row == selection[0] && col == i){
          return true;
        }
      }
    }
    else if (selection[1] == selection[3]){
      // System.out.println("Same Col");

      upperBound = Math.max(selection[0], selection[2]);
      lowerBound = Math.min(selection[0], selection[2]);
      for(int i = lowerBound; i <= upperBound; i++){
        if(row == i && col == selection[1]){
          return true;
        }
      }
    }
    return false;
  }

  private void printSelection(int[] toPrint){
    System.out.print("\n[");
    for (int i = 0; i < toPrint.length; i++){
      System.out.print(toPrint[i]);
      if(i < toPrint.length - 1){
        System.out.print(", ");
      }
    }
    System.out.print("]\n");
  }

  public void cycleValues(int row, int col){
    int current = getValueAt(row,col);
    if(current == BoardMaker.NUM_VALUES-1){
      setValueAt(row,col,BoardMaker.CELL_EMPTY);
    } else {
      setValueAt(row,col, current+1);
    }
    notifyListeners();
  }

  // set the entire board to empty cells
  public void resetBoard() {
      for(int i=0;i < numrows; i++) {
          for(int j=0;j < numcols; j++) {
              board[i][j] = BoardMaker.CELL_EMPTY;
          }
      }
  }

  public void addCascade(){
    System.out.println("trying to make board state: " + name + "~" + String.valueOf(1));
    BoardState temp;
    if(numCascades() == 0){
      temp = new BoardState(this, name + "~" + String.valueOf(numCascades()+1));
    } else {
      temp = new BoardState(getCascade(numCascades()-1), name + "~" + String.valueOf(numCascades()+1));
    }
    cascades.add(temp);
  }

  public int numCascades(){
    return cascades.size();
  }

  public BoardState getCascade(int index){
    return cascades.get(index);
  }

  public void removeLastCascade(){
    cascades.remove(cascades.size()-1);
  }

  public String toString(){
    StringBuilder SB = new StringBuilder();
    SB.append("======== "+ name + " =======\n");
    for(int i=0;i < numrows; i++) {
        for(int j=0;j < numcols; j++) {
          SB.append(BoardMaker.CELL_LABELS[board[i][j]]);
          if(j < numcols - 1){
            SB.append(" | ");
          }
        }
        SB.append("\n");
    }
    SB.append("========Board End =========\n");
    return SB.toString();
  }

  public void addListener(BoardStateListener l)
  {
    if (! listeners.contains(l)) {
        listeners.add(l);
    }
  }

  /**
  * removes the specified ToolBarListener
  * @param l: the ToolBarListener to be removed
  */
  public void removeListener(BoardStateListener l)
  {
    listeners.remove(l);
  }

  //Notifies all interested listeners
  private void notifyListeners()
  {
    for (BoardStateListener l : listeners) {
      l.update();
    }
  }

}
