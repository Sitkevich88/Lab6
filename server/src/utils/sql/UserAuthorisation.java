package utils.sql;

import data.UserData;
import utils.MessagesForClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthorisation{

    private final Connection connection;;
    private String username;
    private byte[] password;

    public UserAuthorisation(Connection connection){
        this.connection = connection;
    }

    public boolean authorise(UserData userData){
        if (userData==null){return false;}
        this.username = userData.getLogin();
        this.password = userData.getPassword();
        boolean commandIsAccomplishedSuccessfully;
        switch (userData.getMode()){
            case CREATE:
                commandIsAccomplishedSuccessfully = createUser();
                break;
            case LOG_IN:
                commandIsAccomplishedSuccessfully = connectToUser();
                break;
            default:
                commandIsAccomplishedSuccessfully = false;
        }
        System.out.println("Удалось авторизтроваться - " + commandIsAccomplishedSuccessfully);
        return commandIsAccomplishedSuccessfully;
    }

    private boolean connectToUser() {

        try {
            PreparedStatement prst = connection.prepareStatement("SELECT COUNT(*) FROM auth WHERE login = ? AND password =? LIMIT 1;");
            prst.setString(1, username);
            prst.setBytes(2,password);
            ResultSet rs = prst.executeQuery();
            rs.next();
            if (rs.getInt(1)==1){
                MessagesForClient.recordMessage("You have successfully logged in");
                return true;
            }else {
                MessagesForClient.recordMessage("Incorrect login or password");
                return false;
            }
        } catch (SQLException throwables) {
            MessagesForClient.recordMessage(throwables.getMessage());
            return false;
        }
    }

    private boolean createUser(){

        try {
            PreparedStatement prst = connection.prepareStatement("INSERT INTO auth(login, password) VALUES (?,?);");
            prst.setString(1, username);
            prst.setBytes(2,password);
            prst.executeUpdate();
            MessagesForClient.recordMessage("Successful registration. You have logged in");
            return true;
        } catch (SQLException throwables) {

            if (throwables.getLocalizedMessage().contains("duplicate key value")){
                MessagesForClient.recordMessage("This login already exists.");
            }else {
                MessagesForClient.recordMessage(throwables.getMessage());
            }

            return false;
        }
    }

}
