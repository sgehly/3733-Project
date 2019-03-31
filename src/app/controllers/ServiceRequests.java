package app.controllers;

import app.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import java.sql.*;
import java.awt.event.MouseEvent;
import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;


import java.io.IOException;

public class ServiceRequests {

    String dbPath = "jdbc:derby:myDB";
    int idgnerator = 1;


    Date date = new Date();

    long time = date.getTime();

    Timestamp ts = new Timestamp(time);


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

    @FXML
    public void logout() throws Exception {
        Parent pane = FXMLLoader.load(Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    private void navigateToHome() throws Exception {
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    void checkRoomValidity(MouseEvent event) {

    }


    @FXML
    public void goToRequestList() throws IOException {
        Parent pane = FXMLLoader.load(Main.getFXMLURL("serviceRequestsList"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    public void makeSanitationRequest() throws IOException {
            System.out.println("----Test Connection----");

            try {

                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                Connection conn = DriverManager.getConnection(dbPath);
                String query = "insert into  APP.REQUESTINPROGRESS  (REQUESTID , ROOM , NOTE , DATE, STATUS ) values (?,?,?,?,?)";
                //
                //PreparedStatement stmt=conn.prepareStatement("insert into  REQUESTINPROGRESS set REQUESTID = ? , ROOM = ?, NOTE = ?, DATE = ?, STATUS = ?");
                //PreparedStatement stmt = conn.prepareStatement("SELECT * from REQUESTINPROGRESS");
                PreparedStatement stmt = conn.prepareStatement(query);

                stmt.setInt(1, (idgnerator));
                stmt.setString(2, (sanitationRoomNumber.getText()));
                stmt.setString(3, sanitationNotes.getText());
                stmt.setTimestamp(4, ts);
                stmt.setBoolean(5, false);
                System.out.println("in table ");

                stmt.executeUpdate();
                stmt.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            idgnerator = idgnerator++;
            System.out.println("id generator ");
        }


        @FXML
        void makeLanguageRequest(ActionEvent e){
        }


        @FXML// This method is called by the FXMLLoader when initialization is complete
        void initialize(){
            //general asserts
            assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            //language asserts
            assert languageRoomNumber != null : "fx:id=\"roomNumber\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageSelection != null : "fx:id=\"languageSelection\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageRequestButton != null : "fx:id=\"requestButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
            assert languageNotes != null : "fx:id=\"notesText\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        }
    }

