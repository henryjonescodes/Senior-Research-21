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

  public AgentView(ImageIcon icon, Color c){
    agentButton = new JButton();
    agentText = new JLabel(" ");
    agentButton.setIcon(icon);
    agentButton.setOpaque(true);
    agentButton.setBorderPainted(false);
    agentText.setFont(new Font("Verdana", Font.PLAIN, 20));
    agentText.setForeground(c);

    this.setLayout(new BorderLayout());
    this.add(agentButton, BorderLayout.PAGE_START);
    this.add(agentText, BorderLayout.PAGE_END);
  }

  public void updateText(String text){
    agentText.setText(text);
  }
}
