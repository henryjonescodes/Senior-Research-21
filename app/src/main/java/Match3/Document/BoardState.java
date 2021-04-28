package Match3.Document;

import Match3.View.*;
import Match3.IO.IO_Format;
import Match3.Listeners.*;
import java.util.*;
import java.io.*;
// import static Match3.Settings.*;

// class Board
// underlying game engine for Match3
public class BoardState implements Serializable
{
  //
  // private String BOARD_HEADER = "#B:";
  // private String BOARD_FOOTER = "$B:";

  final boolean verbose = false;
  private Vector<BoardStateListener> listeners;
  private int numrows,numcols;
  private int numRandomRows;
  private int score = 0;
  private int notification = 0;
  private int[][] board;
  private String name;
  private int[] agentA = new int[4];
  private int[] agentB = new int[4];;
  private int[] highLight = new int[4];;

  private Vector<BoardState> cascades;

  public BoardState(int numrows, int numcols, String name)
  {
     // super(numrows, numcols);
     // listeners = new Vector<BoardStateListener>();
     this.numrows = numrows;
     this.numcols = numcols;
     this.name = name;
     // this.score = score;
     // this.notification = notification;
     // cascades = new Vector<BoardState>();
     board = new int[numrows][numcols];
     for(int i=0;i < numrows; i++) {
       for(int j=0;j < numcols; j++) {
           board[i][j] = BoardMaker.CELL_EMPTY;
       }
     }
     go();
  }

  // construct a copy of an existing board
  public BoardState(BoardState oldboard, String name) {
      // super(oldboard);
      // listeners = new Vector<BoardStateListener>();
      numrows = oldboard.getNumRows();
      numcols = oldboard.getNumCols();
      this.name = name;
      // cascades = new Vector<BoardState>();
      board = new int[numrows][numcols];
      for(int i=0;i < numrows; i++) {
          for(int j=0;j < numcols; j++) {
              board[i][j] = oldboard.board[i][j];
          }
      }
      go();
  }

  public BoardState(int[][] contents, String name, int numRows, int numCols){
    this.numrows = numRows;
    this.numcols = numCols;
    this.name = name;
    board = contents;

    // for(int i = 0; i < numRows; i++){
    //    for(int j = 0; j < numCols; j++){
    //      System.out.print(board[i][j]);
    //    }
    //    System.out.print("\n");
    //  }

    // board = new int[numrows][numcols];
    // for(int i=0;i < numrows; i++) {
    //     for(int j=0;j < numcols; j++) {
    //         board[i][j] = contents[i][j];
    //     }
    // }
    go();
  }

  // public void setScore(int value){
  //   score = value;
  // }
  //
  // public void getScore(){
  //   return score;
  // }
  //
  // public void setNotification(int value){
  //   notification = value;
  // }
  //
  // public int getNotification(){
  //   return notification;
  // }

  public void loadCascade(BoardState bs){
    cascades.add(bs);
    notifyListeners();
  }

  private void go(){
    listeners = new Vector<BoardStateListener>();
    cascades = new Vector<BoardState>();
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

  public void setName(String name){
    this.name = name;
  }

  public int getNumRows() {
      return numrows;
  }

  public int getNumCols() {
      return numcols;
  }

  public boolean updateAgentSelection(int agentID, int[] position){
    // printSelection(position);
    if(agentID != 0 && agentID != 1 && agentID != 2){
      System.out.println("Agent ID Invalid");
      return false;
    }
    else if(position.length != 4) {
      System.out.println("Position Invalid");
      return false;
    }
    // System.out.println("Updating Agent: " + agentID);
    if(agentID == 0){
      System.out.println("Setting A");
      agentA = position.clone();
    } else if(agentID == 1){
      System.out.println("Setting B");
      agentB = position.clone();
    } else if(agentID == 2){
      System.out.println("Setting Highlight");
      highLight = position.clone();
    }
    // printSelection(position);
    notifyListeners();
    return true;
  }

  public int isHighlighted(int row, int col){
    if(searchInSelection(row, col, agentA)){
      // System.out.println("returning 0");
      return 0;
    } else if(searchInSelection(row, col, agentB)){
      return 1;
    } else if(searchInSelection(row, col, highLight)){
      return 2;
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
    // System.out.println("trying to make board state: " + name + "~" + String.valueOf(1));
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
    SB.append(IO_Format.BOARD_HEADER + "\n");
    SB.append(name + "\n");
    SB.append(IO_Format.AGENT_A + "\n");
    SB.append(formatArray(agentA, 4)+ "\n");
    SB.append(IO_Format.AGENT_B + "\n");
    SB.append(formatArray(agentB, 4)+ "\n");
    SB.append(IO_Format.HIGHLIGHT + "\n");
    SB.append(formatArray(highLight, 4) + "\n");
    SB.append(IO_Format.CONTENT_HEADER + "\n");

    // for(int i = 0; i < numrows; i++){
    //    for(int j = 0; j < numcols; j++){
    //      System.out.print(board[i][j]);
    //    }
    //    System.out.print("\n");
    //  }

    for(int i=0;i < numrows; i++) {
        for(int j=0;j < numcols; j++) {
          SB.append(board[i][j]);
          // SB.append(BoardMaker.CELL_LABELS[board[i][j]]);
          if(j < numcols - 1){
            SB.append("|");
          }
        }
        if(i< numrows -1){
            SB.append("\n");
        }
    }
    SB.append("\n" + IO_Format.BOARD_FOOTER);

    // SB.append(IO_Format.BOARD_FOOTER);
    for(BoardState bs : cascades){
      SB.append("\n"+bs.toString());
    }
    return SB.toString();
  }

  private String formatArray(int[] arr, int size){
    StringBuilder SB = new StringBuilder();
    for(int i = 0; i < size; i++){
        SB.append(arr[i]);
        if(i < size -1){
          SB.append(",");
        }
      }
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
