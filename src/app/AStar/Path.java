package app.AStar;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * Professor wong suggested we created a path object to hide some of the implementation
 *
 * Right now it's just an abstraction of an array list, but it will be more modular for upcoming iterations
 */

public class Path {
    private List<Node> finalPath;

    public Path() {
        finalPath = new ArrayList<>();
    }

    //getters and setters for the final path
    public List<Node> getFinalPath(){return finalPath;}
    public void setFinalPath(List<Node> finalPath){this.finalPath = finalPath;}

    /**
     * Add a node to the path
     * @param n - The node to add
     */
    public void add(Node n){
        finalPath.add(n);
    }

    /**
     * Get the path
     * @return A list of nodes representing the path.
     */
    public List<Node> getPath(){
        return finalPath;
    }

    /**
     * Remove a node from the path
     * @param n - The node to remove
     */
    public void remove(Node n){
        finalPath.remove(n);
    }
}
