package edu.wpi.cs3733.d19.teamM.utilities.General;

import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;

public class Login {

    private String username;
    private int adminInt;
    private String userPass;

    public Login(String username, int adminInt, String userPass) {
        this.username = username;
        this.adminInt = adminInt;
        this.userPass = userPass;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getAdminInt() {
        return adminInt;
    }

    void createTables(){
        DatabaseUtils conn = new DatabaseUtils();
        conn.getConnection();
        //TODO idk write this shit?
    }
/*
    String encryptPass(){

    }

    int fetchPass(){

    }

*/


}

/*
create table users(
    username varchar(100) primary key not null,
    accountInt int not null,
    userPass varchar(30) not null,
    isLoggedIn int,
    constraint adminBool check (accountInt = 100 or accountInt = 3 or accountInt = 2 or accountInt = 1 or accountInt = 0),
    constraint loggedBool check (isLoggedIn = 0 or isLoggedIn = 1)
);
 */
