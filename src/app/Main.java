package app;

import app.addDelete.DatabaseParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import java.net.URL;

import java.util.Map;

import app.AStar.*;

/**
 * Primary class that runs the JavaFx Application
 */
public class Main extends Application {

    //Create the primary stage and set it to null
    private static Stage primaryStage = null;

    /**
     * This method is to return the current stage we are working on for referencing the stage
     * @return Stage: The current stage we are using
     */
    public static Stage getStage(){
       return Main.primaryStage;
    }

    /**
     * This method is to reference and access FXML pages to display. The name of the page is input and a URL is output
     * @param name: The name of the FXML page we wish to access
     * @return URL: The url of the page we want to access
     */
    public static URL getFXMLURL(String name){
            return Main.class.getResource("views/"+name+".fxml");
    }

    /**
     * This method creates and sets the stage of the viewable JavaFX screen
     * @param primaryStage: The stage to display on start
     * @throws Exception: Any possible Exception that may arise while trying to start and display the application
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        //Set the reference to the primary stage
        this.primaryStage = primaryStage;

        //Load the fonts that we want to use for the application
        //Fonts have been taken from what B & H hospital uses as their official fonts
        Font.loadFont(getClass().getResource("../resources/palatino-linotype/palab.ttf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("../resources/palatino-linotype/pala.ttf").toExternalForm(), 10);

        //Get the main parent scene and load the FXML
        Parent root = FXMLLoader.load(getClass().getResource("views/welcome.fxml"));
        Scene mainScene = new Scene(root);

        //Set the color and the title and the screen
        mainScene.setFill(Color.web("#012d5a"));
        primaryStage.setTitle("Brigham and Women's Hospital");
        primaryStage.setScene(mainScene);

        //Set the bounds and other height and width attributes
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    /**
     * The primary method that is run once the application starts
     * @param args: The arguments for the application, if any (none in this case)
     */
    public static void main(String[] args) {

        //CSVParser parse = new CSVParser("C:\\Users\\kenne\\IdeaProjects\\3733-Project\\src\\app\\nodes.csv", "C:\\Users\\kenne\\IdeaProjects\\3733-Project\\src\\app\\edges.csv");
        //AStar aS = new AStar();
        //Map<String, Node> mappedNodes = parse.getNodes();
//        List<Node> path = aS.findPath(mappedNodes.get("GHALL002L1"), mappedNodes.get("GHALL006L1"));
        //aS.drawPath(path);
        DatabaseParser parser = new DatabaseParser();

        //parser.connect();
        //parser.nodeParse();
        //parser.edgeParse();
       // parser.floorTables();



       launch(args); //Launches the JavaFX application
    }
}
