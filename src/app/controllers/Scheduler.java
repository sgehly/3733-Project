
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

public class Scheduler {
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

    @FXML
    private TableView tableView = new TableView();

    @FXML
    private TableColumn<DisplayTable,String> roomId = new TableColumn("roomId");;

    @FXML
    private TableColumn<DisplayTable,String> details = new TableColumn("details");;

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

        LocalDate dateEnd = startDate.getValue();
        LocalTime timeEnd = startTime.getValue();

        //Database operations to get available rooms from the database;
        System.out.println(dateStart+":"+timeStart+"-"+dateEnd+":"+timeEnd);
    }

    @FXML
    private void bookRoom() throws Exception{
        LocalDate dateStart = startDate.getValue();
        LocalTime timeStart = startTime.getValue();

        LocalDate dateEnd = startDate.getValue();
        LocalTime timeEnd = startTime.getValue();

        TablePosition pos = (TablePosition)tableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();

        Object item = tableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        String data = (String) col.getCellObservableValue(item).getValue();

        System.out.println(dateStart+":"+timeStart+"-"+dateEnd+":"+timeEnd+"-"+data);
    }


    @FXML
    private void navigateToHome() throws Exception{
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
                ent.setNotes(rs.getString("details"));
                System.out.println(rs.getString("details"));
                System.out.println(rs.getString("roomId"));
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
        String query = "SELECT * FROM ROOMS";
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

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        try {
            ObservableList<DisplayTable> entList = getAllRecords();
            roomId.setCellValueFactory(new PropertyValueFactory<>("Room"));
            details.setCellValueFactory(new PropertyValueFactory<>("Notes"));
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
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        Image source = null;
        try {
            String path = getClass().getResource("/resources/maps/scheduler.jpg").toString().replace("file:","");
            System.out.println(path);
            source = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        double ratio = source.getWidth() / source.getHeight();

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
        image.setImage(source);
        System.out.println(imageView.getBoundsInParent().getWidth());
        image.setFitWidth(primaryScreenBounds.getWidth());
        image.setFitHeight(primaryScreenBounds.getHeight()-200);
        height = (int) source.getHeight();
        width = (int) source.getWidth();
        System.out.println("height = " + height + "\nwidth = " + width);
        HBox zoom = new HBox(10);
        zoom.setAlignment(Pos.CENTER);
    }
}
