/**
 * Sample Skeleton for 'serviceRequests.fxml' Controller Class
 */

package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.*;

/**
 * This class is the controller for all the ServiceRequestPage related UI elements
 */
public class SchedulerList {

    @FXML
    private TableColumn<DisplayTable,String> date;

    @FXML
    private JFXComboBox<String> dateDropDown;

    @FXML
    private Text userText;

    @FXML
    private Label lblClock;

    @FXML
    private JFXComboBox<String> roomDropDown;

    ObservableList<String> rooms = FXCollections.observableArrayList("CR_1","CR_2","CR_3","CR_4","CR_5","CR_6","CR_7","CR_8");

    @FXML
    private TableView BookedRooms = new TableView();

    @FXML
    private Label lblDate;

    @FXML
    private TableColumn<DisplayTable,String> roomName;

    @FXML
    private TableColumn<DisplayTable,String> roomType;


    /**
     *
     * @return
     * @throws SQLException
     */


    public ObservableList<String> getDatesForDropDown() throws SQLException {

        ObservableList<String> list = FXCollections.observableArrayList();

        String query ="SELECT STARTTIME FROM BOOKEDTIMES";

        try {

            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                list.add(rs.getString(1));
            }

            return list;
        } catch (SQLException e) {

            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
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
                ent.setStartTime(rs.getString("StartDate"));
                entList.add(ent);
            }
            return entList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }



    public ObservableList<DisplayTable> getAllRecords2() throws ClassNotFoundException, SQLException {
        //Get the query from the database
        String query = "SELECT * from BOOKEDTIMES WHERE ROOMID = ? and STARTTIME = ?";


        try {
        Connection conn = new DatabaseUtils().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,roomDropDown.getSelectionModel().getSelectedItem());
        stmt.setString(2,dateDropDown.getSelectionModel().getSelectedItem());
        ResultSet rs = stmt.executeQuery();
        ObservableList<DisplayTable> entryList = getEntryObjects2(rs);
        BookedRooms.setItems(entryList);
        initialize();
        return entryList;
    } catch (SQLException e) {
        System.out.println("Error while trying to fetch all records");
        e.printStackTrace();
        throw e;
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


    /**
     * This method is meant to initialize the controller for use
     */

    @FXML
    void initialize() throws SQLException {

        roomDropDown.setValue("CR_1");
        roomDropDown.setItems(rooms);
        dateDropDown.setValue("Date");
        dateDropDown.setItems(this.getDatesForDropDown());



        roomName.setCellValueFactory(new PropertyValueFactory<>("room"));
        date.setCellValueFactory(new PropertyValueFactory<>("startime"));


        new Clock(lblClock, lblDate);

       // userText.setText(User.getUsername());

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
