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
                if((neighbor.getF() > (current.getF() + current.getDistance(neighbor))) && !visited.containsKey(neighbor.getId())){
                    neighbor.setParent(current);
                    setHueristics(neighbor, end);
                    queue.addToQueue(neighbor);
                }
            }
        }
        return null;
    }

    public Path findPresetPath(Node start, String destType, Map<String, Node> map){
        Iterator it = map.entrySet().iterator();
        Node closest = null;
        start.setG(0);
        double lowestCost = 500000;
        while(it.hasNext()) {
            Map.Entry set = (Map.Entry) it.next();
            Node n = (Node) set.getValue();
            n.setG(n.getDistance(start)); //set its g, h, f, and parent
            n.setP(getDeltaFloor(n, start) * 1000);
            n.setB(getDeltaBuilding(n, start) * 5000);
            n.setF(n.getG() + n.getP() + n.getB());
            if (n != null && (n.getNodeType().equals(destType))) {
                System.out.println("Checking Node "+   n.getId());
                if(n.getF() < lowestCost){
                    closest = n;
                    lowestCost = n.getF();
                }
            }
        }
        if(closest != null){
            return findPath(start, closest);
        }
        else{
            return null;
        }
    }


    /**
     * If a node has been visited
     * @param n - A node
     * @return If it has been visited before
     */
    private boolean beenVisited(Node n){
        return visited.containsKey(n.getId());
    }


    private void setHueristics(Node curNode, Node endNode){
        curNode.setH(curNode.getDistance(endNode));
        curNode.setG(curNode.getParent().getDistance(curNode) + curNode.getParent().getG());
        curNode.setP(getDeltaFloor(curNode, endNode) * 1000);
        curNode.setB(getDeltaBuilding(curNode, endNode) * 5000);
        curNode.setF(curNode.getG() + curNode.getP() + curNode.getH() + curNode.getB());
    }

    private double getDeltaFloor(Node n1, Node n2){
        /*
        L2 - 0
        L1 - 1
        G  - 2
        1  - 3
        2  - 4
        3  - 5
        */
        double floor1 = getFloorValue(n1.getFloor());
        double floor2 = getFloorValue(n2.getFloor());
        return Math.abs(floor1 - floor2);
    }

    private double getFloorValue(String f){
        if (f.equals("G")) return  0;
        else if (f.equals("L1")) return  1;
        else if (f.equals("L2")) return  2;
        else if (f.equals("1")) return  3;
        else if (f.equals("2")) return  4;
        else if (f.equals("3")) return  5;
        return -1;
    }

    private double getBuildingValue(String build){
        if(build.equals("BTM")) return 0;
        else if(build.equals("Shapiro")) return 1;
        else if (build.equals("Tower")) return 2;
        else if(build.equals("45 Francis")) return 3;
        else if(build.equals("15 Francis")) return 4;
        return -1;
    }

    private double getDeltaBuilding(Node n1, Node n2){
        /*
        BTM - 0
        Shapiro  - 1
        Tower  - 2
        45 Francis  - 3
        15 Francis  - 4
        */
        double n1BuildingValue = getBuildingValue(n1.getBuilding());
        double n2BuildingValue = getBuildingValue(n2.getBuilding());
        return Math.abs(n1BuildingValue - n2BuildingValue);
    }
}