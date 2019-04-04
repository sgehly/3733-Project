package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;

import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.MapPoint;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Node;
import edu.wpi.cs3733.d19.teamM.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
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

    double scaledWidth;
    double scaledHeight;

    MapPoint dragDelta = new MapPoint(0,0);
    MapPoint initial = new MapPoint(0,0);

    Rectangle2D primaryScreenBounds;

    String dbPath = "jdbc:derby:myDB";

    @FXML
    private Pane imageView;

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
        Main.setScene("home");
    }

    @FXML
    private void addEdge() throws Exception{
        String startNode = startNodeTextBox.getText();
        String endNode = endNodeTextBox.getText();

        Floor floorOne = new Floor("1");
        floorOne.addEdge(startNode+"_"+endNode);
    }

    @FXML
    private void removeEdge() throws Exception{
        String startNode = startNodeTextBox.getText();
        String endNode = endNodeTextBox.getText();

        Floor floorOne = new Floor("1");
        floorOne.removeEdge(startNode+"_"+endNode);
    }

    @FXML
    private void addNode() throws Exception{
        Floor floorOne = new Floor("1");
        String nodeID = nodeIdTextBox.getText();
        String xCoord = xCoordTextBox.getText();
        String yCoord = yCoordTextBox.getText();
        String floor = floorTextBox.getText();
        String building = buildingTextBox.getText();
        String type = typeTextBox.getText();
        String longName = longNameTextBox.getText();
        String shortName = shortNameTextBox.getText();
        floorOne.addNode(nodeID, Integer.valueOf(xCoord), Integer.valueOf(yCoord), floor, building, type, longName, shortName);
        this.getAllRecords();
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

    @FXML
    private void removeNode() throws Exception{
        Floor floorOne = new Floor("1");
        String nodeID = nodeIdTextBox.getText();
        System.out.println(nodeID);
        Node theNode = floorOne.getFloorMap().get(nodeID);
        System.out.println(theNode.getXCoord());
        floorOne.removeNode(theNode);
        this.getAllRecords();
    }


    @FXML
    private void updateNode(){
        try{
            System.out.println(1);
            updateHelper();
        }
        catch(Throwable e) {
            e.printStackTrace();
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(Main.getStage());
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Label("NODE NOT FOUND"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }

    private void updateHelper() throws Exception{
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            PreparedStatement stmt = conn.prepareStatement("update NODE set xcoord = ?, ycoord = ?, nodetype = ?,  longname = ?, shortname = ? where nodeid = ?");
            stmt.setString(1, xCoordTextBox.getText());
            stmt.setString(2, yCoordTextBox.getText());
            stmt.setString(3, typeTextBox.getText());
            stmt.setString(4, longNameTextBox.getText());
            stmt.setString(5, shortNameTextBox.getText());
            stmt.setString(6, nodeIdTextBox.getText());
            stmt.executeUpdate();
            conn.close();

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn2 = DriverManager.getConnection(dbPath);
            PreparedStatement stmt2 = conn2.prepareStatement("update FLOOR1 set xcoord = ?, ycoord = ?, nodetype = ?, longname = ?, shortname = ? where nodeid = ?");
            stmt2.setString(1, xCoordTextBox.getText());
            stmt2.setString(2, yCoordTextBox.getText());
            stmt2.setString(3, typeTextBox.getText());
            stmt2.setString(4, longNameTextBox.getText());
            stmt2.setString(5, shortNameTextBox.getText());
            stmt2.setString(6, nodeIdTextBox.getText());
            System.out.println("ping");
            stmt2.executeUpdate();
            System.out.println("pong");
            conn2.close();
            this.getAllRecords();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private MapPoint scalePoints(int pointX, int pointY){
        double rawWidth = 5000;
        double rawHeight = 3400;

        System.out.println(scaledHeight+" - "+scaledWidth);
        double scaledX = (pointX*scaledWidth)/rawWidth;
        double scaledY = (pointY*scaledHeight)/rawHeight;
        System.out.println(scaledX+" - "+scaledY);
        return new MapPoint(scaledX, scaledY);

    }

    public void updateValues(String nodeId) throws Exception{
        System.out.println(nodeId);
        //TODO: Can someone on database make this so SQL Injection can't happen
        String query = "SELECT * FROM FLOOR1 WHERE NODEID = ?";
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection conn = DriverManager.getConnection(dbPath);
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

                System.out.println("New Button! ("+String.valueOf(rs.getInt("xcoord")-(size/2))+","+String.valueOf(rs.getInt("ycoord")-(size/2))+") -- ("+String.valueOf(primaryScreenBounds.getWidth())+","+String.valueOf(primaryScreenBounds.getHeight()-200)+") => ("+generated.x+","+generated.y+")");
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
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
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
        String filename = "Romminfo.csv";
        try {
            FileWriter fw = new FileWriter(filename);
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection("jdbc:derby:myDB;create=true");
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

}
