package edu.wpi.cs3733.d19.teamM.utilities.AStar;
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
    private final String floorID;

    public Path() {

        finalPath = new ArrayList<>();
        floorID = "";
    }

    public Path(String floorID){
        finalPath = new ArrayList<>();
        this.floorID = floorID;
    }

    public String getFloorID(){
        return this.floorID;
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

    /**
     * Seperate a global path into local floor paths
     * @return A list of paths for each floor
     */
    public List<Path> getFloorPaths(){

        List<Path> paths = new ArrayList<>();
        String curFloor = finalPath.get(0).getFloor();
        paths.add(new Path(curFloor));
        int index = 0;

        for (Node n : finalPath){
            if (!n.getFloor().equals(curFloor)) {
                curFloor = n.getFloor();
                paths.add(new Path(curFloor));
                index++;
            }
            paths.get(index).add(n);
        }
        return paths;
    }

    public List<Path> getSpecificPath(String floorID){
        List<Path> paths = new ArrayList<>();
        for (Path p : getFloorPaths()){
            //if (floorID.equals("4") && p.getFloorID().equals("2")) paths.add(p);
            //if (floorID.equals("2") && p.getFloorID().equals("4")) paths.add(p);
            if (p.getFloorID().equals(floorID)) paths.add(p);
        }
        return paths;
    }
}
