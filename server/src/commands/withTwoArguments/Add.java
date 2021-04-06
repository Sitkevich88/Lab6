package commands.withTwoArguments;


import data.MusicBand;
import data.ProtoMusicBand;
import utils.CommandsParser;
import utils.MusicBandCreator;

import java.util.Stack;

/**
 * Command 'add'. Adds a new element to the collection.
 */

public class Add {

    /**
     * Executes the command.
     * @param collection - old collection
     * @return Collection. Updated collection with newly added element.
     */

    public Stack<MusicBand> updateCollection(Stack<MusicBand> collection, ProtoMusicBand protoBand){
        MusicBand band;

        if (CommandsParser.isBufferEmpty()){
            band = new MusicBand(protoBand);
        }else {
            MusicBandCreator musicBandCreator = new MusicBandCreator();
            band = musicBandCreator.getMusicBandFromScriptInBuffer();
        }

        if (collection==null){
            collection = new Stack<>();
        }
        if (band!=null){
            collection.push(band);
        }
        return collection;
    }

}
