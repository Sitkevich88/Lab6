package utils;


import data.*;
import org.slf4j.Logger;
import utils.sql.DataBaseConnector;
import utils.sql.UserAuthorisation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnresolvedAddressException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * Server
 */

public class ServerNew extends Thread{

    private static Logger logger;
    private SynchronousQueue<Object> requestExchangerBetweenReceiverAndHandler;
    private SynchronousQueue<Integer> requestExchangerBetweenHandlerAndSender;
    private final ExecutorService responseSender = Executors.newFixedThreadPool(3);
    private final ExecutorService requestHandler = Executors.newFixedThreadPool(3);
    private final int PORT;
    private final int PACKET_SIZE = 10000;
    private DatagramSocket socket;
    private volatile DatagramPacket packet ;
    //private CopyOnWriteArrayList<DatagramPacket> packets;
    private byte[] buf;
    private RequestsHandler handler;
    private Serializer serializer;
    private boolean running;
    private volatile boolean shouldHandle;
    private final String hostName;

    /**
     * Constructor
     * @param PORT port to listen to
     * @param hostName client to connect to
     */

    public ServerNew(int PORT, String hostName) {
        this.PORT = PORT;
        serializer = new Serializer();
        this.hostName = hostName;
        logger = new LogFactory().getLogger(this);
        running = true;
        requestExchangerBetweenReceiverAndHandler = new SynchronousQueue<>();
        requestExchangerBetweenHandlerAndSender = new SynchronousQueue<>();
        //packets = new CopyOnWriteArrayList<>();
    }

    /**
     * runs the server
     */

    public void run() {

        logger.info("Server is launched");
        connect(hostName);
        loadCollection();
        waitToGetClosed();
        do{
            Thread t = receiveRequest();
            handleRequest();
            Future f = sendRequest();
            //while (t.isAlive()){}
            while (!f.isDone()){}
        }while (running);
        System.exit(0);
    }


    /**
     * connects the server to the client
     * @param hostName client name
     */

    private void connect(String hostName){
        try{
            logger.warn("Server is trying to connect to " + hostName);
            DatagramChannel channel = DatagramChannel.open();
            InetSocketAddress iAdd = new InetSocketAddress(hostName, PORT);
            channel.bind(iAdd);
            socket = channel.socket();
            logger.info("Server has successfully connected");
        }
        catch (IOException | UnresolvedAddressException e){
            logger.error(e.getMessage());
            System.exit(1);
        }
    }


    private static LinkedBlockingQueue<MusicBand> bands = new LinkedBlockingQueue<>();

    public static void setBands(LinkedBlockingQueue<MusicBand> bands) {
        ServerNew.bands = bands;
    }

    /**
     * loads collection to RequestsHandler
     */

    private void loadCollection() {

        //LinkedBlockingQueue<MusicBand> bands = new LinkedBlockingQueue<>();
        try {
            Statement st = DataBaseConnector.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT owner, id, name, x, y, creation_date, number_of_participants, " +
                    "description, establishment_date, genre, album_name, tracks, length, sales " +
                    "FROM music_bands;");
            while (rs.next()){
                String owner = rs.getString("owner");
                long id = rs.getLong("id");
                String name = rs.getString("name");
                Coordinates coordinates = new Coordinates(rs.getInt("x"),rs.getInt("y"));
                Date creationDate = rs.getDate("creation_date");
                Integer numberOfParticipants = rs.getInt("number_of_participants");
                String description = rs.getString("description");
                ZonedDateTime establishmentDate = rs.getTimestamp("establishment_date").toLocalDateTime().atZone(ZoneId.of("+3"));
                //ZonedDateTime establishmentDate = ZonedDateTime.ofInstant(rs.getTimestamp("establishment_date").toInstant(), ZoneId.of("UTC"));
                MusicGenre genre = MusicGenre.getEnum(rs.getString("genre"));
                Album bestAlbum = new Album(rs.getString("album_name"), rs.getInt("tracks"),
                        rs.getInt("length"), rs.getFloat("sales"));
                bands.add(new MusicBand(owner, id, name, coordinates, creationDate,
                        numberOfParticipants, description, establishmentDate, genre, bestAlbum));
            }
        }catch (SQLException e){
            logger.error("Server did not manage to download collection");
            logger.error(e.getMessage());
            System.exit(1);
        }

