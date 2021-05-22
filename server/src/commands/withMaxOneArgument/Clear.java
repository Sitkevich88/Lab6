package commands.withMaxOneArgument;


import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'clear'. Clears the collection.
 */

public class Clear extends AbstractCommandWhichRequiresCollection {

    public Clear(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }


    public void invoke(String sender){

        if (getCollection()!=null){
            try {
                Statement st = DataBaseConnector.getConnection().createStatement();
                st.execute("DELETE FROM music_bands WHERE owner = \'"+ sender + "\';");
                getCollection().removeIf(musicBand -> musicBand.getOwner().equals(sender));
                //collection.clear();
            } catch (SQLException e) {
                getMessages().recordMessage(e.getMessage());
            }
        }
    }
}
