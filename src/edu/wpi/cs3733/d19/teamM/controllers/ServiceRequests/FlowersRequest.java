package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import edu.wpi.cs3733.d19.teamM.Main;
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
import java.util.ResourceBundle;

public class FlowersRequest implements Initializable {


    String[] flowers = {"Roses","Dahlia","Tulip","Sun Flower"};


    //Tesxt Field for flower type input
    @FXML
    private TextField flowerType;


    //Text field for room location input
    @FXML
    private TextField room;


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
        System.out.println("----Test Connection----");

        try {
            Connection conn = new DatabaseUtils().getConnection();
            String query = "insert into SERVICEREQUEST  ( ROOM , TYPE , NOTES,SERVICE_TYPE ) values (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            System.out.println("Connected");

            stmt.setString(1,room.getText());
            System.out.println("1");
            stmt.setString(2, (flowerType.getText()));
            System.out.println("2");
            stmt.setString(3, notes.getText());
            System.out.println("3");
            stmt.setString(4, "Flowers");
            System.out.println("4");
            stmt.executeUpdate();
            stmt.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TextFields.bindAutoCompletion(flowerType,flowers);
    }
}
