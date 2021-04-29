package Match3.Listeners;

import Match3.Document.*;

public interface TreeSelectionListener {

  /**
  * updates view
  */
  public void swapBoard(BoardState state);

  public void copyBoard(BoardState copy);
}
