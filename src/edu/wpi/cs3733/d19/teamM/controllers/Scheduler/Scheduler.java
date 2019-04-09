
package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;

import java.io.FileWriter;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.beans.value.ChangeListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.stage.Stage;
import edu.wpi.cs3733.d19.teamM.controllers.Scheduler.DisplayTable;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * The controller class associated with the scheduler
 */
public class Scheduler {
   //Instances of necessary objects
    public static String path;


    @FXML
    private AnchorPane root;

    static double offSetX,offSetY,zoomlvl;

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="zoomLvl"
    private Slider zoomLvl; // Value injected by FXMLLoader

    @FXML
    private Text userText;

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
    private TableView tableView2 = new TableView();

    @FXML
    private TableColumn<DisplayTable,Integer> roomidCol = new TableColumn("room");

    @FXML
    private TableColumn<DisplayTable,Integer> starttimeCol = new TableColumn("starttime");

    @FXML
    private TableColumn<DisplayTable,Integer> endtimeCol = new TableColumn("endtime");

//    @FXML
//    private TableColumn<DisplayTable,String> roomId = new TableColumn("roomId");
//
//    @FXML
//    private TableColumn<DisplayTable,String> capacity = new TableColumn("capacity");
//
//    @FXML
//    private TableColumn<DisplayTable,String> roomType = new TableColumn("roomType");

    @FXML
    private JFXDatePicker startDate = new JFXDatePicker();

    @FXML
    private JFXTimePicker startTime = new JFXTimePicker();;

    @FXML
    private JFXDatePicker endDate = new JFXDatePicker();

    @FXML
    private JFXTimePicker endTime = new JFXTimePicker();

    @FXML
    private Label title = new Label();

    @FXML
    private Label subtitle = new Label();

    @FXML
    private Label availability = new Label();

    @FXML
    private Button bookButton = new Button();

    @FXML
    private Button exp = new Button();

    @FXML
    private ImageView room1 = new ImageView();
    @FXML
    private ImageView room2 = new ImageView();
    @FXML
    private ImageView room3 = new ImageView();
    @FXML
    private ImageView room4 = new ImageView();
    @FXML
    private ImageView room5 = new ImageView();
    @FXML
    private ImageView room6 = new ImageView();
    @FXML
    private ImageView room7 = new ImageView();
    @FXML
    private ImageView room8 = new ImageView();
    @FXML
    private ImageView room9 = new ImageView();
    @FXML
    private ImageView room10 = new ImageView();


    Node[] focusable = new Node[] {startTime, startDate, endTime, endDate};

    boolean[] available = new boolean[10];
    int[] capacities = new int[10];
    String[] types = new String[10];
    String[] ids = new String[10];

    boolean error = false;

