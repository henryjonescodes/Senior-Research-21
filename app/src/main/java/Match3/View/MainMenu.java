package Match3.View;

import Match3.Listeners.*;
import Match3.Document.*;
import Match3.IO.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class MainMenu extends JPanel implements ActionListener{

  private JButton levelEditor, match3;
  private boolean makeMatch3, makeBoardMaker;
  private Vector<MainMenuListener> listeners;


  public MainMenu(){
    levelEditor = new JButton("Level Editor");
    match3 = new JButton("Match 3");
    listeners = new Vector<MainMenuListener>();
    makeMatch3 = false;
    makeBoardMaker = false;

    levelEditor.addActionListener(this);
    match3.addActionListener(this);

    this.setLayout(new FlowLayout());
    this.add(levelEditor);
    this.add(match3);
  }

  // handle button presses
  public void actionPerformed(ActionEvent e) {
      System.out.println("(Menu)" + e.getActionCommand());
      switch(e.getActionCommand()) {
          case "Level Editor":
              makeBoardMaker = true;
              notifyListeners();
              break;
          case "Match 3":
              makeMatch3 = true;
              notifyListeners();
              break;
          default:
              System.out.println("(Menu)Invalid Command");
      }
  }

  public void addListener(MainMenuListener l)
  {
    if (! listeners.contains(l)) {
        listeners.add(l);
    }
  }

  /**
  * removes the specified ToolBarListener
  * @param l: the ToolBarListener to be removed
  */
  public void removeListener(MainMenuListener l)
  {
    listeners.remove(l);
  }

  //Notifies all interested listeners
  private void notifyListeners()
  {
    for (MainMenuListener l : listeners) {
      if(makeBoardMaker){
        makeBoardMaker = false;
        l.makeBoardMaker();
      } else if (makeMatch3){
        makeMatch3 = false;
        l.makeMatch3();
      }
    }
  }
}
