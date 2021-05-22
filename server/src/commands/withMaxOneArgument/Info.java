package commands.withMaxOneArgument;

import commands.AbstractCommandWhichRequiresCollection;
import data.MusicBand;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command 'info'. Prints a short piece of information about the collection
 */

public class Info extends AbstractCommandWhichRequiresCollection {

    public Info(LinkedBlockingQueue<MusicBand> collection, MessagesForClient messages) {
        super(collection, messages);
    }

    public void invoke(){
        try{
            getMessages().recordMessage( "Тип коллекции: " + getCollection().getClass().getSimpleName() +
                    "\nРазмер коллекции: " + getCollection().size() +
                    "\nДата инициализации: " + getLastInit());

        }catch (NullPointerException e){
            getMessages().recordMessage("Тип коллекции: неопределен" +
                    "\nРазмер коллекции: 0" +
                    "\nДата инициализации: undefined");
        }
    }
    private String getLastInit(){
        String lastInit = "undefined";
        try {
            //Statement statement = DataBaseConnector.getConnection().prepareStatement("SELECT creation_date FROM music_bands WHERE owner = ? ORDER BY creation_date  desc LIMIT 1;");
            Statement statement = DataBaseConnector.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT creation_date FROM music_bands ORDER BY creation_date  desc LIMIT 1;");
            rs.next();
            lastInit = rs.getTimestamp(1).toLocalDateTime().toString();

        } catch (SQLException throwables) {
            getMessages().recordMessage(throwables.getMessage());
        }
        return lastInit;
    }
}
