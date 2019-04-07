package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javax.swing.*;
import java.awt.*;

public class SecurityRequests {

    @FXML
    JFXRadioButton low;

    @FXML
    JFXRadioButton medium;

    @FXML
    JFXRadioButton high;

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;



    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("serviceRequests");
    }

    @FXML
    void initialize(){
        new Clock(lblClock, lblDate);
    }
}
