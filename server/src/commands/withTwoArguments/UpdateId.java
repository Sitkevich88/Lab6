package commands.withTwoArguments;


import data.MusicBand;
import data.ProtoMusicBand;
import utils.CommandsParser;
import utils.MessagesForClient;
import utils.ProtoMusicBandCreator;
import utils.sql.DataBaseConnector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public void invoke(String sender, Stack<MusicBand> collection, long id, ProtoMusicBand protoBand){


        boolean idFound = false;

        if (collection==null){
            return;
        }
        for (MusicBand band : collection){

            if (band.getId()==id){

                idFound = true;

                if (!CommandsParser.isBufferEmpty()){
                    ProtoMusicBandCreator protoMusicBandCreator = new ProtoMusicBandCreator();
                    protoBand = protoMusicBandCreator.getProtoMusicBandFromScriptInBuffer();
                }

                if (protoBand!=null){
                    try {
                        PreparedStatement prst = DataBaseConnector.getConnection().prepareStatement("UPDATE music_bands SET name = ?, x = ?, y = ?, number_of_participants = ?," +
                                " description = ?, establishment_date = ?, genre = CAST (? AS genre), album_name = ?, tracks = ?, length = ?, sales = ? " +
                                "WHERE id = ?;");

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
                        prst.executeUpdate();

                        band.setName(protoBand.getName());
                        band.setCoordinates(protoBand.getCoordinates());
                        band.setNumberOfParticipants(protoBand.getNumberOfParticipants());
                        band.setDescription(protoBand.getDescription());
                        band.setEstablishmentDate(protoBand.getEstablishmentDate());
                        band.setGenre(protoBand.getGenre());
                        band.setBestAlbum(protoBand.getBestAlbum());
                    }catch (SQLException e){
                        e.printStackTrace();
                        System.exit(1);
                    }
                    break;
                }

            }
        }
        if (!idFound){
            MessagesForClient.recordMessage("This id has not been found");
        }

    }
}
