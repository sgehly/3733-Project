package app.controllers;

import app.Main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class ServiceRequests {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="logoutButton"
    private Button logoutButton; // Value injected by FXMLLoader

    @FXML // fx:id="roomNumber"
    private TextArea roomNumber; // Value injected by FXMLLoader

    @FXML // fx:id="languageSelection"
    private ChoiceBox<?> languageSelection; // Value injected by FXMLLoader

    @FXML // fx:id="requestButton"
    private Button requestButton; // Value injected by FXMLLoader

    @FXML // fx:id="notesText"
    private TextArea notesText; // Value injected by FXMLLoader

    @FXML
    public void logout() throws Exception {
        Parent pane = FXMLLoader.load(Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }
    @FXML
    private void navigateToHome() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    void makeRequest(ActionEvent event) {
        //to be implemented later on
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert roomNumber != null : "fx:id=\"roomNumber\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert languageSelection != null : "fx:id=\"languageSelection\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert requestButton != null : "fx:id=\"requestButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert notesText != null : "fx:id=\"notesText\" was not injected: check your FXML file 'serviceRequests.fxml'.";

    }
}
