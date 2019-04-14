package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;

public class GenerateReport implements Initializable {



    @FXML
    private Text userText;


    @FXML
    private CategoryAxis X;

    @FXML
    private NumberAxis Y;

    @FXML
    private TextField end;

    @FXML
    private TextField Start;

    @FXML
    private TextField Typeofrequest;

    @FXML
    private CheckBox it;

    @FXML
    private CheckBox sanitation;

    @FXML
    private CheckBox security;

    @FXML
    private CheckBox language;

    @FXML
    private CheckBox flowers;


    @FXML
    private BarChart<?, ?> chart;


    @FXML
    private javafx.scene.control.Label lblClock;

    @FXML
    private javafx.scene.control.Label lblDate;


    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     *
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     *
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateToHome() throws Exception {
        Main.setScene("serviceRequests");
    }


    @FXML
    private void generateReport() {
        System.out.println("Trying to make request");
        XYChart.Series set1 = new XYChart.Series<>();
        set1.getData().clear();
        //for it services
        if (it.isSelected()) {
            int size = 0;
            try {
                Connection conn = new DatabaseUtils().getConnection();
                String query = "select count(*) from REQUESTINPROGRESS where TYPE = 'it'";

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    size = rs.getInt(1);
                    System.out.println(size);


                }
                set1.getData().add(new XYChart.Data("IT", size));
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //For sanitation
        if (sanitation.isSelected()) {
            int s = 0;
            try {
                Connection conn = new DatabaseUtils().getConnection();
                String query = "select count(*) from REQUESTINPROGRESS where TYPE = 'sanitation'";

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    s = rs.getInt(1);


                }
                set1.getData().add(new XYChart.Data("SANITATION", s));
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (security.isSelected()) {
            int s = 0;
            try {
                Connection conn = new DatabaseUtils().getConnection();
                String query = "select count(*) from REQUESTINPROGRESS where TYPE = 'security'";

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    s = rs.getInt(1);


                }
                set1.getData().add(new XYChart.Data("SECURITY", s));
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (language.isSelected()) {
            int s = 0;
            try {
                Connection conn = new DatabaseUtils().getConnection();
                String query = "select count(*) from REQUESTINPROGRESS where TYPE = 'interpreter'";

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    s = rs.getInt(1);


                }
                set1.getData().add(new XYChart.Data("LANGUAGE", s));
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (flowers.isSelected()) {
            int s = 0;
            try {
                Connection conn = new DatabaseUtils().getConnection();
                String query = "select count(*) from REQUESTINPROGRESS where TYPE = 'flowers'";

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    s = rs.getInt(1);


                }
                set1.getData().add(new XYChart.Data("FLOWERS", s));
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        chart.getData().addAll(set1);


    }


    //"SELECT cnt as count(TYPE)from REQUESTLOG where (SELECT * from REQUESTLOG where TYPE = 'it')";


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new Clock(lblClock, lblDate);
        //userText.setText(User.getUsername());
        userText.setText("");
    }
}

