package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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

public class FlowersRequest implements Initializable {

    @FXML
    private Text userText;

    @FXML
    private Text errorMessage;

    @FXML
    private ListView listEmployees;

    @FXML
    private javafx.scene.control.Label lblClock;

    @FXML
    private javafx.scene.control.Label lblDate;

    String[] flowers = {"Amaryllis","Azalea","Baby’s Breath","Bird of Paradise","Buttercup",
            "Carnation","Chrysanthemum","Daffodil","Dahlia","Daisy","Delphinium","Freesia","Gardenia",
            "Gladiolus","Hibiscus","Hyacinth","Hydrangea","Iris","Jasmine","Lavender","Lilac","Lily","Magnolia",
            "Marigold","Orchid","Pansy","Peony","Petunia","Poinsettia","Roses","Snapdragon",
            "Sunflower","Tulip","Violet","Waterlily"};

    //Tesxt Field for flower type input
    @FXML
    private TextField flowerType;

    @FXML
    private javafx.scene.control.Button btn;


    //Text field for room location input
    @FXML
    private TextField room;

    @FXML
    private JFXCheckBox replace;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Button submitReuqest;

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
    private void navigateToFlowers() throws Exception {
        Main.setScene("serviceRequests/flowers");
    }

    /**
     * This method allows the user to create a flowers request using the button
     * @param : The action that is associated with making the flowers request
     */
    @FXML
    public void setFlower(String flower) {
        flowerType.setText(flower);
    }

    @FXML
    public void makeFlowersRequest() throws IOException {
        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("flowers", room.getText(), flowerType.getText(), notes.getText(), replace.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean areFieldsEmpty() {
        return room.getText().isEmpty() || flowerType.getText().isEmpty();
    }

    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isFlor = ?";
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

        TextFields.bindAutoCompletion(flowerType,flowers);
    }
}
