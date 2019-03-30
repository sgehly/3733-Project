package app.AStar;

import java.util.*;

public class Path {
    List<Node> finalPath;

    public Path() {
        finalPath = new ArrayList<>();
    }

    //getters and setters for the final path
    public List<Node> getFinalPath(){return finalPath;}
    public void setFinalPath(List<Node> finalPath){this.finalPath = finalPath;}
}
