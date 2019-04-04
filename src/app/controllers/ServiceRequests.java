package app.controllers;

import app.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.sql.*;
import java.awt.event.MouseEvent;
import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import java.io.IOException;

/**
 * The controller class associated with creating service requests
 */
public class ServiceRequests {

    //The path to the databse that we need to use
    String dbPath = "jdbc:derby:myDB";
    int idgnerator = 1;

    //Scene setup from service requests

    //Create all objects and FXML objects that are needed
    Date date = new Date();

    //initializing the timestamp
    long time = date.getTime();
    Timestamp ts = new Timestamp(time);

    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane sanitation;
    @FXML
    private TitledPane language;



    @FXML
    private Button logoutButton;

    @FXML
    private TextArea sanitationRoomNumber;

    @FXML
    private Button sanitationRequestButton;

    @FXML
    private TextArea sanitationNotes;

    @FXML
    private TextArea languageRoomNumber;

    @FXML
    private ChoiceBox<?> languageSelection;

    @FXML
    private Button languageRequestButton;

    @FXML
    private TextArea languageNotes;


    public int RandIDgenerator(){
        Random rand = new Random();
        int id = rand.nextInt(10000);
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            String query = "SELECT REQUESTID FROM REQUESTINPROGRESS";
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
    private void navigateToHome() throws Exception {
        Main.setScene("home");
    }

    /**
     * This method checks room availability after a button is clicked
     * @param event
     */
    @FXML
    void checkRoomValidity(MouseEvent event) {

    }


    /**
     * This method send teh user to the service request list page
     * @throws IOException: Any input/output errors that occur
     */
    @FXML
    public void goToRequestList() throws IOException {
        Main.setScene("serviceRequestList");
    }

    @FXML
    public void makeSanitationRequest() throws IOException {
            System.out.println("----Test Connection----");

            try {

                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                Connection conn = DriverManager.getConnection(dbPath);
                String query = "insert into  APP.REQUESTINPROGRESS  (REQUESTID , ROOM , NOTE , DATE ) values (?,?,?,?)";
                //
                //PreparedStatement stmt=conn.prepareStatement("insert into  REQUESTINPROGRESS set REQUESTID = ? , ROOM = ?, NOTE = ?, DATE = ?, STATUS = ?");
                //PreparedStatement stmt = conn.prepareStatement("SELECT * from REQUESTINPROGRESS");
                PreparedStatement stmt = conn.prepareStatement(query);

                stmt.setInt(1, (RandIDgenerator()));
                stmt.setString(2, (sanitationRoomNumber.getText()));
                stmt.setString(3, sanitationNotes.getText());
                stmt.setTimestamp(4, ts);
                //stmt.setBoolean(5, false);
                System.out.println("in table ");

                stmt.executeUpdate();
                stmt.close();

                this.goToRequestList();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    /**
     * This method allows the user to create a language request using the button
     * @param e: The action that is associated with making the language request
     */

    @FXML
    void makeLanguageRequest(ActionEvent e){
    }


    /**
     * This method initializes the ServiceRequest Controller class and deals with the associated assert statements
     */
    @FXML// This method is called by the FXMLLoader when initialization is complete
    void initialize(){
            accordion.setExpandedPane(sanitation);
            //general asserts
            assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            //language asserts
            assert languageRoomNumber != null : "fx:id=\"roomNumber\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageSelection != null : "fx:id=\"languageSelection\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageRequestButton != null : "fx:id=\"requestButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageNotes != null : "fx:id=\"notesText\" was not injected: check your FXML file 'serviceRequests.fxml'.";
    }
}

