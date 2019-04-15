package edu.wpi.cs3733.d19.teamM.controllers.Chat;

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

public class Chat {

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Text userText;

    @FXML
    private ListView listEmployees;

    @FXML
    private ListView messages;

    @FXML
    private TextField messageBox;

    Channel channel;

    ArrayList<String> online = new ArrayList<String>();

    ArrayList<String> messagesArr = new ArrayList<String>();

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
        channel.publish(new Message("leave", User.getUsername()));
        Main.setScene("serviceRequests");
    }

    @FXML
    private void sendMessage() throws Exception {
        channel.publish("message", User.getUsername()+": "+messageBox.getText());
        messageBox.setText("");
    }

    @FXML
    void initialize() throws IOException {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        userText.setText("");

        try {
            AblyRealtime ably = new AblyRealtime("URg4iA.H7_X5w:2Zc5-2d-nGC8UmjV");

            channel = ably.channels.get("chat");

            online.add(User.getUsername());
            channel.publish(new Message("join", User.getUsername()));
            channel.subscribe(message -> {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(message.name.equals("join")){
                            online.add(message.data.toString());
                            listEmployees.setItems(FXCollections.observableArrayList(online));
                        }
                        else if(message.name.equals("leave")){
                            online.remove(message.data.toString());
                        }
                        else{
                            messagesArr.add(message.data.toString());
                            messages.setItems(FXCollections.observableArrayList(messagesArr));
                        }
                    }
                });

            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
