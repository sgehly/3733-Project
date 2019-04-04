package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Button;
import javafx.animation.*;
import javafx.util.Duration;
import java.net.URL;

public class Controller {

    @FXML
    private MediaView mediaView;

    @FXML
    private Button button;

    @FXML
    private Pane mainContent;


    @FXML
    protected void initialize() {
        URL url = getClass().getResource("/Pressure.mp4");
        System.out.println(url);
        Media media = new Media(url.toExternalForm());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    @FXML
    protected void transitionToLogin(){
        FadeTransition ft = new FadeTransition(Duration.millis(3000), mainContent);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }


    public void navigateToHome(MouseEvent mouseEvent) {
    }
}
