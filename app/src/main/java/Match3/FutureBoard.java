package Match3;

import java.util.*;
import static Match3.Settings.*;

// class Board
// underlying game engine for Match3
class FutureBoard
{
    final boolean verbose = false;
    int numrows,numcols;
    int numRandomRows;
    int[][] board;

    // create an empty board
    FutureBoard(int numrows, int numcols)
    {
        // super(numrows, numcols);
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

    // create an empty board
    FutureBoard(int numrows, int numcols, int numRandomRows)
    {
        // super(numrows, numcols);
        this.numRandomRows = numRandomRows;
        this.numrows = numrows;
        this.numcols = numcols;
        board = new int[numrows][numcols];
        for(int i=0;i < numrows; i++) {
            for(int j=0;j < numcols; j++) {
                board[i][j] = CELL_EMPTY;
            }
        }
    }

    // construct a copy of an existing board
    FutureBoard(FutureBoard oldboard) {
        // super(oldboard);
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

    public int getNumRandomRows(){
        return numRandomRows;
    }

    public int getNumRows() {
        return numrows;
    }

    public int getNumCols() {
        return numcols;
    }

    public int getNextPiece(int fromColumn){
      int toReturn = board[numrows-1][fromColumn];
      board[numrows-1][fromColumn]  = CELL_EMPTY;
      dropPieces();
      // fillRandomRows();
      return toReturn;
    }

    // set the entire board to empty cells
    public void resetBoard() {
        for(int i=0;i < numrows; i++) {
            for(int j=0;j < numcols; j++) {
                board[i][j] = CELL_EMPTY;
            }
        }
    }

    public void refillBoard() {
      resetBoard();
      dropPieces();
    }

    public void fillRandomRows(){
        FutureBoard tempBoard = new FutureBoard(numRandomRows,numcols);
        tempBoard.dropPieces();
        for(int i=0;i < numRandomRows; i++) {
            for(int j=0;j < numcols; j++) {
                board[i][j] = tempBoard.board[i][j];
            }
        }
    }

    // let all pieces fall to the bottom under gravity, then let in
    // new ones from the top drawn randomly
    public void dropPieces() {
        // work column by column
        for(int j=0; j<numcols; j++) {

            // Make temporary storage for moved pieces
            // and fill it with empty values
            int[] thiscol = new int[numrows];
            for(int i=0;i<numrows;i++) {
                thiscol[i] = CELL_EMPTY;
            }

            // Starting at the top of each collumn, store eveything
            // but the empty cells in new order
            int target_index = numrows - 1;
            for(int i=numrows-1; i>=0; i--) {
                if(board[i][j] != CELL_EMPTY) {
                    thiscol[target_index] = board[i][j];
                    target_index--;
                }
            }

            // If target_index hasn't fully traversed the collumn,
            // add new randomized pieces at the top
            while(target_index >= 0) {
                thiscol[target_index] = new Random().nextInt(CELL_MAX)+1;
                target_index--;
            }

            // Transfer data to the board's data structure
            for(int i=0;i<numrows;i++) {
                board[i][j] = thiscol[i];
            }

            // Fill top with random piececs
        }
        // fillRandomRows();
    }

    public boolean locationOutOfBounds(int row, int col){
      if(row < 0 || row >= getNumRows()){
        return true;
      } else if (col < 0 || col >= getNumCols()) {
        return true;
      }
      return false;
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
}
