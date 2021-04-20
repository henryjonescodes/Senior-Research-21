package Match3.Document;

import Match3.View.*;
import Match3.Listeners.*;
import java.util.*;
import static Match3.Settings.*;

// class Board
// underlying game engine for Match3
public class BoardState
{

  final boolean verbose = false;
  private Vector<BoardStateListener> listeners;
  int numrows,numcols;
  int numRandomRows;
  int[][] board;
  private String name;

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
           board[i][j] = CELL_EMPTY;
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

  public void cycleValues(int row, int col){
    int current = getValueAt(row,col);
    if(current == NUM_VALUES-1){
      setValueAt(row,col,CELL_EMPTY);
    } else {
      setValueAt(row,col, current+1);
    }
    notifyListeners();
  }

  // set the entire board to empty cells
  public void resetBoard() {
      for(int i=0;i < numrows; i++) {
          for(int j=0;j < numcols; j++) {
              board[i][j] = CELL_EMPTY;
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
    SB.append("========Board Start =======\n");
    for(int i=0;i < numrows; i++) {
        for(int j=0;j < numcols; j++) {
          SB.append(CELL_LABELS[board[i][j]]);
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
