package edu.wpi.cs3733.d19.teamM.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.sql.*;

import com.gluonhq.charm.down.common.PlatformFactory;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;

public class DatabaseUtils {

    String dbPath = "jdbc:sqlite:";
    private static DatabaseUtils DBUtils;


    synchronized public static DatabaseUtils getDBUtils(){
        if(DBUtils == null){
            DBUtils = new DatabaseUtils();
        }
        return DBUtils;
    }

    public Connection getConnection(){
        try{
            Class.forName("SQLite.JDBCDriver");

            File dir;

            try {
                dir = PlatformFactory.getPlatform().getPrivateStorage();
                File db = new File (dir, "myDB");
                dbPath = dbPath + db.getAbsolutePath();
            } catch (IOException ex) {
                System.out.println("Error " + ex);
            }

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
        InputStream file = Main.getResource("/resources/nodesv5.csv");
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
       // try {
            Connection conn = this.getConnection();
            String createTable1 = "CREATE TABLE node(NODEID varchar(20) PRIMARY KEY NOT NULL, XCOORD int, ycoord int, floor varchar(2), building varchar(15), nodetype varchar(4), longname varchar(100), shortname varchar(100))";
            String createTable14 = "CREATE TABLE edge(edgeid varchar(21) PRIMARY KEY NOT NULL, startnode varchar(10), endnode varchar(10))";
            String createTable8 = "create table Rooms(roomID varchar(20),capacity int,details varchar(100),roomType varchar(5), Constraint comRoom_PK Primary key (roomID),Constraint checkType CHECK (roomType in ('COMP', 'CLASS')))";
            String createTable9 = "create table BookedTimes(roomID varchar(20),startTime timestamp, endTime timestamp,Constraint room_FK Foreign Key (roomID) REFERENCES Rooms(roomID))";
            String createTable10 = "create table REQUESTINPROGRESS (REQUESTID   INTEGER not null constraint REQUESTINPROGRESS_REQUESTID_UINDEX unique, ROOM VARCHAR(200), SUBTYPE VARCHAR(200), DESCRIPTION VARCHAR(200), DATE TIMESTAMP, CHECKBOX INT, TYPE VARCHAR(200) default 'Sanitation', FINISHED_BY VARCHAR(30)  default 'NULL')";
            //String createTable11 = "create table users(username varchar(100) primary key not null, accountInt int not null,userPass varchar(100) not null,isLoggedIn int,constraint adminBool check (accountInt = 100 or accountInt = 3 or accountInt = 2 or accountInt = 1 or accountInt = 0),constraint loggedBool check (isLoggedIn = 0 or isLoggedIn = 1))";
            String createTable20 = "create table users(username varchar(100) primary key not null, accountInt int not null,userPass varchar(100) not null,pathtopic varchar(100), constraint adminBool check (accountInt = 100 or accountInt = 11 or accountInt = 10 or accountInt = 9 or accountInt = 8 or accountInt = 7 or accountInt = 6 or accountInt = 5 or accountInt = 4 or accountInt = 3 or accountInt = 2 or accountInt = 1 or accountInt = 0))";
            String createTable13 = "DELETE FROM users";
            String createTable12 = "insert into USERS values ('jeff', 100, '098f6bcd4621d373cade4e832627b4f6','src/resources/People_Pictures/Jeff.jpg'),('staff', 100, '1253208465b1efa876f982d8a9e73eef', '/resources/bwh-logo.png'),('Sam', 100, 'ba0e0cde1bf72c28d435c89a66afc61a','src/resources/People_Pictures/Sam.png'), ('caitlin', 1, '098f6bcd4621d373cade4e832627b4f6', 'src/resources/People_Pictures/Caitlin.jpg'), ('ken', 1, '098f6bcd4621d373cade4e832627b4f6', 'src/resources/People_Pictures/Ken.jpg'), ('connor', 1, '098f6bcd4621d373cade4e832627b4f6', 'src/resources/People_Pictures/Connor.jpg'), ('jack', 2, '098f6bcd4621d373cade4e832627b4f6', 'src/resources/People_Pictures/Jack.jpg'), ('bridget', 2, '098f6bcd4621d373cade4e832627b4f6', 'src/resources/People_Pictures/Bridget.jpg'), ('joe', 2, '098f6bcd4621d373cade4e832627b4f6', 'src/resources/People_Pictures/Joe.jpg')";
//            String insert1 = "insert into users (username, accountant, userpass) values ";
//            String insert2 = "insert into users (username, accountant, userpass) values ";
//            String insert3 = "insert into users (username, accountant, userpass) values ";
//            String insert4 = "insert into users (username, accountant, userpass) values ";
//            String insert5 = "insert into users (username, accountant, userpass) values ";
//            String insert6 = "insert into users (username, accountant, userpass) values ";
            try {
                Statement stmt1 = conn.createStatement();
                stmt1.executeUpdate(createTable1);
                System.out.println("Node created!");
            }catch(Exception e){
                e.printStackTrace();
            };
            try{
                Statement stmt14 = conn.createStatement();
                stmt14.executeUpdate(createTable14);
                System.out.println("Edge created!");
            }catch(Exception e){e.printStackTrace();}
            try {
                Statement stmt8 = conn.createStatement();
                stmt8.executeUpdate(createTable8);
                System.out.println("Rooms created!");
            }catch(Exception e){e.printStackTrace();};
            try {
                Statement stmt9 = conn.createStatement();
                stmt9.executeUpdate(createTable9);
                System.out.println("BookedTimes created!");
            }catch(Exception e){e.printStackTrace();};
            try {
                Statement stmt10 = conn.createStatement();
                stmt10.executeUpdate(createTable10);
                System.out.println("RIP created!");
            }catch(Exception e){
                e.printStackTrace();
            };
            try {
                Statement stmt20 = conn.createStatement();
                stmt20.executeUpdate(createTable20);
                System.out.println("Users created!");
            }catch(Exception e){
                e.printStackTrace();
            };
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
//            try {
//                Statement stmt30 = conn.createStatement();
//                stmt30.executeUpdate(insert1);
//            }catch(Exception e){};
//            try {
//                Statement stmt31 = conn.createStatement();
//                stmt31.executeUpdate(insert2);
//            }catch(Exception e){};
//            try {
//                Statement stmt32 = conn.createStatement();
//                stmt32.executeUpdate(insert3);
//            }catch(Exception e){};
//            try {
//                Statement stmt33 = conn.createStatement();
//                stmt33.executeUpdate(insert4);
//            }catch(Exception e){};
//            try {
//                Statement stmt34 = conn.createStatement();
//                stmt34.executeUpdate(insert5);
//            }catch(Exception e){};
//            try {
//                Statement stmt35 = conn.createStatement();
//                stmt35.executeUpdate(insert6);
//            }catch(Exception e){};
            try{conn.close();}catch(Exception e){e.printStackTrace();}
        //}catch(Exception e){e.printStackTrace();};

        //try{
            Connection conn2 = this.getConnection();

            String createTable11 = "create table REQUESTLOG (REQUESTID INTEGER constraint REQUESTLOG_REQUESTID_UINDEX unique, ROOM VARCHAR(100), TYPE VARCHAR(50), SUBTYPE VARCHAR(50), DESCRIPTION VARCHAR(200), CHECKBOX INT, DATE TIMESTAMP, FINISHED_BY VARCHAR(30))";

            //String createTable12 = "CREATE TABLE ROOMS (ROOMID VARCHAR(50),CAPACITY INTEGER,DETAILS VARCHAR(200),ROOMTYPE VARCHAR(200));";

            String createTable131 = "CREATE TABLE BOOKEDTIMES (ROOMID VARCHAR(50),STARTTIME TIMESTAMP,ENDTIME TIMESTAMP)";

           // String clearRooms = "DELETE FROM ROOMS";

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
                Statement stmt131 = conn2.createStatement();
                stmt131.executeUpdate(createTable131);
            }catch(Exception e){};

//            try{
//                Statement stmt15 = conn2.createStatement();
//                stmt15.executeUpdate(clearRooms);
//            }catch(Exception e){e.printStackTrace();};

            try{
                Statement stmt14 = conn2.createStatement();
                stmt14.executeUpdate(populateRooms);
            }catch(Exception e){e.printStackTrace();};

            try{conn2.close();}catch(Exception e){e.printStackTrace();}

        //}catch(Exception e){e.printStackTrace();};

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

            String createTable2 = "CREATE TABLE edge(edgeid varchar(21) PRIMARY KEY NOT NULL, startnode varchar(10), endnode varchar(10))";
            String createTable3 = "create table Rooms(roomID varchar(20),capacity int,details varchar(100),roomType varchar(5), Constraint comRoom_PK Primary key (roomID),Constraint checkType CHECK (roomType in ('COMP', 'CLASS')))";
            String createTable4 = "create table BookedTimes(roomID varchar(20),startTime timestamp, endTime timestamp,Constraint room_FK Foreign Key (roomID) REFERENCES Rooms(roomID))";
            String createTable5 = "create table REQUESTINPROGRESS (REQUESTID INTEGER not null constraint REQUESTINPROGRESS_REQUESTID_UINDEX unique, ROOM VARCHAR(200), SUBTYPE VARCHAR(200), DESCRIPTION VARCHAR(200), DATE TIMESTAMP, CHECKBOX INT, TYPE VARCHAR(200) default 'Sanitation', FINISHED_BY VARCHAR(30)  default 'NULL')";
            String createTable6 = "create table users(username varchar(100) primary key not null, isSan int, isInterp int, isIT int, isAV int, isGift int, isFlor int, isInt int, isExt int, isRel int, isSec int, isPer int, isLab int, accountInt int, userPass varchar(100) not null,pathtopic varchar(100), phoneEmail varchar(100), constraint adminBool check(accountInt = 100 or accountInt = 0), constraint sanBool check(isSan = 1 or isSan = 0), constraint interpBool check(isInterp = 1 or isInterp = 0), constraint ITBool check(isIT = 1 or isIT = 0), constraint AVBool check(isAV = 1 or isAV = 0), constraint giftBool check(isGift = 1 or isGift = 0), constraint florBool check(isFlor = 1 or isFlor = 0), constraint intBool check(isInt = 1 or isInt = 0), constraint extBool check(isExt = 1 or isExt = 0), constraint RelBool check(isRel = 1 or isRel = 0), constraint secBool check(isSec = 1 or isSec = 0), constraint perBool check(isPer = 1 or isPer = 0), constraint labBool check(isLab = 1 or isLab = 0))";
            String insert1 = "insert into USERS values ('jeff', 1,1,0,0,0,0,0,0,0,0,0,0,100, '098f6bcd4621d373cade4e832627b4f6','Jeff.jpg', '7742809552'),('staff', 0,0,1,1,0,0,0,0,0,0,0,0,100, '1253208465b1efa876f982d8a9e73eef', 'bwh-logo.png', null),('Sam', 0,0,0,0,1,1,0,0,0,0,0,0,100, 'ba0e0cde1bf72c28d435c89a66afc61a','Sam.png', '5083178724'), ('caitlin', 0,0,0,0,0,0,1,1,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'Caitlin.jpg', null), ('ken', 0,0,0,0,0,0,0,0,1,1,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'Ken.jpg', null), ('connor', 0,0,0,0,0,0,0,0,0,1,1,1,100, '098f6bcd4621d373cade4e832627b4f6', 'Connor.jpg', '7742930250'), ('jack', 1,0,1,0,0,0,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'Jack.jpg', null), ('bridget', 0,1,0,1,0,0,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'Bridget.jpg', null), ('joe', 0,0,0,0,1,0,1,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'Joe.jpg', null)";
            String insert2 = "insert into USERS values ('vishnu', 0,0,0,0,0,1,0,1,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('phil', 0,0,0,0,0,0,0,0,1,0,1,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('rose', 0,0,0,0,0,0,0,0,0,1,0,1,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('jessica', 1,0,0,1,0,0,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('beatrice', 0,1,0,0,1,0,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('eugene', 0,0,1,0,0,1,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('maria', 0,0,0,1,0,0,1,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('clark', 0,0,0,0,1,0,0,1,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('melissa', 0,0,0,0,0,1,0,0,1,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('amanda', 0,0,0,0,0,0,1,0,0,1,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('austin', 0,0,0,0,0,0,0,1,0,0,1,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('sarah', 0,0,0,0,0,0,0,0,1,0,0,1,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('ashley', 1,1,1,0,0,0,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('dean', 0,0,0,1,1,1,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('ingrid', 0,0,0,0,0,0,1,1,1,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('harry', 0,0,0,0,0,0,0,0,0,1,1,1,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('niall', 1,0,1,0,1,0,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('louis', 0,1,0,1,0,1,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('liam', 0,0,0,0,0,0,1,0,1,0,1,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('zayn', 0,0,0,0,0,0,0,1,0,1,0,1,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('wilson', 0,1,0,1,1,0,0,0,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('jake', 0,0,0,0,0,1,0,1,1,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('courtney', 0,0,0,0,0,1,1,0,0,0,1,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('nicole', 0,1,1,0,0,0,0,1,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('michelle', 0,0,1,1,0,0,0,0,0,0,0,1,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('adele', 0,0,0,0,0,1,0,1,0,0,1,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('taylor', 1,0,0,0,1,0,0,0,0,1,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('faye', 0,1,0,0,0,0,1,1,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('hannah', 0,0,0,0,0,0,0,0,1,1,1,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null),('kathryn', 0,0,0,1,1,0,0,1,0,0,0,0,0, '098f6bcd4621d373cade4e832627b4f6', 'bwh-logo.png', null)";
            String createTable7 = "create table REQUESTLOG (REQUESTID INTEGER constraint REQUESTLOG_REQUESTID_UINDEX unique, ROOM VARCHAR(100), TYPE VARCHAR(50), SUBTYPE VARCHAR(50), DESCRIPTION VARCHAR(200), CHECKBOX INT, DATE TIMESTAMP, FINISHED_BY VARCHAR(30))";
            String createTable8 = "CREATE TABLE BOOKEDTIMES (ROOMID VARCHAR(50),STARTTIME TIMESTAMP,ENDTIME TIMESTAMP)";
            String delete2 = "DELETE FROM ROOMS";
            String insert3 = "insert into ROOMS values('CR_1', 19, 'TBD', 'COMP'),('CR_2', 17, 'TBD', 'COMP'),('CR_3', 17, 'TBD', 'COMP'),('CR_4', 19, 'TBD', 'CLASS'),('CR_5', 25, 'TBD', 'COMP'),('CR_6', 19, 'TBD', 'CLASS'),('CR_7', 17, 'TBD', 'COMP'),('CR_8', 15, 'TBD', 'CLASS')";

            //Statement stmt1 = conn.createStatement();
           // stmt1.executeUpdate(createTable1);
            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate(createTable2);
            Statement stmt3 = conn.createStatement();
            stmt3.executeUpdate(createTable3);
            Statement stmt4 = conn.createStatement();
            stmt4.executeUpdate(createTable4);
            Statement stmt5 = conn.createStatement();
            stmt5.executeUpdate(createTable5);

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