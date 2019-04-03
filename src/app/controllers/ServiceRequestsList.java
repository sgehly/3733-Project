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

/**
 * This class is the controller for all the ServiceRequestPage related UI elements
 */
public class ServiceRequestsList {

    //The relative path to the database for access
    String dbPath = "jdbc:derby:myDB";

    //All the different objects that have to be created for the page fxid are all the same name as the instance name

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

    @FXML // fx:id="requestInProgress"
    private TableView requestInProgress = new TableView(); // Value injected by FXMLLoader

    @FXML
     private TableColumn<DisplayTable,String> Room = new TableColumn("room");

    @FXML
    private TableColumn<DisplayTable,String> Notes = new TableColumn("notes");


    @FXML
    private TableColumn<DisplayTable,String> Type = new TableColumn("type");


    /**
     * Default method to delete a request
     * @param event: based on mouse input
     */
    @FXML
    void deleteRequest(MouseEvent event) {
        //delete a request
    }

    /**
     * The method that helps create the service request
     * @param event: Takes in the event of clicking the button
     * @throws IOException: Any errors that occur with the selection or other aspects
     */
    @FXML
    void goToMakeServiceRequest(MouseEvent event) throws IOException {
        //Get the screen, create a scene of it, and set it as the primary stage to display
        Parent pane = FXMLLoader.load(app.Main.getFXMLURL("serviceRequests"));
        Scene scene = new Scene(pane);
        app.Main.getStage().setScene(scene);
    }

    /**
     * This method is linked to the button that allows the individuals to return to the home screen
     * @throws Exception: Any exception that arises as the person tries to go back to the screen
     */
    @FXML
    private void navigateToHome() throws Exception{
        //Get the screen, create a scene of it, and set it as the primary stage to display
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    /**
     * This method is linked to the button that allows the individuals to return to the welcome screen
     * @throws Exception: Any exception that arises as the person tries to go back to the screen
     */
    @FXML
    public void logout() throws Exception{
        //Get the screen, create a scene of it, and set it as the primary stage to display
        Parent pane = FXMLLoader.load(app.Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        app.Main.getStage().setScene(scene);
    }

    /**
     * Method that is linked to button that allows individual to mark their service request as completed
     * @param event
     */
    @FXML
    void markAsComplete(MouseEvent event) {
        //move to complete table
    }

    /**
     * This method gets the current objects from the Database and returns it as an Observable List
     * @param rs: The corresponding result set
     * @return ObservableList<DisplayTable>: A list with displayable tables to view in the UI the database entries for the service requests
     * @throws SQLException: Any SQL errors that might occur while trying to get the service requests
     */
    private static ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws SQLException {
        //The list we will populate
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                //Get the correct entries from the text fields and add to the list so that we can add it to the database
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("room"));
                ent.setNotes(rs.getString("notes"));
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

    /**
     * This method gets all the records from the database so that they can be added to the display on the screen
     * @return ObservableList<DisplayTable>: The List of records that we want to actually display on the screen from the service requests
     * @throws ClassNotFoundException: If classes are not found
     * @throws SQLException: Any issues with the database
     */
    public ObservableList<DisplayTable> getAllRecords() throws ClassNotFoundException, SQLException {
        //Get the query from the database
        String query = "SELECT * FROM REQUESTINPROGRESS";
        try {
            //Get the information that we want from the database
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Store the results we get in the entry list display table
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method is meant to initialize the controller for use
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        try {
            //Create the table and get get the records and populate it it for viewing
            ObservableList<DisplayTable> entList = getAllRecords();
            Room.setCellValueFactory(new PropertyValueFactory<>("room"));
            Notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
            Type.setCellValueFactory(new PropertyValueFactory<>("type"));
            requestInProgress.setItems(entList);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Necessary assert statements for the different FXML elements
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert requestFulfilledButton != null : "fx:id=\"requestFulfilledButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert deleteIncompleteButton != null : "fx:id=\"deleteIncompleteButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert deleteCompletedButton != null : "fx:id=\"deleteCompletedButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";

    }


}
