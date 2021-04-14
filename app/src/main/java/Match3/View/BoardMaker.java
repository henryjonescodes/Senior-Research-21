package Match3.View;
/*
 * @author Henry Jones
 */
import Match3.Listeners.*;
import Match3.Document.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import static Match3.Settings.*;

public class BoardMaker extends JPanel implements ActionListener, BoardStateListener
{
  BoardState displayedBoard;

  JButton boardButtons[][];
  JLabel statusBar;
  JButton resetButton;
  JButton quitButton;
  JPanel buttonBar;
  JPanel boardPanel;
  GridLayout layout;

  int numRows, numCols;

  final Color DESELECTED_COLOR = Color.LIGHT_GRAY;
  final Color SELECTED_COLOR = Color.YELLOW;

  private final Color PIECE_COLORS[] = {Color.LIGHT_GRAY, Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK};


  public BoardMaker(){
    numRows = NUM_ROWS;
    numCols = NUM_COLS;
    displayedBoard = new BoardState(numRows,numCols);
    displayedBoard.addListener(this);
    go(numRows, numCols);
  }

  public BoardMaker(BoardState board){
    numRows = board.getNumRows();
    numCols = board.getNumCols();
    displayedBoard = board;
    displayedBoard.addListener(this);
    go(numRows, numCols);
  }

  public BoardMaker(int numRows, int numCols){
    this.numRows = numRows;
    this.numCols = numCols;
    displayedBoard = new BoardState(this.numRows,this.numCols);
    displayedBoard.addListener(this);
    go(this.numRows, this.numCols);
  }



  public void go(int rows, int cols){
    layout = new GridLayout(rows,cols,3,3);
    boardButtons = new JButton[rows][cols];
    boardPanel = new JPanel(layout);
    this.setLayout(new BorderLayout());

    for(int i=0;i<rows;i++) {
        for(int j=0;j<cols;j++) {
            boardButtons[i][j] = new JButton(CELL_LABELS[CELL_EMPTY]);
            boardButtons[i][j].setFont(new Font("Monospaced",Font.PLAIN,20));
            boardButtons[i][j].setActionCommand(
                String.valueOf(i) + " " + String.valueOf(j));
            boardButtons[i][j].addActionListener(this);

            boardPanel.add(boardButtons[i][j]);
            boardButtons[i][j].setBackground(DESELECTED_COLOR);
            boardButtons[i][j].setOpaque(true);
            boardButtons[i][j].setBorderPainted(false);
        }
    }
    this.add(boardPanel,BorderLayout.CENTER);
    this.add(boardPanel);
    update();
  }

  // handle button presses
  public void actionPerformed(ActionEvent e) {
      // setStatus("Points: " + String.valueOf(points));
      switch(e.getActionCommand()) {
          // case "Exit":
          //     System.exit(0);
          // case "Reset Board":
          //     reset();
          //     resetAllBackgrounds();
          //     break;
          // case "All Matches":
          //     findValidSwaps();
          //     break;
          default: // board piece
              String[] cmd = e.getActionCommand().split(" ");
              int cmdrow = Integer.parseInt(cmd[0]);
              int cmdcol = Integer.parseInt(cmd[1]);
              handleClick(cmdrow,cmdcol);
              break;
      }
  }

  public void handleClick(int row,int col) {
    displayedBoard.cycleValues(row, col);
  }

  public void update(){
    for(int i=0; i<NUM_ROWS; i++) {
        for(int j=0; j<NUM_COLS; j++) {
          int value = displayedBoard.getValueAt(i,j);
            boardButtons[i][j].setText(CELL_LABELS[value]);
            boardButtons[i][j].setBackground(PIECE_COLORS[value]);
        }
    }
  }


  /**
  * gets the size of the layout
  * @return the Dimensions of layout
  */
  @Override
  public Dimension getPreferredSize() {
        // return layout.preferredLayoutSize(this);
        return BOARD_SIZE;
  }

  /**
  * protects the rest of the paint code from
  * irrevocable changes
  * @param g the Graphics object to project
  */
  @Override
  public void paintComponent(Graphics g) {
        super.paintComponent(g);
  }

}
