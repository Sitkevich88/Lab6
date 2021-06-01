package commands.withTwoArguments;


import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import data.ProtoMusicBand;
import utils.CommandsParser;
import utils.MessagesForClient;
import utils.ProtoMusicBandCreator;
import utils.sql.DataBaseConnector;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Command 'update'. Updates an element with specific id in the collection
 */


public class UpdateId extends AbstractCommandWhichRequiresCollection {


    public UpdateId(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }

    /**
     * Executes the command.
     * @param id - long specific id
     * @return collection with the updated element
     */

    public void invoke(String sender, long id, ProtoMusicBand protoBand){


        boolean idFound = false;
        String bandName = "";

        if (getCollection()==null){
            return;
        }
        for (MusicBand band : getCollection()){


            if (band.getId()==id && band.getOwner().equals(sender)){

                idFound = true;
                bandName = band.getName();

                if (!CommandsParser.isBufferEmpty()){
                    ProtoMusicBandCreator protoMusicBandCreator = new ProtoMusicBandCreator();
                    protoBand = protoMusicBandCreator.getProtoMusicBandFromScriptInBuffer(getMessages());
                }

                if (protoBand!=null){
                    try {
                        PreparedStatement prst = DataBaseConnector.getConnection().prepareStatement("UPDATE music_bands SET name = ?, x = ?, y = ?, number_of_participants = ?," +
                                " description = ?, establishment_date = ?, genre = CAST (? AS genre), album_name = ?, tracks = ?, length = ?, sales = ? " +
                                "WHERE id = ? AND owner = ?;");

                        prst.setString(1, protoBand.getName());
                        prst.setLong(2,protoBand.getCoordinates().getX());
                        prst.setInt(3,protoBand.getCoordinates().getY());
                        prst.setInt(4,protoBand.getNumberOfParticipants());
                        prst.setString(5, protoBand.getDescription());
                        prst.setTimestamp(6, Timestamp.from(protoBand.getEstablishmentDate().toInstant()));
                        prst.setString(7, protoBand.getGenre().toString());
                        prst.setString(8, protoBand.getName());
                        prst.setInt(9,protoBand.getBestAlbum().getTracks());
                        prst.setInt(10,protoBand.getBestAlbum().getLength());
                        prst.setFloat(11, protoBand.getBestAlbum().getSales());
                        prst.setLong(12, id);
                        prst.setString(13, sender);
                        prst.executeUpdate();

                        band.setName(protoBand.getName());
                        band.setCoordinates(protoBand.getCoordinates());
                        band.setNumberOfParticipants(protoBand.getNumberOfParticipants());
                        band.setDescription(protoBand.getDescription());
                        band.setEstablishmentDate(protoBand.getEstablishmentDate());
                        band.setGenre(protoBand.getGenre());
                        band.setBestAlbum(protoBand.getBestAlbum());
                    }catch (SQLException e){
                        getMessages().recordMessage(e.getMessage());
                    }
                    break;
                }

            }
        }
        if (!idFound){
            getMessages().recordMessage("This id has not been found in your collection");
        } else {
            getMessages().recordMessage(bandName + " has been updated");
        }

    }
}
