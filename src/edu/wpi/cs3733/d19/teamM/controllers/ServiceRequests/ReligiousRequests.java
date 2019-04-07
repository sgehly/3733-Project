package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests.ServiceRequests;
import javafx.fxml.FXML;

public class ReligiousRequests {

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */

    @FXML
    private JFXTextField roomField;

    @FXML
    private JFXTextField religion;

    @FXML
    private JFXTextArea requestText;

    @FXML
    private JFXCheckBox possession;

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
        new ServiceRequests().makeRequest("religion", roomField.getText(), religion.getText(), requestText.getText(), possession.isSelected());
    }

}
