package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.HashMap;
import java.util.Map;

/**
 * Breadth first search, doesn't account for path weights
 */
public class BFS implements Searchable {

    private PriorityQueue<Node> queue;
    private Map<String, Node> visited;

    /**
     * Consturctor for Bredth first search
     */
    public BFS(){
        queue = new PriorityQueue<Node>();
        visited = new HashMap<>();
    }

    /**
     * Find the path between two nodes
     * @param start The start of the path
     * @param end The end of the path
     * @return A path object
     */
    public Path findPath(Node start, Node end){
        Node cur = start;
        Node eN;

        queue.addToQueue(start);
        while (queue.getLength() > 0){
            for (Edge e : cur.getEdgeList()){
                eN = e.getEndNode();
                if (!beenVisited(eN)){ // if hasn't been visited
                    eN.setParent(cur); // set parent to current
                    if (eN.getId().equals(end.getId())) return tracePath(start, end);
                    queue.addToQueue(eN); // add edge end node to queue
                }
            }
            cur = queue.pop();
        }

        return null;
    }

    /**
     * Trace path from start node to an end node
     *
     * @param start - The path's start
     * @param end - The path's end
     * @return A Path object, represented by a list of consecutive points
     */
    private Path tracePath(Node start, Node end){
        Node cur = end;
        Path p = new Path();
        while (cur.getParent() != null && !cur.getParent().getId().equals(start.getId())){
            p.add(cur);
            cur = cur.getParent();
        }
        return p;
    }

    /**
     * Checks if the node has been visited before
     *
     * @param n - Node to check
     * @return If it has been visited
     */
    private boolean beenVisited(Node n){return this.visited.containsKey(n.getId());}

}
