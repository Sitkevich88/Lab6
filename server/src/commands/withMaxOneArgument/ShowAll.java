package commands.withMaxOneArgument;

import data.Album;
import data.Coordinates;
import data.MusicBand;
import data.MusicGenre;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Stack;

/**
 * Command 'show_all'. Prints all objects of the collection
 */

public class ShowAll {
    /**
     * Executes the command.
     */
    public ShowAll(){
        Stack<MusicBand> bands = new Stack<>();
        try {
            Statement st = DataBaseConnector.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name, x, y, creation_date, number_of_participants, " +
                    "description, establishment_date, genre, album_name, tracks, length, sales " +
                    "FROM music_bands ;");
            while (rs.next()){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                Coordinates coordinates = new Coordinates(rs.getInt("x"),rs.getInt("y"));
                Date creationDate = rs.getDate("creation_date");
                Integer numberOfParticipants = rs.getInt("number_of_participants");
                String description = rs.getString("description");
                ZonedDateTime establishmentDate = rs.getTimestamp("establishment_date").toLocalDateTime().atZone(ZoneId.of("+3"));
                MusicGenre genre = MusicGenre.getEnum(rs.getString("genre"));
                Album bestAlbum = new Album(rs.getString("album_name"), rs.getInt("tracks"),
                        rs.getInt("length"), rs.getFloat("sales"));
                bands.add(new MusicBand(id, name, coordinates, creationDate,
                        numberOfParticipants, description, establishmentDate, genre, bestAlbum));
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        MessagesForClient.recordMessage(bands.toString());

    }
}
