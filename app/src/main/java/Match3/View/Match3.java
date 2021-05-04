package Match3.View;
/*
 * @author Henry Jones
 */
import Match3.Listeners.*;
import Match3.Document.*;
import Match3.IO.*;
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
  public static final Dimension BOARD_SIZE = new Dimension(500,500);
  public static final Color SELECTED_COLOR = Color.RED;
  public static final Color PIECE_COLORS[] = {Color.LIGHT_GRAY, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.PINK};
  public static final int SPEED = 3500;
  public static final int PAUSE = 2000;

  // public static final int SPEED = 100;
  // public static final int PAUSE = 100;

  // public static final int BUTTON_LAYER = 0;
  // public static final int HIGHLIGHT_LAYER = 1;
  private Logger logger;

  private ImageIcon[] icons;
  private DecisionTree dt;
  private BoardState displayedBoard, rootBoard;
  private GridLayout layout;
  private JButton boardButtons[][];
  private int numRows, numCols;
  // private JPanel boardPanel;
  private JPanel linesPanel;
  private JPanel boardPanel;
  private Timer timer;
  private boolean gameEnded;

  private int displayedCascade, agentChoice;
  private Vector<GameListener> listeners;
  private Vector<GameOverListener> goListeners;

  private int loggedScore, loggedMove,loggedChoice, loggedNotification;

  public Match3(DecisionTree dt){
    logger = new Logger();
    loggedScore = 0;
    loggedMove = 0;
    loggedChoice = 0;
    gameEnded = false;
    loggedNotification = 0;
    listeners = new Vector<GameListener>();
    goListeners = new Vector<GameOverListener>();
    IconLoader IL = new IconLoader();
    icons = IL.getIcons();

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
    agentChoice = 0;
    generateButtons(numRows,numCols);
    repaint();
  }

  public void generateButtons(int rows, int cols){
    // boardPanel = new JPanel(layout);
    boardPanel = new JPanel();
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
            boardButtons[i][j].setMargin(new Insets(0, 0, 0, 0));
            boardButtons[i][j].setHorizontalAlignment(SwingConstants.CENTER);
            boardPanel.add(boardButtons[i][j]);
            // boardButtons[i][j].setBackground(DESELECTED_COLOR);
            boardButtons[i][j].setOpaque(true);
            boardButtons[i][j].setBorderPainted(false);
        }
    }

    // linesPanel = new JPanel();
    // linesPanel.setBounds(0,0, boardPanel.getBounds().width, boardPanel.getBounds().height);
    // boardPanel.add(linesPanel, HIGHLIGHT_LAYER);


    // System.out.println(boardButtons[1][1].getBounds());
    // System.out.println(boardButtons[1][1].getPreferredSize());

    this.add(boardPanel, BorderLayout.CENTER);
    update();
  }

  public void update(){
    for(int i=0; i<numRows; i++) {
        for(int j=0; j<numCols; j++) {
            int value = displayedBoard.getValueAt(i,j);
            // boardButtons[i][j].setText(CELL_LABELS[value]);
            // boardButtons[i][j].setBackground(PIECE_COLORS[value]);
            int highlightValue = displayedBoard.isHighlighted(i,j);
            if(highlightValue == 0){
              // boardButtons[i][j].setBackground(Color.blue);
              boardButtons[i][j].setBorderPainted(true);
              boardButtons[i][j].setBorder(BorderFactory.createBevelBorder(1, Color.blue, Color.blue));
              // boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.blue, 5));
            } else if(highlightValue == 1){
                // boardButtons[i][j].setBackground(Color.red);
                boardButtons[i][j].setBorderPainted(true);
                boardButtons[i][j].setBorder(BorderFactory.createBevelBorder(1, Color.red, Color.red));

                // boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.blue, 5));
            } else if(highlightValue == 2){
                boardButtons[i][j].setBorderPainted(false);
                boardButtons[i][j].setBackground(Color.green);
                // boardButtons[i][j].setBorderPainted(true);
                // boardButtons[i][j].setBorder(BorderFactory.createBevelBorder(1, Color.green,Color.green));
                // boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.blue, 5));
            } else {
              boardButtons[i][j].setBackground(null);
              boardButtons[i][j].setBorderPainted(false);
            }

            if(value != 0){
              boardButtons[i][j].setIcon(icons[value-1]);
            } else {
              boardButtons[i][j].setIcon(null);
            }
        }
    }
    // handleHighlights(displayedBoard.getAgentSelection(0));
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

  // public void gameOver(){
  //   JLabel message = new JLabel("GAME OVER");
  //   String scoreString = "Points: " + String.valueOf(displayedBoard.getScore());
  //   JLabel scoreMessage = new JLabel(scoreString);
  //   JPanel gameOverPanel = new JPanel();
  //   gameOverPanel.setLayout(new BorderLayout());
  //   gameOverPanel.add(message, BorderLayout.PAGE_START);
  //   gameOverPanel.add(scoreMessage, BorderLayout.PAGE_END);
  //   this.remove(boardPanel);
  //   this.remove(sb);
  //   this.add(gameOverPanel, BorderLayout.CENTER);
  // }



  public void handleClick(int row,int col) {
    int highlightValue = displayedBoard.isHighlighted(row,col);
    if(highlightValue == 0){
      // System.out.println("Agent 0");
      agentChoice = 1;
      timer.restart();
      getNextBoard(1);
    } else if (highlightValue == 1){
      // System.out.println("Agent 1");
      agentChoice = 0;
      timer.restart();
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
      // System.out.println("Searching for: " + nameToSearch);
      BoardState stateToSwap = dt.searchInMove(moveToSearch,nameToSearch);
      updateState(stateToSwap, 0);
    }
  }

  public void updateState(BoardState board, int cascade){
    rootBoard = board;
    displayedCascade = cascade;
    int numCascades = rootBoard.numCascades();

    // System.out.println("displayedCascade: " + displayedCascade);

    if(displayedCascade == 0){
      displayedBoard = board;
    } else if (displayedCascade <= numCascades){
      displayedBoard = rootBoard.getCascade(displayedCascade-1);
    }

    // System.out.println("trying to display " + displayedBoard.getName());


    // numRows = displayedBoard.getNumRows();
    // numCols = displayedBoard.getNumCols();
    // numCascades = displayedBoard.numCascades();
    displayedBoard.addListener(this);
    update();
    notifyListeners();
  }

  public void addListener(GameListener l)
  {
    if (! listeners.contains(l)) {
        listeners.add(l);
    }
  }


  public void removeListener(GameListener l)
  {
    listeners.remove(l);
  }

  //Notifies all interested listeners
  private void notifyListeners()
  {
    for (GameListener l : listeners) {
      l.setScore(displayedBoard.getScore());
      l.setNotification(displayedBoard.getNotification());
      l.setChoice(agentChoice);
      if(!gameEnded){
          loggedChoice = agentChoice;
          loggedScore = displayedBoard.getScore();
          loggedNotification = displayedBoard.getNotification();
          logger.logMove(rootBoard.getMoveNum(), displayedBoard.getName(), loggedChoice, loggedScore, loggedNotification);
        }
    }
  }

  public void addListener(GameOverListener l)
  {
    if (! goListeners.contains(l)) {
        goListeners.add(l);
    }
  }


  public void removeListener(GameOverListener l)
  {
    goListeners.remove(l);
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
        // drawDashedLine(linesPanel.getGraphics(), 10,10,100,100,Color.RED);
  }

  // public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2, Color c){
  //
  //   // Create a copy of the Graphics instance
  //   Graphics2D g2d = (Graphics2D) g.create();
  //
  //   // Set the stroke of the copy, not the original
  //   Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
  //                                   0, new float[]{9}, 0);
  //   g2d.setStroke(dashed);
  //   g2d.setColor(c);
  //
  //   // Draw to the copy
  //   g2d.drawLine(x1, y1, x2, y2);
  //
  //   // Get rid of the copy
  //   g2d.dispose();
  // }

  ActionListener cascadeListener = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        if(displayedCascade < rootBoard.numCascades()){
          displayedCascade += 1;
          // System.out.println("trying to update " + rootBoard.getName() + " Cascade: " + displayedCascade);
          updateState(rootBoard, displayedCascade);
        }
        else if (rootBoard.getMoveNum() >= dt.getNumMoves()){
          for (GameOverListener l : goListeners) {
            gameEnded = true;
            System.out.println("Game Over");
            timer = null;
            l.gameOver(displayedBoard.getScore(),logger);
          }
        }
    }
  };

}
