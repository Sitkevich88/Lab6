package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.*;
import java.io.*;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.Stack;
import java.lang.*;


/**
 * Server
 */

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

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

    public Server(int PORT, String hostName){
        this.PORT = PORT;
        serializer = new Serializer();
        this.hostName = hostName;
    }

    /**
     * runs the server
     */

    public void run() {

        logger.info("Server is launched");
        connect(hostName);
        boolean shouldAnswer;

        loadCollection();
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
        //close();
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
            //if (request.getCommand().trim().equals("exit"))
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

    /**
     * loads json collection to RequestsHandler
     */

    private void loadCollection(){
        String collectionPath = ".\\src\\data\\bands.json";
        CollectionSaver collectionSaver = new CollectionSaver(collectionPath);
        Stack<MusicBand> bands = collectionSaver.saveFileToCollection();
        handler = new RequestsHandler(bands, collectionSaver);
        logger.info("Collection is loaded from " + collectionPath);
    }

    /**
     * sends all buffered messages from MessagesForClient packed in a request
     */

    private void sendRequest(){
        try {
            InetAddress address = packet.getAddress();
            logger.warn("Server tries to send a request to " + address.getHostAddress());
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

    private void close(){
        running = false;
        shouldHandle = false;
        socket.close();
        //System.out.println("Работа серевера завершена");
        logger.info("The server is closed");
    }
}