    int selectedRoom = 0;



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
        Main.setScene("welcome");
    }

    @FXML
    private void bookRoom() throws Exception{
        LocalDate dateStart = startDate.getValue();
        LocalTime timeStart = startTime.getValue();

        LocalDate dateEnd = endDate.getValue();
        LocalTime timeEnd = endTime.getValue();

        DisplayTable pos = (DisplayTable)tableView.getSelectionModel().getSelectedItem();

        String roomID = ids[selectedRoom];

        LocalDateTime start = LocalDateTime.of(dateStart, timeStart);
        LocalDateTime end = LocalDateTime.of(dateEnd, timeEnd);

        Timestamp ts = Timestamp.valueOf(start);
        Timestamp te = Timestamp.valueOf(end);

        System.out.println("Booking "+roomID+" between "+ts+" and "+te);
        this.addBookedTime(roomID, ts, te);
        this.checkAvailability();
    }

    private void addBookedTime(String roomID, Timestamp start, Timestamp end){
        String query = "INSERT INTO BookedTimes VALUES(?, ?, ?)";
        try{
            Connection conn = new DatabaseUtils().getConnection();
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
    private void exportToCsv(ActionEvent event) throws SQLException,ClassNotFoundException{
        System.out.println("in print");
        String filename = "bookedRooms.csv";
        try {
            FileWriter file = new FileWriter(filename);
            Connection conn = new DatabaseUtils().getConnection();
            String query = "select * from BOOKEDTIMES";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                file.append(rs.getString(1));
                file.append(',');
                file.append(rs.getString(2));
                System.out.println(rs.getString(2));
                file.append(',');
                file.append(rs.getString(3));
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
    private void navigateToHome() throws Exception{
        Main.setScene("home");
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
            tableView2.setItems(entList);
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
            Connection conn = new DatabaseUtils().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            tableView2.setItems(entryList);
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }


    private void processImage(String roomId, boolean available){

        char fileSuffix = available ? 'a' : 't';
        String file = "/resources/maps/room"+roomId.split("_")[1]+fileSuffix+".png";

        Image imageFile = new Image(Main.getResource(file));
        switch(roomId){
            case "CR_1": room1.setImage(imageFile);
            case "CR_2": room2.setImage(imageFile);
            case "CR_3": room3.setImage(imageFile);
            case "CR_4": room4.setImage(imageFile);
            case "CR_5": room5.setImage(imageFile);
            case "CR_6": room6.setImage(imageFile);
            case "CR_7": room7.setImage(imageFile);
            case "CR_8": room8.setImage(imageFile);
            case "CR_9": room9.setImage(imageFile);
            case "CR_10": room10.setImage(imageFile);
        }

    }

    public ObservableList<DisplayTable> getAvailableRooms(Timestamp start, Timestamp end) throws ClassNotFoundException, SQLException {

        String query2 = "SELECT ROOMID, CAPACITY, ROOMTYPE FROM Rooms";
        String query1 = "SELECT Rooms.ROOMID, CAPACITY, ROOMTYPE FROM BookedTimes RIGHT JOIN Rooms ON (Rooms.roomID) = (BookedTimes.roomID) EXCEPT ( SELECT Rooms.ROOMID, CAPACITY, ROOMTYPE FROM BookedTimes JOIN Rooms ON (Rooms.roomID) = (BookedTimes.roomID) WHERE ((BookedTimes.startTime >= ? AND BOOKEDTIMES.STARTTIME <= ? OR (BookedTimes.endTime <= ? AND BookedTimes.endTime >= ?))))";
        //STARTTIME, ENDTIME, ENDTIME, STARTTIME

        ImageView[] rooms = new ImageView[] {room1,room2,room3,room4,room5,room6,room7,room8,room9,room10};

        if (end.before(start)) { //if the end time comes before the start time, show error pop up
            for(int i=0;i<rooms.length;i++){rooms[i].setVisible(false);}
            title.setText("Error");
            error = true;
            subtitle.setText("End Date comes before Start Date");
            availability.setText("");
            title.setStyle("-fx-text-fill: "+("#eb3b5a")+";");
            bookButton.setDisable(true);
            bookButton.setStyle("-fx-opacity: "+(0.5)+";");
        } else { //else, run as usual
            for(int i=0;i<rooms.length;i++){rooms[i].setVisible(true);}
            title.setStyle("-fx-text-fill: "+("#fff")+";");
            error = false;
            try {

            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            stmt.setTimestamp(3, end);
            stmt.setTimestamp(4, start);
            ResultSet availRooms = stmt.executeQuery();
            ResultSet allRooms = conn.createStatement().executeQuery(query2);

            int index = 0;

            while(allRooms.next()){

                boolean roomIsAvailable = false;

                String allRoomName = allRooms.getString("ROOMID");

                while(availRooms.next()){
                    String availRoomName = availRooms.getString("ROOMID");
                    System.out.println(availRoomName+"-"+allRoomName);
                    if(allRoomName.equals(availRoomName)){
                        roomIsAvailable = true;
                    }
                }
                ids[index] = allRoomName;
                types[index] = allRooms.getString("roomType");
                capacities[index] = allRooms.getInt("capacity");
                available[index] = roomIsAvailable;
                index++;

                System.out.println(allRoomName+":"+roomIsAvailable);
                processImage(allRoomName, roomIsAvailable);
                availRooms.beforeFirst();
            }

            this.switchToRoom(this.selectedRoom);

            } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error trying to get available rooms");
            throw e;
            }
        }
        return null;
    }

    /**
     * This method initializes the controller and deals with assert statements
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize(){

        String strd = "DELETE FROM ROOMS";
        String str1 = "INSERT INTO Rooms VALUES('CR_1', 19, 'TBD', 'COMP')";
        String str2 = "insert into Rooms values('CR_2', 17, 'TBD', 'COMP')";
        String str3 = "insert into Rooms values('CR_3', 17, 'TBD', 'COMP')";
        String str4 = "insert into Rooms values('CR_4', 19, 'TBD', 'CLASS')";
        String str5 = "insert into Rooms values('CR_5', 25, 'TBD', 'COMP')";
        String str6 = "insert into Rooms values('CR_6', 19, 'TBD', 'CLASS')";
        String str7 = "insert into Rooms values('CR_7', 17, 'TBD', 'COMP')";
        String str8 = "insert into Rooms values('CR_8', 15, 'TBD', 'CLASS')";
        String str9 = "insert into Rooms values('CR_9', 15, 'TBD', 'CLASS')";
        String str10 = "insert into Rooms values('CR_10', 15, 'TBD', 'CLASS')";

        Connection conn = new DatabaseUtils().getConnection();

        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(strd);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str1);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str2);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str3);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str4);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str5);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str6);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str7);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str8);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str9);}catch(Exception e){}
        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str10);}catch(Exception e){}

        new Clock(lblClock, lblDate);

        userText.setText(User.getUsername());

        startDate.setStyle("-jfx-unfocus-color: WHITE;");
        startTime.setStyle("-jfx-unfocus-color: WHITE;");
        endDate.setStyle("-jfx-unfocus-color: WHITE;");
        endTime.setStyle("-jfx-unfocus-color: WHITE;");


        try{
            System.out.println("printing inside initialize");
           // initWithType();
           // ObservableList<DisplayTable> entList = getAllRecords2();
            roomidCol.setCellValueFactory(new PropertyValueFactory<>("Room"));
            System.out.println(" the fucking printed room is " + new PropertyValueFactory<>("Room"));
            starttimeCol.setCellValueFactory(new PropertyValueFactory<>("starttime"));
            endtimeCol.setCellValueFactory(new PropertyValueFactory<>("endtime"));
            //tableView2.setItems(entList);
            initWithType();

            startDate.setValue(LocalDate.now());
            endDate.setValue(LocalDate.now().plus(1, ChronoUnit.DAYS));

            startTime.setValue(LocalTime.now());
            endTime.setValue(LocalTime.now().plus(1, ChronoUnit.HOURS));

            startDate.valueProperty().addListener((ov, oldValue, newValue) -> {try{this.checkAvailability();}catch(Exception e){} });
            startTime.valueProperty().addListener((ov, oldValue, newValue) -> {try{this.checkAvailability();}catch(Exception e){} });
            endDate.valueProperty().addListener((ov, oldValue, newValue) -> {try{this.checkAvailability();}catch(Exception e){} });
            endTime.valueProperty().addListener((ov, oldValue, newValue) -> {try{this.checkAvailability();}catch(Exception e){} });


            this.checkAvailability();
            //initWithType();
        }catch(Exception e){e.printStackTrace();};
        //initWithType();
//        roomidCol.setCellValueFactory(new PropertyValueFactory<>("room"));
//        System.out.println(new PropertyValueFactory<>("room"));
//        starttimeCol.setCellValueFactory(new PropertyValueFactory<>("starttime"));
//        endtimeCol.setCellValueFactory(new PropertyValueFactory<>("endtime"));
        //initWithType();
        assert zoomLvl != null : "fx:id=\"zoomLvl\" was not injected: check your FXML file 'scheduler.fxml'.";
        assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'scheduler.fxml'.";
        assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'scheduler.fxml'.";

    }

    private void switchToRoom(int id){
        if(error) return;
        this.selectedRoom = id;
        title.setText(ids[id]);
        subtitle.setText(types[id]+" | CAPACITY: "+capacities[id]);
        availability.setText(available[id] ? "ROOM AVAILABLE" : "ROOM UNAVAILABLE");
        availability.setStyle("-fx-text-fill: "+(available[id] ? "#20bf6b" : "#eb3b5a")+";");
        bookButton.setDisable(!available[id]);
        bookButton.setStyle("-fx-opacity: "+(available[id] ? 1 : 0.5)+";");
    }


    @FXML
    private void switchToRoom1(){switchToRoom(0);};

    @FXML
    private void switchToRoom2(){switchToRoom(1);};

    @FXML
    private void switchToRoom3(){switchToRoom(2);};

    @FXML
    private void switchToRoom4(){switchToRoom(3);};

    @FXML
    private void switchToRoom5(){switchToRoom(4);};

    @FXML
    private void switchToRoom6(){switchToRoom(5);};

    @FXML
    private void switchToRoom7(){switchToRoom(6);};

    @FXML
    private void switchToRoom8(){switchToRoom(7);};

    @FXML
    private void switchToRoom9(){switchToRoom(8);};

    @FXML
    private void switchToRoom10(){switchToRoom(9);};



    private static ObservableList<DisplayTable> getEntryObjects2(ResultSet rs) throws SQLException {
        //The list we will populate
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                //Get the correct entries from the text fields and add to the list so that we can add it to the database
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("Roomid"));
                System.out.println("The fuckin room is " +rs.getString("Roomid"));
                ent.setStartTime(rs.getString("starttime"));
                ent.setEndTime(rs.getString("endtime"));
                entList.add(ent);
            }
            return entList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }


    private void initWithType(){

        try{
            ObservableList<DisplayTable> getRec = getAllRecords2();
            //getAllRecords2();
            tableView2.setItems(getRec);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * This method gets all the records from the database so that they can be added to the display on the screen
     * @return ObservableList<DisplayTable>: The List of records that we want to actually display on the screen from the service requests
     * @throws ClassNotFoundException: If classes are not found
     * @throws SQLException: Any issues with the database
     */
    public ObservableList<DisplayTable> getAllRecords2() throws ClassNotFoundException, SQLException {
        //Get the query from the database
        String query = "SELECT * FROM BOOKEDTIMES";

        try {
            Connection conn = new DatabaseUtils().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<DisplayTable> entryList = getEntryObjects2(rs);
            tableView2.setItems(entryList);

            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }
}

