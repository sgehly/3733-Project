package edu.wpi.cs3733.d19.teamM.controllers.Home;

import edu.wpi.cs3733.d19.teamM.Main;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * The controller for the Home screen
 */
public class Home {

    @FXML
    private Label lblClock;

    private int hrs;
    private int mins;
    private int secs;

    @FXML
    private Label lblDate;

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
    public void initialize() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            secs = LocalTime.now().getSecond();
            mins = LocalTime.now().getMinute();
            hrs = LocalTime.now().getHour();

            lblClock.setText(hrs + ":" + (mins) + ":" + secs);
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        lblDate.setText(date.format(d));
    }
}
