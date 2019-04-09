package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;

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
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.shape.Line;
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
    private ScrollPane buttonContainer;

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
        resetTextFields();
    }

    @FXML
    private void removeEdge(){
        nodeLabel.setVisible(false);
        String edgeID = startNodeTextBox.getText() + "_" + endNodeTextBox.getText();
        removeE(edgeID);
        resetTextFields();
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
        resetTextFields();
    }

    @FXML
    private void removeNode(){
        edgeLabel.setVisible(false);
        removeN();
        resetTextFields();
    }

    @FXML
    private void updateNode(){
        edgeLabel.setVisible(false);
        updateN();
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

    public void updateValues(String nodeId) throws Exception{

        if(storedYellowButton != null){
            storedYellowButton.setStyle("");
        }

        routeArr.forEach(route -> {
            util.buttonPane.getChildren().remove(route);
        });

        System.out.println(nodeId);
        //TODO: Can someone on database make this so SQL Injection can't happen
        String query = "SELECT * FROM NODE WHERE LONGNAME = ?";
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

                System.out.println(oneX+","+oneY+" -> "+twoX+","+twoY+" ~~ "+Math.max(Math.abs(twoX-oneX),50)+"x"+Math.max(50, Math.abs(twoY-oneY)));

                Line pathObj = new Line();
                pathObj.setStartX(oneX+2.5);
                pathObj.setStartY(oneY+2.5);
                pathObj.setEndX(twoX+2.5);
                pathObj.setEndY(twoY+2.5);
                pathObj.setStyle("-fx-background-color: red");
                routeArr.add(pathObj);
                util.buttonPane.getChildren().add(pathObj);
            }
        }

        conn.close();
    }

    private void setValues(ActionEvent value){
        try {
            String nodeId = ((Button)value.getSource()).getId();
            this.updateValues(nodeId);
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

    @FXML
    protected void initialize() throws Exception {

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        edgeLabel.setVisible(false);
        nodeLabel.setVisible(false);
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();


        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        graph = new Floor();
        path = new Path();
        util = new MapUtils(buttonContainer, imageView, image, new ImageView(), new JFXSlider(), this::setValues, this::clickValues);
        util.initialize();

    }

    @FXML
    public void logout() throws Exception{
        Main.setScene("welcome");
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

            nodeLabel.setTextFill(Color.GREEN);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node Removed!");
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
            PreparedStatement stmt = conn.prepareStatement("update NODE set xcoord = ?, ycoord = ?, nodetype = ?,  longname = ?, shortname = ? where nodeid = ?");
            stmt.setString(1, xCoordTextBox.getText());
            stmt.setInt(2, Integer.parseInt(yCoordTextBox.getText()));
            stmt.setInt(3, Integer.parseInt(typeTextBox.getText()));
            stmt.setString(4, longNameTextBox.getText());
            stmt.setString(5, shortNameTextBox.getText());
            stmt.setString(6, nodeIdTextBox.getText());
            stmt.executeUpdate();
            conn.close();
            nodeLabel.setTextFill(Color.GREEN);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node Updated!");
        }
        catch(Exception e){
            e.printStackTrace();
            nodeLabel.setTextFill(Color.RED);
            nodeLabel.setVisible(true);
            nodeLabel.setText("Node Not Updated!");
        }
    }

}
