package Match3;
/*
 * @author Henry Jones
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import static Match3.Settings.*;

public class ViewContainer extends JPanel {

  //Swing components
  private static JFrame frame;
  private static BoardMaker bm, bm2;
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
    dt.generateTree();
    // bm2 = new BoardMaker();

    //Add components to frame and layout
    this.add(bm,BorderLayout.NORTH);
    // this.add(bm2,BorderLayout.SOUTH);

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

  /**
  * protects the rest of the paint code from
  * irrevocable changes
  * @param g the Graphics object to project
  */
  public void paintComponent(Graphics g)
  {
      super.paintComponent(g);
  }

  // /**
  // * gets the preferred size of the layout
  // * @return the Dimension
  // */
  // @Override
  // public Dimension getPreferredSize() {
  //       int minWidth = Math.max((int)MIN_WINDOW_SIZE.getWidth(), (int)layout.preferredLayoutSize(this).getWidth());
  //       int minHeight = Math.max((int)MIN_WINDOW_SIZE.getHeight(), (int)layout.preferredLayoutSize(this).getHeight());
  //
  //       return new Dimension(minWidth, minHeight);
  //       // return layout.preferredLayoutSize(this);
  //       // return MIN_WINDOW_SIZE;
  // }

  /**
  * displays view
  */
  public static void main(String[] args) {

      frame = new JFrame("BoardMaker");
      ViewContainer display = new ViewContainer();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(display);
      frame.pack();

      frame.setSize(500,1000);

      frame.setResizable(false);
      frame.setVisible(true);
  }
}
