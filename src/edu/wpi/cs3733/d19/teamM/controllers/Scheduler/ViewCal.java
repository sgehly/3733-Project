package edu.wpi.cs3733.d19.teamM.controllers.Scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.text.html.parser.Entity;


public class ViewCal {


    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Text userText;

    @FXML
    public CalendarView calendar = new CalendarView();

    @FXML
    public void navigateToWelcome() throws Exception {
        Main.setScene("welcome");
    }


    @FXML
    public MenuItem backMenuItem;

    Calendar CR_1 = new Calendar("Classroom 1");

    Calendar CR_2 = new Calendar("Classroom 2");
    Calendar CR_3 = new Calendar("Classroom 3");
    Calendar CR_4 = new Calendar("Classroom 4");
    Calendar CR_5 = new Calendar("Classroom 5");
    Calendar CR_6 = new Calendar("Classroom 6");
    Calendar CR_7 = new Calendar("Classroom 7");
    Calendar CR_8 = new Calendar("Classroom 8");
    Calendar CR_9 = new Calendar("Classroom 9");
    Calendar CR_10 = new Calendar("Theater");

    CalendarSource myCalendarSource = new CalendarSource("MY CALENDARS");



    public void generateEntries() throws SQLException{

        myCalendarSource.getCalendars().addAll(CR_1,CR_2,CR_3,CR_4,CR_5,CR_6,CR_7,CR_8,CR_9,CR_10);
        calendar.getCalendarSources().addAll(myCalendarSource);

        CR_1.setStyle(Style.STYLE1);
        CR_2.setStyle(Style.STYLE2);
        CR_3.setStyle(Style.STYLE3);
        CR_4.setStyle(Style.STYLE4);
        CR_5.setStyle(Style.STYLE5);
        CR_6.setStyle(Style.STYLE6);
        CR_7.setStyle(Style.STYLE7);
        CR_8.setStyle(Style.STYLE1);
        CR_9.setStyle(Style.STYLE2);
        CR_10.setStyle(Style.STYLE3);





        String query = "Select ROOMID ,STARTTIME ,ENDTIME from BookedTimes";

        try {

            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                System.out.println("in while");
                System.out.println(rs.getString(1));

                Interval interval = new Interval(rs.getDate(2).toLocalDate(),rs.getTime(2).toLocalTime(),rs.getDate(3).toLocalDate(),rs.getTime(3).toLocalTime());
                Entry ent = new Entry("Room:" + rs.getString(1),interval);
                if (rs.getString(1).equals("CR_1")){
                    CR_1.addEntries(ent);
                }

                if (rs.getString(1).equals("CR_2")){
                    CR_2.addEntries(ent);
                }
                if (rs.getString(1).equals("CR_3")){
                    CR_3.addEntries(ent);
                }
                if (rs.getString(1).equals("CR_4")){
                    CR_4.addEntries(ent);
                }
                if (rs.getString(1).equals("CR_5")){
                    CR_5.addEntries(ent);
                }
                if (rs.getString(1).equals("CR_6")){
                    CR_6.addEntries(ent);
                }
                if (rs.getString(1).equals("CR_7")){
                    CR_7.addEntries(ent);
                }
                if (rs.getString(1).equals("CR_8")){
                    CR_8.addEntries(ent);
                }
                if (rs.getString(1).equals("CR_9")){
                    CR_9.addEntries(ent);
                }
                if (rs.getString(1).equals("CR_10")){
                    CR_9.addEntries(ent);
                }
            }
            conn.close();
        }

        catch (SQLException e) {

            System.out.println("Error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
    }


    @FXML
    private void navigateToHome() throws Exception{
        Main.setScene("home");
    }

    @FXML
    public void logout() throws Exception{
        Main.logOut();
    }

    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("scheduler");
    }



    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws SQLException {

            generateEntries();


       // new Clock(lblClock, lblDate);
        //userText.setText(User.getUsername());
    }
}

