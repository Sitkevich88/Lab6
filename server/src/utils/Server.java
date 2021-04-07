package utils;

import data.*;
import java.io.*;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.Stack;
import java.lang.*;

public class Server {

    private final int PORT;
    private final int PACKET_SIZE = 10000;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buf;
    private RequestsHandler handler;
    private Serializer serializer;
    private boolean running;
    private boolean shouldHandle;

    public Server(int PORT){
        this.PORT = PORT;
        serializer = new Serializer();
    }

    public void run() {

        connect();
        loadCollection();
        running = true;
        while (running) {
            shouldHandle = true;
            ServerRequest serverRequest = receiveRequest();
            if (shouldHandle){
                running = handler.handle(serverRequest);
            }
            if (!running){
                continue;
            }
            sendRequest();

        }
        //close();
    }


    private void connect(){
        try{
            DatagramChannel channel = DatagramChannel.open();
            InetSocketAddress iAdd = new InetSocketAddress("localhost", PORT);
            channel.bind(iAdd);
            socket = channel.socket();
        }
        catch (IOException e){
            System.out.println("IO server error");
        }
    }


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
            System.out.println("Server got a request");
        } catch (ClassNotFoundException e) {
            MessagesForClient.recordMessage("Incorrect request");
            shouldHandle = false;
        } catch (IOException e){
            MessagesForClient.recordMessage("IO exception");
            shouldHandle = false;
        }

        return request;
    }
    private void loadCollection(){
        CollectionSaver collectionSaver = new CollectionSaver(".\\src\\data\\bands.json");
        Stack<MusicBand> bands = collectionSaver.saveFileToCollection();
        handler = new RequestsHandler(bands, collectionSaver);
    }

    private void send(){
        try {
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            byte[] localBuffer = serializer.serialize(MessagesForClient.popMessagesInString());
            packet = new DatagramPacket(localBuffer, localBuffer.length, address, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(){
        try {
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            ClientRequest clientRequest = new ClientRequest(MessagesForClient.popMessagesInString());
            byte[] localBuffer = serializer.serialize(clientRequest);
            packet = new DatagramPacket(localBuffer, localBuffer.length, address, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close(){
        running = false;
        shouldHandle = false;
        socket.close();
        System.out.println("Работа серевера завершена");
    }
}