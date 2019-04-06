package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;

import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.MapPoint;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Node;
import edu.wpi.cs3733.d19.teamM.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.sql.*;


public class AdminUI {

    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static double offSetX,offSetY,zoomlvl;
    String dbPath = "jdbc:derby:myDB";

    double scaledWidth;
    double scaledHeight;

    MapPoint dragDelta = new MapPoint(0,0);
    MapPoint initial = new MapPoint(0,0);

    Rectangle2D primaryScreenBounds;

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
    private Pane buttonContainer;

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
    private void navigateToHome() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    private void addEdge() throws Exception{
        String edgeID = startNodeTextBox.getText() + "_" + endNodeTextBox.getText();
        addE(edgeID);
        resetTextFields();
    }

    @FXML
    private void removeEdge(){
        String edgeID = startNodeTextBox.getText() + "_" + endNodeTextBox.getText();
        removeE(edgeID);
        resetTextFields();
    }

    @FXML
    private void addNode(){
        addN();
        resetTextFields();
    }

    @FXML
    private void removeNode(){
        removeN();
        resetTextFields();
    }

    @FXML
    private void updateNode(){
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
        System.out.println(nodeId);
        //TODO: Can someone on database make this so SQL Injection can't happen
        String query = "SELECT * FROM node WHERE NODEID = ?";
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

    private ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws Exception, SQLException {
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        buttonContainer.getChildren().clear();
        try {
            while (rs.next()) {
                Button newButton = new Button();
                double size = 5;
                String id = rs.getString("nodeId");

                newButton.setMinWidth(size);
                newButton.setMaxWidth(size);
                newButton.setMinHeight(size);
                newButton.setPrefHeight(size);
                newButton.setPrefWidth(size);
                newButton.setMaxHeight(size);
                newButton.setId(id);
                newButton.setOnAction(this::setValues);

                MapPoint generated = scalePoints(rs.getInt("xcoord"), rs.getInt("ycoord"));
                newButton.setLayoutX(generated.x-(size/2));
                newButton.setLayoutY(generated.y-(size/2));
                newButton.setStyle("-fx-background-color: blue");

                buttonContainer.getChildren().add(newButton);
            }
            return entList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }


    public ObservableList<DisplayTable> getAllRecords() throws ClassNotFoundException, SQLException, Exception {
        String query = "SELECT * FROM FLOOR1";
        try {
            Connection conn = new DatabaseUtils().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            conn.close();
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    protected void initialize() throws Exception {

        new Clock(lblClock, lblDate);

        primaryScreenBounds = Screen.getPrimary().getVisualBounds();



        Image source = new Image(Main.getResource("/resources/maps/01_thefirstfloor.png"));

        image.setImage(source);
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight()-200);

        scaledWidth = imageView.getBoundsInParent().getWidth();
        scaledHeight = imageView.getBoundsInParent().getHeight()-50;

        buttonContainer.setPrefWidth(image.getFitWidth());
        buttonContainer.setPrefHeight(image.getFitHeight());
        getAllRecords();
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
        }
        catch(Exception e){
            e.printStackTrace();
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
        }catch(Exception e){
            e.printStackTrace();
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
        }catch(Exception e){
            e.printStackTrace();
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
        } catch(Exception e){
            e.printStackTrace();
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
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
