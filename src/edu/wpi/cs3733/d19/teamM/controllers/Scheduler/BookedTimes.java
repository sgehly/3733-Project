package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import java.sql.*;

public class BookedTimes {

    @FXML
    private Text userName;

    @FXML
    private javafx.scene.control.Label lblClock;

    @FXML
    private javafx.scene.control.Label lblDate;

    @FXML
    private javafx.scene.control.TableView table = new TableView();

    @FXML
    private javafx.scene.control.TableColumn<DisplayTable,String> roomName;

    @FXML
    private javafx.scene.control.TableColumn<DisplayTable,String> startTime;

    @FXML
    private javafx.scene.control.TableColumn<DisplayTable,String> endTime;

    @FXML
    private TableColumn<DisplayTable,String> user;


    @FXML
    private Button deleteButton;

    @FXML
    private void callFun(){
        try{
            Connection conn = new DatabaseUtils().getConnection();
            String sql = "select accountint from users where username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName.getText().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println("inside");
                int accountint = rs.getInt("accountint");
                System.out.println(accountint);

                if (isAdmin()) {
                    showAll();
                    System.out.println("user is an admin ");
                 } else {
                    showUser(userName.getText().toLowerCase());

                }
             }
            conn.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("BOOOO");
    }

    private void showUser(String user){

        try{
            System.out.println("in show user ");
            Connection conn = new DatabaseUtils().getConnection();
            String sql = "select * from bookedtimes where users = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            toList(rs);
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void showAll(){
        try{
            Connection conn = new DatabaseUtils().getConnection();
            String sql = "select * from bookedtimes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            toList(rs);
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void toList(ResultSet rs) throws Exception{

        ObservableList<DisplayTable> entList = FXCollections.observableArrayList();
        try {
            while (rs.next()) {
                DisplayTable ent = new DisplayTable();
                ent.setRoom(rs.getString("ROOMID"));
                ent.setUser(rs.getString("users"));
                ent.setStartTime(rs.getString("Starttime"));
                ent.setEndTime(rs.getString("endtime"));
                entList.add(ent);
            }
            setTable(entList);



        } catch (Exception e) {
            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }


    public void setTable(ObservableList<DisplayTable> o){
        table.setItems(o);
        roomName.setCellValueFactory(new PropertyValueFactory<>("room"));
        user.setCellValueFactory(new PropertyValueFactory<>("user"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("starttime"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endtime"));
    }



    @FXML
    private void deleteFromList(){

        DisplayTable dT = curItem;

        try{
            Connection conn = new DatabaseUtils().getConnection();
            String sql = "DELETE from bookedtimes where ROOMID = ? and STARTTIME = ? and ENDTIME = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dT.getRoom());
            stmt.setString(2, dT.starttimeProperty().get());
            stmt.setString(3, dT.endtimeProperty().get());
            stmt.executeUpdate();
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        table.getItems().clear();
        showAll();
    }


    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    @FXML
    public void navigateBack() throws Exception {
        Main.setScene("scheduler");
    }

    private DisplayTable curItem;

    private void updateSelectedItem(Object val){
        curItem = (DisplayTable) val;
    }


    public boolean isAdmin(){
        try{
            Connection conn = new DatabaseUtils().getConnection();
            String sql = "select accountint from users where username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName.getText().toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("inside");
                int accountint = rs.getInt("accountint");
                System.out.println(accountint);

                if (accountint == 100) {
                    return true;
                }
            }
            conn.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    return false;

    };


    @FXML
    void initialize() throws SQLException {


        table.getSelectionModel().selectedItemProperty().addListener((changed, oldVal, newVal) ->
            updateSelectedItem(newVal)
        );

        new Clock(lblClock, lblDate);
        System.out.println("First Point");
        userName.setText(User.getUsername());
        callFun();

        //showAll();


        this.setButton();
    }

    private void setButton() {
        if(isAdmin()){
            deleteButton.setVisible(true);
        }
        else{
            deleteButton.setVisible(false);
        }
    }


}