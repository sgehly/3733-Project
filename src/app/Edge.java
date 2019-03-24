package app;

public class Edge{
    private Node end;
    private int weight;

    public Edge (Node end) {
        this.end = end;
    }

    public Node getEndNode() {
        return end;
    }

    public int getWeight() {
        return weight;
    }
}
