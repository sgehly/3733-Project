package edu.wpi.cs3733.d19.teamM.controllers.LogIn;

/**
 * Sample Skeleton for 'login.fxml' Controller Class
 */

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.PathToString;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LogInController {


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private Label errorMessage;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private TextField verifyCode;

    @FXML
    private TextField emailNumber;

    @FXML // fx:id="mainContent"
    private AnchorPane mainContent; // Value injected by FXMLLoader

    @FXML // fx:id="mediaView"
    private MediaView mediaView; // Value injected by FXMLLoader

    @FXML
    public void sendVerify() {
        String numberEmail = emailNumber.getText();
        int myCode = (int)generateCode();
        Twilio.init("ACbfbd0226f179ee74597c887298cbda10", "eeb459634d5a8407d077635504386d44");
        if (!numberEmail.contains("@")) {
            try {
                Message message = Message.creator(new PhoneNumber(numberEmail), new PhoneNumber("+15085383787"), "Hello from Brigham & Women's! Your authentication code is " + myCode).create();
            } catch (Exception e) {
                e.printStackTrace();
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
            } catch (SendGridException e) {
                System.out.println(e);
            }
        }
        TwoFactor myFactor = TwoFactor.getTwoFactor();
        myFactor.setTheCode(myCode);
    }

    @FXML
    private void checkCode(){
        TwoFactor myFactor = TwoFactor.getTwoFactor();
        if(myFactor.getTheCode() == Integer.parseInt(verifyCode.getText())){
            System.out.println("Yay");
        }
        else{
            System.out.println("no");
        }
    }

    @FXML
    void logIn(ActionEvent event) {
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

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //Gets the video resources and stores it as a media file
        Media media = new Media(getClass().getResource("/resources/Pressure.mp4").toExternalForm());

        //Creates the media player to play the video
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        //Sets the video on infinite loop and passes it to the mediaView to display on the screen
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView.setMediaPlayer(mediaPlayer);

        assert mainContent != null : "fx:id=\"mainContent\" was not injected: check your FXML file 'login.fxml'.";
        assert mediaView != null : "fx:id=\"mediaView\" was not injected: check your FXML file 'login.fxml'.";
    }

    private double generateCode(){
        double one = Math.random();
        double two = Math.random();
        double three = Math.random();
        double four = Math.random();
        double five = Math.random();
        double six = Math.random();
        double combind = one + two * 10 + three * 100 + four * 1000 + five * 10000 + six * 100000;
        return combind;
    }
}
