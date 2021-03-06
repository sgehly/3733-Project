package edu.wpi.cs3733.d19.teamM;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.General.Options;
import edu.wpi.cs3733.d19.teamM.utilities.Timeout.IdleMonitor;
import edu.wpi.cs3733.d19.teamM.utilities.Timeout.SavedState;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.realtime.CompletionListener;
import io.ably.lib.types.ClientOptions;
import io.ably.lib.types.ErrorInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
//import giftRequest.*;
import jdk.nashorn.internal.objects.Global;
import org.checkerframework.checker.nullness.Opt;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;


/**
 * Primary class that runs the JavaFx Application
 */
public class Main extends Application {

    //Create the primary stage and set it to null



    static boolean isLoaded = false;

    public static Stage primaryStage = null;

    final static KeyCombination keyShiftTab = new KeyCodeCombination(KeyCode.TAB, KeyCombination.SHIFT_ANY);

    private static Parent homePane;
    private static Parent adminPane;
    private static Parent schedulerPane;
    private static Parent serviceRequestPane;
    private static Parent serviceRequestListPane;
    private static Parent welcomePane;
    private static Parent loginPane;
    private static Parent addUserPane;

    private static Scene homeScene;
    private static Scene adminScene;

    private static Scene schedulerScene;
    private static Scene serviceRequestScene;
    private static Scene serviceRequestListScene;
    private static Scene welcomeScene;
    private static Scene calendar;
    private static Scene loginScene;
    private static Scene addUserScene;

    private static IdleMonitor idleMonitor;
    public static SavedState savedState;

    public static Channel channel;

    public static Channel dmChannel;

    public static AblyRealtime ably;

    private static Timer timer;

    /**
     * This method is to return the current stage we are working on for referencing the stage
     * @return Stage: The current stage we are using
     */
    public static Stage getStage(){
        return Main.primaryStage;
    }

    public static void startIdleCheck(){

    }

    public static void setScene(String scene){
        if(scene == "admin"){
            primaryStage.setScene(adminScene);
            savedState.setState("admin");
        }
        else if(scene == "scheduling"){
            primaryStage.setScene(schedulerScene);
            savedState.setState("scheduling");
        }

        else if(scene == "serviceRequest"){
            primaryStage.setScene(serviceRequestScene);
            savedState.setState("serviceRequests");
        }
        else if(scene == "serviceRequestList"){
            primaryStage.setScene(serviceRequestListScene);
            savedState.setState("serviceRequestList");
        }
        else if(scene == "welcome"){
            try{dmChannel.detach();}catch(Exception e){}
            primaryStage.setScene(welcomeScene);
        }
        else if(scene == "login"){
            primaryStage.setScene(loginScene);
        }else{
            try{
                primaryStage.setScene(new Scene(FXMLLoader.load(Main.getFXMLURL(scene))));
                //primaryStage.sizeToScene();
                //primaryStage.setMaximized(true);
            }catch(Exception e){e.printStackTrace();}
       }

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setMaxWidth(primaryScreenBounds.getWidth());
        primaryStage.setMaxHeight(primaryScreenBounds.getHeight());

        /*if(scene != "welcome"){

            EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>(){
                @Override
                public void handle(KeyEvent e)
                {
                    if (keyShiftTab.match(e))
                    {
                        Main.setScene("welcome");
                        e.consume();
                    }
                }
            };

            primaryStage.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, handler);
            primaryStage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, handler);
        }*/

    }

    /**
     * This method is to reference and access FXML pages to display. The name of the page is input and a URL is output
     * @param name: The name of the FXML page we wish to access
     * @return URL: The url of the page we want to access
     */
    public static URL getFXMLURL(String name){
        return Main.class.getResource("views/"+name+".fxml");
    }

    public static InputStream getResource(String path){
        System.out.println("Fetching Resource: "+path);
        return Main.class.getResourceAsStream(path);
    }

    public static InputStream getResourceFromRoot(String path){
        System.out.println("Fetching Resource from Root: "+path);
        return Main.class.getClassLoader().getResourceAsStream(path);
    }

