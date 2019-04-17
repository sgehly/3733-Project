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
import edu.wpi.cs3733.d19.teamM.utilities.Transitions;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
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

//SQL imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @TODO refactor this code
 * @TODO add back buttons (elaborate more later, but the concept is there)
 */


/**
 * This class is the controller for the primary welcome screen
 */
public class WelcomeAndLogin {

    //background video object
    @FXML
    private MediaView mediaView; //object for playing background video



    //all of the fields that will be put used for transitions
    @FXML
    private VBox welcomeField; //area that contains all welcome objects
    @FXML
    private VBox loginField; //area that contains all of the login objects
    @FXML
    private HBox phoneField; //area that contains the phone number prompt objects
    @FXML
    private VBox codeField; //area that contains the objects where you enter code
    @FXML
    private HBox loginBox; //areea that contains the login button object
    @FXML
    private HBox overrideLoginButtonBox;


    //the text fields that the 2 factor auth code will be entered into
    //there are four because of the 4 digit code, each field will only contain one number and auto tab to the next textfield
    @FXML
    private TextField code1;
    @FXML
    private TextField code2;
    @FXML
    private TextField code3;
    @FXML
    private TextField code4;
    @FXML
    private Label codeErrorLabel;



    //objects that are for the login section
    @FXML
    private Label loginLabel;
    @FXML
    private TextField username; //username text field
    @FXML
    private PasswordField password; //password text field
    @FXML
    private Label loginError; //will let the user know if their credentials are invalid



    //object that is for the two factor section
    @FXML
    private TextField phoneNumber;
    @FXML
    private Label secondFactorLabel;
    @FXML
    private Label phoneNumberErrorLabel;
    @FXML
    private Button overrideLoginButton;



    //transitions used throughout the scene
    Transitions transitions = new Transitions();
    //all components of the loginToPhoneTransition transition
    private ParallelTransition first; //first component of "loginToPhoneTransition"
    private SequentialTransition second; //second component of "loginToPhoneTransition"
    private ParallelTransition loginToPhoneTransition; //transitions for login section to the phone prompt section

    //other transitions that occur in the scene, names are self explanatory
    private SequentialTransition welcomeToLoginTransition; //transitions for welcome section to the login section
    private SequentialTransition phoneToCodeTransition; //transitions for phone prompt section to entering code section
    private SequentialTransition fadeOutFadeIn; //transition for labels popping in and out



    //triggers to keep track of scene progression
    private boolean hasNotBeenClicked; //trigger to indicate whether the screen has been clicked or not
    private boolean hasNotBeenClicked2; //trigger to indicate whether the login button has been pressed yet
    private boolean hasNotBeenClicked3; //trigger to indicate whether the phone code has been sent
    private boolean hasBeenAdjusted; //triggers when the code boxes move
    private boolean tabTrigger; //triggers when tabbing can begin between the code boxes (is used to fix a bug)



    @FXML
    protected void initialize(){
        //initial steps for setting up the scene
        this.setInitialOpacities();
        this.setInitialDisabled();
        this.setInitialTriggers();
        this.setupTransitions();
        this.startMedia();
    }


    /**
     * Helper functions used by the initialize() method
     */
    private void setInitialOpacities() {
        welcomeField.setOpacity(1);
        overrideLoginButtonBox.setOpacity(0);
        loginField.setOpacity(0);
        phoneField.setOpacity(0);
        codeField.setOpacity(0);
        loginBox.setOpacity(0);
        loginError.setOpacity(0);
        secondFactorLabel.setOpacity(0);
        phoneNumberErrorLabel.setOpacity(0);
        codeErrorLabel.setOpacity(0);
        overrideLoginButton.setOpacity(0);
    }

    private void setInitialDisabled() {
        welcomeField.setDisable(false);
        loginField.setDisable(true);
        phoneField.setDisable(true);
        codeField.setDisable(true);
        loginBox.setDisable(true);
        overrideLoginButton.setDisable(true);
        overrideLoginButtonBox.setDisable(true);
    }

