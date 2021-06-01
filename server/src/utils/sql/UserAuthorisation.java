package utils.sql;

import data.UserData;
import utils.LogFactory;
import utils.MessagesForClient;
import utils.OnlineUsers;

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

    public MessagesForClient authorise(UserData userData, MessagesForClient messages, int userPort){
        //if (userData==null){return false;}
        this.username = userData.getLogin();
        this.password = userData.getPassword();
        boolean commandIsAccomplishedSuccessfully;
        switch (userData.getMode()){
            case CREATE:
                commandIsAccomplishedSuccessfully = createUser(messages);
                break;
            case LOG_IN:
                commandIsAccomplishedSuccessfully = connectToUser(messages);
                break;
            default:
                commandIsAccomplishedSuccessfully = false;
        }
        //System.out.println("Удалось авторизтроваться - " + commandIsAccomplishedSuccessfully);
        LogFactory logFactory = new LogFactory();
        logFactory.getLogger(this).info("user " + userPort + " got access to " + username + " account - " + commandIsAccomplishedSuccessfully);
        if (commandIsAccomplishedSuccessfully){
            OnlineUsers.addUser(userData);
        }
        return messages;
    }

    private boolean connectToUser(MessagesForClient messages) {

        try {
            PreparedStatement prst = connection.prepareStatement("SELECT COUNT(*) FROM auth WHERE login = ? AND password =? LIMIT 1;");
            prst.setString(1, username);
            prst.setBytes(2,password);
            ResultSet rs = prst.executeQuery();
            rs.next();
            if (rs.getInt(1)==1){
                messages.recordMessage("You have successfully logged in");
                return true;
            }else {
                messages.recordMessage("Incorrect login or password");
                return false;
            }
        } catch (SQLException throwables) {
            messages.recordMessage(throwables.getMessage());
            return false;
        }
    }

    private boolean createUser(MessagesForClient messages){

        try {
            PreparedStatement prst = connection.prepareStatement("INSERT INTO auth(login, password) VALUES (?,?);");
            prst.setString(1, username);
            prst.setBytes(2,password);
            prst.executeUpdate();
            messages.recordMessage("Successful registration. You have logged in");
            return true;
        } catch (SQLException throwables) {

            if (throwables.getLocalizedMessage().contains("duplicate key value")){
                messages.recordMessage("This login already exists.");
            }else {
                messages.recordMessage(throwables.getMessage());
            }

            return false;
        }
    }

}
