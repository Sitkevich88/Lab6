package data;

import java.io.Serializable;

public class ClientRequest implements Serializable {

    private String messages;

    public ClientRequest(String messages){
        this.messages = messages;
    }

    public String getMessages(){
        return messages;
    }
}
