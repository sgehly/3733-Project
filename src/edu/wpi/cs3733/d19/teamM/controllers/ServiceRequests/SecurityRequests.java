package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    @FXML
    private Text errorMessage;

    @FXML
    private ListView listEmployees;

    String[] security = {"Intruder","Fight","Unruly Patient","Weapons Sighting","CODE ADAM","Lost Patient"};

    @FXML
    private javafx.scene.control.Button clrBtn;

    @FXML
    private javafx.scene.control.Button fight;
    @FXML
    private javafx.scene.control.Button intruder;
    @FXML
    private javafx.scene.control.Button lostPatient;
    @FXML
    private javafx.scene.control.Button unrulyPatient;
    @FXML
    private Button weapons;

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
    public void handleClear(ActionEvent event) {
        if(event.getSource() == clrBtn) {
            type.setText("");
        }
    }

    @FXML
    public void handleSecurityShortcuts(ActionEvent event) {
        if(event.getSource() == fight) {
            type.setText(fight.getText());
        } else if(event.getSource() == intruder) {
            type.setText(intruder.getText());
        } else if(event.getSource() == lostPatient) {
            type.setText(lostPatient.getText());
        } else if(event.getSource() == unrulyPatient) {
            type.setText(unrulyPatient.getText());
        } else if(event.getSource() == weapons) {
            type.setText(weapons.getText());
        }
    }

    public void makeSecurityRequest() throws IOException {

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("security", room.getText(), type.getText(), notes.getText(), emergency.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean areFieldsEmpty() {
        return room.getText().isEmpty() || type.getText().isEmpty();
    }

    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @FXML
    void initialize(){

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isSec = ?";
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        Connection conn = DBUtils.getConnection();
        try{
            PreparedStatement s = conn.prepareStatement(query);
            s.setInt(1, 1);
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

        TextFields.bindAutoCompletion(type, security);

    }

}
