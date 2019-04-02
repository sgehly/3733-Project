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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;



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
    private TableView reqeustLog = new TableView();

    @FXML
    private TableColumn<DisplayTable,String> room1 = new TableColumn("room");

    @FXML
    private TableColumn<DisplayTable,String> notes1 = new TableColumn("notes");

    @FXML
    private TableColumn<DisplayTable,String> type1 = new TableColumn("type");

    @FXML
    private TableColumn<DisplayTable,Integer> requestId1 = new TableColumn("requestId");

    @FXML
    private TableColumn<DisplayTable,String> filledBy1 = new TableColumn("filledBy");


    @FXML
    private TextField FilledBy;

    @FXML
    private TextField getid;



    @FXML
    void deleteRequest(MouseEvent event)throws ClassNotFoundException {
        //delete a request
        String query3 = " DELETE FROM REQUESTINPROGRESS Where REQUESTID = ?";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);


            PreparedStatement s = conn.prepareStatement(query3);
            s.setString(1, getid.getText());
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
        String query = "\n" +
                "INSERT INTO REQUESTLOG (REQUESTID, ROOM, NOTE, DATE, TYPE, FILLEDBY) SELECT REQUESTID,ROOM,\n" +
                "NOTE,DATE,TYPE,filledby from REQUESTINPROGRESS where REQUESTID = ?";
        String query2 = " UPDATE REQUESTLOG SET FILLEDBY = ? WHERE REQUESTID = ?";
        String query3 = " DELETE FROM REQUESTINPROGRESS Where REQUESTID = ?";

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);

            // insert from requesinprogress to request log the desired request
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, getid.getText());
            stmt.executeUpdate();
            System.out.println("inserted into db");


            //add the name of the person that got it done
            PreparedStatement st = conn.prepareStatement(query2);
            st.setString(1, FilledBy.getText());
            st.setString(2, getid.getText());
            st.executeUpdate();
            System.out.println("inserted into db");


            //delet from request that has been moved.
            PreparedStatement s = conn.prepareStatement(query3);
            s.setString(1, getid.getText());
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
                ent.setFilledBy(rs.getString("filledby"));
                //System.out.println(rs.getString("filledby"));
                entList.add(ent);
            }
            return entList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    private static ObservableList<DisplayTable> getEntryObjects2(ResultSet rs) throws SQLException {
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("room"));
                ent.setNotes(rs.getString("note"));
                ent.setType(rs.getString("type"));
                ent.setId(rs.getInt("requestId"));
                ent.setFilledBy(rs.getString("filledby"));
                //System.out.println(rs.getString("filledby"));
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
            ObservableList<DisplayTable> entryList = getEntryObjects2(rs);
            reqeustLog.setItems(entryList);

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
            ObservableList<DisplayTable> entList2 = getAllRecords2();



            requestId.setCellValueFactory(new PropertyValueFactory<>("id"));
            room.setCellValueFactory(new PropertyValueFactory<>("room"));
            notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));

            requestId1.setCellValueFactory(new PropertyValueFactory<>("id"));
            room1.setCellValueFactory(new PropertyValueFactory<>("room"));
            notes1.setCellValueFactory(new PropertyValueFactory<>("notes"));
            type1.setCellValueFactory(new PropertyValueFactory<>("type"));
            filledBy1.setCellValueFactory(new PropertyValueFactory<>("FilledBy"));



            requestInProgress.setItems(entList);
            reqeustLog.setItems(entList2);
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
