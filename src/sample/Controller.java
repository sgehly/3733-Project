package sample;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.net.URLClassLoader;
public class Controller {

    @FXML
    private MediaView mediaView;

    @FXML
    protected void initialize() {
        URL url = getClass().getResource("/resources/Pressure.mp4");
        System.out.println(url);
        Media media = new Media(url.toExternalForm());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView.setMediaPlayer(mediaPlayer);
    }


}
