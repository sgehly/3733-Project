package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;


import edu.wpi.cs3733.d19.teamM.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class About {
    @FXML
    private Button logout;

    @FXML
    private ImageView back;

    @FXML
    private void navigateToHome() throws Exception{
        Main.setScene("home");
    }

    @FXML
    private void navigateToWelcome() throws Exception{
        Main.setScene("welcome");
    }
}
