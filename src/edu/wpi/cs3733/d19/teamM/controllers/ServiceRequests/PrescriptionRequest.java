package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;

public class PrescriptionRequest {

    @FXML
    private Text userText;

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
    private Text errorMessage;

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

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("prescriptions", room.getText(), fillId.getText(), notes.getText(), urgent.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean areFieldsEmpty() {
        return room.getText().isEmpty() || fillId.getText().isEmpty();
    }

    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @FXML
    private void initialize(){
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
    }
}
