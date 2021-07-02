package commands.withUpdate;


import commands.AbstractCommandWhichRequiresCollection;
import data.ClientRequest;
import data.MessageFromServerToClient;
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

    public RemoveById(LinkedBlockingQueue<MusicBand> collection, ClientRequest clientRequest) {
        super(collection, clientRequest);
    }

    /**
     * Executes the command.
     * @param id - specific id
     */

    public LinkedBlockingQueue invoke(String sender, long id){

        int initialLength = 0;
        String bandName = "";
        try{
            initialLength = getCollection().size();
            try {
                Statement st = DataBaseConnector.getConnection().createStatement();
                st.execute("DELETE FROM music_bands WHERE id = "+id+" AND owner = \'"+sender+"\';");
                bandName = getCollection().stream().filter(band->band.getId()==id).
                        map(band->band.getName()).collect(Collectors.joining());
                setCollection(getCollection().
                        stream().
                        filter(band->band.getId()!=id).
                        collect(Collectors.toCollection(LinkedBlockingQueue::new)));
            }catch (SQLException e){
                //getMessages().recordMessage(e.getMessage());
                getClientRequest().setMessage(MessageFromServerToClient.ERROR);
            }
        }catch (NullPointerException e){
            warnIdDoesNotExist();
        }

        if (initialLength==getCollection().size()){
            warnIdDoesNotExist();
        } else {
            getClientRequest().setMessage(MessageFromServerToClient.OBJECT_REMOVED);
            getClientRequest().setAuthor(sender);
            //getMessages().recordMessage(bandName + " has been removed");
        }

        return (LinkedBlockingQueue) getCollection();
    }
    private void warnIdDoesNotExist(){
        getClientRequest().setMessage(MessageFromServerToClient.ERROR);
    }
}
