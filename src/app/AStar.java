/*
The #Algo Team yip yip
 */
package app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is to run the A* algorithm and to also handle any other methods associated with the path-finding
 */
public class AStar {

    /**
     * This method takes in the final list of nodes in the A* path and returns an array made of two arraylists, where one has nodes and the other has edges.
     * @param nodeArrayList: The list of the final nodes in the path
     * @return ArrayList[]: This will have 2 elements:
     *                                                 The first element is the Arraylist of Nodes
     *                                                 The second element is the Arraylist of Edges
     *                                                 The path should be read by taking the first Node from the first ArrayList, then the first Edge for the second arrayList, etc.
     *                                                 There should be one more node than edge
     */
    public ArrayList[] getNodesAndEdges(ArrayList<Node> nodeArrayList)
    {
        ArrayList[] finalPath = new ArrayList[2]; //Create the arrayList array
        ArrayList<Node> finalNodes = nodeArrayList; //Get final nodes
        ArrayList<Edge> finalEdges = new ArrayList<Edge>(); //Create arraylist of edges
        int nodeListLen = finalNodes.size(); //Get size of node list

        for(int i = 0; i<nodeListLen-1;i++)//Loop through till we reach the second to last node and check the edge that connects it to the next node and add it to the edge list
        {
            //Get first node, get all edges, and see which one is also in second node (only one should match)
            Node firstNode = finalNodes.get(i);
            Node secondNode = finalNodes.get(i+1);
            List<Edge> firstNodeEdges = new ArrayList<Edge>();
            List<Edge> secondNodeEdges = new ArrayList<Edge>();

            firstNodeEdges = firstNode.getEdges();//Add firstNode edges to firstNodeEdges
            secondNodeEdges = secondNode.getEdges(); //Add secondNode edges to secondNodeEdges

            for(Edge anEdge : firstNodeEdges) //For each of the edges of the firstNodes
            {
                if(secondNodeEdges.contains(anEdge)) //If it is in the secondList
                {
                    finalEdges.add(anEdge);//Add it to our list of finalEdges and exit
                    break;
                }
            }
        }

        //Add both arrayLists to the array of arrayLists and return to the user
        finalPath[0] = finalNodes;
        finalPath[1] = finalEdges;
        return finalPath;
    }

    //Actual A* algorithm
    public List<Node> findPath(Node start, Node end) {
        double cost = 0;
        start.setG(0);
        start.setF(0);
        PriorityQueue openList = new PriorityQueue(); //prio queue to keep track of lowest f score
        openList.addtoHeap(start);
        HashMap<String, Node> closedList = new HashMap<String, Node>();
        while (!openList.getHeap().get(0).getId().equals(end.getId())) { //while the node with the lowest f score isn't the goal
            openList.buildHeap(); //build the heap again
            Node current = openList.getHeap().get(0); //get the current lowest f score node
            openList.getHeap().remove(0); //remove it from the open list
            closedList.put(current.getId(), current); //put it in the closed list
            for (Edge e : current.getEdges()) { //for every single neighbor of that node
                Node neighbor = e.getEndNode();
                cost = current.getG() + neighbor.getDistance(current);
                if (openList.findNode(neighbor) && cost < neighbor.getG()) { //if the node is already on the open list, but its cost can be lowered
                    for (Node n : openList.getHeap()) {
                        if (n.getId().equals(neighbor.getId())) {
                            int index = openList.getHeap().indexOf(n); //find it in the open list and record its index
                            openList.getHeap().remove(index); //remove it from the open list
                        }
                    }
                }
                if (closedList.containsKey(neighbor.getId()) && cost < neighbor.getG()) { //if its on the closed list and its cost can be lowered
                    closedList.remove(neighbor.getId()); //remove it from the closed list
                }
                if (!openList.findNode(neighbor) && !closedList.containsKey(neighbor.getId())) { //if its not on either list
                    if (neighbor.getFloor().equals(end.getFloor())) { //if it is on the same floor as the destination node
                        neighbor.setG(cost); //set its g, h, f, and parent
                        openList.addtoHeap(neighbor);
                        neighbor.setH(neighbor.getDistance(end));
                        neighbor.setF(neighbor.getG() + neighbor.getH());
                        neighbor.setParent(current);
                    }
                }
            }
        }
        List<Node> path = new ArrayList<>(); //reconstruct and return the completed path
        Node current = end;
        while (!current.getId().equals(start.getId())) {
            path.add(current);
            current = current.getParent();
        }
        path.add(start);
        return path;
    }

    /**
     * This method will take in the list of Nodes and Produce a transparent image with the nodes and path.
     * This version of the method will the locally store the image, while the other will actually transfer the image itself
     * @param nodeArrayList
     */
    public static void drawPath(List<Node> nodeArrayList)
    {
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
        for(int i = 0; i<listSize-1;i++) //For every node except the last one
        {
            Node firstNode = nodeArrayList.get(i);
            Node secondNode = nodeArrayList.get(i+1);
            g2d.drawLine(firstNode.getXCoord(),firstNode.getYCoord(),secondNode.getXCoord(),secondNode.getYCoord()); //Draw a line from it to the next node
        }
        //use the nodes to create points for each of the nodes (black points)
        for(Node node : nodeArrayList)
        {
          /*  int a = 255;//(int)(Math.random()*256); //alpha
            int r = 255;//(int)(Math.random()*256); //red
            int g = 255;//(int)(Math.random()*256); //green
            int b = 0;//(int)(Math.random()*256); //blue
            int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel*/

          int diameter = 2; //Diameter of the circle
          g2d.setColor(Color.YELLOW);
          Shape circle = new Ellipse2D.Double(node.getXCoord()-diameter/2,node.getYCoord()-diameter/2,diameter,diameter); //Draw the circles

          g2d.draw(circle);
            //img.setRGB(node.getXCoord(), node.getYCoord(), p); //Set the point of the node to black
        } //Repeat for each node

        //write image
        try{
            f = new File("C:\\Users\\kenne\\Downloads\\PathTestOutput.png");
            ImageIO.write(img, "png", f); //We will write out a png to my downloads folder
        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }
}
