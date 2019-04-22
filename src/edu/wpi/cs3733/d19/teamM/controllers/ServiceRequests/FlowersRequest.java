package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.*;
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

    String[] flowers = {"Alstroemeria","Amaryllis","Asiatic Lily","Aster","Azalea","Baby’s Breath","Begonia","Bird of Paradise"
            ,"Calla Lily","Campanula","Carnation","Chrysanthemum","Cockscomb","Crocus","Cyclamen","Cymbidium Orchid","Daffodil","Daisy","Delphinium","Dendrobium Orchid",
            "Freesia","Gardenia","Geranium","Gladiolus","Heather","Hibiscus","Hyacinth","Hydrangea","Hypericum","Iris","Jasmine","Jonquil","Kalanchoe","Larkspur","Lavender",
            "Daffodil","Liatris","Lilac","Lily","Limonium","Lisianthus","Magnolia","Marigold","Mini-Carnation","Narcissus","Orchid","Oriental Lily","Pansy","Peace Lily","Petite Rose",
            "Phalaenopsis Orchid","Poinsettia","Pompon","Protea","Ranunculus","Snapdragon","Solidago","Spray Rose","Star of Bethlehem","Stargazer Lily","Statice","Stock",
            "Tea Rose","Trachelium","Tuberose","Violet","Waxflower","Zinnia", "Dahlia","Tulip"};

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

    @FXML
    private Button colorfulBouquet;
    @FXML
    private Button earthyBouquet;
    @FXML
    private Button pinkBouquet;
    @FXML
    private Button roseBouquet;
    @FXML
    private Button sunflowerBouquet;

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
    public void handleFlowerBouquet(ActionEvent event) throws IOException{
        System.out.println("HANDLE FLOWER BUTTON");
        if(event.getSource() == colorfulBouquet) {
            flowerType.setText(colorfulBouquet.getText());
        } else if(event.getSource() == earthyBouquet) {
            flowerType.setText(earthyBouquet.getText());
        } else if(event.getSource() == pinkBouquet) {
            flowerType.setText(pinkBouquet.getText());
        } else if(event.getSource() == roseBouquet) {
            flowerType.setText(roseBouquet.getText());
        } else if(event.getSource() == sunflowerBouquet) {
            flowerType.setText(sunflowerBouquet.getText());
        }
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
        System.out.println("ROOM NUM: " + room.getText());

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

//        System.out.println("HERE");
//        room = new TextField();
//        room.setText(room.getText());
//        ObservableList<String> input = FXCollections.observableArrayList();
//        input.add(room.getText());
//        input.add(flowerType.getText());

        TextFields.bindAutoCompletion(flowerType,flowers);
    }
}
