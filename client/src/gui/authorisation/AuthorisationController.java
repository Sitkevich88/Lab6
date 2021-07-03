package gui.authorisation;

import data.AuthMode;
import gui.App;
import gui.AppWindow;
import gui.common.LanguagesSwitcher;
import gui.common.Message;
import gui.common.UTF8Control;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthorisationController{

    private CredentialsValidator credentialsValidator = new CredentialsValidator();

    @FXML
    private Label registrationTitle;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private ComboBox<String> languageButton;

    @FXML
    private Button test;

    private String login;
    private String password;
    private LanguagesSwitcher switcher = new LanguagesSwitcher();


    @FXML
    void initialize(){
        loginField.setStyle("-fx-text-inner-color: #000000;");
        passwordField.setStyle("-fx-text-inner-color: #000000;");
        loadLanguages(languageButton);
        setLanguage(switcher.getLocale(languageButton));
    }

    @FXML
    void fastSignIn(MouseEvent event) {
        login = "teacher";
        password = "teacher";
        CredentialsWithMode credentialsWithMode = new CredentialsWithMode(login, password, AuthMode.SIGN_IN);

        App.getClient().authorise(credentialsWithMode);
        App.setUserName(login);
        
    }

    public void receiveMessage(Message message){
        if (message==null){
            return;
        }
        showMessage(message);
        if (message.equals(Message.OK)){
            App.setCurrentWindow(AppWindow.MAIN_TABLE);
        } else {
            loginField.setStyle("-fx-background-color: #ffffff");
            passwordField.setStyle("-fx-background-color: #ffffff");
        }
    }

    @FXML
    void signIn(MouseEvent event) {
        loggingRequest(AuthMode.SIGN_IN);
    }

    @FXML
    void signUp(MouseEvent event) {
        loggingRequest(AuthMode.SIGN_UP);
    }

    private void loggingRequest(AuthMode mode){
        login = loginField.getText();
        password = passwordField.getText();
        boolean goodLogin = credentialsValidator.validateLogin(login);
        boolean goodPassword = credentialsValidator.validatePassword(password);
        if (goodLogin && goodPassword){
            loginField.setStyle(("-fx-background-color: #FFFFFF"));
            passwordField.setStyle(("-fx-background-color: #FFFFFF"));
            CredentialsWithMode credentialsWithMode = new CredentialsWithMode(login, password, mode);
            App.getClient().authorise(credentialsWithMode);
            App.setUserName(login);
            /*Message message2 = App.getAuthorisationWindowCommunicator().takeMessage();
            if (message2==null){
                return;
            }
            if (message2.equals(Message.OK)){
                Stage thisWindow = (Stage)signInButton.getScene().getWindow();
                thisWindow.close();
                openWindow(thisWindow.getTitle());
            } else {
                loginField.setStyle(("-fx-background-color: #ffffff"));
                passwordField.setStyle(("-fx-background-color: #ffffff"));
            }
            showMessage(message2);*/
        }else {
            if (goodLogin){
                loginField.setStyle("-fx-background-color: #ffffff");
            }else {
                loginField.setStyle("-fx-background-color: #ffCCCC");
            }
            if (goodPassword) {
                passwordField.setStyle("-fx-background-color: #ffffff");
            } else {
                passwordField.setStyle("-fx-background-color: #ffCCCC");
            }
            showMessage(Message.BAD_CREDENTIALS);
        }
    }

    public void showMessage(Message message){
        String messageInString = App.getStringFromMessage(message);
        showMessage(messageInString, 4);
    }

    public void showMessage(String message, int seconds){
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


    private void loadLanguages(ComboBox<String> button){
        switcher.loadLanguages(button, App.getCurrentLocale());
    }

    @FXML
    void switchLanguage(ActionEvent event){
        setLanguage(switcher.getLocale(languageButton));
    }



    private void setLanguage(Locale chosenLanguage){

        App.setLocale(chosenLanguage);
        ResourceBundle labels = ResourceBundle.getBundle("gui.authorisation.RegistrationWindow", chosenLanguage, new UTF8Control());

        registrationTitle.setText(switcher.getLocaleString(labels, "registrationTitle"));
        loginField.setPromptText(switcher.getLocaleString(labels, "loginPrompt"));
        passwordField.setPromptText(switcher.getLocaleString(labels, "passwordPrompt"));
        signInButton.setText(switcher.getLocaleString(labels, "signInLabel"));
        signUpButton.setText(switcher.getLocaleString(labels, "signUpLabel"));
    }

}
