/*
The #Algo Team
 */
package app;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is to run the A* algorithm and to also handle any other methods associated with the path-finding
 */
public class AStar {

    /**
     * This method takes in the final list of nodes in the A* path and returns an array made of two arraylists, where one has nodes and the other has edges.
     * @param nodeArrayList: The list of the final nodes in the path
     * @return ArrayList[]: This will have 2 elements:
     *                                                 The first element is the Arraylist of Nodes
     *                                                 The second element is the Arraylist of Edges
     *                                                 The path should be read by taking the first Node from the first ArrayList, then the first Edge for the second arrayList, etc.
     *                                                 There should be one more node than edge
     */
    public ArrayList[] getNodesAndEdges(ArrayList<Node> nodeArrayList)
    {
        ArrayList[] finalPath = new ArrayList[2]; //Create the arrayList array
        ArrayList<Node> finalNodes = nodeArrayList; //Get final nodes
        ArrayList<Edge> finalEdges = new ArrayList<Edge>(); //Create arraylist of edges
        int nodeListLen = finalNodes.size(); //Get size of node list

        for(int i = 0; i<nodeListLen-1;i++)//Loop through till we reach the second to last node and check the edge that connects it to the next node and add it to the edge list
        {
            //Get first node, get all edges, and see which one is also in second node (only one should match)
            Node firstNode = finalNodes.get(i);
            Node secondNode = finalNodes.get(i+1);
            List<Edge> firstNodeEdges = new ArrayList<Edge>();
            List<Edge> secondNodeEdges = new ArrayList<Edge>();

            firstNodeEdges = firstNode.getEdges();//Add firstNode edges to firstNodeEdges
            secondNodeEdges = secondNode.getEdges(); //Add secondNode edges to secondNodeEdges

            for(Edge anEdge : firstNodeEdges) //For each of the edges of the firstNodes
            {
                if(secondNodeEdges.contains(anEdge)) //If it is in the secondList
                {
                    finalEdges.add(anEdge);//Add it to our list of finalEdges and exit
                    break;
                }
            }
        }

        //Add both arrayLists to the array of arrayLists and return to the user
        finalPath[0] = finalNodes;
        finalPath[1] = finalEdges;
        return finalPath;
    }

    //Actual A* algorithm
    public List<Node> findPath(Node start, Node end){
        PriorityQueue openList = new PriorityQueue(start, end);
        ArrayList<Node> closedList = new ArrayList<Node>();
        start.setG(0);
        start.setH(start.getDistance(end));
        start.setF(start.getG() + start.getH());
        openList.addtoHeap(start);
        //TODO: Find node of lowest F
        while(openList.getHeapSize() != 0){
            Node current = openList.getHeap().get(0);
            openList.getHeap().remove(0);
            for(Edge e : current.getEdges()){
                Node neighbor = e.getEndNode();
                neighbor.setParent(current);
                openList.addtoHeap(neighbor);
                if(neighbor.getId().equals(end.getId())){
                    List<Node> path = new ArrayList<>();
                    current = neighbor;
                    while (!current.getId().equals(start.getId())) {
                        path.add(current);
                        current = current.getParent();
                    }
                    return path;
                }
            }
        }
        return null;
    }
}
