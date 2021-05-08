package utils;

import commands.withMaxOneArgument.*;
import commands.withTwoArguments.*;
import data.MusicBand;
import data.ServerRequest;
import org.slf4j.Logger;
import utils.sql.DataBaseConnector;
import java.sql.Connection;
import java.util.Stack;

public class RequestsHandler {

    private static Logger logger;
    private Stack<MusicBand> collection;
    private Connection connection;

    public RequestsHandler(Stack<MusicBand> collection) {
        this.collection = collection;
        connection = DataBaseConnector.getConnection();
        logger = new LogFactory().getLogger(this);
    }

    public boolean handle(ServerRequest request){
        String[] str;
        do{
            try {
                str = CommandsParser.parseArguments(request);
                String sender = request.getSender();
                logger.warn("Server is handling \"" + str[0] + "\" command");
                Sort sorter = new Sort();
                sorter.invoke(collection);
                switch (str[0]) {
                    case ("help"):
                        new Help();
                        break;
                    case ("info"):
                        Info info = new Info();
                        info.invoke(sender, collection);
                        break;
                    case ("show"):
                        new Show(collection);
                        break;
                    case ("show_all"):
                        new ShowAll();
                        break;
                    case ("add"):
                        Add adder = new Add();
                        adder.updateCollection(sender, collection, request.getBand());
                        break;
                    case ("clear"):
                        Clear clearer = new Clear();
                        clearer.invoke(sender, collection);
                        break;
                    /*case ("save"):
                        new Save(collection, collectionSaver);
                        break;*/
                    case ("exit"):
                        CommandsParser.clearBuffer();
                        logger.info("One client has disconnected via \"exit\" command");
                        return false;
                    case ("sort"):
                        Sort sorter1 = new Sort();
                        sorter1.invoke(collection);
                        break;
                    case ("sum_of_number_of_participants"):
                        SumOfNumberOfParticipants sum = new SumOfNumberOfParticipants();
                        sum.print(collection);
                        break;
                    case ("print_field_ascending_description"):
                        PrintFieldAscendingDescription printer = new PrintFieldAscendingDescription();
                        printer.invoke(collection);
                        break;
                    case ("update"):
                        long idToUpdate = Long.parseLong(str[1]);
                        UpdateId updater = new UpdateId();
                        updater.invoke(sender, collection, idToUpdate, request.getBand());
                        break;
                    case ("remove_by_id"):
                        long idToRemove = Long.parseLong(str[1]);
                        RemoveById remover = new RemoveById();
                        remover.invoke(sender, collection, idToRemove);
                        break;
                    case ("execute_script"):
                        ExecuteScript scriptExecutor = new ExecuteScript(request.getScript());
                        scriptExecutor.execute();
                        break;
                    /*case ("insert_at"):
                        int index = Integer.parseInt(str[1]);
                        InsertAt insertAt = new InsertAt();
                        insertAt.invoke(sender, collection, index, request.getBand());
                        break;*/
                    case ("remove_greater"):
                        long idToRemoveFrom = Long.parseLong(str[1]);
                        RemoveGreater removeGreater = new RemoveGreater();
                        collection = removeGreater.invoke(sender, collection, idToRemoveFrom);
                        break;
                    case ("count_greater_than_best_album"):
                        CountGreaterThanBestAlbum countGreaterThanBestAlbum = new CountGreaterThanBestAlbum();
                        countGreaterThanBestAlbum.invoke(collection, str[1]);
                        break;
                    default:
                        //System.out.println("Unknown command");
                        MessagesForClient.recordMessage("Unknown command");
                }
            }catch (IndexOutOfBoundsException | IllegalArgumentException e){
                MessagesForClient.recordMessage("Incorrect argument");
            }catch (Exception e){
                MessagesForClient.recordMessage(e.getMessage());
            }
        }while (!CommandsParser.isBufferEmpty());

        return true;

    }
}
