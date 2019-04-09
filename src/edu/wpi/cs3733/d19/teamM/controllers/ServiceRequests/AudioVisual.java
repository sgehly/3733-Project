package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.controlsfx.control.textfield.TextFields;


import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.ResourceBundle;

public class AudioVisual implements Initializable {


    String[] audioVis = {"Headphones","Speakers","Radio","TV","Camera"};


    //Tesxt Field for audio/visual type input
    @FXML
    private TextField audioVisType;

    //Text field for room location input
    @FXML
    private TextField room;

    @FXML
    private JFXCheckBox pickUp;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Button submitReuqest;

    @FXML
    private Label lblClock;

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

    /**
     * This method allows the user to create a flowers request using the button
     * @param : The action that is associated with making the flowers request
     */

    @FXML
    public void makeAudioVisRequest() throws IOException {
        new ServiceRequests().makeRequest("av", room.getText(), audioVisType.getText(), notes.getText(), pickUp.isSelected());

    }
    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TextFields.bindAutoCompletion(audioVisType,audioVis);

        Clock clock = new Clock(lblClock, lblDate);
    }
}
