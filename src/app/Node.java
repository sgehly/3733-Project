package app;

import java.util.ArrayList;

public class Node {

    protected class Edge{
        Node start;
        Node end;
        int id;
        int weight;
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

    //getter for a node's edge list
    public ArrayList<Edge> getEdges(){
        return this.edgeList;
    }

    //this function for returning the distance between a node and
    public int getDistance(Node n){

    }
    public void addEdge(Node n, Node m){};
}

