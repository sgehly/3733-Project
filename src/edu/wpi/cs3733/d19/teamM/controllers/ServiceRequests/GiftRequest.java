package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private Text userText;

    @FXML
    private JFXCheckBox packaged;

    @FXML
    private Text errorMessage;

    @FXML
    private ListView listEmployees;

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

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("gift", room.getText(), giftTypes.getText(), requestText.getText(), packaged.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean areFieldsEmpty() {
        return room.getText().isEmpty() || giftTypes.getText().isEmpty();
    }

    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @FXML
    void initialize()
    {
        new Clock(lblClock,lblDate); //initialize the clock
        //Create the listener for the text with a few options

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where ACCOUNTINT = ?";
        Connection conn = new DatabaseUtils().getConnection();
        try{
            PreparedStatement s = conn.prepareStatement(query);
            s.setInt(1, 6);
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                list.add(rs.getString(1));
                System.out.println(rs.getString(1));
            }
            for(String s1 : list){
                listEmployees.getItems().add(s1);
            }
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

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

        userText.setText(User.getUsername());
        //userText.setText("");
    }

}
