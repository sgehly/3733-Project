package app.AStar;

public class AddDelNode {
    public AddDelNode() {}

    public void removeNode(Node rem) {

        // check to make sure part of the graph
//        if(!this.containsValue(rem)) {
//            System.out.println("sorry doesn't exist in map");
//        }

        // removes all the edges of the node that you want to remove
        for(Edge e: rem.getEdges()) {
            rem.removeEdge(rem, e.getEdgeID());
        }

        // remove node
        this.remove(rem.getId()); // does this work??

    }

    public void addNode(Node addN) {
        // use breadth first search to find where node should go in tree
        // yeah i really don't understand how to do this
        // find a way to go through of the map

        this.add(addN);
    }
}
