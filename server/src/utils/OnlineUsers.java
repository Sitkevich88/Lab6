package utils;

import data.UserData;
import java.util.HashSet;

public class OnlineUsers {

    private static HashSet<UserData> onlineAccounts = new HashSet<>();
    private static HashSet<Integer> onlineUsers = new HashSet<>();

    public static void addAccount(UserData user){
        onlineAccounts.add(user);
    }
    public static void removeAccount(UserData user){
        if (onlineAccounts.contains(user)){
            onlineAccounts.remove(user);
        }
    }
    public static boolean isAccountOnline(UserData user){
        return onlineAccounts.contains(user);
    }

    public static void addUser(int port){
        onlineUsers.add(port);
    }

    public static Integer[] getUsers(){
        return onlineUsers.toArray(new Integer[0]);
    }

    public static void removeUser(int port){
        onlineUsers.remove(port);
    }
}
