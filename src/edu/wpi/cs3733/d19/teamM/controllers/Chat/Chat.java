package edu.wpi.cs3733.d19.teamM.controllers.Chat;

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
import javafx.scene.input.KeyCode;
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

    Channel pubChannel;

    ArrayList<String> online = new ArrayList<String>();

    ArrayList<String> messagesArr = new ArrayList<String>();

    @FXML
    public void logout() throws Exception {
        try{
            channel.presence.leave(new CompletionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(ErrorInfo reason) {

                }
            });
            pubChannel.presence.leave(new CompletionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(ErrorInfo reason) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        Main.setScene("welcome");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateBack() throws Exception {
        try{
            channel.presence.leave(new CompletionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(ErrorInfo reason) {

                }
            });
            pubChannel.presence.leave(new CompletionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(ErrorInfo reason) {

                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

        Main.setScene("home");
    }

    @FXML
    void onEnterPressed(){
        messageBox.setOnKeyPressed(
                event -> {
                    if(event.getCode().equals(KeyCode.ENTER)){
                        try{this.sendMessage();}catch(Exception e){e.printStackTrace();}
                    }
                }
        );
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
        messages.setStyle("-fx-font-size: 32px;");
        try {
            pubChannel = Main.ably.channels.get("chat");
            channel = Main.ably.channels.get("help-"+User.getUsername());
            channel.subscribe(message -> {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        messagesArr.add(message.data.toString());
                        messages.setItems(FXCollections.observableArrayList(messagesArr));
                    }
                });

            });


            new Thread(() -> {

                try{
                    PresenceMessage[] members = channel.presence.get();

                    Platform.runLater(() -> {
                        for(int i=0;i<members.length;i++) {
                            online.add(members[i].clientId);
                            listEmployees.setItems(FXCollections.observableArrayList(online));
                        }
                    });

                    channel.presence.subscribe(new Presence.PresenceListener() {
                        @Override
                        public void onPresenceMessage(PresenceMessage member) {

                            Platform.runLater(() -> {
                                if(member.action == PresenceMessage.Action.enter){
                                    online.add(member.clientId);
                                }
                                if(member.action == PresenceMessage.Action.leave){
                                    online.remove(member.clientId);
                                }

                                Platform.runLater(() -> {
                                    listEmployees.setItems(FXCollections.observableArrayList(online));
                                });
                            });

                        }
                    });

                    channel.presence.enter("iskrattar du förlorar du", new CompletionListener() {
                        @Override
                        public void onSuccess() {

                        }
                        @Override
                        public void onError(ErrorInfo info) {

                        }
                    });

                    pubChannel.presence.enter("iskrattar du förlorar du", new CompletionListener() {
                        @Override
                        public void onSuccess() {

                        }
                        @Override
                        public void onError(ErrorInfo info) {

                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();;
                }

            }).start();



        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
