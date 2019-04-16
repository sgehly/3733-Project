package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class PrescriptionRequestForm {
    //Text field for room location input
    @FXML
    private TextField room;

    @FXML
    private TextField type;

    @FXML
    private JFXCheckBox emergency;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Text userText;

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private JFXTextField requested;

    @FXML
    private JFXTextField timeSpent;

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
        Main.setScene("serviceRequestsList");
    }

    @FXML
    private void navigateToList() throws Exception {
        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            Main.setScene("serviceRequestsList");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean areFieldsEmpty() {
        return requested.getText().isEmpty() || timeSpent.getText().isEmpty();
    }


    @FXML
    void initialize(){

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        //userText.setText("");
    }

}

