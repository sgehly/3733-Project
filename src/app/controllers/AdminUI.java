package app.controllers;

import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class AdminUI {

    public class MapPoint {
        double x;
        double y;

        public MapPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static double offSetX,offSetY,zoomlvl;

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
    private void navigateToHome() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    private MapPoint scalePoints(int pointX, int pointY){
        double rawWidth = 5000;
        double rawHeight = 3400;

        double scaledWidth = imageView.getBoundsInParent().getWidth();
        double scaledHeight = imageView.getBoundsInParent().getHeight()-50;

        System.out.println(scaledHeight+" - "+scaledWidth);
        double scaledX = (pointX*scaledWidth)/rawWidth;
        double scaledY = (pointY*scaledHeight)/rawHeight;
        System.out.println(scaledX+" - "+scaledY);
        return new MapPoint(scaledX, scaledY);

    }

    private void setValues(ActionEvent value){
        try {
            String nodeId = ((Button)value.getSource()).getId();
            System.out.println(nodeId);
            //TODO: Can someone on database make this so SQL Injection can't happen
            String query = "SELECT * FROM FLOOR1 WHERE NODEID = ?";
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nodeId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                nodeIdTextBox.setText(rs.getString("nodeID"));
                xCoordTextBox.setText(String.valueOf(rs.getInt("xCoord")));
                yCoordTextBox.setText(String.valueOf(rs.getInt("yCoord")));
                floorTextBox.setText(rs.getString("floor"));
                buildingTextBox.setText(rs.getString("building"));
                typeTextBox.setText(rs.getString("type"));
                longNameTextBox.setText(rs.getString("longName"));
                shortNameTextBox.setText(rs.getString("shortName"));
            }

        }
        catch (Exception e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
        }
    }

    private ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws Exception, SQLException {
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                Button newButton = new Button();
                double size = 5;
                newButton.setMinWidth(size);
                newButton.setMaxWidth(size);
                newButton.setMinHeight(size);
                newButton.setPrefHeight(size);
                newButton.setPrefWidth(size);
                newButton.setMaxHeight(size);
                newButton.setId(rs.getString("nodeId"));
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
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    protected void initialize() throws Exception {

        //Adapted from: https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx
        //------------------------------------------------------------------------------------------------
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        Image source = null;
        try {
            String path = getClass().getResource("/resources/maps/01_thefirstfloor.png").toString().replace("file:","");
            System.out.println(path);
            source = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double ratio = source.getWidth() / source.getHeight();

        if (500 / ratio < 500) {
            width = 500;
            height = (int) (500 / ratio);
        } else if (500 * ratio < 500) {
            height = 500;
            width = (int) (500 * ratio);
        } else {
            height = 500;
            width = 500;
        }
        image.setImage(source);
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight()-200);

        buttonContainer.setPrefWidth(image.getFitWidth());
        buttonContainer.setPrefHeight(image.getFitHeight());
        getAllRecords();
    }

    @FXML
    public void logout() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

}
