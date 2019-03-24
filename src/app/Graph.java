package app;

import java.util.List;

public class Graph {

    String imgURL;

    /**
     * Constructor for Graph
     *
     * @param mapImgURL - The Img Location of the map for the specific graph.
     * @param nodes - The list of nodes to be modified
     */
    public Graph(String mapImgURL, List<Node> nodes) {
        imgURL = mapImgURL;
    }

}
