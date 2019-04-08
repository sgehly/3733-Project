package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;

public class PrescriptionRequest {

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;

    //Tesxt Field for flower type input
    @FXML
    private TextField fillId;


    //Text field for room location input
    @FXML
    private TextField room;


    @FXML
    private javafx.scene.control.TextArea notes;


    @FXML
    private JFXCheckBox urgent;

    @FXML
    private Button submitReuqest;

    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("serviceRequests");
    }


    @FXML
    public void makePrescriptionReqeust() throws IOException {
        new ServiceRequests().makeRequest("prescriptions", room.getText(), fillId.getText(), notes.getText(), urgent.isSelected());

    }



    @FXML
    private void initialize(){
        new Clock(lblClock, lblDate);
    }
}
