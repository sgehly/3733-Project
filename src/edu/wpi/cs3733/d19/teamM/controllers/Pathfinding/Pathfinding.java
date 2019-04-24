package edu.wpi.cs3733.d19.teamM.controllers.Pathfinding;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
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
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Path;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import externalLibraries.Arrow.*;
import org.w3c.dom.css.Rect;
import sun.font.TextLabel;

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
    ArrayList<Line> lines = new ArrayList<Line>();
    ArrayList<Rectangle> arrows = new ArrayList<Rectangle>();
    ArrayList<javafx.scene.shape.Path> paths = new ArrayList<javafx.scene.shape.Path>();

    private Map<String, String> longNameMap;


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

    @FXML
    private ListView directoryList;

    @FXML
    private VBox directoryPane;

    @FXML
    private VBox navPane;

    @FXML
    private Button dirButton;

    @FXML
    private Button navButton;

    @FXML
    private Button showDir;

    @FXML
    private Button sendRobotButton;

    @FXML
    private TitledPane filters;

    @FXML
    private Button textToSpeech;

    @FXML
    private Button clearPathButton;

    private List<String> filterOut;


    @FXML
    protected void clearPath(){
        lines.forEach(node -> util.buttonPane.getChildren().remove(node));
        arrows.forEach(node -> util.buttonPane.getChildren().remove(node));
        clearNodes.forEach(node -> util.buttonPane.getChildren().remove(node));
        paths.forEach(path -> util.buttonPane.getChildren().remove(path));
        clocks.forEach(clock -> {
            clock.stop();
        });

        path = null;

        textToSpeech.setDisable(true);
        clearPathButton.setDisable(true);
        sendRobotButton.setDisable(true);
        showDir.setDisable(true);

        textSpeech.quitSpeaking();
        textToSpeech.setText("SPEAK DIRECTIONS");
    }
    /**
     * This method will initialize the pathfinding screen's controller
     * @throws Exception: Any exception that arises in the screen
     */
    @FXML
    protected void initialize() throws Exception {
        textToSpeech.setText("SPEAK DIRECTIONS");

        textToSpeech.setDisable(true);
        clearPathButton.setDisable(true);
        showDir.setDisable(true);
        sendRobotButton.setDisable(true);

        //scheduling.setVisible(false);
        filters.setExpanded(false);
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        longNameMap = new HashMap<>();

        sendRobotButton.setDisable(true);

        gesturePane.setContent(mappingStuff);

        graph = Floor.getFloor();
        path = new Path();
        util = new MapUtils(buttonContainer, imageView, image, overlayImage, zoomSlider, this::setValues, this::clickValues, false, null, null);
        setUpListeners();
        util.initialize();
        floorLabel.setText(util.getFloorLabel());

//        hearDir.setDisable(true);
        showDir.setDisable(true);
        showDir.setText("NO DIRECTIONS");
        filterOut = new ArrayList<>();
        filterOut.add("HALL");
        loadDirectory(filterOut);

        chooseNav();
    }
    Image imageBox;
    ImageView imageBoxView;
    VBox dialogBoxVbox;
    Label tf = new Label();

    Button buttonBox;
    File pathFile = new File("resource.txt");
    Scanner directionsScanner;
    Stage dialogBox;

    @FXML
    private void showText(){

        {
            try {
                directionsScanner = new Scanner(pathFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
         dialogBox = new Stage();
        tf.setTextAlignment(TextAlignment.CENTER);
        Font myFont = new Font("Open Sans",20);

        tf.setFont(myFont);
        directionsScanner.nextLine();
        //TODO: Create the VBOX
        //TODO: Get the text directions
        //TODO: For each set of text directions
            //TODO: Set the image and the text and the next button
        dialogBox.initModality(Modality.APPLICATION_MODAL);
        dialogBox.initOwner(Main.getStage());

        dialogBoxVbox = new VBox(20);
        buttonBox = new Button();
        buttonBox.setText("NEXT DIRECTION");
        imageBox = new Image("resources/icons/street_view.png");
        imageBoxView = new ImageView();
        imageBoxView.setImage(imageBox);
        tf.setWrapText(true);
        tf.setText(directionsScanner.nextLine());
        tf.setPrefSize(400,200);
        dialogBoxVbox.getChildren().add(imageBoxView);
        dialogBoxVbox.setAlignment(Pos.CENTER);
        dialogBoxVbox.getChildren().add(tf);
        dialogBoxVbox.getChildren().add(buttonBox);
        buttonBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updateBox();
            }
        });
        Scene dialogScene = new Scene(dialogBoxVbox, 450, 600);
        dialogBox.setScene(dialogScene);
        dialogBox.setTitle("Text Directions");
        dialogBox.show();
    }

    private void updateBox()
    {

        String line = directionsScanner.nextLine();
        if(line.isEmpty())
        {
            updateBox();
        }
        else
        {
            if(line.toLowerCase().contains("straight"))
            {
                imageBoxView.setImage(new Image("resources/icons/circled_straight.png"));

            }
            else if(line.toLowerCase().contains("right"))
            {
                imageBoxView.setImage(new Image("resources/icons/circled_right.png"));

            }
            else if(line.toLowerCase().contains("left"))
            {
                imageBoxView.setImage(new Image("resources/icons/circled_left.png"));

            }
            else if(line.toLowerCase().contains("stair"))
            {
                imageBoxView.setImage(new Image("resources/icons/stairs.png"));
            }
            else if(line.toLowerCase().contains("elevator"))
            {
                imageBoxView.setImage(new Image("resources/icons/elevator.png"));
            }

            tf.setText(line);

            if(line.toUpperCase().contains("ARRIVE") && !line.toUpperCase().contains("ELEVATOR") && !line.toUpperCase().contains("STAIR"))
            {
                imageBoxView.setImage(new Image("resources/icons/arrived.png"));
                buttonBox.setText("CLOSE");
                buttonBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        dialogBox.close();
                    }
                });
            }
        }
    }

    private void loadDirectory(List<String> filterOut){
        ObservableList<String> nodeList = FXCollections.observableArrayList();
        directoryList.getItems().clear();
        for (Node n : graph.getNodes().values()){
            for (String s : filterOut) {
                if (!n.getNodeType().equals(s)) {
                    String nodeName = n.getLongName();
                    nodeList.add("[" + n.getFloor() + "] " + n.getLongName() + " \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<" + n.getId() + ">");
                }
            }
        }

        FXCollections.sort(nodeList); // sorted directory alphabetically

        directoryList.setItems(nodeList);
        directoryList.setEditable(false);
        directoryList.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });
        directoryList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                // Your action here
                String nodeId = newValue.split("<")[1].replace(">","");

                if(nodeId != null){
                    Floor graph = Floor.getFloor();
                    if(startText.getText().length() == 0){
                        startText.setText(nodeId);
                        for(Node n : graph.getNodes().values()){
                            if(n.getId().equals(startText.getText())){
                                System.out.println("found it!");
                                try {
                                        String floor = n.getFloor();
                                        util.setFloor(floor);
                                }catch (Exception e){}
                            }
                        }
                        endText.requestFocus();
                    }else{
                        endText.setText(nodeId);
                    }
                }
            }
        });
    }

    @FXML
    private void chooseNav(){
        directoryPane.setMouseTransparent(true);
        directoryPane.setOpacity(0);
        navPane.setMouseTransparent(false);
        navPane.setOpacity(1);
        dirButton.setStyle("");
        navButton.setStyle("-fx-background-color: white; -fx-text-fill: black");
    }

    @FXML
    private void chooseDirectory(){
        directoryPane.setMouseTransparent(false);
        directoryPane.setOpacity(1);
        navPane.setMouseTransparent(true);
        navPane.setOpacity(0);
        navButton.setStyle("");
        dirButton.setStyle("-fx-background-color: white; -fx-text-fill: black");
    }

    //A global int to keep track of whether the thing is speaking or not
    TextSpeech textSpeech = new TextSpeech();
    MediaPlayer player = textSpeech.getMediaPlayer();


    @FXML
    private void handleSpeaking(){
        player.setOnEndOfMedia(() -> {
            textSpeech.quitSpeaking();
            textToSpeech.setText("SPEAK DIRECTIONS");
        });
        if(textToSpeech.getText().equals("SPEAK DIRECTIONS") && showDir.getText().equals("TEXT DIRECTIONS")){
            textToSpeech.setText("CANCEL SPEAKING");
            textSpeech.speakToUser();
        }
        else{
            textSpeech.quitSpeaking();
            textToSpeech.setText("SPEAK DIRECTIONS");
        }
    }


    private void clickValues(MouseEvent evt){}

    /**
     * This method lets the user navigate back to the home page
     * @throws Exception
     */
    @FXML
    private void navigateToHome() throws Exception{
        filters.setExpanded(false);
       // scheduling.setVisible(false);
        lines.forEach(node -> util.buttonPane.getChildren().remove(node));
        arrows.forEach(node -> util.buttonPane.getChildren().remove(node));
        clearNodes.forEach(node -> util.buttonPane.getChildren().remove(node));
        paths.forEach(path -> util.buttonPane.getChildren().remove(path));
        util = null;
        Main.setScene("home");
        if(textToSpeech.getText().equals("CANCEL SPEAKING")) {
            handleSpeaking();
        }
    }

    @FXML
    private void navigateToScheduling() throws Exception{
        filters.setExpanded(false);
        //scheduling.setVisible(false);
        lines.forEach(node -> util.buttonPane.getChildren().remove(node));
        arrows.forEach(node -> util.buttonPane.getChildren().remove(node));
        clearNodes.forEach(node -> util.buttonPane.getChildren().remove(node));
        paths.forEach(path -> util.buttonPane.getChildren().remove(path));
        Main.setScene("scheduling");
        if(textToSpeech.getText().equals("CANCEL SPEAKING")) {
            handleSpeaking();
        }
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
        Node startNode = getNodeFromString(start);
        if (startNode != null) {
            path = graph.findPresetPath(startNode, type, graph.getNodes());
        }
        else {
            path = null;
        }

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        util.setFloor(path.getFinalPath().get(path.getFinalPath().size()-1).getFloor());
        floorLabel.setText(util.getFloorLabel());
        PathToString.getDirections(path);

        if (path != null){
            showDir.setDisable(false);
            showDir.setText("TEXT DIRECTIONS");
        }

        Path curPath = path.getFloorPaths().get(0);

        updateMap(curPath.getPath().get(0), curPath.getPath().get(curPath.getPath().size() - 1));
        resetTextBox();
    }



    private void findPath(Node startNode, Node endNode) throws Exception{
        //SocketClient s = new SocketClient();
        //Get path
        path = graph.findPath(startNode, endNode);
        PathToString.getDirections(path);

        util.setFloor(path.getFinalPath().get(0).getFloor());
        floorLabel.setText(util.getFloorLabel());
        if (path != null){
            showDir.setDisable(false);
            sendRobotButton.setDisable(false);
            showDir.setText("TEXT DIRECTIONS");
        }

        resetTextBox();

        Path curPath = path.getFloorPaths().get(0);

        updateMap(curPath.getPath().get(0), curPath.getPath().get(curPath.getPath().size() - 1));
    }

    @FXML
    void sendRobot(){
        Runnable robotThread = () -> {
            try {
                SocketClient s = new SocketClient();
                s.toConnString(PathToString.pathToInstructions(path));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(robotThread).start();
    }

    private void filterNodes(String s) {
        util.filterNodes(s);
        filterOut.add(s);
        loadDirectory(filterOut);
    }

    private void unfilterNodes(String s) {
        util.unfilterNodes(s);
        filterOut.remove(s);
        loadDirectory(filterOut);
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
                longNames.add(node.getLongName() + " on Floor: " + node.getFloor());
                longNameMap.put(node.getLongName() + " on Floor: " + node.getFloor(), node.getId());
            }
        }
        startText.textProperty().addListener((ov, oldValue, newValue) -> {
            AutoCompletionBinding<String> meta = TextFields.bindAutoCompletion(startText,longNames);
            meta.setOnAutoCompleted((ev) -> {
                endText.requestFocus();
            });
        });
        endText.textProperty().addListener((ov, oldValue, newValue) -> {
            AutoCompletionBinding<String> meta = TextFields.bindAutoCompletion(endText,longNames);
            meta.setOnAutoCompleted((ev) -> {
                String start = startText.getText();
                String end = endText.getText();
                try{
                    checkInputAndRunSearch(start, end);
                }
                catch (Exception e){

                };
            });
            String start = startText.getText();
            String end = endText.getText();
            try{
                checkInputAndRunSearch(start, end);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    private void checkInputAndRunSearch(String str1, String str2) throws Exception {
        Node sN = getNodeFromString(str1);
        Node eN = getNodeFromString(str2);

        if (sN != null && eN != null) findPath(sN, eN);
    }

    private Node getNodeFromString(String str){
        Node n = null;

        if (graph.getNodes().containsKey(str)) {
            n = graph.getNodes().get(str);
        }
        else if (graph.getNodes().containsKey(longNameMap.get(str))){
            n = graph.getNodes().get(longNameMap.get(str));
        }

        return n;
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
        /*if(floorLabel.getText().equals("Floor Four")){
            scheduling.setVisible(true);
        }
        else{
            scheduling.setVisible(false);
        }*/
        updateMap(null,null);
    }

    public void moveDown(ActionEvent value) throws Exception{
        util.moveDown();
        floorLabel.setText(util.getFloorLabel());
        if(floorLabel.getText().equals("Floor Four")){
            //scheduling.setVisible(true);
        }
        else{
            //scheduling.setVisible(false);
        }
        updateMap(null,null);
    }

    private Button generateButton(String text, double x, double y){
        return new Button();
    }

    ArrayList<Timeline> clocks = new ArrayList<Timeline>();

    private void travelPath(Rectangle traveller, ArrayList<MapPoint> points, int index, int clockIndex){

        if(index > points.size()-1){
            clocks.get(clockIndex).stop();
            traveller.setX(points.get(0).x);
            traveller.setY(points.get(0).y);
            this.travelPath(traveller, points, 0, clockIndex);
            return;
        }

        double finalX = points.get(index).x;
        double finalY = points.get(index).y;
        if(clockIndex > clocks.size()-1){
            clocks.add(new Timeline());
        }
        clocks.set(clockIndex, new Timeline(new KeyFrame(Duration.ZERO, e -> {

            double currentX = traveller.getX();
            double currentY = traveller.getY();

            if((int)currentX == (int)finalX && (int)currentY == (int)finalY){
                if(clockIndex < clocks.size()){
                    clocks.get(clockIndex).stop();
                }
                travelPath(traveller, points, index+1, clockIndex);
                return;
            }
            if(currentX < finalX){
                traveller.setX(currentX+0.01);
            }
            if(currentX > finalX){
                traveller.setX(currentX-0.01);
            }

            if(currentY < finalY){
                traveller.setY(currentY+0.01);
            }
            if(currentY > finalY){
                traveller.setY(currentY-0.01);
            }

            traveller.setTranslateX(-traveller.getWidth()/2);
            traveller.setTranslateY(-traveller.getHeight()/2);
        }), new KeyFrame(Duration.seconds(0.0005))));

        clocks.get(clockIndex).setCycleCount(Animation.INDEFINITE);
        clocks.get(clockIndex).play();
    }

    private void zoomToPath(Node startN, Node endN){
        double startX = startN.getX();
        double startY = startN.getY();

        double endX = endN.getX();
        double endY = endN.getY();

        double newXRaw = startN.getX() - ((startN.getX()-endN.getX()) / 2);
        double newYRaw = startN.getY() - ((startN.getY()-endN.getY()) / 2);

        double deltaX = Math.abs((startN.getX()-endN.getX()) / 2);
        double deltaY = Math.abs((startN.getY()-endN.getY()) / 2);

        gesturePane.reset();
        double scale = deltaX > deltaY ? gesturePane.getWidth() / deltaX : gesturePane.getHeight() / deltaY;
        MapPoint p = util.scalePoints((int)newXRaw,(int)newYRaw);
        gesturePane.zoomBy(scale * 0.6, new Point2D(p.x, p.y));
    }

    private void updateMap(Node startNode, Node endNode) throws Exception{
        clearNodes.forEach(node -> util.buttonPane.getChildren().remove(node));
        lines.forEach(node -> util.buttonPane.getChildren().remove(node));
        arrows.forEach(node -> util.buttonPane.getChildren().remove(node));
        paths.forEach(path -> util.buttonPane.getChildren().remove(path));

        clocks.forEach(clock -> {
            clock.stop();
        });

        clocks.removeAll(clocks);

        List<Path> floorPaths = path.getSpecificPath(util.getCurrentFloorID());
        List<Path> allPaths = path.getFloorPaths();

        if (floorPaths != null){
            int clockIndex = 0;
            for(Path path : floorPaths){
                List<Node> nodes = path.getPath();

                //lol
                final javafx.scene.shape.Path travellerPath = new javafx.scene.shape.Path();

                for(int i=0;i<nodes.size()-1;i++){

                    MapPoint start = util.scalePoints(nodes.get(i).getX(), nodes.get(i).getY());
                    MapPoint end = util.scalePoints(nodes.get(i+1).getX(), nodes.get(i+1).getY());

                    if(i == 0){
                        travellerPath.getElements().add(new MoveTo(start.x, start.y));
                    }

                    travellerPath.getElements().add(new LineTo(end.x, end.y));

                    Line line = new Line();
                    line.setStartX(start.x);
                    line.setStartY(start.y);

                    line.setEndX(end.x);
                    line.setEndY(end.y);
                    line.setStrokeWidth(2);

                    line.setStroke(Color.valueOf("#012d5a"));

                    double midX = start.x+(Math.abs(end.x-start.x)/2);
                    double midY = start.y+(Math.abs(end.y-start.y)/2);

                    Arrow arrow = new Arrow(start.x, start.y, end.x, end.y, 4);
                    arrow.setFill(Color.valueOf("#f6bd38"));
                    //util.buttonPane.getChildren().add(line);
                    arrow.setScaleX(0.5);
                    arrow.setScaleY(0.5);
                    line.setStyle("-fx-border-radius:5px");
                    lines.add(line);
                }

                for(int i=0;i<travellerPath.getElements().size();i++){
                    //System.out.println("("+((LineTo)travellerPath.getElements().get(i)).getX()+","+((LineTo)travellerPath.getElements().get(i)).getY()+")");
                }

                Rectangle traveller = new Rectangle();

                if(travellerPath.getElements().size() != 0){

                    traveller.setX(((MoveTo)travellerPath.getElements().get(0)).getX());
                    traveller.setY(((MoveTo)travellerPath.getElements().get(0)).getY());
                    traveller.setWidth(10);
                    traveller.setHeight(10);
                    System.out.println("Generate!");
                    traveller.setFill(Color.web("#f6bd38"));
                    traveller.setStroke(Color.web("#012d5a"));
                    travellerPath.setOpacity(1);
                    travellerPath.setStrokeWidth(3);
                    traveller.setStrokeWidth(1);

                    paths.add(travellerPath);

                    util.buttonPane.getChildren().add(travellerPath);
                    util.buttonPane.getChildren().add(traveller);
                    arrows.add(traveller);
                    traveller.toFront();
                    traveller.setArcHeight(999);
                    traveller.setArcWidth(999);

                    final PathTransition pathTransition = new PathTransition();

                    pathTransition.setDuration(Duration.seconds(travellerPath.getElements().size()/2));
                    pathTransition.setDelay(Duration.seconds(0));
                    pathTransition.setPath(travellerPath);
                    pathTransition.setNode(traveller);
                    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition.setCycleCount(Timeline.INDEFINITE);
                    pathTransition.setAutoReverse(false);
                    pathTransition.play();

                    clockIndex++;
                }

                travellerPath.toFront();
                traveller.toFront();

            }
        }
        else {
            overlayImage.setImage(null);
        }

        arrows.forEach(node -> node.toFront());
        util.nodes.forEach(node -> {
            node.toFront();
        });

        textToSpeech.setDisable(false);
        clearPathButton.setDisable(false);
        showDir.setDisable(false);
        sendRobotButton.setDisable(false);

        if(startNode != null && endNode != null){
            //We zoomin boys

            zoomToPath(startNode, endNode);

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
                startChangeButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-cursor: hand;-fx-font-size:8px; -fx-font-family: \"Open Sans Bold\"");
                startChangeButton.setMinWidth(75);
                startChangeButton.setMinHeight(10);
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
                startChangeButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-cursor: hand;-fx-font-size:8px; -fx-font-family: \"Open Sans Bold\"");
                startChangeButton.setMinWidth(75);
                startChangeButton.setMinHeight(10);
                startChangeButton.setOpacity(0.5);
                startChangeButton.setOnMouseEntered((ov) -> {startChangeButton.setOpacity(1);});
                startChangeButton.setOnMouseExited((ov) -> {startChangeButton.setOpacity(0.5);});
                util.buttonPane.getChildren().add(startChangeButton);
                clearNodes.add(startChangeButton);
            }

            //if(start.getId().equals(end.getId())) return;
            //ELEV or STAI

            String startNodeType = end.getNodeType();
            System.out.println("START NODE: "+start.getLongName()+" | STARTNODETYPE: "+startNodeType);

            if((startNodeType.equals("ELEV") || startNodeType.equals("STAI")) && !end.getId().equals(path.getFinalPath().get(0)) && !end.getId().equals(path.getFinalPath().get(path.getFinalPath().size()-1))){

                int nextFloor = util.floor;

                Node nextFloorStart = null;
                Node nextFloorEnd = null;

                for (int i = 0; i <= allPaths.size() - 2; i++){
                    if (allPaths.get(i).getPath().get(0).getId().equals(start.getId())){
                        while (allPaths.get(i + 1).getPath().size() < 2 && i < allPaths.size() - 1) {
                            i++;
                        }
                        nextFloorStart = allPaths.get(i + 1).getPath().get(0);
                        nextFloorEnd = allPaths.get(i + 1).getPath().get(allPaths.get(i + 1).getPath().size() - 1);
                        nextFloor = util.idToFloor(nextFloorStart.getFloor());
                    }
                }

                if(nextFloor != util.idToFloor(start.getFloor())){
                    System.out.println("Creating");
                    Button startChangeButton = new Button();
                    startChangeButton.setText("TAKE THE "+(startNodeType.equals("ELEV") ? "ELEVATOR" : "STAIRS")+" "+(nextFloor > util.floor ? "UP" : "DOWN")+" TO "+util.getFloorLabel(nextFloor).toUpperCase());
                    MapPoint mp = util.scalePoints(end.getX(), end.getY());
                    startChangeButton.setLayoutX(mp.x);
                    startChangeButton.setLayoutY(mp.y);
                    startChangeButton.setStyle("-fx-background-color: #012d5a; -fx-border-width: 1px; -fx-border-color:  #00172f; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 7px;-fx-cursor: hand; -fx-font-family: \"Open Sans Bold\"; -fx-font-size:6px;");
                    startChangeButton.setMinWidth(75);
                    startChangeButton.setMinHeight(10);
                    startChangeButton.setOpacity(0.5);
                    startChangeButton.setOnMouseEntered((ov) -> {startChangeButton.setOpacity(1);});
                    startChangeButton.setOnMouseExited((ov) -> {startChangeButton.setOpacity(0.5);});

                    //if (nextFloor == 6) nextFloor = 4;
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
                    startChangeButton.setStyle("-fx-background-color: #012d5a; -fx-border-width: 1px; -fx-border-color:  #00172f; -fx-text-fill: white; -fx-border-radius: 5px; -fx-background-radius: 7px;-fx-cursor: hand; -fx-font-family: \"Open Sans Bold\"; -fx-font-size:6px;");
                    startChangeButton.setMinWidth(75);
                    startChangeButton.setMinHeight(10);
                    startChangeButton.setOpacity(0.5);
                    startChangeButton.setOnMouseEntered((ov) -> {startChangeButton.setOpacity(1);});
                    startChangeButton.setOnMouseExited((ov) -> {startChangeButton.setOpacity(0.5);});

                    //if (nextFloor == 6) nextFloor = 4;
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
        util = null;
        Main.logOut();
        if(textToSpeech.getText().equals("CANCEL SPEAKING")) {
            handleSpeaking();
        }
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
