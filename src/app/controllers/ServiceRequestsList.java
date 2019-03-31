/**
 * Sample Skeleton for 'serviceRequests.fxml' Controller Class
 */

package app.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class ServiceRequestsList {


    String dbPath = "jdbc:derby:myDB";

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="back"
    private ImageView back; // Value injected by FXMLLoader

    @FXML // fx:id="logoutButton"
    private Button logoutButton; // Value injected by FXMLLoader

    @FXML // fx:id="requestFulfilledButton"
    private Button requestFulfilledButton; // Value injected by FXMLLoader

    @FXML
    private Button refresh;

    @FXML // fx:id="deleteIncompleteButton"
    private Button deleteIncompleteButton; // Value injected by FXMLLoader

    @FXML // fx:id="deleteCompletedButton"
    private Button deleteCompletedButton; // Value injected by FXMLLoader

    @FXML
    private TableView requestInProgress = new TableView();

    @FXML
     private TableColumn<DisplayTable,String> Room = new TableColumn("room");

    @FXML
    private TableColumn<DisplayTable,String> Notes = new TableColumn("notes");


    @FXML
    private TableColumn<DisplayTable,String> Type = new TableColumn("type");


    @FXML
    void deleteRequest(MouseEvent event) {
        //delete a request
    }

    @FXML
    void goToMakeServiceRequest(MouseEvent event) throws IOException {
        Parent pane = FXMLLoader.load(app.Main.getFXMLURL("serviceRequests"));
        Scene scene = new Scene(pane);
        app.Main.getStage().setScene(scene);
    }

    @FXML
    private void navigateToHome() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    public void logout() throws Exception{
        Parent pane = FXMLLoader.load(app.Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        app.Main.getStage().setScene(scene);
    }

    @FXML
    void markAsComplete(MouseEvent event) {
        //move to complete table
    }


    private static ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws SQLException {
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("room"));
                ent.setNotes(rs.getString("note"));
                ent.setType(rs.getString("type"));
                entList.add(ent);
            }
            return entList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }


    public ObservableList<DisplayTable> getAllRecords() throws ClassNotFoundException, SQLException {
        String query = "SELECT * FROM APP.REQUESTINPROGRESS";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);

            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        try {

            ObservableList<DisplayTable> entList = getAllRecords();
            Room.setCellValueFactory(new PropertyValueFactory<>("room"));
            Notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
            Type.setCellValueFactory(new PropertyValueFactory<>("type"));
            requestInProgress.setItems(entList);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert requestFulfilledButton != null : "fx:id=\"requestFulfilledButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert deleteIncompleteButton != null : "fx:id=\"deleteIncompleteButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert deleteCompletedButton != null : "fx:id=\"deleteCompletedButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";

    }


}
