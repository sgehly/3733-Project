/**
 * Sample Skeleton for 'serviceRequests.fxml' Controller Class
 */

package app.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import app.Main;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;



public class ServiceRequestsList {

    //path to the database
    String dbPath = "jdbc:derby:myDB";


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
    @FXML
    private TableView requestInProgress = new TableView();
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



    @FXML
    void deleteIncompleteRequest(MouseEvent event)throws ClassNotFoundException {
        //delete a request
        String query = " DELETE FROM REQUESTINPROGRESS Where REQUESTID = ?";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
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
        String query = " DELETE FROM REQUESTLOG Where REQUESTID = ?";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            PreparedStatement s = conn.prepareStatement(query);
            s.setString(1, this.getIdFromTable("complete"));
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
    void markAsComplete(MouseEvent event) throws ClassNotFoundException{
        //move to complete table
        String query1 = "\n" +
                "INSERT INTO REQUESTLOG (REQUESTID, ROOM, NOTE, DATE, TYPE, FINISHED_BY) SELECT REQUESTID,ROOM,\n" +
                "NOTE,DATE,TYPE,FINISHED_BY from REQUESTINPROGRESS where REQUESTID = ?";
        String query2 = " UPDATE REQUESTLOG SET FINISHED_BY = ? WHERE REQUESTID = ?";
        String query3 = " DELETE FROM REQUESTINPROGRESS Where REQUESTID = ?";

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);

            // insert from requesinprogress to request log the desired request
            PreparedStatement stmt = conn.prepareStatement(query1);
            stmt.setString(1, this.getIdFromTable("incomplete"));
            stmt.executeUpdate();
            System.out.println("inserted into db");


            //add the name of the person that got it done
            PreparedStatement st = conn.prepareStatement(query2);
            st.setString(1, FilledBy.getText());
            st.setString(2, this.getIdFromTable("complete"));
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

    private static ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws SQLException {
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
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

    public ObservableList<DisplayTable> getAllRecords() throws ClassNotFoundException, SQLException {
        String query = "SELECT * FROM REQUESTINPROGRESS";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
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
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
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

    @FXML
    void initialize() {
        try {
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

        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert requestFulfilledButton != null : "fx:id=\"requestFulfilledButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert deleteIncompleteButton != null : "fx:id=\"deleteIncompleteButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";
        assert deleteCompletedButton != null : "fx:id=\"deleteCompletedButton\" was not injected: check your FXML file 'serviceRequests.fxml'.";

    }


}
