package commands.withTwoArguments;


import data.MusicBand;
import data.ProtoMusicBand;
import utils.CommandsParser;
import utils.MessagesForClient;
import utils.ProtoMusicBandCreator;

import java.util.Stack;


/**
 * Command 'update'. Updates an element with specific id in the collection
 */


public class UpdateId {


    /**
     * Executes the command.
     * @param collection - old collection
     * @param id - long specific id
     * @return collection with the updated element
     */

    public Stack<MusicBand> invoke(Stack<MusicBand> collection, long id, ProtoMusicBand pBand){


        boolean idFound = false;

        if (collection==null){
            return null;
        }
        for (MusicBand band : collection){

            if (band.getId()==id){

                idFound = true;

                if (!CommandsParser.isBufferEmpty()){
                    ProtoMusicBandCreator protoMusicBandCreator = new ProtoMusicBandCreator();
                    pBand = protoMusicBandCreator.getProtoMusicBandFromScriptInBuffer();
                }

                if (pBand!=null){
                    band.setName(pBand.getName());
                    band.setCoordinates(pBand.getCoordinates());
                    band.setNumberOfParticipants(pBand.getNumberOfParticipants());
                    band.setDescription(pBand.getDescription());
                    band.setEstablishmentDate(pBand.getEstablishmentDate());
                    band.setGenre(pBand.getGenre());
                    band.setBestAlbum(pBand.getBestAlbum());
                    break;
                }

            }
        }
        if (!idFound){
            MessagesForClient.recordMessage("This id has not be found");
        }


        return collection;
    }
}
