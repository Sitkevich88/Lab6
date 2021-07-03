package gui.table;

import data.*;
import gui.App;
import gui.AppWindow;
import gui.common.LanguagesSwitcher;
import gui.common.UTF8Control;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import utils.ServerRequestsFactory;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.*;

public class TableController {

    public static ArrayList<MusicBand> lastBands = new ArrayList<>();
    public static ArrayList<MusicBand> bands = new ArrayList<>();
    private SortedList<MusicBand> list = null;

    private FormController formController = null;
    private Stage formStage = null;

    @FXML
    private Label userName;

    @FXML
    private Label message;

    @FXML
    private CustomTextField searcher;

    @FXML
    private TableView<MusicBand> table;

    @FXML
    private TableColumn<MusicBand, Long> id;

    @FXML
    private TableColumn<MusicBand, String> owner;

    @FXML
    private TableColumn<MusicBand, String> name;

    @FXML
    private TableColumn<?, ?> coordinates;

    @FXML
    private TableColumn<MusicBand, Long> x;

    @FXML
    private TableColumn<MusicBand, Integer> y;

    @FXML
    private TableColumn<MusicBand, ZonedDateTime> establishmentDate;

    @FXML
    private TableColumn<MusicBand, Integer> numberOfParticipants;

    @FXML
    private TableColumn<MusicBand, String> description;

    @FXML
    private TableColumn<MusicBand, Date> creationDate;

    @FXML
    private TableColumn<MusicBand, MusicGenre> genre;

    @FXML
    private TableColumn<?, ?> album;

    @FXML
    private TableColumn<MusicBand, String> albumName;

    @FXML
    private TableColumn<MusicBand, Integer> tracks;

    @FXML
    private TableColumn<MusicBand, Integer> length;

    @FXML
    private TableColumn<MusicBand, String> sales;

    @FXML
    private Label graphic;

    @FXML
    private AnchorPane objectsShower;

    @FXML
    private Button infoButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button sumOfParticipantsButton;

    @FXML
    private Button removeGreaterButton;

    @FXML
    private Button fieldAscendingDescriptionButton;

    @FXML
    private Button greaterThanBestAlbumButton;

    @FXML
    private Button scriptButton;

    @FXML
    private ComboBox<String> languageButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button removeButton;

    @FXML
    void add(MouseEvent event) {
        formStage.show();
        formController.add();
    }

    private void checkStage(){
        //todo
    }

