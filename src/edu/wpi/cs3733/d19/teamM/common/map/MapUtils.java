package edu.wpi.cs3733.d19.teamM.common.map;

import com.jfoenix.controls.JFXSlider;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.MapPoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class MapUtils {

    Pane buttonContainer;
    public Pane buttonPane;
    ImageView image;
    Pane imageView;
    ImageView overlayImage;
    JFXSlider zoomSlider;
    Rectangle2D primaryScreenBounds;
    EventHandler<ActionEvent> callback;
    EventHandler<MouseEvent> clickCallback;
    boolean showHallways = false;


    private String[] images = {"00_thegroundfloor.png", "00_thelowerlevel1.png", "00_thelowerlevel2.png",  "01_thefirstfloor.png", "02_thesecondfloor.png", "03_thethirdfloor.png"};
    private String[] labels = {"Ground Floor",  "Lower Level 1", "Lower Level 2", "Floor One", "Floor Two", "Floor Three"};
    public String[] dbPrefixes = {"G", "L1", "L2", "1", "2", "3"};
    private HashMap<Integer, Image> imageFiles = new HashMap<Integer, Image>();

    public int floor = 3;
    int width;
    int height;
    double offSetX;
    double offSetY;
    double zoom;
    double initx;
    double inity;

    double deltax;
    double deltay;

    Image EMPTY = new Image(Main.getResource("/resources/maps/emptyOverlay.png")); //See if we can get the image to overlay and then create a new image object from it

    public HashMap<String, Button> buttonMap = new HashMap<String,Button>();

    //Create needed object instances
    public double cachedScaledWidth = 0;
    public double cachedScaledHeight = 0;

    public MapUtils(Pane buttonContainer, Pane imageView, ImageView image, ImageView overlayImage, JFXSlider zoomSlider, EventHandler<ActionEvent> callback, EventHandler<MouseEvent> clickCallback, boolean showHallways) {
        this.buttonContainer = buttonContainer;
        this.buttonPane = new Pane();
        buttonPane.setLayoutY(buttonContainer.getLayoutY());
        buttonPane.setLayoutX(buttonContainer.getLayoutX());
        //buttonPane.setStyle("-fx-border-color: red;-fx-border-width: 3px");

        //buttonContainer.setStyle("-fx-border-color: blue;-fx-border-width: 5px");
        this.buttonContainer.getChildren().add(buttonPane);
        this.image = image;
        this.imageView = imageView;
        this.callback = callback;
        this.overlayImage = overlayImage;
        this.zoomSlider = zoomSlider;
        this.clickCallback = clickCallback;
        this.showHallways = showHallways;
    }

    /**
     * This method scales all the points that are displayed on the map
     * @param pointX: The x coordinate
     * @param pointY: The y coordinate
     * @return MapPoint: The point on the map that we have scaled
     */
    public MapPoint scalePoints(int pointX, int pointY){
        //The literal width and height of the image
        double rawWidth = 5000;
        double rawHeight = 3400;

        //The scaled width and height of the image
        if(cachedScaledHeight == 0){
            cachedScaledHeight = imageView.getBoundsInParent().getHeight()-45;
            buttonPane.setPrefHeight(cachedScaledHeight);
        }
        if(cachedScaledWidth == 0){
            cachedScaledWidth = imageView.getBoundsInParent().getWidth();
            buttonPane.setPrefWidth(cachedScaledWidth);
        }

        //Scale the x and y coordinates and set / return as a new map point
        double scaledX = (pointX*cachedScaledWidth)/rawWidth;
        double scaledY = (pointY*cachedScaledHeight)/rawHeight;
        return new MapPoint(scaledX, scaledY);

    }

    public MapPoint scalePointReversed(double scaledX, double scaledY){
        double rawWidth = 5000;
        double rawHeight = 3400;

        double originalX = (scaledX*rawWidth)/cachedScaledWidth;
        double originalY = (scaledY*rawHeight)/cachedScaledHeight;

        return new MapPoint(originalX, originalY);
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
        buttonPane.getChildren().clear();
        try {
            while (rs.next()) {
                //Create a button and set its size
                if(!showHallways){
                    if(rs.getString("nodeType").equals("HALL")){
                        continue;
                    }
                }

                javafx.scene.control.Button newButton = new Button();
                double size = 6;
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
                buttonPane.getChildren().add(newButton); //Add it to the button container
                buttonMap.put(rs.getString("longName"), newButton);
            }
            return entList; //Return this list
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void filterNodes(String type){
        Floor g = Floor.getFloor();
        for (Node b : buttonPane.getChildren()){
            for (edu.wpi.cs3733.d19.teamM.utilities.AStar.Node n : g.getNodes().values()) {
                if (n.getId().equals(b.getId()) && n.getNodeType().equals(type)) {
                    n.disable();
                    b.setVisible(false);
                }
            }
        }
    }

    public void unfilterNodes(String type){
        Floor g = Floor.getFloor();
        for (Node b : buttonPane.getChildren()){
            for (edu.wpi.cs3733.d19.teamM.utilities.AStar.Node n : g.getNodes().values()) {
                if (n.getId().equals(b.getId()) && n.getNodeType().equals(type)) {
                    n.enable();
                    b.setVisible(true);
                }
            }
        }
    }

    public void updatePosition(double newValue){
        /*System.out.println("MINVALS: ("+image.getViewport().getMinX()+","+image.getViewport().getMinY()+")");
        System.out.println("LAYOUTS: "+image.getFitWidth()+"x"+image.getFitHeight());
        //double xBase = ((image.getViewport().getMinX()/zoom)-(image.getViewport().getMinX()/zoom)+(image.getFitWidth()/zoom));
        //double yBase = ((image.getViewport().getMinY()/zoom)-(image.getViewport().getMinY()/zoom)+(image.getFitHeight()/zoom));

        buttonContainer.setScaleX(zoom);
        buttonContainer.setScaleY(zoom);

        double xBase = -image.getViewport().getMinX()+width/2;
        double yBase = (image.getViewport().getMinY()+height)/2;

        buttonContainer.setTranslateX(xBase);
        buttonContainer.setTranslateY(yBase);*/

     //   buttonContainer.setTranslateX((image.getViewport().getMinX()-image.getViewport().getMinX())/(zoom/2));
      //  buttonContainer.setTranslateY((image.getViewport().getMinY()-image.getViewport().getMinY())/(zoom/2));




    //    double xBase = ((image.getViewport().getMinX()/zoom)+image.getViewport().getMinX());
     //   double yBase = ((image.getViewport().getMinY()/zoom)+image.getViewport().getMinY());

       // System.out.println(xBase+","+yBase);
     //   buttonContainer.setTranslateX(xBase);
      //  buttonContainer.setTranslateY(yBase);
        //System.out.println("Changing position");
    //    Rectangle boundy = new Rectangle(image.getViewport().getMinX(), image.getViewport().getMinY(), image.getViewport().getWidth(), image.getViewport().getHeight());
        //System.out.println(bounds.toString());

    //   Rectangle2D bounds = new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue);
        //System.out.println(bounds.toString());

      //  buttonContainer.setViewportBounds(boundy.getLayoutBounds());

   //     overlayImage.setViewport(bounds);

  //      image.setViewport(bounds);
        //System.out.println(bounds);
    //    Rectangle boundRect = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        //System.out.println(boundRect.toString());
       // buttonContainer.setViewportBounds(boundRect.getLayoutBounds());*/

        //Rectangle2D bounds = new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue);
       // image.setViewport(bounds);
        //overlayImage.setViewport(image.getViewport());
    }

    /**
     * This method gets all the records from the database so that they can be added to the display on the screen
     * @return ObservableList<DisplayTable>: The List of records that we want to actually display on the screen from the service requests
     * @throws ClassNotFoundException: If classes are not found
     * @throws SQLException: Any issues with the database
     */
    public ObservableList<DisplayTable> getAllRecords(int floor) throws ClassNotFoundException, SQLException, Exception {
        //Get the query from the database
        System.out.println("123");
        Image source = imageFiles.get(floor);
        image.setImage(source);
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight() - 200);
        //image.setStyle("-fx-border-color: pink;-fx-border-width: 5px");
        //Gets the overlay image and sets the width and the height of that

        //Initially set the image to empty and get the width and height
        overlayImage.setImage(EMPTY);
        //overlayImage.setStyle("-fx-border-color: orange;-fx-border-width: 5px");
        overlayImage.setFitWidth(primaryScreenBounds.getWidth());
        overlayImage.setFitHeight(primaryScreenBounds.getHeight() - 200);

        //Get the buttons on the screen and set the preferred width and height to that of the image
        buttonContainer.setPrefWidth(cachedScaledWidth);
        buttonContainer.setPrefHeight(cachedScaledHeight);
        buttonContainer.setMaxWidth(cachedScaledWidth);
        buttonContainer.setMaxHeight(cachedScaledHeight);
        buttonContainer.setMinWidth(cachedScaledWidth);
        buttonContainer.setMinHeight(cachedScaledHeight);
       // buttonContainer.setStyle("-fx-border-color: aqua;-fx-border-width: 5px");


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

        height = (int) source.getHeight();
        width = (int) source.getWidth();

        System.out.println("SOURCE: 5000x3400");
        System.out.println("SCALED: "+image.getFitWidth()+"x"+image.getFitHeight());

        zoomSlider.setMax(10);
        zoomSlider.setMin(1);
        zoomSlider.setValue(1);

        offSetX = width / 2;
        offSetY = height / 2;

        Slider Hscroll = new Slider();
        Hscroll.setMin(0);
        Hscroll.setMax(width);
        Hscroll.setMaxWidth(image.getFitWidth());
        Hscroll.setMinWidth(image.getFitWidth());
       // Hscroll.setTranslateY(-999999);
        Slider Vscroll = new Slider();
        Vscroll.setMin(0);
        Vscroll.setMax(height);
        Vscroll.setMaxHeight(image.getFitHeight());
        Vscroll.setMinHeight(image.getFitHeight());
        Vscroll.setOrientation(Orientation.VERTICAL);
        //Vscroll.setTranslateX(-9999);

        /*Hscroll.valueProperty().addListener(e -> {
            offSetX = Hscroll.getValue();
            zoom = zoomSlider.getValue();
            double newValue = (double) ((int) (zoom * 10)) / 10;
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }

            System.out.println("HORIZONTAL SCROLL:"+offSetY+" - "+inity+" = "+(offSetY-inity));

            updatePosition(newValue);

        });

        Vscroll.valueProperty().addListener(e -> {
            offSetY = height - Vscroll.getValue();
            zoom = zoomSlider.getValue();
            double newValue = (double) ((int) (zoom * 10)) / 10;
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }

            System.out.println("VERTICAL SCROLL:"+offSetY+" - "+inity+" = "+(offSetY-inity));

            updatePosition(newValue);
        });*/

        /*zoomSlider.valueProperty().addListener(e -> {
            zoom = zoomSlider.getValue();
            double newValue = (double) ((int) (zoom * 10)) / 10;
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }

            double minX = offSetX - ((width / newValue) / 2);
            double minY = offSetY - ((height / newValue) / 2);

            Hscroll.setValue(width-offSetX);
            Vscroll.setValue(height-offSetY);

            buttonPane.setScaleX(newValue);
            buttonPane.setScaleY(newValue);

            Rectangle2D bounds = new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue);
            image.setViewport(bounds);
            overlayImage.setViewport(image.getViewport());

            buttonPane.setTranslateX(0);
            buttonPane.setTranslateY(0);
            buttonContainer.setViewportBounds(overlayImage.getLayoutBounds());

            //System.out.println(minX+"/"+minY+"/"+bounds.getMinX()+"/"+bounds.getMinY());


        });*/
     //   double yikes;
       // double oof;
       //buttonPane.setCursor(Cursor.OPEN_HAND);
        buttonPane.setOnMousePressed(e -> {
          //  initx = e.getSceneX();
           /// inity = e.getSceneY();
            //buttonPane.setCursor(Cursor.CLOSED_HAND);
            clickCallback.handle(e);
        });
        /*buttonPane.setOnMouseReleased(e -> {
            buttonPane.setCursor(Cursor.OPEN_HAND);
            double yikes = Hscroll.getValue() + (initx - e.getSceneX());
            double oof = Vscroll.getValue() - (inity - e.getSceneY());

            double shiftx = (initx - e.getSceneX());
            double shifty = (inity - e.getSceneY());


            buttonPane.setTranslateY(shifty);
            buttonPane.setTranslateX(shiftx);

        //    Hscroll.setValue(yikes);
          //  Vscroll.setValue(oof);

        });
       buttonPane.setOnMouseDragged(e -> {


           double yikes = Hscroll.getValue() + (initx - e.getSceneX());
           double oof = Vscroll.getValue() - (inity - e.getSceneY());
           Hscroll.setValue(yikes);
           Vscroll.setValue(oof);
            double newValue = (double) ((int) (zoom * 10)) / 10;

            //System.out.println((initx - e.getSceneX())+"/"+(inity - e.getSceneY()));

            Rectangle2D bounds = new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue);

            initx = e.getSceneX();
            inity = e.getSceneY();

        });*/

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

        imageFiles.put(this.floor, new Image(Main.getResource("/resources/maps/"+this.images[this.floor])));

        for (int i = 0; i < images.length; i++) {
            if (i == this.floor) continue;
            final int index = i;
            new Thread(() -> {
                imageFiles.put(index, new Image(Main.getResource("/resources/maps/" + this.images[index])));
            }).start();
        }

        try{
            this.getAllRecords(this.floor);
        }catch(Exception e){
            e.printStackTrace();
        }
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

    public void setFloor(int floor) throws Exception{
        System.out.println("Setting floor to "+floor);
        this.floor = floor;
        this.getAllRecords(floor);
    }
    public void setFloor(String floorId) throws Exception{
        System.out.println("Raw floor id: "+floorId);
        for(int i=0;i<dbPrefixes.length;i++){
            System.out.println("Checking "+dbPrefixes[i]);
            if(dbPrefixes[i].equals(floorId)){

                System.out.println("F Setting floor to "+i);
                this.floor = i;
                this.getAllRecords(i);
            }
        }
    }

    public int idToFloor(String id){
        for(int i=0;i<dbPrefixes.length;i++){
            if(dbPrefixes[i].equals(id)){
                return i;
            }
        }
        return -5;
    }

    public String getFloorLabel(){
        return labels[this.floor];
    }
    public String getFloorLabel(int floor){
        return labels[floor];
    }

    public String getCurrentFloorID(){
        return dbPrefixes[this.floor];
    }

}
