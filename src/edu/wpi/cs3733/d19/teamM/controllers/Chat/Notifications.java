package edu.wpi.cs3733.d19.teamM.controllers.Chat;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.Message;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Notifications {

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Text userText;

    @FXML
    private JFXTextArea message;

    @FXML
    private JFXTextField title;

    Channel channel;

    @FXML
    public void logout() throws Exception {
        channel.publish(new Message("leave", User.getUsername()));
        Main.setScene("welcome");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("home");
    }

    @FXML
    private void sendMessage() throws Exception {
        channel.publish(title.getText(), message.getText());
        title.setText("");
        message.setText("");
    }

    @FXML
    void initialize() throws IOException {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        userText.setText("");

        try {
            AblyRealtime ably = new AblyRealtime("URg4iA.H7_X5w:2Zc5-2d-nGC8UmjV");
            channel = ably.channels.get("notifications");

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
