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

  public void writeToFile(String toWrite, File location){
    // Write the content in file
    try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(location.getAbsolutePath()))) {
        bufferedWriter.write(toWrite);
    } catch (IOException e) {
        // Exception handling
    }
  }

  public String readFromFile(File location){
    Map<String, int[][]> decoded = new HashMap<String, int[][]>();
    Map<Integer, Vector<String>> names = new HashMap<Integer, Vector<String>>();

    boolean readingBoardLines = false;
    boolean readingBoard = false;

    String boardName = "";
    boolean readingMove = true;
    int currentMove = 0;
    int numMoves = 0;
    int numRows = 0;
    int numCols = 0;
    // Read the content from file
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(location.getAbsolutePath()))) {
        String line = bufferedReader.readLine();
        Vector<String> lineOfNames = new Vector<>();
        while(line != null) {
            // System.out.println(line);
            //Get metadata from save file
            if(line.equals(IO_Format.TREE_HEADER)){
              //Advance to the info line
              line = bufferedReader.readLine();
              System.out.println(line);

              //Read integers from data line
              numRows = Character.getNumericValue(line.charAt(0));
              numCols = Character.getNumericValue(line.charAt(2));
              numMoves = Character.getNumericValue(line.charAt(4));
              System.out.println("Recovered Rows: " + numRows + " Cols: " + numCols + " Moves" + numMoves);
              line = bufferedReader.readLine();
            }
            else if(line.equals(IO_Format.BOARD_HEADER)){
              //Advance to the neme line
              line = bufferedReader.readLine();
              boardName = line;
              lineOfNames.add(boardName);
              System.out.println(line);

              //Reading board, advance to start
              readingBoard = true;
              line = bufferedReader.readLine();
            }
            else if(readingBoard){
              int[][] thisBoard = new int[numRows][numCols];
              for(int i = 0; i < numRows; i++){
                int colIndex = 0;
                for(int j = 0; j < numCols + (numCols - 1); j += 2){
                  thisBoard[i][colIndex] = Character.getNumericValue(line.charAt(j));
                  colIndex += 1;
                }
                System.out.println(line);
                line = bufferedReader.readLine();
              }
              decoded.put(boardName, thisBoard);
              readingBoard = false;
            }
            //Read each move
            else if(line.equals(IO_Format.MOVE_SEPPARATOR)){
              names.put(currentMove, lineOfNames);
              currentMove += 1;
              line = bufferedReader.readLine();
            }
            else if(line.equals(IO_Format.TREE_FOOTER)){
              System.out.println("Done Reading from file " + location.getAbsolutePath());
              line = bufferedReader.readLine();
            }
        }
    } catch (FileNotFoundException e) {
        // Exception handling
    } catch (IOException e) {
        // Exception handling
    }
    return null;
  }
}
