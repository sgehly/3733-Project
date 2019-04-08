package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ITRequests implements Initializable {

    @FXML
    private TextField typeOfEquipment;

    //Text field for room location input
    @FXML
    private TextField room;

    @FXML
    private JFXCheckBox urgent;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Button submitReuqest;

    @FXML
    private javafx.scene.control.Label lblClock;

    @FXML
    private javafx.scene.control.Label lblDate;

    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("serviceRequests");
    }

    @FXML
    public void makeItRequest() throws IOException {
        new ServiceRequests().makeRequest("it", room.getText(), typeOfEquipment.getText(), notes.getText(), urgent.isSelected());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Clock clock = new Clock(lblClock, lblDate);
    }
}
