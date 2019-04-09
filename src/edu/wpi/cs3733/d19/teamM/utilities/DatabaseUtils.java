package edu.wpi.cs3733.d19.teamM.utilities;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.sql.*;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;

public class DatabaseUtils {

    String dbPath = "jdbc:derby:myDB;create=true";

    public Connection getConnection(){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            return DriverManager.getConnection(dbPath);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void edgeParse() {

        InputStream file = Main.getResource("/resources/edgesv5.csv");

        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;

        try {
            inputStream = new Scanner(file);
            while (inputStream.hasNext()) {
                String line = inputStream.next();
                String[] values = line.split(",");
                lines.add(Arrays.asList(values));
            }
            inputStream.close();
        } catch (Exception q) {
            q.printStackTrace();
        }

        try {
            int lineNum = 1;
            for (List<String> line : lines) {
                Connection conn = this.getConnection();
                String query = "insert into edge (edgeID,startNode,endNode) values (?, ?, ?)";
                PreparedStatement preStmt = conn.prepareStatement(query);
                for (int columnNum = 0; columnNum < 3; columnNum++) {
                    if (lineNum != 1) {
                        if (columnNum != 2) {
                            preStmt.setString(columnNum + 1, line.get(columnNum));
                        }
                        else if (columnNum == 2) {
                            preStmt.setString(columnNum + 1, line.get(columnNum));
                            preStmt.executeUpdate();
                            conn.close();
                        }
                    }
                    else {
                        conn.close();
                    }
                }
                lineNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //conn.close();

      /* } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
            return;
        }*/
        //System.out.println("Java DB connection established!");
    }

    public void nodeParse(){
        InputStream file = Main.getResource("/resources/nodesv4.csv");
        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;

        try {
            inputStream = new Scanner(file);
            inputStream.nextLine();
            while (inputStream.hasNext()) {
                String line = inputStream.nextLine();
                String[] values = line.split(",");
                lines.add(Arrays.asList(values));
            }
            inputStream.close();
        } catch (Exception q) {
            q.printStackTrace();
        }

        try {
            int lineNum = 1;
            for (List<String> line : lines) {
                Connection conn = this.getConnection();
                //String query = "insert into floor" + line.get(3) +" (nodeid, xcoord, ycoord, floor, building, nodetype, longname, shortname) values (?, ?, ?, ?, ?, ?, ?, ?)";
                String query = "insert into node (nodeid, xcoord, ycoord, floor, building, nodetype, longname, shortname) values (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preStmt = conn.prepareStatement(query);
                for (int columnNum = 0; columnNum < 8; columnNum++) {
                    if (lineNum != 1) {
                        if (columnNum == 1 || columnNum == 2) {
                            preStmt.setInt(columnNum + 1, Integer.parseInt(line.get(columnNum)));
                        }
                        else if (columnNum != 1 || columnNum != 2){
                            if(columnNum != 7){
                                preStmt.setString(columnNum + 1, line.get(columnNum));
                            }
                            else {
                                preStmt.setString(columnNum + 1, line.get(columnNum));
                                preStmt.executeUpdate();
                                conn.close();
                            }
                        }
                    }
                    else {
                        conn.close();
                    }
                }
                lineNum++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        Encrypt encrypt = new Encrypt();

        try {
            Connection conn = this.getConnection();
            String createTable1 = "CREATE TABLE node(NODEID varchar(20) PRIMARY KEY NOT NULL, XCOORD int, ycoord int, floor varchar(2), building varchar(15), nodetype varchar(4), longname varchar(100), shortname varchar(100))";
            String createTable8 = "create table Rooms(roomID varchar(20),capacity int,details varchar(100),roomType varchar(5), Constraint comRoom_PK Primary key (roomID),Constraint checkType CHECK (roomType in ('COMP', 'CLASS')))";
            String createTable9 = "create table BookedTimes(roomID varchar(20),startTime timestamp, endTime timestamp,Constraint room_FK Foreign Key (roomID) REFERENCES Rooms(roomID))";
            String createTable10 = "create table REQUESTINPROGRESS (REQUESTID   INTEGER not null constraint REQUESTINPROGRESS_REQUESTID_UINDEX unique, ROOM VARCHAR(200), SUBTYPE VARCHAR(200), DESCRIPTION VARCHAR(200), DATE TIMESTAMP, CHECKBOX INT, TYPE VARCHAR(200) default 'Sanitation', FINISHED_BY VARCHAR(30)  default 'NULL')";
            String createTable11 = "create table users(username varchar(100) primary key not null, accountInt int not null,userPass varchar(100) not null,isLoggedIn int,constraint adminBool check (accountInt = 100 or accountInt = 3 or accountInt = 2 or accountInt = 1 or accountInt = 0),constraint loggedBool check (isLoggedIn = 0 or isLoggedIn = 1))";
            String createTable20 = "create table users(username varchar(100) primary key not null, accountInt int not null,userPass varchar(100) not null,pathtopic varchar(100), constraint adminBool check (accountInt = 100 or accountInt = 3 or accountInt = 2 or accountInt = 1 or accountInt = 0))";
            String createTable13 = "DELETE FROM users";
            String createTable12 = "insert into USERS values ('jeff', 100, '098f6bcd4621d373cade4e832627b4f6','src/resources/People_Pictures/Jeff.jpg'),('staff', 1, '1253208465b1efa876f982d8a9e73eef', '/resources/bwh-logo.png'),('Sam', 100, 'ba0e0cde1bf72c28d435c89a66afc61a','src/resources/People_Pictures/Sam.png')";//"('bridget', 0 '098f6bcd4621d373cade4e832627b4f6', '/resources/People_Pictures/Bridget.jpg'),('wong', 1, '098f6bcd4621d373cade4e832627b4f6', '/resources/People_Pictures/Wong.jpg'), ('sam', 2, '098f6bcd4621d373cade4e832627b4f6', '/resources/People_Pictures/Sam.jpg'),('ken', 100, '098f6bcd4621d373cade4e832627b4f6', '/resources/People_Pictures/Ken.jpg')";
            try {
                Statement stmt1 = conn.createStatement();
                stmt1.executeUpdate(createTable1);
            }catch(Exception e){};
            try {
                Statement stmt8 = conn.createStatement();
                stmt8.executeUpdate(createTable8);
            }catch(Exception e){};
            try {
                Statement stmt9 = conn.createStatement();
                stmt9.executeUpdate(createTable9);
            }catch(Exception e){};
            try {
                Statement stmt10 = conn.createStatement();
                stmt10.executeUpdate(createTable10);
            }catch(Exception e){};
            try {
                Statement stmt20 = conn.createStatement();
                stmt20.executeUpdate(createTable20);
            }catch(Exception e){};
            try {
                Statement stmt13 = conn.createStatement();
                stmt13.executeUpdate(createTable13);
            }catch(Exception e){};
            try {
                Statement stmt12 = conn.createStatement();
                stmt12.executeUpdate(createTable12);
            }catch(Exception e){
                e.printStackTrace();
            };
            conn.close();
        }catch(Exception e){e.printStackTrace();};

        try{
            Connection conn2 = this.getConnection();

            String createTable11 = "create table REQUESTLOG (REQUESTID INTEGER constraint REQUESTLOG_REQUESTID_UINDEX unique, ROOM VARCHAR(100), TYPE VARCHAR(50), SUBTYPE VARCHAR(50), DESCRIPTION VARCHAR(200), CHECKBOX INT, DATE TIMESTAMP, FINISHED_BY VARCHAR(30))";

            String createTable12 = "CREATE TABLE ROOMS (ROOMID VARCHAR(50),CAPACITY INTEGER,DETAILS VARCHAR(200),ROOMTYPE VARCHAR(200));";

            String createTable13 = "CREATE TABLE BOOKEDTIMES (ROOMID VARCHAR(50),STARTTIME TIMESTAMP,ENDTIME TIMESTAMP)";

            String clearRooms = "DELETE FROM ROOMS";

            String populateRooms = "insert into ROOMS values('CR_1', 19, 'TBD', 'COMP'),('CR_2', 17, 'TBD', 'COMP'),('CR_3', 17, 'TBD', 'COMP'),('CR_4', 19, 'TBD', 'CLASS'),('CR_5', 25, 'TBD', 'COMP'),('CR_6', 19, 'TBD', 'CLASS'),('CR_7', 17, 'TBD', 'COMP'),('CR_8', 15, 'TBD', 'CLASS')";
            try{
                Statement stmt11 = conn2.createStatement();
                stmt11.executeUpdate(createTable11);
            }catch(Exception e){};

            try{
                Statement stmt12 = conn2.createStatement();
                stmt12.executeUpdate(createTable11);
            }catch(Exception e){};

            try{
                Statement stmt13 = conn2.createStatement();
                stmt13.executeUpdate(createTable13);
            }catch(Exception e){};

            try{
                Statement stmt15 = conn2.createStatement();
                stmt15.executeUpdate(clearRooms);
            }catch(Exception e){e.printStackTrace();};

            try{
                Statement stmt14 = conn2.createStatement();
                stmt14.executeUpdate(populateRooms);
            }catch(Exception e){e.printStackTrace();};

            conn2.close();

        }catch(Exception e){e.printStackTrace();};

    }

    public void floorTables(){

        String floor1PopQuery = "INSERT INTO Floor1 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = '1'";
        String floor2PopQuery = "INSERT INTO Floor2 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = '2'";
        String floor3PopQuery = "INSERT INTO Floor3 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = '3'";
        String floorL1PopQuery = "INSERT INTO FloorL1 SELECT nodeID, xcoord, ycoord,floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = 'L1'";
        String floorL2PopQuery = "INSERT INTO FloorL2 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = 'L2s'";

        //String floor1PopQuery = "INSERT INTO Floor1 SELECT * FROM node WHERE floor = '1'";
        //String floor2PopQuery = "INSERT INTO Floor2 SELECT * FROM node WHERE floor = '2'";
        //String floor3PopQuery = "INSERT INTO Floor3 SELECT * FROM node WHERE floor = '3'";
        //String floorL1PopQuery = "INSERT INTO FloorL1 SELECT * FROM node WHERE floor = 'L1'";
        //String floorL2PopQuery = "INSERT INTO FloorL2 SELECT * FROM node WHERE floor = 'L2'";
        //String edgePopQuery = "insert into Edge select * from Edge";

        try {
            connect();
            Connection conn = this.getConnection();
            Statement stmt6 = conn.createStatement();
            Statement stmt7 = conn.createStatement();
            Statement stmt8 = conn.createStatement();
            Statement stmt9 = conn.createStatement();
            Statement stmt10 = conn.createStatement();

            try{stmt6.execute(floor1PopQuery);}catch(Exception e){e.printStackTrace();}
            try{stmt7.execute(floor2PopQuery);}catch(Exception e){e.printStackTrace();}
            try{stmt8.execute(floor3PopQuery);}catch(Exception e){e.printStackTrace();}
            try{stmt9.execute(floorL1PopQuery);}catch(Exception e){e.printStackTrace();}
            try{stmt10.execute(floorL2PopQuery);}catch(Exception e){e.printStackTrace();}

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Uh oh");

        }
    }
}
/*
            CREATE TABLE node(
            NODEID varchar(20) PRIMARY KEY NOT NULL,
            XCOORD int,
            YCOORD varchar(2),
            FLOOR int,
            BUILDING varchar(15),
            NODETYPE varchar(4),
            LONGNAME varchar(100),
            SHORTNAME varchar(100)
            );

            create table edge(
            edgeid varchar(21),
            startnode varchar(10),
            endnode varchar(10),
            );

            create table floor1(
            NODEID varchar(20) PRIMARY KEY NOT NULL,
            XCOORD int,
            YCOORD varchar(2),
            FLOOR int,
            BUILDING varchar(15),
            NODETYPE varchar(4),
            LONGNAME varchar(100),
            SHORTNAME varchar(100)
            );

            create table floor2(
            NODEID varchar(20) PRIMARY KEY NOT NULL,
            XCOORD int,
            YCOORD varchar(2),
            FLOOR int,
            BUILDING varchar(15),
            NODETYPE varchar(4),
            LONGNAME varchar(100),
            SHORTNAME varchar(100)
            );

            create table floor3(
            NODEID varchar(20) PRIMARY KEY NOT NULL,
            XCOORD int,
            YCOORD varchar(2),
            FLOOR int,
            BUILDING varchar(15),
            NODETYPE varchar(4),
            LONGNAME varchar(100),
            SHORTNAME varchar(100)
            );

            create table floorL1(
            NODEID varchar(20) PRIMARY KEY NOT NULL,
            XCOORD int,
            YCOORD varchar(2),
            FLOOR int,
            BUILDING varchar(15),
            NODETYPE varchar(4),
            LONGNAME varchar(100),
            SHORTNAME varchar(100)
            );

            create table floorL2(
            NODEID varchar(20) PRIMARY KEY NOT NULL,
            XCOORD int,
            YCOORD varchar(2),
            FLOOR int,
            BUILDING varchar(15),
            NODETYPE varchar(4),
            LONGNAME varchar(100),
            SHORTNAME varchar(100)
            );

            create table Rooms(
            roomID varchar2(20),
            capacity number(3),
            details varchar2(100),
            roomType varchar2(5),
            Constraint comRoom_PK Primary key (roomID),
            Constraint checkType CHECK (roomType in ('COMP', 'CLASS'))
            );

            create table BookedTimes(
            roomID varchar2(20),
            startTime timestamp,
            endTime timestamp,
            Constraint room_FK Foreign Key (roomID) REFERENCES Rooms(roomID)
            );
            create table REQUESTINPROGRESS
(
  REQUESTID   INTEGER not null
    constraint REQUESTINPROGRESS_REQUESTID_UINDEX
      unique,
  ROOM        VARCHAR(200),
  NOTE        VARCHAR(200),
  DATE        TIMESTAMP,
  TYPE        VARCHAR(200) default 'Sanitation',
  FINISHED_BY VARCHAR(30)  default 'NULL'
);

create table REQUESTLOG
(
  REQUESTID   INTEGER
    constraint REQUESTLOG_REQUESTID_UINDEX
      unique,
  ROOM        VARCHAR(100),
  NOTE        VARCHAR(200),
  DATE        TIMESTAMP,
  TYPE        VARCHAR(50),
  FINISHED_BY VARCHAR(30)
);

CREATE TABLE ROOMS (
                     ROOMID VARCHAR(50),
                     CAPACITY INTEGER,
                     DETAILS VARCHAR(200),
                     ROOMTYPE VARCHAR(200)
);

CREATE TABLE BOOKEDTIMES (
                           ROOMID VARCHAR(50),
                           STARTTIME TIMESTAMP,
                           ENDTIME TIMESTAMP
);

            */