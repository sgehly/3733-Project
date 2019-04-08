package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
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





    public int RandIDgenerator(){
        Random rand = new Random();
        int id = rand.nextInt(10000);
        try {
            String query = "SELECT REQUESTID FROM SERVICEREQUEST";
            Connection conn = new DatabaseUtils().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                if(rs.getInt(1) == id){
                    RandIDgenerator();
                }
                else{
                    return id;
                }
            }
        }


        catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }



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
        new ServiceRequests().makeRequest("flowers", room.getText(), flowerType.getText(), notes.getText(), replace.isSelected());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new Clock(lblClock, lblDate);

        TextFields.bindAutoCompletion(flowerType,flowers);
    }
}
