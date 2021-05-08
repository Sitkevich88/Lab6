package commands.withTwoArguments;


import data.MusicBand;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Command 'remove_by_id'. Removes an element by id from the collection
 */

public class RemoveById {

    /**
     * Executes the command.
     * @param collection - old collection
     * @param id - specific id
     * @return updated collection
     */

    public void invoke(String sender, Stack<MusicBand> collection, long id){

        int initialLength = 0;
        try{
            initialLength = collection.size();
            try {
                Statement st = DataBaseConnector.getConnection().createStatement();
                st.execute("DELETE FROM music_bands WHERE id = "+id+" AND owner = \'"+sender+"\';");

            }catch (SQLException e){
                MessagesForClient.recordMessage(e.getMessage());
            }
            collection = collection.
                    stream().
                    filter(band->band.getId()!=id).
                    collect(Collectors.toCollection(Stack<MusicBand>::new));
        }catch (NullPointerException e){
            warnIdDoesNotExist();
            return;
        }

        if (initialLength==collection.size()){
            warnIdDoesNotExist();
        }

    }
    private void warnIdDoesNotExist(){
        MessagesForClient.recordMessage("This id does not exist");
    }
}
