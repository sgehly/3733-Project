package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    /**
     * Reads the CSV File
     *
     * NOTE: This method uses commas ',' as a delimiter
     *     : Skips over first line assuming it is a heading
     *
     * @param fileName - The file location of the csv you want to parse
     */
    public List<Node> readCVS(String fileName) {

        String myCSV = fileName;
        BufferedReader br = null;
        String line = "";
        String delimiter = ",";
        List<Node> nodeList = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(myCSV));
            line = br.readLine(); // To skip over file header
            while ((br.readLine()) != null) {
                line = br.readLine();
                String[] locations = line.split(delimiter);
                // Convert the integer
                int xCoord = Integer.parseInt(locations[1]);
                int yCoord = Integer.parseInt(locations[2]);
                nodeList.add(new Node(locations[0], xCoord, yCoord, locations[3], locations[4], locations[5], locations[6], locations[7]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found!");
        } catch (IOException e) {
            System.out.println("couldn't read data!");
        }

        return  nodeList;
    }
}
