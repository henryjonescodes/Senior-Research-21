package Match3.IO;
/*
 * @author Henry Jones
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;



public class IconLoader{
  public final int IMAGE_SIZE = 40;
  public final int AGENT_SIZE = 65;
  public IconLoader(){

  }

  public ImageIcon[] getAgentImgs(){
    ImageIcon[] icons = null;
    try {
      ImageIcon img1 = readSingleIcon("src/main/java/Match3/Icons/agent0.png",AGENT_SIZE);
      ImageIcon img2 = readSingleIcon("src/main/java/Match3/Icons/agent1.png",AGENT_SIZE);
      icons = new ImageIcon[]{img1,img2};
    } catch (Exception ex) {
       System.out.println(ex);
    }
    return icons;
  }

  public ImageIcon[] getIcons(){
    ImageIcon[] icons = null;
    try {
      ImageIcon img1 = readSingleIcon("src/main/java/Match3/Icons/icon1.png",IMAGE_SIZE);
      ImageIcon img2 = readSingleIcon("src/main/java/Match3/Icons/icon2.png",IMAGE_SIZE);
      ImageIcon img3 = readSingleIcon("src/main/java/Match3/Icons/icon3.png",IMAGE_SIZE);
      ImageIcon img4 = readSingleIcon("src/main/java/Match3/Icons/icon4.png",IMAGE_SIZE);
      ImageIcon img5 = readSingleIcon("src/main/java/Match3/Icons/icon5.png",IMAGE_SIZE);
      icons = new ImageIcon[]{img1,img2,img3,img4,img5};
    } catch (Exception ex) {
       System.out.println(ex);
    }
    return icons;
  }

  private ImageIcon readSingleIcon(String location, int imageSize){
    ImageIcon icon = null;
    try {
        BufferedImage myPicture = ImageIO.read(new File(location));
        Image scaled = myPicture.getScaledInstance(imageSize,imageSize,Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaled);
    } catch (Exception ex) {
       System.out.println(ex);
    }
    return icon;
  }
}
