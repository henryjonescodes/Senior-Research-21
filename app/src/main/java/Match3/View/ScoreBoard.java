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

public class ScoreBoard extends JPanel implements GameListener{

  private final String MISSED_CASCADE_NOTIFICATION = "You missed a cascade!";


  private JLabel score;
  private AgentView agent0, agent1;
  private int scoreValue, prevScore, notiValue, thisChoice;

  private JButton agent1Button, agent0Button;
  private JLabel agentText;
  private ImageIcon[] icons;
  // private JButton agentText;

  public ScoreBoard(){
    IconLoader IL = new IconLoader();
    JPanel agentPanel = new JPanel();
    JPanel textPanel = new JPanel();
    textPanel.setLayout(new FlowLayout());
    agentPanel.setLayout(new FlowLayout());
    icons = IL.getAgentImgs();
    score = new JLabel("0");
    score.setFont(new Font("Verdana", Font.PLAIN, 30));
    agent0Button = new JButton();
    agent1Button = new JButton();
    agent0Button.setIcon(icons[0]);
    agent1Button.setIcon(icons[1]);
    agent0Button.setOpaque(true);
    agent1Button.setOpaque(true);
    agent0Button.setBorderPainted(false);
    agent1Button.setBorderPainted(false);
    agentText = new JLabel(" ");
    // agentText = new JButton(" ");
    agentText.setFont(new Font("Verdana", Font.PLAIN, 20));
    // agent0 = new AgentView(icons[0], Color.red);
    // agent1 = new AgentView(icons[1], Color.blue);
    // this.setLayout(new BorderLayout());
    // this.add(agent0, BorderLayout.LINE_START);
    // this.add(score, BorderLayout.CENTER);
    // this.add(agent1, BorderLayout.LINE_END);

    this.setLayout(new BorderLayout());
    agentPanel.add(agent0Button);
    agentPanel.add(score);
    agentPanel.add(agent1Button);
    textPanel.add(agentText);
    this.add(agentPanel,BorderLayout.PAGE_START);
    this.add(textPanel, BorderLayout.CENTER);

    // this.add(agent0);
    // this.add(agent1);
  }

  public void setScore(int value){
    prevScore = scoreValue;
    scoreValue = value;
    update();
  }

  public void setChoice(int value){
    thisChoice = value;
  }

  public void setNotification(int value){
    notiValue = value;
    update();
  }

  public void update(){
    score.setText(String.valueOf(scoreValue));
    agentText.setText(" ");
    agentText.setOpaque(false);
    agentText.setBackground(Color.white);
    agent0Button.setIcon(icons[0]);
    agent1Button.setIcon(icons[1]);
    // setAgentText(0, " ");
    // setAgentText(1, " ");
    if(notiValue == -1){
      agent1Button.setIcon(icons[3]);
      agentText.setOpaque(true);
      agentText.setForeground(Color.blue);
      agentText.setBackground(new Color(153, 187, 255));
      agentText.setText(MISSED_CASCADE_NOTIFICATION);
      // setAgentText(1, MISSED_CASCADE_NOTIFICATION);
    } else if (notiValue == 1){
      agent0Button.setIcon(icons[2]);
      agentText.setOpaque(true);
      agentText.setForeground(Color.red);
      agentText.setBackground(new Color(255, 161, 181));
      agentText.setText(MISSED_CASCADE_NOTIFICATION);
      // setAgentText(0, MISSED_CASCADE_NOTIFICATION);
    } else if (prevScore != scoreValue){
      String temp = "Good Job! That's " + String.valueOf(scoreValue - prevScore) + " points!";
      if(thisChoice == 0){
        agent0Button.setIcon(icons[2]);
        agentText.setOpaque(true);
        agentText.setForeground(Color.red);
        agentText.setBackground(new Color(255, 161, 181));
        agentText.setText(temp);
      } else if (thisChoice == 1){
        agent1Button.setIcon(icons[3]);
        agentText.setOpaque(true);
        agentText.setForeground(Color.blue);
        agentText.setBackground(new Color(153, 187, 255));
        agentText.setText(temp);
      }
      // setAgentText(thisChoice,temp);
    }
  }

  public void setAgentText(int agentID, String text){
    if(agentID == 0){
      agent0.updateText(text);
    } else if (agentID == 1){
      agent1.updateText(text);
    } else {
      System.out.println("Invalid agent ID");
    }
  }
}
