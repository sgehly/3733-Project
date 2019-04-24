package edu.wpi.cs3733.d19.teamM.controllers.Chat;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.CompletionListener;
import io.ably.lib.realtime.Presence;
import io.ably.lib.types.ErrorInfo;
import io.ably.lib.types.Message;
import io.ably.lib.types.PaginatedResult;
import io.ably.lib.types.PresenceMessage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class AdminChat {

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

    Channel pubChannel;
    Channel channel;

    ArrayList<String> online = new ArrayList<String>();

    ArrayList<String> messagesArr = new ArrayList<String>();

    @FXML
    public void logout() throws Exception {

        try {
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
        Main.setScene("welcome");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateBack() throws Exception {

        Main.setScene("home");

        try{

            channel.presence.leave(new CompletionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(ErrorInfo reason) {

                }
            });

            if(pubChannel != null){
                pubChannel.presence.leave(new CompletionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(ErrorInfo reason) {

                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }

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
        pubChannel.publish("message", User.getUsername()+": "+messageBox.getText());
        messageBox.setText("");
    }

    @FXML
    void initialize() throws IOException {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        messages.setStyle("-fx-font-size: 32px;");


        listEmployees.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                messages.setItems(FXCollections.observableArrayList());
                try{
                    if(pubChannel != null){
                        pubChannel.detach();
                    }
                    pubChannel = Main.ably.channels.get("help-"+newValue.toString());

                    new Thread(() -> {
                        try{
                            PaginatedResult<Message> resultPage = pubChannel.history(null);

                            if(resultPage.items().length < 1){
                                return;
                            }
                            io.ably.lib.types.Message lastMessage[] = resultPage.items();

                            for(int i=0;i<lastMessage.length;i++){
                                messagesArr.add(lastMessage[i].data.toString());
                            }
                            Platform.runLater(() -> {
                                messages.setItems(FXCollections.observableArrayList(messagesArr));
                            });
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                    }).start();


                    pubChannel.subscribe(message -> {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                messagesArr.add(message.data.toString());
                                messages.setItems(FXCollections.observableArrayList(messagesArr));
                            }
                        });

                    });
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        try {

            channel = Main.ably.channels.get("chat");

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

                }catch(Exception e){
                    e.printStackTrace();;
                }

            }).start();



        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
