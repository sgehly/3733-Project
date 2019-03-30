package app.controllers;

import app.AStar.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Home {

    @FXML
    public void navigateToPathfinding() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("pathfinding"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    public void navigateToServiceRequests() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("serviceRequests"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    public void logout() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }
}
