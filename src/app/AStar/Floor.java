package app.AStar;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.*;

public class Floor{
    private Map<String,Node> floorMap;
    private String floorid;

    /**
     * Constructor for Floor
     */
    public Floor(String f) {
        floorMap = new HashMap<>();
        this.floorid = f;
        this.populateFloor();
    }

    //getters and setters for the floor maps
    public Map<String, Node> getFloorMap(){return floorMap;}

    public void setFloorMap(Map<String, Node> floorMap) {this.floorMap = floorMap;}

    /**
     * Adds a node to the graph if the node already exists in the database
     *
     * @param x
     * @param y
     */
    public void addNode(int x, int y) {
        // use breadth first search to find where node should go in tree
        // yeah i really don't understand how to do this
        // find a way to go through of the map

        //this.add(addN);
        //floorMap.put(addN.getId(), addN);

        try{
            Node newNode = new Node(null, x, y, null, null, null, null, null);
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt = conn.prepareStatement("select * from floor" + floorid + " where xcoord = ? and ycoord = ?");
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            ResultSet rs =  stmt.executeQuery();

            newNode.setX(x);
            newNode.setY(y);
            //rs.next();

            newNode.setId(rs.getString("nodeID"));
            newNode.setFloor(rs.getString("floor"));
            newNode.setBuilding(rs.getString("building"));
            newNode.setNodeType(rs.getString("nodeType"));
            newNode.setLongName(rs.getString("longName"));
            newNode.setShortName(rs.getString("shortName"));

            conn.close();
            floorMap.put(newNode.getId(), newNode);
        }
        catch (Exception e){
                e.printStackTrace();
            }
    }

