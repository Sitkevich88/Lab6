package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Commands Parser. Parses server.commands from buffer. If buffer is empty, then parses from console
 */

public class CommandsParser {


    /**
     * Parses a line
     * @return String parsed line
     */
    public static String parseLineWithNoTrim(){

        String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            line = reader.readLine();
        } catch (IOException e) {
            System.out.println("Unable to read file");
        }catch (NullPointerException e){
            System.out.println("Exiting the program...");
            System.exit(0);
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
     * Parses a line and splits it into 2 arguments
     * @return String array with max 2 arguments
     */
    public static String[] parseArguments(){
        String arguments = parseLine();
        return arguments.split("\\s");
    }

}
