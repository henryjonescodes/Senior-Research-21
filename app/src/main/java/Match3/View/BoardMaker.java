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
  public static final Color SELECTED_COLOR = Color.RED;
  public static final Color PIECE_COLORS[] = {Color.LIGHT_GRAY, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.PINK};
  public static final int NUM_VALUES = 6;
  private final int NUM_ROWS = 7;
  private final int NUM_COLS = 7;


  // Debug and tracing
  public static final boolean PRINT_ALL_MATCHED = false;
  public static final boolean PRINT_BOARD_COMPARE = false;
  public static final boolean PRINT_ALL_BOARDS = true;

  private JButton addCascade, removeCascade, next, prev, agent0, agent1, highlight;
  private JButton scoreUp, scoreDown, notiUp, notiDown;
  private JToolBar toolBar, scoreBar;

  private boolean agentA_select, agentB_select, highlight_select, selecting;
  private int[] selection = new int[4];

  BoardState displayedBoard, rootBoard;

  JButton boardButtons[][];
  JLabel statusBar;
  JButton resetButton;
  JButton quitButton;
  JPanel boardPanel;
  JLabel nameLabel, scoreLabel, notificationLabel;
  GridLayout layout;

  int numRows, numCols, numCascades, displayedCascade;


  //For cascades, update name button and state then generate new buttons??

  public BoardMaker(){
    numRows = NUM_ROWS;
    numCols = NUM_COLS;
    numCascades = 0;
    displayedCascade = 0;
    displayedBoard = new BoardState(numRows,numCols, "unnamed",0,0);
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
    displayedBoard = new BoardState(this.numRows,this.numCols,"unnamed",0,0);
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
      // System.out.println("trying to display " + displayedBoard.getName());
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

  public BoardState getCurrentBoard(){
    return displayedBoard;
  }


  public void go(int rows, int cols){
    scoreLabel = new JLabel("Null");
    notificationLabel = new JLabel("Null");
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

    displayedBoard.updateAgentSelection(0, new int[]{-1,-1,-1,-1});
    displayedBoard.updateAgentSelection(1, new int[]{-1,-1,-1,-1});
    displayedBoard.updateAgentSelection(2, new int[]{-1,-1,-1,-1});
    this.add(makeToolbar(),BorderLayout.PAGE_START);
    this.add(boardPanel,BorderLayout.CENTER);
    this.add(makeScoreBar(),BorderLayout.PAGE_END);
    // this.add(nameLabel,BorderLayout.PAGE_END);
    this.add(boardPanel);
    update();
  }

  public JToolBar makeScoreBar(){
    scoreBar = new JToolBar();
    scoreUp = new JButton("Score +");
    scoreDown = new JButton("Score -");
    notiUp = new JButton("Noti +");
    notiDown = new JButton("Noti -");

    scoreUp.addActionListener(this);
    scoreDown.addActionListener(this);
    notiUp.addActionListener(this);
    notiDown.addActionListener(this);

    scoreBar.addSeparator(new Dimension(10,10));
    scoreBar.add(scoreUp);
    scoreBar.add(scoreLabel);
    scoreBar.add(scoreDown);
    scoreBar.addSeparator(new Dimension(10,10));
    scoreBar.add(notiUp);
    scoreBar.add(notificationLabel);
    scoreBar.add(notiDown);
    scoreBar.addSeparator(new Dimension(10,10));

    scoreBar.setFloatable(false);

    return scoreBar;
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
    // System.out.println("trying to update");
    updateState(rootBoard, displayedCascade);
  }

  private void removeCascade(){
    displayedCascade -=1;
    // System.out.println("trying to delete");
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
              if(!agentB_select && !agentA_select && !highlight_select){
                agentA_select = true;
                agent0.setForeground(SELECTED_COLOR);
              }
              else if (agentA_select){
                selecting = false;
                agentA_select = false;
                displayedBoard.updateAgentSelection(0, new int[]{-1,-1,-1,-1});
                agent0.setForeground(DESELECTED_COLOR);
              }
              break;
          case "Agent 1":
              if(!agentA_select && !agentB_select && !highlight_select){
                agentB_select = true;
                agent1.setForeground(SELECTED_COLOR);
              }
              else if (agentB_select){
                selecting = false;
                agentB_select = false;
                displayedBoard.updateAgentSelection(1, new int[]{-1,-1,-1,-1});
                agent1.setForeground(DESELECTED_COLOR);
              }
              break;
          case "Highlight":
              if(!agentA_select && !agentB_select && !highlight_select){
                highlight_select = true;
                highlight.setForeground(SELECTED_COLOR);
              }
              else if (highlight_select){
                selecting = false;
                highlight_select = false;
                displayedBoard.updateAgentSelection(2, new int[]{-1,-1,-1,-1});
                highlight.setForeground(DESELECTED_COLOR);
              }
              break;
          case "Prev":
              previous();
              break;
          case "Next":
              next();
              break;
          case "Noti +":
              displayedBoard.setNotification(displayedBoard.getNotification() + 1);
              break;
          case "Noti -":
              displayedBoard.setNotification(displayedBoard.getNotification() - 1);
              break;
          case "Score +":
              displayedBoard.setScore(displayedBoard.getScore() + 1);
              break;
          case "Score -":
              displayedBoard.setScore(Math.max((displayedBoard.getScore() - 1), 0));
              break;
          default: // board piece
              String[] cmd = e.getActionCommand().split(" ");
              int cmdrow = Integer.parseInt(cmd[0]);
              int cmdcol = Integer.parseInt(cmd[1]);
              handleClick(cmdrow,cmdcol);
              break;
      }
      update();
  }

  public void handleClick(int row,int col) {
    if(!agentA_select && !agentB_select && !highlight_select){
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
        } else if (highlight_select){
          displayedBoard.updateAgentSelection(2, selection);
        }
        selecting = false;
        agentA_select = false;
        agentB_select = false;
        highlight_select = false;
        agent0.setForeground(DESELECTED_COLOR);
        agent1.setForeground(DESELECTED_COLOR);
        highlight.setForeground(DESELECTED_COLOR);

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
    scoreLabel.setText(String.valueOf(displayedBoard.getScore()));
    notificationLabel.setText(String.valueOf(displayedBoard.getNotification()));

    // printSelection(selection);
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
