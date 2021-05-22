package commands.withTwoArguments;


import utils.CommandsParser;
import utils.MessagesForClient;
import utils.ParserMode;

/**
 * Command 'execute_script'. Executes script.
 */


public class ExecuteScript {

    //private String script = "";
    private MessagesForClient messages;
    public ExecuteScript(MessagesForClient messages){
        this.messages = messages;
    }


    public void execute(String script){
        if (CommandsParser.getLastMode().equals(ParserMode.REQUEST)){
            CommandsParser.loadScript(script);
        }else {
            messages.recordMessage("Script path is unreachable");
        }

    }

}
