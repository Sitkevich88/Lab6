package utils;

import java.util.ArrayList;

public class MessagesForClient {

    private ArrayList<String> messages = new ArrayList<>();

    public ArrayList<String> peekMessages(){return  messages;}

    public ArrayList<String> popMessages(){
        ArrayList<String> savedMessages = messages;
        messages.clear();
        return savedMessages;
    }

    public String popMessagesInString(){
        StringBuilder allMessages = new StringBuilder();
        for (String m : messages){
            allMessages.append(m + '\n');

        }
        if (allMessages.length()!=0){
            allMessages.deleteCharAt(allMessages.length()-1);
        }

        messages.clear();
        //System.out.println(allMessages.toString());
        return allMessages.toString();
    }

    public void recordMessage(String newMessage){
        messages.add(newMessage);
    }
    public void appendLastMessage(String str){
        String lastMessage = messages.get(messages.size()-1);
        messages.remove(messages.size()-1);
        messages.add(lastMessage + str);
    }
}
