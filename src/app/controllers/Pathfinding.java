package app.controllers;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import app.AStar.AStar;
import app.AStar.Floor;
import app.AStar.Node;
import app.AStar.SendEmail;
import app.Main;
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
import javafx.stage.Screen;

import javax.swing.*;

public class Pathfinding {
    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static double offSetX,offSetY,zoomlvl;

    @FXML
    private Pane imageView;


    @FXML
    private ImageView image;

    @FXML
    private ImageView overlayImage;

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
            Floor floor = new Floor("1");//Create an instance of the floor and get the start and end nodes
            HashMap<String,Node> floorMap = (HashMap<String, Node>) floor.getFloorMap();//Get the floorMap
            Node startNode = floorMap.get(startString); //Get starting and ending string using keys
            Node endNode = floorMap.get(endString);

            //Now we create an A* object to find the path between the two and store the final list of nodes
            AStar aStar = new AStar();
            List<Node> nodeArrayList = aStar.findPath(startNode,endNode);
            System.out.println("Got Path");
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //Now use this list to draw the path and put it in resources "/resources/maps/PathOutput.png"
            floor.drawPath(nodeArrayList);
            System.out.println("Plotted path");
            //SendEmail sendEmail = new SendEmail();
            //String email = JOptionPane.showInputDialog("Enter your email id if you would like to have map with path sent to you");
            //sendEmail.sendMail(email);
            //System.out.println("Sent Mail");

            //Now we will try to get the image
            try
            {
                Image Overlaysource;
                Overlaysource = new Image(new FileInputStream("src/resources/maps/PathOutput.png")); //See if we can get the image to overlay and then create a new image object from it
                overlayImage.setImage(Overlaysource); //set the image as the overlay image

            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }
    }

    @FXML
    protected void initialize() throws Exception {

        //Adapted from: https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx
        //------------------------------------------------------------------------------------------------
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

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
        System.out.println("height = " + height + "\nwidth = " + width);
        HBox zoom = new HBox(10);
        zoom.setAlignment(Pos.CENTER);

        //zoomLvl.setMax(10);
        //zoomLvl.setMin(1);

        offSetX = width / 2;
        offSetY = height / 2;

        Slider Hscroll = new Slider();
        Hscroll.setMin(0);
        Hscroll.setMax(width);
        Hscroll.setMaxWidth(image.getFitWidth());
        Hscroll.setMinWidth(image.getFitWidth());
        Hscroll.setTranslateY(20);
        Slider Vscroll = new Slider();
        Vscroll.setMin(0);
        Vscroll.setMax(height);
        Vscroll.setMaxHeight(image.getFitHeight());
        Vscroll.setMinHeight(image.getFitHeight());
        Vscroll.setOrientation(Orientation.VERTICAL);
        Vscroll.setTranslateX(-20);

        //imageView.setAlignment(Hscroll, Pos.CENTER);
        //imageView.setAlignment(Vscroll, Pos.CENTER_LEFT);

        Hscroll.valueProperty().addListener(e -> {
            offSetX = Hscroll.getValue();
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }

            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
            //overlayImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));

        });
        Vscroll.valueProperty().addListener(e -> {
            offSetY = height - Vscroll.getValue();
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }
            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
            //overlayImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));

        });
        //imageView.setCenter(image);
        //imageView.setTop(Hscroll);
        //imageView.setRight(Vscroll);
        /*zoomLvl.valueProperty().addListener(e -> {
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
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
            Hscroll.setValue(offSetX);
            Vscroll.setValue(height - offSetY);
            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
            //overlayImage.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));

        });
        imageView.setCursor(Cursor.OPEN_HAND);
        image.setOnMousePressed(e -> {
            initx = e.getSceneX();
            inity = e.getSceneY();
            imageView.setCursor(Cursor.CLOSED_HAND);
        });
        image.setOnMouseReleased(e -> {
            imageView.setCursor(Cursor.OPEN_HAND);
        });
        image.setOnMouseDragged(e -> {
            Hscroll.setValue(Hscroll.getValue() + (initx - e.getSceneX()));
            Vscroll.setValue(Vscroll.getValue() - (inity - e.getSceneY()));
            initx = e.getSceneX();
            inity = e.getSceneY();
        });*/
    }

    @FXML
    public void logout() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }
}
