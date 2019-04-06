package edu.wpi.cs3733.d19.teamM.controllers.Pathfinding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wpi.cs3733.d19.teamM.common.map.MapUtils;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.*;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.SendEmail;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.MapPoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * The controller class associated with the pathfinding fuctions
 */
public class Pathfinding {

    MapUtils util;

    Rectangle2D primaryScreenBounds;

    Floor graph;

    Path path;

    //Get the FXML objects to be linked
    @FXML
    private Pane imageView;

    @FXML
    private ImageView image;

    @FXML
    private ImageView overlayImage;

    @FXML
    private Pane buttonContainer;

    @FXML
    private Slider zoomLvl;

    @FXML
    private TextField startText;

    @FXML
    private TextField endText;

    @FXML
    private TextField sendMapTextBox;

    @FXML
    private Text floorLabel;

    /**
     * This method lets the user navigate back to the home page
     * @throws Exception
     */
    @FXML
    private void navigateToHome() throws Exception{
        Main.setScene("home");
    }

    @FXML
    private void sendMap() throws Exception{
        //Load the home screen pane, get the scene and update the stage

        String email = sendMapTextBox.getText();

        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if(mat.matches()){
            new SendEmail("email", email).start();
        }else{
            new SendEmail("phone", email).start();
        }
        sendMapTextBox.getText();
    }

    @FXML
    private void findBathroom() throws Exception{
        if(startText.getText() != null){
            findPresetHelper("REST");
        }
    }
    @FXML
    private void findStaircase() throws Exception{
        if(startText.getText() != null){
            findPresetHelper("STAI");
        }

    }

    @FXML
    private void findElevator() throws Exception{
        if(startText.getText() != null){
            findPresetHelper("ELEV");
        }
    }

    @FXML
    private void findExit() throws Exception{
        if(startText.getText() != null){
            findPresetHelper("EXIT");
        }

    }
    @FXML
    private void findServiceDesk() throws Exception{
        if(startText.getText() != null){
            findPresetHelper("INFO");
        }

    }


    //TODO: fix with new graph class
    private void findPresetHelper(String type) {
        String start = startText.getText();
        path = graph.findPresetPath(graph.getNodes().get(start), type);
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        updateMap();
        resetTextBox();
    }

    @FXML
    private void findPath() throws Exception{
        //Get the starting and ending nodes ID
        String startString = startText.getText();
        String endString = endText.getText();
        //Check if either are empty
        if(graph.getNodes().containsKey(startString) && graph.getNodes().containsKey(endString)) //If not empty
        {
            Map<String, Node> floorMap = graph.getNodes();
            Node startNode = floorMap.get(startString); //Get starting and ending string using keys
            Node endNode = floorMap.get(endString);
            //Now we create an A* object to find the path between the two and store the final list of nodes
            path = graph.findPath(startNode, endNode);
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //Now use this list to draw the path and put it in resources "/resources/maps/PathOutput.png"
            updateMap();
            resetTextBox();
        }
    }

    /**
     * This method will initialize the pathfinding screen's controller
     * @throws Exception: Any exception that arises in the screen
     */
    @FXML
    protected void initialize() throws Exception {

        graph = new Floor();
        path = new Path();
        util = new MapUtils(buttonContainer, imageView, image, overlayImage, this::setValues);

        //Get the necessary records for pathfinding
        util.initialize();
    }

    /**
     * Set the values of the nodeID and other relevant text for each of the buttons that act as the nodes on the map to display for pathfinding options
     * @param value: The action event associated with the method
     */
    private void setValues(ActionEvent value) {
        //Get the id of the node
        String nodeId = ((Button)value.getSource()).getId();

        //Get required tex to display as one of the values for the pathfinding
        if(startText.getText().length() == 0){
            startText.setText(nodeId);
            endText.requestFocus();
        }else{
            endText.setText(nodeId);
            try {
                this.findPath();
            } catch (Exception e){}
        }
    }

    public void moveUp(ActionEvent value) throws Exception{
        util.moveUp();
        floorLabel.setText(util.getFloorLabel());

        updateMap();
    }

    public void moveDown(ActionEvent value) throws Exception{
        util.moveDown();
        floorLabel.setText(util.getFloorLabel());

        updateMap();
    }

    private void updateMap(){
        Path floorPath = path.getSpecificPath(util.getCurrentFloorID());
        if (floorPath != null){
            overlayImage.setImage(graph.drawPath(floorPath));
        }
        else {
            overlayImage.setImage(null);
        }
    }

    private void resetTextBox(){
        startText.setText("");
        endText.setText("");
    }

    /**
     * This method allows a user to return back to the welcome page
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    public void logout() throws Exception{
        Main.setScene("welcome");
    }
}
