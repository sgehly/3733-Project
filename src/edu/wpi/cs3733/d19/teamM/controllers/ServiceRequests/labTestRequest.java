package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ResourceBundle;

public class labTestRequest implements Initializable {


    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */

    @FXML
    private JFXTextField roomField;

    @FXML
    private JFXTextField testType;

    @FXML
    private JFXTextArea requestText;

    @FXML
    private JFXCheckBox urgent;

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
        new ServiceRequests().makeRequest("laboratory", roomField.getText(), testType.getText(), requestText.getText(), urgent.isSelected());
    }
    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        new Clock(lblClock, lblDate);

    }

}
