package app;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple parser to extract the mapping information
 */
public class CSVParser {

    Map<String, Node> nodes;

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
            line = br.readLine(); // To skip over file header
            while ((br.readLine()) != null) {
                line = br.readLine();
                String[] locations = line.split(delimiter);
                // Convert the integer
                int xCoord = Integer.parseInt(locations[1]);
                int yCoord = Integer.parseInt(locations[2]);
                nodeTable.put(locations[0], new Node(locations[0], xCoord, yCoord, locations[3], locations[4], locations[5], locations[6], locations[7]));
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
            line = br.readLine(); // To skip over file header
            while ((br.readLine()) != null) {
                line = br.readLine();
                String[] edgeInfo = line.split(delimiter);
                nodeMap.get(line[1]).addEdge(line[2]);
                nodeMap.get(line[2]).addEdge(line[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found!");
        } catch (IOException e) {
            System.out.println("couldn't read data!");
        }
    }
}
