package commands.withMaxOneArgument;

import data.MusicBand;
import utils.MessagesForClient;

import java.util.Collection;

/**
 * Command 'sum_of_number_of_participants'. Counts the total amount of participants
 */

public class SumOfNumberOfParticipants {

    /**
     * Executes the command but does not print the result.
     * @param collection - collection to be examined
     * @return long total amount of participants
     */

    public long invoke(Collection<MusicBand> collection){
        if (collection==null){return 0;}
        return collection.stream().
                filter(band->band.getNumberOfParticipants()!=null).
                mapToLong(band->band.getNumberOfParticipants().longValue()).
                sum();
    }

    /**
     * Executes the command and prints the result.
     * @param collection - collection to be examined
     */

    public void print(Collection<MusicBand> collection){
        Long sumOfNumberOfParticipants = invoke(collection);
        MessagesForClient.recordMessage(sumOfNumberOfParticipants.toString() + " participants in total");
    }
}
