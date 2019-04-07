package edu.wpi.cs3733.d19.teamM.User;

public class User {

    private static User user;
    
    private static String username;
    private static int privilege;

    public static String getUsername() {
        return username;
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