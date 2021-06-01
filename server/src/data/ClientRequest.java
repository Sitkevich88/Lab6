package data;

import java.io.Serializable;

public class ClientRequest implements Serializable {

    private String messages;
    private boolean checkResult = false;

    public ClientRequest(String messages){
        this.messages = messages;
    }

    public ClientRequest(String messages, boolean checkResult){
        this.messages = messages;
        this.checkResult = checkResult;
    }

    public ClientRequest(boolean checkResult){
        this.checkResult = checkResult;
    }

    public String getMessages(){
        return messages;
    }

    public boolean getResult() {
        return checkResult;
    }

    public void setResult(boolean checkResult) {
        this.checkResult = checkResult;
    }
}
