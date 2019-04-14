package edu.wpi.cs3733.d19.teamM.utilities.AStar;
import java.util.*;


public class Dijkstra extends SearchAlgorithm{

    private Map<String, Node> visited;
    private Path p;


    public Dijkstra(){}

    @Override
    public Path findPath(Node start, Node end) {
        Floor graph = Floor.getFloor();
        for(Node n : graph.getNodes().values()){
            n.setF(999000);
        }
        start.setF(0);
        start.setParent(null);
        PriorityQueue<Node> queue = new PriorityQueue<>();
        visited = new HashMap<>();
        p = new Path();
        queue.addToQueue(start);
        while(queue.getLength() != 0){
            Node current = queue.pop();
            System.out.println(current.getId());
            if(current.getId().equals(end.getId())){
                while(current.getParent() != null){
                    p.add(current);
                    current = current.getParent();
                }
                p.add(start);
                return p;
            }
            visited.put(current.getId(), current);
            for(Edge e : current.getEdges()){
                Node neighbor = e.getEndNode();
                if((neighbor.getF() > (current.getF() + current.getDistance(neighbor))) && !beenVisited(neighbor)){
                    neighbor.setParent(current);
                    setHeuristics(neighbor, end);
                    queue.addToQueue(neighbor);
                }
            }
        }
        return null;
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