    public void populateFloor(){
        try {
            String cmd = "select * from floor" + floorid;
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(cmd);
            while (set.next()) {
                String id = set.getString("nodeID");
                String building = set.getString("building");
                String type = set.getString("nodeType");
                String longName = set.getString("longName");
                String shortName = set.getString("shortName");
                int x = set.getInt("XCoord");
                int y = set.getInt("YCoord");
                Node n = new Node(id, x, y, floorid, building, type, longName, shortName);
                floorMap.put(id, n);
            }
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            String cmd = "select * from edge";
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(cmd);
            Node n1 = new Node("343", 34,34, "d", "e", "e","e","e");
            Node n2 = new Node("243", 34,34, "d", "e", "e","e","e");
            while (rs.next()) {
                String startingNode = rs.getString("startNode");
                String endingNode = rs.getString("endNode");
                String edgeID = rs.getString("edgeID");
                if(floorMap.containsKey(startingNode) && floorMap.containsKey(endingNode)){
                    n1 = floorMap.get(startingNode);
                    n2 = floorMap.get(endingNode);
                    n1.addEdge(n2, edgeID);
                    n2.addEdge(n1, edgeID);
                }
            }
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    public void addNode(String id, int x, int y, String floor, String building, String type, String longN, String shortN){
        try{
            Node newNode  = new Node(id, x, y, floor, building, type, longN, shortN);
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt = conn.prepareStatement("insert into floor" + floorid + " values(?, ? ,? ,?, ?, ?, ?, ?)");
            stmt.setString(1, id);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setString(4, floor);
            stmt.setString(5, building);
            stmt.setString(6, type);
            stmt.setString(7, longN);
            stmt.setString(8, shortN);

            stmt.execute();

            conn.close();
            floorMap.put(newNode.getId(), newNode);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Removes a node from the graph
     *
     * @param rem
     */
    public void removeNode(Node rem){
        try{
            for(Edge e: rem.getEdges()){
                this.removeEdge(e.getEdgeID());
            }
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt = conn.prepareStatement("delete from floor" + floorid + " where nodeID = ?");
            stmt.setString(1, rem.getId());
            stmt.execute();
            conn.close();
            floorMap.remove(rem.getId());
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void removeEdge(String edgeID){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt = conn.prepareStatement("delete from edge where edgeID = ?");
            stmt.setString(1, edgeID);
            stmt.execute();
            conn.close();
            String delim = "_";
            String[] nodeKeys;
            nodeKeys = edgeID.split(delim);
            Node start = floorMap.get(nodeKeys[0]);
            Node end = floorMap.get(nodeKeys[1]);
            for(Edge e: start.getEdges()){
                if(e.getEdgeID().equals(edgeID)){
                    start.getEdges().remove(e);
                }
            }
            for(Edge e: end.getEdges()){
                if(e.getEdgeID().equals(edgeID)){
                    end.getEdges().remove(e);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void addEdge(String edgeID){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String[] edgeNodes = edgeID.split("_");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt = conn.prepareStatement("insert into edge values(?,?,?)");
            stmt.setString(1, edgeID);
            stmt.setString(2, edgeNodes[0]);
            stmt.setString(3, edgeNodes[1]);
            stmt.execute();
            conn.close();

            Node start = floorMap.get(edgeNodes[0]);
            Node end = floorMap.get(edgeNodes[1]);
            start.addEdge(end, edgeID);
            end.addEdge(start, edgeID);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method will take in the list of Nodes and Produce a transparent image with the nodes and path.
     * This version of the method will the locally store the image, while the other will actually transfer the image itself
     * @param nodeArrayList - A list of nodes that represent a path
     */
    public static void drawPath(List<Node> nodeArrayList) {
        int width = 5000; //The given width and height of the image
        int height = 3400;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        File f = null;
        //Draw lines before points so that points do not get covered up
        Graphics2D g2d = img.createGraphics(); //Create the graphics
        g2d.setColor(Color.BLACK);//We will set line color white to visualize it better
        g2d.setStroke(new BasicStroke(15.0f));
        //We will draw all the lines
        int listSize = nodeArrayList.size();//Get the number of nodes
        for (int i = 0; i < listSize - 1; i++) //For every node except the last one
        {
            Node firstNode = nodeArrayList.get(i);
            Node secondNode = nodeArrayList.get(i + 1);
            g2d.drawLine(firstNode.getXCoord(), firstNode.getYCoord(), secondNode.getXCoord(), secondNode.getYCoord()); //Draw a line from it to the next node
        }
        //use the nodes to create points for each of the nodes (black points)
        for (Node node : nodeArrayList) {
          /*  int a = 255;//(int)(Math.random()*256); //alpha
            int r = 255;//(int)(Math.random()*256); //red
            int g = 255;//(int)(Math.random()*256); //green
            int b = 0;//(int)(Math.random()*256); //blue
            int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel*/

            int diameter = 7; //Diameter of the circle
            g2d.setColor(Color.YELLOW);
            Shape circle = new Ellipse2D.Double(node.getXCoord() - diameter / 2, node.getYCoord() - diameter / 2, diameter, diameter); //Draw the circles

            g2d.draw(circle);
            //img.setRGB(node.getXCoord(), node.getYCoord(), p); //Set the point of the node to black
        } //Repeat for each node

        //write image
        try {
            f = new File("src/resources/maps/PathOutput.png");
            ImageIO.write(img, "png", f); //We will write out a png to my downloads folder
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void addNodeDB(Node node, String floorNum){
        try{
            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Didnt work");
            }
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            Statement stmt1 = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            String floorTable = null; //which floor table are we inserting into?
            if (floorNum.equals("1")){
                floorTable  = "Floor1";
            }
            else if (floorNum.equals("2")){
                floorTable = "Floor2";
            }
            else if (floorNum.equals("3")){
                floorTable = "Floor3";
            }
            else if (floorNum.equals("L1")){
                floorTable = "FloorL1";
            }
            else if (floorNum.equals("L2")){
                floorTable = "FloorL2";
            }
            else{
                System.out.println("That floor does not exist");
            }

            String floorTableQuery = "INSERT INTO "+floorTable+" VALUES("+node.getId()+", "+node.getXCoord()+", "+node.getYCoord()+")";
            String nodeTableQuery = "INSERT INTO Node VALUES("+node.getId()+", "+node.getXCoord()+", "+node.getYCoord()+", "+node.getFloor()+", "+node.getBuilding()+", "+node.getNodeType()+", "+node.getLongName()+", "+node.getShortName()+")";

            stmt1.execute(floorTableQuery);
            stmt2.execute(nodeTableQuery);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Oh no");
        }

    }

}
