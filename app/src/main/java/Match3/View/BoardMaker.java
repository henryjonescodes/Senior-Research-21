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
// import static Match3.Settings.*;

public class BoardMaker extends JPanel implements ActionListener, BoardStateListener
{
  public static final int CELL_EMPTY = 0;   // empty cell
  public static final int CELL_X = 1;       // X
  public static final int CELL_STAR = 2;    // *
  public static final int CELL_O = 3;       // O
  public static final int CELL_DIAMOND = 4; // <>
  public static final int CELL_BOX = 5;     // []
  public static final String[] CELL_LABELS = {" ","X","*","O","<>","[]"};
  public static final int CELL_MIN = CELL_X;
  public static final int CELL_MAX = CELL_BOX;
  public static final Dimension BOARD_SIZE = new Dimension(500,500);
  public static final Color DESELECTED_COLOR = Color.LIGHT_GRAY;
  public static final Color SELECTED_COLOR = Color.YELLOW;
  public static final Color PIECE_COLORS[] = {Color.LIGHT_GRAY, Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK};
  public static final int NUM_VALUES = 6;
  private final int NUM_ROWS = 7;
  private final int NUM_COLS = 7;


  // Debug and tracing
  public static final boolean PRINT_ALL_MATCHED = false;
  public static final boolean PRINT_BOARD_COMPARE = false;
  public static final boolean PRINT_ALL_BOARDS = true;

  private JButton addCascade, removeCascade, next, prev, agent0, agent1, highlight;
  private JToolBar toolBar;

  private boolean agentA_select, agentB_select, selecting;
  private int[] selection = new int[4];

  BoardState displayedBoard, rootBoard;

  JButton boardButtons[][];
  JLabel statusBar;
  JButton resetButton;
  JButton quitButton;
  JPanel boardPanel;
  JLabel nameLabel;
  GridLayout layout;

  int numRows, numCols, numCascades, displayedCascade;


  //For cascades, update name button and state then generate new buttons??

  public BoardMaker(){
    numRows = NUM_ROWS;
    numCols = NUM_COLS;
    numCascades = 0;
    displayedCascade = 0;
    displayedBoard = new BoardState(numRows,numCols, "unnamed");
    displayedBoard.addListener(this);
    go(numRows, numCols);
  }

  public BoardMaker(BoardState board){
    numRows = board.getNumRows();
    numCols = board.getNumCols();
    numCascades = board.numCascades();
    displayedCascade = 0;
    displayedBoard = board;
    displayedBoard.addListener(this);
    go(numRows, numCols);
  }

  public BoardMaker(int numRows, int numCols){
    this.numRows = numRows;
    this.numCols = numCols;
    numCascades = 0;
    displayedCascade = 0;
    displayedBoard = new BoardState(this.numRows,this.numCols,"unnamed");
    displayedBoard.addListener(this);
    go(this.numRows, this.numCols);
  }

  public void updateState(BoardState board,int cascade){
    rootBoard = board;
    displayedCascade = cascade;
    agentA_select = false;
    agentB_select = false;
    selecting = false;

    if(displayedCascade >= 1){
      displayedBoard = board.getCascade(displayedCascade-1);
      System.out.println("trying to display " + displayedBoard.getName());
    } else {
      displayedBoard = board;
    }

    nameLabel.setText(displayedBoard.getName());
    numRows = displayedBoard.getNumRows();
    numCols = displayedBoard.getNumCols();
    numCascades = displayedBoard.numCascades();
    displayedBoard.addListener(this);
    update();
  }


  public void go(int rows, int cols){
    agentA_select = false;
    agentB_select = false;
    selecting = false;
    selection = new int[]{-1,-1,-1,-1};
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

    displayedBoard.updateAgentSelection(0,selection);
    displayedBoard.updateAgentSelection(1,selection);
    this.add(makeToolbar(),BorderLayout.PAGE_START);
    this.add(boardPanel,BorderLayout.CENTER);
    // this.add(nameLabel,BorderLayout.PAGE_END);
    this.add(boardPanel);
    update();
  }

