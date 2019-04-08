package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFS implements Searchable {

    Stack<Node> stack;
    Map<String, Node> visited;
    Path p;

    /**
     * Constructor for DFS
     */
    public DFS(){
        stack = new Stack<>();
        visited = new HashMap<>();
    }

    /**
     * Find the path between a start and end node
     * @param start - The node to start the path
     * @param end - The node to end the path
     * @return A list of nodes that represent a path. path[n] is the end node and path[0] is the start node where n
     *         is path.size() - 1
     */
    public Path findPath(Node start, Node end){
        p = new Path();
        Node curNode = start;
        p.add(curNode);
        stack.addToStack(start); /// add start node to the stack
        while ((curNode = getNextNode(curNode)) != null && !curNode.getId().equals(end.getId())){
            p.add(curNode);
        }

        if (curNode == null){
            return null;
        }
        p.add(end);
        return p;
    }

    /**
     * Get next node
     *
     * @param n - The current node
     * @return Next node on the stack
     */
    private Node getNextNode(Node n) {
        for (Edge e : n.getEdges()){
            if (!beenVisited(e.getEndNode()) && e.getEndNode().isEnabled()){ // If haven't been visited and enabled
                visited.put(e.getEndNode().getId(), e.getEndNode()); // add to visited
                stack.addToStack(e.getEndNode()); // add to top of stack
                return stack.getTop(); // return the top of the stack
            }
        }
        stack.pop(); // No where to go remove top
        p.remove(n);// remove from path as well
        return stack.getTop();
    }

    /**
     * If a node has been visited
     * @param n - A node
     * @return If it has been visited before
     */
    private boolean beenVisited(Node n){
        return visited.containsKey(n.getId());
    }
}
