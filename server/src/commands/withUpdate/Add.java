package commands.withUpdate;


import commands.AbstractCommandWhichRequiresCollection;
import data.*;
import utils.CommandsParser;
import utils.ProtoMusicBandCreator;
import utils.sql.DataBaseConnector;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'add'. Adds a new element to the collection.
 */

public class Add extends AbstractCommandWhichRequiresCollection {

    public Add(LinkedBlockingQueue<MusicBand> collection, ClientRequest request) {
        super(collection, request);
    }

    /**
     * Executes the command.
     *
     */

    public void updateCollection(String sender, ProtoMusicBand protoBand, ClientRequest clientRequest){

        MusicBand band = null;

        if (CommandsParser.isBufferEmpty()){
            band = convertProtoMusicBandToMusicBand(sender, protoBand, clientRequest);
        }else {
            ProtoMusicBandCreator protoMusicBandCreator = new ProtoMusicBandCreator();
            ProtoMusicBand protoMusicBand = protoMusicBandCreator.getProtoMusicBandFromScriptInBuffer(clientRequest);
            if (protoMusicBand!=null){
                band = convertProtoMusicBandToMusicBand(sender, protoMusicBand, clientRequest);
            }
        }

        if (getCollection()==null){
            setCollection(new LinkedBlockingQueue<>());
        }
        addMusicBand(band, clientRequest);
        getClientRequest().setAuthor(sender);

    }

    private MusicBand convertProtoMusicBandToMusicBand(String sender,ProtoMusicBand protoBand, ClientRequest clientRequest){
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
            ResultSet rs = st.executeQuery("SELECT id, creation_date " +
                    "FROM music_bands WHERE owner=\'"+ sender +"\';");

            ArrayList<Long> ids = new ArrayList<>();
            getCollection().stream().mapToLong(b->b.getId()).map(Long::valueOf).
                    forEach(b1->ids.add(b1));
            long id = 0;

            while (rs.next()){
                id = rs.getLong("id");
                if (!ids.contains(id)){
                    break;
                }
            }

            /*long id = rs.getLong("id");*/
            String name = protoBand.getName();
            Coordinates coordinates = protoBand.getCoordinates();
            Date creationDate = rs.getDate("creation_date");
            Integer numberOfParticipants = protoBand.getNumberOfParticipants();
            String description = protoBand.getDescription();
            ZonedDateTime establishmentDate = protoBand.getEstablishmentDate();
            //ZonedDateTime establishmentDate = ZonedDateTime.ofInstant(rs.getTimestamp("establishment_date").toInstant(), ZoneId.of("UTC"));
            MusicGenre genre = protoBand.getGenre();
            Album bestAlbum = protoBand.getBestAlbum();
            band = new MusicBand(sender, id, name, coordinates, creationDate,
                    numberOfParticipants, description, establishmentDate, genre, bestAlbum);

        }catch (SQLException e){
            //getMessages().recordMessage(e.getMessage());
            clientRequest.setMessage(MessageFromServerToClient.ERROR);
        }finally {
            return band;
        }
    }

    private void addMusicBand(MusicBand band, ClientRequest clientRequest){

        if (band!=null){
            getCollection().add(band);
            clientRequest.setMessage(MessageFromServerToClient.OBJECT_ADDED);
            //getMessages().recordMessage(  "\'" + band.getName() + "\' has been added to your collection");
        }
    }
}