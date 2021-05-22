package utils;

import data.UserData;

import java.util.HashSet;

public class OnlineUsers {
    private static HashSet<UserData> onlineUsers = new HashSet<>();

    public static void addUser(UserData user){
        onlineUsers.add(user);
    }
    public static void removeUser(UserData user){
        if (onlineUsers.contains(user)){
            onlineUsers.remove(user);
        }
    }
    public static boolean isUserOnline (UserData user){
        return onlineUsers.contains(user);
    }
}
