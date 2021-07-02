package utils.sql;

import data.AuthorisationResult;
import data.UserData;
import utils.LogFactory;
import utils.MessagesForClient;
import utils.OnlineUsers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthorisation{

    private final Connection connection;
    private String username;
    private byte[] password;
    private AuthorisationResult res = AuthorisationResult.OK;

    public UserAuthorisation(Connection connection){
        this.connection = connection;
    }

    public AuthorisationResult authorise(UserData userData, int userPort){
        //if (userData==null){return false;}
        this.username = userData.getLogin();
        this.password = userData.getPassword();
        boolean commandIsAccomplishedSuccessfully;
        switch (userData.getMode()){
            case SIGN_UP:
                commandIsAccomplishedSuccessfully = createUser();
                break;
            case SIGN_IN:
                commandIsAccomplishedSuccessfully = connectToUser();
                break;
            default:
                commandIsAccomplishedSuccessfully = false;
        }
        //System.out.println("Удалось авторизтроваться - " + commandIsAccomplishedSuccessfully);
        LogFactory logFactory = new LogFactory();
        logFactory.getLogger(this).info("user " + userPort + " got access to " + username + " account - " + commandIsAccomplishedSuccessfully);
        if (commandIsAccomplishedSuccessfully){
            OnlineUsers.addAccount(userData);
        }
        return res;
    }

    private boolean connectToUser() {

        try {
            PreparedStatement prst = connection.prepareStatement("SELECT COUNT(*) FROM auth WHERE login = ? AND password =? LIMIT 1;");
            prst.setString(1, username);
            prst.setBytes(2,password);
            ResultSet rs = prst.executeQuery();
            rs.next();
            if (rs.getInt(1)==1){
                //messages.recordMessage("You have successfully logged in");
                res = AuthorisationResult.OK;
                return true;
            }else {
                res = AuthorisationResult.SIGN_IN_ERROR;
                //messages.recordMessage("Incorrect login or password");
                return false;
            }
        } catch (SQLException throwables) {

            //messages.recordMessage(throwables.getMessage());
            return false;
        }
    }

    private boolean createUser(){

        try {
            PreparedStatement prst = connection.prepareStatement("INSERT INTO auth(login, password) VALUES (?,?);");
            prst.setString(1, username);
            prst.setBytes(2,password);
            prst.executeUpdate();
            res = AuthorisationResult.OK;
            //messages.recordMessage("Successful registration. You have logged in");
            return true;
        } catch (SQLException throwables) {

            if (throwables.getLocalizedMessage().contains("duplicate key value")){
                //messages.recordMessage("This login already exists.");
                res = AuthorisationResult.SIGN_UP_ERROR;
            }else {
                //messages.recordMessage(throwables.getMessage());
            }

            return false;
        }
    }

}
