//package declaration
package edu.wpi.cs3733.d19.teamM.controllers.WelcomeAndLogin;

//imports
import edu.wpi.cs3733.d19.teamM.Main;

//necessary package importations
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;

//FXML packages
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

//SQL imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This class is the controller for the primary welcome screen
 */
public class WelcomeAndLogin {

    @FXML
    private VBox welcomeField; //area that contains all welcome objects

    @FXML
    private VBox loginField; //area that cofntains all of the login objects

    @FXML
    private MediaView mediaView; //object for playing background video

    @FXML
    private Label errorMessage; //label for handeling errors in login

    @FXML
    private TextField username; //username text field

    @FXML
    private PasswordField password; //password text field

    @FXML
    private Button logInButton;


    private SequentialTransition sequentialTransition; //used to make transition between welcome and login
    private boolean hasNotBeenClicked; //trigger to indicate whether the screen has been clicked or not



    /**
     * This method is used to initialize the application by loading all necessary aspects and displaying them
     */
    @FXML
    protected void initialize(){
        loginField.setOpacity(0); //initially needs to be invisible
        this.setLoginDisable();
        hasNotBeenClicked = true; //stating that the screen hasn't been clicked as the transition should happen exactly once per login


        //setting up the transition between fields
        sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(this.fadeOut(welcomeField, 1000), this.fadeCusion(welcomeField, 500), this.fadeIn(loginField, 1000));

        //sets media to specified video
        Media media = new Media(getClass().getResource("/resources/Pressure.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media); //initialize the MediaPlayer to play the video
        mediaPlayer.setAutoPlay(true); //auto play
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); //play indefinitely
        mediaView.setMediaPlayer(mediaPlayer); //setting the mediaview FXML object to contain the specified media player
    }

    /**
     * This method is for the button that allows the individual to navigate to the login screen
     */
    @FXML
    public void fadeToLogin(){
        //this if statement ensures that the transition will only appear once every time it is loaded
        if(hasNotBeenClicked) {
            this.setLoginEnable();
            sequentialTransition.play();
            hasNotBeenClicked = false;
        }
    }

    private void setLoginEnable() {
        username.setDisable(false);
        password.setDisable(false);
        logInButton.setDisable(false);
    }

    private void setLoginDisable() {
        username.setDisable(true);
        password.setDisable(true);
        logInButton.setDisable(true);
    }

    private FadeTransition fadeCusion(Node anyNode, int duration) {
        FadeTransition fadeCusion = new FadeTransition();
        fadeCusion.setDuration(Duration.millis(duration));
        fadeCusion.setNode(anyNode);
        fadeCusion.setToValue(0);
        fadeCusion.setFromValue(0);
        return fadeCusion;
    }

    private FadeTransition fadeOut(Node node, int duration) {
        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setDuration(Duration.millis(duration));
        fadeOut.setNode(node);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        return fadeOut;
    }

    private FadeTransition fadeIn(Node node, int duration) {
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setDuration(Duration.millis(duration));
        fadeIn.setNode(node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        return fadeIn;
    }

    @FXML
    void onEnterPressed(){
        password.setOnKeyPressed(
                event -> {
                    if(event.getCode().equals(KeyCode.ENTER)){
                        this.logIn();
                    }
                }
        );
    }

    @FXML
    void logIn() {
        /*
        TODO add username to pages: scheduler, service requests, sr list
         */
        try {
            String query = "SELECT * from USERS where USERNAME = ?";
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username.getText());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (Encrypt.getMd5(password.getText()).compareTo(rs.getString("USERPASS")) == 0) {
                User.getUser();
                User.setUsername(rs.getString("USERNAME"));
                User.setPathToPic(rs.getString("pathtopic"));
                username.setText("");
                password.setText("");
                errorMessage.setText("");
                User.setPrivilege(rs.getInt("ACCOUNTINT"));
                System.out.println("Logged in " + User.getUsername() + " with privilege " + User.getPrivilege());
                Main.loadScenes();
                this.resetScene();
                Main.setScene("home");
            } else {
                System.out.println("user not found");
                errorMessage.setText("Incorrect Credentials");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("User " + username.getText() + " Not Found");
            username.setText("");
            password.setText("");
        }
    }

    //resets the scene for the next time it is called
    private void resetScene() {
        welcomeField.setOpacity(1);
        loginField.setOpacity(0);
        hasNotBeenClicked = true;
        this.setLoginDisable();
    }


    //OLD CODE - used for guest login when we had it implemented in iteration one
    @FXML
    void guestLogIn(ActionEvent event) {
        try {
            User.getUser();
            User.setUsername("Guest");
            username.setText("");
            password.setText("");
            errorMessage.setText("");
            User.setPrivilege(0);
            System.out.println("Logged in Guest");
            Main.loadScenes();
            Main.setScene("home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
