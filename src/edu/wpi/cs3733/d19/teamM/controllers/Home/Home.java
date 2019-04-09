package edu.wpi.cs3733.d19.teamM.controllers.Home;

import com.github.sarxos.webcam.Webcam;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import edu.wpi.cs3733.d19.teamM.controllers.LogIn.LogInController;
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

    /**
     * This method
     * @throws Exception
     */
    @FXML
    public void navigateToPathfinding(){
        Main.setScene("pathfinding");
    }

    @FXML
    public void navigateToServiceRequests(){
        Main.setScene("serviceRequest");
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
    void initialize() throws IOException {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        File f = new File(User.getPathToPic());
        Image image = new Image(f.toURI().toString());
        myImg.setImage(image);
        welcomeMessage.setText("Welcome to Brigham and Women's " + User.getUsername());
        if(User.getPrivilege() != 100){
            admin.setVisible(false);
        }
    }
}