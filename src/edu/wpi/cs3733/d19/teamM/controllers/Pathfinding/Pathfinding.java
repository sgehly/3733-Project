package edu.wpi.cs3733.d19.teamM.controllers.Pathfinding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.AStar;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Node;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.common.map.MapUtils;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.*;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.SendEmail;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.MapPoint;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;

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
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Text userText;

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
    private JFXTextField startText;

    @FXML
    private JFXTextField endText;

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
        Path nodeArrayList = graph.findPresetPath(graph.getNodes().get(start), type);
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        updateMap();
    }

    @FXML
    private void findPath() throws Exception{
        //Get the starting and ending nodes ID
        String startString = startText.getText();
        String endString = endText.getText();
        //Check if either are empty
        if(!startString.equals("") && !endString.equals("")) //If not empty
        {
            try {

                Map<String, Node> floorMap = graph.getNodes();
                Node startNode = null;
                Node endNode = null;

                if(startString.length() == 10 && endString.length() == 10) //if by node ID
                {
                    startNode = floorMap.get(startString); //Get starting and ending string using keys
                    endNode = floorMap.get(endString);
                }
                else
                {
                    if(startString.length() == 10 && endString.length() == 10) //if by node ID
                    {
                        try
                        {
                            startNode = floorMap.get(startString); //Get starting and ending string using keys
                            endNode = floorMap.get(endString);

                        }catch (Exception e) //Maybe it was normal string of 10 and not node ID
                        {
                            try{
                                for(Node node : floorMap.values())
                                {
                                    if(node.getLongName().equals(startString))
                                    {
                                        startNode = node;
                                    }
                                    if(node.getLongName().equals(endString))
                                    {
                                        endNode = node;
                                    }
                                }

                            }catch (Exception f) //If that breaks too
                            {

                            }

                        }

                    }
                    else { //If it is just a normal string
                        try{
                            for(Node node : floorMap.values())
                            {
                                if(node.getLongName().equals(startString))
                                {
                                    startNode = node;
                                }
                                if(node.getLongName().equals(endString))
                                {
                                    endNode = node;
                                }
                            }

                        }catch (Exception e) //Catch any external exception
                        {

                        }

                    }

                }

                if(startNode == null || endNode == null)
                {
                    return;
                }

                //Now we create an A* object to find the path between the two and store the final list of nodes
                path = graph.findPath(startNode, endNode);
                final JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //Now use this list to draw the path and put it in resources "/resources/maps/PathOutput.png"
                updateMap();
            }
            catch (Exception e){
                e.printStackTrace();
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(Main.getStage());
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Label("The node that you entered is not found or there is no path between the given starting node and destination"));
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        }

    }

    /**
     * This method will initialize the pathfinding screen's controller
     * @throws Exception: Any exception that arises in the screen
     */
    @FXML
    protected void initialize() throws Exception {

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        //Adapted from: https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx
        //------------------------------------------------------------------------------------------------
        primaryScreenBounds = Screen.getPrimary().getVisualBounds(); //Get the bounds of the screen

        Image source = new Image(Main.getResource("/resources/maps/01_thefirstfloor.png"));;

        image.setImage(source);//Set the image as the source
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight() - 200);



        //Gets the overlay image and sets the width and the height of that
        overlayImage.setFitWidth(image.getFitWidth());
        overlayImage.setFitHeight(image.getFitHeight());
        Image EMPTY = new Image(Main.getResource("/resources/maps/emptyOverlay.png")); //See if we can get the image to overlay and then create a new image object from it

        //Initially set the image to empty and get the width and height
        overlayImage.setImage(EMPTY);

        //Get the buttons on the screen and set the preferred width and height to that of the image
        buttonContainer.setPrefWidth(image.getFitWidth());
        buttonContainer.setPrefHeight(image.getFitHeight());

        graph = new Floor();
        path = new Path();
        util = new MapUtils(buttonContainer, imageView, image, overlayImage, this::setValues);



        //Get the necessary records for pathfinding
        util.initialize();

        Map<String, Node> floorMap = graph.getNodes();
        ArrayList<String> longNames = new ArrayList<String>();//ArrayList of LongNames

        for(Node node: floorMap.values())
        {
            longNames.add(node.getLongName());
        } //Add all long names
        startText.textProperty().addListener((ov, oldValue, newValue) -> {
            TextFields.bindAutoCompletion(startText,longNames);

            if(startText.getText().length() == 10 && endText.getText().length() == 10){
                //Both are present.
                try{this.findPath();}catch(Exception e){e.printStackTrace();}
            }
            else if(startText.getText().length() >= 0 && endText.getText().length() >= 0)
            {
                //If the lengths of both aren't zero, try to see if you can get path
                try{this.findPath();}catch(Exception e){e.printStackTrace();}
            }
        });
        endText.textProperty().addListener((ov, oldValue, newValue) -> {
            TextFields.bindAutoCompletion(endText,longNames);

            if(startText.getText().length() == 10 && endText.getText().length() == 10){
                //Both are present.
                try{this.findPath();}catch(Exception e){e.printStackTrace();}
            }
            else if(startText.getText().length() >= 0 && endText.getText().length() >= 0)
            {
                //If the lengths of both aren't zero, try to see if you can get path
                try{this.findPath();}catch(Exception e){e.printStackTrace();}
            }
        });
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

    public void updateMap(){
        Path floorPath = path.getSpecificPath(util.getCurrentFloorID());
        if (floorPath != null){
            overlayImage.setImage(graph.drawPath(floorPath));
        }
        else {
            overlayImage.setImage(null);
        }
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
