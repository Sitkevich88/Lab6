package commands.withUpdate;


import data.ClientRequest;
import data.MessageFromServerToClient;
import utils.CommandsParser;
import utils.ParserMode;

/**
 * Command 'execute_script'. Executes script.
 */


public class ExecuteScript {

    //private String script = "";
    private ClientRequest clientRequest;
    public ExecuteScript(ClientRequest clientRequest){
        this.clientRequest = clientRequest;
    }


    public void execute(String script){
        if (CommandsParser.getLastMode().equals(ParserMode.REQUEST)){
            CommandsParser.loadScript(script);
        }else {
            //messages.recordMessage("Script path is unreachable");
            clientRequest.setMessage(MessageFromServerToClient.ERROR);
        }

    }

}
