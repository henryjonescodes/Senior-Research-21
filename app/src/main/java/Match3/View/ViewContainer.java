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
  private Match3Container match3;
  private boolean showingEditor, showingGame;


  public ViewContainer(){
    showingEditor = false;
    showingGame = false;
  }

  public void makeBoardMaker(){
    showingEditor = true;
    frame.remove(menu);
    levelEditor = new BoardMakerContainer();
    levelEditor.addListener(this);

    frame.add(levelEditor);
    frame.setSize(500,700);
  }
  public void makeMatch3(){
    showingGame = true;
    frame.remove(menu);
    match3 = new Match3Container();
    match3.addListener(this);
    frame.add(match3);
    frame.setSize(500,500);
  }

  public void exit(){
    if(showingGame){
      frame.remove(match3);
      showingGame = false;
    } else if (showingEditor){
      frame.remove(levelEditor);
      showingEditor = false;
    }
    frame.add(menu);
    frame.setSize(250,75);
  }

  public void go(){
    frame = new JFrame("Modified Match-3");
    menu = new MainMenu();
    menu.addListener(this);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(menu);
    frame.pack();
    frame.setSize(250,75);

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
