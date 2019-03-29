package app;

public class Edge{
    private Node end;
    private int weight;
    private String edgeID;

    /**
     * Constructor for Edge
     *
     * NOTE: The start node is assumed to be the the node object
     *       that creates an edge.
     *
     * @param end - The node the edge ends at
     * @param edgeID - The ID of the edge
     */
    public Edge (Node end, String edgeID) {
        this.end = end;
        this.edgeID = edgeID;
    }

    /**
     * Set the edges weight
     *
     * @param w - The new weight of the edge
     */
    public void setWeight(int w){
        weight = w;
    }

    /**
     * Get the end of the edge
     *
     * @return The node the edge ends at
     */
    public Node getEndNode() {
        return end;
    }

    /**
     * Get the weight of the edge
     *
     * @return The edges weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Get the edges ID
     *
     * @return The edges ID
     */
    public String getEdgeID(){
        return edgeID;
    }

    public Node getEnd() {
        return end;
    }
}
