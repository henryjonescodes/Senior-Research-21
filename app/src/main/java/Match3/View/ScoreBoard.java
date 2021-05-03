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

public class ScoreBoard extends JPanel implements GameListener{

  private final String MISSED_CASCADE_NOTIFICATION = "You missed a cascade!";


  private JLabel score;
  private AgentView agent0, agent1;
  private int scoreValue, prevScore, notiValue, thisChoice;
  public ScoreBoard(){
    score = new JLabel("0");
    agent0 = new AgentView();
    agent1 = new AgentView();
    // this.setLayout(new BorderLayout());
    // this.add(agent0, BorderLayout.LINE_START);
    // this.add(score, BorderLayout.CENTER);
    // this.add(agent1, BorderLayout.LINE_END);

    this.setLayout(new FlowLayout());
    this.add(agent0);
    this.add(score);
    this.add(agent1);
  }

  public void setScore(int value){
    prevScore = scoreValue;
    System.out.println("score");
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
    setAgentText(0, " ");
    setAgentText(1, " ");
    if(notiValue == -1){
      setAgentText(1, MISSED_CASCADE_NOTIFICATION);
    } else if (notiValue == 1){
      setAgentText(0, MISSED_CASCADE_NOTIFICATION);
    } else if (prevScore != scoreValue){
      String temp = "Good Job! That's " + String.valueOf(scoreValue - prevScore) + " points!";
      setAgentText(thisChoice,temp);
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
