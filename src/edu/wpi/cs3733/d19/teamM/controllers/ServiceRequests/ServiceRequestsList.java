/**
 * Sample Skeleton for 'serviceRequests.fxml' Controller Class
 */

package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d19.teamM.Main;

import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests.DisplayTable;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.scene.text.Text;

/**
 * This class is the controller for all the ServiceRequestPage related UI elements
 */
public class ServiceRequestsList {

    private int hrs;
    private int mins;
    private int secs;

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private TitledPane incompletePane;
    @FXML
    private TitledPane completePane;
    @FXML
    private Text userText;

    int currentTab = 0;

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
    private TableView requestsInProgress = new TableView(); // Value injected by FXMLLoader

    @FXML
    private TableColumn<DisplayTable, Integer> RIPIdCol = new TableColumn("id");
    @FXML
    private TableColumn<DisplayTable, String> RIPTypeCol = new TableColumn("type");
    @FXML
    private TableColumn<DisplayTable, String> RIPRoomCol = new TableColumn("room");
    @FXML
    private TableColumn<DisplayTable, String> RIPSubTypeCol = new TableColumn("subType");
    @FXML
    private TableColumn<DisplayTable, String> RIPDescCol = new TableColumn("description");
    @FXML
    private TableColumn<DisplayTable, Integer> RIPCheckboxCol = new TableColumn("checkbox");


    //table and columns for request log table.
    @FXML
    private TableView requestsCompleted = new TableView();
    @FXML
    private TableColumn<DisplayTable, Integer> RCIdCol = new TableColumn("id");
    @FXML
    private TableColumn<DisplayTable, String> RCTypeCol = new TableColumn("type");
    @FXML
    private TableColumn<DisplayTable, String> RCRoomCol = new TableColumn("room");
    @FXML
    private TableColumn<DisplayTable, String> RCSubTypeCol = new TableColumn("subType");
    @FXML
    private TableColumn<DisplayTable, String> RCDescCol = new TableColumn("description");
    @FXML
    private TableColumn<DisplayTable, Integer> RCCheckboxCol = new TableColumn("checkbox");
    @FXML
    private TableColumn<DisplayTable, String> RCFilledByCol = new TableColumn("filledBy");


    @FXML
    private Button fulfill;

    @FXML
    private TextField FilledBy;

    @FXML
    private TextField getid;


    @FXML
    private JFXComboBox dropdown;


    @FXML
    private JFXComboBox usersDropDown;


    /**
     * Default method to delete a request
     *
     * @param event: based on mouse input
     */
    @FXML
    void deleteRequest(ActionEvent event) {
        //delete a request

        if (requestsInProgress.getFocusModel().getFocusedIndex() >= 0) {
            String query = "DELETE FROM REQUESTINPROGRESS Where REQUESTID = ?";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement s = conn.prepareStatement(query);
                s.setString(1, this.getIdFromTable("incomplete"));
                s.executeUpdate();

                System.out.println("deleted from db");
                //stmt.setString(6,FilledBy.getText());
                conn.close();

                this.initWithType(this.currentTab);
                requestsInProgress.getSelectionModel().clearSelection();
                requestsCompleted.getSelectionModel().clearSelection();

            } catch (Exception e) {
                System.out.println("Error while trying to fetch all records");
                e.printStackTrace();
            }
        }

