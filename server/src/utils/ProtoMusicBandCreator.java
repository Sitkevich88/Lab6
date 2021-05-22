package utils;

import data.*;
import java.time.ZonedDateTime;

public class ProtoMusicBandCreator {

    //private InputFromBufferValidator checker = new InputFromBufferValidator();

    /**
     * Executes the command.
     * @return MusicBand new band
     */

    public ProtoMusicBand getProtoMusicBandFromScriptInBuffer(MessagesForClient messages){


        InputFromBufferValidator checker = new InputFromBufferValidator(messages);

        try {
            String name = checker.nextLine("Insert the name: ",false, false);
            long x = checker.nextLong("Insert the coordinate x: ");
            Integer y = checker.nextInt("Insert the coordinate y: ",false,false);
            Integer numberOfParticipants = checker.nextInt("Insert the number of participants : ",true, true);
            String description = checker.nextLine("Insert the description : ", true, true);
            ZonedDateTime establishmentDate = checker.nextZonedDateTime();
            MusicGenre genre = checker.nextMusicGenre("Choose the music genre : ");
            String albumName = checker.nextLine("Insert the name of their best album : ", false, false);
            int tracks = checker.nextInt( "Insert the number of tracks in their best album : ", false, true).intValue();
            int length = checker.nextInt("Insert the length of their best album : ",false, true).intValue();
            Float sales = checker.nextFloat("Insert the sales of their best album : ", true,true);
            ProtoMusicBand pBand = new ProtoMusicBand(name, new Coordinates(x, y),
                    numberOfParticipants, description, establishmentDate,
                    genre, new Album(albumName, tracks, length, sales));

            return pBand;
        }catch (EmptyBufferException e){
            messages.recordMessage("Script contains a MusicBand field which cannot be parsed");
            return null;
        }

    }
}
