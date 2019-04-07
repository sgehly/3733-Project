package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import edu.wpi.cs3733.d19.teamM.Main;

import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    ObservableList<String> languages = FXCollections.observableArrayList("Acholi",
            "Afrikaans", "Akan", "Albanian","Amharic","Arabic", "Ashante", "Asl", "Assyrian", "Azerbaijani", "Azeri", "Bajuni", "Basque",
            "Behdini", "Belorussian", "Bengali", "Berber", "Bosnian", "Bravanese", "Bulgarian","Burmese", "Cakchiquel", "Cambodian", "Cantonese","Catalan",
            "Chaldean", "Chamorro", "Chao-chow", "Chavacano", "Chin", "Chuukese", "Cree", "Croatian", "Czech", "Dakota", "Danish", "Dari", "Dinka",
            "Diula", "Dutch", "Edo", "English", "Estonian", "Ewe", "Fante", "Farsi", "Fijian Hindi", "Finnish", "Flemish", "French", "French Canadian", "Fukienese",
            "Fula", "Fulani", "Fuzhou", "Ga", "Gaddang", "Gaelic", "Gaelic-irish", "Gaelic-scottish", "Georgian", "German", "Gorani", "Greek", "Gujarati",
            "Haitian Creole", "Hakka", "Hakka-chinese", "Hausa", "Hebrew", "Hindi", "Hmong", "Hungarian", "Ibanag", "Ibo", "Icelandic", "Igbo", "Ilocano",
            "Indonesian", "Inuktitut", "Italian", "Jakartanese", "Japanese", "Javanese", "Kanjobal", "Karen", "Karenni", "Kashmiri", "Kazakh", "Kikuyu",
            "Kinyarwanda", "Kirundi", "Korean", "Kosovan", "Kotokoli", "Krio", "Kurdish", "Kurmanji", "Kyrgyz", "Lakota", "Laotian",
            "Latvian", "Lingala", "Lithuanian", "Luganda", "Luo", "Maay", "Macedonian", "Malay", "Malayalam", "Maltese", "Mandarin", "Mandingo", "Mandinka",
            "Marathi", "Marshallese", "Mien", "Mina", "Mirpuri", "Moldavan", "Mongolian", "Montenegrin", "Navajo", "Neapolitan", "Nepali", "Nigerian Pidgin",
            "Norwegian", "Oromo", "Pahari", "Papago", "Papiamento", "Pashto", "Patois", "Pidgin English", "Polish", "Portug.creole", "Portuguese", "Pothwari",
            "Pulaar", "Punjabi", "Putian", "Quichua", "Romanian", "Russian", "Samoan", "Serbian", "Shanghainese", "Shona", "Sichuan", "Sicilian", "Sinhalese", "Slovak",
            "Somali", "Sorani", "Spanish", "Sudanese Arabic", "Sundanese", "Susu", "Swahili", "Swedish", "Sylhetti", "Tagalog", "Taiwanese", "Tajik", "Tamil",
            "Telugu", "Thai", "Tibetan", "Tigre", "Tigrinya", "Toishanese", "Tongan", "Toucouleur", "Trique", "Tshiluba", "Turkish", "Twi", "Ukrainian", "Urdu", "Uyghur", "Uzbek",
            "Vietnamese", "Visayan", "Welsh", "Wolof", "Yiddish", "Yoruba", "Yupik");

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
        Main.setScene("serviceRequests/securityRequests");
    }

    @FXML
    private void  navigateToInterpretter(){
        Main.setScene("serviceRequests/interpretterRequests");
    }

    /**
     * This method send teh user to the service request list page
     * @throws IOException: Any input/output errors that occur
     */
    @FXML
    public void goToRequestList() throws IOException {
        Main.setScene("serviceRequestList");
    }

    public void makeRequest(String type, String room, String subtype, String description, boolean checkbox){
        try {
            Connection conn = new DatabaseUtils().getConnection();
            String query = "insert into APP.REQUESTINPROGRESS  (REQUESTID, TYPE, ROOM, SUBTYPE, NOTE , DATE, CHECKBOX) values (?,?,?,?,?,?,?)";
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

            this.goToRequestList();

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
    }
}

