package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Commands Parser. Parses server.commands from buffer. If buffer is empty, then parses from console
 */

public class CommandsParser {

    private static String lastScript;
    private static String buffer = "";

    /**
     * Parses a line
     * @return String parsed line
     */
    public static String parseLineWithNoTrim(){
        String line = "";
        if (buffer.length()==0){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.out.println("Unable to read file");
            }catch (NullPointerException e){
                System.out.println("Exiting the program...");
                System.exit(0);
            }
        }else{
            String[] lines = buffer.split("\\r?\\n",2);
            line = lines[0];
            buffer = lines[1];
            System.out.println(line);
        }
        return line;
    }

    public static String parseLine(){
        try {
            return parseLineWithNoTrim().trim();
        }catch (NullPointerException e){
            System.out.println("Exiting the program...");
            System.exit(0);
            return "";
        }
    }

    /**
     * Adds script to the buffer
     * @param script String server.commands on separated lines
     */
    public static void loadScript(String script){
        if (script.length()!=0){
            try {
                if (script.equals(lastScript)&&buffer.length()!=0){
                    System.out.println("Recursion detected, script skipped");
                }else {
                    lastScript = script;
                    buffer = script + "\n" + buffer;
                }
            }catch (NullPointerException e){
                lastScript = script;
                buffer = script + "\n" + buffer;
            }

        }
    }

    /**
     * Parses a line and splits it into 2 arguments
     * @return String array with max 2 arguments
     */
    public static String[] parseArguments(){
        String arguments = parseLine();
        return arguments.split("\\s");
    }

    /**
     *
     * @param line String parsed line
     * @return max 2 String in array split by space
     */
    public static String[] convertLineToArguments(String line){
        return line.split("\\s");
    }

}
