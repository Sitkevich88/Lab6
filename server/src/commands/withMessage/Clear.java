package commands.withMessage;


import commands.AbstractCommandWhichRequiresCollection;
import data.ClientRequest;
import data.MessageFromServerToClient;
import data.MusicBand;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'clear'. Clears the collection.
 */

public class Clear extends AbstractCommandWhichRequiresCollection {

    public Clear(LinkedBlockingQueue<MusicBand> collection, ClientRequest request) {
        super(collection, request);
    }


    public void invoke(String sender){

        if (getCollection()!=null){
            try {
                Statement st = DataBaseConnector.getConnection().createStatement();
                st.execute("DELETE FROM music_bands WHERE owner = \'"+ sender + "\';");
                getCollection().removeIf(musicBand -> musicBand.getOwner().equals(sender));
                getClientRequest().setMessage(MessageFromServerToClient.PRIVATE_COLLECTION_CLEARED);
                getClientRequest().setAuthor(sender);
                //getMessages().recordMessage("Now your collection is empty");
            } catch (SQLException e) {
                //getMessages().recordMessage(e.getMessage());
            }
        }
    }
}
