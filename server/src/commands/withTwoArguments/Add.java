package commands.withTwoArguments;


import data.*;
import utils.CommandsParser;
import utils.MessagesForClient;
import utils.MusicBandCreator;
import utils.ProtoMusicBandCreator;
import utils.sql.DataBaseConnector;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public void updateCollection(String sender, Stack<MusicBand> collection, ProtoMusicBand protoBand){
        MusicBand band = null;

        if (CommandsParser.isBufferEmpty()){
            band = convertProtoMusicBandToMusicBand(sender, protoBand);
        }else {
            ProtoMusicBandCreator protoMusicBandCreator = new ProtoMusicBandCreator();
            ProtoMusicBand protoMusicBand = protoMusicBandCreator.getProtoMusicBandFromScriptInBuffer();
            if (protoMusicBand!=null){
                band = convertProtoMusicBandToMusicBand(sender, protoMusicBand);
            }
        }

        if (collection==null){
            collection = new Stack<>();
        }
        if (band!=null){
            collection.add(band);
        }

    }

    private MusicBand convertProtoMusicBandToMusicBand(String sender,ProtoMusicBand protoBand){
        MusicBand band = null;
        try {
            PreparedStatement prst = DataBaseConnector.getConnection().prepareStatement("INSERT INTO music_bands (owner, name, x, y, number_of_participants," +
                    " description, establishment_date, genre, album_name, tracks, length, sales) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, CAST (? AS genre), ?, ?, ?, ?);");

            prst.setString(1,sender);
            prst.setString(2, protoBand.getName());
            prst.setLong(3,protoBand.getCoordinates().getX());
            prst.setInt(4,protoBand.getCoordinates().getY());
            prst.setInt(5,protoBand.getNumberOfParticipants());
            prst.setString(6, protoBand.getDescription());
            prst.setTimestamp(7, Timestamp.from(protoBand.getEstablishmentDate().toInstant()));
            prst.setString(8, protoBand.getGenre().toString());
            prst.setString(9, protoBand.getName());
            prst.setInt(10,protoBand.getBestAlbum().getTracks());
            prst.setInt(11,protoBand.getBestAlbum().getLength());
            prst.setFloat(12, protoBand.getBestAlbum().getSales());
            prst.executeUpdate();

            Statement st = DataBaseConnector.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name, x, y, creation_date, number_of_participants, " +
                    "description, establishment_date, genre, album_name, tracks, length, sales " +
                    "FROM music_bands WHERE owner=\'"+ sender +"\';");
            rs.next();
            long id = rs.getLong("id");
            String name = rs.getString("name");
            Coordinates coordinates = new Coordinates(rs.getInt("x"),rs.getInt("y"));
            Date creationDate = rs.getDate("creation_date");
            Integer numberOfParticipants = rs.getInt("number_of_participants");
            String description = rs.getString("description");
            ZonedDateTime establishmentDate = rs.getTimestamp("establishment_date").toLocalDateTime().atZone(ZoneId.of("+3"));
            //ZonedDateTime establishmentDate = ZonedDateTime.ofInstant(rs.getTimestamp("establishment_date").toInstant(), ZoneId.of("UTC"));
            MusicGenre genre = MusicGenre.getEnum(rs.getString("genre"));
            Album bestAlbum = new Album(rs.getString("album_name"), rs.getInt("tracks"),
                    rs.getInt("length"), rs.getFloat("sales"));
            band = new MusicBand(id, name, coordinates, creationDate,
                    numberOfParticipants, description, establishmentDate, genre, bestAlbum);

        }catch (SQLException e){
            MessagesForClient.recordMessage(e.getMessage());
        }finally {
            return band;
        }
    }

}
