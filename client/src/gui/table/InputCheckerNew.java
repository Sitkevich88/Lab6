package gui.table;

import data.MusicGenre;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

import java.time.LocalDate;

public class InputCheckerNew {
    
    private Button okButton;

    
    private ComboBox<Long> idField;

    
    private TextField groupNameField;

    
    private TextField xField;

    
    private TextField yField;

    
    private TextField participantsField;

    
    private TextField descriptionField;

    
    private DatePicker dateField;

    
    private ComboBox<MusicGenre> genreField;

    
    private TextField albumNameField;

    
    private TextField tracksField;

    
    private TextField lengthField;

    
    private TextField salesField;

    public InputCheckerNew(Button okButton, ComboBox<Long> idField, TextField groupNameField, TextField xField, TextField yField,
                           TextField participantsField, TextField descriptionField, DatePicker dateField,
                           ComboBox<MusicGenre> genreField, TextField albumNameField, TextField tracksField,
                           TextField lengthField, TextField salesField) {
        this.okButton = okButton;
        this.idField = idField;
        this.groupNameField = groupNameField;
        this.xField = xField;
        this.yField = yField;
        this.participantsField = participantsField;
        this.descriptionField = descriptionField;
        this.dateField = dateField;
        this.genreField = genreField;
        this.albumNameField = albumNameField;
        this.tracksField = tracksField;
        this.lengthField = lengthField;
        this.salesField = salesField;
    }

    public boolean canUpdate(){
        boolean result = checkGroupName() & checkX() & checkY() & checkParticipants() &
                checkAlbumName() & checkTracks() & checkLength() & checkSales() & checkEstablishmentDate();
        return result;
    }


    private void fieldIsGood(TextField field, boolean good){
        if (good){
            field.setStyle("-fx-background-color: #FFFFFF");
        }else {
            field.setStyle("-fx-background-color: #FFCCCC");
            //field.setText("");
        }
    }

    private boolean checkGroupName(){
        String bandName = groupNameField.getText();
        boolean good = false;
        if (bandName!=null && bandName.length()>0){
            good = true;
        }
        fieldIsGood(groupNameField, good);
        return good;
    }

    private boolean checkX(){
        boolean good = false;
        try {
            Long x = Long.valueOf(xField.getText());
            if (x!=null && x>=0 && x<=100){
                good = true;
            }
        }catch (NullPointerException | NumberFormatException e){
            good = false;
        }

        fieldIsGood(xField, good);
        return good;
    }

    private boolean checkY(){

        boolean good = false;
        try {
            Integer y = Integer.valueOf(yField.getText());
            if (y!=null && y>=0 && y<=100){
                good = true;
            }
        }catch (NullPointerException | NumberFormatException e){
            good = false;
        }

        fieldIsGood(yField, good);
        return good;
    }

    private boolean checkParticipants(){
        boolean good = false;
        Integer participants = null;
        try {
            participants = Integer.valueOf(participantsField.getText());

            if (participants!=null && participants>0){
                good=true;
            }
        }catch (NullPointerException | NumberFormatException e){
            good = false;
            /*if (participantsField.getText()==null || participantsField.getText().length()==0){
                good=true;
            }*/
        }

        fieldIsGood(participantsField, good);
        return good;
    }

    private boolean checkAlbumName(){
        String name = albumNameField.getText();
        boolean good = false;
        if (name!=null && name.length()>0){
            good = true;
        }
        fieldIsGood(albumNameField, good);
        return good;
    }

    private boolean checkTracks(){
        boolean good = false;
        try {
            Integer tracks = Integer.valueOf(tracksField.getText());
            if (tracks!=null && tracks>0){
                good = true;
            }
        }catch (NullPointerException | NumberFormatException e){
            good = false;
        }

        fieldIsGood(tracksField, good);
        return good;
    }

    private boolean checkLength(){
        boolean good = false;

        try {
            Integer length = Integer.valueOf(lengthField.getText());
            if (length!=null && length>0){
                good = true;
            }
        }catch (NullPointerException | NumberFormatException e){
            good = false;
        }

        fieldIsGood(lengthField, good);
        return good;
    }

    private boolean checkSales(){
        boolean good = false;
        try {
            Float sales = Float.valueOf(salesField.getText());

            if (sales!=null && sales>0){
                good = true;
            }
            if (sales==null){
                good = true;
            }
        }catch (NullPointerException | NumberFormatException e){
            good = false;
            /*if (salesField.getText()==null || salesField.getText().length()==0){
                good=true;
            }*/
        }
        fieldIsGood(salesField, good);
        return good;
    }

    private boolean checkEstablishmentDate(){
        boolean good = false;
        try {
            LocalDate date = dateField.getValue();
            if (date!=null){
                good = true;
            }
        }catch (NullPointerException | NumberFormatException e){
            good = false;
        }
        if (good){
            dateField.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));
        }else {
            dateField.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFCCCC"), CornerRadii.EMPTY, Insets.EMPTY)));
            //field.setText("");
        }
        return good;
    }

    /*private boolean checkId(){
        boolean good = false;
        try {
            Long id1 = idField.getValue();
            if (id1!=null && id1>0){
                good = true;
            }
        }catch (NullPointerException | NumberFormatException e){
            good = false;
        }
        if (good){
            dateField.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), CornerRadii.EMPTY, Insets.EMPTY)));
        }else {
            dateField.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFCCCC"), CornerRadii.EMPTY, Insets.EMPTY)));
            //field.setText("");
        }
        return good;
    }*/

}
