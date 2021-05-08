package commands.withTwoArguments;

import data.MusicBand;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Command 'remove_greater'. Removes all greater elements in the collection
 */

public class RemoveGreater {

    /**
     * Executes the command.
     * @param collection - old collection
     * @param id - long specific id
     * @return updated collection
     */

    public Stack<MusicBand> invoke(String sender, Stack<MusicBand> collection, long id) throws IllegalArgumentException{

        try {
            int initialLength = collection.size();

            try {
                Statement st = DataBaseConnector.getConnection().createStatement();
                st.execute("DELETE FROM music_bands WHERE id >= "+id+" AND owner = \'"+sender+"\';");
                collection = collection.stream().
                        filter(band->band.getId()>=id).
                        collect(Collectors.toCollection(Stack<MusicBand>::new));
            }catch (SQLException e){
                e.printStackTrace();
            }

            if (initialLength==collection.size()){
                throw new NullPointerException();
            }

        }catch (NullPointerException e){
            MessagesForClient.recordMessage("Nothing has been removed. This index is out of range.");
        }
        return collection;
    }
}
