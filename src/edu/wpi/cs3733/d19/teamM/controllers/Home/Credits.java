package edu.wpi.cs3733.d19.teamM.controllers.Home;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Credits {

    @FXML
    private Button logout;

    @FXML
    private ImageView back;

    @FXML
    private Text userText;

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private void navigateToHome() throws Exception{
        Main.setScene("home");
    }

    @FXML
    private void navigateToWelcome() throws Exception{
        Main.setScene("welcome");
    }

    @FXML
    void initialize() {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
    }
}
