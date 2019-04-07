package edu.wpi.cs3733.d19.teamM.common.map;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.MapPoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MapUtils {

    Pane buttonContainer;
    ImageView image;
    Pane imageView;
    ImageView overlayImage;
    Rectangle2D primaryScreenBounds;
    EventHandler<ActionEvent> callback;

    private String[] images = {"00_thelowerlevel2.png", "00_thelowerlevel1.png", "01_thefirstfloor.png", "02_thesecondfloor.png", "03_thethirdfloor.png"};
    private String[] labels = {"Lower Level 2", "Lower Level 1", "Floor One", "Floor Two", "Floor Three"};
    private String[] dbPrefixes = {"L2", "L1", "1", "2", "3"};
    private Image[] imageFiles = new Image[5];

    int floor = 2;

    //Create needed object instances
    double cachedScaledWidth = 0;
    double cachedScaledHeight = 0;

    public MapUtils(Pane buttonContainer, Pane imageView, ImageView image, ImageView overlayImage, EventHandler<ActionEvent> callback) {
        this.buttonContainer = buttonContainer;
        this.image = image;
        this.imageView = imageView;
        this.callback = callback;
        this.overlayImage = overlayImage;
    }

    /**
     * This method scales all the points that are displayed on the map
     * @param pointX: The x coordinate
     * @param pointY: The y coordinate
     * @return MapPoint: The point on the map that we have scaled
     */
    private MapPoint scalePoints(int pointX, int pointY){
        //The literal width and height of the image
        double rawWidth = 5000;
        double rawHeight = 3400;

        //The scaled width and height of the image
        if(cachedScaledHeight == 0){
            cachedScaledHeight = imageView.getBoundsInParent().getHeight()-50;
        }
        if(cachedScaledWidth == 0){
            cachedScaledWidth = imageView.getBoundsInParent().getWidth();
        }

        //Scale the x and y coordinates and set / return as a new map point
        double scaledX = (pointX*cachedScaledWidth)/rawWidth;
        double scaledY = (pointY*cachedScaledHeight)/rawHeight;
        return new MapPoint(scaledX, scaledY);

    }

    /**
     * This method gets the current objects from the DatabaseUtils and returns it as an Observable List
     * @param rs: The corresponding result set
     * @return ObservableList<DisplayTable>: A list with displayable tables to view in the UI the database entries for the service requests
     * @throws SQLException : Any SQL errors that might occur while trying to get the service requests
     */
    private ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws Exception, SQLException {
        //The list we will populate
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        buttonContainer.getChildren().clear();
        try {
            while (rs.next()) {
                //Create a button and set its size
                javafx.scene.control.Button newButton = new Button();
                double size = 5;
                newButton.setMinWidth(size);
                newButton.setMaxWidth(size);
                newButton.setMinHeight(size);
                newButton.setPrefHeight(size);
                newButton.setPrefWidth(size);
                newButton.setMaxHeight(size);

                //Set its id to the node that it will be representing
                newButton.setId(rs.getString("nodeId"));
                newButton.setOnAction(callback);

                //Generate a map point out of the node button and place it on the screen and make it blue
                MapPoint generated = scalePoints(rs.getInt("xcoord"), rs.getInt("ycoord"));
                newButton.setLayoutX(generated.x-(size/2));
                newButton.setLayoutY(generated.y-(size/2));
                newButton.getStylesheets().add("resources/mapNode.css");
                buttonContainer.getChildren().add(newButton); //Add it to the button container
            }
            return entList; //Return this list
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * This method gets all the records from the database so that they can be added to the display on the screen
     * @return ObservableList<DisplayTable>: The List of records that we want to actually display on the screen from the service requests
     * @throws ClassNotFoundException: If classes are not found
     * @throws SQLException: Any issues with the database
     */
    public ObservableList<DisplayTable> getAllRecords(int floor) throws ClassNotFoundException, SQLException, Exception {
        //Get the query from the database

        Image source = imageFiles[floor];
        image.setImage(source);
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight() - 200);

        //Gets the overlay image and sets the width and the height of that
        Image EMPTY = new Image(Main.getResource("/resources/maps/emptyOverlay.png")); //See if we can get the image to overlay and then create a new image object from it

        //Initially set the image to empty and get the width and height
        overlayImage.setImage(EMPTY);
        overlayImage.setFitWidth(primaryScreenBounds.getWidth());
        overlayImage.setFitHeight(primaryScreenBounds.getHeight() - 200);

        //Get the buttons on the screen and set the preferred width and height to that of the image
        buttonContainer.setPrefWidth(image.getFitWidth());
        buttonContainer.setPrefHeight(image.getFitHeight());

        String query = "SELECT * FROM NODE WHERE FLOOR='"+this.getCurrentFloorID()+"'";
        try {
            //Get the information that we want from the database
            Connection conn = new DatabaseUtils().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Store the results we get in the entry list display table
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }


    public void initialize() throws Exception{
        primaryScreenBounds = Screen.getPrimary().getVisualBounds(); //Get the bounds of the screen

        for(int i=0;i<this.images.length;i++){
            imageFiles[i] = new Image(Main.getResource("/resources/maps/"+this.images[i]));;
        }

        this.getAllRecords(this.floor);
    }

    public void moveUp() throws Exception{
        int newFloor = this.floor+1;
        if(newFloor > dbPrefixes.length-1){
            //ERROR, no more floors!;
            return;
        }
        this.floor = newFloor;
        this.getAllRecords(this.floor);
    }

    public void moveDown() throws Exception{
        int newFloor = this.floor-1;
        if(newFloor < 0){
            //ERROR, no more floors!;
            return;
        }
        this.floor = newFloor;
        this.getAllRecords(this.floor);
    }

    public String getFloorLabel(){
        return labels[this.floor];
    }

    public String getCurrentFloorID(){
        return dbPrefixes[this.floor];
    }

}
