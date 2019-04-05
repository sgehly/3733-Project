package edu.wpi.cs3733.d19.teamM.controllers.LogIn;

/**
 * Sample Skeleton for 'login.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.cs3733.d19.teamM.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class LogInController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="mainContent"
    private AnchorPane mainContent; // Value injected by FXMLLoader

    @FXML // fx:id="mediaView"
    private MediaView mediaView; // Value injected by FXMLLoader

    @FXML
    void guestLogIn(ActionEvent event) {
        Main.setScene("home");
    }

    @FXML
    void logIn(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //Gets the video resources and stores it as a media file
        Media media = new Media(getClass().getResource("/resources/Pressure.mp4").toExternalForm());

        //Creates the media player to play the video
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        //Sets the video on infinite loop and passes it to the mediaView to display on the screen
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView.setMediaPlayer(mediaPlayer);

        assert mainContent != null : "fx:id=\"mainContent\" was not injected: check your FXML file 'login.fxml'.";
        assert mediaView != null : "fx:id=\"mediaView\" was not injected: check your FXML file 'login.fxml'.";

    }
}
