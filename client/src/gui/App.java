package gui;


import data.*;
import gui.authorisation.AuthorisationController;
import gui.common.Message;
import gui.common.UTF8Control;
import gui.table.TableController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.ClientNew;
import utils.ServerRequestsFactory;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {

    private static AppWindow currentWindow = AppWindow.AUTHORISATION;
    private static int numberOfApplications = 0;
    private static final String applicationName = "Music Bands";
    private static Stage currentStage;
    private static Scene authWindow;
    private static Scene tableWindow;
    private static AuthorisationController authorisationController;
    private static TableController tableController;
    private static String userName = "";
    private static Locale currentLocale = new Locale("ru");
    private static ArrayList<MusicBand> bands;
    private static ClientNew client;


    public static AppWindow getCurrentWindow() {
        return currentWindow;
    }

    public void launch(){

        if (numberOfApplications!=0){
            return;
        }
        client = new ClientNew("localhost", 19117);
        Runnable r = () -> {
            Application.launch();
        };
        new Thread(r).start();
        client.runReceiver();

        numberOfApplications++;
    }

    public static void receiveAuthorisationServerAnswer(Message authorisationServerAnswer){
        App.setCurrentWindow(AppWindow.AUTHORISATION);
        //Message mes = App.convert(authorisationServerAnswer.gerAuthorisationResult());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                authorisationController.receiveMessage(authorisationServerAnswer);
            }
        });

    }

    public static void receiveClientRequest(ClientRequest clientRequest){
        App.setCurrentWindow(AppWindow.MAIN_TABLE);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableController.receiveClientRequest(clientRequest);
            }
        });

    }
    
    @Override
    public void start(Stage stage) throws Exception {
        currentStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("authorisation/Authorisation.fxml"));
        Parent authRoot = loader.load();
        authWindow = new Scene(authRoot);
        authWindow.setUserData(loader);
        authorisationController = loader.getController();


        stage.setTitle(applicationName);
        stage.setScene(authWindow);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.getScene();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                client.close();
                Platform.exit();
                //todo
                /*tableController.closeForm();
                stage.close();*/
            }
        });
        currentWindow = AppWindow.AUTHORISATION;
        stage.show();
    }


    public static String getStringFromMessage(Message m){
        ResourceBundle labels = ResourceBundle.getBundle("gui.common.Messages", currentLocale, new UTF8Control());
        String message;
        switch (m){
            case BAD_CREDENTIALS:
                message = labels.getString("incorrectCredentials");
                break;
            case SIGN_IN_ERROR:
                message = labels.getString("signInError");
                break;
            case SIGN_UP_ERROR:
                message = labels.getString("signUpError");
                break;
            case IO_ERROR:
                message = labels.getString("IOError");
                break;
            case BAD_ANSWER:
                message = labels.getString("badAnswer");
                break;
            case SERVER_IS_SILENT:
                message = labels.getString("serverIsSilent");
                break;
            default:
                message = labels.getString("ok");
        }
        return message;
    }

    public static String getStringFromMessage(MessageFromServerToClient m){
        ResourceBundle labels = ResourceBundle.getBundle("gui.common.Messages", currentLocale, new UTF8Control());
        String message;
        switch (m){
            case NOT_AUTHORISED:
                message = labels.getString("notAuthorised");
                break;
            case OBJECT_ADDED:
                message = labels.getString("objectAdded");
                break;
            case ERROR:
                message = labels.getString("error");
                break;
            case OBJECT_REMOVED:
                message = labels.getString("objectRemoved");
                break;
            case OBJECT_UPDATED:
                message = labels.getString("objectUpdated");
                break;
            case MANY_OBJECTS_REMOVED:
                message = labels.getString("manyObjectsRemoved");
                break;
            case PRIVATE_COLLECTION_CLEARED:
                message = labels.getString("private_collection_cleared");
                break;
            case INFO:
                message = labels.getString("info1") + "\n";
                message+= labels.getString("info2") + "\n";
                message+= labels.getString("info3");
                break;

            default:
                message = labels.getString("commandExecuted");
        }
        return message;
    }

    public static Message convert(AuthorisationResult res){
        return Message.valueOf(res.toString().toUpperCase());
    }

    public static String getStringFromClientRequest(ClientRequest request){

        if (request==null || request.getMessage()==null){
            return null;
        }
        MessageFromServerToClient messageFromServerToClient = request.getMessage();

        String mes = "";
        switch (messageFromServerToClient) {
            case PRIVATE_COLLECTION_CLEARED:
            case OBJECT_UPDATED:
            case OBJECT_REMOVED:
            case OBJECT_ADDED:
                mes = request.getAuthor() + ": " + App.getStringFromMessage(messageFromServerToClient);
                break;
            case MANY_OBJECTS_REMOVED:
                mes = request.getAuthor() + ": (" + request.getWords().get(0) + ") " + App.getStringFromMessage(messageFromServerToClient);
                break;
            case INFO:
                String[] messages = App.getStringFromMessage(messageFromServerToClient).split("\\n");
                mes = messages[0] + ": " + request.getWords().get(0) + "\n";
                mes += messages[1] + ": " + request.getWords().get(1) + "\n";
                mes += messages[2] + ": " + request.getWords().get(2);
                break;
            case OK:
                if (request.getWords() != null && request.getWords().size() > 0) {
                    mes = "" + request.getWords().get(0);
                } else {
                    mes = App.getStringFromMessage(messageFromServerToClient);
                }
                break;
            case PRINT_LIST:
                if (request.getWords() != null && request.getWords().size() > 0) {
                    mes = "";
                    for (String word : request.getWords()) {
                        mes += word + "\n";
                    }
                } else {
                    mes = "???";
                }
                break;
            case SERVER_IS_SILENT:
                mes = App.getStringFromMessage(Message.SERVER_IS_SILENT);
                break;
            case NOT_AUTHORISED:
                mes = App.getStringFromMessage(messageFromServerToClient);
                /*Stage thisWindow = (Stage) exitButton.getScene().getWindow();
                thisWindow.close();
                openWindow(thisWindow.getTitle());*/
                break;
            default:
                mes = App.getStringFromMessage(MessageFromServerToClient.ERROR);
        }

        return mes;
    }




    public static void setUserName(String userName){
        App.userName = userName;
    }

    public static String getUserName(){
        return App.userName;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setLocale(Language l){
        switch (l){
            case ENGLISH:
                currentLocale = new Locale("en", "CA");
                break;
            case ROMANIAN:
                currentLocale = new Locale("ro");
                break;
            case LITHUANIAN:
                currentLocale = new Locale("lt");
                break;
            default:
                currentLocale = new Locale("ru");
        }
    }

    public static void setLocale(Locale l){
        currentLocale = l;
    }

    public static ArrayList<MusicBand> getBands() {
        return bands;
    }

    public static void setBands(ArrayList<MusicBand> bands) {
        App.bands = bands;
    }

    private static boolean loadedTableControler = false;

    public static void setCurrentWindow(AppWindow currentWindow) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (App.currentWindow.equals(currentWindow)){
                    return;
                }
                App.currentWindow = currentWindow;
                if (currentWindow.equals(AppWindow.MAIN_TABLE)) {
                    if (!loadedTableControler){
                        try {
                            FXMLLoader loader1 = new FXMLLoader(App.class.getResource("table/Table.fxml"));
                            Parent tableRoot = loader1.load();
                            tableWindow = new Scene(tableRoot);
                            tableWindow.setUserData(loader1);
                            tableController = loader1.getController();
                            loadedTableControler = true;
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    } else {
                        tableController.setBands(App.getBands());
                        tableController.setUserName();
                    }
                    currentStage.setResizable(true);
                    currentStage.setScene(tableWindow);
                    currentStage.centerOnScreen();
                    currentStage.setFullScreen(true);
                } else if (currentWindow.equals(AppWindow.AUTHORISATION)) {
                    tableController.setBands(new ArrayList<>());
                    currentStage.setFullScreen(false);
                    currentStage.setResizable(false);
                    currentStage.setScene(authWindow);
                    setBands(new ArrayList<>());
                    currentStage.centerOnScreen();
                }
            }
        });
    }

    public static ClientNew getClient() {
        return client;
    }

    public static void setClient(ClientNew client) {
        App.client = client;
    }
}
