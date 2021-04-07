package commands.withTwoArguments;



import utils.CommandsParser;
import utils.MessagesForClient;
import utils.ParserMode;

/**
 * Command 'execute_script'. Executes script.
 */


public class ExecuteScript {

    private String script = "";

    public ExecuteScript(String script){
        this.script = script;
    }


    public void execute(){
        if (CommandsParser.getLastMode().equals(ParserMode.REQUEST)){
            CommandsParser.loadScript(script);
        }else {
            MessagesForClient.recordMessage("Script path is unreachable");
        }

    }

}
