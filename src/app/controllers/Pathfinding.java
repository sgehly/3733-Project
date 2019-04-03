package app.controllers;

import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;
import java.util.List;

import app.AStar.AStar;
import app.AStar.Floor;
import app.AStar.Node;
import app.AStar.SendEmail;
import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.Cursor;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * The controller class associated with the pathfinding fuctions
 */
public class Pathfinding {

    //Get the string value of the database path
    String dbPath = "jdbc:derby:myDB";

    //Create needed object instances
    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static double offSetX,offSetY,zoomlvl;

    Rectangle2D primaryScreenBounds;

    //Get the FXML objects to be linked
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
    private TextField startText;

    @FXML
    private TextField endText;

    /**
     * This method lets the user navigate back to the home page
     * @throws Exception
     */
    @FXML
    private void navigateToHome() throws Exception{
        //Load the home screen pane, get the scene and update the stage
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
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

    private void findPresetHelper(String type) {
        String start = startText.getText();
        Floor floor = new Floor("1");//Create an instance of the floor and get the start and end nodes
        HashMap<String, Node> floorMap = (HashMap<String, Node>) floor.getFloorMap();//Get the floorMap
        Node startNode = floorMap.get(start); //Get starting and ending string using keys
        AStar aStar = new AStar();
        List<Node> nodeArrayList = aStar.findPresetPath(startNode, type, floorMap);
        System.out.println("Got Path");
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //Now use this list to draw the path and put it in resources "/resources/maps/PathOutput.png"
        floor.drawPath(nodeArrayList);
        System.out.println("Plotted path");
        //System.out.println("Sent Mail");

        //Now we will try to get the image
        try {
            Image Overlaysource;
            Overlaysource = new Image(new FileInputStream("src/resources/maps/PathOutput.png")); //See if we can get the image to overlay and then create a new image object from it
            overlayImage.setImage(Overlaysource); //set the image as the overlay image

            startText.setText("");
            endText.setText("");
            // SendEmail sendEmail = new SendEmail();
            //String email = JOptionPane.showInputDialog("Enter your email id if you would like to have map with path sent to you");
            //sendEmail.sendMail(email);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void findPath() throws Exception{
    //Get the starting and ending nodes ID
    String startString = startText.getText();
    String endString = endText.getText();
    //Check if either are empty
        if(!startString.equals("") && !endString.equals("")) //If not empty
        {
            // Creating the main window of our application

            //frame.pack();

            // Release the window and quit the application when it has been closed
          /*

            // Creating a button and setting its action
            final JButton clickMeButton = new JButton("Click Me!");
            clickMeButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    // Ask for the user name and say hello

                }
            });*/

            // Add the button to the window and resize it to fit the button
           // frame.getContentPane().add(clickMeButton);

            // Displaying the window
           // frame.setVisible(true);


           /* //Create A popup asking if they want to print it
            Alert dg = new Alert(Alert.AlertType.NONE);
            dg.setTitle("Print Map?");
            dg.setHeaderText("Request to Print Map");
            dg.setContentText("Would you like to send a printout of this map to your email?");

            ButtonType send_email = new ButtonType("Send Email", ButtonBar.ButtonData.LEFT);
            ButtonType no_thanks = new ButtonType("No Thanks", ButtonBar.ButtonData.RIGHT);
            dg.getButtonTypes().add(send_email);
            dg.getButtonTypes().add(no_thanks);
            dg.show();
*/
           try {
               Floor floor = new Floor("1");//Create an instance of the floor and get the start and end nodes
               HashMap<String, Node> floorMap = (HashMap<String, Node>) floor.getFloorMap();//Get the floorMap
               Node startNode = floorMap.get(startString); //Get starting and ending string using keys
               Node endNode = floorMap.get(endString);

               //Now we create an A* object to find the path between the two and store the final list of nodes
               AStar aStar = new AStar();
               List<Node> nodeArrayList = aStar.findPath(startNode, endNode);
               System.out.println("Got Path");
               final JFrame frame = new JFrame();
               frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
               //Now use this list to draw the path and put it in resources "/resources/maps/PathOutput.png"
               floor.drawPath(nodeArrayList);
               System.out.println("Plotted path");
               //System.out.println("Sent Mail");

               //Now we will try to get the image
               try {
                   Image Overlaysource;
                   Overlaysource = new Image(new FileInputStream("src/resources/maps/PathOutput.png")); //See if we can get the image to overlay and then create a new image object from it
                   overlayImage.setImage(Overlaysource); //set the image as the overlay image

                   startText.setText("");
                   endText.setText("");
                   // SendEmail sendEmail = new SendEmail();
                   //String email = JOptionPane.showInputDialog("Enter your email id if you would like to have map with path sent to you");
                   //sendEmail.sendMail(email);
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
    }

    /**
     * This method will initialize the pathfinding screen's controller
     * @throws Exception: Any exception that arises in the screen
     */
    @FXML
    protected void initialize() throws Exception {

        //Adapted from: https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx
        //------------------------------------------------------------------------------------------------
        primaryScreenBounds = Screen.getPrimary().getVisualBounds(); //Get the bounds of the screen

        Image source = null; //Start with the image being null
        try {//Try to retrieve the image of the first floor
            String path = getClass().getResource("/resources/maps/01_thefirstfloor.png").toString().replace("file:", "");
            System.out.println(path);
            source = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double ratio = source.getWidth() / source.getHeight();

        //If the ratio and the actual width and height are not proper, make them a necessary size
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
        image.setImage(source);//Set the image as the source
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight() - 200);

        //Gets the overlay image and sets the width and the height of that
        overlayImage.setFitWidth(image.getFitWidth());
        overlayImage.setFitHeight(image.getFitHeight());
        Image EMPTY = new Image(new FileInputStream("src/resources/maps/emptyOverlay.png")); //See if we can get the image to overlay and then create a new image object from it

        //Initially set the image to empty and get the width and height
        overlayImage.setImage(EMPTY);
        height = (int) source.getHeight();
        width = (int) source.getWidth();

        //Get the buttons on the screen and set the preferred width and height to that of the image
        buttonContainer.setPrefWidth(image.getFitWidth());
        buttonContainer.setPrefHeight(image.getFitHeight());

        //Get the necessary records for pathfinding
        getAllRecords();
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
        double scaledWidth = imageView.getBoundsInParent().getWidth();
        double scaledHeight = imageView.getBoundsInParent().getHeight()-50;

        System.out.println(scaledHeight+" - "+scaledWidth);

        //Scale the x and y coordinates and set / return as a new map point
        double scaledX = (pointX*scaledWidth)/rawWidth;
        double scaledY = (pointY*scaledHeight)/rawHeight;
        System.out.println(scaledX+" - "+scaledY);
        return new MapPoint(scaledX, scaledY);

    }

    /**
     * Set the values of the nodeID and other relevant text for each of the buttons that act as the nodes on the map to display for pathfinding options
     * @param value: The action event associated with the method
     */
    private void setValues(ActionEvent value) {
        //Get the id of the node
        String nodeId = ((Button)value.getSource()).getId();

        //Get required tex to display as one of the values for the pathfinding
        if(startText.getText().length() == 0){
            startText.setText(nodeId);
            endText.requestFocus();
        }else{
            endText.setText(nodeId);
            try {
                this.findPath();
            } catch (Exception e){}
        }
    }

    /**
     * This method gets the current objects from the Database and returns it as an Observable List
     * @param rs: The corresponding result set
     * @return ObservableList<DisplayTable>: A list with displayable tables to view in the UI the database entries for the service requests
     * @throws SQLException: Any SQL errors that might occur while trying to get the service requests
     */
    private ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws Exception, SQLException {
        //The list we will populate
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
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
                newButton.setOnAction(this::setValues);

                //Generate a map point out of the node button and place it on the screen and make it blue
                MapPoint generated = scalePoints(rs.getInt("xcoord"), rs.getInt("ycoord"));
                newButton.setLayoutX(generated.x-(size/2));
                newButton.setLayoutY(generated.y-(size/2));
                newButton.setStyle("-fx-background-color: blue");
                System.out.println("New Button! ("+String.valueOf(rs.getInt("xcoord")-(size/2))+","+String.valueOf(rs.getInt("ycoord")-(size/2))+") -- ("+String.valueOf(primaryScreenBounds.getWidth())+","+String.valueOf(primaryScreenBounds.getHeight()-200)+") => ("+generated.x+","+generated.y+")");
                buttonContainer.getChildren().add(newButton); //Add it to the button container
            }
            return entList; //Return this list
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
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
    public ObservableList<DisplayTable> getAllRecords() throws ClassNotFoundException, SQLException, Exception {
        //Get the query from the database
        String query = "SELECT * FROM FLOOR1";
        try {
            //Get the information that we want from the database
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
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

    /**
     * This method allows a user to return back to the welcome page
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    public void logout() throws Exception{
        //Get the pane, the scene, and place it on as the primary stage
        Parent pane = FXMLLoader.load(Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }
}
