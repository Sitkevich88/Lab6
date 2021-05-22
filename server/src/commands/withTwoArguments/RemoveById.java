package commands.withTwoArguments;


import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * Command 'remove_by_id'. Removes an element by id from the collection
 */

public class RemoveById extends AbstractCommandWhichRequiresCollection {

    public RemoveById(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }

    /**
     * Executes the command.
     * @param id - specific id
     */

    public void invoke(String sender, long id){

        int initialLength;
        try{
            initialLength = getCollection().size();
            try {
                Statement st = DataBaseConnector.getConnection().createStatement();
                st.execute("DELETE FROM music_bands WHERE id = "+id+" AND owner = \'"+sender+"\';");

            }catch (SQLException e){
                getMessages().recordMessage(e.getMessage());
            }
            setCollection(getCollection().
                    stream().
                    filter(band->band.getId()!=id).
                    collect(Collectors.toCollection(LinkedBlockingQueue::new)));
        }catch (NullPointerException e){
            warnIdDoesNotExist();
            return;
        }

        if (initialLength==getCollection().size()){
            warnIdDoesNotExist();
        }

    }
    private void warnIdDoesNotExist(){
        getMessages().recordMessage("This id does not exist");
    }
}
