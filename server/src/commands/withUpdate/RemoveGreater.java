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
 * Command 'remove_greater'. Removes all greater elements in the collection
 */

public class RemoveGreater extends AbstractCommandWhichRequiresCollection {


    public RemoveGreater(LinkedBlockingQueue<MusicBand> collection, ClientRequest clientRequest) {
        super(collection, clientRequest);
    }

    /**
     * Executes the command.
     * @param id - long specific id
     * @return updated collection
     */

    public LinkedBlockingQueue<MusicBand> invoke(String sender, long id) throws IllegalArgumentException{
        long numOfRemovedBands = 0;
        //String haveOrHas = "bands have";

        try {
            int initialLength = getCollection().size();

            try {
                Statement st = DataBaseConnector.getConnection().createStatement();
                st.execute("DELETE FROM music_bands WHERE id > "+id+" AND owner = \'"+sender+"\';");
                numOfRemovedBands = getCollection().stream().
                        filter(band->band.getId()>id && band.getOwner().equals(sender)).count();
                /*if (numOfRemovedBands==1){
                    haveOrHas = "band has";
                }*/
                setCollection(getCollection().stream().
                        filter(band->(band.getId()<=id & band.getOwner().equals(sender)) || !band.getOwner().equals(sender)).
                        collect(Collectors.toCollection(LinkedBlockingQueue::new)));
                //getMessages().recordMessage(numOfRemovedBands + " " + haveOrHas + " been deleted");
                getClientRequest().setMessage(MessageFromServerToClient.MANY_OBJECTS_REMOVED);
                getClientRequest().setAuthor(sender);
                getClientRequest().addWords(new String[]{Long.toString(numOfRemovedBands)});
            }catch (SQLException e){
                getClientRequest().setMessage(MessageFromServerToClient.ERROR);
            }

            if (initialLength==getCollection().size()){
                throw new NullPointerException();
            }

        }catch (NullPointerException e){
            //getMessages().recordMessage("Nothing has been removed from your collection. This index is out of range.");
            //getClientRequest().setMessage(MessageFromServerToClient.OK);
            getClientRequest().setMessage(MessageFromServerToClient.MANY_OBJECTS_REMOVED);
            getClientRequest().addWords(new String[]{Long.toString(0L)});
        }
        return (LinkedBlockingQueue<MusicBand>) getCollection();
    }
}
