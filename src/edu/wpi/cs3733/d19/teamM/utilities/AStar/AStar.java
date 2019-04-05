/*
The #Algo Team yip yip
 */
package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.*;

/**
 * This class is to run the A* algorithm and to also handle any other methods associated with the path-finding
 */
public class AStar implements Searchable {
    /**
     * some stuff
     * @param start
     * @param destType
     * @param map
     * @return
     */
    public Path findPresetPath(Node start, String destType, Map<String, Node> map){
        Iterator it = map.entrySet().iterator();
        Node closest = null;
        double lowestCost = 500000;
        while(it.hasNext()) {
            Map.Entry set = (Map.Entry) it.next();
            Node n = (Node) set.getValue();
            if (n.getNodeType().equals(destType)) {
                if(n.getDistance(start) < lowestCost){
                    closest = n;
                    lowestCost = n.getDistance(start);
                }
            }
        }
        if(closest != null){
            return findPath(start, closest);
        }
        else{
            return null;
        }
    }

    /**
     * Find the path between a start and end node
     * @param start - The node to start the path
     * @param end - The node to end the path
     * @return A list of nodes that represent a path. path[0] is the end node and path[n] is the start node where n
     *         is path.size() - 1
     */
    public Path findPath(Node start, Node end) {
        start.setG(0);
        start.setF(0);
        Path path = new Path(); //where the final path will be stored
        PriorityQueue<Node> openList = new PriorityQueue<>(); //priority queue to keep track of lowest f score
        openList.addToQueue(start);
        HashMap<String, Node> closedList = new HashMap<String, Node>(); //closed list
        while(openList.getLength() != 0){ //while the open list is not empty
            Node current = openList.pop(); //set it to current
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
                if (!closedList.containsKey(neighbor.getId())) { //if its in the closed list, ignore it
                    neighbor.setG(neighbor.getDistance(current) + current.getG()); //set its g, h, f, and parent
                    neighbor.setP(getDeltaFloor(neighbor, end) * 1000);
                    neighbor.setH(neighbor.getDistance(end));
                    neighbor.setF(neighbor.getG() + neighbor.getH() + neighbor.getP());
                    neighbor.setParent(current);
                    for (Node n : openList.getQueue()) {
                        if (neighbor == n) { //if its in the open list
                            if (neighbor.getG() < n.getG()) { //if the cost of the node cannot be lowered, continue
                                n.setP(getDeltaFloor(n, end) * 1000);
                                n.setParent(current);
                                n.setG(n.getDistance(current) + current.getG());
                                n.setF(n.getG() + n.getH() + n.getP());
                            }
                        }
                    }
                }
                if(!closedList.containsKey(neighbor.getId()) && neighbor.isEnabled()) { //if the neighbor is on the same floor as the end node, add it the open list
                    openList.addToQueue(neighbor);
                }
            }
        }
        return null;
    }

    private double getDeltaFloor(Node n1, Node n2){
        /*
        L2 - 0
        L1 - 1
        G  - 2
        1  - 3
        2  - 4
        3  - 5
        */
        double floor1 = getFloorValue(n1.getFloor());
        double floor2 = getFloorValue(n2.getFloor());
        return Math.abs(floor1 - floor2);
    }

    private double getFloorValue(String f){
        if (f.equals("L2")) return  0;
        else if (f.equals("L1")) return  1;
        else if (f.equals("G")) return  2;
        else if (f.equals("1")) return  3;
        else if (f.equals("2")) return  4;
        else if (f.equals("3")) return  5;
        return -1;
    }

}
