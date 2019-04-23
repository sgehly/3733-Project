package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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

public class AudioVisual implements Initializable {


    String[] audioVis = {"Headphones","Speakers","Radio","TV","Camera","Microphone","CD","DVD","DVD Player","Projector","Video Camera"};

    @FXML
    private ListView listEmployees;

    @FXML
    private Text errorMessage;

    //Tesxt Field for audio/visual type input
    @FXML
    private TextField audioVisType;

    @FXML
    private Text userText;

    //Text field for room location input
    @FXML
    private TextField room;

    @FXML
    private JFXCheckBox pickUp;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Button submitReuqest;

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private javafx.scene.control.Button clrBtn;

    @FXML
    private javafx.scene.control.Button camera;
    @FXML
    private javafx.scene.control.Button earbuds;
    @FXML
    private javafx.scene.control.Button projector;
    @FXML
    private javafx.scene.control.Button speakers;
    @FXML
    private javafx.scene.control.Button tv;


    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     *
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     *
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("serviceRequests");
    }

    @FXML
    public void handleClear(ActionEvent event) {
        if(event.getSource() == clrBtn) {
            audioVisType.setText("");
        }
    }

    @FXML
    public void handleAVShortcuts(ActionEvent event) {
        if(event.getSource() == camera) {
            audioVisType.setText(camera.getText());
        } else if(event.getSource() == earbuds) {
            audioVisType.setText(earbuds.getText());
        } else if(event.getSource() == projector) {
            audioVisType.setText(projector.getText());
        } else if(event.getSource() == speakers) {
            audioVisType.setText(speakers.getText());
        } else if(event.getSource() == tv) {
            audioVisType.setText(tv.getText());
        }
    }

    /**
     * This method allows the user to create a flowers request using the button
     *
     * @param : The action that is associated with making the flowers request
     */
    @FXML
    public void makeAudioVisRequest() throws IOException {
        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("av", room.getText(), audioVisType.getText(), notes.getText(), pickUp.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean areFieldsEmpty() {
        return audioVisType.getText().isEmpty() || room.getText().isEmpty();
    }

    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Clock clock = new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isAV = ?";
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

        TextFields.bindAutoCompletion(audioVisType, audioVis);

    }
}
