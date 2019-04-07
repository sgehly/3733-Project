package edu.wpi.cs3733.d19.teamM.controllers.Pathfinding;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.*;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.common.map.MapUtils;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.SendEmail;
import edu.wpi.cs3733.d19.teamM.Main;
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
import org.controlsfx.control.textfield.AutoCompletionBinding;
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
        System.out.println(1);
        System.out.println(startString+" - "+endString);
        System.out.println(1);

        try {

            Map<String, Node> floorMap = graph.getNodes();
            Node startNode = null;
            Node endNode = null;

            try{
                for(Node node : floorMap.values())
                {
                    if(node.getLongName().equals(startString))
                    {
                        System.out.println("Found start node!");
                        startNode = node;
                    }
                    if(node.getLongName().equals(endString))
                    {
                        System.out.println("Found end node!");
                        endNode = node;
                    }
                }

            }catch (Exception f) //If that breaks too
            {
                f.printStackTrace();
            }

            if(startNode == null || endNode == null)
            {
                System.out.println("ERR1");
                return;
            }

            System.out.println("Finding path between " + startNode.getId() + " and " + endNode.getId());

            //Now we create an A* object to find the path between the two and store the final list of nodes
            AStar aStar = new AStar();
            Path nodeArrayList = aStar.findPath(startNode, endNode);
            for (Path p : nodeArrayList.getFloorPaths()){
                System.out.println("Path for floor " + p.getFloorID());
                System.out.println(PathToString.getDirections(p));
            }
            System.out.println("Got Path");
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //Now use this list to draw the path and put it in resources "/resources/maps/PathOutput.png"
            graph.drawPath(nodeArrayList);
            try {
                Image Overlaysource;
                URL theUrl = new URL("file:///" + System.getProperty("user.dir") + File.separator + "PathOutput.png");
                Overlaysource = new Image(theUrl.toURI().toString()); //See if we can get the image to overlay and then create a new image object from it
                overlayImage.setImage(Overlaysource); //set the image as the overlay image

                startText.setText("");
                endText.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        //Get the necessary records for pathfinding
        //getAllRecords();



        //Get the necessary records for pathfinding
        util.initialize();

        Map<String, Node> floorMap = graph.getNodes();
        ArrayList<String> longNames = new ArrayList<String>();//ArrayList of LongNames

        for(Node node: floorMap.values())
        {
            longNames.add(node.getLongName());
        } //Add all long names

        AutoCompletionBinding<String> sb = TextFields.bindAutoCompletion(startText,longNames);
        AutoCompletionBinding<String> eb = TextFields.bindAutoCompletion(endText,longNames);

        sb.setOnAutoCompleted((ov) -> {
            try{this.findPath();}catch(Exception e){e.printStackTrace();}
        });
        eb.setOnAutoCompleted((ov) -> {
            try{this.findPath();}catch(Exception e){e.printStackTrace();}
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
        if(startText.getText().length() == 0 && endText.getText().length() == 0){
            startText.setText(nodeId);
            endText.requestFocus();
        }
        else if(startText.getText().length() == 0){
            endText.setText(nodeId);
            startText.requestFocus();
        }
        else{
            endText.setText(nodeId);
            try{this.findPath();}catch(Exception e){e.printStackTrace();}
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

        System.out.println(path);
        System.out.println(util.getCurrentFloorID());

        Path floorPath = path.getSpecificPath(util.getCurrentFloorID());

        System.out.println(floorPath);

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
