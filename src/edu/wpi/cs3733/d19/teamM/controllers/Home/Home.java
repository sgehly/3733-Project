package edu.wpi.cs3733.d19.teamM.controllers.Home;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.MBTA;
import edu.wpi.cs3733.d19.teamM.utilities.TwitterFeed;
import edu.wpi.cs3733.d19.teamM.utilities.Weather;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;
import io.ably.lib.types.PaginatedResult;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import edu.wpi.cs3733.d19.teamM.User.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import twitter4j.Status;

import javax.imageio.ImageIO;

import javafx.scene.text.Font;

//import javax.swing.text.html.ImageView;

/**
 * The controller for the Home screen
 */
public class Home{

    @FXML
    private ImageView myImg;

    @FXML
    private Label lblClock;

    private int hrs;
    private int mins;
    private int secs;

    @FXML
    private Label lblDate;

    @FXML
    private Text welcomeMessage;

    @FXML
    private Text userText;

    @FXML
    private Button admin;

    @FXML
    private Button about;

    @FXML
    private Label tweet;

    @FXML
    private Label trainTime;

    @FXML
    private Pane tweetPane;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private Label weatherText;

    @FXML
    private HBox notificationWrapper;

    @FXML
    private Pane notificationColor;

    @FXML
    private Label notificationText;

    @FXML
    private Label notificationTitle;

    @FXML
    private ScrollPane scrollContainer;

    @FXML
    private HBox contentContainer;

    /**
     * This method
     * @throws Exception
     */
    @FXML
    public void navigateToPathfinding(){
        Parent pathFindingPane;
        Scene pathFindingScene;
        try {
            pathFindingPane = FXMLLoader.load(Main.getFXMLURL("pathfinding"));
            pathFindingScene = new Scene(pathFindingPane);
            Main.getStage().setScene(pathFindingScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void navigateToChat(){
        Main.setScene("chat");
    }

    @FXML
    public void navigateToNotifications(){
        Main.setScene("notifications");
    }

    @FXML
    public void navigateToServiceRequests(){
        Main.setScene("serviceRequest");
    }

    @FXML
    public void navigateToDMs(){
        Main.setScene("dms");
    }

    @FXML
    public void logout(){
        Main.setScene("welcome");
    }

    @FXML
    public void navigateToScheduling(){
        Main.setScene("scheduling");
    }

    @FXML
    public void navigateToAdmin(){
        Main.setScene("admin");
    }

    @FXML
    public void navigateToAbout(){Main.setScene("about");}


    Timeline clock;
    Timeline reverseClock;

    @FXML
    void initialize() throws IOException, AblyException {

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        try{
            System.out.println(User.getPathToPic());
            Image image = new Image(Main.getResource("/resources/pics/"+User.getPathToPic()));
            myImg.setStyle("-fx-background-radius: 1000; -fx-border-radius:1000");
            myImg.setImage(image);
            myImg.setClip(new Circle(24.5,24.5,24));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        double delta = 0.0004;

        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {


            if(scrollContainer.getHvalue() == 1){
               this.clock.stop();
               this.reverseClock.play();
            }

            scrollContainer.setHvalue(scrollContainer.getHvalue()+delta);

        }), new KeyFrame(Duration.seconds(0.01)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


        reverseClock = new Timeline(new KeyFrame(Duration.ZERO, e -> {


            if(scrollContainer.getHvalue() == 0){
                this.reverseClock.stop();
                this.clock.play();
            }

            scrollContainer.setHvalue(scrollContainer.getHvalue()-delta);

        }), new KeyFrame(Duration.seconds(0.01)));
        reverseClock.setCycleCount(Animation.INDEFINITE);



        welcomeMessage.setText("Welcome to Brigham and Women's, " + User.getUsername());

        new Thread(() -> {
            ArrayList<Status> tweets = new TwitterFeed().getUpdates(1);
            Platform.runLater(() -> {
                tweet.setText(tweets.get(0).getText());
                double width = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(tweets.get(0).getText(), Font.font("Open Sans"));
                tweet.setMinWidth(width);
                tweetPane.setMinWidth(width+120);
            });
        }).start();

        new Thread(() -> {
            long nextTrainMins = new MBTA().getNextTrain();
            Platform.runLater(() -> {
                if(nextTrainMins > 0){
                    trainTime.setText(nextTrainMins+" Minutes");
                }else{
                    trainTime.setText("Arriving Now");
                }
            });
        }).start();

        new Thread(() -> {
            Platform.runLater(() -> {
                new Weather(weatherText, weatherIcon);
            });
        }).start();

        new Thread(() -> {
            try{
                PaginatedResult<Message> resultPage = Main.channel.history(null);

                Platform.runLater(() -> {
                    if(resultPage.items().length < 1){
                        notificationTitle.setText("No New Notifications");
                        notificationText.setText("Have a nice day!");
                        return;
                    }
                    Message lastMessage = resultPage.items()[0];

                    double width = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(lastMessage.data.toString(), Font.font("Open Sans"));
                    notificationTitle.setText(lastMessage.name);
                    notificationText.setText(lastMessage.data.toString());
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }).start();

        if(User.getPrivilege() != 100){
            admin.setVisible(false);
        }
    }
}