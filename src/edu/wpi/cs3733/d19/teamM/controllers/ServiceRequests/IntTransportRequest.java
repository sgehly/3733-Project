package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests.ServiceRequests;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class IntTransportRequest {

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */


    @FXML
    private Text userText;

    @FXML
    private JFXTextField roomField;

    @FXML
    private JFXTextField modeTransport;

    @FXML
    private JFXTextArea requestText;

    @FXML
    private JFXCheckBox urgent;

    @FXML
    private Text errorMessage;

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;

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
    private void goToServiceRequestsList() throws Exception {
        Main.setScene("serviceRequests");
    }

    @FXML
    private void makeServiceRequest() throws Exception {

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("internal", roomField.getText(), modeTransport.getText(), requestText.getText(), urgent.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean areFieldsEmpty() {
        return roomField.getText().isEmpty() || modeTransport.getText().isEmpty();
    }

    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }
    @FXML
    private void initialize(){
        new Clock(lblClock, lblDate);
        //userText.setText(User.getUsername());
        userText.setText("");
    }



}
