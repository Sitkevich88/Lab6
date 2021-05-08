package commands.withMaxOneArgument;

import data.MusicBand;
import utils.MessagesForClient;
import utils.sql.DataBaseConnector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Command 'info'. Prints a short piece of information about the collection
 */

public class Info {

    /**
     * Executes the command.
     * @param collection - collection to examine
     */

    public void invoke(String sender, Collection<MusicBand> collection){
        try{
            MessagesForClient.recordMessage( "Тип коллекции: " + collection.getClass().getSimpleName() +
                    "\nРазмер коллекции: " + collection.size() +
                    "\nДата инициализации: " + getLastInit(sender));

        }catch (NullPointerException e){
            MessagesForClient.recordMessage("Тип коллекции: неопределен" +
                    "\nРазмер коллекции: 0" +
                    "\nДата инициализации: undefined");
        }
    }
    private String getLastInit(String sender){
        String lastInit = "undefined";
        try {
            PreparedStatement prst = DataBaseConnector.getConnection().prepareStatement("SELECT creation_date FROM music_bands WHERE owner = ? ORDER BY creation_date  desc LIMIT 1;");
            prst.setString(1, sender);
            ResultSet rs = prst.executeQuery();
            rs.next();
            lastInit = rs.getTimestamp(1).toLocalDateTime().toString();

        } catch (SQLException throwables) {
            MessagesForClient.recordMessage(throwables.getMessage());
        }
        return lastInit;
    }
}
