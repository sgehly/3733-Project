
package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;

import java.io.FileWriter;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
import java.util.Date;

/**
 * The controller class associated with the scheduler
 */
public class Scheduler {
   //Instances of necessary objects
    public static String path;

    //for the randomization purposes of the rooms
    int secondsPassed = 0;

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



    //For popup purposes
    @FXML
    private VBox popup;
    @FXML
    private VBox content;
    @FXML
    private ImageView backgroundImage;



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

    //random rooms that are not bookable but will change color with rng on every update of time and date
    @FXML
    private Pane randomRoom1;
    @FXML
    private Pane randomRoom2;
    @FXML
    private Pane randomRoom3;
    @FXML
    private Pane randomRoom4;
    @FXML
    private Pane randomRoom5;
    @FXML
    private Pane randomRoom6;
    @FXML
    private Pane randomRoom7;
    @FXML
    private Pane randomRoom8;
    @FXML
    private Pane randomRoom9;
    @FXML
    private Pane randomRoom10;
    @FXML
    private Pane randomRoom11;
    @FXML
    private Pane randomRoom12;
    @FXML
    private Pane randomRoom13;
    @FXML
    private Pane randomRoom14;
    @FXML
    private Pane randomRoom15;
    @FXML
    private Pane randomRoom16;
    @FXML
    private Pane randomRoom17;
    @FXML
    private Pane randomRoom18;
    @FXML
    private Pane randomRoom19;
    @FXML
    private Pane randomRoom20;
    @FXML
    private Pane randomRoom21;
    @FXML
    private Pane randomRoom22;
    @FXML
    private Pane randomRoom23;
    @FXML
    private Pane randomRoom24;
    @FXML
    private Pane randomRoom25;
    @FXML
    private Pane randomRoom26;
    @FXML
    private Pane randomRoom27;
    @FXML
    private Pane randomRoom28;
    @FXML
    private Pane randomRoom29;
    @FXML
    private Pane randomRoom30;
    @FXML
    private Pane randomRoom31;
    @FXML
    private Pane randomRoom32;
    @FXML
    private Pane randomRoom33;
    @FXML
    private Pane randomRoom34;
    @FXML
    private Pane randomRoom35;
    @FXML
    private Pane randomRoom36;
    @FXML
    private Pane randomRoom37;
    @FXML
    private Pane randomRoom38;
    @FXML
    private Pane randomRoom39;
    @FXML
    private Pane randomRoom40;
    @FXML
    private Pane randomRoom41;
    @FXML
    private Pane randomRoom42;
    @FXML
    private Pane randomRoom43;
    @FXML
    private Pane randomRoom44;
    @FXML
    private Pane randomRoom45;
    @FXML
    private Pane randomRoom46;
    @FXML
    private Pane randomRoom47;
    @FXML
    private Pane randomRoom48;
    @FXML
    private Pane randomRoom49;
    @FXML
    private Pane randomRoom50;
    @FXML
    private Pane randomRoom51;
    @FXML
    private Pane randomRoom52;
    @FXML
    private Pane randomRoom53;
    @FXML
    private Pane randomRoom54;
    @FXML
    private Pane randomRoom55;
    @FXML
    private Pane randomRoom56;
    @FXML
    private Pane randomRoom57;
    @FXML
    private Pane randomRoom58;
    @FXML
    private Pane randomRoom59;
    @FXML
    private Pane randomRoom60;
    @FXML
    private Pane randomRoom61;
    @FXML
    private Pane randomRoom62;
    @FXML
    private Pane randomRoom63;
    @FXML
    private Pane randomRoom64;
    @FXML
    private Pane randomRoom65;
    @FXML
    private Pane randomRoom66;
    @FXML
    private Pane randomRoom67;
    @FXML
    private Pane randomRoom68;
    @FXML
    private Pane randomRoom69;
    @FXML
    private Pane randomRoom70;

    ArrayList<Pane> randomPanes;

    Node[] focusable = new Node[] {startTime, startDate, endTime, endDate};

    boolean[] available = new boolean[10];
    int[] capacities = new int[10];
    String[] types = new String[10];
    String[] ids = new String[10];

    boolean error = false;

    int selectedRoom = 0;

    /**
     * Checks room availability
     *
     * @throws Exception Throws exception if the room that the user wants to select is already booked for the
     *                   selected time and date
     */
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

