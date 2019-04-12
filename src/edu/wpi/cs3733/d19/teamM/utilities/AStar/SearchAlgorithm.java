package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.Map;

public abstract class SearchAlgorithm {
    abstract Path findPath(Node start, Node end);
    abstract Path findPresetPath(Node start, String type, Map<String, Node> map);
}
