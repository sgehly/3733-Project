
package app.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TablePosition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * The controller class associated with the scheduler
 */
public class Scheduler {
   //Instances of necessary objects
    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static double offSetX,offSetY,zoomlvl;

    String dbPath = "jdbc:derby:myDB";

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="zoomLvl"
    private Slider zoomLvl; // Value injected by FXMLLoader

    @FXML // fx:id="imageView"
    private HBox imageView; // Value injected by FXMLLoader

    @FXML // fx:id="image"
    private ImageView image; // Value injected by FXMLLoader

    /**
     * This method is for the button that allows the user to navigate back to the home screen
     * @throws Exception
     */
    @FXML
    private TableView tableView = new TableView();

    @FXML
    private TableColumn<DisplayTable,String> roomId = new TableColumn("roomId");

    @FXML
    private TableColumn<DisplayTable,String> capacity = new TableColumn("capacity");

    @FXML
    private TableColumn<DisplayTable,String> roomType = new TableColumn("roomType");

    @FXML
    private JFXDatePicker startDate = new JFXDatePicker();

    @FXML
    private JFXTimePicker startTime = new JFXTimePicker();;

    @FXML
    private JFXDatePicker endDate = new JFXDatePicker();

    @FXML
    private JFXTimePicker endTime = new JFXTimePicker();

    @FXML
    private void checkAvailability() throws Exception{
        LocalDate dateStart = startDate.getValue();
        LocalTime timeStart = startTime.getValue();

        LocalDate dateEnd = endDate.getValue();
        LocalTime timeEnd = endTime.getValue();

        LocalDateTime start = LocalDateTime.of(dateStart, timeStart);
        LocalDateTime end = LocalDateTime.of(dateEnd, timeEnd);

        Timestamp ts = Timestamp.valueOf(start);
        Timestamp te = Timestamp.valueOf(end);

        System.out.println(ts+" TO "+te);

        this.getAvailableRooms(ts, te);
    }

    @FXML
    public void logout() throws Exception{
        Parent pane = FXMLLoader.load(Main.getFXMLURL("welcome"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    @FXML
    private void bookRoom() throws Exception{
        LocalDate dateStart = startDate.getValue();
        LocalTime timeStart = startTime.getValue();

        LocalDate dateEnd = endDate.getValue();
        LocalTime timeEnd = endTime.getValue();

        DisplayTable pos = (DisplayTable)tableView.getSelectionModel().getSelectedItem();

        String roomID = pos.getRoom();

        LocalDateTime start = LocalDateTime.of(dateStart, timeStart);
        LocalDateTime end = LocalDateTime.of(dateEnd, timeEnd);

        Timestamp ts = Timestamp.valueOf(start);
        Timestamp te = Timestamp.valueOf(end);

        System.out.println("Booking "+roomID+" between "+ts+" and "+te);
        this.addBookedTime(roomID, ts, te);
    }

    private void addBookedTime(String roomID, Timestamp start, Timestamp end){
        String query = "INSERT INTO BookedTimes VALUES(?, ?, ?)";
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, roomID);
            stmt.setTimestamp(2, start);
            stmt.setTimestamp(3, end);
            stmt.execute();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error at addBookedTime()");
        }
    }

    @FXML
    private void navigateToHome() throws Exception{
        //Gets an instance of the screen and sets it as the main stage to display
        Parent pane = FXMLLoader.load(Main.getFXMLURL("home"));
        Scene scene = new Scene(pane);
        Main.getStage().setScene(scene);
    }

    private ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws SQLException {
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("roomId"));
                ent.setCapacity(String.valueOf(rs.getInt("capacity")));
                ent.setRoomType(rs.getString("roomType"));
                System.out.println(rs.getString("roomId"));
                System.out.println(rs.getString("capacity"));
                System.out.println(rs.getString("roomType")); //nope
                entList.add(ent);
            }
            tableView.setItems(entList);
            return entList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<DisplayTable> getAllRecords() throws ClassNotFoundException, SQLException {
        String query = "SELECT roomID, capacity, roomtype FROM ROOMS";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            tableView.setItems(entryList);
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<DisplayTable> getAvailableRooms(Timestamp start, Timestamp end) throws ClassNotFoundException, SQLException{

        String query1 = "SELECT Rooms.ROOMID, CAPACITY, ROOMTYPE FROM BookedTimes RIGHT JOIN Rooms ON (Rooms.roomID) = (BookedTimes.roomID) EXCEPT ( SELECT Rooms.ROOMID, CAPACITY, ROOMTYPE FROM BookedTimes JOIN Rooms ON (Rooms.roomID) = (BookedTimes.roomID) WHERE ((BookedTimes.startTime >= ? AND BOOKEDTIMES.STARTTIME <= ? OR (BookedTimes.endTime <= ? AND BookedTimes.endTime >= ?))))";
        //STARTTIME, ENDTIME, ENDTIME, STARTTIME

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            PreparedStatement stmt = conn.prepareStatement(query1);
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            stmt.setTimestamp(3, end);
            stmt.setTimestamp(4, start);
            ResultSet availRooms = stmt.executeQuery();
            ObservableList<DisplayTable> entryList = getEntryObjects(availRooms);
            tableView.setItems(entryList);
            return entryList;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error trying to get available rooms");
            throw e;
        }

    }

