package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.Map;

public interface Searchable {
    Path findPath(Node start, Node end);
    Path findPresetPath(Node start, String type, Map<String, Node> map);
}
