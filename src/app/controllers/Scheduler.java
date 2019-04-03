
package app.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import app.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * The controller class associated with the scheduler
 */
public class Scheduler {
   //Instances of necessary objects
    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static double offSetX,offSetY,zoomlvl;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="zoomLvl"
    private Slider zoomLvl; // Value injected by FXMLLoader

    @FXML // fx:id="imageView"
    private HBox imageView; // Value injected by FXMLLoader

    @FXML // fx:id="image"
    private ImageView image; // Value injected by FXMLLoader

    /**
     * This method is for the button that allows the user to navigate back to the home screen
     * @throws Exception
     */
    @FXML
    private void navigateToHome() throws Exception{
        //Gets an instance of the screen and sets it as the main stage to display
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    /**
     * This method initializes the controller and deals with assert statements
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert zoomLvl != null : "fx:id=\"zoomLvl\" was not injected: check your FXML file 'scheduler.fxml'.";
        assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'scheduler.fxml'.";
        assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'scheduler.fxml'.";

        //Adapted from: https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx
        //------------------------------------------------------------------------------------------------
        //Get the bounds of the screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        Image source = null; //Create the image object and initially have it to null

        try { //Try to retrieve the image of the first floor
            String path = getClass().getResource("/resources/maps/01_thefirstfloor.png").toString().replace("file:","");
            System.out.println(path);
            source = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double ratio = source.getWidth() / source.getHeight(); //Get the width to height ratio

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


        image.setImage(source); //Set the image as the source
        System.out.println(imageView.getBoundsInParent().getWidth());

        //Set the proper width and height for the image
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight()-200);
        height = (int) source.getHeight();
        width = (int) source.getWidth();

        System.out.println("height = " + height + "\nwidth = " + width);

        //Deal with the HBox display and its zoom and offset settings
        HBox zoom = new HBox(10);
        zoom.setAlignment(Pos.CENTER);
        zoomLvl.setMax(10);
        zoomLvl.setMin(1);
        offSetX = width / 2;
        offSetY = height / 2;

        //These deal with creating and setting the dimensions for the horizontal and vertical scroll
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

        Hscroll.valueProperty().addListener(e -> { //Deals with the event in which someone tries to scroll horizontally
            //Get the offset and the zoom
            offSetX = Hscroll.getValue();
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;

            //Adjust the values based on the offset and the zoom
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }
            //Set the view of the image by updating it
            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        Vscroll.valueProperty().addListener(e -> {
            //Get the offset and the zoom
            offSetY = height - Vscroll.getValue();
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;

            //Adjust the values based on the offset and the zoom
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }
            //Set the view of the image by updating it
            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        //imageView.setCenter(image);
        //imageView.setTop(Hscroll);
        //imageView.setRight(Vscroll);

        zoomLvl.valueProperty().addListener(e -> { //Listener to update the zoom level as the user scrolls in and out
            //Get the new zoom level and adjust the view of the screen
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
            //Adjust the scrolling of the horizontal and the vertical based on the zoom levels
            Hscroll.setValue(offSetX);
            Vscroll.setValue(height - offSetY);

            //Updae the view of the image based on the scroll levels
            image.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });

        //set and update the view of the image based on the cursor
        imageView.setCursor(Cursor.OPEN_HAND);

        //When the mouse is pressed on the image, get the x and y of where it was pressed and set the cursor to there
        image.setOnMousePressed(e -> {
            initx = e.getSceneX();
            inity = e.getSceneY();
            imageView.setCursor(Cursor.CLOSED_HAND);
        });

        //When the mouse is released display the cursor with a closed hand
        image.setOnMouseReleased(e -> {
            imageView.setCursor(Cursor.OPEN_HAND);
        });

        //When the mouse is dragged update the scrolling values as well as the views
        image.setOnMouseDragged(e -> {
            Hscroll.setValue(Hscroll.getValue() + (initx - e.getSceneX()));
            Vscroll.setValue(Vscroll.getValue() - (inity - e.getSceneY()));
            initx = e.getSceneX();
            inity = e.getSceneY();
        });

    }
}
