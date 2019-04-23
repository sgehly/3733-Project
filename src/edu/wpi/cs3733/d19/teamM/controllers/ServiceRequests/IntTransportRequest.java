package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests.ServiceRequests;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    private ListView listEmployees;

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Button clrBtn;

    @FXML
    private Button crutches;
    @FXML
    private Button moveBeds;
    @FXML
    private Button stretcher;
    @FXML
    private Button walker;
    @FXML
    private Button wheelchair;

    String[] intTrans = {"Stretcher","Wheelchair","Rascal Scooter","Moveable Beds","Hospital Trolley","Walker","Crutches","Cane"};

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
    public void handleClear(ActionEvent event) {
        if(event.getSource() == clrBtn) {
            modeTransport.setText("");
        }
    }

    @FXML
    public void handleIntTransShortcuts(ActionEvent event) {
        if(event.getSource() == crutches) {
            modeTransport.setText(crutches.getText());
        } else if(event.getSource() == moveBeds) {
            modeTransport.setText(moveBeds.getText());
        } else if(event.getSource() == stretcher) {
            modeTransport.setText(stretcher.getText());
        } else if(event.getSource() == walker) {
            modeTransport.setText(walker.getText());
        } else if(event.getSource() == wheelchair) {
            modeTransport.setText(wheelchair.getText());
        }
    }

    @FXML
    private void makeInternalServiceRequest() throws Exception {

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
        userText.setText(User.getUsername());

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isInt = ?";
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

        TextFields.bindAutoCompletion(modeTransport, intTrans);

    }
}
