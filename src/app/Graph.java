package app;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    String imgURL;

    /**
     * Simple class to track the various paths
     */
    protected class Path {

        List<Node> includedNodes;

        protected Path() {
            includedNodes = new ArrayList<>();
        }

        /**
         * Add a node to the path
         *
         * @param n - The node to add to the path
         */
        protected void addNodeToPath(Node n) {
            includedNodes.add(n);
        }

        /**
         * Get the path
         *
         * @return A consecutive and ordered list of the nodes along the path
         */
        protected List<Node> getPath() {
            return includedNodes;
        }
    }

    /**
     * Constructor for Graph
     *
     * @param mapImgURL - The Img Location of the map for the specific graph.
     * @param nodes - The list of nodes to be modified
     */
    public Graph(String mapImgURL) {
        imgURL = mapImgURL;
    }

    public List<Node> findPath(Node start, Node end) {
        return null;
    }

}
