package app;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import java.net.URL;

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

        Main.primaryStage = primaryStage;

        Font.loadFont(getClass().getResource("/resources/palatino-linotype/palab.ttf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("/resources/palatino-linotype/pala.ttf").toExternalForm(), 10);

        Parent root = FXMLLoader.load(getClass().getResource("views/home.fxml"));
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
        launch(args);
    }
}
