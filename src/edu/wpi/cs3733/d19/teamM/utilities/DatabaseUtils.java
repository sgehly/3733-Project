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

public class DatabaseUtils {
    public static void edgeParse() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        InputStream file = Main.getResource("/resources/edgesv3.csv");

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
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
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

    public static void nodeParse(){
        InputStream file = Main.getResource("/resources/nodesv3.csv");
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
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
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

        try {Class.forName("org.apache.derby.jdbc.EmbeddedDriver");}catch(Exception e){e.printStackTrace();};
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            String createTable1 = "CREATE TABLE node(NODEID varchar(20) PRIMARY KEY NOT NULL, XCOORD int, ycoord int, floor varchar(2), building varchar(15), nodetype varchar(4), longname varchar(100), shortname varchar(100))";
            String createTable2 = "create table edge(edgeid varchar(21) primary key, startnode varchar(10), endnode varchar(10))";
            String createTable3 = "CREATE TABLE floor1(NODEID varchar(20) PRIMARY KEY NOT NULL, XCOORD int, ycoord int, floor varchar(2), building varchar(15), nodetype varchar(4), longname varchar(100), shortname varchar(100))";
            String createTable4 = "CREATE TABLE floor2(NODEID varchar(20) PRIMARY KEY NOT NULL, XCOORD int, ycoord int, floor varchar(2), building varchar(15), nodetype varchar(4), longname varchar(100), shortname varchar(100))";
            String createTable5 = "CREATE TABLE floor3(NODEID varchar(20) PRIMARY KEY NOT NULL, XCOORD int, ycoord int, floor varchar(2), building varchar(15), nodetype varchar(4), longname varchar(100), shortname varchar(100))";
            String createTable6 = "CREATE TABLE floorL1(NODEID varchar(20) PRIMARY KEY NOT NULL, XCOORD int, ycoord int, floor varchar(2), building varchar(15), nodetype varchar(4), longname varchar(100), shortname varchar(100))";
            String createTable7 = "CREATE TABLE floorL2(NODEID varchar(20) PRIMARY KEY NOT NULL, XCOORD int, ycoord int, floor varchar(2), building varchar(15), nodetype varchar(4), longname varchar(100), shortname varchar(100))";
            String createTable8 = "create table Rooms(roomID varchar(20),capacity int,details varchar(100),roomType varchar(5), Constraint comRoom_PK Primary key (roomID),Constraint checkType CHECK (roomType in ('COMP', 'CLASS')))";
            String createTable9 = "create table BookedTimes(roomID varchar(20),startTime timestamp, endTime timestamp,Constraint room_FK Foreign Key (roomID) REFERENCES Rooms(roomID))";
            String createTable10 = "create table REQUESTINPROGRESS (REQUESTID   INTEGER not null constraint REQUESTINPROGRESS_REQUESTID_UINDEX unique, ROOM VARCHAR(200), NOTE VARCHAR(200), DATE TIMESTAMP, TYPE VARCHAR(200) default 'Sanitation', FINISHED_BY VARCHAR(30)  default 'NULL')";
            try {
                Statement stmt1 = conn.createStatement();
                stmt1.executeUpdate(createTable1);
            }catch(Exception e){};
            try {
                Statement stmt2 = conn.createStatement();
                stmt2.executeUpdate(createTable2);
            }catch(Exception e){};
            try {
                Statement stmt3 = conn.createStatement();
                stmt3.executeUpdate(createTable3);
            }catch(Exception e){};
            try {
                Statement stmt4 = conn.createStatement();
                stmt4.executeUpdate(createTable4);
            }catch(Exception e){};
            try {
                Statement stmt5 = conn.createStatement();
                stmt5.executeUpdate(createTable5);
            }catch(Exception e){};
            try {
                Statement stmt6 = conn.createStatement();
                stmt6.executeUpdate(createTable6);
            }catch(Exception e){};
            try {
                Statement stmt7 = conn.createStatement();
                stmt7.executeUpdate(createTable7);
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

            conn.close();
        }catch(Exception e){e.printStackTrace();};

        try {Class.forName("org.apache.derby.jdbc.EmbeddedDriver");}catch(Exception e){e.printStackTrace();};
        try{
            Connection conn2 = DriverManager.getConnection("jdbc:derby:myDB;create=true");

            String createTable11 = "create table REQUESTLOG (REQUESTID INTEGER constraint REQUESTLOG_REQUESTID_UINDEX unique, ROOM VARCHAR(100), NOTE VARCHAR(200),DATE TIMESTAMP, TYPE VARCHAR(50),FINISHED_BY VARCHAR(30))";

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
        String floorL2PopQuery = "INSERT INTO FloorL2 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = 'L2'";

        //String floor1PopQuery = "INSERT INTO Floor1 SELECT * FROM node WHERE floor = '1'";
        //String floor2PopQuery = "INSERT INTO Floor2 SELECT * FROM node WHERE floor = '2'";
        //String floor3PopQuery = "INSERT INTO Floor3 SELECT * FROM node WHERE floor = '3'";
        //String floorL1PopQuery = "INSERT INTO FloorL1 SELECT * FROM node WHERE floor = 'L1'";
        //String floorL2PopQuery = "INSERT INTO FloorL2 SELECT * FROM node WHERE floor = 'L2'";
        //String edgePopQuery = "insert into Edge select * from Edge";

        try {
            connect();
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            Statement stmt6 = conn.createStatement();
            Statement stmt7 = conn.createStatement();
            Statement stmt8 = conn.createStatement();
            Statement stmt9 = conn.createStatement();
            Statement stmt10 = conn.createStatement();

            stmt6.execute(floor1PopQuery);
//            stmt1.execute(floor1TableQuery);
//            stmt2.execute(floor2TableQuery);
//            stmt3.execute(floor3TableQuery);
//            stmt4.execute(floorL1TableQuery);
//            stmt5.execute(floorL2TableQuery);
            //stmt6.execute(floor1PopQuery);
            //stmt7.execute(floor2PopQuery);
            //stmt8.execute(floor3PopQuery);
            //stmt9.execute(floorL1PopQuery);
            //stmt10.execute(floorL2PopQuery);
            //stmt11.execute(edgeTableQuery);
            //stmt12.execute(edgePopQuery);

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