    /**
     * This method initializes the controller and deals with assert statements
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        startDate.setStyle("-jfx-unfocus-color: WHITE;");
        startTime.setStyle("-jfx-unfocus-color: WHITE;");
        endDate.setStyle("-jfx-unfocus-color: WHITE;");
        endTime.setStyle("-jfx-unfocus-color: WHITE;");

        try {
            String str1 = "INSERT INTO Rooms VALUES('CR_1', 19, 'TBD', 'COMP')";
            String str2 = "insert into Rooms values('CR_2', 17, 'TBD', 'COMP')";
            String str3 = "insert into Rooms values('CR_3', 17, 'TBD', 'COMP')";
            String str4 = "insert into Rooms values('CR_4', 19, 'TBD', 'CLASS')";
            String str5 = "insert into Rooms values('CR_5', 25, 'TBD', 'COMP')";
            String str6 = "insert into Rooms values('CR_6', 19, 'TBD', 'CLASS')";
            String str7 = "insert into Rooms values('CR_7', 17, 'TBD', 'COMP')";
            String str8 = "insert into Rooms values('CR_8', 15, 'TBD', 'CLASS')";

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection conn = DriverManager.getConnection(dbPath);
            /*Statement stmt1 = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            Statement stmt3 = conn.createStatement();
            Statement stmt4 = conn.createStatement();
            Statement stmt5 = conn.createStatement();
            Statement stmt6 = conn.createStatement();
            Statement stmt7 = conn.createStatement();
            Statement stmt8 = conn.createStatement();

            stmt1.executeUpdate(str1);
            stmt2.executeUpdate(str2);
            stmt3.executeUpdate(str3);
            stmt4.executeUpdate(str4);
            stmt5.executeUpdate(str5);
            stmt6.executeUpdate(str6);
            stmt7.executeUpdate(str7);
            stmt8.executeUpdate(str8);*/

            ObservableList<DisplayTable> entList = getAllRecords();
            roomId.setCellValueFactory(new PropertyValueFactory<>("Room"));
            capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            roomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
            tableView.setItems(entList);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        assert zoomLvl != null : "fx:id=\"zoomLvl\" was not injected: check your FXML file 'scheduler.fxml'.";
        assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'scheduler.fxml'.";
        assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'scheduler.fxml'.";

        //Adapted from: https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx
        //------------------------------------------------------------------------------------------------
        //Get the bounds of the screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


        Image source = new Image(Main.getResource("/resources/maps/scheduler.jpg"));

        double ratio = source.getWidth() / source.getHeight(); //Get the width to height ratio

        //If the ratio and the actual width and height are not proper, make them a necessary size
        if (500 / ratio < 500) {
            width = 500;
            height = (int) (500 / ratio);
        } else if (500 * ratio < 500) {
            height = 500;
            width = (int) (500 * ratio);
        } else {
            height = 500;
            width = 500;
        }


        image.setImage(source); //Set the image as the source
        System.out.println(imageView.getBoundsInParent().getWidth());

        //Set the proper width and height for the image
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight()-200);
        height = (int) source.getHeight();
        width = (int) source.getWidth();
    }
}
