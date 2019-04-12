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

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.*;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.AStar;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Node;
import edu.wpi.cs3733.d19.teamM.common.map.MapUtils;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.*;
import edu.wpi.cs3733.d19.teamM.Main;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
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

    ArrayList<Button> clearNodes = new ArrayList<Button>();

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
    private JFXSlider zoomSlider;

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

    @FXML
    private GesturePane gesturePane;

    @FXML
    private VBox mappingStuff;

    /**
     * This method will initialize the pathfinding screen's controller
     * @throws Exception: Any exception that arises in the screen
     */
    @FXML
    protected void initialize() throws Exception {
        gesturePane.setContent(mappingStuff);

        System.out.println("Initializing pathfinding");
        new Clock(lblClock, lblDate);
        //userText.setText(User.getUsername());
        userText.setText("");

        new Thread(() -> {
            try{
                graph = Floor.getFloor();
                path = new Path();
                util = new MapUtils(buttonContainer, imageView, image, overlayImage, zoomSlider, this::setValues, this::clickValues, false, null, null);
                setUpListeners();

                System.out.println("Init maputils");
                util.initialize();

                floorLabel.setText(util.getFloorLabel());
            }catch(Exception e){
                e.printStackTrace();
            }

        }).start();

        System.out.println("Finished pathfinding");

    }


    @FXML
    private void speakDirections()
    {
        TextSpeech textSpeech = new TextSpeech();
        textSpeech.speakToUser();
    }

    private void clickValues(MouseEvent evt){}

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
            new SendEmail("email", email, path).start();
        }else{
            new SendEmail("phone", email, path).start();
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

   @FXML
   private void findFood() throws Exception{
        if(startText.getText() != null){
            findPresetHelper("RETL");
        }
   }

    //TODO: fix with new graph class
    private void findPresetHelper(String type) throws Exception{
        String start = startText.getText();
        Node startNode = null;
        for (Node n : graph.getNodes().values()){
            if (n.getId().equals(start)){
                startNode = n;
            }
        }
        if (startNode != null) {
            path = graph.findPresetPath(startNode, type, graph.getNodes());
        }

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        util.setFloor(path.getFinalPath().get(path.getFinalPath().size()-1).getFloor());
        floorLabel.setText(util.getFloorLabel());
        PathToString.getDirections(path);

        updateMap(null, null);
        resetTextBox();
    }

    private void findPath() throws Exception{
        //Get path
        String start = startText.getText();
        String end = endText.getText();
        Node startNode = graph.getNodes().get(start);
        Node endNode = graph.getNodes().get(end);
        path = graph.findPath(startNode, endNode);
        PathToString.getDirections(path);
        System.out.println(path+"/"+graph);
        util.setFloor(path.getFinalPath().get(0).getFloor());
        floorLabel.setText(util.getFloorLabel());

        resetTextBox();
        updateMap(null,null);
    }

    private void findPathWithLongNames() throws Exception{

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
            PathToString.getDirections(path);
           // Printing myPrinter = new Printing();
            //myPrinter.printDirections("C:\\Users\\kenne\\Desktop\\the-file-name.txt");
        }

        int newFloorInt = util.idToFloor(path.getFinalPath().get(path.getFinalPath().size()-1).getFloor());
        System.out.println("Setting floor to "+newFloorInt);
        util.setFloor(newFloorInt);
        floorLabel.setText(util.getFloorLabel());

        System.out.println("Seeing util floor as "+util.floor);

        updateMap(null,null);
        resetTextBox();
    }

    private void filterNodes(String s) {
        util.filterNodes(s);
    }

    private void unfilterNodes(String s) {
        util.unfilterNodes(s);
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
       else {
           unfilterNodes("REST");
           unfilterNodes("BATH");
       }
       if(stairs.isSelected()) {
           filterNodes("STAI");
       }
       else{
           unfilterNodes("STAI");
       }
       if(elevators.isSelected()) {
           filterNodes("ELEV");
       }
       else{
           unfilterNodes("ELEV");
       }
       if(labs.isSelected()) {
           filterNodes("LABS");
       }
       else {
           unfilterNodes("LABS");
       }
       if(confs.isSelected()) {
           filterNodes("CONF");
       }
       else {
           unfilterNodes("CONF");
       }
       if(food.isSelected()) {
           filterNodes("RETL");
       }
       else {
           unfilterNodes("RETL");
       }
       if(exits.isSelected()) {
           filterNodes("EXIT");
       }
       else {
           unfilterNodes("EXIT");
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
            if(!node.getNodeType().equals("HALL"))
            {
                longNames.add(node.getLongName());
            }
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
    private void setValues(MouseEvent value) {
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

        updateMap(null,null);
    }

    public void moveDown(ActionEvent value) throws Exception{
        util.moveDown();
        floorLabel.setText(util.getFloorLabel());

        updateMap(null,null);
    }

    private Button generateButton(String text, double x, double y){
        return new Button();
    }

    private void updateMap(Node startNode, Node endNode) throws Exception{
        clearNodes.forEach(node -> {
            util.buttonPane.getChildren().remove(node);
        });
        System.out.println("-=-=-=-=INITIALIZING UPDATEMAP-=-=-=-=-=");
        System.out.println("current floor id: "+util.getCurrentFloorID());
        List<Path> floorPaths = path.getSpecificPath(util.getCurrentFloorID());
        List<Path> allPaths = path.getFloorPaths();

        if (floorPaths != null){
            overlayImage.setImage(graph.drawPath(floorPaths));
        }
        else {
            overlayImage.setImage(null);
        }

        if(startNode != null && endNode != null){
            //We zoomin boys

            double startX = startNode.getX();
            double startY = startNode.getY();

            double endX = endNode.getX();
            double endY = endNode.getY();

            int newXRaw = startNode.getX()+(Math.abs(startNode.getX()-endNode.getX())/2);
            int newYRaw = startNode.getY()+(Math.abs(startNode.getY()-endNode.getY())/2);

            MapPoint scale = util.scalePoints((int)startX,(int)startY);
            gesturePane.zoomTo(5, new Point2D(scale.x, scale.y));

        }

        floorPaths.forEach(floorPath -> {
            //Path is each path on a specific floor.
            List<Node> thePath = floorPath.getPath();
            Node start = thePath.get(0);
            Node end = thePath.get(thePath.size()-1);

            if(start.getId().equals(this.path.getFinalPath().get(0).getId())){
                Button startChangeButton = new Button();
                startChangeButton.setText("YOU ARE HERE: "+start.getLongName());
                MapPoint mp = util.scalePoints(start.getX(), start.getY());
                startChangeButton.setLayoutX(mp.x);
                startChangeButton.setLayoutY(mp.y);
                startChangeButton.setStyle("-fx-background-color: white; -fx-border-width: .2em; -fx-border-color:  black; -fx-text-fill: black; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-cursor: hand;-fx-font-size:10px; -fx-font-family: \"Open Sans Bold\"");
                startChangeButton.setMinWidth(100);
                startChangeButton.setMinHeight(15);
                startChangeButton.setOpacity(0.5);
                startChangeButton.setOnMouseEntered((ov) -> {startChangeButton.setOpacity(1);});
                startChangeButton.setOnMouseExited((ov) -> {startChangeButton.setOpacity(0.5);});
                util.buttonPane.getChildren().add(startChangeButton);
                clearNodes.add(startChangeButton);
            }
            if(end.getId().equals(this.path.getFinalPath().get(this.path.getFinalPath().size()-1).getId())){
                Button startChangeButton = new Button();
                startChangeButton.setText("YOUR DESTINATION: "+end.getLongName());
                MapPoint mp = util.scalePoints(end.getX(), end.getY());
                startChangeButton.setLayoutX(mp.x);
                startChangeButton.setLayoutY(mp.y);
                startChangeButton.setStyle("-fx-background-color: white; -fx-border-width: .2em; -fx-border-color:  black; -fx-text-fill: black; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-cursor: hand;-fx-font-size:10px; -fx-font-family: \"Open Sans Bold\"");
                startChangeButton.setMinWidth(100);
                startChangeButton.setMinHeight(15);
                startChangeButton.setOpacity(0.5);
                startChangeButton.setOnMouseEntered((ov) -> {startChangeButton.setOpacity(1);});
                startChangeButton.setOnMouseExited((ov) -> {startChangeButton.setOpacity(0.5);});
                util.buttonPane.getChildren().add(startChangeButton);
                clearNodes.add(startChangeButton);
            }

            if(start.getId().equals(end.getId())) return;
            //ELEV or STAI

            String startNodeType = end.getNodeType();
            System.out.println("START NODE: "+start.getLongName()+" | STARTNODETYPE: "+startNodeType);

            if((startNodeType.equals("ELEV") || startNodeType.equals("STAI")) && !end.getId().equals(path.getFinalPath().get(0)) && !end.getId().equals(path.getFinalPath().get(path.getFinalPath().size()-1))){

                int nextFloor = util.floor;

                Node nextFloorStart = null;
                Node nextFloorEnd = null;

                for(int i=0;i<allPaths.size();i++){

                    if(util.idToFloor(allPaths.get(i).getFloorID()) != util.idToFloor(floorPaths.get(0).getFloorID())){
                        if(i < allPaths.size()){
                            System.out.println("Looking at next floor");
                            List<Node> nextPath = allPaths.get(i).getPath();

                            nextFloor = util.idToFloor(nextPath.get(0).getFloor());
                            nextFloorStart = nextPath.get(0);
                            nextFloorEnd = nextPath.get(nextPath.size()-1);
                        }
                    }
                }

                if(nextFloor != util.idToFloor(start.getFloor())){
                    System.out.println("Creating");
                    Button startChangeButton = new Button();
                    startChangeButton.setText("TAKE THE "+(startNodeType.equals("ELEV") ? "ELEVATOR" : "STAIRS")+" TO "+util.getFloorLabel(nextFloor).toUpperCase());
                    MapPoint mp = util.scalePoints(end.getX(), end.getY());
                    startChangeButton.setLayoutX(mp.x);
                    startChangeButton.setLayoutY(mp.y);
                    startChangeButton.setStyle("-fx-background-color: white; -fx-border-width: .2em; -fx-border-color:  black; -fx-text-fill: black; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-cursor: hand;-fx-font-size:10px; -fx-font-family: \"Open Sans Bold\"");
                    startChangeButton.setMinWidth(100);
                    startChangeButton.setMinHeight(15);
                    startChangeButton.setOpacity(0.5);
                    startChangeButton.setOnMouseEntered((ov) -> {startChangeButton.setOpacity(1);});
                    startChangeButton.setOnMouseExited((ov) -> {startChangeButton.setOpacity(0.5);});

                    final int nf = nextFloor;
                    final Node ns = nextFloorStart;
                    final Node ne = nextFloorEnd;
                    startChangeButton.setOnAction(evt -> {
                        try{
                            util.setFloor(nf);
                            floorLabel.setText(util.getFloorLabel());
                            this.updateMap(ns, ne);
                        }catch(Exception e){e.printStackTrace();}
                    });
                    util.buttonPane.getChildren().add(startChangeButton);
                    clearNodes.add(startChangeButton);
                }

            }

            String endNodeType = start.getNodeType();
            System.out.println("END NODE: "+end.getLongName()+" | ENDNODETYPE: "+endNodeType);

            if((endNodeType.equals("ELEV") || endNodeType.equals("STAI")) && !start.getId().equals(path.getFinalPath().get(0)) && !start.getId().equals(path.getFinalPath().get(path.getFinalPath().size()-1))){
                int nextFloor = util.floor;
                System.out.print(allPaths.size());
                for(int i=0;i<allPaths.size();i++){
                    if(util.idToFloor(allPaths.get(i).getFloorID()) != util.idToFloor(floorPaths.get(0).getFloorID())){

                        if(i >= 0){
                            System.out.println("Looking at previous floor");
                            for(int j=0;j<util.dbPrefixes.length;j++){
                                nextFloor = util.idToFloor(allPaths.get(i).getPath().get(0).getFloor());
                            }
                        }
                    }
                }

                if(nextFloor != util.idToFloor(start.getFloor())){
                    System.out.println("Creating Previous");
                    Button startChangeButton = new Button();
                    startChangeButton.setText("BACK (TO "+util.getFloorLabel(nextFloor)+")");
                    MapPoint mp = util.scalePoints(start.getX(), start.getY());
                    startChangeButton.setLayoutX(mp.x);
                    startChangeButton.setLayoutY(mp.y);
                    startChangeButton.setStyle("-fx-background-color: white; -fx-border-width: .2em; -fx-border-color:  black; -fx-text-fill: black; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-cursor: hand;-fx-font-size:10px; -fx-font-family: \"Open Sans Bold\"");
                    startChangeButton.setMinWidth(100);
                    startChangeButton.setMinHeight(15);
                    startChangeButton.setOpacity(0.5);
                    startChangeButton.setOnMouseEntered((ov) -> {startChangeButton.setOpacity(1);});
                    startChangeButton.setOnMouseExited((ov) -> {startChangeButton.setOpacity(0.5);});

                    final int nf = nextFloor;

                    startChangeButton.setOnAction(evt -> {
                        try{
                            util.setFloor(nf);
                            floorLabel.setText(util.getFloorLabel());
                            this.updateMap(null, null);
                        }catch(Exception e){e.printStackTrace();}
                    });
                   //util.buttonPane.getChildren().add(startChangeButton);
                    //clearNodes.add(startChangeButton);
                }
            }

        });
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
