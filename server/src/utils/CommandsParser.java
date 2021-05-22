package utils;


import data.ServerRequest;


/**
 * Commands Parser. Parses server.commands from buffer. If buffer is empty, then parses from console
 */

public class CommandsParser {

    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static String buffer = "";
    private static ParserMode lastMode = ParserMode.REQUEST;
    private static MessagesForClient messages;

    public static boolean isBufferEmpty(){
        return buffer.length()==0;
    }

    public static ParserMode getLastMode() {
        return lastMode;
    }

    public static int getBufferSize() {
        return buffer.length();
    }

    public static void setMessages(MessagesForClient messages1){
        messages = messages1;
    }

    public static String parseLine(ServerRequest request){
        String line = "";
        try {
            if (isBufferEmpty()){
                line = request.getCommand();
                if (request.getArguments()!=null){
                    for (String argument : request.getArguments()){
                        line += " " + argument;
                    }
                }
                lastMode = ParserMode.REQUEST;
            }else{
                String[] lines = buffer.split("\\r?\\n",2);
                line = lines[0];
                buffer = lines[1];
                messages.recordMessage(ANSI_GREEN + line + ANSI_RESET);
                lastMode = ParserMode.SCRIPT;
            }
        }catch (NullPointerException e){}
        return line;
    }

    public static String getBufferLine(){
        String line = "";
        lastMode = ParserMode.REQUEST;
        try {
            String[] lines = buffer.split("\\r?\\n",2);
            line = lines[0];
            buffer = lines[1];
            lastMode = ParserMode.SCRIPT;
            messages.recordMessage(ANSI_GREEN + line + ANSI_RESET);
        }catch (NullPointerException e){}
        return line;
    }

    public static void clearBuffer(){
        buffer = "";
    }

    /**
     * Adds script to the buffer
     * @param script String server.commands on separated lines
     */
    public static void loadScript(String script){
        if (script.length()!=0){
            buffer = script + "\n" + buffer;
        }
    }

    /**
     * Parses a line and splits it into 2 arguments
     * @return String array with max 2 arguments
     */
    public static String[] parseArguments(ServerRequest serverRequest){
        String arguments = parseLine(serverRequest);
        return arguments.split("\\s");
    }

}
