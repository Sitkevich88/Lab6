package commands.withMaxOneArgument;

import data.MusicBand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Command 'sort'. Sorts collection by band name
 */

public class Sort {

    /**
     *
     * @param collection - collection to be sorted
     * @return sorted collection
     */

    public Stack<MusicBand> invoke(Stack<MusicBand> collection){
        if (collection==null){
            return null;
        }
        return collection.stream().
                sorted().
                collect(Collectors.toCollection(Stack<MusicBand>::new));
    }

}
