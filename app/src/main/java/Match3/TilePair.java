package Match3;

public class TilePair {
  int col1, col2, row1, row2;
  public TilePair(int row1, int col1, int row2, int col2){
    this.col1 = col1;
    this.col2 = col2;
    this.row1 = row1;
    this.row2 = row2;
  }

  public String toString(){
    StringBuilder SB = new StringBuilder();
    SB.append("Piece (" + row1 + ", " + col1 + ")\n");
    SB.append("Match (" + row2 + ", " + col2 + ")\n");

    return SB.toString();
  }
}