    public static void loadScenes() throws Exception {
        homePane = FXMLLoader.load(Main.getFXMLURL("home"));
        homeScene = new Scene(Main.homePane);

        if(true) {
            Runnable loadAdminThread = () -> {
                Platform.runLater(() -> {
                    try {
                        System.out.println("Loading scenes");
                        adminPane = FXMLLoader.load(Main.getFXMLURL("admin"));
                        System.out.println(adminPane);
                        adminScene = new Scene(adminPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            };
            Runnable loadSchedulerThread = () -> {
                Platform.runLater(() -> {
                    try {
                        schedulerPane = FXMLLoader.load(Main.getFXMLURL("scheduler"));
                        schedulerScene = new Scene(schedulerPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            };
            Runnable loadServiceRequestsThread = () -> {
                Platform.runLater(() -> {
                    try {
                        serviceRequestPane = FXMLLoader.load(Main.getFXMLURL("serviceRequests"));
                        serviceRequestScene = new Scene(serviceRequestPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            };
            Runnable loadSRListThread = () -> {
                Platform.runLater(() -> {
                    try {
                        serviceRequestListPane = FXMLLoader.load(Main.getFXMLURL("serviceRequestsList"));
                        serviceRequestListScene = new Scene(serviceRequestListPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            };


            new Thread(loadAdminThread).start();
            new Thread(loadSchedulerThread).start();
            new Thread(loadServiceRequestsThread).start();
            new Thread(loadSRListThread).start();

            channel.presence.leave("", new CompletionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(ErrorInfo reason) {

                }
            });
            channel.detach();

            if(ably.connection != null && ably.connection.id != null){
                ably.close();
                ably = null;
            }


            ClientOptions options = new ClientOptions("URg4iA.H7_X5w:2Zc5-2d-nGC8UmjV");
            options.clientId = User.getUsername();
            ably = new AblyRealtime(options);

            dmChannel = ably.channels.get("usernotify-"+User.getUsername());
            channel = ably.channels.get("notifications");

            Main.notificationSubscribe(dmChannel);
            Main.notificationSubscribe(channel);

            isLoaded = true;

        }
        else{

        }
    };

    public static void loadAddUsers() throws Exception{
        addUserPane = FXMLLoader.load(Main.getFXMLURL("addUser"));
        addUserScene = new Scene(addUserPane);
    }

    public static void logOut(){
        Main.savedState.setState("home");
        Main.setScene("welcome");
    }

    public static void updateTimer(){
        Options o = Options.getOptions();

        timer.stop();
        timer = null;

        ActionListener uiReset = e -> Platform.runLater(() -> {
            timer.restart();
            Main.setScene("welcome");
        });



        timer = new Timer(o.getTimeout(), uiReset);

        /*Options.getOptions();
        ActionListener uiReset = e -> Platform.runLater(() -> {
            Main.setScene("welcome");
            timer.restart();
        });  //resets ui on timer trigger
        timer = new Timer(1000000,uiReset); //creates the timer, attach to ui reset
        primaryStage.addEventHandler(MouseEvent.MOUSE_MOVED,idleReset);  //add event handler to the application
        //Options.setTimeout(10000);
        */

        EventHandler<MouseEvent> idleReset = e -> timer.restart(); //event handler to reset the timer

        primaryStage.removeEventHandler(MouseEvent.MOUSE_MOVED,idleReset);
        primaryStage.addEventHandler(MouseEvent.MOUSE_MOVED,idleReset);

        System.out.println(o.getTimeout());

        timer.start();
    }

    /**
     * This method creates and sets the stage of the viewable JavaFX screen
     * @param primaryStage: The stage to display on start
     * @throws Exception: Any possible Exception that may arise while trying to start and display the application
     */
    @Override
    public void start(Stage primaryStage) throws Exception{


        //TODO on logout, set memento to home DONE
        //TODO on login, login to memento saved state DONE
        //TODO change memento based on visited pages DONE
        savedState = new SavedState();

        ably = new AblyRealtime("URg4iA.H7_X5w:2Zc5-2d-nGC8UmjV");
        channel = ably.channels.get("notifications");
        Main.notificationSubscribe(channel);

        //Set the reference to the primary stage
        this.primaryStage = primaryStage;

        //Load the fonts that we want to use for the application
        //Fonts have been taken from what B & H hospital uses as their official fonts
        Font.loadFont(Main.getResourceFromRoot("resources/palatino-linotype/palab.ttf"), 10);
        Font.loadFont(Main.getResourceFromRoot("resources/palatino-linotype/pala.ttf"), 10);
        Font.loadFont(Main.getResourceFromRoot("resources/fonts/SourceSerifPro-Black.otf"), 10);
        Font.loadFont(Main.getResourceFromRoot("resources/fonts/SourceSerifPro-Bold.otf"), 10);
        Font.loadFont(Main.getResourceFromRoot("resources/fonts/SourceSerifPro-Regular.otf"), 10);
        Font.loadFont(Main.getResourceFromRoot("resources/fonts/SourceSerifPro-Semibold.otf"), 10);
        Font.loadFont(Main.getResourceFromRoot("resources/fonts/Prequel-bold.otf"), 10);
        Font.loadFont(Main.getResourceFromRoot("resources/fonts/VarelaRound-Regular.otf"), 10);
        Font.loadFont(Main.getResourceFromRoot("resources/fonts/Bariol_Serif_Regular.otf"), 10);
        //Get the main parent scene and load the FXML
        Parent root = FXMLLoader.load(Main.getFXMLURL("welcome"));
        Scene mainScene = new Scene(root);

//        loginPane = FXMLLoader.load(Main.getFXMLURL("login"));
//        loginScene = new Scene(loginPane);
        welcomePane = FXMLLoader.load(Main.getFXMLURL("welcome"));
        welcomeScene= new Scene(welcomePane);

        //create the idle detection system
        Options.getOptions();
        ActionListener uiReset = e -> Platform.runLater(() -> {
            Main.setScene("welcome");
            timer.restart();
        });  //resets ui on timer trigger
        timer = new Timer(1000000,uiReset); //creates the timer, attach to ui reset
        EventHandler<MouseEvent> idleReset = e -> timer.restart(); //event handler to reset the timer
        primaryStage.addEventHandler(MouseEvent.MOUSE_MOVED,idleReset);  //add event handler to the application
        //Options.setTimeout(10000);
        timer.start();


        //Set the color and the title and the screen
        mainScene.setFill(Color.web("#012d5a"));
        primaryStage.setTitle("Brigham and Women's Hospital");
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);

        //Set the bounds and other height and width attributes
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setFullScreen(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                try{
                    ably.close();
                }catch(Exception e){}

                Platform.exit();

                Thread start = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO Auto-generated method stub
                        System.exit(0);
                    }
                });

                start.start();
            }
        });
    }

    public static void notificationSubscribe(Channel channel) throws Exception {
        channel.subscribe(message -> {
            System.out.println("New message! " + message.name + " - " + message.data.toString());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    StackPane stackPane = new StackPane();
                    //stackPane.autosize();
                    JFXDialogLayout content = new JFXDialogLayout();
                    content.setHeading(new Text(message.name));
                    content.setBody(new Text(message.data.toString()));
                    JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
                    Pane imInPane = (Pane) primaryStage.getScene().getRoot();
                    imInPane.getChildren().add(stackPane);

                   // Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

                    //System.out.println(content.getLayoutBounds().getWidth()+"/"+content.getLayoutBounds().getHeight());
                    AnchorPane.setBottomAnchor(stackPane, 10.0);
                    AnchorPane.setRightAnchor(stackPane, 10.0);
                    dialog.show();

                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                this.sleep(5000);
                                Platform.runLater(() -> {
                                    dialog.close();
                                    imInPane.getChildren().remove(stackPane);
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });

        });

        channel.presence.enter("fjklsdnfjkdf", new CompletionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(ErrorInfo reason) {

            }
        });
    }

    /**
     * The primary method that is run once the application starts
     * @param args: The arguments for the application, if any (none in this case)
     */
    public static void main(String[] args) {
        DatabaseUtils parser = DatabaseUtils.getDBUtils();
        parser.connect();
        parser.nodeParse();
        parser.edgeParse();
        launch(args);
    }

}
