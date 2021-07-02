package utils;


import commands.withMessage.*;
import commands.withUpdate.*;
import data.ClientRequest;
import data.MessageFromServerToClient;
import data.MusicBand;
import data.ServerRequest;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class RequestsHandler {

    private static Logger logger;
    private LinkedBlockingQueue<MusicBand> collection;
    private LinkedList<ClientRequest> queue = new LinkedList<>();

    public RequestsHandler(LinkedBlockingQueue<MusicBand> collection) {
        this.collection = collection;
        logger = new LogFactory().getLogger(this);
    }

    public synchronized LinkedList<ClientRequest> handle(ServerRequest request){
        String[] str;
        ClientRequest clientRequest = new ClientRequest();
        //CommandsParser.setClientRequest(clientRequest);
        /*if (queue.size()>0){
            return queue.poll();
        }*/
        do{
            try {
                clientRequest = new ClientRequest();
                //System.out.println(request.getCommand());
                str = CommandsParser.parseArguments(request);
                String sender = request.getSender();
                //messages.setSender(sender);
                String wholeCommand = "";
                for (String word : str){
                    wholeCommand += word +" ";
                }
                if (wholeCommand.length()>0){wholeCommand = wholeCommand.substring(0, wholeCommand.length()-1);}
                logger.warn("Server is handling \"" + wholeCommand + "\" command for \"" + request.getSender() + "\" account");
                /*Sort sorter = new Sort(collection, messages);
                sorter.invoke();*/
                switch (str[0]) {

                    case ("info"):
                        Info info = new Info(collection, clientRequest);
                        info.invoke();
                        break;
                    case ("add"):
                        Add adder = new Add(collection, clientRequest);
                        adder.updateCollection(sender, request.getBand(), clientRequest);
                        break;
                    case ("clear"):
                        Clear clearer = new Clear(collection, clientRequest);
                        clearer.invoke(sender);
                        break;
                    case ("exit"):
                        CommandsParser.clearBuffer();
                        logger.info("One client has disconnected via \"exit\" command");
                        OnlineUsers.removeAccount(request.getUserData());
                        break;
                    case ("sum_of_number_of_participants"):
                        SumOfNumberOfParticipants sum = new SumOfNumberOfParticipants(collection, clientRequest);
                        sum.invoke();
                        break;
                    case ("print_field_ascending_description"):
                        PrintFieldAscendingDescription printer = new PrintFieldAscendingDescription(collection, clientRequest);
                        printer.invoke();
                        break;
                    case ("update"):
                        long idToUpdate = Long.parseLong(str[1]);
                        UpdateId updater = new UpdateId(collection, clientRequest);
                        updater.invoke(sender, idToUpdate, request.getBand());
                        break;
                    case ("remove_by_id"):
                        long idToRemove = Long.parseLong(str[1]);
                        RemoveById remover = new RemoveById(collection, clientRequest);
                        collection = remover.invoke(sender, idToRemove);
                        break;
                    case ("execute_script"):
                        ExecuteScript scriptExecutor = new ExecuteScript(clientRequest);
                        scriptExecutor.execute(request.getScript());
                        break;

                    case ("remove_greater"):
                        long idToRemoveFrom = Long.parseLong(str[1]);
                        RemoveGreater removeGreater = new RemoveGreater(collection, clientRequest);
                        collection = removeGreater.invoke(sender, idToRemoveFrom);
                        break;
                    case ("count_greater_than_best_album"):
                        CountGreaterThanBestAlbum countGreaterThanBestAlbum = new CountGreaterThanBestAlbum(collection, clientRequest);
                        countGreaterThanBestAlbum.invoke(str[1]);
                        break;
                    default:
                        //System.out.println("Unknown command");
                        clientRequest.setMessage(MessageFromServerToClient.ERROR);
                        //messages.recordMessage("Unknown command");
                }
            }catch (IndexOutOfBoundsException | IllegalArgumentException e){
                //messages.recordMessage("Incorrect argument");
                clientRequest.setMessage(MessageFromServerToClient.ERROR);
            }catch (Exception e){
                //messages.recordMessage(e.getMessage());
                logger.warn(e.getMessage());
            }
            //if clientRequest
            ServerNew.setBands(collection);
            clientRequest.setBands(collection.stream().collect(Collectors.toCollection(ArrayList::new)));
            if (clientRequest.getMessage()!=null){
                queue.add(clientRequest);
            }
            //ServerNew.setBands(collection);
        }while (!CommandsParser.isBufferEmpty());

        LinkedList<ClientRequest> queueCopy = new LinkedList<>();
        queueCopy.addAll(queue);
        queue.clear();
        return queueCopy;
    }
}
