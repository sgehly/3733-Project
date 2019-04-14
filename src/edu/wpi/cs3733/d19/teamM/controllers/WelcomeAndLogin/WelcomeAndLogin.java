//package declaration
package edu.wpi.cs3733.d19.teamM.controllers.WelcomeAndLogin;

//imports
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import edu.wpi.cs3733.d19.teamM.Main;

//necessary package importations
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;

//FXML packages
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.paint.Color;
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
    private VBox field2FA;

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

    @FXML
    private  TextField verifyCode;

    @FXML
    private Button verifyButton;

    @FXML
    private Button resendButton;

    @FXML
    private Button loginButton2;

    @FXML
    private TextField emailNumber;

    @FXML
    private Label sent;

    @FXML
    private Label wrong;


    private SequentialTransition welcomeToLoginTransition; //used to make transition between welcome and login
    private SequentialTransition loginTo2FATransition;
    private boolean hasNotBeenClicked; //trigger to indicate whether the screen has been clicked or not
    private boolean hasNotBeenClicked2; //trigger to indicate whether the login button has been pressed yet



    /**
     * This method is used to initialize the application by loading all necessary aspects and displaying them
     */
    @FXML
    protected void initialize(){
        loginField.setOpacity(0); //initially needs to be invisible
        loginField.setDisable(true);
        hasNotBeenClicked = true; //stating that the screen hasn't been clicked as the transition should happen exactly once per login

        field2FA.setOpacity(0);
        field2FA.setDisable(true);
        hasNotBeenClicked2 = true;

        sent.setVisible(false);
        wrong.setVisible(false);

        //setting up the transition between login and 2FA
        loginTo2FATransition = new SequentialTransition();
        loginTo2FATransition.getChildren().addAll(this.dropFade(loginField, 1000,100), this.drop(field2FA, 10, 100), this.raiseFade(field2FA, 1000, 100));

        //setting up the transition between welcome and login
        welcomeToLoginTransition = new SequentialTransition();
        welcomeToLoginTransition.getChildren().addAll(this.fadeOut(welcomeField, 1000), this.fadeCusion(welcomeField, 100), this.fadeIn(loginField, 1000));

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
        System.out.println("fading to login");
        //this if statement ensures that the transition will only appear once every time it is loaded
        if(hasNotBeenClicked) {
            welcomeField.setDisable(true);
            loginField.setDisable(false);
            System.out.println("the login stuff is enabled");
            welcomeToLoginTransition.play();
            hasNotBeenClicked = false;
        }
    }

    @FXML
    public void transitionTo2FA(){
        //this if statement ensures that the transition will only appear once everytime it is loaded
        if(hasNotBeenClicked2){
            loginField.setDisable(true);
            field2FA.setDisable(false);
            loginTo2FATransition.play();
            hasNotBeenClicked2 = false;
        }
    }


    private ParallelTransition dropFade(Node anyNode, int duration, int distance) {
        ParallelTransition dropFade = new ParallelTransition();
        dropFade.getChildren().addAll(this.drop(anyNode,duration,distance),this.fadeOut(anyNode, duration));
        return dropFade;
    }

    private ParallelTransition raiseFade(Node anyNode, int duration, int distance) {
        ParallelTransition raiseFade = new ParallelTransition();
        raiseFade.getChildren().addAll(this.raise(anyNode,duration,distance),this.fadeIn(anyNode, duration));
        return raiseFade;
    }

    private TranslateTransition drop(Node anyNode, int duration, int distance){
        TranslateTransition drop = new TranslateTransition();
        drop.setDuration(Duration.millis(duration));
        drop.setNode(anyNode);
        drop.setByY(distance);

        return drop;
    }

    private TranslateTransition raise(Node anyNode, int duration, int distance){
        TranslateTransition raise = new TranslateTransition();
        raise.setDuration(Duration.millis(duration));
        raise.setNode(anyNode);
        raise.setByY(0-distance);
        return raise;
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
                        this.transitionTo2FA();
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
                Main.setScene("home");
                conn.close();
                //Main.startIdleCheck();
                //uncomment this line to start using idle check
                if(User.getUsername().compareTo(Main.savedState.getUserName()) != 0){
                    Main.savedState.setUserName(User.getUsername());
                    Main.savedState.setState("home");
                }
                System.out.println(User.getUsername());
                System.out.println(Main.savedState.getUserName());
                System.out.println(Main.savedState.getState());
                Main.setScene(Main.savedState.getState());
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
        loginField.setDisable(true);
        field2FA.setDisable(true);
        welcomeField.setDisable(false);
    }

    @FXML
    public void sendVerify() {
        String numberEmail = emailNumber.getText();
        if(numberEmail.compareTo("") == 1){
            sent.setTextFill(Color.web("#ff0000"));
            sent.setText("Please enter an email or phone number");
            sent.setVisible(true);
            System.out.println("Nothing");

        }
        int myCode = (int)generateCode();
        Twilio.init("ACbfbd0226f179ee74597c887298cbda10", "eeb459634d5a8407d077635504386d44");
        if (!numberEmail.contains("@")) {
            try {
                Message message = Message.creator(new PhoneNumber(numberEmail), new PhoneNumber("+15085383787"), "Hello from Brigham & Women's! Your authentication code is " + myCode).create();
                TwoFactor myFactor = TwoFactor.getTwoFactor();
                myFactor.setTheCode(myCode);
                sent.setTextFill(Color.web("#009933"));
                sent.setText("Code sent");
                sent.setVisible(true);
                System.out.println("sent");
            } catch (Exception e) {
                e.printStackTrace();
                sent.setTextFill(Color.web("#ff0000"));
                sent.setText("Could not send your code");
                sent.setVisible(true);
                System.out.println("failed");
            }
        }
        else{
            String sendgrid_username  = "sgehlywpi";
            String sendgrid_password  = "MangoManticores1!";
            String to = numberEmail;
            SendGrid sendgrid = new SendGrid(sendgrid_username, sendgrid_password);
            SendGrid.Email email = new SendGrid.Email();

            email.addTo(numberEmail);
            email.setFrom(numberEmail);
            email.setFromName("Brigham & Women's");
            email.setReplyTo("mangomanticores@gehly.net");
            email.setSubject("Authentication Code");
            email.setHtml(" from Brigham & Women's! Your authentication code is " + myCode);
            try {
                SendGrid.Response response = sendgrid.send(email);
                TwoFactor myFactor = TwoFactor.getTwoFactor();
                myFactor.setTheCode(myCode);
                sent.setTextFill(Color.web("#009933"));
                sent.setText("Code sent");
                sent.setVisible(true);
                System.out.println("sent");
            } catch (SendGridException e) {
                System.out.println(e);
                sent.setTextFill(Color.web("#ff0000"));
                sent.setText("Could not send your code");
                sent.setVisible(true);
                System.out.println("failed");
            }
        }
    }

    @FXML
    private void checkCode(){
        TwoFactor myFactor = TwoFactor.getTwoFactor();
        if(myFactor.getTheCode() == Integer.parseInt(verifyCode.getText())){
            System.out.println("Yay");
            this.logIn();
        }
        else{
            wrong.setTextFill(Color.web("#ff0000"));
            wrong.setText("Code is incorrect");
            wrong.setVisible(true);
        }
    }

    private double generateCode(){
        double one = Math.random();
        double two = Math.random();
        double three = Math.random();
        double four = Math.random();
        double five = Math.random();
        double six = Math.random();
        double seven = Math.random();
        double combind = one + two * 10 + three * 100 + four * 1000 + five * 10000 + six * 100000 + seven * 10000000;
        return combind;
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
