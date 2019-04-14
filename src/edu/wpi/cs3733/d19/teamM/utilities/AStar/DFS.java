package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.*;

public class DFS implements Searchable{

    private Stack<Node> stack;
    private Map<String, Node> visited;
    private Path p;

    /**
     * Constructor for DFS
     */
    public DFS(){
    }

    /**
     * Find the path between a start and end node
     * @param start - The node to start the path
     * @param end - The node to end the path
     * @return A list of nodes that represent a path. path[n] is the end node and path[0] is the start node where n
     *         is path.size() - 1
     */
    public Path findPath(Node start, Node end){
        stack = new Stack<>();
        visited = new HashMap<>();
        p = new Path();
        Node curNode = start;
        curNode.setParent(null);
        curNode.setG(0);
        visited.put(curNode.getId(), curNode);

        while ((curNode = getNextNode(curNode, end)) != null && !curNode.getId().equals(end.getId())){
            System.out.println(curNode.getId() + " is the current node");
        }

        //trace path
        while (curNode.getParent() != null){
            p.add(curNode);
            curNode = curNode.getParent();
        }

        if (curNode == null){
            return null;
        }

        p.add(start);

        return p;
    }

    public Path findPresetPath(Node start, String destType,Map<String, Node> map){
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
     * Get next node
     *
     * @param n - The current node
     * @return Next node on the stack
     */
    private Node getNextNode(Node n, Node end) {

        List<Node> adjNodes = new ArrayList<>();

        for (Edge e : n.getEdges()){
            if (!beenVisited(e.getEndNode()) && e.getEndNode().isEnabled()){ // If haven't been visited and enabled
                visited.put(e.getEndNode().getId(), e.getEndNode()); // add to visited
                e.getEndNode().setParent(n);
                setHeuristics(e.getEndNode(), end);
                adjNodes.add(e.getEndNode());
            }
        }

        Collections.sort(adjNodes);
        Collections.reverse(adjNodes);
        addToStack(adjNodes);

        return stack.pop();
    }

    void setHeuristics(Node curNode, Node endNode){
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

    private void addToStack(List<Node> nodesToAdd){
        for (Node n : nodesToAdd){
            stack.addToStack(n);
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
}
