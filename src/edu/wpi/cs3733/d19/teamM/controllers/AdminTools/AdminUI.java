package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXSlider;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.common.map.MapUtils;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Path;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.MapPoint;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Node;
import edu.wpi.cs3733.d19.teamM.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.shape.Line;
import net.kurobako.gesturefx.GesturePane;

import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdminUI {

    static double initx;
    static double inity;
    static int height;
    static int width;
    static double offSetX,offSetY,zoomlvl;
    String dbPath = "jdbc:derby:myDB";

    double scaledWidth;
    double scaledHeight;

    ArrayList<Line> routeArr = new ArrayList<Line>();

    MapPoint dragDelta = new MapPoint(0,0);
    MapPoint initial = new MapPoint(0,0);

    MapUtils util;

    Rectangle2D primaryScreenBounds;

    Floor graph;
    Path path;

    Button storedYellowButton;

    @FXML
    private Pane imageView;

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private ImageView image;

    @FXML
    private Slider zoomLvl;

    @FXML
    private Text userText;

    @FXML
    private Pane buttonContainer;

    @FXML
    private JFXSlider zoomSlider;

    @FXML
    private Text floorLabel;

    @FXML
    private TextField nodeIdTextBox;

    @FXML
    private TextField xCoordTextBox;

    @FXML
    private TextField yCoordTextBox;

    @FXML
    private TextField floorTextBox;

    @FXML
    private TextField buildingTextBox;

    @FXML
    private TextField typeTextBox;

    @FXML
    private TextField longNameTextBox;

    @FXML
    private TextField shortNameTextBox;

    @FXML
    private TextField startNodeTextBox;

    @FXML
    private TextField endNodeTextBox;

    @FXML
    private Label edgeLabel;

    @FXML
    private Label nodeLabel;

    @FXML
    private RadioButton AStar;

    @FXML
    private RadioButton BFS;

    @FXML
    private RadioButton DFS;

    @FXML
    private RadioButton Dijkstra;

    @FXML
    private VBox mapStuff;

    @FXML
    private GesturePane gesturePane;

    @FXML
    private JFXButton searchAlgorithmButton;
    @FXML
    private JFXButton AStarButton;
    @FXML
    private JFXButton BFSButton;
    @FXML
    private JFXButton DFSButton;
    @FXML
    private JFXButton DStarButton;


    @FXML
    private void navigateToHome() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
        nodeLabel.setVisible(false);
        edgeLabel.setVisible(false);
    }

    @FXML
    private void addEdge() throws Exception{
        nodeLabel.setVisible(false);
        String edgeID = startNodeTextBox.getText() + "_" + endNodeTextBox.getText();
        addE(edgeID);

        graph = Floor.getFloor();

        resetTextFields();
    }

    @FXML
    private void removeEdge(){
        nodeLabel.setVisible(false);
        String edgeID = startNodeTextBox.getText() + "_" + endNodeTextBox.getText();
        removeE(edgeID);
        graph = Floor.getFloor();
        resetTextFields();
    }

    @FXML
    private void changeAlgorithm(){
        graph = Floor.getFloor();
        if(AStar.isSelected()){
            graph.setAStar();
        }
        else if(DFS.isSelected()){
            graph.setDFS();
        }
        else if(BFS.isSelected()){
            graph.setBFS();
        }
        else if (Dijkstra.isSelected()){
            graph.setDijkstra();
        }
        else{
            graph.setAStar();
        }
    }

    @FXML
    private void newUser() throws Exception {
        Main.loadAddUsers();
        Main.setScene("addUser");
    }

    @FXML
    private void addNode(){
        edgeLabel.setVisible(false);
        addN();
        graph = Floor.getFloor();
        resetTextFields();
    }

    @FXML
    private void removeNode(){
        edgeLabel.setVisible(false);
        removeN();
        graph = Floor.getFloor();
        resetTextFields();
    }

    @FXML
    private void updateNode(){
        edgeLabel.setVisible(false);
        updateN();
        graph = Floor.getFloor();
        resetTextFields();
    }

    private void resetTextFields(){
        nodeIdTextBox.setText("");
        xCoordTextBox.setText("");
        yCoordTextBox.setText("");
        floorTextBox.setText("");
        buildingTextBox.setText("");
        typeTextBox.setText("");
        longNameTextBox.setText("");
        shortNameTextBox.setText("");
    }

    private MapPoint scalePoints(int pointX, int pointY){
        double rawWidth = 5000;
        double rawHeight = 3400;

        double scaledX = (pointX*scaledWidth)/rawWidth;
        double scaledY = (pointY*scaledHeight)/rawHeight;
        return new MapPoint(scaledX, scaledY);

    }

    Line edgeCreationLine;
    Node edgeCreationStorage;
    boolean isDrag;

    public void updateValues(String nodeId, boolean shiftDown) throws Exception{

        if(storedYellowButton != null){
            storedYellowButton.setStyle("");
        }

        if(isDrag){
            isDrag = false;
            return;
        }

        routeArr.forEach(route -> {
            util.buttonPane.getChildren().remove(route);
        });


        System.out.println("Updating values...");
        //TODO: Can someone on database make this so SQL Injection can't happen
        String query = "SELECT * FROM NODE WHERE NODEID = ?";
        Connection conn = new DatabaseUtils().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, nodeId);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()){
            System.out.println(rs.getString("nodeType"));
            System.out.println(rs.getString("longName"));
            System.out.println(rs.getString("shortName"));
            nodeIdTextBox.setText(rs.getString("nodeID"));
            xCoordTextBox.setText(String.valueOf(rs.getInt("xCoord")));
            yCoordTextBox.setText(String.valueOf(rs.getInt("yCoord")));
            floorTextBox.setText(rs.getString("floor"));
            buildingTextBox.setText(rs.getString("building"));
            typeTextBox.setText(rs.getString("nodeType"));
            longNameTextBox.setText(rs.getString("longName"));
            shortNameTextBox.setText(rs.getString("shortName"));

            if(shiftDown){
                if(edgeCreationStorage != null){
                    addE(edgeCreationStorage.getId()+"_"+rs.getString("nodeID"));
                    edgeCreationStorage = null;
                    edgeCreationLine = null;
                    startNodeTextBox.setText("");
                    endNodeTextBox.setText("");
                }else{
                    edgeCreationStorage = graph.getNodes().get(rs.getString("nodeID"));
                    double oX = util.buttonMap.get(edgeCreationStorage.getLongName()).getLayoutX();
                    double oY = util.buttonMap.get(edgeCreationStorage.getLongName()).getLayoutY();

                    startNodeTextBox.setText(rs.getString("nodeID"));

                    Line pathObj = new Line();
                    pathObj.setStartX(oX+3);
                    pathObj.setStartY(oY+3);
                    pathObj.setEndX(oX+3);
                    pathObj.setEndY(oY+3);
                    pathObj.setStrokeWidth(3);
                    pathObj.setMouseTransparent(true);
                    pathObj.setStroke(Color.web("#012d5a"));
                    edgeCreationLine = pathObj;
                    util.buttonPane.getChildren().add(pathObj);
                }
            }


            String edgeQuery = "SELECT * FROM EDGE WHERE STARTNODE = ? OR ENDNODE = ?";
            PreparedStatement edgeStatement = conn.prepareStatement(edgeQuery);
            edgeStatement.setString(1, rs.getString("nodeID"));
            edgeStatement.setString(2, rs.getString("nodeID"));
            ResultSet edges = edgeStatement.executeQuery();

            while(edges.next()){
                System.out.println("Edge Found: "+edges.getString("edgeID"));
                String otherNodeId = edges.getString("edgeID").replace("_","").replace(rs.getString("nodeID"),"");
                Node one = graph.getNodes().get(otherNodeId);
                Node two = graph.getNodes().get(rs.getString("nodeID"));
                System.out.println(one+" / "+two);
                System.out.println("Path from "+one.getLongName()+" to "+two.getLongName());

                if(util.buttonMap.get(one.getLongName()) == null || util.buttonMap.get(two.getLongName()) == null){
                    //It's a multifloor
                    if(util.buttonMap.get(one.getLongName()) == null){
                        storedYellowButton = util.buttonMap.get(two.getLongName());
                        util.buttonMap.get(two.getLongName()).setStyle("-fx-background-color: yellow;-fx-scale-x: 1.5;-fx-scale-y: 1.5");
                    }else{
                        //Two is multifloor.
                        storedYellowButton = util.buttonMap.get(one.getLongName());
                        util.buttonMap.get(one.getLongName()).setStyle("-fx-background-color: yellow;-fx-scale-x: 1.5;-fx-scale-y: 1.5");
                    }
                    continue;
                }
                double oneX = util.buttonMap.get(one.getLongName()).getLayoutX();
                double oneY = util.buttonMap.get(one.getLongName()).getLayoutY();

                double twoX = util.buttonMap.get(two.getLongName()).getLayoutX();
                double twoY = util.buttonMap.get(two.getLongName()).getLayoutY();


                Line pathObj = new Line();
                pathObj.setStartX(oneX+3);
                pathObj.setStartY(oneY+3);
                pathObj.setEndX(twoX+3);
                pathObj.setEndY(twoY+3);
                pathObj.setStrokeWidth(3);
                pathObj.setMouseTransparent(true);
                pathObj.setStroke(Color.web("#012d5a"));
                routeArr.add(pathObj);
                util.buttonPane.getChildren().add(pathObj);
            }
        }

        conn.close();
    }


    private void setValues(MouseEvent value){
        try {
            String nodeId = ((Button)value.getSource()).getId();

            if(nodeId.equals(nodeIdTextBox.getText())){
                routeArr.forEach(node -> {
                    util.buttonPane.getChildren().remove(node);
                });
                nodeIdTextBox.setText("");
                longNameTextBox.setText("");
                shortNameTextBox.setText("");
                floorTextBox.setText("");
                buildingTextBox.setText("");
                typeTextBox.setText("");
                return;
            }

            this.updateValues(nodeId, value.isShiftDown());
        }
        catch (Exception e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
        }
    }

    private void clickValues(MouseEvent value){
        System.out.println("("+value.getX()+","+value.getY()+")");
        System.out.println("("+value.getSceneX()+","+value.getSceneY()+")");

        MapPoint original = util.scalePointReversed(value.getX(), value.getY());
        xCoordTextBox.setText(String.valueOf((int)original.x));
        yCoordTextBox.setText(String.valueOf((int)original.y));

        nodeIdTextBox.setText("");
        longNameTextBox.setText("");
        shortNameTextBox.setText("");
        floorTextBox.setText("");
        buildingTextBox.setText("");
        typeTextBox.setText("");

    }

    private void hoverCallback(MouseEvent value){
        if(value.isShiftDown() && edgeCreationLine != null){
            edgeCreationLine.setEndX(edgeCreationLine.getLayoutX()+value.getX()-2.5);
            edgeCreationLine.setEndY(edgeCreationLine.getLayoutY()+value.getY()-2.5);
        }else{
            startNodeTextBox.setText("");
            endNodeTextBox.setText("");
            edgeCreationStorage = null;
            util.buttonPane.getChildren().remove(edgeCreationLine);
            edgeCreationLine = null;
        }
    }

    private void dragCallback(MouseEvent value){

        isDrag = true;

        Button toMove = (Button)value.getSource();

        try{
            this.updateValues(toMove.getId(), false);
        }catch(Exception e){
            e.printStackTrace();
        }

        /*routeArr.forEach(node -> {
            route.setStartX(toMove.getLayoutX()+2.5);
            route.setStartY(toMove.getLayoutY()+2.5);
        });*/

        System.out.println("("+value.getX()+","+value.getY()+")");
        toMove.setLayoutX(toMove.getLayoutX()+value.getX()-2.5);
        toMove.setLayoutY(toMove.getLayoutY()+value.getY()-2.5);

        MapPoint original = util.scalePointReversed(toMove.getLayoutX()+value.getX()-2.5, toMove.getLayoutY()+value.getY()-2.5);

        xCoordTextBox.setText(String.valueOf((int)original.x));
        yCoordTextBox.setText(String.valueOf((int)original.y));

        routeArr.forEach(route -> {
            route.setEndX(toMove.getLayoutX()+2.5);
            route.setEndY(toMove.getLayoutY()+2.5);
        });

    }

    @FXML
    protected void initialize() throws Exception {
        this.setupAlgorithmsButton();
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        gesturePane.setContent(mapStuff);

        //nodeIdTextBox.setOpacity(0);
        //nodeIdTextBox.setDisable(true);
        //userText.setText("");

        edgeLabel.setVisible(false);
        nodeLabel.setVisible(false);
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();


        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        graph = Floor.getFloor();
        path = new Path();
        util = new MapUtils(buttonContainer, imageView, image, new ImageView(), new JFXSlider(), this::setValues, this::clickValues, true, this::dragCallback, this::hoverCallback);
        util.initialize();

    }

    private void setupAlgorithmsButton() {


    }

    @FXML
    public void logout() throws Exception{
        Main.logOut();
    }

    @FXML
    private void exportToCsv(ActionEvent event) throws  SQLException,ClassNotFoundException{
        String filename = "BWRoomExport.csv";
        try {
            FileWriter fw = new FileWriter(filename);
            Connection conn = new DatabaseUtils().getConnection();
            String query = "select * from node";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                fw.append(rs.getString(1));
                fw.append(',');
                fw.append(rs.getString(2));
                fw.append(',');
                fw.append(rs.getString(3));
                fw.append(',');
                fw.append(rs.getString(4));
                fw.append(',');
                fw.append(rs.getString(5));
                fw.append(',');
                fw.append(rs.getString(6));
                fw.append(',');
                fw.append(rs.getString(7));
                fw.append(',');
                fw.append(rs.getString(8));
                fw.append('\n');
            }
            fw.flush();
            fw.close();
            conn.close();
            System.out.println("CSV File is created successfully.");
        } catch (Exception ev) {
            ev.printStackTrace();
        }
    }

    private void addN(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            PreparedStatement stmt = conn.prepareStatement("insert into NODE values (?,?,?,?,?,?,?,?)");
            stmt.setString(1, nodeIdTextBox.getText());
            stmt.setInt(2, Integer.parseInt(xCoordTextBox.getText()));
            stmt.setInt(3, Integer.parseInt(yCoordTextBox.getText()));
            stmt.setString(4, floorTextBox.getText());
            stmt.setString(5, buildingTextBox.getText());
            stmt.setString(6, typeTextBox.getText());
            stmt.setString(7, longNameTextBox.getText());
            stmt.setString(8, shortNameTextBox.getText());
            stmt.executeUpdate();
            conn.close();
            nodeLabel.setTextFill(Color.GREEN);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node Added!");
            Node newNode = new Node(nodeIdTextBox.getText(), Integer.parseInt(xCoordTextBox.getText()), Integer.parseInt(yCoordTextBox.getText()), floorTextBox.getText(), buildingTextBox.getText(), typeTextBox.getText(), longNameTextBox.getText(), shortNameTextBox.getText());
            graph.getNodes().put(nodeIdTextBox.getText(), newNode);
            util.getAllRecords(util.floor);
        }
        catch(Exception e){
            e.printStackTrace();
            nodeLabel.setTextFill(Color.RED);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node not added!");
        }
    }

    public void removeN(){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt2 = conn.prepareStatement("delete from node where nodeID = ?");
            stmt2.setString(1, nodeIdTextBox.getText());
            stmt2.execute();
            conn.close();

            graph.getNodes().remove(nodeIdTextBox.getText());

            nodeLabel.setTextFill(Color.GREEN);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node Removed!");
            util.initialize();
            util.getAllRecords(util.floor);
        }catch(Exception e){
            e.printStackTrace();
            nodeLabel.setTextFill(Color.RED);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node Not Removed!");
        }
    }

    public void removeE(String edgeID){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt = conn.prepareStatement("delete from edge where edgeID = ?");
            stmt.setString(1, edgeID);
            stmt.execute();
            conn.close();
            edgeLabel.setTextFill(Color.GREEN);
            edgeLabel.setVisible(true);
            edgeLabel.setText("Edge Removed!");

            String[] edgeNodes = edgeID.split("_");
            graph.getNodes().get(edgeNodes[0]).removeEdge(graph.getNodes().get(edgeNodes[1]));
            graph.getNodes().get(edgeNodes[1]).removeEdge(graph.getNodes().get(edgeNodes[0]));

            util.getAllRecords(util.floor);
        }catch(Exception e){
            e.printStackTrace();
            edgeLabel.setTextFill(Color.RED);
            edgeLabel.setVisible(true);
            edgeLabel.setText("Edge does not exist!");
        }
    }

    public void addE(String edgeID){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            String[] edgeNodes = edgeID.split("_");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
            PreparedStatement stmt = conn.prepareStatement("insert into edge values(?,?,?)");
            stmt.setString(1, edgeID);
            stmt.setString(2, edgeNodes[0]);
            stmt.setString(3, edgeNodes[1]);
            stmt.execute();
            conn.close();
            edgeLabel.setTextFill(Color.GREEN);
            edgeLabel.setVisible(true);
            edgeLabel.setText("Edge Added!");
            graph.getNodes().get(edgeNodes[0]).addEdge(graph.getNodes().get(edgeNodes[1]), edgeID);
            graph.getNodes().get(edgeNodes[1]).addEdge(graph.getNodes().get(edgeNodes[0]), edgeID);
            util.getAllRecords(util.floor);
        } catch(Exception e){
            e.printStackTrace();
            edgeLabel.setTextFill(Color.RED);
            edgeLabel.setVisible(true);
            edgeLabel.setText("Edge already exists!");
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
        List<Path> floorPath = path.getSpecificPath(util.getCurrentFloorID());
        if (floorPath != null){
            //overlayImage.setImage(graph.drawPath(floorPath));
        }
        else {
           // overlayImage.setImage(null);
        }
    }

    private void updateN(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            PreparedStatement stmt = conn.prepareStatement("update NODE set xcoord = ?, ycoord = ?, nodetype = ?, floor = ?, building = ?, longname = ?, shortname = ? where nodeid = ?");
            stmt.setInt(1, Integer.parseInt(xCoordTextBox.getText()));
            stmt.setInt(2, Integer.parseInt(yCoordTextBox.getText()));
            stmt.setString(3, typeTextBox.getText());
            stmt.setString(4, floorTextBox.getText());
            stmt.setString(5, buildingTextBox.getText());
            stmt.setString(6, shortNameTextBox.getText());
            stmt.setString(7, longNameTextBox.getText());
            stmt.setString(8, nodeIdTextBox.getText());
            stmt.executeUpdate();
            conn.close();

            Node nodeToModify = graph.getNodes().get(nodeIdTextBox.getText());
            nodeToModify.setBuilding(buildingTextBox.getText());
            nodeToModify.setLongName(longNameTextBox.getText());
            nodeToModify.setShortName(shortNameTextBox.getText());
            nodeToModify.setNodeType(typeTextBox.getText());
            nodeToModify.setFloor(floorTextBox.getText());
            nodeToModify.setX(Integer.parseInt(xCoordTextBox.getText()));
            nodeToModify.setY(Integer.parseInt(yCoordTextBox.getText()));

            nodeLabel.setTextFill(Color.GREEN);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node Updated!");
            util.getAllRecords(util.floor);
        }
        catch(Exception e){
            e.printStackTrace();
            nodeLabel.setTextFill(Color.RED);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node Not Updated!");
        }
    }

}
