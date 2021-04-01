package Match3;

import java.util.*;
import static Match3.Settings.*;

// class Board
// underlying game engine for Match3
class Board
{
    final boolean verbose = false;
    final boolean printBoardCompare = true;
    final boolean printAllBoards = true;
    int numrows,numcols;
    int[][] board;
    FutureBoard future;

    // create a board
    Board(int numrows, int numcols)
    {
        this.numrows = numrows;
        this.numcols = numcols;
        future = new FutureBoard(numrows,numcols, NUM_RANDOM);
        future.dropPieces();
        if(printBoardCompare) {System.out.println("Future \n" + future);}
        board = new int[numrows][numcols];

        for(int i=0;i < numrows; i++) {
            for(int j=0;j < numcols; j++) {
                board[i][j] = future.getValueAt(i,j);
            }
        }
        if(printBoardCompare) {System.out.println("Actual \n" + this);}
        future.refillBoard();
        if(printBoardCompare) {System.out.println("New Future \n" + future);}
    }

    // construct a copy of an existing board
    Board(Board oldboard) {
        numrows = oldboard.getNumRows();
        numcols = oldboard.getNumCols();
        future = oldboard.getFutureBoard();
        board = new int[numrows][numcols];
        for(int i=0;i < numrows; i++) {
            for(int j=0;j < numcols; j++) {
                board[i][j] = oldboard.board[i][j];
            }
        }
    }


    public FutureBoard getFutureBoard(){
      return future;
    }

    public int getNumRows() {
        return numrows;
    }

    public int getNumCols() {
        return numcols;
    }

    // set the entire board to empty cells
    public void resetBoard() {
        future = new FutureBoard(numrows, numcols, NUM_RANDOM);
        future.dropPieces();
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

    public int getValueAt(int row, int col) {
        return board[row][col];
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
                thiscol[target_index] = future.getNextPiece(j);
                future.dropPieces();
                future.fillRandomRows();
                // if(printAllBoards) {System.out.println("Future \n" + future);}
                // thiscol[target_index] = new Random().nextInt(CELL_MAX)+1;
                target_index--;
            }

            // Transfer data to the board's data structure
            for(int i=0;i<numrows;i++) {
                board[i][j] = thiscol[i];
            }
        }
        if(printAllBoards) {System.out.println("Future \n" + future);}
    }

    // check if there exists an empty cell
    public boolean existsEmptyCell() {
        boolean empty_cell_found = false;

        for(int i=0;i < numrows; i++) {
            for(int j=0;j < numcols; j++) {
                if(board[i][j] == CELL_EMPTY) {
                    empty_cell_found = true;
                }
            }
        }

        return empty_cell_found;
    }

    // eliminate any 3 consecutive matching pieces
    public void eliminateMatches() {
        for(int i=0;i<numrows;i++) {
            for(int j=0;j<numcols;j++) {
                // 3 in a row
                if(0<j && j<numcols-1) {
                    if(board[i][j-1] == board[i][j] &&
                                board[i][j+1] == board[i][j]) {
                        if(verbose){
                          System.out.printf("Matched (%d, %d) with (%d, %d) and (%d, %d)\n", i,j-1,i,j,i,j+1);
                          System.out.println("Values " + CELL_LABELS[board[i][j-1]]
                              + ", " + CELL_LABELS[board[i][j]] + ", " + CELL_LABELS[board[i][j+1]]);
                        }
                        board[i][j] = CELL_EMPTY;
                        board[i][j-1] = CELL_EMPTY;
                        board[i][j+1] = CELL_EMPTY;
                    }
                }

                // 3 in a col
                if(0<i && i<numrows-1) {
                    if(board[i-1][j] == board[i][j] &&
                                board[i+1][j] == board[i][j]) {
                        if(verbose){
                          System.out.printf("Matched (%d, %d) with (%d, %d) and (%d, %d)\n", i-1,j,i,j,i+1,j);
                          System.out.println("Values " + CELL_LABELS[board[i-1][j]]
                              + ", " + CELL_LABELS[board[i][j]] + ", " + CELL_LABELS[board[i+1][j]]);
                        }
                        board[i][j] = CELL_EMPTY;
                        board[i-1][j] = CELL_EMPTY;
                        board[i+1][j] = CELL_EMPTY;
                    }
                }
            }
        }
    }

    public boolean isValidSwap(int row1,int col1,int row2,int col2) {
        //Are they in the same row AND next to each other
        if(!(row1 == row2 && Math.abs(col1-col2)==1) && !(col1 == col2 && Math.abs(row1-row2)==1)){
            return false;
        } else {

            //Make a copy of this board and do some tests
            Board testBoard = new Board(this);

            //Is there already a match on the board?
            testBoard.eliminateMatches();
            if(testBoard.existsEmptyCell()) {
                return false;
            }

            //This is a valid match, swap the pair and handle matches
            int thisTile = testBoard.board[row1][col1];
            testBoard.board[row1][col1] = testBoard.board[row2][col2];
            testBoard.board[row2][col2] = thisTile;
            testBoard.eliminateMatches();
            if(testBoard.existsEmptyCell()) {
                return true;
            } else {
                return false;
            }
        }
    }




    public boolean locationOutOfBounds(int row, int col){
      if(row < 0 || row >= getNumRows()){
        return true;
      } else if (col < 0 || col >= getNumCols()) {
        return true;
      }
      return false;
    }


    // swap the given pieces, then call eliminateMatches() and then dropPieces()
    // until no matches exist
    public void makeSwap(int row1,int col1,int row2,int col2) {
        int tmp = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = tmp;

        do {
            dropPieces();
            eliminateMatches();
        } while(existsEmptyCell());
    }

    public String toString(){
      StringBuilder SB = new StringBuilder();
      SB.append("========Board Start =========\n");
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
