package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;

import edu.wpi.cs3733.d19.teamM.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Settings {

    @FXML
    private void navigateToHome() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("admin"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    public void navigateToWelcome() throws Exception{
        Main.logOut();
    }

}
