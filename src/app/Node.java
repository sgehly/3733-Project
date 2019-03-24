package app;

import java.util.ArrayList;

public class Node {

    protected class Edge{
        Node end;
        int id;
        int weight;

        protected Edge (Node end) {
            this.end = end;
        }
    }

    private String id;
    private int x;
    private int y;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;
    private ArrayList<Edge> edgeList;

    public Node(String id, int x, int y, String floor, String building, String nodeType, String longName, String shortName) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
    }

    /**
     * Get list of classes edges
     *
     * @return List of edges to this node
     */
    public ArrayList<Edge> getEdges(){
        return this.edgeList;
    }

    /**
     * Get the X coordinate of this node
     *
     * @return The node's x coordinate
     */
    public int getXCoord() {
        return this.x;
    }

    /**
     * Get the Y coordinate of this node
     *
     * @return The node's y coordinate
     */
    public int getYCoord() {
        return this.y;
    }

    /**
     * The Distance between this node and another node
     *
     * @param n - The node to find the distance between
     * @return The distance in pixels
     */
    public double getDistance(Node n){

        // x-coordinate values
        int x1 = this.getXCoord();
        int x2 = n.getXCoord();

        // y-coordinate values
        int y1 = this.getYCoord();
        int y2 = n.getYCoord();

        // differences between coordinates
        int xDiff = x1 - x2;
        int yDiff = y1 - y2;

        // Math.pow is being mean to me so I just multiplied them together the long way
        int xSqr = xDiff * xDiff;
        int ySqr = yDiff * yDiff;

        return Math.sqrt(xSqr + ySqr); // returns distance between nodes

    }

    /**
     * Add edge to the node
     *
     * @param n - The node that an edge needs to be formed with
     */
    public void addEdge(Node n){
        this.edgeList.add(new Edge(n));
    }
}

