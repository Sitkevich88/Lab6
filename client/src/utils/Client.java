package utils;

import data.ClientRequest;
import data.ServerRequest;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.UnresolvedAddressException;

/**
 * Client
 */

public class Client extends Thread {

    private ServerRequestsFactory requestsFactory;
    private Serializer serializer;
    private DatagramChannel channel;
    private SocketAddress serverAddress;
    private final int PORT;
    private int PACKET_SIZE = 10000;
    private byte[] buf = new byte[PACKET_SIZE];
    private ByteBuffer buffer;
    private boolean running;
    private boolean shouldReceive;
    private Selector selector;
    private SelectionKey key;


    /**
     * Client constructor
     * @param host server address name
     * @param port server port to connect to
     */

    public Client(String host, int port){

        this.PORT = port;
        serverAddress = new InetSocketAddress(host, PORT);
        serializer = new Serializer();
        try {
            channel = DatagramChannel.open();
            channel.bind(null);
            requestsFactory = new ServerRequestsFactory();
            running = true;
            channel.configureBlocking(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * runs client
     */

    @Override
    public void run() {

        boolean connected = connect();
        if (connected){
            while (running){
                shouldReceive = true;
                sendRequest();
                if (!running){
                    continue;
                }
                if (shouldReceive){
                    receiveClientRequest();
                }
            }
        }
        close();
    }

    /**
     * connects the client to the server
     * @return boolean if the client is connected to the server
     */

    public boolean connect(){
        try {
            channel.connect(serverAddress);
            if (channel.isConnected()){
                System.out.println("Client has successfully connected");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnresolvedAddressException e){
            System.out.println("Incorrect server address");
        }
        System.out.println("Cannot connect to the server");
        return false;
    }

    /**
     * sends request to the server
     */

    public void sendRequest() {
        ServerRequest request = requestsFactory.getRequestFromConsole();
        if (request.getCommand().equals("exit")){
            running = false;
        } else if (request.getScript()!=null && request.getScript().length()>0){
            if (request.getScript().contains("exit")){
                System.out.println("Script contains exit command");
                running = false;
            }
        }
        buffer = null;
        try {
            buffer = serializer.serialize(request);
            channel.send(buffer, serverAddress);
        } catch (IOException e) {
            System.out.println("IO error\nRequest has not been sent");
            shouldReceive = false;
        }
        //System.out.println("Клиент отправил сообщение");
    }

    /**
     * receives requests from the server
     */

    public void receiveClientRequest() {

        buffer = ByteBuffer.allocate(PACKET_SIZE);
        try {
            selector = Selector.open();
            key = channel.register(selector, SelectionKey.OP_READ);
            selector.select(3000);
            channel.receive(buffer);
            buffer.flip();
            ClientRequest clientRequest = (ClientRequest) serializer.deserialize(buffer.array());
            if (clientRequest.getMessages()!=null && clientRequest.getMessages().length()>0){
                System.out.println(clientRequest.getMessages());

            }
        } catch (IOException e) {
            System.out.println("Server does not respond. Send new request later");
        } catch (ClassNotFoundException e) {
            System.out.println("Client received an unreadable request");
        }catch (NullPointerException e){
            running = false;
        }
    }

    /**
     * disconnects the client from the server
     */

    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Session has been finished");
    }

}