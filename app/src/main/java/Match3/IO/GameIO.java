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
        System.out.println("Saved:  " + location.getAbsolutePath());
    } catch (IOException e) {
        // Exception handling
    }
  }

  private int[] getHighlightValues(String line){
      int[] out = new int[4];
      out[0] = Character.getNumericValue(line.charAt(0));
      out[1] = Character.getNumericValue(line.charAt(2));
      out[2] = Character.getNumericValue(line.charAt(4));
      out[3] = Character.getNumericValue(line.charAt(6));
      return out;
  }

  public DecisionTree readFromFile(File location){
    Map<String, int[][]> decoded = new HashMap<String, int[][]>();
    Map<Integer, Vector<String>> names = new HashMap<Integer, Vector<String>>();
    Map<String, Vector<int[]>> highlights = new HashMap<String, Vector<int[]>>();
    Map<String, Integer> scores = new HashMap<String, Integer>();
    Map<String, Integer> notifications = new HashMap<String, Integer>();



    boolean readingBoardLines = false;
    boolean readingBoard = false;

    String boardName = "";
    boolean readingMove = true;
    int currentMove = 1;
    int numMoves = 0;
    int numRows = 0;
    int numCols = 0;

    //Initialize structs for board data
    int[] agentA = new int[4];
    int[] agentB = new int[4];
    int[] highLight = new int[4];
    Vector<int[]> theseHighlights = new Vector<int[]>();
    int score = 0;
    int notification = 0;

    int boardCounter = 0;
    int numNamesAdded = 0;

    // Read the content from file
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(location.getAbsolutePath()))) {
        String line = bufferedReader.readLine();
        Vector<String> lineOfNames = new Vector<>();
        while(line != null) {
          if(!readingBoard){
              if(line.equals(IO_Format.TREE_HEADER)){
                //Advance to the info line
                line = bufferedReader.readLine();
                // System.out.println(line);

                //Read integers from data line
                numRows = Character.getNumericValue(line.charAt(0));
                numCols = Character.getNumericValue(line.charAt(2));
                numMoves = Character.getNumericValue(line.charAt(4));

                //Trace
                // System.out.println("Recovered Rows: " + numRows + " Cols: " + numCols + " Moves: " + numMoves);

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.BOARD_HEADER)){
                //Advance to the neme line
                line = bufferedReader.readLine();
                // line = bufferedReader.readLine();
                boardName = line;
                lineOfNames.add(boardName);

                //re-Initialize structs for this boards data
                agentA = new int[4];
                agentB = new int[4];
                highLight = new int[4];
                theseHighlights = new Vector<int[]>();
                score = 0;
                notification = 0;

                //Trace
                numNamesAdded += 1;
                // System.out.println("Recovered Name: " + boardName);

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.AGENT_A)){
                line = bufferedReader.readLine();
                // System.out.println("Attempting AgentA: " + line);

                agentA = getHighlightValues(line);

                // System.out.println("Recovered AgentA: " + line);

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.AGENT_B)){
                line = bufferedReader.readLine();
                // System.out.println("Attempting AgentB: " + line);

                agentB = getHighlightValues(line);
//
                // System.out.println("Recovered AgentB: " + line);

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.HIGHLIGHT)){
                line = bufferedReader.readLine();
                // System.out.println("Attempting highLight: " + line);

                highLight = getHighlightValues(line);

                // System.out.println("Recovered highLight: " + line);

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.SCORE_HEADER)){
                line = bufferedReader.readLine();
                score = Integer.parseInt(line);

                scores.put(boardName, score);

                // System.out.println("Recovered Score: " + score);

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.NOTIFICATION_HEADER)){
                line = bufferedReader.readLine();
                notification = Integer.parseInt(line);

                notifications.put(boardName, notification);

                // System.out.println("Recovered notification: " + notification);

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.BOARD_FOOTER)){
                //Save highlights
                theseHighlights.add(agentA);
                theseHighlights.add(agentB);
                theseHighlights.add(highLight);
                highlights.put(boardName,theseHighlights);
                readingBoard = false;

                // System.out.println("Finished Reading Board: " + boardName);

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.MOVE_SEPPARATOR)){
                names.put(currentMove, lineOfNames);
                lineOfNames = new Vector<>();
                // System.out.println("Sepparating Move: " + currentMove + " Names: " + names.get(currentMove).size());
                currentMove += 1;

                //Advance
                line = bufferedReader.readLine();
              }
              else if(line.equals(IO_Format.TREE_FOOTER)){
                System.out.println("Done Reading from file " + location.getAbsolutePath());
                break;
              }
              else if(line.equals(IO_Format.CONTENT_HEADER)){
                readingBoard = true;
                //Advance
                line = bufferedReader.readLine();
              }
              else{
                System.out.println("No Tag Found: " + line);
                //Advance
                line = bufferedReader.readLine();
              }
           }
           //if readingBoard
           else {
             boardCounter += 1;
             //Read the board into a 2-D array
             int[][] thisBoard = new int[numRows][numCols];
             for(int i = 0; i < numRows; i++){
               int colIndex = 0;
               for(int j = 0; j < numCols + (numCols - 1); j += 2){
                 thisBoard[i][colIndex] = Character.getNumericValue(line.charAt(j));
                 colIndex += 1;
               }
               // System.out.println(line);
               line = bufferedReader.readLine();
             }
             decoded.put(boardName, thisBoard);
             readingBoard = false;
           }
      }
    } catch (FileNotFoundException e) {
        // Exception handling
    } catch (IOException e) {
        // Exception handling
    }
    // for(String key : decoded.keySet()){
    //     System.out.print(key + "\n");
    //      for(int i = 0; i < numRows; i++){
    //           for(int j = 0; j < numCols; j++){
    //             System.out.print(decoded.get(key)[i][j]);
    //           }
    //           System.out.print("\n");
    //         }
    //
    // }
    // System.out.println("Number of boards: " + boardCounter);
    // System.out.println("Number of names added: " + numNamesAdded);
    // System.out.println("Number of decoded: " + decoded.size());
    // for(int move: names.keySet()){
    //   System.out.println("Move: " + move + " Names: " +  names.get(move).size());
    // }


    return rebuildTree(names, decoded, highlights,numRows,numCols,numMoves,scores,notifications);
  }


  private DecisionTree rebuildTree(Map<Integer, Vector<String>> names,
                                   Map<String, int[][]> decodedBoards,
                                   Map<String, Vector<int[]>> highlights,
                                   int numRows, int numCols, int numMoves,
                                   Map<String, Integer> scores,
                                   Map<String, Integer> notifications){

      Map<Integer, Vector<BoardState>> boardMap = new HashMap<Integer, Vector<BoardState>>();
      Map<Integer, Vector<String>> nameMap = new HashMap<Integer, Vector<String>>();

      DecisionTree dt = new DecisionTree(numMoves);
      dt.generateBlankTree();
      // System.out.println("Number of root boards: " + dt.numStates());

      int currentMove = 1;

      //Iterate through each move (each layer in the tree)
      for(int move = 1; move <= numMoves; move += 1){
        // System.out.println("Rebuilding move: " + move);
        //Create storage for resulting states
        Vector<BoardState> stateVec = new Vector<BoardState>();
        Vector<String> nameVec = new Vector<String>();

        //Fill in the right amount of root boards (and their cascades)
        int numRootBoards = dt.getNumBoardsAtMove(move);
        // System.out.println("numRootBoards at move " + move + ": " + numRootBoards);


        BoardState root;
        int i = 0;
        int boardCount = 0;
        // System.out.println("NumNames at " + move + ": " + names.get(move).size());
        while(i < names.get(move).size()){
          String thisName = names.get(move).elementAt(i);
          //IF this is a root board
          if(!thisName.contains("~")){
            // System.out.println("Adding Root Board: " + thisName);
            // System.out.println("i = " + i);
            root = new BoardState(decodedBoards.get(thisName),thisName,numRows,numCols,
                                  scores.get(thisName),notifications.get(thisName));

            root.updateAgentSelection(0,highlights.get(thisName).elementAt(0));
            root.updateAgentSelection(1,highlights.get(thisName).elementAt(1));
            root.updateAgentSelection(2,highlights.get(thisName).elementAt(2));
            stateVec.add(root);
            nameVec.add(thisName);
            boardCount += 1;
          }
          //If this is NOT a root board
          else {
            // System.out.println("Adding Non-Root Board: " + thisName);
            // System.out.println("i = " + i);
            BoardState thisBoard = new BoardState(decodedBoards.get(thisName),thisName,numRows,numCols,
                                                  scores.get(thisName),notifications.get(thisName));

            thisBoard.updateAgentSelection(0,highlights.get(thisName).elementAt(0));
            thisBoard.updateAgentSelection(1,highlights.get(thisName).elementAt(1));
            thisBoard.updateAgentSelection(2,highlights.get(thisName).elementAt(2));
            stateVec.get(boardCount-1).loadCascade(thisBoard);
          }
          i++;
        }
        nameMap.put(move, nameVec);
        boardMap.put(move, stateVec);
      }
      dt.importFromMap(boardMap, nameMap);
      // System.out.println("Number of boards: " + dt.numStates());

      // System.out.println(dt);
      return dt;

  }
}
