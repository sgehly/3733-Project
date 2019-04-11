package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests.ServiceRequests;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ReligiousRequests {

    @FXML
    private Text userText;

    @FXML
    JFXTextField roomField;

    @FXML
    JFXTextField religion;

    @FXML
    JFXTextArea requestText;

    @FXML
    JFXCheckBox possession;

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Text errorMessage;

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
    private void initialize(){
        new Clock(lblClock, lblDate);
        //userText.setText(User.getUsername());
        userText.setText("");
    }

    @FXML
    private void goToServiceRequestsList() throws Exception {
        Main.setScene("serviceRequests");
    }
    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @FXML
    private void makeServiceRequest() throws Exception {

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("religion", roomField.getText(), religion.getText(), requestText.getText(), possession.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean areFieldsEmpty() {
        return roomField.getText().isEmpty() || religion.getText().isEmpty();
    }

}