        this.getAvailableRooms(ts, te);
    }

    /**
     * Logs user out of the app
     *
     * @throws Exception
     */
    @FXML
    public void logout() throws Exception{
        Main.logOut();
    }

    /**
     * Books a room
     *
     * @throws Exception Throws exception if the selected room cannot be booked
     */
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

        this.addBookedTime(roomID, ts, te);
        this.checkAvailability();
    }

    /**
     * Adds the booked room to the database
     *
     * @param roomID
     * @param start
     * @param end
     */
    private void addBookedTime(String roomID, Timestamp start, Timestamp end){
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        String query = "INSERT INTO BookedTimes VALUES(?, ?, ?, ?)";
        try{
            Connection conn = DBUtils.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, roomID);
            stmt.setString(2, userText.getText().toLowerCase());
            stmt.setTimestamp(3, start);
            stmt.setTimestamp(4, end);
            stmt.execute();
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error at addBookedTime()");
        }
    }

    @FXML
    private void displayPopup(){
        backgroundImage.setEffect(new GaussianBlur());
        backgroundImage.setDisable(true);
        content.setEffect(new GaussianBlur());
        content.setDisable(true);
        popup.setDisable(false);
        popup.setOpacity(1);
    }

    @FXML
    private void hidePopup(){
        backgroundImage.setEffect(null);
        backgroundImage.setDisable(false);
        content.setEffect(null);
        content.setDisable(false);
        popup.setDisable(true);
        popup.setOpacity(0);
    }


    /**
     * Exports the booked rooms table as a CSV
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    private void exportToCsv() throws SQLException,ClassNotFoundException{
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        System.out.println("in print");
        String filename = "bookedRooms.csv";
        this.hidePopup();
        try {
            FileWriter file = new FileWriter(filename);
            Connection conn = DBUtils.getConnection();
            String query = "select * from BOOKEDTIMES";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                file.append(rs.getString(1));
                file.append(',');
                file.append(rs.getString(2));
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

    /**
     * Returns to the home screen
     *
     * @throws Exception
     */
    @FXML
    private void navigateToHome() throws Exception{
        Main.setScene("home");
    }

    /**
     * Goes to the scheduler calendar that displays a list of the rooms booked
     *
     * @throws Exception
     */
    @FXML
    private void navigateToDetails() throws Exception{
        Main.setScene("schedulerList");
    }
    @FXML
    private void navigateToCalendar() throws Exception{
        Main.setScene("CalendarView");
    }

    /**
     * Shows the booked rooms in the database in a table
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private ObservableList<DisplayTable> getEntryObjects(ResultSet rs) throws SQLException {
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("roomId"));
                ent.setCapacity(String.valueOf(rs.getInt("capacity")));
                ent.setRoomType(rs.getString("roomType"));
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

    /**
     * Gets all of the items in the database
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ObservableList<DisplayTable> getAllRecords() throws ClassNotFoundException, SQLException {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        String query = "SELECT roomID, capacity, roomtype FROM ROOMS";
        try {
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ObservableList<DisplayTable> entryList = getEntryObjects(rs);
            tableView2.setItems(entryList);
            conn.close();
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Produces the image of the rooms to be booked
     *
     * @param roomId
     * @param available
     */
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

    /**
     * Shows the available rooms to be booked on the image
     *
     * @param start
     * @param end
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ObservableList<DisplayTable> getAvailableRooms(Timestamp start, Timestamp end) throws ClassNotFoundException, SQLException {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();

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

            Connection conn = DBUtils.getConnection();
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
                    if(allRoomName.equals(availRoomName)){
                        roomIsAvailable = true;
                    }
                }
                ids[index] = allRoomName;
                types[index] = allRooms.getString("roomType");
                capacities[index] = allRooms.getInt("capacity");
                available[index] = roomIsAvailable;
                index++;

                processImage(allRoomName, roomIsAvailable);
                availRooms.beforeFirst();
            }
            this.switchToRoom(this.selectedRoom);
            conn.close();
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
        this.startRandomization();
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        this.initializePaneList();
        this.randomizeRandomRoomColors();
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        //for popup purposes
        popup.setOpacity(0);
        popup.setDisable(true);

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

        Connection conn = DBUtils.getConnection();

//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(strd);}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str1);}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str2);}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str3);}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str4);conn.close();}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str5);conn.close();}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str6);conn.close();}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str7);conn.close();}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str8);conn.close();}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str9);conn.close();}catch(Exception e){}
//        try{Statement stmt1 = conn.createStatement();stmt1.executeUpdate(str10);conn.close();}catch(Exception e){}

        try{
            Statement stmt1 = conn.createStatement();
            stmt1.executeUpdate(strd);

            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate(str1);

            Statement stmt3 = conn.createStatement();
            stmt3.executeUpdate(str2);

            Statement stmt4 = conn.createStatement();
            stmt4.executeUpdate(str3);

            Statement stmt5 = conn.createStatement();
            stmt5.executeUpdate(str4);

            Statement stmt6 = conn.createStatement();
            stmt6.executeUpdate(str5);

            Statement stmt7 = conn.createStatement();
            stmt7.executeUpdate(str6);

            Statement stmt8 = conn.createStatement();
            stmt8.executeUpdate(str7);

            Statement stmt9 = conn.createStatement();
            stmt9.executeUpdate(str8);

            Statement stmt10 = conn.createStatement();
            stmt10.executeUpdate(str9);

            Statement stmt11 = conn.createStatement();
            stmt11.executeUpdate(str10);


            conn.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }

        startDate.setStyle("-jfx-unfocus-color: WHITE;");
        startTime.setStyle("-jfx-unfocus-color: WHITE;");
        endDate.setStyle("-jfx-unfocus-color: WHITE;");
        endTime.setStyle("-jfx-unfocus-color: WHITE;");

        try{
            System.out.println("printing inside initialize");
            roomidCol.setCellValueFactory(new PropertyValueFactory<>("Room"));
            starttimeCol.setCellValueFactory(new PropertyValueFactory<>("starttime"));
            endtimeCol.setCellValueFactory(new PropertyValueFactory<>("endtime"));
            initWithType();

            startDate.setValue(LocalDate.now());
            endDate.setValue(LocalDate.now().plus(1, ChronoUnit.DAYS));

            startTime.setValue(LocalTime.now());
            endTime.setValue(LocalTime.now().plus(1, ChronoUnit.HOURS));

            startDate.valueProperty().addListener((ov, oldValue, newValue) -> {try{this.checkAvailability();this.randomizeRandomRoomColors();}catch(Exception e){} });
            startTime.valueProperty().addListener((ov, oldValue, newValue) -> {try{this.checkAvailability();this.randomizeRandomRoomColors();}catch(Exception e){} });
            endDate.valueProperty().addListener((ov, oldValue, newValue) -> {try{this.checkAvailability();this.randomizeRandomRoomColors();}catch(Exception e){} });
            endTime.valueProperty().addListener((ov, oldValue, newValue) -> {try{this.checkAvailability();this.randomizeRandomRoomColors();}catch(Exception e){} });


            this.checkAvailability();
        }catch(Exception e){e.printStackTrace();};

        assert zoomLvl != null : "fx:id=\"zoomLvl\" was not injected: check your FXML file 'scheduler.fxml'.";
        assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'scheduler.fxml'.";
        assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'scheduler.fxml'.";
    }

    /**
     * Randomizes the availability of the workspaces
     */
    private void startRandomization() {
        Timer timer = new Timer();
        TimerTask task = new Helper(this);
        timer.schedule(task, 2000, 5000);
    }

    private void initializePaneList() {
        randomPanes = new ArrayList<>();
        randomPanes.add(randomRoom1);
        randomPanes.add(randomRoom2);
        randomPanes.add(randomRoom3);
        randomPanes.add(randomRoom4);
        randomPanes.add(randomRoom5);
        randomPanes.add(randomRoom6);
        randomPanes.add(randomRoom7);
        randomPanes.add(randomRoom8);
        randomPanes.add(randomRoom9);
        randomPanes.add(randomRoom10);
        randomPanes.add(randomRoom11);
        randomPanes.add(randomRoom12);
        randomPanes.add(randomRoom13);
        randomPanes.add(randomRoom14);
        randomPanes.add(randomRoom15);
        randomPanes.add(randomRoom16);
        randomPanes.add(randomRoom17);
        randomPanes.add(randomRoom18);
        randomPanes.add(randomRoom19);
        randomPanes.add(randomRoom20);
        randomPanes.add(randomRoom21);
        randomPanes.add(randomRoom22);
        randomPanes.add(randomRoom23);
        randomPanes.add(randomRoom24);
        randomPanes.add(randomRoom25);
        randomPanes.add(randomRoom26);
        randomPanes.add(randomRoom27);
        randomPanes.add(randomRoom28);
        randomPanes.add(randomRoom29);
        randomPanes.add(randomRoom30);
        randomPanes.add(randomRoom31);
        randomPanes.add(randomRoom32);
        randomPanes.add(randomRoom33);
        randomPanes.add(randomRoom34);
        randomPanes.add(randomRoom35);
        randomPanes.add(randomRoom36);
        randomPanes.add(randomRoom37);
        randomPanes.add(randomRoom38);
        randomPanes.add(randomRoom39);
        randomPanes.add(randomRoom40);
        randomPanes.add(randomRoom41);
        randomPanes.add(randomRoom42);
        randomPanes.add(randomRoom43);
        randomPanes.add(randomRoom44);
        randomPanes.add(randomRoom45);
        randomPanes.add(randomRoom46);
        randomPanes.add(randomRoom47);
        randomPanes.add(randomRoom48);
        randomPanes.add(randomRoom49);
        randomPanes.add(randomRoom50);
        randomPanes.add(randomRoom51);
        randomPanes.add(randomRoom52);
        randomPanes.add(randomRoom53);
        randomPanes.add(randomRoom54);
        randomPanes.add(randomRoom55);
        randomPanes.add(randomRoom56);
        randomPanes.add(randomRoom57);
        randomPanes.add(randomRoom58);
        randomPanes.add(randomRoom59);
        randomPanes.add(randomRoom60);
        randomPanes.add(randomRoom61);
        randomPanes.add(randomRoom62);
        randomPanes.add(randomRoom63);
        randomPanes.add(randomRoom64);
        randomPanes.add(randomRoom65);
        randomPanes.add(randomRoom66);
        randomPanes.add(randomRoom67);
        randomPanes.add(randomRoom68);
        randomPanes.add(randomRoom69);
        randomPanes.add(randomRoom70);
    }

    /**
     * Randomizes the colors of the workspaces on the image
     */
    public void randomizeRandomRoomColors() {
        Random random = new Random();
        double randomFloat = 0;
        for(int i = 0; i < randomPanes.size(); i++){
            randomFloat = random.nextDouble();
            if(randomFloat > .5){
                randomPanes.get(i).setStyle("-fx-background-color:  #8b1111");
            }
            else{
                randomPanes.get(i).setStyle("-fx-background-color: #118b56");
            }
        }
    }

    /**
     * Switches the labels to the selected room
     *
     * @param id
     */
    private void switchToRoom(int id){
        if(error) return;
        this.selectedRoom = id;
        title.setText(ids[id]);
        subtitle.setText(types[id]+" | CAPACITY: "+capacities[id]);
        availability.setText(available[id] ? "ROOM AVAILABLE" : "ROOM UNAVAILABLE");
        availability.setStyle("-fx-text-fill: "+(available[id] ? "#20bf6b" : "#eb3b5a")+";");
        bookButton.setDisable(!available[id]);
        bookButton.setStyle("-fx-opacity: "+(available[id] ? 1 : 0.5)+";");

        try{
            ObservableList<DisplayTable> getRec = getAllRecords2();
            tableView2.setItems(getRec);
        }catch(Exception e){
            e.printStackTrace();
        }
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

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private static ObservableList<DisplayTable> getEntryObjects2(ResultSet rs) throws SQLException {
        //The list we will populate
        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                //Get the correct entries from the text fields and add to the list so that we can add it to the database
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("Roomid"));
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

    /**
     *
     */
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
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        //Get the query from the database
        String query = "SELECT * FROM BOOKEDTIMES WHERE ROOMID=?";

        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, ids[this.selectedRoom]);
            ResultSet rs = stmt.executeQuery();
            ObservableList<DisplayTable> entryList = getEntryObjects2(rs);
            tableView2.setItems(entryList);
            conn.close();
            return entryList;
        } catch (SQLException e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }
}

