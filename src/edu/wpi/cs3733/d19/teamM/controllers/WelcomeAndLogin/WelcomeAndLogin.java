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
import javafx.scene.layout.HBox;
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
import java.sql.SQLException;

/**
 * This class is the controller for the primary welcome screen
 */
public class WelcomeAndLogin {

    @FXML
    private VBox welcomeField; //area that contains all welcome objects

    @FXML
    private VBox loginField; //area that cofntains all of the login objects

    @FXML
    private HBox phoneField;

    @FXML
    private VBox codeField;

    @FXML
    private HBox loginBox;


    @FXML
    private TextField code1;

    @FXML
    private TextField code2;

    @FXML
    private TextField code3;

    @FXML
    private TextField code4;

    private boolean tabTrigger;


    @FXML
    private MediaView mediaView; //object for playing background video

    @FXML
    private TextField username; //username text field

    @FXML
    private PasswordField password; //password text field



    @FXML
    private Button verifyButton;

    @FXML
    private Button resendButton;

    @FXML
    private Button loginButton2;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Label sent;

    @FXML
    private Label wrong;

    @FXML
    private Label titleLabel;

    @FXML
    private Label loginError;

    @FXML
    private HBox codeBox;

    @FXML
    private Label loginLabel;

    @FXML
    private Label secondFactorLabel;




    private SequentialTransition welcomeToLoginTransition; //used to make transition between welcome and login
    private SequentialTransition loginToPhoneTransition;
    private SequentialTransition phoneToCodeTransition;
    private SequentialTransition fadeOutFadeIn;

    private boolean hasNotBeenClicked; //trigger to indicate whether the screen has been clicked or not
    private boolean hasNotBeenClicked2; //trigger to indicate whether the login button has been pressed yet
    private boolean hasNotBeenClicked3; //trigger to indicate whether the phone code has been sent
    private boolean hasBeenAdjusted; //triggers when the code boxes move




