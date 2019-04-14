/*
The #Algo Team yip yip
 */
package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.*;

/**
 * This class is to run the A* algorithm and to also handle any other methods associated with the path-finding
 */
public class AStar extends SearchAlgorithm{
    /**
     * Find the path between a start and end node
     * @param start - The node to start the path
     * @param end - The node to end the path
     * @return A list of nodes that represent a path. path[0] is the end node and path[n] is the start node where n
     *         is path.size() - 1
     */
    @Override
    public Path findPath(Node start, Node end) {
        start.setG(0);
        start.setF(0);
        System.out.println("Start node: " + start.getId() + " End node:" + end.getId());
        System.out.println("Start node's first edge is  " + start.getEdges().get(0));
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
                    neighbor.setP(getDeltaFloor(neighbor, end) * 500);
                    neighbor.setB(getDeltaBuilding(neighbor, end) * 1000);
                    neighbor.setH(neighbor.getDistance(end));
                    neighbor.setF(neighbor.getG() + neighbor.getH() + neighbor.getP() + neighbor.getB());
                    neighbor.setParent(current);
                    for (Node n : openList.getQueue()) {
                        if (neighbor == n) { //if its in the open list
                            if (neighbor.getG() < n.getG()) { //if the cost of the node cannot be lowered, continue
                                n.setParent(current);
                                setHeuristics(n, end);
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



}
