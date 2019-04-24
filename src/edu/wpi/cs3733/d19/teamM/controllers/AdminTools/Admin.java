package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.General.Options;
import edu.wpi.cs3733.d19.teamM.utilities.Transitions;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.AblyException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
import java.util.Calendar;
import java.util.Date;
import edu.wpi.cs3733.d19.teamM.User.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;

//import javax.swing.text.html.ImageView;

/**
 * The controller for the Home screen
 */
public class Admin{

    Transitions transitions;

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
    private StackPane stackpane;

    @FXML
    private StackPane dialog;

    @FXML
    private VBox contentPane;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ImageView exitButton;

    @FXML
    private JFXTextField jfxTextfield;
    @FXML
    private Label selected;



    /**
     * This method
     * @throws Exception
     */
    @FXML
    public void navigateToSettings(){
        Main.setScene("settings");
    }

    @FXML
    public void loadDialogue(){
        backgroundImage.setEffect(new GaussianBlur());
        //backgroundImage.setDisable(true);
        contentPane.setEffect(new GaussianBlur());
        //contentPane.setDisable(true);
        dialog.setDisable(false);
        transitions.fadeIn(dialog, 250).play();
    }

    @FXML
    public void unloadDialogue(){
        backgroundImage.setEffect(null);
        //backgroundImage.setDisable(false);
        contentPane.setEffect(null);
        //contentPane.setDisable(false);
        dialog.setDisable(true);
        transitions.fadeOut(dialog, 250).play();
    }

    @FXML
    public void navigateToChat(){
        selected.setVisible(false);
        Main.setScene("adminHelpdesk");
    }

    @FXML
    public void navigateToNotifications(){
        Main.setScene("notifications");
    }

    @FXML
    public void navigateToServiceRequests(){
        selected.setVisible(false);
        Main.setScene("addUser");
    }

    @FXML
    public void navigateToDMs(){
        Main.setScene("dms");
    }

    @FXML
    public void logout(){
        selected.setVisible(false);
        Main.setScene("welcome");
    }

    @FXML
    public void navigateToScheduling(){
        selected.setVisible(false);
        Main.setScene("adminUI");
    }

    @FXML
    public void navigateToAdmin(){
        Main.setScene("admin");
    }

    @FXML
    public void navigateToAbout(){
        selected.setVisible(false);
        Main.setScene("about");}

    @FXML
    public void navigateToHome(){
        selected.setVisible(false);
        Main.setScene("home");}

    @FXML
    private void chooseAStar(){
        Floor graph = Floor.getFloor();
        selected.setStyle("-fx-text-fill: green;");
        selected.setText("A* Selected!");
        selected.setVisible(true);
        graph.setAStar();
    }

    @FXML
    private void chooseDFS(){
        Floor graph = Floor.getFloor();
        selected.setStyle("-fx-text-fill: green;");
        selected.setText("DFS Selected!");
        selected.setVisible(true);
        graph.setDFS();
    }

    @FXML
    private void chooseBFS(){
        Floor graph = Floor.getFloor();
        selected.setStyle("-fx-text-fill: green;");
        selected.setText("BFS Selected!");
        selected.setVisible(true);
        graph.setBFS();
    }

    @FXML
    private void chooseDijkstra(){
        Floor graph = Floor.getFloor();
        selected.setStyle("-fx-text-fill: green;");
        selected.setText("Dijkstra Selected!");
        selected.setVisible(true);
        graph.setDijkstra();
    }

    @FXML
    private void setTimeout(){
        if(jfxTextfield.getText() != "") {
            Options.setTimeout(Integer.parseInt(jfxTextfield.getText()));
            System.out.println(Options.getTimeout());
        }
    }

    @FXML
    void initialize() throws IOException, AblyException {
        selected.setVisible(false);
        //blur stuff
        BoxBlur bb = new BoxBlur();
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(3);

        dialog.setOpacity(0);
        dialog.setDisable(true);
        contentPane.setDisable(false);
        backgroundImage.setDisable(false);
        transitions = new Transitions();
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
    }
}