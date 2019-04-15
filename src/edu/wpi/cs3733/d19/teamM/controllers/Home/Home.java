package edu.wpi.cs3733.d19.teamM.controllers.Home;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.AblyException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import edu.wpi.cs3733.d19.teamM.User.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;

//import javax.swing.text.html.ImageView;

/**
 * The controller for the Home screen
 */
public class Home{

    @FXML
    private ImageView myImg;

    @FXML
    private Label lblClock;

    private int hrs;
    private int mins;
    private int secs;

    @FXML
    private Label lblDate;

    @FXML
    private Text welcomeMessage;

    @FXML
    private Text userText;

    @FXML
    private Button admin;

    @FXML
    private Button about;

    /**
     * This method
     * @throws Exception
     */
    @FXML
    public void navigateToPathfinding(){
        Main.setScene("pathfinding");
    }

    @FXML
    public void navigateToChat(){
        Main.setScene("chat");
    }

    @FXML
    public void navigateToNotifications(){
        Main.setScene("notifications");
    }

    @FXML
    public void navigateToServiceRequests(){
        Main.setScene("serviceRequest");
    }

    @FXML
    public void navigateToDMs(){
        Main.setScene("dms");
    }

    @FXML
    public void logout(){
        Main.setScene("welcome");
    }

    @FXML
    public void navigateToScheduling(){
        Main.setScene("scheduling");
    }

    @FXML
    public void navigateToAdmin(){
        Main.setScene("admin");
    }

    @FXML
    public void navigateToAbout(){Main.setScene("about");}

    @FXML
    void initialize() throws IOException, AblyException {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        userText.setText("");
        try{
            File f = new File(User.getPathToPic());
            Image image = new Image(f.toURI().toString());
            myImg.setStyle("-fx-background-radius: 1000; -fx-border-radius:1000");
            myImg.setImage(image);
            myImg.setClip(new Circle(24.5,24.5,24));
        }
        catch(Exception e){e.printStackTrace();}

        welcomeMessage.setText("Welcome to Brigham and Women's, " + User.getUsername());


        if(User.getPrivilege() != 100){
            admin.setVisible(false);
        }
    }
}