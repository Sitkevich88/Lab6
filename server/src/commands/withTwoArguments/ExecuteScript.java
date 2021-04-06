package commands.withTwoArguments;



import utils.CommandsParser;

/**
 * Command 'execute_script'. Executes script.
 */


public class ExecuteScript {

    private String script = "";

    public ExecuteScript(String script){
        this.script = script;
    }


    public void execute(){
        CommandsParser.loadScript(script);
    }

}