    private void setInitialTriggers() {
        hasNotBeenClicked = true; //stating that the screen hasn't been clicked as the transition should happen exactly once per login
        hasNotBeenClicked2 = true;
        hasNotBeenClicked3 = true;
        tabTrigger = false;
        hasBeenAdjusted = false;
    }

    private void setupTransitions() {
        //setting up the transition between welcome and login
        welcomeToLoginTransition = new SequentialTransition();
        welcomeToLoginTransition.getChildren().addAll(transitions.fadeOut(welcomeField, 500), transitions.fadeCusion(welcomeField, 100), transitions.fadeIn(loginField, 500));

        //setting up the transition between login and phone
        second = new SequentialTransition();
        second.getChildren().addAll(transitions.fadeOut(loginField, 500), transitions.fadeCusion(welcomeField, 100), transitions.fadeIn(secondFactorLabel, 500), transitions.fadeIn(phoneField, 500));

        //setting up the transition between phone and code
        phoneToCodeTransition = new SequentialTransition();
        first = new ParallelTransition();
        first.getChildren().addAll(transitions.fadeCusion(secondFactorLabel, 500), transitions.fadeIn(secondFactorLabel, 500));
        loginToPhoneTransition = new ParallelTransition();
        phoneToCodeTransition.getChildren().addAll(transitions.fadeIn(codeField, 500),transitions.fadeOut(phoneField, 100), transitions.fadeCusion(welcomeField, 50), transitions.raise(codeField, 250, 150));
        loginToPhoneTransition.getChildren().addAll(first, phoneToCodeTransition);

        fadeOutFadeIn = new SequentialTransition();
        fadeOutFadeIn.getChildren().addAll(transitions.fadeOut(secondFactorLabel, 500), transitions.fadeCusion(secondFactorLabel, 250), transitions.fadeIn(secondFactorLabel, 500));
    }

