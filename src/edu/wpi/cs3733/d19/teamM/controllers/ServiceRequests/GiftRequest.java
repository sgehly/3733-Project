package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GiftRequest {

    @FXML
    JFXTextField giftTypes;

    @FXML
    JFXTextField room;

    @FXML
    JFXTextArea requestText;

    @FXML
    Label lblClock;

    @FXML
    Label lblDate;

    @FXML
    private JFXCheckBox packaged;
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
    public void makeGiftRequest() throws IOException {
        new ServiceRequests().makeRequest("gift", room.getText(), giftTypes.getText(), requestText.getText(), packaged.isSelected());

    }

    @FXML
    void initialize()
    {
        new Clock(lblClock,lblDate); //initialize the clock
        //Create the listener for the text with a few options
        ArrayList<String> giftOptions = new ArrayList<String>();//ArrayList with options
        giftOptions.add("Gift Cards");
        giftOptions.add("Teddy Bears");
        giftOptions.add("Chocolates");
        giftOptions.add("Photo Frames");
        giftOptions.add("Balloons");

        giftTypes.textProperty().addListener((ov, oldValue, newValue) ->
        {
            TextFields.bindAutoCompletion(giftTypes,giftOptions);

        });
    }

}
