package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.HashMap;
import java.util.Map;

public class MapSystem{
    private Map<String, Map<String, Node>> mapSystem;

    public MapSystem(){
        mapSystem = new HashMap<>();
    }


    //getters and setters for the map system
    public Map<String, Map<String, Node>> getMapSystem(){return mapSystem;}
    public void setMapSystem(Map<String, Map<String, Node>> mapSystem){this.mapSystem = mapSystem;}
}
