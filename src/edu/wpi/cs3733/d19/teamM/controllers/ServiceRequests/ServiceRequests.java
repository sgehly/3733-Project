package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import giftRequest.GiftRequest;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import java.sql.*;
import java.awt.event.MouseEvent;
import java.lang.String;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import java.io.IOException;

/**
 * The controller class associated with creating service requests
 */
public class ServiceRequests {

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    private int hrs;
    private int mins;
    private int secs;

    //The path to the databse that we need to use
    int idgnerator = 1;

    //Scene setup from service requests

    //Create all objects and FXML objects that are needed
    Date date = new Date();

    //initializing the timestamp
    long time = date.getTime();
    Timestamp ts = new Timestamp(time);

    @FXML
    private Text userText;

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
    private ChoiceBox<String> languageSelection;

    @FXML
    private Button languageRequestButton;

    @FXML
    private TextArea languageNotes;

    public int RandIDgenerator(){
        Random rand = new Random();
        int id = rand.nextInt(10000);
        try {
            String query = "SELECT REQUESTID FROM REQUESTINPROGRESS";
            DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
            Connection conn = DBUtils.getConnection();
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
        Main.logOut();
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
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateToReligious() throws Exception {
        Main.setScene("serviceRequests/religiousRequests");
    }

    /**
     * This method send teh user to the service request list page
     * @throws IOException: Any input/output errors that occur
     */
    @FXML
    private void navigateToSecurity() throws Exception{
        Main.setScene("serviceRequests/securityRequest");
    }

    @FXML
    private void  navigateToLab(){
        Main.setScene("serviceRequests/labTestRequest");
    }

    /**
     * This method navigates to the gift page when clicked
     * @throws Exception
     */
    @FXML
    private void navigateToGifts() throws Exception
    {
        Main.setScene("serviceRequests/giftRequest");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateToFLowers() throws Exception{
        Main.setScene("serviceRequests/flowersRequest");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateToSanitaton() throws Exception{
        Main.setScene("serviceRequests/sanitationRequest");
    }

    @FXML
    private void navigateToIT() throws Exception{
        Main.setScene("serviceRequests/ITRequests");
    }

    @FXML
    private void navigateToAudioVis() throws Exception{
        Main.setScene("serviceRequests/audiovisual");
    }

    @FXML
    private void naviagateToExternal() throws Exception{
        Main.setScene("serviceRequests/ExtTransport");
    }

    @FXML
    private void navigateToIntTransport() throws Exception{
        Main.setScene("serviceRequests/IntTransport");
    }
    @FXML
    private void navigateToPrescription() throws Exception{
        Main.setScene("serviceRequests/prescriptionsRequest");
    }
    @FXML
    private void navigateToLanguage() throws Exception{
        Main.setScene("serviceRequests/languageRequest");
    }

    @FXML
    private void navigateToLGift() throws Exception {
        GiftRequest e = new GiftRequest();
            try {
                e.run(0, 0, 1000, 1000, "/resources/StylesheetAPI.css", "what", "what");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

//    @FXML
//    private void toPizza(){
//        try{
//            edu.wpi.cs3733.d19.teamO.request.Request pizza = new edu.wpi.cs3733.d19.teamO.request.Request();
//            pizza.run(0,0,1500,1500,"/resources/stylesheet.css", "", "");
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void toElevator(){
//        try{
//            InternalTransportRequestApi i = new InternalTransportRequestApi();
//            i.run(0,0,1500,1500,"/resources/stylesheetAPI.css","","AELEV00S01",true,"m");
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }

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
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    public void makeRequest(String type, String room, String subtype, String description, boolean checkbox){
        System.out.println("Trying to make request");
        try {
            DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
            Connection conn = DBUtils.getConnection();
            String query = "insert into APP.REQUESTINPROGRESS  (REQUESTID, TYPE, ROOM, SUBTYPE, DESCRIPTION, DATE, CHECKBOX) values (?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, (RandIDgenerator()));
            stmt.setString(2, type);
            stmt.setString(3, room);
            stmt.setString(4, subtype);
            stmt.setString(5, description);
            stmt.setTimestamp(6, ts);
            stmt.setInt(7, checkbox ? 1 : 0);

            stmt.executeUpdate();
            stmt.close();
            conn.close();

            //this.goToList();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method initializes the ServiceRequest Controller class and deals with the associated assert statements
     */
    @FXML// This method is called by the FXMLLoader when initialization is complete
    void initialize(){
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        //userText.setText("");
            //general asserts
            assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            //language asserts
            assert languageRoomNumber != null : "fx:id=\"roomNumber\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageSelection != null : "fx:id=\"languageSelection\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageRequestButton != null : "fx:id=\"requestButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageNotes != null : "fx:id=\"notesText\" was not injected: check your FXML file 'serviceRequests.fxml'.";
    }
}

