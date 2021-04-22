package Match3.IO;
/*
 * @author Henry Jones
 */

import Match3.Document.*;
import java.util.*;
import java.io.*;
import org.apache.commons.io.FilenameUtils;

public class GameIO {
  public GameIO(){

  }

  public void exportState(DecisionTree dt, File saveLoc){
    try {
      FileOutputStream fos = new FileOutputStream(saveLoc);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(dt);
      oos.close();
      fos.close();
      System.out.println("Serialized to: " + saveLoc.getAbsolutePath());
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  public DecisionTree importState(File importLoc){
    DecisionTree dt = null;
    try {
      FileInputStream fis = new FileInputStream(importLoc);
      ObjectInputStream ois = new ObjectInputStream(fis);
      dt = (DecisionTree) ois.readObject();
      ois.close();
      fis.close();
      System.out.println("deserialized from: " + importLoc.getAbsolutePath());
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return dt;
  }
}
