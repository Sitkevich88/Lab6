package commands.withMessage;

import commands.AbstractCommandWhichRequiresCollection;
import data.ClientRequest;
import data.MessageFromServerToClient;
import data.MusicBand;
import utils.MessagesForClient;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'sum_of_number_of_participants'. Counts the total amount of participants
 */

public class SumOfNumberOfParticipants extends AbstractCommandWhichRequiresCollection {

    public SumOfNumberOfParticipants(LinkedBlockingQueue<MusicBand> collection, ClientRequest request) {
        super(collection, request);
    }

    /**
     * Executes the command but does not print the result.
     *
     */

    public void invoke(){

        getClientRequest().setMessage(MessageFromServerToClient.PRINT_LIST);

        if (getCollection()==null){
            getClientRequest().addWords(new String[]{Integer.toString(0)});
            return;}
        Long sumOfNumberOfParticipants = getCollection().stream().
                filter(band->band.getNumberOfParticipants()!=null).
                mapToLong(band->band.getNumberOfParticipants().longValue()).
                sum();
        //getMessages().recordMessage(sumOfNumberOfParticipants.toString() + " participants in total");
        getClientRequest().addWords(new String[]{Long.toString(sumOfNumberOfParticipants)});
    }
}
