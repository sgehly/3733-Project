package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.nio.channels.SelectableChannel;
import java.sql.*;

public class BookedTimes {

    @FXML
    private Label userText;

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
    private void callFun(){
        String user = userText.getText();
        Connection conn = new DatabaseUtils().getConnection();
        String sql = "select accountint from users where username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userText.getText());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                int accountint = rs.getInt("accountint");
                if(accountint == 100){
                    showAll();
                }
                else{
                    showUser(rs.getString("username"));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    private void showUser(String user){
        try{
            Connection conn = new DatabaseUtils().getConnection();
            String sql = "select * from bookedtimes where users = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            toList(rs);
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

    private void setTable(ObservableList<DisplayTable> ol){
        table.setItems(ol);
        roomName.setCellValueFactory(new PropertyValueFactory<>("room"));
        user.setCellValueFactory(new PropertyValueFactory<>("user"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("starttime"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endtime"));
    }

    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    @FXML
    public void navigateBack() throws Exception {
        Main.setScene("scheduler");
    }


    @FXML
    void initialize() throws SQLException {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
    }

}