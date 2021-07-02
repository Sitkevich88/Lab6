package utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientProperties {

    private DatagramChannel datagramChannel;
    private int timeout;
    private SocketAddress serverAddress;
    private int PACKET_SIZE;


    public ClientProperties(String host, int port) {
        timeout = 5*1000;
        PACKET_SIZE = 10*1000;

        serverAddress = new InetSocketAddress(host, port);
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(null);
            datagramChannel.configureBlocking(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DatagramChannel getDatagramChannel() {
        return datagramChannel;
    }

    public int getTimeout() {
        return timeout;
    }

    public SocketAddress getServerAddress() {
        return serverAddress;
    }

    public int getPACKET_SIZE() {
        return PACKET_SIZE;
    }

    public byte[] getBuffer(){
        return new byte[PACKET_SIZE];
    }

    public boolean connectToServer() throws IOException{
        boolean isConnected = false;
        datagramChannel.connect(serverAddress);
        isConnected = datagramChannel.isConnected();

        return isConnected;
    }

    public void send(ByteBuffer b) throws IOException{
        datagramChannel.send(b, serverAddress);
    }

    public void receive(ByteBuffer b) throws IOException{
        datagramChannel.receive(b);
    }
}
