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

    /**
     * Constructor for Floor
     */
    public Floor() {
        floorMap = new HashMap<>();
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
        Node newNode = new Node(null, x, y, null, null, null, null, null);
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt = ((Connection) conn).prepareStatement("select * from node where xcoord = ? and ycoord = ?");
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            ResultSet rs =  stmt.executeQuery();

            while(rs.next()) {
                newNode.setId(rs.getString("nodeID"));
                newNode.setFloor(rs.getString("floor"));
                newNode.setBuilding(rs.getString("building"));
                newNode.setNodeType(rs.getString("nodeType"));
                newNode.setLongName(rs.getString("longName"));
                newNode.setShortName(rs.getString("shortName"));
            }

            conn.close();
        }
        catch (Exception e){
                e.printStackTrace();
            }




    }

    /**
     * Removes a node from the graph
     *
     * @param rem
     */
    public void removeNode(Node rem) {

        // check to make sure part of the graph
        if(!floorMap.containsKey(rem.getId())) {
            System.out.println("sorry doesn't exist in map");
        }

        // removes all the edges of the node that you want to remove
        for(Edge e: rem.getEdges()) {
            rem.removeEdge(rem, e.getEdgeID());
        }

        // remove node
        floorMap.remove(rem.getId()); // does this work??

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
        g2d.setStroke(new BasicStroke(5.0f));
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

            int diameter = 2; //Diameter of the circle
            g2d.setColor(Color.YELLOW);
            Shape circle = new Ellipse2D.Double(node.getXCoord() - diameter / 2, node.getYCoord() - diameter / 2, diameter, diameter); //Draw the circles

            g2d.draw(circle);
            //img.setRGB(node.getXCoord(), node.getYCoord(), p); //Set the point of the node to black
        } //Repeat for each node

        //write image
        try {
            f = new File("C:\\Users\\kenne\\Downloads\\PathTestOutput.png");
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
            String floorTable; //which floor table are we inserting into?
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

            String floorTableQuery = "INSERT INTO "+floorNum+"VALUES("+node.getId()+", "+node.getXCoord()+", "+node.getYCoord()+")";
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
