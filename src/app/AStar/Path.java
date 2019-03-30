package app.AStar;

import java.util.ArrayList;
import java.util.List;

/**
 * Professor wong suggested we created a path object to hide some of the implementation
 *
 * Right now it's just an abstraction of an array list, but it will be more modular for upcoming iterations
 */
public class Path {

    List<Node> path;

    public Path(){
        path = new ArrayList<>();
    }

    /**
     * Add a node to the path
     * @param n - The node to add
     */
    public void add(Node n){
        path.add(n);
    }

    /**
     * Get the path
     * @return A list of nodes representing the path.
     */
    public List<Node> getPath(){
        return path;
    }

    /**
     * Remove a node from the path
     * @param n - The node to remove
     */
    public void remove(Node n){
        path.remove(n);
    }

}
