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

public class AgentView extends JPanel{

  private JButton agentButton;
  private JLabel agentText;

  public AgentView(){
    agentButton = new JButton("Agent");
    agentText = new JLabel(" ");
    this.setLayout(new BorderLayout());
    this.add(agentButton, BorderLayout.PAGE_START);
    this.add(agentText, BorderLayout.PAGE_END);
  }

  public void updateText(String text){
    agentText.setText(text);
  }
}
