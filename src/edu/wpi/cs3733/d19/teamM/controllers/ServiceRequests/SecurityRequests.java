package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SecurityRequests {


    //Text field for room location input
    @FXML
    private Text userText;

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

    public void makeSecurityRequest() throws IOException {
        new ServiceRequests().makeRequest("security", room.getText(), type.getText(), notes.getText(), emergency.isSelected());
    }
    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @FXML
    void initialize(){
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
    }

}
