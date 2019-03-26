package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Font.loadFont(getClass().getResource("/resources/palatino-linotype/palab.ttf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("/resources/palatino-linotype/pala.ttf").toExternalForm(), 10);

        Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
        Scene mainScene = new Scene(root, 1024, 768);
        mainScene.setFill(Color.web("#012d5a"));
        primaryStage.setTitle("Brigham and Women's Hospital");
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        //primaryStage.setFullScreen(true);
    }


    public static void main(String[] args) {

        CSVParser parse = new CSVParser("C:\\Users\\kenne\\IdeaProjects\\3733-Project\\src\\app\\nodes.csv", "C:\\Users\\kenne\\IdeaProjects\\3733-Project\\src\\app\\edges.csv");
        AStar aS = new AStar();

        Map<String, Node> mappedNodes = parse.getNodes();

        List<Node> path = aS.findPath(mappedNodes.get("GHALL002L1"), mappedNodes.get("GHALL006L1"));
        aS.drawPath(path);



        launch(args);
    }
}
