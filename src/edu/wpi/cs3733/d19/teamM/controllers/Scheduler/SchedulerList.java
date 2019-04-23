/**
 * Sample Skeleton for 'serviceRequests.fxml' Controller Class
 *//**
 * Sample Skeleton for 'serviceRequests.fxml' Controller Class
 */

package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.controlsfx.control.PropertySheet;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This class is the controller for all the ServiceRequestPage related UI elements
 */
public class SchedulerList {

    @FXML
    private TableColumn<DisplayTable,String> startTime;

    @FXML
    private JFXComboBox<String> dateDropDown;

    @FXML
    private Text userText;

    @FXML
    private Label lblClock;

    @FXML
    private JFXComboBox<String> roomDropDown;

    ObservableList<String> rooms = FXCollections.observableArrayList("ALL","CR_1","CR_2","CR_3","CR_4","CR_5","CR_6","CR_7","CR_8","CR_9","CR_10","NONE");

    @FXML
    private TableView BookedRooms = new TableView();

    @FXML
    private Label lblDate;

    @FXML
    private TableColumn<DisplayTable,String> roomName;

    @FXML
    private TableColumn<DisplayTable,String> roomType;

    @FXML
    private TableColumn<DisplayTable,String> endTime;

   // DetailedDayView ddv = new DetailedDayView(); // initializes the detailed day view

    /**
     *
     * @return
     * @throws SQLException
     */

    public ObservableList<String> getDatesForDropDown() throws SQLException {

        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("ALL");

        if(roomDropDown.getSelectionModel().getSelectedItem() == "NONE"){
            String query ="SELECT STARTTIME FROM BOOKEDTIMES";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    if (!list.contains(rs.getDate(1).toString())){
                        list.add(rs.getDate(1).toString());
                    }
                }
                conn.close();

                return list;
            } catch (SQLException e) {

                System.out.println("Error while trying to fetch all records");
                e.printStackTrace();
                throw e;
            }
        }
        else {
            String query = "SELECT STARTTIME FROM BOOKEDTIMES where ROOMID = ?";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, roomDropDown.getSelectionModel().getSelectedItem());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    if (!list.contains(rs.getDate(1).toString())) {
                        list.add(rs.getDate(1).toString());
                    }
                }
                conn.close();
                return list;
            } catch (SQLException e) {
                System.out.println("Error while trying to fetch all records");
                e.printStackTrace();
                throw e;
            }
        }
    }

    /**
     *
     * @param rs
     * @return an observable list
     * @throws SQLException
     */
    private static ObservableList<DisplayTable> getEntryObjects2(ResultSet rs) throws SQLException {
        //The list we will populate
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                //Get the correct entries from the text fields and add to the list so that we can add it to the database
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("ROOMID"));
                ent.setStartTime(rs.getString("Starttime"));
                ent.setEndTime(rs.getString("endtime"));

                if(rs.getString("Roomid").equals("CR_1")  ||rs.getString("Roomid").equals("CR_2")  || rs.getString("Roomid").equals("CR_3") ||
                        rs.getString("Roomid").equals("CR_5")  || rs.getString("Roomid").equals("CR_7") ){
                    ent.setType("COMP");
                }
                else{
                    ent.setType("CLASS");
                }

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
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ObservableList<DisplayTable> getAllRecords() throws ClassNotFoundException, SQLException {

        System.out.println(roomDropDown.getSelectionModel().getSelectedItem());
        //Get the query from the database

        if(roomDropDown.getSelectionModel().getSelectedItem() == "ALL"){
            String query = "SELECT * FROM BOOKEDTIMES";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);

                ResultSet resultSet = stmt.executeQuery();
                ObservableList<DisplayTable> entryList = getEntryObjects2(resultSet);
                BookedRooms.setItems(entryList);
                initialize();
                conn.close();
                return entryList;
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }

