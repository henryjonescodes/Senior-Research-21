package Match3;

import java.awt.*;


public final class Settings{
  private Settings(){

  }
  //----------------------------------Match3------------------------------------

  public static final int CELL_EMPTY = 0;   // empty cell
  public static final int CELL_X = 1;       // X
  public static final int CELL_STAR = 2;    // *
  public static final int CELL_O = 3;       // O
  public static final int CELL_DIAMOND = 4; // <>
  public static final int CELL_BOX = 5;     // []
  public static final String[] CELL_LABELS = {" ","X","*","O","<>","[]"};
  public static final int CELL_MIN = CELL_X;
  public static final int CELL_MAX = CELL_BOX;

  public static final int NUM_RANDOM = 3;


  // Debug and tracing
  public static final boolean PRINT_ALL_MATCHED = false;
  public static final boolean PRINT_BOARD_COMPARE = false;
  public static final boolean PRINT_ALL_BOARDS = true;


  //--------------------------------BoardMaker----------------------------------

  public static final int NUM_ROWS = 7;
  public static final int NUM_COLS = 7;
  public static final int NUM_VALUES = 6;
  public static final Dimension BOARD_SIZE = new Dimension(500,500);

  // private final Color PIECE_COLORS[] = {Color.LIGHT_GRAY, Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK};




  public static final Dimension MIN_WINDOW_SIZE = new Dimension(500,500);


  //------------------------------DecisionTree----------------------------------

  // private static final int NUM_MOVES;
  // private static final Dimension BUTTON_SIZE = new Dimension(20, 20);

}
