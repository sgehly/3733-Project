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

    ArrayList<String> longNames;

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

    @FXML
    private CheckBox bathrooms;

    @FXML
    private CheckBox stairs;

    @FXML
    private CheckBox elevators;

    @FXML
    private CheckBox labs;

    @FXML
    private CheckBox confs;

    @FXML
    private CheckBox food;

    @FXML
    private CheckBox services;

    @FXML
    private CheckBox exits;

    /**
     * This method will initialize the pathfinding screen's controller
     * @throws Exception: Any exception that arises in the screen
     */
    @FXML
    protected void initialize() throws Exception {

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        // find a way to connect the long name in the database and then assign the input start string as that
        //TextFields.bindAutoCompletion();
        //startText.textProperty().bind()

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
        setUpListeners();

        util.initialize();


    }

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
        if (startText.getText() != null){
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

    private void findPath() throws Exception{
        //Get path
        String start = startText.getText();
        String end = endText.getText();
        Node startNode = graph.getNodes().get(start);
        Node endNode = graph.getNodes().get(end);
        path = graph.findPath(startNode, endNode);
        resetTextBox();
        updateMap();
    }

    private void findPathWithLongNames() {

        String start = startText.getText();
        String end = endText.getText();
        Node startNode = null;
        Node endNode = null;

        for (Node n : graph.getNodes().values()){
            if (n.getLongName().equals(start)){
                startNode = n;
            }
            if (n.getLongName().equals(end)){
                endNode = n;
            }
        }

        if (startNode != null && endNode != null) {
            path = graph.findPath(startNode, endNode);
        }

        updateMap();
        resetTextBox();
    }

    /**
     *
     * @param s
     */
    private void filterNodes(String s) throws Exception, SQLException {
        try {
            //Floor floor = new Floor("1");
            //Get the information that we want from the database
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt = conn.prepareStatement("select * from node where nodeType = ?");
            stmt.setString(1, s);
            ResultSet rs = stmt.executeQuery(); // execute where the node type is that specified in the database

            //util.getEntryObjects(rs);

            conn.close();

        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
        }

    }

    /**
     *
     * @param e
     */
    public void handleButton(ActionEvent e) throws Exception, SQLException{
       if(bathrooms.isSelected()) {
           filterNodes("REST");
           filterNodes("BATH");
       }
       if(stairs.isSelected()) {
           filterNodes("STAI");
       }
       if(elevators.isSelected()) {
           filterNodes("ELEV");
       }
       if(labs.isSelected()) {
           filterNodes("LABS");
       }
       if(confs.isSelected()) {
           filterNodes("CONF");
       }
       if(food.isSelected()) {
           filterNodes("RETL");
       }
       if(exits.isSelected()) {
           filterNodes("EXIT");
       }
    }

    /**
     * Sets up listeners for text fields
     */
    private void setUpListeners(){

        Map<String, Node> floorMap = graph.getNodes();
        longNames = new ArrayList<String>();//ArrayList of LongNames

        for(Node node: floorMap.values())
        {
            longNames.add(node.getLongName());
        }
        startText.textProperty().addListener((ov, oldValue, newValue) -> {
            TextFields.bindAutoCompletion(startText,longNames);
        });
        endText.textProperty().addListener((ov, oldValue, newValue) -> {
            TextFields.bindAutoCompletion(endText,longNames);
            String start = startText.getText();
            String end = endText.getText();
            try{
                if (graph.getNodes().containsKey(start) && graph.getNodes().containsKey(end)){
                    findPath();
                }
                else if (checkValidLongNameInput()){
                    findPathWithLongNames();
                }
            }
            catch (Exception e){
                e.printStackTrace();
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

    private boolean checkValidLongNameInput(){

        boolean flag = false;
        String start = startText.getText();
        String end = endText.getText();

        for (String n : longNames){
            if (flag && (n.equals(start) || n.equals(end))){
                return true;
            }
            else if (n.equals(start) || n.equals(end)){
                flag = true;
            }
        }
        return false;
    }
}