        if (requestsCompleted.getFocusModel().getFocusedIndex() >= 0) {
            String query = "DELETE FROM REQUESTLOG Where REQUESTID = ?";
            try {
                Connection conn = new DatabaseUtils().getConnection();
                PreparedStatement s = conn.prepareStatement(query);
                s.setString(1, this.getIdFromTable("complete"));
                s.executeUpdate();

                System.out.println("deleted from db");
                //stmt.setString(6,FilledBy.getText());
                conn.close();

                this.initWithType(this.currentTab);

            } catch (Exception e) {
                System.out.println("Error while trying to fetch all records");
                e.printStackTrace();
                requestsInProgress.getSelectionModel().clearSelection();
                requestsCompleted.getSelectionModel().clearSelection();
            }
        }

    }

    //TODO on request form list employees that can handle them
    @FXML
    void disengageComplete() {
        try {
            int temp = 0;
            String query = "SELECT * FROM USERS Where USERNAME = ?";
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement s = conn.prepareStatement(query);
            s.setString(1, (String) usersDropDown.getSelectionModel().getSelectedItem());
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                temp = rs.getInt("ACCOUNTINT");
            }
            System.out.println(temp);
            System.out.println((String) usersDropDown.getSelectionModel().getSelectedItem());
            System.out.println(getRequestFromTable("incomplete"));
            if (getRequestFromTable("incomplete").equals("sanitation") && temp == 2 || User.getPrivilege() != 100)
                fulfill.setDisable(true);
            else if (getRequestFromTable("incomplete").equals("interpreter") && temp == 1 || User.getPrivilege() != 100)
                fulfill.setDisable(true);
            else if (usersDropDown.getSelectionModel().getSelectedItem() == null){
                fulfill.setDisable(true);
            }
            else
                fulfill.setDisable(false);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestsCompleted.getSelectionModel().clearSelection();
    }

    @FXML
    void disengageInProgress() {
        requestsInProgress.getSelectionModel().clearSelection();
    }

    private String getIdFromTable(String table) {
        if (table.equals("incomplete")) {
            ObservableValue<Integer> id = RIPIdCol.getCellObservableValue(requestsInProgress.getSelectionModel().getFocusedIndex());
            return Integer.toString(id.getValue());
        } else {
            ObservableValue<Integer> id = RCIdCol.getCellObservableValue(requestsCompleted.getSelectionModel().getFocusedIndex());
            return Integer.toString(id.getValue());
        }
    }

    private String getRequestFromTable(String table) {
        if (table.equals("incomplete")) {
            ObservableValue<String> id = RIPTypeCol.getCellObservableValue(requestsInProgress.getSelectionModel().getFocusedIndex());
            return id.getValue();
        } else {
            ObservableValue<String> id = RCTypeCol.getCellObservableValue(requestsCompleted.getSelectionModel().getFocusedIndex());
            return id.getValue();
        }
    }

    /**
     * The method that helps create the service request
     *
     * @param event: Takes in the event of clicking the button
     * @throws IOException: Any errors that occur with the selection or other aspects
     */
    @FXML
    void goToMakeServiceRequest(MouseEvent event) throws IOException {
        Main.setScene("serviceRequest");
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
     * Method that is linked to button that allows individual to mark their service request as completed
     *
     * @param event
     */
    @FXML
    void markAsComplete(ActionEvent event) throws ClassNotFoundException {
        fulfill.setDisable(true);
        //move to complete table
        String query1 = "INSERT INTO REQUESTLOG (REQUESTID, TYPE, ROOM, SUBTYPE, DESCRIPTION, CHECKBOX, DATE, FINISHED_BY) SELECT REQUESTID, TYPE, ROOM, SUBTYPE, DESCRIPTION, CHECKBOX, DATE, FINISHED_BY from REQUESTINPROGRESS where REQUESTID = ?";
        String query2 = " UPDATE REQUESTLOG SET FINISHED_BY = ? WHERE REQUESTID = ?";
        String query3 = " DELETE FROM REQUESTINPROGRESS Where REQUESTID = ?";

        String nextPage = getRequestFromTable("incomplete");

        if (requestsInProgress.getFocusModel().getFocusedIndex() == -1) return;

        try {
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query1);
            stmt.setString(1, this.getIdFromTable("incomplete"));
            stmt.executeUpdate();
            System.out.println("inserted into db");


            //add the name of the person that got it done
            PreparedStatement st = conn.prepareStatement(query2);
            st.setString(1, (String) usersDropDown.getSelectionModel().getSelectedItem());
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
            this.initWithType(this.currentTab);
        } catch (Exception e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
        }

        if (nextPage.equals("interpreter")) {
            Main.setScene("serviceRequestReports/languageReportTemplate");
        }

        if (nextPage.equals("sanitation")) {
            Main.setScene("serviceRequestReports/sanitationReportTemplate");
        }

        if(nextPage.equals("it")) {
            Main.setScene("serviceRequestReports/ITReportTemplate");
        }

        if(nextPage.equals("prescriptions")) {
            Main.setScene("serviceRequestReports/prescriptionReportTemplate");
        }

        if(nextPage.equals("internal")) {
            Main.setScene("serviceRequestReports/IntTransportReportTemplate");
        }

        if(nextPage.equals("flowers")) {
            Main.setScene("serviceRequestReports/flowersReportTemplate");
        }
        if(nextPage.equals("religion")) {
            Main.setScene("serviceRequestReports/religiousReportTemplate");
        }
        if(nextPage.equals("security")) {
            Main.setScene("serviceRequestReports/securityReportTemplate");
        }
        if(nextPage.equals("gift")) {
            Main.setScene("serviceRequestReports/giftReportTemplate");
        }
        if(nextPage.equals("av")) {
            Main.setScene("serviceRequestReports/aVReportTemplate");
        }
        if(nextPage.equals("external")) {
            Main.setScene("serviceRequestReports/extTransportReportTemplate");
        }
        if(nextPage.equals("laboratory")) {
            Main.setScene("serviceRequestReports/laboratoryReportTemplate");
        }
    }

    private void goToProperReport(String nextPage) {
        if (nextPage.equals("interpreter")) {
            System.out.println(this.getRequestFromTable("incomplete"));
            Main.setScene("languageReportTemplate");
        } else if (nextPage.equals("sanitation")) {
            System.out.println(this.getRequestFromTable("incomplete"));
            Main.setScene("sanitationReportTemplate");
        }
        else if(nextPage.equals("prescription")) {
            System.out.println(this.getRequestFromTable("incomplete"));
            Main.setScene("prescriptionReportTemplate");
        }
        else if(nextPage.equals("flowers")) {
            System.out.println(this.getRequestFromTable("incomplete"));
            Main.setScene("flowersReportTemplate");
        }
        else if(nextPage.equals("religion")) {
            System.out.println(this.getRequestFromTable("incomplete"));
            Main.setScene("religiousReportTemplate");
        }
        else if(nextPage.equals("security")) {
            System.out.println(this.getRequestFromTable("incomplete"));
            Main.setScene("securityReportTemplate");
        }
        else if(nextPage.equals("gift")) {
            System.out.println(this.getRequestFromTable("incomplete"));
            Main.setScene("giftReportTemplate");
        }
        else if(nextPage.equals("external")) {
            System.out.println(this.getRequestFromTable("incomplete"));
            Main.setScene("extTransportReportTemplate");
        }
        System.out.println("yo: " + this.getRequestFromTable("incomplete"));
    }

    /**
     * This method gets the current objects from the DatabaseUtils and returns it as an Observable List
     *
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
                ent.setType(rs.getString("type"));
                ent.setRoom(rs.getString("room"));
                ent.setSubType(rs.getString("subType"));
                ent.setDescription(rs.getString("description"));
                ent.setCheckbox(rs.getString("checkbox").equals("0") ? "NO" : "YES");
                ent.setRequestId(rs.getInt("requestId"));
                ent.setFilledBy(rs.getString("finished_by"));
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
     *
     * @return ObservableList<DisplayTable>: The List of records that we want to actually display on the screen from the service requests
     * @throws ClassNotFoundException: If classes are not found
     * @throws SQLException:           Any issues with the database
     */
    public ObservableList<DisplayTable> getAllRecords(String type, String table) throws ClassNotFoundException, SQLException {
        //Get the query from the database
        String query = "SELECT * FROM " + table;

        if (type != "all") {
            query += " WHERE TYPE='" + type + "'";
        }

        try {
            Connection conn = new DatabaseUtils().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //Store the results we get in the entry list display table
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            requestsInProgress.setItems(entryList);
            conn.close();
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void exportInProgress(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("in print");
        String filename = "RequestInProgress.csv";
        try {
            FileWriter file = new FileWriter(filename);
            Connection conn = new DatabaseUtils().getConnection();
            String query = "select * from REQUESTINPROGRESS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                file.append(rs.getString(1));
                file.append(',');
                file.append(rs.getString(2));
                file.append(',');
                file.append(rs.getString(3));
                file.append(',');
                file.append(rs.getString(4));
                file.append(',');
                file.append(rs.getString(5));
                file.append(',');
                file.append(rs.getString(6));
                file.append(',');
                file.append(rs.getString(7));
                file.append(',');
                file.append(rs.getString(8));
                file.append('\n');
            }
            file.flush();
            file.close();
            conn.close();
            System.out.println("CSV File is created successfully.");
        } catch (Exception ev) {
            ev.printStackTrace();
        }
    }

    @FXML
    private void navigateToReport() throws Exception {
        Main.setScene("generateReport");
    }

    @FXML
    private void exportComplete(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println("in print");
        String filename = "CompletedRequests.csv";
        try {
            FileWriter file = new FileWriter(filename);
            Connection conn = new DatabaseUtils().getConnection();
            String query = "select * from REQUESTLOG";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                file.append(rs.getString(1));
                file.append(',');
                file.append(rs.getString(2));
                file.append(',');
                file.append(rs.getString(3));
                file.append(',');
                file.append(rs.getString(4));
                file.append(',');
                file.append(rs.getString(5));
                file.append(',');
                file.append(rs.getString(6));
                file.append(',');
                file.append(rs.getString(7));
                file.append(',');
                file.append(rs.getString(8));
                file.append('\n');
            }
            file.flush();
            file.close();
            conn.close();
            System.out.println("CSV File is created successfully.");
        } catch (Exception ev) {
            ev.printStackTrace();
        }
    }


    private void initWithType(int index) {

        String identifiers[] = new String[]{"all", "sanitation", "interpreter", "it", "av", "gift", "flowers", "internal", "external", "religion", "security", "prescriptions", "laboratory"};
        String identifier = identifiers[index];

        try {
            //Create the table and get get the records and populate it it for viewing
            ObservableList<DisplayTable> inProgressItems = getAllRecords(identifier, "REQUESTINPROGRESS");
            ObservableList<DisplayTable> completedItems = getAllRecords(identifier, "REQUESTLOG");
            requestsInProgress.setItems(inProgressItems);
            requestsCompleted.setItems(completedItems);


            if (index > 0) {
                String subTypeLabel = "";
                String descLabel = "";
                String checkboxLabel = "";

                switch (identifier) {
                    case "religion":
                        subTypeLabel = "Religion";
                        descLabel = "Request";
                        checkboxLabel = "Possession?";
                        break;

                    case "internal":
                        subTypeLabel = "Internal Transportation";
                        descLabel = "Request";
                        checkboxLabel = "Urgent";
                        break;

                    case "external":
                        subTypeLabel = "External Transportation";
                        descLabel = "Request";
                        checkboxLabel = "Urgent";
                        break;
                    case "it":
                        subTypeLabel = "IT";
                        descLabel = "Request";
                        checkboxLabel = "Urgent";
                        break;
                    case "interpreter":
                        subTypeLabel = "Language";
                        descLabel = "Request";
                        checkboxLabel = "Urgent";
                        break;
                    case "av":
                        subTypeLabel = "Audio Visuals";
                        descLabel = "Request";
                        checkboxLabel = "Pick Up";
                        break;
                    case "gift":
                        subTypeLabel = "Gift shop";
                        descLabel = "Request";
                        checkboxLabel = "Packaged";
                        break;
                    case "flowers":
                        subTypeLabel = "Flowers";
                        descLabel = "Request";
                        checkboxLabel = "Replace Old Flowers";
                        break;
                    case "prescriptions":
                        subTypeLabel = "Prescriptions";
                        descLabel = "Request";
                        checkboxLabel = "Urgent";
                        break;
                    case "sanitation":
                        subTypeLabel = "Sanitation";
                        descLabel = "Request";
                        checkboxLabel = "Radioactive";
                        break;
                    case "laboratory":
                        subTypeLabel = "Lab test";
                        descLabel = "Request";
                        checkboxLabel = "Urgent";
                        break;
                    case "security":
                        subTypeLabel = "Security";
                        descLabel = "Request";
                        checkboxLabel = "Emergency";
                        break;


                }

                RIPSubTypeCol.setText(subTypeLabel);
                RIPDescCol.setText(descLabel);
                RIPCheckboxCol.setText(checkboxLabel);

                RCSubTypeCol.setText(subTypeLabel);
                RCDescCol.setText(descLabel);
                RCCheckboxCol.setText(checkboxLabel);
            } else {
                RIPSubTypeCol.setText("");
                RIPDescCol.setText("");
                RIPCheckboxCol.setText("");

                RCSubTypeCol.setText("");
                RCDescCol.setText("");
                RCCheckboxCol.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is meant to initialize the controller for use
     */

    @FXML
    void initialize() {

        fulfill.setDisable(true);
        ObservableList<String> uDropDown = FXCollections.observableArrayList();
        try {
            Connection conn = new DatabaseUtils().getConnection();
            String query = "SELECT * From USERS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                uDropDown.add(rs.getString(1));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        usersDropDown.setItems(uDropDown);
        usersDropDown.setOnAction((e) -> {
            this.disengageComplete();
        });

        requestsInProgress.getSelectionModel().clearSelection();
        requestsCompleted.getSelectionModel().clearSelection();

        new Clock(lblClock, lblDate);

       // userText.setText(User.getUsername());
        userText.setText("");

        ObservableList<String> dropdownList = FXCollections.observableArrayList();
        ;
        dropdownList.setAll("All", "Sanitation", "Interpreter", "IT Service", "AV Service", "Gift Shop", "Florist", "Internal Transport", "External Transport", "Religious", "Security", "Prescriptions", "Lab test");

        dropdown.setItems(dropdownList);
        dropdown.getSelectionModel().select("All");

        dropdown.setOnAction((e) -> {
            currentTab = dropdown.getSelectionModel().getSelectedIndex();
            initWithType(currentTab);
        });

        RIPIdCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        RIPTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        RIPRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        RIPSubTypeCol.setCellValueFactory(new PropertyValueFactory<>("subType"));
        RIPDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        RIPCheckboxCol.setCellValueFactory(new PropertyValueFactory<>("checkbox"));

        RCIdCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        RCTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        RCRoomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        RCSubTypeCol.setCellValueFactory(new PropertyValueFactory<>("subType"));
        RCDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        RCCheckboxCol.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
        RCFilledByCol.setCellValueFactory(new PropertyValueFactory<>("filledBy"));

        initWithType(0);
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


}