///////////////specific room
        if(dateDropDown.getSelectionModel().getSelectedItem() == "ALL" || dateDropDown.getSelectionModel().getSelectedItem() == null){

            String query = "SELECT * FROM BOOKEDTIMES WHERE ROOMID=?";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);

                stmt.setString(1,roomDropDown.getSelectionModel().getSelectedItem());

                ResultSet resultSet = stmt.executeQuery();
                ObservableList<DisplayTable> entryList = getEntryObjects2(resultSet);
                String roomNum = roomDropDown.getSelectionModel().getSelectedItem();
                //Entry<DisplayTable> roomEnt;

                entryList.forEach(DisplayTable -> {
                    //if(DisplayTable.getRoom().equals("CR_1"))
                        //Entry<DisplayTable> ent = new Entry<DisplayTable>(DisplayTable);
                });


                BookedRooms.setItems(entryList);
                initialize();
                conn.close();
                return entryList;
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }

        if(roomDropDown.getSelectionModel().getSelectedItem() == "NONE"){

            String query = "SELECT * from BOOKEDTIMES WHERE  STARTTIME > ? AND STARTTIME  < ?";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);

                System.out.println(dateDropDown.getSelectionModel().getSelectedItem() + " 00:00:00.000000000");
                stmt.setString(1, dateDropDown.getSelectionModel().getSelectedItem() + " 00:00:00.000000000");
                stmt.setString(2, dateDropDown.getSelectionModel().getSelectedItem() + " 23:59:59.000000000");
                System.out.println(dateDropDown.getSelectionModel().getSelectedItem() + " 23:59:59.000000000");

                ResultSet rs = stmt.executeQuery();
                ObservableList<DisplayTable> entryList = getEntryObjects2(rs);
                BookedRooms.setItems(entryList);
                initialize();
                conn.close();
                return entryList;
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
        else  {
            String query = "SELECT * from BOOKEDTIMES WHERE ROOMID = ? AND STARTTIME > ? AND STARTTIME  < ?";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);

                System.out.println(dateDropDown.getSelectionModel().getSelectedItem() + " 00:00:00.000000000");
                stmt.setString(1, roomDropDown.getSelectionModel().getSelectedItem());
                stmt.setString(2, dateDropDown.getSelectionModel().getSelectedItem() + " 00:00:00.000000000");
                stmt.setString(3, dateDropDown.getSelectionModel().getSelectedItem() + " 23:59:59.000000000");
                System.out.println(dateDropDown.getSelectionModel().getSelectedItem() + " 23:59:59.000000000");

                ResultSet rs = stmt.executeQuery();
                ObservableList<DisplayTable> entryList = getEntryObjects2(rs);
                BookedRooms.setItems(entryList);
                initialize();
                conn.close();
                return entryList;
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /**
     * This method is linked to the button that allows the individuals to return to the home screen
     *
     * @throws Exception: Any exception that arises as the person tries to go back to the screen
     */
    @FXML
    private void navigateToHome() throws Exception {
        Main.setScene("home");
    }

    /**
     * This method is linked to the button that allows the individuals to return to the welcome screen
     *
     * @throws Exception: Any exception that arises as the person tries to go back to the screen
     */
    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    @FXML
    public void updateTable()throws SQLException{

        System.out.println("in update");
        BookedRooms.refresh();
        roomDropDown.setItems(rooms);
        dateDropDown.setItems(this.getDatesForDropDown());

        if(getDatesForDropDown().size() == 0){
            BookedRooms.getItems().clear();
        }

        try {
            getAllRecords();
            roomName.setCellValueFactory(new PropertyValueFactory<>("room"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("starttime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endtime"));
            roomType.setCellValueFactory(new PropertyValueFactory<>("type"));
        }
        catch (Exception e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
        }
    }

    /**
     * Default method to delete a request
     *
     * @param event: based on mouse input
     */
    @FXML
    void deleteRequest(ActionEvent event) {
        //delete a request

        if (BookedRooms.getFocusModel().getFocusedIndex() >= 0) {
            String query = "DELETE FROM BOOKEDTIMES Where ROOMID = ?";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement s = conn.prepareStatement(query);
                s.setString(1, roomDropDown.getSelectionModel().getSelectedItem());
                s.executeUpdate();

                System.out.println("deleted from db");
                //stmt.setString(6,FilledBy.getText());
                conn.close();

                BookedRooms.getSelectionModel().clearSelection();
                // get selected index and then remove it

            } catch (Exception e) {
                System.out.println("Error while trying to fetch all records");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void disengageInProgress() {
        BookedRooms.getSelectionModel().clearSelection();
    }

    /**
     * This method is meant to initialize the controller for use
     */

    @FXML
    void initialize() throws SQLException {

        new Clock(lblClock, lblDate);

        userText.setText(User.getUsername());

        roomDropDown.setItems(rooms);

        dateDropDown.setItems(getDatesForDropDown());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
//                        ddv.setToday(LocalDate.now());
//                        ddv.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        //Calendar c1 = new Calendar();
        //c1.addEntries(getAllRecords());
        // make entries from database
        //c1.addEntries(dateDropDown.getSelectionModel().);
        //ddv.setDate(dateDropDown.getSelectionModel().getSelectedItem());



//
//        try {
//
//            roomName.setCellValueFactory(new PropertyValueFactory<>("room"));
//            startTime.setCellValueFactory(new PropertyValueFactory<>("starttime"));
//            endTime.setCellValueFactory(new PropertyValueFactory<>("endtime"));
//            roomType.setCellValueFactory(new PropertyValueFactory<>("type"));
//        }
//        catch (Exception e) {
//            System.out.println("Error while trying to fetch all records");
//            e.printStackTrace();
//        }



    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     *
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("scheduler");
    }


}
