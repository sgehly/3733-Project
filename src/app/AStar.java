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
        start.setG(0);
        start.setF(0);
        List<Node> path = new ArrayList<>(); //where the final path will be stored
        PriorityQueue openList = new PriorityQueue(); //prio queue to keep track of lowest f score
        openList.addtoHeap(start);
        HashMap<String, Node> closedList = new HashMap<String, Node>(); //closed list
        while(openList.getHeapSize() != 0){ //while the open list is not empty
            openList.buildHeap(); //bring best node to head of open list
            Node current = openList.getHeap().get(0); //set it to current
            openList.getHeap().remove(0); //remove it from the open list
            closedList.put(current.getId(), current); //add it to the closed list
            if(current.getId().equals(end.getId())){ //if the goal is found, add nodes along path to the path list
                current = end;
                while(!current.getId().equals(start.getId())){ //while the current node isn't the starting node
                    path.add(current); //add it to the final path
                    current = current.getParent(); //go to the parent
                }
                path.add(start); //add the start to the path and return the path
                return path;
            }
            for(Edge e : current.getEdges()){ //for every neighbor of the current node
                Node neighbor = e.getEndNode();
                if(closedList.containsKey(neighbor.getId())){ //if its in the closed list, ignore it
                    continue;
                }
                neighbor.setG(neighbor.getDistance(current) + current.getG()); //set its g, h, f, and parent
                neighbor.setH(neighbor.getDistance(end));
                neighbor.setF(neighbor.getG() + neighbor.getH());
                neighbor.setParent(current);
                for(Node n : openList.getHeap()){
                    if(neighbor == n){ //if its in the open list
                        if(neighbor.getG() >= n.getG()){ //if the cost of the node cannot be lowered, continue
                            continue;
                        }
                        else{ //else if the cost can be lowered
                            n.setParent(current);
                            n.setG(n.getDistance(current) + current.getG());
                            n.setF(n.getG() + n.getH());
                        }
                    }
                }
                if(neighbor.getFloor().equals(end.getFloor())) { //if the neighbor is on the same floor as the end node, add it the open list
                    openList.addtoHeap(neighbor);
                }
            }
        }
        return null;
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
