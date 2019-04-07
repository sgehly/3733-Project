/**
 * Sample Skeleton for 'serviceRequests.fxml' Controller Class
 */

package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import edu.wpi.cs3733.d19.teamM.Main;

import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * This class is the controller for all the ServiceRequestPage related UI elements
 */
public class ServiceRequestsList {

    //All the different objects that have to be created for the page fxid are all the same name as the instance name
    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane incompletePane;
    @FXML
    private TitledPane completePane;
    @FXML
    private Text userText;


    //all items for the service request list view
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


    // table and columns for the request in progress table.
    @FXML // fx:id="requestInProgress"
    private TableView requestInProgress = new TableView(); // Value injected by FXMLLoader

    @FXML
    private TableColumn<DisplayTable,String> room = new TableColumn("room");
    @FXML
    private TableColumn<DisplayTable,String> notes = new TableColumn("notes");
    @FXML
    private TableColumn<DisplayTable,String> type = new TableColumn("type");
    @FXML
    private TableColumn<DisplayTable,Integer> requestId = new TableColumn("requestId");

    //table and columns for request log table.
    @FXML
    private TableView requestCompleted = new TableView();
    @FXML
    private TableColumn<DisplayTable,String> room1 = new TableColumn("room");
    @FXML
    private TableColumn<DisplayTable,String> notes1 = new TableColumn("notes");
    @FXML
    private TableColumn<DisplayTable,String> type1 = new TableColumn("type");
    @FXML
    private TableColumn<DisplayTable,Integer> requestId1 = new TableColumn("requestId");
    @FXML
    private TableColumn<DisplayTable,String> filledBy1 = new TableColumn("finished_by");


    @FXML
    private TextField FilledBy;

    @FXML
    private TextField getid;



    /**
     * Default method to delete a request
     * @param event: based on mouse input
     */
    @FXML
    void deleteIncompleteRequest(MouseEvent event)throws ClassNotFoundException {
        //delete a request
        String query = "DELETE FROM REQUESTINPROGRESS Where REQUESTID = ?";
        try {
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement s = conn.prepareStatement(query);
            s.setString(1, this.getIdFromTable("incomplete"));
            s.executeUpdate();
            System.out.println("deleted from db");
            //stmt.setString(6,FilledBy.getText());
            conn.close();
        }
        catch (Exception e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
        }

        initialize();

    }

    @FXML
    void deleteCompleteRequest(MouseEvent event)throws ClassNotFoundException{
        String query = "DELETE FROM REQUESTLOG Where REQUESTID = ?";
        try {
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement s = conn.prepareStatement(query);
            s.setString(1, this.getIdFromTable("complete"));
            s.executeUpdate();
            System.out.println("deleted from db");
            conn.close();
        }
        catch (Exception e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
        }

        initialize();
    }

    private String getIdFromTable(String table) {
        if(table.equals("incomplete")) {
            ObservableValue<Integer> id = requestId.getCellObservableValue(requestInProgress.getSelectionModel().getFocusedIndex());
            return Integer.toString(id.getValue());
        }
        else{
            ObservableValue<Integer> id = requestId1.getCellObservableValue(requestCompleted.getSelectionModel().getFocusedIndex());
            return Integer.toString(id.getValue());
        }
    }

    /**
     * The method that helps create the service request
     * @param event: Takes in the event of clicking the button
     * @throws IOException: Any errors that occur with the selection or other aspects
     */
    @FXML
    void goToMakeServiceRequest(MouseEvent event) throws IOException {
        Main.setScene("serviceRequest");
    }

    /**
     * This method is linked to the button that allows the individuals to return to the home screen
     * @throws Exception: Any exception that arises as the person tries to go back to the screen
     */
    @FXML
    private void navigateToHome() throws Exception{
        Main.setScene("home");
    }

    /**
     * This method is linked to the button that allows the individuals to return to the welcome screen
     * @throws Exception: Any exception that arises as the person tries to go back to the screen
     */
    @FXML
    public void logout() throws Exception{
        Main.setScene("welcome");
    }

    /**
     * Method that is linked to button that allows individual to mark their service request as completed
     * @param event
     */
    @FXML
    void markAsComplete(MouseEvent event) throws ClassNotFoundException{
        //move to complete table
        String query1 = "\n" +
                "INSERT INTO REQUESTLOG (REQUESTID, ROOM, NOTE, DATE, TYPE, FINISHED_BY) SELECT REQUESTID,ROOM,\n" +
                "NOTE,DATE,TYPE,FINISHED_BY from REQUESTINPROGRESS where REQUESTID = ?";
        String query2 = " UPDATE REQUESTLOG SET FINISHED_BY = ? WHERE REQUESTID = ?";
        String query3 = " DELETE FROM REQUESTINPROGRESS Where REQUESTID = ?";

        try {
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query1);
            stmt.setString(1, this.getIdFromTable("incomplete"));
            stmt.executeUpdate();
            System.out.println("inserted into db");


            //add the name of the person that got it done
            PreparedStatement st = conn.prepareStatement(query2);
            st.setString(1, FilledBy.getText());
            st.setString(2, this.getIdFromTable("incomplete"));
            st.executeUpdate();
            System.out.println("inserted into db");

            //delete from request that has been moved.
            PreparedStatement s = conn.prepareStatement(query3);
            s.setString(1, this.getIdFromTable("incomplete"));
            s.executeUpdate();
            System.out.println("deleted from db");
            //stmt.setString(6,FilledBy.getText());
            conn.close();

        }
        catch (Exception e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
        }

        initialize();
    }

    /**
     * This method gets the current objects from the DatabaseUtils and returns it as an Observable List
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
                ent.setNotes(rs.getString("note"));
                ent.setType(rs.getString("type"));
                ent.setId(rs.getInt("requestId"));
                ent.setFilledBy(rs.getString("finished_by"));
                //System.out.println(rs.getString("finished_by"));
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
            Connection conn = new DatabaseUtils().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Store the results we get in the entry list display table
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            requestInProgress.setItems(entryList);

            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<DisplayTable> getAllRecords2() throws ClassNotFoundException, SQLException {
        String query = "SELECT * FROM REQUESTLOG";
        try {
            Connection conn = new DatabaseUtils().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            requestCompleted.setItems(entryList);

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

    @FXML
    void initialize() {
        try {
            userText.setText(User.getUsername());
            accordion.setExpandedPane(incompletePane);
            //Create the table and get get the records and populate it it for viewing
            ObservableList<DisplayTable> entList = getAllRecords();
            ObservableList<DisplayTable> entList2 = getAllRecords2();



            requestId.setCellValueFactory(new PropertyValueFactory<>("id"));
            room.setCellValueFactory(new PropertyValueFactory<>("room"));
            notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));

            requestId1.setCellValueFactory(new PropertyValueFactory<>("id"));
            room1.setCellValueFactory(new PropertyValueFactory<>("room"));
            notes1.setCellValueFactory(new PropertyValueFactory<>("notes"));
            type1.setCellValueFactory(new PropertyValueFactory<>("type"));
            filledBy1.setCellValueFactory(new PropertyValueFactory<>("filledBy"));



            requestInProgress.setItems(entList);
            requestCompleted.setItems(entList2);
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
