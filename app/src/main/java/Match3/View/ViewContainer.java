package Match3.View;

import Match3.Listeners.*;
import Match3.Document.*;
import Match3.IO.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;


public class ViewContainer implements MainMenuListener, ExitListener{

  private JFrame frame;
  private MainMenu menu;
  private BoardMakerContainer levelEditor;

  public ViewContainer(){

  }

  public void makeBoardMaker(){
    frame.remove(menu);
    levelEditor = new BoardMakerContainer();
    levelEditor.getDisplay().addListener(this);

    frame.add(levelEditor);
    frame.setSize(500,700);
  }
  public void makeMatch3(){

  }

  public void exit(){
    frame.remove(levelEditor);
    frame.add(menu);
    frame.setSize(200,75);
  }

  public void go(){
    frame = new JFrame("Modified Match-3");
    menu = new MainMenu();
    menu.addListener(this);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(menu);
    frame.pack();
    frame.setSize(200,75);

    // frame.setSize(500,700);

    frame.setResizable(false);
    frame.setVisible(true);
  }

  /**
  * displays view
  */
  public static void main(String[] args) {
      ViewContainer vc = new ViewContainer();
      vc.go();
  }
}
