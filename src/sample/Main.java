package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.scene.text.Font;

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
        launch(args);
    }
}
