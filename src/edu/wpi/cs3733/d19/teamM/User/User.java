package edu.wpi.cs3733.d19.teamM.User;


public class User {

    private static User user;
    
    private static String username;
    private static int privilege;
    private static String pathToPic;

    public static String getUsername() {
        if(username == null) return "Guest";
        return username.substring(0, 1).toUpperCase() + username.substring(1);
    }
    public static void setUsername(String username) {
        user.username = username;
    }
    public static int getPrivilege() {
        return privilege;
    }
    public static void setPrivilege(int privilege) {
        user.privilege = privilege;
    }

    public static String getPathToPic() {
        return pathToPic;
    }

    public static void setPathToPic(String pathToPic) {
        User.pathToPic = pathToPic;
    }

    private User(){}

    public static User getUser(){
        if(user == null){
            user = new User();
        }
        return user;
    }
}

/*
package com.journaldev.singleton;

public class LazyInitializedSingleton {

    private static LazyInitializedSingleton instance;

    private LazyInitializedSingleton(){}

    public static LazyInitializedSingleton getInstance(){
        if(instance == null){
            instance = new LazyInitializedSingleton();
        }
        return instance;
    }
}
 */