package utils;


import org.slf4j.Logger;
import data.*;
import utils.sql.DataBaseConnector;
import utils.sql.UserAuthorisation;
import java.io.*;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnresolvedAddressException;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.util.Stack;
import java.lang.*;


/**
 * Server
 */

public class Server extends Thread{

    private static Logger logger;

    private final int PORT;
    private final int PACKET_SIZE = 10000;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buf;
    private RequestsHandler handler;
    private Serializer serializer;
    private boolean running;
    private boolean shouldHandle;
    private final String hostName;

    /**
     * Constructor
     * @param PORT port to listen to
     * @param hostName client to connect to
     */

    public Server(int PORT, String hostName) {
        this.PORT = PORT;
        serializer = new Serializer();
        this.hostName = hostName;
        logger = new LogFactory().getLogger(this);

    }

    /**
     * runs the server
     */

    public void run() {

        logger.info("Server is launched");
        connect(hostName);
        boolean shouldAnswer;
        String login = authoriseUser();

        loadCollection(login);
        running = true;
        while (running) {
            shouldHandle = true;
            shouldAnswer = true;
            ServerRequest serverRequest = receiveRequest();
            if (shouldHandle){
                shouldAnswer = handler.handle(serverRequest);
            }
            if (!running || !shouldAnswer){
                continue;
            }
            sendRequest();

        }
    }

    /**
     * connects the server to the client
     * @param hostName client name
     */

    private void connect(String hostName){
        try{
            logger.warn("Server tries to connect to " + hostName);
            DatagramChannel channel = DatagramChannel.open();
            InetSocketAddress iAdd = new InetSocketAddress(hostName, PORT);
            channel.bind(iAdd);
            socket = channel.socket();
            logger.info("Server has successfully connected");
        }
        catch (IOException e){
            logger.error("IO exception");
            System.exit(1);
        } catch (UnresolvedAddressException e){
            logger.error("Server could not connect");
            System.exit(1);
        }
    }

    /**
     * receives requests from the client
     * @return ServerRequest
     */

    private ServerRequest receiveRequest() {

        buf = new byte[PACKET_SIZE];
        packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerRequest request = null;
        try {
            request = (ServerRequest) serializer.deserialize(packet.getData());
            logger.info("Server got a request from " + packet.getAddress().getHostAddress());
        } catch (ClassNotFoundException e) {
            logger.warn("Server got an incorrect request");
            MessagesForClient.recordMessage("Incorrect request");
            shouldHandle = false;
        } catch (IOException e){
            logger.error("IO exception");
            MessagesForClient.recordMessage("IO exception");
            shouldHandle = false;
        }

        return request;
    }

    private String authoriseUser(){
        UserAuthorisation userAuthorisation = new UserAuthorisation(DataBaseConnector.getConnection());
        UserData userData = null;
        boolean isLoggedIn = false;

        while (!isLoggedIn){

            try {
                buf = new byte[PACKET_SIZE];
                packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);
                userData = (UserData) serializer.deserialize(packet.getData());
                logger.info("Server got an authorisation form from " + packet.getAddress().getHostAddress());
                isLoggedIn = userAuthorisation.authorise(userData);
            } catch (ClassNotFoundException e) {
                logger.warn("Server got an incorrect authorisation form");
                MessagesForClient.recordMessage("Incorrect request. Send authorisation form");

            } catch (IOException e){
                logger.error("IO exception");
                MessagesForClient.recordMessage("IO exception");
                e.printStackTrace();

            }catch (ClassCastException e){
                logger.error("Server got an incorrect request");
                MessagesForClient.recordMessage("Send an authorisation form");

            }finally {
                sendRequest();
            }

        }
        return userData.getLogin();
    }

    /**
     * loads json collection to RequestsHandler
     */

    private void loadCollection(String userName) {

        Stack<MusicBand> bands = new Stack<>();
        try {
            Statement st = DataBaseConnector.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name, x, y, creation_date, number_of_participants, " +
                    "description, establishment_date, genre, album_name, tracks, length, sales " +
                    "FROM music_bands WHERE owner=\'"+ userName +"\';");
            while (rs.next()){
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
                bands.add(new MusicBand(id, name, coordinates, creationDate,
                        numberOfParticipants, description, establishmentDate, genre, bestAlbum));
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.exit(1);
        }

        handler = new RequestsHandler(bands);
        logger.info("Collection is loaded");
    }

    /**
     * sends all buffered messages from MessagesForClient packed in a request
     */

    private void sendRequest(){
        try {
            InetAddress address = packet.getAddress();
            logger.warn("Server is trying to send a request to " + address.getHostAddress());
            int port = packet.getPort();
            ClientRequest clientRequest = new ClientRequest(MessagesForClient.popMessagesInString());
            byte[] localBuffer = serializer.serialize(clientRequest);
            packet = new DatagramPacket(localBuffer, localBuffer.length, address, port);
            socket.send(packet);
            logger.info("Request is sent to " + address.getHostAddress());
        } catch (IOException e) {
            logger.error(e.getStackTrace().toString());
        }
    }

    /**
     * terminates the server
     */

    public void waitToGetClosed() {

        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.println("Write \'close\' if you want to shutdown the server");
        while (true){
            command = scanner.nextLine();
            if (command.trim().toLowerCase().equals("close")){
                break;
            }
        }
        running = false;
        shouldHandle = false;
        socket.close();
        interrupt();
        logger.info("The server is closed");
    }
}