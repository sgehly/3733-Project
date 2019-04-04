package edu.wpi.cs3733.d19.teamM.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Button;
import javafx.animation.*;
import javafx.util.Duration;
import java.net.URL;

/**
 * The controller class that deals with the idle screen
 */
public class Idle {

    //The linked FXML elements
    @FXML
    private MediaView mediaView;

    @FXML
    private Button button;

    @FXML
    private Pane mainContent;

    /**
     * The method that initializes the IDLE screen
     */
    @FXML
    protected void initialize() {
        //Get the URL for the screen and create the media object
        URL url = getClass().getResource("/resources/Pressure.mp4");
        System.out.println(url);
        Media media = new Media(url.toExternalForm());

        //With the media player, play it on loop forever and play it with the media player
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    /**
     * This lets the user transition to the login screen
     */
    @FXML
    protected void transitionToLogin(){
        //Have a fade transition towards the main content screen and fade slowly to it
        FadeTransition ft = new FadeTransition(Duration.millis(3000), mainContent);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }


}
