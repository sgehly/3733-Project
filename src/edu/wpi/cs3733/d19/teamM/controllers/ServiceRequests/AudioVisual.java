package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import edu.wpi.cs3733.d19.teamM.Main;

import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

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
public class AudioVisual {

    ObservableList list = FXCollections.observableArrayList();
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
    private Button logoutButton;

    @FXML
    private TextArea aVRoomNumber;

    @FXML
    private Button aVRequestButton;

    @FXML
    private TextArea aVNotes;

    @FXML
    private ChoiceBox<?> aVSelection;

    public int RandIDgenerator(){
        Random rand = new Random();
        int id = rand.nextInt(10000);
        try {
            String query = "SELECT REQUESTID FROM REQUESTINPROGRESS";
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
    private void navigateToHome() throws Exception {
        Main.setScene("home");
    }

    /**
     * This method send the user to the service request list page
     * @throws IOException: Any input/output errors that occur
     */
    @FXML
    public void goToRequestList() throws IOException {
        Main.setScene("serviceRequestList");
    }

    @FXML
    public void makeAVRequest() throws IOException {
        System.out.println("----Test Connection----");

        try {
            Connection conn = new DatabaseUtils().getConnection();
            String query = "insert into APP.REQUESTINPROGRESS  (REQUESTID , ROOM , NOTE , DATE ) values (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setInt(1, (RandIDgenerator()));
            stmt.setString(2, (aVRoomNumber.getText()));
            stmt.setString(3, aVNotes.getText());
            stmt.setTimestamp(4, ts);
            stmt.executeUpdate();
            stmt.close();

            this.goToRequestList();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds items to the choice box for the audio-visual selections available for patients
     */
    private void loadData() {
        list.removeAll(list);
        String audio1 = "Headphones";
        String audio2 = "Speakers";
        String audio3 = "Radio";
        String video1 = "TV";
        String video2 = "Camera";
        list.addAll(audio1, audio2, audio3, video1, video2);

        aVSelection.getItems().addAll(list);
    }

    /**
     * This method initializes the ServiceRequest Controller class and deals with the associated assert statements
     */
    @FXML// This method is called by the FXMLLoader when initialization is complete
    void initialize(){

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            secs = LocalTime.now().getSecond();
            mins = LocalTime.now().getMinute();
            hrs = LocalTime.now().getHour();

            lblClock.setText(hrs + ":" + (mins) + ":" + secs);
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        lblDate.setText(date.format(d));

        loadData();

        //general asserts
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        //language asserts
        assert aVRoomNumber != null : "fx:id=\"roomNumber\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert aVSelection != null : "fx:id=\"languageSelection\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert aVRequestButton != null : "fx:id=\"requestButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert aVNotes != null : "fx:id=\"notesText\" was not injected: check your FXML file 'serviceRequests.fxml'.";
    }
}