    /**
     * This method is used to initialize the application by loading all necessary aspects and displaying them
     */
    @FXML
    protected void initialize(){
        loginField.setOpacity(0); //initially needs to be invisible
        loginField.setDisable(true);
        hasNotBeenClicked = true; //stating that the screen hasn't been clicked as the transition should happen exactly once per login

        phoneField.setOpacity(0);
        phoneField.setDisable(true);
        hasNotBeenClicked2 = true;

        codeField.setOpacity(0);
        codeField.setDisable(true);
        hasNotBeenClicked3 = true;

        loginBox.setOpacity(0);
        loginBox.setDisable(true);

        tabTrigger = false;
        hasBeenAdjusted = false;

        //setting up the transition between welcome and login
        welcomeToLoginTransition = new SequentialTransition();
        welcomeToLoginTransition.getChildren().addAll(this.fadeOut(welcomeField, 500), this.fadeCusion(welcomeField, 100), this.fadeIn(loginField, 500));

        //setting up the transition between login and phone
        loginToPhoneTransition = new SequentialTransition();
        loginToPhoneTransition.getChildren().addAll(this.fadeOut(loginLabel, 500),this.fadeOut(loginField, 500), this.fadeCusion(welcomeField, 100), this.fadeIn(secondFactorLabel, 500), this.fadeIn(phoneField, 500));

        //setting up the transition between phone and code
        phoneToCodeTransition = new SequentialTransition();
        phoneToCodeTransition.getChildren().addAll(this.fadeIn(codeField, 500),this.fadeOut(phoneField, 100), this.fadeCusion(welcomeField, 50), this.raise(codeField, 100, 85));

        fadeOutFadeIn = new SequentialTransition();
        fadeOutFadeIn.getChildren().addAll(this.fadeOut(secondFactorLabel, 500), this.fadeCusion(secondFactorLabel, 250), this.fadeIn(secondFactorLabel, 500));

//        titleLabel.setOpacity(0);
        loginError.setOpacity(0);
        wrong.setOpacity(0);
        secondFactorLabel.setOpacity(0);




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
            //username.requestFocus();
            hasNotBeenClicked = false;
        }
    }

    @FXML
    public void transitionToPhone(){
        //this if statement ensures that the transition will only appear once everytime it is loaded
        if(hasNotBeenClicked2){
            secondFactorLabel.setOpacity(1);
            loginField.setDisable(true);
            phoneField.setDisable(false);
            loginToPhoneTransition.play();
            //phoneNumber.requestFocus();
            hasNotBeenClicked2 = false;
        }
    }

    @FXML
    public void transitionToCode(){
        if(hasNotBeenClicked3){
            this.fadeOut(secondFactorLabel, 1000).play();
            secondFactorLabel.setText("Enter Sent Code");
            this.fadeCusion(secondFactorLabel, 500).play();
            this.fadeIn(secondFactorLabel, 1000).play();
            codeField.setDisable(false);
            phoneToCodeTransition.play();
            code1.requestFocus();
            hasNotBeenClicked3 = false;
        }
    }

    @FXML
    public void displayLogin(){
        loginBox.setDisable(false);
        SequentialTransition loginAppear = new SequentialTransition();
        if(!hasBeenAdjusted) {
            this.fadeIn(loginBox, 500).play();
            hasBeenAdjusted = true;
        }
        else{

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

    private TranslateTransition left(Node anyNode, int duration, int distance){
        TranslateTransition raise = new TranslateTransition();
        raise.setDuration(Duration.millis(duration));
        raise.setNode(anyNode);
        raise.setByX(0-distance);
        return raise;
    }

    private TranslateTransition right(Node anyNode, int duration, int distance){
        TranslateTransition right = new TranslateTransition();
        right.setDuration(Duration.millis(duration));
        right.setNode(anyNode);
        right.setByX(distance);
        return right;
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
    void onKeyPressed(){
        password.setOnKeyPressed(
                event -> {
                    if(event.getCode().equals(KeyCode.ENTER)){
                        try {
                            if(this.isValidLogin()) {
                                this.transitionToPhone();
                            }
                            else{
                                System.out.println("Incorrect username or password");
                                loginError.setText("Incorrect username or password");
                                this.fadeIn(loginError,500).play();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        phoneNumber.setOnKeyPressed(
                event -> {
                    if(event.getCode().equals(KeyCode.ENTER)){
                        this.sendVerify();
                    }
                }
        );
        code4.setOnKeyPressed(
                event -> {
                    if(event.getCode().equals(KeyCode.ENTER)){
                        this.checkCode();
                    }
                }
        );
    }

    private boolean isValidLogin() throws SQLException {
        String query = "SELECT * from USERS where USERNAME = ?";
        Connection conn = new DatabaseUtils().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username.getText());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if (Encrypt.getMd5(password.getText()).compareTo(rs.getString("USERPASS")) == 0) {
            conn.close();
            return true;
        }
        conn.close();
        return false;
    }

    @FXML
    void tabTo2(){
        if(!tabTrigger) {
            System.out.println("real deal!");
            code1.requestFocus();
            tabTrigger = true;
        }
        else{
            code2.requestFocus();
        }
    }
    @FXML
    void tabTo3(){
        code3.requestFocus();
    }
    @FXML
    void tabTo4(){
        code4.requestFocus();
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
            User.getUser();
            User.setUsername(rs.getString("USERNAME"));
            User.setPathToPic(rs.getString("pathtopic"));
            username.setText("");
            password.setText("");
            User.setPrivilege(rs.getInt("ACCOUNTINT"));
            System.out.println("Logged in " + User.getUsername() + " with privilege " + User.getPrivilege());
            Main.loadScenes();
            this.resetScene();
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
        }catch (Exception e) {
            e.printStackTrace();
            username.setText("");
            password.setText("");
        }

    }

    //resets the scene for the next time it is called
    private void resetScene() {
        welcomeField.setOpacity(1);
        loginField.setOpacity(0);
        phoneField.setOpacity(0);
        codeField.setOpacity(0);
        loginBox.setOpacity(0);
        loginError.setOpacity(0);
        secondFactorLabel.setOpacity(0);
        secondFactorLabel.setText("Second Factor");
        hasNotBeenClicked = true;
        hasNotBeenClicked2 = true;
        hasNotBeenClicked3 = true;
        hasBeenAdjusted = false;
        loginField.setDisable(true);
        phoneField.setDisable(true);
        welcomeField.setDisable(false);
        codeField.setDisable(false);
        loginBox.setDisable(false);
        this.clearFields();
        this.right(codeBox,10,200);

        tabTrigger = false;
        this.drop(codeField,10,85).play();
    }

    private void clearFields() {
        phoneNumber.clear();
        code1.clear();
        code2.clear();
        code3.clear();
        code4.clear();
    }

    @FXML
    public void sendVerify() {
        Connection conn = new DatabaseUtils().getConnection();
        String query = "select PHONEEMAIL from USERS where USERNAME = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,username.getText());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String emailNumber1 = rs.getString("PHONEEMAIL");
            System.out.println(emailNumber1);
            System.out.println(phoneNumber.getText());
            if(emailNumber1.equals(phoneNumber.getText())){
                String numberEmail = phoneNumber.getText();
                int myCode = (int)generateCode();
                Twilio.init("ACbfbd0226f179ee74597c887298cbda10", "eeb459634d5a8407d077635504386d44");
                if (!numberEmail.contains("@")) {
                    try {
                        Message message = Message.creator(new PhoneNumber(numberEmail), new PhoneNumber("+15085383787"), "Hello from Brigham & Women's! Your authentication code is " + myCode).create();
                        TwoFactor myFactor = TwoFactor.getTwoFactor();
                        myFactor.setTheCode(myCode);
                        wrong.setTextFill(Color.web("#009933"));
                        wrong.setText("Code sent");
                        this.fadeIn(wrong,1000);
                        System.out.println("sent");
                        this.transitionToCode();
                    } catch (Exception e) {
                        e.printStackTrace();
                        wrong.setTextFill(Color.web("#ff0000"));
                        wrong.setText("Could not send your code");
                        this.fadeIn(wrong,1000);
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
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void checkCode(){
        TwoFactor myFactor = TwoFactor.getTwoFactor();
        if(myFactor.getTheCode() == this.getCode()){
            System.out.println("Yay");
            this.logIn();
        }
        else{
            this.fadeIn(wrong, 750);
            wrong.setTextFill(Color.web("#ff0000"));
            wrong.setText("Code is incorrect");

        }
    }

    private int getCode() {
        String code = "";
        code += code1.getText();
        code += code2.getText();
        code += code3.getText();
        code += code4.getText();
        System.out.println(code);
        return Integer.parseInt(code);
    }

    private double generateCode(){
        double combind = 0;
        while(combind < 1000 || combind >= 10000) {
            double one = Math.random();
            double two = Math.random();
            double three = Math.random();
            double four = Math.random();
            combind = one * 10 + two * 100 + three * 1000 + four * 10000;
        }
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
            User.setPrivilege(0);
            System.out.println("Logged in Guest");
            Main.loadScenes();
            Main.setScene("home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
