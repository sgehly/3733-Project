package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple parser to extract the mapping information
 */
public class CSVParser {

    private Map<String, Node> nodes;

    /**
     * CSVParser Constructor
     *
     * @param roomCSVFile - The csv file with the room information
     * @param edgesCSVFile - The csv file with the edge information
     */
    public CSVParser(String roomCSVFile, String edgesCSVFile){
        nodes = readCVS(roomCSVFile);
        addEdgesFromCSV(edgesCSVFile, nodes);
    }

    /**
     * Gets the list of nodes
     *
     * @return A list of nodes
     */
    public Map<String, Node> getNodes(){
        return nodes;
    }

    /**
     * Reads the CSV File
     *
     * NOTE: This method uses commas ',' as a delimiter
     *     : Skips over first line assuming it is a heading
     *
     * @param fileName - The file location of the csv you want to parse
     */
    private Map<String ,Node> readCVS(String fileName) {

        String myCSV = fileName;
        BufferedReader br = null;
        String line = "";
        String delimiter = ",";
        Map<String, Node> nodeTable = new HashMap<>();
        try {
            br = new BufferedReader(new FileReader(myCSV));
            br.readLine();
            while ((line = br.readLine()) != null) {
                try {
                    String[] locations = line.split(delimiter);
                    int xCoord = Integer.parseInt(locations[1]);
                    int yCoord = Integer.parseInt(locations[2]);
                    nodeTable.put(locations[0], new Node(locations[0], xCoord, yCoord, locations[3], locations[4], locations[5], locations[6], locations[7]));
                }
                catch (NullPointerException e) {
                    System.out.println("Invalid line: " + line);
                }
                // Convert the integer
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found!");
        } catch (IOException e) {
            System.out.println("couldn't read data!");
        }

        return nodeTable;
    }

    /**
     * Reads the CSV File
     *
     * NOTE: This method uses commas ',' as a delimiter
     *     : Skips over first line assuming it is a heading
     *
     * @param fileName - The file location of the csv you want to parse
     */
    private void addEdgesFromCSV(String fileName, Map<String, Node> nodeMap) {

        String myCSV = fileName;
        BufferedReader br = null;
        String line = "";
        String delimiter = ",";

        try {
            br = new BufferedReader(new FileReader(myCSV));
            br.readLine(); // To skip over file header
            while ((line = br.readLine()) != null) {
                try {
                    String[] edgeInfo = line.split(delimiter);
                    Node firstNode = nodeMap.get(edgeInfo[1]);
                    Node secondNode = nodeMap.get(edgeInfo[2]);
                    firstNode.addEdge(secondNode, edgeInfo[0]);
                    secondNode.addEdge(firstNode, edgeInfo[0]);
                }
                catch (NullPointerException e) {
                    System.out.println("Invalid line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found!");
        } catch (IOException e) {
            System.out.println("couldn't read data!");
        }
    }
}
