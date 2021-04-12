package Match3;

import java.util.*;
import static Match3.Settings.*;

// class Board
// underlying game engine for Match3
class BoardState
{

  final boolean verbose = false;
  private Vector<BoardStateListener> listeners;
  int numrows,numcols;
  int numRandomRows;
  int[][] board;


  BoardState(int numrows, int numcols)
  {
     // super(numrows, numcols);
     listeners = new Vector<BoardStateListener>();
     this.numrows = numrows;
     this.numcols = numcols;
     numRandomRows = 0;
     board = new int[numrows][numcols];
     for(int i=0;i < numrows; i++) {
       for(int j=0;j < numcols; j++) {
           board[i][j] = CELL_EMPTY;
       }
     }
  }

  // construct a copy of an existing board
  BoardState(BoardState oldboard) {
      // super(oldboard);
      listeners = new Vector<BoardStateListener>();
      numrows = oldboard.getNumRows();
      numcols = oldboard.getNumCols();
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
