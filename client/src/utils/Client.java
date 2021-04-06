package utils;

import data.ClientRequest;
import data.ServerRequest;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {

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



    public Client(String host, int port){

        this.PORT = port;
        serverAddress = new InetSocketAddress(host, PORT);
        serializer = new Serializer();
        try {
            channel = DatagramChannel.open();
            channel.bind(null);
            System.out.println("Клиент успешно подключился");
            requestsFactory = new ServerRequestsFactory();
            running = true;
            channel.socket().setSoTimeout(5*1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (running){
            shouldReceive = true;
            sendRequest();
            if (running==false){
                continue;
            }
            if (shouldReceive){
                receiveClientRequest();
            }
        }
        close();
    }

    public void sendRequest() {
        ServerRequest request = requestsFactory.getRequestFromConsole();
        if (request.getCommand().equals("exit")){
            running = false;
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


    /*public void receive () throws IOException, ClassNotFoundException {
        buffer = ByteBuffer.allocate(PACKET_SIZE);
        channel.receive(buffer);
        //System.out.println("Клиент получил сообщение");
        buffer.flip();
        String msg = (String) serializer.deserialize(buffer.array());
        System.out.println(msg);
    }*/

    public void receiveClientRequest() {
        buffer = ByteBuffer.allocate(PACKET_SIZE);
        try {
            channel.receive(buffer);
            buffer.flip();
            ClientRequest clientRequest = (ClientRequest) serializer.deserialize(buffer.array());
            System.out.println(clientRequest.getMessages());
        } catch (IOException e) {
            System.out.println("IO error, No requests are received");
        } catch (ClassNotFoundException e) {
            System.out.println("Client received an unreadable request");
        }catch (NullPointerException e){
            running = false;
        }
    }

    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Клиент закончил сессию");
    }

}