  public JToolBar makeToolbar(){
    nameLabel = new JLabel("Null");
    toolBar = new JToolBar();
    // JButton addCascade, removeCascade, next, prev, agent0, agent1, highlight;
    addCascade = new JButton("Add Cascade");
    removeCascade = new JButton("Remove Cascade");
    next = new JButton("Next");
    prev = new JButton("Prev");
    agent0 = new JButton("Agent 0");
    agent1 = new JButton("Agent 1");
    highlight = new JButton("Highlight");

    addCascade.addActionListener(this);
    removeCascade.addActionListener(this);
    agent0.addActionListener(this);
    agent1.addActionListener(this);
    highlight.addActionListener(this);
    next.addActionListener(this);
    prev.addActionListener(this);

    toolBar.add(addCascade);
    toolBar.add(removeCascade);
    toolBar.addSeparator(new Dimension(10,10));
    toolBar.add(prev);
    toolBar.add(nameLabel);
    toolBar.add(next);
    toolBar.addSeparator(new Dimension(10,10));
    toolBar.add(agent0);
    toolBar.addSeparator(new Dimension(10,10));
    toolBar.add(agent1);
    toolBar.addSeparator(new Dimension(10,10));
    toolBar.add(highlight);

    toolBar.setFloatable(false);

    return toolBar;
  }

  private void addCascade(){
    rootBoard.addCascade();
    displayedCascade += 1;
    System.out.println("trying to update");
    updateState(rootBoard, displayedCascade);
  }

  private void removeCascade(){
    displayedCascade -=1;
    System.out.println("trying to delete");
    updateState(rootBoard, displayedCascade);
    rootBoard.removeLastCascade();
  }

  private void previous(){
    if(displayedCascade > 0){
      displayedCascade = displayedCascade - 1;
      updateState(rootBoard, displayedCascade);
    }
  }

  private void next(){
    if(displayedCascade < rootBoard.numCascades()){
      displayedCascade = displayedCascade + 1;
      updateState(rootBoard, displayedCascade);
    }
  }

  // handle button presses
  public void actionPerformed(ActionEvent e) {
      System.out.println("Processing Command: " + e.getActionCommand());
      switch(e.getActionCommand()) {
          case "Add Cascade":
              addCascade();
              break;
          case "Remove Cascade":
              removeCascade();
              break;
          case "Agent 0":
              if(!agentB_select && !agentA_select){
                agentA_select = true;
                agent0.setBackground(SELECTED_COLOR);
              }
              break;
          case "Agent 1":
              if(!agentA_select && !agentB_select){
                agentB_select = true;
                agent1.setBackground(SELECTED_COLOR);
              }
              break;
          case "Highlight":
              break;
          case "Prev":
              previous();
              break;
          case "Next":
              next();
              break;
          default: // board piece
              String[] cmd = e.getActionCommand().split(" ");
              int cmdrow = Integer.parseInt(cmd[0]);
              int cmdcol = Integer.parseInt(cmd[1]);
              handleClick(cmdrow,cmdcol);
              break;
      }
  }

  public void handleClick(int row,int col) {
    if(!agentA_select && !agentB_select){
      displayedBoard.cycleValues(row, col);
    } else {
      if(!selecting){
        selection[0] = row;
        selection[1] = col;
        selecting = true;
      } else {
        selection[2] = row;
        selection[3] = col;
        if(agentA_select){
          displayedBoard.updateAgentSelection(0, selection);
        } else if (agentB_select){
          displayedBoard.updateAgentSelection(1, selection);
        }
        selecting = false;
        agentA_select = false;
        agentB_select = false;
      }
      // printSelection(selection);
    }
  }

  private void printSelection(int[] toPrint){
    System.out.print("\n[");
    for (int i = 0; i < toPrint.length; i++){
      System.out.print(toPrint[i]);
      if(i < toPrint.length - 1){
        System.out.print(", ");
      }
    }
    System.out.print("]\n");
  }

  public void update(){
    // printSelection(selection);
    for(int i=0; i<numRows; i++) {
        for(int j=0; j<numCols; j++) {
          int value = displayedBoard.getValueAt(i,j);
            boardButtons[i][j].setText(CELL_LABELS[value]);
            boardButtons[i][j].setBackground(PIECE_COLORS[value]);
            if(displayedBoard.isHighlighted(i,j) == 0){
              boardButtons[i][j].setBackground(Color.blue);
              // boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.blue, 5));
              System.out.println("Highlighting " + i + ", " + j);
            }
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
