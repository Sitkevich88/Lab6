package commands.withMaxOneArgument;

import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import utils.MessagesForClient;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'sum_of_number_of_participants'. Counts the total amount of participants
 */

public class SumOfNumberOfParticipants extends AbstractCommandWhichRequiresCollection {

    public SumOfNumberOfParticipants(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }

    /**
     * Executes the command but does not print the result.
     *
     */

    public void invoke(){
        if (getCollection()==null){return;}
        Long sumOfNumberOfParticipants = getCollection().stream().
                filter(band->band.getNumberOfParticipants()!=null).
                mapToLong(band->band.getNumberOfParticipants().longValue()).
                sum();
        getMessages().recordMessage(sumOfNumberOfParticipants.toString() + " participants in total");
    }
}
