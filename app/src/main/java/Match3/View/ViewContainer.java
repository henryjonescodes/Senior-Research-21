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

public class ViewContainer extends JPanel implements TreeSelectionListener{

  //Swing components
  private static JFrame frame;
  private static BoardMaker bm;
  private JScrollPane scroller;
  private DecisionTree dt;
  private BorderLayout layout;



  /**
  * ViewContainer constructor
  */
 public ViewContainer(){
    //Layout the container
    layout = new BorderLayout();
    this.setLayout(layout);

    //Initialize toolbar and diagram
    bm = new BoardMaker();
    dt = new DecisionTree(5);
    dt.addListener(this);

    // for(String s: dt.stateNames()){
    //   System.out.print(s + ", ");
    // }
    // System.out.print("\n");

    scroller = new JScrollPane(dt,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


    // bm2 = new BoardMaker();

    //Add components to frame and layout
    this.add(bm,BorderLayout.PAGE_START);
    this.add(scroller,BorderLayout.CENTER);

    this.setPreferredSize(new Dimension(1000,1000));
    // dt.generateButtons();

    layout.layoutContainer(this);
  }

  /**
  * updates the view
  */
  public void update(){
    layout.layoutContainer(this);
    setVisible(true);
    revalidate();
    repaint();
  }

  public void swapBoard(BoardState state){
    this.remove(bm);
    // bm = null;
    bm = new BoardMaker(state);
    this.add(bm,BorderLayout.PAGE_START);
    update();
  }

  /**
  * protects the rest of the paint code from
  * irrevocable changes
  * @param g the Graphics object to project
  */
  public void paintComponent(Graphics g)
  {
      super.paintComponent(g);
  }


  /**
  * displays view
  */
  public static void main(String[] args) {

      frame = new JFrame("BoardMaker");
      ViewContainer display = new ViewContainer();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(display);
      frame.pack();

      frame.setSize(500,700);

      frame.setResizable(true);
      frame.setVisible(true);
  }
}
