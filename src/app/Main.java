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

import javax.xml.crypto.Data;

public class Main extends Application {

    private static Stage primaryStage = null;

    public static Stage getStage(){
       return Main.primaryStage;
    }

    public static URL getFXMLURL(String name){
            return Main.class.getResource("views/"+name+".fxml");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        Font.loadFont(getClass().getResource("../resources/palatino-linotype/palab.ttf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("../resources/palatino-linotype/pala.ttf").toExternalForm(), 10);

        Parent root = FXMLLoader.load(getClass().getResource("views/welcome.fxml"));
        Scene mainScene = new Scene(root);
        mainScene.setFill(Color.web("#012d5a"));
        primaryStage.setTitle("Brigham and Women's Hospital");
        primaryStage.setScene(mainScene);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setMaxWidth(bounds.getWidth() * 2);
        primaryStage.show();
    }

    public static void main(String[] args) {

//        CSVParser parse = new CSVParser("C:\\Users\\kenne\\IdeaProjects\\3733-Project\\src\\app\\nodes.csv", "C:\\Users\\kenne\\IdeaProjects\\3733-Project\\src\\app\\edges.csv");
//        AStar aS = new AStar();
//
//        Map<String, Node> mappedNodes = parse.getNodes();

//        List<Node> path = aS.findPath(mappedNodes.get("GHALL002L1"), mappedNodes.get("GHALL006L1"));
        //aS.drawPath(path);
       /* DatabaseParser parser = new DatabaseParser();
        parser.nodeParse();
        parser.edgeParse();*/

        DatabaseParser parser = new DatabaseParser();
        //parser.edgeParse();
        //parser.nodeParse();

        launch(args);
        //DatabaseParser parser = new DatabaseParser();

        //parser.nodeParse();
        //parser.edgeParse();
        //parser.floorTables();
    }
}
