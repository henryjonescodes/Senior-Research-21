package Match3;
/*
 * @author Henry Jones and ryanden2018
 *
 * A match-3 game using java and swing based extensivly on
 * code by ryanden2018 on github:
 * https://github.com/ryanden2018/Match3
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/////////////////////////////////////////
// class Match3 implements ActionListener
// constructs the GUI and handles user input for the Match3 game
class Match3 implements ActionListener
{
    Board gameboard;
    final int numrows = 10;
    final int numcols = 6;
    boolean pieceSelected = false;
    int selectedRow = -1;
    int selectedCol = -1;
    JButton boardButtons[][];
    JLabel statusBar;
    JButton resetButton;
    JButton quitButton;
    JPanel buttonBar;
    JPanel boardPanel;
    JFrame jfrm;
    final Color DESELECTED_COLOR = Color.LIGHT_GRAY;
    final Color SELECTED_COLOR = Color.YELLOW;
    int points = 0;

    // ---------------------------New Stuff---------------------------------- //

    private final Color debugColors[] = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK};
    private JButton showMatchesButton;
    private JButton showAllCascadesButton;

    // ---------------------------------------------------------------------- //


    // construct the game board
    Match3() {
        gameboard = new Board(numrows,numcols);


        boardButtons = new JButton[numrows][numcols];
        buttonBar = new JPanel(new FlowLayout());
        resetButton = new JButton("Reset Board");
        quitButton = new JButton("Exit");
        boardPanel = new JPanel(new GridLayout(numrows,numcols,3,3));
        statusBar = new JLabel("Points: " + String.valueOf(points));
        jfrm = new JFrame("Match3");

        showMatchesButton = new JButton("All Matches");
        showAllCascadesButton = new JButton("All Cascades");

        for(int i=0;i<numrows;i++) {
            for(int j=0;j<numcols;j++) {
                boardButtons[i][j] = new JButton(Board.CELL_LABELS[Board.CELL_EMPTY]);
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

        resetButton.addActionListener(this);
        quitButton.addActionListener(this);
        showMatchesButton.addActionListener(this);
        buttonBar.add(resetButton);
        buttonBar.add(quitButton);
        buttonBar.add(showMatchesButton);
        buttonBar.add(showAllCascadesButton);

        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.add(statusBar,BorderLayout.SOUTH);
        jfrm.add(buttonBar,BorderLayout.NORTH);
        jfrm.add(boardPanel,BorderLayout.CENTER);
        jfrm.setSize(500,500);
        jfrm.setResizable(false);
        jfrm.setVisible(true);

        reset();
    }

    public void findValidSwaps(){
      Agent bond = new Agent();
      Vector<TilePair> validSwaps = bond.getValidSwaps(gameboard);
      // System.out.println(validSwaps);
      int colorCounter = 0;
      for(TilePair tp : validSwaps){
        boardButtons[tp.row1][tp.col1].setBackground(debugColors[colorCounter]);
        boardButtons[tp.row2][tp.col2].setBackground(debugColors[colorCounter]);
        if(colorCounter >= debugColors.length-1){
          colorCounter = 0;
        } else {
          colorCounter +=1;
        }
      }
    }

    public void resetAllBackgrounds(){
      for(int i = 0; i <numrows; i++){
        for(int j = 0; j <numcols; j++){
            boardButtons[i][j].setBackground(DESELECTED_COLOR);
        }
      }
    }

    // reset the board to a random state
    public void reset() {
        gameboard.resetBoard();
        points = 0;
        setStatus("Points: " + String.valueOf(points));
        while(gameboard.existsEmptyCell()) {
            gameboard.dropPieces();
            gameboard.eliminateMatches();
        }
        redrawBoard();
    }

    // change the status bar text
    public void setStatus(String str) {
        statusBar.setText(str);
    }

    // handle button presses
    public void actionPerformed(ActionEvent e) {
        setStatus("Points: " + String.valueOf(points));
        switch(e.getActionCommand()) {
            case "Exit":
                System.exit(0);
            case "Reset Board":
                reset();
                resetAllBackgrounds();
                break;
            case "All Matches":
                findValidSwaps();
                break;
            default: // board piece
                String[] cmd = e.getActionCommand().split(" ");
                int cmdrow = Integer.parseInt(cmd[0]);
                int cmdcol = Integer.parseInt(cmd[1]);
                handleClick(cmdrow,cmdcol);
                break;
        }
    }

    // handle a click event at (row,col)
    public void handleClick(int row,int col) {
        if(!pieceSelected) {
            pieceSelected = true;
            selectedRow = row;
            selectedCol = col;
            boardButtons[row][col].setBackground(SELECTED_COLOR);
            return;
        }

        if(pieceSelected && selectedRow==row && selectedCol==col) {
            pieceSelected = false;
            selectedRow = -1;
            selectedCol = -1;
            boardButtons[row][col].setBackground(DESELECTED_COLOR);
            return;
        }

        // if we get here, then a piece is selected and it is not the
        // newly clicked piece
        if(!(gameboard.isValidSwap(row,col,selectedRow,selectedCol))) {
            setStatus("Invalid move");
            return;
        }
        points++;
        gameboard.makeSwap(row,col,selectedRow,selectedCol);
        boardButtons[selectedRow][selectedCol].setBackground(DESELECTED_COLOR);
        pieceSelected = false;
        selectedRow = -1;
        selectedCol = -1;
        redrawBoard();
        setStatus("Points: " + String.valueOf(points));
    }

    // re-draw the board from underlying game data
    public void redrawBoard() {
        for(int i=0; i<numrows; i++) {
            for(int j=0; j<numcols; j++) {
                boardButtons[i][j].setText(
                   Board.CELL_LABELS[ gameboard.getValueAt(i,j) ]);
            }
        }
    }

    // main
    public static void main(String args[])
    {
        new Match3();
    }
}