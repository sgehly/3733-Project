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

public class Pathfinding {

    String dbPath = "jdbc:derby:myDB";

    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static double offSetX,offSetY,zoomlvl;

    Rectangle2D primaryScreenBounds;

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

    @FXML
    private void navigateToHome() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);


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

    @FXML
    protected void initialize() throws Exception {

        //Adapted from: https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx
        //------------------------------------------------------------------------------------------------
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        Image source = null;
        try {
            String path = getClass().getResource("/resources/maps/01_thefirstfloor.png").toString().replace("file:", "");
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
        image.setFitHeight(primaryScreenBounds.getHeight() - 200);

        overlayImage.setFitWidth(image.getFitWidth());
        overlayImage.setFitHeight(image.getFitHeight());
        Image EMPTY = new Image(new FileInputStream("src/resources/maps/emptyOverlay.png")); //See if we can get the image to overlay and then create a new image object from it

        overlayImage.setImage(EMPTY);
        height = (int) source.getHeight();
        width = (int) source.getWidth();

        buttonContainer.setPrefWidth(image.getFitWidth());
        buttonContainer.setPrefHeight(image.getFitHeight());

        getAllRecords();
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

    private void setValues(ActionEvent value) {
        String nodeId = ((Button)value.getSource()).getId();

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

    private ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws Exception, SQLException {
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                javafx.scene.control.Button newButton = new Button();
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
    public void logout() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }
}
