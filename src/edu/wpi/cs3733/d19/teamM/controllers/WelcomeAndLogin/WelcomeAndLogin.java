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
import edu.wpi.cs3733.d19.teamM.utilities.*;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;

//FXML packages
import io.ably.lib.types.PaginatedResult;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.util.Duration;
import twitter4j.Status;

//SQL imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private VBox codeField; //area that contains the objects where you enter code
    @FXML
    private HBox loginBox; //areea that contains the login button object


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
    private Label secondFactorLabel;


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

    Timeline clock;
    Timeline reverseClock;



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

        code1.setEditable(true);
        code2.setEditable(true);
        code3.setEditable(true);
        code4.setEditable(true);

        double delta = 0.0004;

        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {


            if(scrollContainer.getHvalue() == 1){
                scrollContainer.setHvalue(0);
                //this.clock.stop();
                //this.reverseClock.play();
            }

            scrollContainer.setHvalue(scrollContainer.getHvalue()+delta);

        }), new KeyFrame(Duration.seconds(0.01)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


        reverseClock = new Timeline(new KeyFrame(Duration.ZERO, e -> {


            if(scrollContainer.getHvalue() == 0){
                //this.reverseClock.stop();
                //his.clock.play();
            }

            scrollContainer.setHvalue(scrollContainer.getHvalue()-delta);

        }), new KeyFrame(Duration.seconds(0.01)));
        reverseClock.setCycleCount(Animation.INDEFINITE);

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


                Platform.runLater(() -> {
                    try{
                        PaginatedResult<io.ably.lib.types.Message> resultPage = Main.channel.history(null);

                        if(resultPage.items().length < 1){
                            notificationTitle.setText("No New Notifications");
                            notificationText.setText("Have a nice day!");
                            return;
                        }
                        io.ably.lib.types.Message lastMessage = resultPage.items()[0];

                        double width = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(lastMessage.data.toString(), Font.font("Open Sans"));
                        notificationTitle.setText(lastMessage.name);
                        notificationText.setText(lastMessage.data.toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                });


        }).start();

        code1.requestFocus();
        code1.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {



                if(code1.getText().length()==1)
                {
                    code1.setEditable(false);
                }

              /*  if (!event.getCharacter().isEmpty()) {
                    code2.requestFocus();
                    code2.setText("");
                }*/

            }
        });

        code2.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCharacter());

                if(code2.getText().length()==1)
                {
                    code2.setEditable(false);


                }

           /*     if (!event.getCharacter().isEmpty()) {
                    code3.requestFocus();
                    code3.setText("");
                }*/

            }
        });

        code3.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCharacter());
                if(code3.getText().length()==1)
                {
                    code3.setEditable(false);

                }

           /*    if (!event.getCharacter().isEmpty()) {
                    code4.requestFocus();
                    code4.setText("");
                }*/

            }
        });

        code4.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCharacter());

                if(code4.getText().length()==1)
                {
                    code4.setEditable(false);

                }
            }
        });
    }


    /**
     * Helper functions used by the initialize() method
     */
    private void setInitialOpacities() {
        welcomeField.setOpacity(1);
        //overrideLoginButtonBox.setOpacity(0);
        loginField.setOpacity(0);
        codeField.setOpacity(0);
        loginBox.setOpacity(0);
        loginError.setOpacity(0);
        secondFactorLabel.setOpacity(0);
        codeErrorLabel.setOpacity(0);
        //overrideLoginButton.setOpacity(0);
    }

    private void setInitialDisabled() {
        welcomeField.setDisable(false);
        loginField.setDisable(true);
        codeField.setDisable(true);
        loginBox.setDisable(true);
        //overrideLoginButton.setDisable(true);
        //overrideLoginButtonBox.setDisable(true);
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
        second.getChildren().addAll(transitions.fadeOut(loginField, 500), transitions.fadeCusion(welcomeField, 100), transitions.fadeIn(secondFactorLabel, 500));

        //setting up the transition between phone and code
        phoneToCodeTransition = new SequentialTransition();
        first = new ParallelTransition();
        first.getChildren().addAll(transitions.fadeCusion(secondFactorLabel, 500), transitions.fadeIn(secondFactorLabel, 500));
        loginToPhoneTransition = new ParallelTransition();
        phoneToCodeTransition.getChildren().addAll(transitions.fadeOut(loginField, 500), transitions.fadeIn(codeField, 500), transitions.fadeCusion(welcomeField, 50), transitions.fadeIn(loginBox, 500));
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
            transitions.fadeOut(codeField, 500);

            loginBox.setDisable(true);
            welcomeField.setDisable(true);
            loginField.setDisable(false);
            System.out.println("the login stuff is enabled");

            welcomeToLoginTransition.play();
            username.requestFocus();
        }
        hasNotBeenClicked = false;
    }
    //this is called when you press enter or the button on login and you are transitioned to the phone input section
    @FXML
    public void transitionToPhone(){
        //this if statement ensures that the transition will only appear once everytime it is loaded
       this.transitionToCode();
    }

    @FXML
    public void goBack(){
        //hasNotBeenClicked = true;
        hasNotBeenClicked3 = true;
        this.transitionBackToLogin();
    }
    //this is called when you enter your phone number and press enter or the send code button and you are transitioned to the code section
    @FXML
    public void transitionToCode(){
        if(hasNotBeenClicked3){
            secondFactorLabel.setText("Enter Sent Code");
            codeField.setDisable(false);
            loginToPhoneTransition.play();
            code1.requestFocus();
            hasNotBeenClicked3 = false;
            loginError.setOpacity(0);
            loginField.setDisable(true);
            loginBox.setDisable(false);
            new Thread(() -> {
                this.sendVerify();
            }).start();
        }
    }
    //this is called when you have completed filling in the code and you are shown the login button
    @FXML
    public void transitionBackToLogin(){
        loginField.setDisable(false);
        transitions.fadeOut(secondFactorLabel, 500).play();
        transitions.fadeOut(loginBox, 500).play();
        transitions.fadeOut(codeField, 500).play();
        SequentialTransition transitionBackToLogin = new SequentialTransition();
        transitionBackToLogin.getChildren().addAll(transitions.fadeIn(loginField,1000));
        transitionBackToLogin.play();
        hasNotBeenClicked2 = true;

    }
    @FXML
    public void displayLogin(KeyEvent e){

        if (e.getCharacter().equals("")) {
            code4.setText("");
            code3.requestFocus();
            code3.setText("");
            return;
        }

        loginBox.setDisable(false);
        SequentialTransition loginAppear = new SequentialTransition();
        if(!hasBeenAdjusted) {
            //transitions.fadeIn(loginBox, 500).play();
            hasBeenAdjusted = true;
        }
        else{

        }
    }

    @FXML
    private void checkButtonManual(){
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
                    else {
                        loginError.setOpacity(0);
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
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        String query = "SELECT * from USERS where USERNAME = ?";
        Connection conn = DBUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username.getText());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        System.out.println("hey check me out: " + rs.getString("PHONEEMAIL"));
        if (rs.getString("PHONEEMAIL") == null || rs.getString("PHONEEMAIL").length() == 0) {
            conn.close();
            return false;
        }
        conn.close();
        return true;
    }
    //helper function for onKeyPressed() to figure out if the inputted username and password is a valid one
    private boolean isValidLogin() throws SQLException {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        String query = "SELECT * from USERS where USERNAME = ?";
        Connection conn = DBUtils.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username.getText());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        try
        {
            if (Encrypt.getMd5(password.getText()).equals(rs.getString("USERPASS"))) {
                conn.close();
                return true;
            }
            else{
                loginError.setOpacity(1);
                //transitions.pulseText(loginLabel, loginLabelBox, "Username or password is invalid");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            loginError.setOpacity(1);
        }

        conn.close();
        return false;
    }


    /**
     * FXML calls from the code boxees that automatically allow them to tab to the next box once something is typed
     */
    @FXML
    void tabTo2(KeyEvent e){
        System.out.println("Char /"+e.getCharacter()+"/"+e.getCode());
        if (e.getCharacter().equals("")) {
            return;
        }

        if(!tabTrigger) {
            System.out.println("real deal!");
            tabTrigger = true;
        }
        else{
            code2.requestFocus();
            code2.setText("");
        }
    }
    @FXML
    void tabTo3(KeyEvent e){
        if (e.getCharacter().equals("")) {
            code2.setText("");
            code1.requestFocus();
            code1.setText("");
            return;
        }
        code3.requestFocus();
    }
    @FXML
    void tabTo4(KeyEvent e){
        if (e.getCharacter().equals("")) {
            code3.setText("");
            code2.requestFocus();
            code2.setText("");
            return;
        }
        code4.requestFocus();
    }



    @FXML
    void logIn() {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        /*
        TODO add username to pages: scheduler, service requests, sr list
         */
        try {
            String query = "SELECT * from USERS where USERNAME = ?";
            Connection conn = DBUtils.getConnection();
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
    }

    private void clearAllTextfields() {
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

            int myCode = (int)generateCode();
            Twilio.init("ACbfbd0226f179ee74597c887298cbda10", "eeb459634d5a8407d077635504386d44");
            try {
                Message message = Message.creator(new PhoneNumber(emailNumber1), new PhoneNumber("+15085383787"), "Hello from Brigham & Women's! Your authentication code is " + myCode).create();
                TwoFactor myFactor = TwoFactor.getTwoFactor();
                myFactor.setTheCode(myCode);
                System.out.println("sent");
                //this.transitionToCode();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("failed");
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
            code1.setEditable(true);
            code2.setEditable(true);
            code3.setEditable(true);
            code4.setEditable(true);
        }
        else{
            code1.clear();
            code2.clear();
            code3.clear();
            code4.clear();
            code1.setEditable(true);
            code2.setEditable(true);
            code3.setEditable(true);
            code4.setEditable(true);
            code1.requestFocus();
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