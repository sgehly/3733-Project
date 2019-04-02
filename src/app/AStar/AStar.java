/*
The #Algo Team yip yip
 */
package app.AStar;

import java.util.*;

/**
 * This class is to run the A* algorithm and to also handle any other methods associated with the path-finding
 */
public class AStar {
    /**
     * some stuff
     * @param start
     * @param destType
     * @param map
     * @return
     */
    public List<Node> findPresetPath(Node start, String destType, Map<String, Node> map){
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
    public List<Node> findPath(Node start, Node end) {
        start.setG(0);
        start.setF(0);
        List<Node> path = new ArrayList<>(); //where the final path will be stored
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
                if(closedList.containsKey(neighbor.getId())){ //if its in the closed list, ignore it
                    continue;
                }
                neighbor.setG(neighbor.getDistance(current) + current.getG()); //set its g, h, f, and parent
                neighbor.setH(neighbor.getDistance(end));
                neighbor.setF(neighbor.getG() + neighbor.getH());
                neighbor.setParent(current);
                for(Node n : openList.getQueue()){
                    if(neighbor == n){ //if its in the open list
                        if(neighbor.getG() < n.getG()){ //if the cost of the node cannot be lowered, continue
                            n.setParent(current);
                            n.setG(n.getDistance(current) + current.getG());
                            n.setF(n.getG() + n.getH());
                        }
                    }
                }
                if(neighbor.getFloor().equals(end.getFloor()) && neighbor.isEnabled()) { //if the neighbor is on the same floor as the end node, add it the open list
                    // also checks for if the node is enabled
                    openList.addToQueue(neighbor);
                }
            }
        }
        return null;
    }

}
