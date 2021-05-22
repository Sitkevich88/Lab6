package utils;

import commands.withMaxOneArgument.*;
import commands.withTwoArguments.*;
import data.MusicBand;
import data.ServerRequest;
import org.slf4j.Logger;
import utils.sql.DataBaseConnector;
import java.sql.Connection;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestsHandler {

    private static Logger logger;
    private LinkedBlockingQueue<MusicBand> collection;
    private Connection connection;

    public RequestsHandler(LinkedBlockingQueue<MusicBand> collection) {
        this.collection = collection;
        connection = DataBaseConnector.getConnection();
        logger = new LogFactory().getLogger(this);
    }

    public synchronized MessagesForClient handle(ServerRequest request){
        String[] str;
        MessagesForClient messages = new MessagesForClient();
        CommandsParser.setMessages(messages);
        do{
            try {
                str = CommandsParser.parseArguments(request);
                String sender = request.getSender();
                logger.warn("Server is handling \"" + str[0] + "\" command");
                Sort sorter = new Sort(collection, messages);
                sorter.invoke();
                switch (str[0]) {
                    case ("help"):
                        Help help = new Help(messages);
                        help.invoke();
                        break;
                    case ("info"):
                        Info info = new Info(collection, messages);
                        info.invoke();
                        break;
                    case ("show"):
                        Show show = new Show(collection, messages);
                        show.invoke(sender);
                        break;
                    case ("show_all"):
                        ShowAll showAll = new ShowAll(collection, messages);
                        showAll.invoke();
                        break;
                    case ("add"):
                        Add adder = new Add(collection, messages);
                        adder.updateCollection(sender, request.getBand());
                        break;
                    case ("clear"):
                        Clear clearer = new Clear(collection, messages);
                        clearer.invoke(sender);
                        break;
                    /*case ("save"):
                        new Save(collection, collectionSaver);
                        break;*/
                    /*case ("exit"):
                        CommandsParser.clearBuffer();
                        logger.info("One client has disconnected via \"exit\" command");
                        //OnlineUsers.removeUser(sender);
                        return false;*/
                    case ("sort"):
                        Sort sorter1 = new Sort(collection, messages);
                        sorter1.invoke();
                        break;
                    case ("sum_of_number_of_participants"):
                        SumOfNumberOfParticipants sum = new SumOfNumberOfParticipants(collection, messages);
                        sum.invoke();
                        break;
                    case ("print_field_ascending_description"):
                        PrintFieldAscendingDescription printer = new PrintFieldAscendingDescription(collection, messages);
                        printer.invoke();
                        break;
                    case ("update"):
                        long idToUpdate = Long.parseLong(str[1]);
                        UpdateId updater = new UpdateId(collection, messages);
                        updater.invoke(sender, idToUpdate, request.getBand());
                        break;
                    case ("remove_by_id"):
                        long idToRemove = Long.parseLong(str[1]);
                        RemoveById remover = new RemoveById(collection, messages);
                        remover.invoke(sender, idToRemove);
                        break;
                    case ("execute_script"):
                        ExecuteScript scriptExecutor = new ExecuteScript(messages);
                        scriptExecutor.execute(request.getScript());
                        break;
                    /*case ("insert_at"):
                        int index = Integer.parseInt(str[1]);
                        InsertAt insertAt = new InsertAt();
                        insertAt.invoke(sender, collection, index, request.getBand());
                        break;*/
                    case ("remove_greater"):
                        long idToRemoveFrom = Long.parseLong(str[1]);
                        RemoveGreater removeGreater = new RemoveGreater(collection, messages);
                        collection = removeGreater.invoke(sender, idToRemoveFrom);
                        break;
                    case ("count_greater_than_best_album"):
                        CountGreaterThanBestAlbum countGreaterThanBestAlbum = new CountGreaterThanBestAlbum(collection, messages);
                        countGreaterThanBestAlbum.invoke(str[1]);
                        break;
                    default:
                        //System.out.println("Unknown command");
                        messages.recordMessage("Unknown command");
                }
            }catch (IndexOutOfBoundsException | IllegalArgumentException e){
                messages.recordMessage("Incorrect argument");
            }catch (Exception e){
                messages.recordMessage(e.getMessage());
            }
        }while (!CommandsParser.isBufferEmpty());

        return messages;

    }
}
