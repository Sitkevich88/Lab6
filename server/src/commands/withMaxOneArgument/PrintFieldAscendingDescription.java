package commands.withMaxOneArgument;

import data.MusicBand;
import utils.MessagesForClient;

import java.util.Collection;

/**
 * Command 'print_field_ascending_description'. Prints all descriptions in ascending alphabetical order
 */

public class PrintFieldAscendingDescription {

    /**
     * Executes the command.
     * @param collection - collection to examine
     */

    public void invoke(Collection<MusicBand> collection){

        try {
            collection.stream().
                    map(MusicBand::getDescription).
                    filter(description->description!=null && description.length()!=0).
                    sorted().
                    forEach(MessagesForClient::recordMessage);
        }catch (NullPointerException e){ }
    }

}
