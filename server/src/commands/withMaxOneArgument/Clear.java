package commands.withMaxOneArgument;


import data.MusicBand;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

/**
 * Command 'clear'. Clears the collection.
 */

public class Clear {

    /**
     *
     * Executes the command.
     * @param collection - old collection
     */

    public void invoke(String sender, Stack<MusicBand> collection){

        if (collection!=null){
            try {
                Statement st = DataBaseConnector.getConnection().createStatement();
                st.execute("DELETE FROM music_bands WHERE owner = \'"+ sender + "\';");
                collection.clear();
            } catch (SQLException throwables) {
                MessagesForClient.recordMessage(throwables.getMessage());
            }
        }
    }
}