        handler = new RequestsHandler(bands);
        logger.info("Collection is loaded");
    }

    /**
     * receives requests from a client
     */

    
    private Thread receiveRequest() {

        Thread receiver = new Thread(()->{
            shouldHandle = true;
            buf = new byte[PACKET_SIZE];
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                //packets.add(packet);
            } catch (IOException e) {
                logger.warn("Receiver is interrupted");
                interrupt();
            }
            Object request = null;
            try {
                request = serializer.deserialize(packet.getData());
                logger.info("Server got a request from user " + packet.getPort());
            } catch (ClassNotFoundException e) {
                logger.warn("Server got an incorrect request");
                //TODO MessagesForClient.recordMessage("Incorrect request");
                shouldHandle = false;
            } catch (IOException e) {
                shouldHandle = false;
                interrupt();
            } catch (NullPointerException e){
                interrupt();
            }finally {
                try {
                    requestExchangerBetweenReceiverAndHandler.put(request);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage());
                    e.printStackTrace();
                } catch (NullPointerException e){
                    interrupt();
                }
            }
        });
        receiver.setName("Receiver");
        receiver.start();
        return receiver;
    }

    private boolean auth = true;
    private boolean shouldAnswer = true;

    private void handleRequest(){
        requestHandler.submit(()->{
            Object request = null;
            //MessageFromServerToClient message = null;
            try {
                request = requestExchangerBetweenReceiverAndHandler.take();
                if (request==null){throw new NullPointerException();}
            } catch (InterruptedException e) {
                logger.warn("Handler is interrupted");
                interrupt();
            } catch (NullPointerException e){
                return;
            }
            shouldAnswer = true;
           
            if (request instanceof ServerRequest) {
                //LinkedList<ClientRequest> clientRequests = new LinkedList<>();
                auth = false;
                ServerRequest serverRequest = (ServerRequest) request;
                if (OnlineUsers.isAccountOnline(serverRequest.getUserData())){
                    //boolean shouldAnswer;
                    shouldHandle = true;
                    shouldAnswer = true;
                    if (shouldHandle){
                        boolean userExits = serverRequest.getCommand().contains("exit");
                        if (userExits){
                            OnlineUsers.removeUser(packet.getPort());
                            OnlineUsers.removeAccount(serverRequest.getUserData());
                        }
                        shouldAnswer = !userExits;
                        //message = handler.handle(serverRequest);
                        clientRequests = handler.handle(serverRequest);
                    }
                    if (!shouldAnswer){
                        //todo responseSender.shutdownNow();
                    }
                }else{
                    //messages.recordMessage("You are not authorised");
                    clientRequests.add(new ClientRequest(MessageFromServerToClient.NOT_AUTHORISED, new ArrayList<>(bands)));
                }
                try {
                    requestExchangerBetweenHandlerAndSender.put(1);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }

            } else if (request instanceof UserData) {
                auth = true;
                UserAuthorisation userAuthorisation = new UserAuthorisation(DataBaseConnector.getConnection());
                UserData userData = (UserData) request;
                authorisationResult = userAuthorisation.authorise(userData, packet.getPort());
                if (authorisationResult.equals(AuthorisationResult.OK)){
                    OnlineUsers.addUser(packet.getPort());
                }
                try {
                    requestExchangerBetweenHandlerAndSender.put(2);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            } else {
                shouldAnswer = false;
                try {
                    requestExchangerBetweenHandlerAndSender.put(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private AuthorisationResult authorisationResult;
    private LinkedList<ClientRequest> clientRequests = new LinkedList<>();


    /**
     * sends all buffered messages from MessagesForClient packed in a request
     */

    private Future sendRequest(){
        Future<Boolean> future = responseSender.submit(() -> {
            //boolean a = false;
            try {
                byte[] localBuffer = new byte[1];
                int number = requestExchangerBetweenHandlerAndSender.take();
                if (!shouldAnswer){
                    return true;
                }
                logger.warn("Server is trying to send a request to user " + packet.getPort());
                if (auth){
                    AuthorisationServerAnswer answer = new AuthorisationServerAnswer(authorisationResult);
                    answer.setBands(bands.stream().collect(Collectors.toCollection(ArrayList::new)));
                    auth = false;
                    localBuffer = serializer.serialize(answer);
                    packet = new DatagramPacket(localBuffer, localBuffer.length, packet.getAddress(), packet.getPort());
                    socket.send(packet);
                    logger.info("Request is sent to user " + packet.getPort());
                }else {
                    for (ClientRequest req : clientRequests){
                        boolean shouldSendEverybody = false;
                        switch (req.getMessage()){
                            case OBJECT_ADDED:
                            case OBJECT_REMOVED:
                            case OBJECT_UPDATED:
                            case MANY_OBJECTS_REMOVED:
                            case PRIVATE_COLLECTION_CLEARED:
                                shouldSendEverybody = true;
                                break;
                            default:
                                shouldSendEverybody = false;
                        }
                        if (shouldSendEverybody){
                            for(int port : OnlineUsers.getUsers()){
                                localBuffer = serializer.serialize(req);
                                packet = new DatagramPacket(localBuffer, localBuffer.length, packet.getAddress(), port);
                                socket.send(packet);
                                logger.info("Request is sent to user " + port);
                            }
                        }else{
                            localBuffer = serializer.serialize(req);
                            packet = new DatagramPacket(localBuffer, localBuffer.length, packet.getAddress(), packet.getPort());
                            socket.send(packet);
                            logger.info("Request is sent to user " + packet.getPort());
                            sleep(20);
                        }
                    }
                }

            } catch (InterruptedException e) {
                logger.warn("Sender is interrupted");
                interrupt();
            } catch (IOException e){
                logger.error(e.getMessage());
            }
            return true;
        });
        return future;

    }

    /**
     * terminates the server
     */

    public void waitToGetClosed() {

        Thread closer = new Thread(()->{
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Scanner scanner = new Scanner(System.in);
            String command;
            System.out.println("Write \'close\' if you want to shut down the server");
            while (true){
                try {
                    command = scanner.nextLine();
                }catch (NoSuchElementException | NullPointerException e){
                    break;
                }

                if (command.trim().toLowerCase().equals("close")){
                    break;
                }
            }
            running = false;
            shouldHandle = false;
            logger.warn("The server is closing");
            socket.close();
            requestHandler.shutdownNow();
            responseSender.shutdownNow();
        });
        closer.start();
    }
}