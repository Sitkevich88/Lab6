package commands.withMaxOneArgument;

import data.MusicBand;
import utils.CollectionAnalyzer;
import utils.MessagesForClient;
import java.util.Collection;

/**
 * Command 'info'. Prints a short piece of information about the collection
 */

public class Info {

    /**
     * Executes the command.
     * @param collection - collection to examine
     */

    public void invoke(Collection<MusicBand> collection){
        try{
            MessagesForClient.recordMessage( "Тип коллекции: " + collection.getClass().getSimpleName() +
                    "\nРазмер коллекции: " + collection.size() +
                    "\nДата инициализации: " + CollectionAnalyzer.InitializationTracker.getLastInit());
            /*System.out.println( "Тип коллекции: " + collection.getClass().getSimpleName() +
                    "\nРазмер коллекции: " + collection.size() +
                    "\nДата инициализации: " + CollectionAnalyzer.InitializationTracker.getLastInit());*/
        }catch (NullPointerException e){
            MessagesForClient.recordMessage("Тип коллекции: неопределен" +
                    "\nРазмер коллекции: 0" +
                    "\nДата инициализации: " + CollectionAnalyzer.InitializationTracker.getLastInit());
            /*System.out.println("Тип коллекции: неопределен" +
                    "\nРазмер коллекции: 0" +
                    "\nДата инициализации: " + CollectionAnalyzer.InitializationTracker.getLastInit());*/
        }
    }
}
