package data;

import java.time.ZonedDateTime;
import java.util.Date;

public class MusicBandForTable extends MusicBand{

    public MusicBandForTable(String owner, long id, String name, Coordinates coordinates, Date creationDate, Integer numberOfParticipants, String description, ZonedDateTime establishmentDate, MusicGenre genre, Album bestAlbum) {
        super(owner, id, name, coordinates, creationDate, numberOfParticipants, description, establishmentDate, genre, bestAlbum);
    }
}
