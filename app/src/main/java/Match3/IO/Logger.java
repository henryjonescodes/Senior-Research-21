package Match3.IO;

import java.util.*;
import java.io.*;
// import java.util.stream.Collectors;

public class Logger{

  private final String[] LOG_HEADER = new String[]{"Move","Name","Choice","Score","Notification"};
  private Vector<String[]> loggedLines;
  public Logger(){
    loggedLines = new Vector<String[]>();
    loggedLines.add(LOG_HEADER);
  }

  public void logMove(int move, String name, int choice, int score, int notification){
    String[] line = new String[]{String.valueOf(move), name,
                                 String.valueOf(choice),
                                 String.valueOf(score),
                                 String.valueOf(notification)};
    loggedLines.add(line);
  }

  public void write(File here) throws IOException{
    try(FileWriter csvWriter = new FileWriter(here)){
      for(String[] logLine : loggedLines){
        csvWriter.append(String.join(",", logLine));
        csvWriter.append("\n");
      }
      csvWriter.flush();
      csvWriter.close();
    }
  }
}