    private void startMedia() {
        Media media = new Media(getClass().getResource("/resources/Pressure.mp4").toExternalForm());//sets media to specified video
        MediaPlayer mediaPlayer = new MediaPlayer(media); //initialize the MediaPlayer to play the video
        mediaPlayer.setAutoPlay(true); //auto play
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); //play indefinitely
        mediaView.setMediaPlayer(mediaPlayer); //setting the mediaview FXML object to contain the specified media player
    }


    /**
     * Transition functions that the FXML file calls at certain points
     */
    //this is called when you click anywhere on the screen when the welcome section is displayed and it fades into the login section
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
    //this is called when you press enter or the button on login and you are transitioned to the phone input section
    @FXML
    public void transitionToPhone(){
        //this if statement ensures that the transition will only appear once everytime it is loaded
        if(hasNotBeenClicked2){
            this.overrideLoginButtonBox.setDisable(false);
            transitions.fadeIn(overrideLoginButtonBox, 1000).play();
            loginField.setDisable(true);
            phoneField.setDisable(false);
            second.play();
            hasNotBeenClicked2 = false;
            overrideLoginButton.setDisable(false);
            transitions.fadeIn(overrideLoginButton, 1000).play();
        }
    }
    //this is called when you enter your phone number and press enter or the send code button and you are transitioned to the code section
    @FXML
    public void transitionToCode(){
        if(hasNotBeenClicked3){
            transitions.fadeOut(overrideLoginButtonBox, 1000).play();
            phoneNumberErrorLabel.setOpacity(0);
            secondFactorLabel.setText("Enter Sent Code");
            codeField.setDisable(false);
            loginToPhoneTransition.play();
            code1.requestFocus();
            hasNotBeenClicked3 = false;
            loginError.setOpacity(0);
        }
    }
    //this is called when you have completed filling in the code and you are shown the login button
    @FXML
    public void transitionBackToLogin(){
        phoneField.setDisable(true);
        loginField.setDisable(false);
        transitions.fadeOut(secondFactorLabel, 500).play();
        SequentialTransition transitionBackToLogin = new SequentialTransition();
        transitions.fadeOut(overrideLoginButtonBox, 1000).play();
        transitionBackToLogin.getChildren().addAll(transitions.fadeOut(phoneField, 1000), transitions.fadeCusion(phoneField,500), transitions.fadeIn(loginField,1000));
        transitionBackToLogin.play();
        hasNotBeenClicked2 = true;

    }
    @FXML
    public void displayLogin(){
        loginBox.setDisable(false);
        SequentialTransition loginAppear = new SequentialTransition();
        if(!hasBeenAdjusted) {
            transitions.fadeIn(loginBox, 500).play();
            hasBeenAdjusted = true;
        }
        else{

        }
    }


    /**
     * This function is a handler for all times you may feel it necessary to press enter instead of a button
     */
    @FXML
    void onKeyPressed(){
        //handles when you press enter when typing in the password; alternatively you can press the login button
        password.setOnKeyPressed(
                event -> {
                    if(event.getCode().equals(KeyCode.ENTER)){
                        try {
                            if(this.isValidLogin()) {
                                if(this.hasPhoneNumber()) {
                                    this.transitionToPhone();
                                }
                                else{
                                    this.logIn();
                                }
                            }
                            else{
                                System.out.println("Incorrect username or password");
                                loginError.setOpacity(1);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        //handles when you press enter when typing in the phone number; alternatively you can press the "send code" button
        phoneNumber.setOnKeyPressed(
                event -> {
                    if(event.getCode().equals(KeyCode.ENTER)){
                        this.sendVerify();
                    }
                }
        );
        //handles when you press enter on the final code box; alternatively you can press the login button
        code4.setOnKeyPressed(
                event -> {
                    if(event.getCode().equals(KeyCode.ENTER)){
                        this.checkCode();
                    }
                }
        );
    }
    //helper function for onKeyPressed() to figure out if the user has a phone number connected to the account
    private boolean hasPhoneNumber() throws SQLException {
        String query = "SELECT * from USERS where USERNAME = ?";
        Connection conn = new DatabaseUtils().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username.getText());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        System.out.println("hey check me out: " + rs.getString("PHONEEMAIL"));
        if (rs.getString("PHONEEMAIL") == null) {
            conn.close();
            return false;
        }
        conn.close();
        return true;
    }
    //helper function for onKeyPressed() to figure out if the inputted username and password is a valid one
    private boolean isValidLogin() throws SQLException {
        String query = "SELECT * from USERS where USERNAME = ?";
        Connection conn = new DatabaseUtils().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username.getText());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if (Encrypt.getMd5(password.getText()).equals(rs.getString("USERPASS"))) {
            conn.close();
            return true;
        }
        else{
            loginError.setOpacity(1);
            //transitions.pulseText(loginLabel, loginLabelBox, "Username or password is invalid");
        }
        conn.close();
        return false;
    }


    /**
     * FXML calls from the code boxees that automatically allow them to tab to the next box once something is typed
     */
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
        this.setInitialOpacities();
        this.setInitialDisabled();
        this.setInitialTriggers();
        this.clearAllTextfields();

        secondFactorLabel.setText("Second Factor");
        transitions.drop(codeField,10,150).play();
    }

    private void clearAllTextfields() {
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
                        System.out.println("sent");
                        this.transitionToCode();
                    } catch (Exception e) {
                        e.printStackTrace();
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
                        System.out.println("sent");
                    } catch (SendGridException e) {
                        System.out.println(e);
                        System.out.println("failed");
                    }
                }
            }
            else{
                System.out.println("showing phone error");
                phoneNumberErrorLabel.setOpacity(1);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void checkCode(){
        TwoFactor myFactor = TwoFactor.getTwoFactor();
        if(myFactor.getTheCode() == this.getUserInputtedCode()){
            System.out.println("Yay");
            this.logIn();
        }
        else{
            codeErrorLabel.setOpacity(1);
        }
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

    private int getUserInputtedCode() {
        String code = "";
        code += code1.getText();
        code += code2.getText();
        code += code3.getText();
        code += code4.getText();
        System.out.println(code);
        return Integer.parseInt(code);
    }



}