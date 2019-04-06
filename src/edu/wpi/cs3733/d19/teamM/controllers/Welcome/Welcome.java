package edu.wpi.cs3733.d19.teamM.controllers.Welcome;

import edu.wpi.cs3733.d19.teamM.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;

/**
 * This class is the controller for the primary welcome screen
 */
public class Welcome {

    //Create the different objects that are present in the screen
    @FXML
    private MediaView mediaView;

    @FXML
    private Button button;

    @FXML
    private Pane mainContent;


    /**
     * This method is used to initialize the application by loading all necessary aspects and displaying them
     */
    @FXML
    protected void initialize() throws Exception{

        //Gets the video resources and stores it as a media file
        Media media = new Media(getClass().getResource("/resources/Pressure.mp4").toExternalForm());

        //Creates the media player to play the video
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        //Sets the video on infinite loop and passes it to the mediaView to display on the screen
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    /**
     * This method is for the button that allows the individual to navigate to the home screen
     * @throws Exception: Any exception or issue is thrown
     */
    @FXML
    public void navigateToHome() throws Exception{
        Main.setScene("login");
    }

}
