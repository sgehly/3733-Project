package app.addDelete;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.sql.*;
import app.AStar.*;

public class DatabaseParser {
    public static void edgeParse() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        File file = new File(DatabaseParser.class.getResource("/resources/edgesv3.csv").getPath());

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
        File file = new File(DatabaseParser.class.getResource("/resources/nodesv3.csv").getPath());
        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;

        try {
            inputStream = new Scanner(file);
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
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Didnt work");
            return;
        }
    }

    public void floorTables(){

        String floor1PopQuery = "INSERT INTO Floor1 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = '1'";
        String floor2PopQuery = "INSERT INTO Floor2 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = '2'";
        String floor3PopQuery = "INSERT INTO Floor3 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = '3'";
        String floorL1PopQuery = "INSERT INTO FloorL1 SELECT nodeID, xcoord, ycoord,floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = 'L1'";
        String floorL2PopQuery = "INSERT INTO FloorL2 SELECT nodeID, xcoord, ycoord, floor, building, NODETYPE, LONGNAME, SHORTNAME FROM node WHERE floor = 'L2'";

        try {
            connect();
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            Statement stmt6 = conn.createStatement();
            Statement stmt7 = conn.createStatement();
            Statement stmt8 = conn.createStatement();
            Statement stmt9 = conn.createStatement();
            Statement stmt10 = conn.createStatement();

            stmt6.execute(floor1PopQuery);
            stmt7.execute(floor2PopQuery);
            stmt8.execute(floor3PopQuery);
            stmt9.execute(floorL1PopQuery);
            stmt10.execute(floorL2PopQuery);

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Uh oh");

        }
    }
}

