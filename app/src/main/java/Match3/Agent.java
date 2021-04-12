package Match3;

import java.util.*;

public class Agent {
  public Agent(){

  }

  public Vector<TilePair> getValidSwaps(Board gameBoard){
    Vector pairs = new Vector<TilePair>();
    for(int i = 0; i < gameBoard.getNumRows(); i++){
      for(int j = 0; j < gameBoard.getNumCols(); j++){
        // System.out.println("Visited: (" + i + ", " + j + ")");
        if(!gameBoard.locationOutOfBounds(i-1, j) && gameBoard.isValidSwap(i,j,i-1,j)){
          TilePair validSwap = new TilePair(i,j,i-1,j);
          pairs.add(validSwap);
        }
        if(!gameBoard.locationOutOfBounds(i+1, j) && gameBoard.isValidSwap(i,j,i+1,j)){
          TilePair validSwap = new TilePair(i,j,i+1,j);
          pairs.add(validSwap);
        }
        if(!gameBoard.locationOutOfBounds(i, j-1) && gameBoard.isValidSwap(i,j,i,j-1)){
          TilePair validSwap = new TilePair(i,j,i,j-1);
          pairs.add(validSwap);
        }
        if(!gameBoard.locationOutOfBounds(i, j+1) && gameBoard.isValidSwap(i,j,i,j+1)){
          TilePair validSwap = new TilePair(i,j,i,j+1);
          pairs.add(validSwap);
        }
      }
    }
    return pairs;
  }

  // public Vector<TilePair> getAllCascades(Board gameBoard){
  //   Vector swaps = getValidSwaps(gameBoard);
  //   for(TilePair tp : swaps){
  //     //Make the swap
  //     //Refil the board
  //     //Check for matches
  //   }
  // }
}
