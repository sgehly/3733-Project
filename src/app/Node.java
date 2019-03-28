package app;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private String id;
    private int x;
    private int y;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;
    private List<Edge> edgeList;
    private double f;
    private double h;
    private double g;
    private Node parent;
    private boolean enabled;

    public double getF() {
        return f;
    }

    public String getId() {
        return id;
    }

    public String getFloor() {
        return floor;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node(String id, int x, int y, String floor, String building, String nodeType, String longName, String shortName) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        edgeList = new ArrayList<>();
        this.parent = null;
        this.enabled = true;
    }

    /**
     * Get list of classes edges
     *
     * @return List of edges to this node
     */
    public List<Edge> getEdges(){
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
     * Enable the node
     */
    public void enable() { this.enabled = false;}

    /**
     * Disable the node
     */
    public void disable() { this.enabled = true;}

    /**
     * Get the state of the node
     *
     * @return if the node is enabled
     */
    public boolean isEnabled(){
        return this.enabled;
    }

    /**
     * Add edge to the node
     *
     * @param n - The node that an edge needs to be formed with
     * @param edgeID - The ID of the specific edge
     */
    public void addEdge(Node n, String edgeID){
        this.edgeList.add(new Edge(n, edgeID));
    }
}

