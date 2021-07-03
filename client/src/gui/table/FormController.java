package gui.table;

import data.*;
import gui.App;
import gui.AppWindow;
import gui.common.UTF8Control;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import utils.ServerRequestsFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FormController {

    @FXML
    private Button okButton;

    @FXML
    private ComboBox<Long> idField;

    @FXML
    private TextField groupNameField;

    @FXML
    private TextField xField;

    @FXML
    private TextField yField;

    @FXML
    private TextField participantsField;

    @FXML
    private TextField descriptionField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<MusicGenre> genreField;

    @FXML
    private TextField albumNameField;

    @FXML
    private TextField tracksField;

    @FXML
    private TextField lengthField;

    @FXML
    private TextField salesField;

    @FXML
    private Label id;

    @FXML
    private Label groupName;

    @FXML
    private Label x;

    @FXML
    private Label y;

    @FXML
    private Label participants;

    @FXML
    private Label description;

    @FXML
    private Label establishmentDate;

    @FXML
    private Label genre;

    @FXML
    private Label albumName;

    @FXML
    private Label tracks;

    @FXML
    private Label length;

    @FXML
    private Label sales;

    @FXML
    private Button cancelButton;

    @FXML
    void cancel(ActionEvent event) {
        close();
    }

    @FXML
    void okPressed(MouseEvent event) {

    }

    @FXML
    void initialize(){
        cancelButton.setGraphic(Glyph.create("FontAwesome|" + FontAwesome.Glyph.BACKWARD.name()));
        inputCheckerNew = new InputCheckerNew(okButton, idField, groupNameField, xField, yField,
                participantsField, descriptionField, dateField, genreField, albumNameField, tracksField, lengthField, salesField);
        setLanguages();
        showDefault();
        clearFields();
    }

    private InputCheckerNew inputCheckerNew;

    void showDefault(){

        ArrayList<MusicGenre> genres = Arrays.stream(MusicGenre.values()).collect(Collectors.toCollection(ArrayList::new));
        ObservableList<MusicGenre> listOfGenres = FXCollections.observableArrayList(genres);
        genreField.setItems(listOfGenres);
        genreField.setValue(MusicGenre.POP);
        setFieldsColour("#FFFFFF");
        setVisibleAllFields(true);
        setVisibleAllLabels(true);
        setEditableAllFields(true);
        clearFields();
    }

    void add(){
        showDefault();
        //clearFields();
        okButton.setDisable(false);
        idField.setVisible(false);
        id.setVisible(false);

        okButton.setOnMouseClicked(event -> {
            if (inputCheckerNew.canUpdate()){
                //pb = createProtoMusicBand();
                ArrayList<String> str = createProtoMusicBand("add");
                //str.add(idField.getValue().toString());
                String[] strArray = str.toArray(new String[0]);
                ServerRequestsFactory.handleCommand(strArray);
                close();
            }
        });
    }

    void update(ArrayList<MusicBand> bands){
        showDefault();
        ArrayList<Long> ids = new ArrayList<>();
        for (MusicBand band : bands){
            if (band.getOwner().equals(App.getUserName())){
                ids.add(band.getId());
            }
        }
        Collections.sort(ids);
        ObservableList<Long> observableList = FXCollections.observableArrayList(ids);
        idField.setItems(observableList);

        okButton.setDisable(true);
        setVisibleAllFields(false);
        setVisibleAllLabels(false);
        idField.setVisible(true);
        id.setVisible(true);
        idField.setEditable(false);
        idField.setOnAction(e->{
            if (idField.getValue()!=null){
                setVisibleAllLabels(true);
                setVisibleAllFields(true);
                okButton.setDisable(false);

                Long id1 = idField.getValue();

                MusicBand band1 = bands.stream().filter(bandy->bandy.getId()==id1.intValue()).
                        collect(Collectors.toCollection(ArrayList::new)).get(0);

                groupNameField.setText(band1.getName());
                xField.setText(Long.toString(band1.getCoordinates().getX()));
                yField.setText(Integer.toString(band1.getCoordinates().getY()));
                participantsField.setText(band1.getNumberOfParticipants().toString());
                descriptionField.setText(band1.getDescription());
                dateField.setValue(band1.getEstablishmentDate().toLocalDate());
                genreField.setValue(band1.getGenre());
                albumNameField.setText(band1.getBestAlbum().getName());
                tracksField.setText(Integer.toString(band1.getBestAlbum().getTracks()));
                lengthField.setText(Integer.toString(band1.getBestAlbum().getLength()));
                salesField.setText(Float.toString(band1.getBestAlbum().getSales()));
            }

        });

        okButton.setOnMouseClicked(event -> {
            if (inputCheckerNew.canUpdate()){
                ArrayList<String> str = createProtoMusicBand("update");
                str.add(idField.getValue().toString());
                String[] strArray = str.toArray(new String[0]);
                ServerRequestsFactory.handleCommand(strArray);
                close();
            }
        });

    }

    void remove(ArrayList<MusicBand> bands){
        showDefault();
        clearFields();
        ArrayList<Long> ids = new ArrayList<>();
        for (MusicBand band : bands){
            if (band.getOwner().equals(App.getUserName())){
                ids.add(band.getId());
            }
        }
        Collections.sort(ids);
        ObservableList<Long> observableList = FXCollections.observableArrayList(ids);
        idField.setItems(observableList);

        setEditableAllFields(false);
        setVisibleAllFields(false);
        setVisibleAllLabels(false);
        idField.setVisible(true);
        id.setVisible(true);
        idField.setEditable(false);
        okButton.setDisable(true);

        idField.setOnAction(e->{
            if (idField.getValue()!=null){
                setVisibleAllLabels(true);
                setVisibleAllFields(true);
                okButton.setDisable(false);

                Long id1 = idField.getValue();
                MusicBand band1 = bands.stream().filter(bandy->bandy.getId()==id1.intValue()).
                        collect(Collectors.toCollection(ArrayList::new)).get(0);

                groupNameField.setText(band1.getName());
                xField.setText(Long.toString(band1.getCoordinates().getX()));
                yField.setText(Integer.toString(band1.getCoordinates().getY()));
                participantsField.setText(band1.getNumberOfParticipants().toString());
                descriptionField.setText(band1.getDescription());
                dateField.setValue(band1.getEstablishmentDate().toLocalDate());
                genreField.setValue(band1.getGenre());
                albumNameField.setText(band1.getBestAlbum().getName());
                tracksField.setText(Integer.toString(band1.getBestAlbum().getTracks()));
                lengthField.setText(Integer.toString(band1.getBestAlbum().getLength()));
                salesField.setText(Float.toString(band1.getBestAlbum().getSales()));
            }
        });

        okButton.setOnMouseClicked(event -> {
            //ArrayList<String> str = createProtoMusicBand("update_id");
            String[] str = new String[2];
            str[0] = "remove_by_id";
            str[1] = idField.getValue().toString();
            ServerRequestsFactory.handleCommand(str);
            close();
        });
    }

    void removeMany(ArrayList<MusicBand> bands){

        showDefault();
        clearFields();
        ArrayList<Long> ids = new ArrayList<>();
        for (MusicBand band : bands){
            if (band.getOwner().equals(App.getUserName())){
                ids.add(band.getId());
            }
        }
        Collections.sort(ids);
        if (ids.size()>0){
            ids.remove(ids.size()-1);
        }
        ObservableList<Long> observableList = FXCollections.observableArrayList(ids);
        idField.setItems(observableList);

        setEditableAllFields(false);
        okButton.setDisable(true);

        idField.setOnAction(e->{

            if (idField.getValue()!=null){
                okButton.setDisable(false);

                Long id1 = idField.getValue();

                MusicBand band1 = bands.stream().filter(bandy->id1.equals(bandy.getId())).
                        collect(Collectors.toCollection(ArrayList::new)).get(0);

                groupNameField.setText(band1.getName());
                xField.setText(Long.toString(band1.getCoordinates().getX()));
                yField.setText(Integer.toString(band1.getCoordinates().getY()));
                participantsField.setText(band1.getNumberOfParticipants().toString());
                descriptionField.setText(band1.getDescription());
                dateField.setValue(band1.getEstablishmentDate().toLocalDate());
                genreField.setValue(band1.getGenre());
                albumNameField.setText(band1.getBestAlbum().getName());
                tracksField.setText(Integer.toString(band1.getBestAlbum().getTracks()));
                lengthField.setText(Integer.toString(band1.getBestAlbum().getLength()));
                salesField.setText(Float.toString(band1.getBestAlbum().getSales()));
            }
        });

        okButton.setOnMouseClicked(event -> {
            //ArrayList<String> str = createProtoMusicBand("update_id");
            String[] str = new String[2];
            str[0] = "remove_greater";
            str[1] = idField.getValue().toString();
            ServerRequestsFactory.handleCommand(str);
            close();
        });
    }

    public void countGreaterThanBestAlbum(ArrayList<MusicBand> bands){
        showDefault();
        clearFields();
        ArrayList<Long> ids = new ArrayList<>();
        for (MusicBand band : bands){
            if (band.getOwner().equals(App.getUserName())){
                ids.add(band.getId());
            }
        }
        Collections.sort(ids);
        ObservableList<Long> observableList = FXCollections.observableArrayList(ids);
        idField.setItems(observableList);
        okButton.setDisable(true);
        setVisibleAllFields(false);
        setEditableAllFields(false);
        idField.setVisible(true);
        okButton.setDisable(true);

        idField.setOnAction(e->{
            if (idField.getValue()!=null){
                okButton.setDisable(false);
                albumNameField.setVisible(true);
                albumName.setVisible(true);
                Long id1 = idField.getValue();
                MusicBand band1 = bands.stream().filter(bandy->bandy.getId()==id1.intValue()).
                        collect(Collectors.toCollection(ArrayList::new)).get(0);

                albumNameField.setText(band1.getBestAlbum().getName());
            }
        });

        okButton.setOnMouseClicked(event -> {
            //ArrayList<String> str = createProtoMusicBand("update_id");
            String[] str = new String[2];
            str[0] = "count_greater_than_best_album";
            str[1] = albumNameField.getText();
            ServerRequestsFactory.handleCommand(str);
            close();
        });
    }


    private void close(){
        Stage thisWindow = (Stage)okButton.getScene().getWindow();
        thisWindow.close();
    }

    private ArrayList<String> createProtoMusicBand(String command){

        String[] str = new String[12];
        str[0] = command;

        str[1] = groupNameField.getText();
        str[2] = xField.getText();
        str[3] = yField.getText();
        str[4] = participantsField.getText();
        str[5] = descriptionField.getText();

        LocalDate d = dateField.getValue();
        str[6] = d.atStartOfDay(ZoneId.systemDefault()).toString();

        str[7] = genreField.getValue().toString().toUpperCase();
        //str[7] = MusicGenre.getEnum(genreField.getValue());
        str[8] = albumNameField.getText();
        str[9] = tracksField.getText();
        str[10] = lengthField.getText();
        str[11] = salesField.getText();

        return Arrays.stream(str).collect(Collectors.toCollection(ArrayList::new));

    }

    private void setVisibleAllFields(boolean bool){
        idField.setVisible(bool);
        groupNameField.setVisible(bool);
        xField.setVisible(bool);
        yField.setVisible(bool);
        participantsField.setVisible(bool);
        descriptionField.setVisible(bool);
        dateField.setVisible(bool);
        genreField.setVisible(bool);
        albumNameField.setVisible(bool);
        tracksField.setVisible(bool);
        lengthField.setVisible(bool);
        salesField.setVisible(bool);
    }

    private void setEditableAllFields(boolean bool){
        idField.setEditable(false);
        groupNameField.setEditable(bool);
        xField.setEditable(bool);
        yField.setEditable(bool);
        participantsField.setEditable(bool);
        descriptionField.setEditable(bool);
        dateField.setEditable(false);
        genreField.setEditable(false);
        albumNameField.setEditable(bool);
        tracksField.setEditable(bool);
        lengthField.setEditable(bool);
        salesField.setEditable(bool);
    }

    private void setVisibleAllLabels(boolean bool){
        id.setVisible(bool);
        groupName.setVisible(bool);
        x.setVisible(bool);
        y.setVisible(bool);
        participants.setVisible(bool);
        description.setVisible(bool);
        establishmentDate.setVisible(bool);
        genre.setVisible(bool);
        albumName.setVisible(bool);
        tracks.setVisible(bool);
        length.setVisible(bool);
        sales.setVisible(bool);
    }

    private void clearFields(){
        //idField.setText("");
        groupNameField.setText("");
        xField.setText("");
        yField.setText("");
        participantsField.setText("");
        descriptionField.setText("");
        dateField.setValue(LocalDate.now());
        genreField.setValue(MusicGenre.POP);
        albumNameField.setText("");
        tracksField.setText("");
        lengthField.setText("");
        salesField.setText("");
    }

    void setFieldsColour(String colour){
        idField.setStyle("-fx-background-color: " + colour);
        groupNameField.setStyle("-fx-background-color: " + colour);
        xField.setStyle("-fx-background-color: " + colour);
        yField.setStyle("-fx-background-color: " + colour);
        participantsField.setStyle("-fx-background-color: " + colour);
        descriptionField.setStyle("-fx-background-color: " + colour);
        dateField.setStyle("-fx-background-color: " + colour);
        genreField.setStyle("-fx-background-color: " + colour);
        albumNameField.setStyle("-fx-background-color: " + colour);
        tracksField.setStyle("-fx-background-color: " + colour);
        lengthField.setStyle("-fx-background-color: " + colour);
        salesField.setStyle("-fx-background-color: " + colour);
    }

    private void setLanguages(){

        ResourceBundle labels = ResourceBundle.getBundle("gui.table.tableWindow", App.getCurrentLocale(), new UTF8Control());

        groupName.setText(labels.getString("name"));
        establishmentDate.setText(labels.getString("establishment_date"));
        participants.setText(labels.getString("numberOfParticipants"));
        description.setText(labels.getString("description"));
        genre.setText(labels.getString("genre"));
        albumName.setText(labels.getString("albumName"));
        tracks.setText(labels.getString("tracks"));
        length.setText(labels.getString("length"));
        sales.setText(labels.getString("sales"));

    }

    /*private void getServerAnswer(){
        ClientRequest request = App.getTableWindowCommunicator().takeRequest();
        if (request==null){
            return;
        }
        MessageFromServerToClient messageFromServerToClient = request.getMessage();
        if (messageFromServerToClient!=null){
            String mes = "";
            switch(messageFromServerToClient){
                case PRIVATE_COLLECTION_CLEARED:
                case MANY_OBJECTS_REMOVED:
                case OBJECT_UPDATED:
                case OBJECT_REMOVED:
                case OBJECT_ADDED:
                    mes = request.getAuthor() + ": " + App.getStringFromMessage(messageFromServerToClient);
                    break;
                case INFO:
                    String[] messages = App.getStringFromMessage(messageFromServerToClient).split("\n");
                    mes = messages[0] + ": " + request.getWords().get(0) + "\n" +
                            messages[1] + ": " + request.getWords().get(1) + "\n" +
                            messages[2] + ": " + request.getWords().get(2);
                case OK:
                    if (request.getWords()!= null && request.getWords().size()>0){
                        mes = "" + request.getWords().get(0);
                    }else {
                        mes = App.getStringFromMessage(messageFromServerToClient);
                    }
                    break;
                case PRINT_LIST:
                    if (request.getWords()!= null && request.getWords().size()>0){
                        for (String word : request.getWords()){
                            mes += word + "\n";
                        }
                    }else {
                        mes = "???";
                    }
                    break;
                case NOT_AUTHORISED:
                    mes = App.getStringFromMessage(messageFromServerToClient);
                    Stage thisWindow = (Stage)okButton.getScene().getWindow();
                    thisWindow.close();
                    openWindow(thisWindow.getTitle());
                    break;
                default:
                    mes = App.getStringFromMessage(MessageFromServerToClient.ERROR);
            }

            showStringMessage(mes);
        }else{
            *//*String message = buildMessage(request);
            showStringMessage(message);*//*
        }
        if (request.getBands()!=null){
            ArrayList<MusicBand> bands1 = request.getBands();
            setBands(bands1);
        }
    }

    public void showStringMessage(String message){
        Notifications notificationBuilder = Notifications.create()
                .title("Notification")
                .text(message)
                .hideAfter(Duration.seconds(6))
                .position(Pos.TOP_RIGHT);
        notificationBuilder.darkStyle();
        notificationBuilder.show();
    }

    private void setBands(ArrayList<MusicBand> bands){

        TableController.bands = bands;
        ObservableList<MusicBand> obsList = FXCollections.observableList(bands);
        FilteredList<MusicBand> filteredList = new FilteredList<>(obsList, p->true);
        MusicBandFieldsReader musicBandFieldsReader = new MusicBandFieldsReader();
        SortedList<MusicBand> sortedList = new SortedList<>(filteredList);

        *//*table.setItems(sortedList);
        table.refresh();*//*
        //table.getItems().forEach(e->System.out.println(e.getName()));
        //table.refresh();
    }

    private void openWindow(String title){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("../authorisation/Authorisation.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setTitle(title);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.setResizable(false);
                    App.setCurrentWindow(AppWindow.AUTHORISATION);
                    stage.show();
                } catch (IOException e) { }
            }
        };
        Thread t = new Thread(r);
        t.start();
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }*/

    /*String name = groupName.getText();
        String x = Long.valueOf(xField.getText()).toString();
        String y = Integer.valueOf(yField.getText()).toString();
        String participants = Integer.valueOf(participantsField.getText()).toString();
        String description1 = descriptionField.getText();

        LocalDate d = dateField.getValue();
        String establishmentDate1 = d.atStartOfDay(ZoneId.systemDefault()).toString();
        String gen = genreField.getValue().toString();
        String aName = albumNameField.getText();
        String tracks1 = Integer.valueOf(tracksField.getText()).toString();
        String length1 = Integer.valueOf(lengthField.getText()).toString();
        String sales1 = Float.valueOf(salesField.getText()).toString();

        return new ProtoMusicBand(name, new Coordinates(x, y), participants, description1,
                establishmentDate1, gen, new Album(aName, tracks1, length1, sales1));*/

}
