package edu.wpi.cs3733.d19.teamM.User;

public class User {
    
    private String username;
    private int privilege;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPrivilege() {
        return privilege;
    }

    public User(String username, int privilege) {
        this.username = username;
        this.privilege = privilege;
    }
}
