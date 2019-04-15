package edu.wpi.cs3733.d19.teamM.controllers.Chat;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.CompletionListener;
import io.ably.lib.realtime.Presence;
import io.ably.lib.types.ClientOptions;
import io.ably.lib.types.ErrorInfo;
import io.ably.lib.types.Message;
import io.ably.lib.types.PresenceMessage;
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

public class DMs {

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Text userText;

    @FXML
    private JFXTextArea message;

    @FXML
    private JFXComboBox userBox;

    @FXML
    private JFXTextField title;

    Channel listChannel;
    Channel userChannel;

    ArrayList<String> onlineUsers = new ArrayList<String>();

    @FXML
    public void logout() throws Exception {
        listChannel.publish(new Message("leave", User.getUsername()));
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
        userChannel.publish(title.getText(), message.getText()+" \n\nSent by "+User.getUsername());
        title.setText("");
        message.setText("");
    }

    @FXML
    void initialize() throws IOException {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        userText.setText("");

        try {
            ClientOptions options = new ClientOptions("URg4iA.H7_X5w:2Zc5-2d-nGC8UmjV");
            options.clientId = User.getUsername();
            AblyRealtime ably = new AblyRealtime(options);

            listChannel = ably.channels.get("notifications");

            userBox.setOnAction((e) -> {
                Platform.runLater(() -> {
                    if(((String)userBox.getSelectionModel().getSelectedItem()).equals("")){
                        return;
                    }
                    System.out.println("Setting user channel to "+(String)userBox.getSelectionModel().getSelectedItem());
                    userChannel = ably.channels.get("usernotify-"+(String)userBox.getSelectionModel().getSelectedItem());
                });
            });

            PresenceMessage[] members = listChannel.presence.get();
            for(int i=0;i<members.length;i++){
                onlineUsers.add(members[i].clientId);
                userBox.setItems(FXCollections.observableArrayList(onlineUsers));
            }

            listChannel.presence.subscribe(new Presence.PresenceListener() {
                @Override
                public void onPresenceMessage(PresenceMessage member) {
                    if(member.action == PresenceMessage.Action.enter){
                        onlineUsers.add(member.clientId);
                    }
                    if(member.action == PresenceMessage.Action.leave){
                        onlineUsers.remove(member.clientId);
                    }
                    userBox.setItems(FXCollections.observableArrayList(onlineUsers));
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