    private void loadFormController() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            formStage = new Stage();
            formStage.setUserData(fxmlLoader);
            formStage.setTitle("Form");
            formStage.setResizable(false);
            formStage.initModality(Modality.WINDOW_MODAL);
            formStage.setScene(scene);
            formController = fxmlLoader.getController();
            //controllerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void clear(MouseEvent event) {
        sendSimpleCommand("clear");
    }

    @FXML
    void countGreaterAlbums(MouseEvent event) {
        formStage.show();
        formController.countGreaterThanBestAlbum(bands);
    }

    @FXML
    void countParticipants(MouseEvent event) {
        sendSimpleCommand("sum_of_number_of_participants");
    }

    @FXML
    void executeScript(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.*"));
        Stage mainStage = (Stage)exitButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile == null) {
            return;
        }
        ServerRequestsFactory.handleCommand(new String[]{"execute_script", selectedFile.getAbsolutePath()});
    }

    @FXML
    void goBack(MouseEvent event) {
        App.setCurrentWindow(AppWindow.AUTHORISATION);
        formStage.close();
        sendSimpleCommand("exit");
    }

    @FXML
    void help(MouseEvent event) {
        //String text = "";
        //showMessage(text, 10);
    }

    @FXML
    void idSort(ActionEvent event) {
        System.out.println(event.getSource());
        //table.sort();
    }

    @FXML
    void info(MouseEvent event) {
        sendSimpleCommand("info");
    }

    @FXML
    void printDescriptions(MouseEvent event) {
        sendSimpleCommand("print_field_ascending_description");
    }

    @FXML
    void remove(MouseEvent event) {
        formStage.show();
        formController.remove(bands);
    }

    @FXML
    void removeGreater(MouseEvent event) {
        formStage.show();
        formController.removeMany(bands);
    }

    @FXML
    void switchLanguage(ActionEvent event) {
        setLanguage(switcher.getLocale(languageButton));
    }

    @FXML
    void update(MouseEvent event) {
        formStage.show();
        formController.update(bands);
    }

    private LanguagesSwitcher switcher = new LanguagesSwitcher();

    @FXML
    void initialize(){

        setUserName();
        searcher.setRight(Glyph.create("FontAwesome|" + FontAwesome.Glyph.SEARCH.name()));
        setLanguage(App.getCurrentLocale());
        switcher.loadLanguages(languageButton, App.getCurrentLocale());
        setSomethingForTable();
        setBands(App.getBands());
        fieldAscendingDescriptionButton.setOnMouseClicked(event -> sendSimpleCommand("print_field_ascending_description"));
        loadFormController();
    }

    public void closeForm(){
        formStage.close();
    }

    private void animateChanges(){
        ArrayList<MusicBand> previous = (ArrayList<MusicBand>) lastBands.clone();
        ArrayList<MusicBand> current = (ArrayList<MusicBand>) bands.clone();
        ArrayList<MusicBand> toShow = (ArrayList<MusicBand>) current.clone();
        ArrayList<MusicBand> toDelete = (ArrayList<MusicBand>) previous.clone();
        toShow.removeAll(previous);
        toDelete.removeAll(current);
        //todo
        //deleteBalls(toDelete);
        showBalls(toShow);
    }

    private void showBalls(ArrayList<MusicBand> bands){
        /*for (MusicBand band : bands){
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    double x0 = objectsShower.getScaleX();
                    double y0 = objectsShower.getScaleY();
                    PathTransition pathTransition = new PathTransition();
                    Circle circle = new Circle(x0, y0, 20);
                    //Group root = new Group(circle);
                    circle.setFill(ColourGetter.getColour(band.getOwner()));
                    pathTransition.setNode(circle);
                    pathTransition.setDuration(Duration.seconds(3));
                    Line line = new Line();
                    line.setStartX(x0);
                    line.setStartY(y0);
                    double deltX = band.getCoordinates().getX();
                    double deltY = band.getCoordinates().getY();
                    line.setEndX(x0 + deltX);
                    line.setEndY(y0 + deltY);
                    pathTransition.setPath(line);
                    pathTransition.setCycleCount(1);
                    pathTransition.play();
                }
            };
            new Thread(r).start();
        }*/

        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Rectangle rect1 = new Rectangle(10, 10, 10, 10);
                rect1.setArcHeight(20);
                rect1.setArcWidth(20);
                rect1.setFill(Color.RED);
                objectsShower.getChildren().add(rect1);
                FadeTransition ft = new FadeTransition(Duration.millis(3000), rect1);
                ft.setFromValue(1.0);
                ft.setToValue(0.1);
                ft.setCycleCount(Timeline.INDEFINITE);
                ft.setAutoReverse(true);
                ft.play();
            }
        });*/


    }

    public void setBands(ArrayList<MusicBand> bands){
        //NavigableSet<MusicBand> list = ;
        objectsShower.setVisible(false);
        lastBands = TableController.bands;
        ColourGetter.updateColours(bands);
        animateChanges();

        TableController.bands = bands;
        ObservableList<MusicBand> obsList = FXCollections.observableList(bands);
        FilteredList<MusicBand> filteredList = new FilteredList<>(obsList, p->true);
        MusicBandFieldsReader musicBandFieldsReader = new MusicBandFieldsReader();
        searcher.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(musicBand -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                return musicBandFieldsReader.findString(musicBand, lowerCaseFilter);
            });
        });
        SortedList<MusicBand> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedList);
        table.refresh();
        //table.getItems().forEach(e->System.out.println(e.getName()));
        //table.refresh();
    }


    private void setSomethingForTable(){
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        //owner.setCellFactory(listView -> createListCell());
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        /*x.setCellValueFactory(new PropertyValueFactory<>("x"));
        y.setCellValueFactory(new PropertyValueFactory<>("y"));*/
        establishmentDate.setCellValueFactory(new PropertyValueFactory<>("establishmentDate"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        numberOfParticipants.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        /*albumName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tracks.setCellValueFactory(new PropertyValueFactory<>("tracks"));
        length.setCellValueFactory(new PropertyValueFactory<>("length"));
        sales.setCellValueFactory(new PropertyValueFactory<>("sales"));*/
        //table.setItems(list);

        //id.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getId()));
        /*owner.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getOwner()));
        name.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getName()));*/
        x.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getCoordinates().getX()));
        y.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getCoordinates().getY()));
        /*establishmentDate.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getEstablishmentDate()));
        numberOfParticipants.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getNumberOfParticipants()));
        description.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getDescription()));
        creationDate.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getCreationDate()));
        genre.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getGenre()));*/
        albumName.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getBestAlbum().getName()));
        tracks.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getBestAlbum().getTracks()));
        length.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(celldata.getValue().getBestAlbum().getLength()));
        sales.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(formatNumber(celldata.getValue().getBestAlbum().getSales())));
        sales.setComparator((o1, o2) -> Float.compare
                (Float.valueOf( Float.valueOf(o1.replace(',','.'))),
                        Float.valueOf(o2.replace(',','.'))));

        //genre.setCellFactory(ComboBoxTableCell.<MusicBand, MusicGenre>forTableColumn(MusicGenre.values()));

    }

    private void formatNumbers(){
        sales.setCellValueFactory(celldata->new ReadOnlyObjectWrapper<>(formatNumber(celldata.getValue().getBestAlbum().getSales())));
    }

    public void receiveClientRequest(ClientRequest clientRequest){
        if (clientRequest==null){
            return;
        }
        showMessage(clientRequest);
        if (clientRequest.getBands()!=null){
            setBands(clientRequest.getBands());
        }
        if (clientRequest.getMessage().equals(MessageFromServerToClient.NOT_AUTHORISED)){
            App.setCurrentWindow(AppWindow.AUTHORISATION);
        }
    }


    public void showMessage(String message, int seconds) {
        if (message==null){
            return;
        }
        Notifications notificationBuilder = Notifications.create()
                .title("Notification")
                .text(message)
                .hideAfter(Duration.seconds(seconds))
                .position(Pos.TOP_RIGHT);
        notificationBuilder.darkStyle();
        notificationBuilder.show();
    }


    public void showMessage(ClientRequest clientRequest) {
        String messageInString = App.getStringFromClientRequest(clientRequest);
        showMessage(messageInString, 6);
    }


    private String formatNumber(Number n){
        Locale locale = App.getCurrentLocale();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        return numberFormat.format(n);
    }

    private void sendSimpleCommand(String command){
        ServerRequestsFactory.handleCommand(new String[]{command});
    }

    public void setUserName(){
        userName.setText(App.getUserName());
    }

    private void setLanguage(Locale chosenLanguage){
        App.setLocale(chosenLanguage);
        ResourceBundle labels = ResourceBundle.getBundle("gui.table.tableWindow", chosenLanguage, new UTF8Control());

        name.setText(labels.getString("name"));
        owner.setText(labels.getString("owner"));
        coordinates.setText(labels.getString("coordinates"));
        establishmentDate.setText(labels.getString("establishment_date"));
        numberOfParticipants.setText(labels.getString("numberOfParticipants"));
        description.setText(labels.getString("description"));
        creationDate.setText(labels.getString("creationDate"));
        genre.setText(labels.getString("genre"));
        album.setText(labels.getString("best_album"));
        albumName.setText(labels.getString("albumName"));
        tracks.setText(labels.getString("tracks"));
        length.setText(labels.getString("length"));
        sales.setText(labels.getString("sales"));

        helpButton.setText(labels.getString("help"));
        infoButton.setText(labels.getString("info"));
        addButton.setText(labels.getString("add"));
        updateButton.setText(labels.getString("update"));
        removeButton.setText(labels.getString("remove_by_id"));
        clearButton.setText(labels.getString("clear"));
        scriptButton.setText(labels.getString("execute_script"));
        exitButton.setText(labels.getString("exit"));
        removeGreaterButton.setText(labels.getString("remove_greater"));
        sumOfParticipantsButton.setText(labels.getString("sum_of_number_of_participants"));
        greaterThanBestAlbumButton.setText(labels.getString("count_greater_than_best_album"));
        fieldAscendingDescriptionButton.setText(labels.getString("field_ascending_description"));
        graphic.setText(labels.getString("display"));

        formatNumbers();
        table.refresh();
    }


    /*
    /*private String getMessage(String key){
        Locale locale = switcher.getLocale(languageButton);
        ResourceBundle labels = ResourceBundle.getBundle("gui.Messages", locale, new UTF8Control());
        return switcher.getLocaleString(labels, key);
    }

    private void close(){
        Runnable r = () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Stage thisWindow = (Stage)addButton.getScene().getWindow();
            thisWindow.setOnCloseRequest(event -> {
                sendSimpleCommand("exit");
                thisWindow.close();
                //todo send exit button
                ClientNew.setRunning(false);
                App.getTableWindowCommunicator().takeRequest();
                //System.exit(0);
            });
        };
        new Thread(r).start();

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

}
