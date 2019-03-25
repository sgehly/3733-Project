package app.controllers;

import app.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;

public class Home {

    @FXML
    public void navigateToPathfinding() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("pathfinding"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }
}
