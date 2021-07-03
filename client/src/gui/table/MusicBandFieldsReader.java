package gui.table;

import data.MusicBand;
import java.util.HashSet;

public class MusicBandFieldsReader {

    public boolean findString(MusicBand band, String lowerCaseFilter){
        HashSet<Boolean> booleans = new HashSet<>();
        booleans.add(Long.toString(band.getId()).contains(lowerCaseFilter));
        booleans.add(band.getOwner().toLowerCase().contains(lowerCaseFilter));
        booleans.add(band.getName().toLowerCase().contains(lowerCaseFilter));
        booleans.add(Long.toString(band.getCoordinates().getX()).contains(lowerCaseFilter));
        booleans.add(Integer.toString(band.getCoordinates().getY()).contains(lowerCaseFilter));
        booleans.add(band.getCreationDate().toString().toLowerCase().contains(lowerCaseFilter));
        booleans.add(band.getEstablishmentDate().toString().toLowerCase().contains(lowerCaseFilter));
        booleans.add(band.getGenre().toString().toLowerCase().contains(lowerCaseFilter));
        booleans.add(band.getBestAlbum().getName().toLowerCase().contains(lowerCaseFilter));
        booleans.add(Integer.toString(band.getBestAlbum().getTracks()).toLowerCase().contains(lowerCaseFilter));
        booleans.add(Integer.toString(band.getBestAlbum().getLength()).toLowerCase().contains(lowerCaseFilter));
        try {
            booleans.add(Integer.toString(band.getNumberOfParticipants()).contains(lowerCaseFilter));
        }catch (NullPointerException e){}

        try {
            booleans.add(band.getDescription().toLowerCase().contains(lowerCaseFilter));
        }catch (NullPointerException e){}

        try {
            booleans.add(Float.toString(band.getBestAlbum().getSales()).toLowerCase().contains(lowerCaseFilter));
        }catch (NullPointerException e){}


        if (booleans.contains(true)){
            return true;
        }else{
            return false;
        }
    }
}
