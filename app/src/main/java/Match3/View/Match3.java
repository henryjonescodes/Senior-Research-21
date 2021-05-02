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
import javax.swing.Timer;

public class Match3 extends JPanel implements ActionListener, BoardStateListener{
  public static final int CELL_EMPTY = 0;   // empty cell
  public static final int CELL_X = 1;       // X
  public static final int CELL_STAR = 2;    // *
  public static final int CELL_O = 3;       // O
  public static final int CELL_DIAMOND = 4; // <>
  public static final int CELL_BOX = 5;     // []
  public static final String[] CELL_LABELS = {" ","X","*","O","<>","[]"};
  public static final int CELL_MIN = CELL_X;
  public static final int CELL_MAX = CELL_BOX;
  public static final Color DESELECTED_COLOR = Color.LIGHT_GRAY;
  public static final Color SELECTED_COLOR = Color.RED;
  public static final Color PIECE_COLORS[] = {Color.LIGHT_GRAY, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.PINK};
  public static final int SPEED = 2500;
  public static final int PAUSE = 1000;


  private DecisionTree dt;
  private BoardState displayedBoard, rootBoard;
  private GridLayout layout;
  private JButton boardButtons[][];
  private int numRows, numCols;
  private JPanel boardPanel;
  private Timer timer;

  private int displayedCascade;

  public Match3(DecisionTree dt){
    timer = new Timer(SPEED, cascadeListener);
    timer.setInitialDelay(PAUSE);
    timer.start();

    this.dt = dt;
    numRows = dt.getNumRows();
    numCols = dt.getNumCols();
    rootBoard = dt.getInitialState();
    displayedBoard = rootBoard;
    displayedBoard.addListener(this);
    displayedCascade = 0;
    generateButtons(numRows,numCols);
  }

  public void generateButtons(int rows, int cols){
    boardPanel = new JPanel(layout);
    layout = new GridLayout(rows,cols,3,3);
    boardButtons = new JButton[rows][cols];
    boardPanel.setLayout(layout);
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
    this.add(boardPanel, BorderLayout.CENTER);
    update();
  }

  public void update(){
    for(int i=0; i<numRows; i++) {
        for(int j=0; j<numCols; j++) {
            int value = displayedBoard.getValueAt(i,j);
            boardButtons[i][j].setText(CELL_LABELS[value]);
            boardButtons[i][j].setBackground(PIECE_COLORS[value]);
            int highlightValue = displayedBoard.isHighlighted(i,j);
            if(highlightValue == 0){
              boardButtons[i][j].setForeground(Color.blue);
              // boardButtons[i][j].setBorder(BorderFactory.createBevelBorder(1, Color.blue, Color.blue));
              // boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.blue, 5));
            } else if(highlightValue == 1){
                boardButtons[i][j].setForeground(Color.red);
                // boardButtons[i][j].setBorder(BorderFactory.createBevelBorder(1, Color.red, Color.red));

                // boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.blue, 5));
            } else if(highlightValue == 2){
                boardButtons[i][j].setForeground(Color.white);
                // boardButtons[i][j].setBorder(BorderFactory.createBevelBorder(1, Color.green,Color.green));
                // boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.blue, 5));
            } else {
              boardButtons[i][j].setForeground(Color.black);
              // boardButtons[i][j].setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            }
        }
    }
  }

  // handle button presses
  public void actionPerformed(ActionEvent e) {
      System.out.println("(Match3)" + e.getActionCommand());
      // if(displayedCascade < displayedBoard.numCascades()){
      //   updateState()
      // } else {
      switch(e.getActionCommand()) {
          default:
              String[] cmd = e.getActionCommand().split(" ");
              int cmdrow = Integer.parseInt(cmd[0]);
              int cmdcol = Integer.parseInt(cmd[1]);
              handleClick(cmdrow,cmdcol);
              break;
      }
  }



  public void handleClick(int row,int col) {
    int highlightValue = displayedBoard.isHighlighted(row,col);
    if(highlightValue == 0){
      System.out.println("Agent 0");
      getNextBoard(1);
    } else if (highlightValue == 1){
      System.out.println("Agent 1");
      getNextBoard(0);
    }
  }
  //0 = agent 0
  //1 = agent 1
  public void getNextBoard(int agent){
    if(agent != 0 && agent != 1){
      System.out.println("Agent code invalid");
    } else {
      int moveToSearch = rootBoard.getMoveNum()+1;
      String rootName = rootBoard.getName();
      String nameToSearch = rootName + '.' + String.valueOf(agent);
      System.out.println("Searching for: " + nameToSearch);
      BoardState stateToSwap = dt.searchInMove(moveToSearch,nameToSearch);
      updateState(stateToSwap, 0);
    }
  }

  // public void updateState(BoardState board,int cascade){
  //   System.out.println("trying to display " + displayedBoard.getName() + " Cascade: " + cascade);
  //
  //   rootBoard = board;
  //   System.out.println("NumCascades: " + rootBoard.numCascades());
  //   System.out.println("displayedCascade: " + displayedCascade);
  //
  //   // displayedCascade = cascade;
  //
  //   if(cascade <= displayedBoard.numCascades() && cascade >= 1){
  //     System.out.println("true");
  //     rootBoard = board;
  //     displayedCascade = cascade;
  //     displayedBoard = rootBoard.getCascade(displayedCascade-1);
  //   } else {
  //     System.out.println("false");
  //     displayedBoard = board;
  //   }
  //   System.out.println("trying to display " + displayedBoard.getName());
  //
  //
  //   // if(displayedCascade <= displayedBoard.numCascades() && displayedCascade >= 1){
  //   //   displayedBoard = board.getCascade(displayedCascade-1);
  //   //   System.out.println("trying to display " + displayedBoard.getName());
  //   // } else {
  //   //   displayedBoard = board;
  //   //   System.out.println("trying to display " + displayedBoard.getName());
  //   // }
  //
  //   // nameLabel.setText(displayedBoard.getName());
  //   numRows = displayedBoard.getNumRows();
  //   numCols = displayedBoard.getNumCols();
  //   // numCascades = displayedBoard.numCascades();
  //   displayedBoard.addListener(this);
  //   update();
  // }

  public void updateState(BoardState board, int cascade){
    rootBoard = board;
    displayedCascade = cascade;
    int numCascades = rootBoard.numCascades();

    System.out.println("displayedCascade: " + displayedCascade);

    if(displayedCascade == 0){
      displayedBoard = board;
    } else if (displayedCascade <= numCascades){
      displayedBoard = rootBoard.getCascade(displayedCascade-1);
    }

    System.out.println("trying to display " + displayedBoard.getName());


    // numRows = displayedBoard.getNumRows();
    // numCols = displayedBoard.getNumCols();
    // numCascades = displayedBoard.numCascades();
    displayedBoard.addListener(this);
    update();
  }

  ActionListener cascadeListener = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        if(displayedCascade < rootBoard.numCascades()){
          displayedCascade += 1;
          System.out.println("trying to update " + rootBoard.getName() + " Cascade: " + displayedCascade);
          updateState(rootBoard, displayedCascade);
        }
    }
  };
}
