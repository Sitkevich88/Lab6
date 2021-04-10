package utils;

import commands.withMaxOneArgument.*;
import commands.withTwoArguments.*;
import data.MusicBand;
import data.ServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class RequestsHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestsHandler.class);
    private Stack<MusicBand> collection;
    private CollectionSaver collectionSaver;

    public RequestsHandler(Stack<MusicBand> collection, CollectionSaver collectionSaver) {
        this.collection = collection;
        this.collectionSaver = collectionSaver;
    }

    public boolean handle(ServerRequest request){
        String[] str;
        do{
            try {
                str = CommandsParser.parseArguments(request);
                logger.warn("Server is handling \"" + str[0] + "\" command");
                Sort sorter = new Sort();
                collection = sorter.invoke(collection);
                switch (str[0]) {
                    case ("help"):
                        new Help();
                        break;
                    case ("info"):
                        Info info = new Info();
                        info.invoke(collection);
                        break;
                    case ("show"):
                        new Show(collection);
                        break;
                    case ("add"):
                        Add adder = new Add();
                        collection = adder.updateCollection(collection, request.getBand());
                        break;
                    case ("clear"):
                        Clear clearer = new Clear();
                        collection = clearer.invoke(collection);
                        break;
                /*case ("save"):
                    new Save(collection, collectionSaver);
                    break;*/
                    case ("exit"):
                        new Save(collection, collectionSaver);
                        CommandsParser.clearBuffer();
                        logger.info("One client has disconnected via \"exit\" command");
                        return false;
                    case ("sort"):
                        Sort sorter1 = new Sort();
                        collection = sorter1.invoke(collection);
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
                        long idToUpdate = Long.valueOf(str[1]).longValue();
                        UpdateId updater = new UpdateId();
                        collection = updater.invoke(collection, idToUpdate, request.getBand());
                        break;
                    case ("remove_by_id"):
                        long idToRemove = Long.valueOf(str[1]).longValue();
                        RemoveById remover = new RemoveById();
                        collection = remover.invoke(collection, idToRemove);
                        break;
                    case ("execute_script"):
                        ExecuteScript scriptExecutor = new ExecuteScript(request.getScript());
                        scriptExecutor.execute();
                        break;
                    case ("insert_at"):
                        int index = Integer.valueOf(str[1]).intValue();
                        InsertAt insertAt = new InsertAt();
                        collection = insertAt.invoke(collection, index, request.getBand());
                        break;
                    case ("remove_greater"):
                        int indexToRemove = Integer.valueOf(str[1]).intValue();
                        RemoveGreater removeGreater = new RemoveGreater();
                        collection = removeGreater.invoke(collection, indexToRemove);
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
                MessagesForClient.recordMessage(e.getStackTrace().toString());
            }
        }while (!CommandsParser.isBufferEmpty());

        return true;

    }
}
