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


    String[] flowers = {"Roses","Dahlia","Tulip","Sun Flower","Alstroemeria","Amaryllis","Asiatic Lily","Aster","Azalea","Baby’s Breath","Begonia","Bird of Paradise"
            ,"Calla Lily","Campanula","Carnation","Chrysanthemum","Cockscomb","Crocus","Cyclamen","Cymbidium Orchid","Daffodil","Daisy","Delphinium","Dendrobium Orchid",
            "Freesia","Gardenia","Geranium","Gladiolus","Heather","Hibiscus","Hyacinth","Hydrangea","Hypericum","Iris","Jasmine","Jonquil","Kalanchoe","Larkspur","Lavender",
            "Daffodil","Liatris","Lilac","Lily","Limonium","Lisianthus","Magnolia","Marigold","Mini-Carnation","Narcissus","Orchid","Oriental Lily","Pansy","Peace Lily","Petite Rose",
            "Phalaenopsis Orchid","Poinsettia","Pompon","Protea","Ranunculus","Snapdragon","Solidago","Spray Rose","Star of Bethlehem","Stargazer Lily","Statice","Stock",
            "Tea Rose","Trachelium","Tuberose","Violet","Waxflower","Zinnia"};


    //Tesxt Field for flower type input
    @FXML
    private TextField flowerType;


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

    /**
     * This method allows the user to create a flowers request using the button
     * @param : The action that is associated with making the flowers request
     */

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

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where ACCOUNTINT = ?";
        Connection conn = new DatabaseUtils().getConnection();
        try{
            PreparedStatement s = conn.prepareStatement(query);
            s.setInt(1, 5);
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

        userText.setText(User.getUsername());
        //userText.setText("");
        TextFields.bindAutoCompletion(flowerType,flowers);
    }
}
