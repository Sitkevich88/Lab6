package utils;

import data.*;
import gui.App;
import gui.authorisation.CredentialsWithMode;
import gui.common.Message;
import utils.auth.AuthorisationRequestFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Client
 */

public class ClientNew {


    private Serializer serializer;
    private ByteBuffer buffer;
    private ClientProperties properties;

    private Selector selector;
    private SelectionKey key;

    private AuthorisationRequestFactory authorisationRequestFactory;

    /*private AuthorisationWindowCommunicator authorisationWindowCommunicator;
    private MainTableWindowCommunicator tableWindowCommunicator;
    private Message message;*/

    private static boolean running = true;


    /**
     * Client constructor
     * @param host server address name
     * @param port server port to connect to
     */

    public ClientNew(String host, int port){
        serializer = new Serializer();
        properties = new ClientProperties(host, port);
        authorisationRequestFactory = new AuthorisationRequestFactory();
    }


    private AtomicBoolean receivingPeriod = new AtomicBoolean(false);

    public void authorise(CredentialsWithMode credentials){
        UserData userData = authorisationRequestFactory.getAuthorisationRequest(credentials);
        ServerRequestsFactory.setUserData(userData);
        if (userData==null){return;}
        try {
            waitToBeAbleSending();
            buffer = serializer.serialize(userData);
            properties.send(buffer);
            setLastRequestType(RequestType.AUTHORISATION);
            requestIsSent.set(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void send(ServerRequest serverRequest){
        try {
            waitToBeAbleSending();
            buffer = serializer.serialize(serverRequest);
            properties.send(buffer);
            if (!serverRequest.getCommand().equals("exit")){
                setLastRequestType(RequestType.SERVER_REQUEST);
                requestIsSent.set(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean waitToBeAbleSending(){
        while (running){
            if (!receivingPeriod.get()){
                return true;
            }
        }
        return false;
    }

    private enum RequestType{
        AUTHORISATION,
        SERVER_REQUEST
    }

    private RequestType lastRequestType = RequestType.AUTHORISATION;

    private void setLastRequestType(RequestType type){
        lastRequestType = type;
    }


    private AtomicBoolean requestIsSent = new AtomicBoolean(false);
    private boolean receiving = true;

    /**
     * runs receiver
     */

    public void runReceiver(){

        Runnable r = () -> {
            try {
                receivingPeriod.set(false);
                //properties.getDatagramChannel().configureBlocking(false);
                selector = Selector.open();
                key = properties.getDatagramChannel().register(selector, SelectionKey.OP_READ, SelectionKey.OP_WRITE);
                running = true;
                boolean requestIsReceived = false;
                while(running & receiving) {

                    long timeOut = 3000L;

                    if (requestIsSent.get()){
                        timeOut = properties.getTimeout();
                    }
                    receivingPeriod.set(true);
                    int readyChannels = selector.select(timeOut);
                    receivingPeriod.set(false);
                    if(readyChannels == 0 & requestIsSent.get()){
                        if (lastRequestType.equals(RequestType.AUTHORISATION)){
                            App.receiveAuthorisationServerAnswer(Message.SERVER_IS_SILENT);
                        } else if (lastRequestType.equals(RequestType.SERVER_REQUEST)){
                            ClientRequest clientRequest = new ClientRequest();
                            clientRequest.setMessage(MessageFromServerToClient.SERVER_IS_SILENT);
                            App.receiveClientRequest(clientRequest);
                        }
                        requestIsSent.set(false);
                        continue;
                    }

                    if(readyChannels == 0) {
                        requestIsReceived = false;
                        continue;
                    }

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();

                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                    while(keyIterator.hasNext()) {

                        SelectionKey key = keyIterator.next();

                        if(key.isReadable()) {
                            receivingPeriod.set(true);
                            buffer = ByteBuffer.allocate(properties.getPACKET_SIZE());
                            properties.receive(buffer);
                            requestIsReceived = true;
                            receivingPeriod.set(false);
                            buffer.flip();
                            try{
                                Object rawRequest = serializer.deserialize(buffer.array());

                                if (rawRequest instanceof ClientRequest){
                                    ClientRequest clientRequest = (ClientRequest) rawRequest;
                                    App.receiveClientRequest(clientRequest);
                                } else if (rawRequest instanceof AuthorisationServerAnswer){
                                    AuthorisationServerAnswer authorisationServerAnswer = (AuthorisationServerAnswer) rawRequest;
                                    App.receiveAuthorisationServerAnswer(App.convert(authorisationServerAnswer.getAuthorisationResult()));
                                    ArrayList<MusicBand> bands = authorisationServerAnswer.getBands();
                                    if (bands!=null){
                                        App.setBands(authorisationServerAnswer.getBands());
                                    }
                                } else {
                                    throw new ClassNotFoundException();
                                }

                            } catch (ClassNotFoundException e){
                                if (lastRequestType.equals(RequestType.AUTHORISATION) & requestIsSent.get()){
                                    App.receiveAuthorisationServerAnswer(Message.SERVER_IS_SILENT);
                                }
                                //App.receiveClientRequest(null);
                            }
                        }
                        keyIterator.remove();
                        if (requestIsSent.get()){
                            requestIsSent.set(false);
                        }
                    }
                }
            } catch (IOException e){
                //System.out.println(e.getLocalizedMessage());
                e.printStackTrace();
                System.exit(1);
            }
        };
        new Thread(r).start();
    }


    /**
     * disconnects the client from the server
     */

    public void close() {
        try {
            setRunning(false);
            properties.getDatagramChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Session has been terminated");
    }

    public static void setRunning(boolean running) {
        ClientNew.running = running;
    }

}