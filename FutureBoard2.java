package Match3;

public class FutureBoard{

    int numrows,numcols,numRandomRows;
    int[][] board;


    // create an empty board
    public FutureBoard(int numRows, int numCols, int numRandomRows){
      this.numrows = numrows;
      this.numcols = numcols;
      this.numRandomRows = numRandomRows;
      board = new int[numrows][numcols];
      for(int i=0;i < numrows; i++) {
          for(int j=0;j < numcols; j++) {
              board[i][j] = CELL_EMPTY;
          }
      }
    }

    // construct a copy of an existing board
    public FutureBoard(FutureBoard oldBoard){
      numrows = oldboard.getNumRows();
      numcols = oldboard.getNumCols();
      numRandomRows = oldboard.getNumRandomRows();
      board = new int[numrows][numcols];
      for(int i=0;i < numrows; i++) {
          for(int j=0;j < numcols; j++) {
              board[i][j] = oldboard.board[i][j];
          }
      }
    }

    public int getNumRows() {
        return numrows;
    }

    public int getNumCols() {
        return numcols;
    }

    public int getNumRandomRows(){
        return numRandomRows;
    }

    public void fillRandomRows(){
        Board tempBoard = new Board(numRandomRows,numCols);
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
        }
    }


}
