package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Node;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ITRequests implements Initializable {

    @FXML
    private Text errorMessage;

    @FXML
    private Text userText;

    @FXML
    private JFXTextField type;

    //Text field for room location input
    @FXML
    private JFXComboBox<String> room;

    @FXML
    private JFXCheckBox urgent;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Button submitReuqest;

    @FXML
    private ListView listEmployees;

    @FXML
    private javafx.scene.control.Label lblClock;

    @FXML
    private javafx.scene.control.Label lblDate;

    String[] it = {"Laptop","Desktop Computer","WiFi","Password/Login","Printer","Phone","Software"};

    @FXML
    private Button clrBtn;

    @FXML
    private Button desktop;
    @FXML
    private Button laptop;
    @FXML
    private Button passLog;
    @FXML
    private Button phone;
    @FXML
    private Button wifi;

    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("serviceRequests");
    }

    @FXML
    public void handleClear(ActionEvent event) {
        if(event.getSource() == clrBtn) {
            type.setText("");
        }
    }

    @FXML
    public void handleITShortcuts(ActionEvent event) {
        if(event.getSource() == desktop) {
            type.setText(desktop.getText());
        } else if(event.getSource() == laptop) {
            type.setText(laptop.getText());
        } else if(event.getSource() == passLog) {
            type.setText(passLog.getText());
        } else if(event.getSource() == phone) {
            type.setText(phone.getText());
        } else if(event.getSource() == wifi) {
            type.setText(wifi.getText());
        }
    }

    @FXML
    public void makeItRequest() throws IOException {

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("it", room.getSelectionModel().getSelectedItem(), type.getText(), notes.getText(), urgent.isSelected());

            errorMessage.setText(" ");

            StackPane stackPane = new StackPane();
            stackPane.autosize();
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Success!"));
            content.setBody(new Text("Your service request was sent."));
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
            Pane imInPane = (Pane) Main.primaryStage.getScene().getRoot();
            imInPane.getChildren().add(stackPane);

            // Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            //System.out.println(content.getLayoutBounds().getWidth()+"/"+content.getLayoutBounds().getHeight());
            AnchorPane.setBottomAnchor(stackPane, 10.0);
            AnchorPane.setRightAnchor(stackPane, 10.0);
            AnchorPane.setTopAnchor(stackPane, 10.0);
            AnchorPane.setLeftAnchor(stackPane, 10.0);
            dialog.show();
            new Thread() {
                @Override
                public void run() {
                    try {
                        this.sleep(5000);
                        Platform.runLater(() -> {
                            dialog.close();
                            imInPane.getChildren().remove(stackPane);
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean areFieldsEmpty() {
        return room.getValue() == null || type.getText().isEmpty();
    }

    @FXML
    private void goToList() throws Exception {
        Main.setScene("serviceRequestsList");
    }

    @FXML
    public void getRoomNodes() {
        Floor graph = Floor.getFloor();
        ObservableList<String> nodeList = FXCollections.observableArrayList();

        for(Node n :graph.getNodes().values()){
            if (!n.getNodeType().equals("HALL")) {
                String nodeName = n.getLongName();
                if (nodeName.toUpperCase().contains("FLOOR")) {
                    nodeList.add(n.getLongName());
                } else {
                    nodeList.add(n.getLongName() + " Floor " + n.getFloor());
                }
            }
        }

        FXCollections.sort(nodeList); // sorted directory alphabetically
        room.setItems(nodeList);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Clock clock = new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isIT = ?";
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        Connection conn = DBUtils.getConnection();
        try{
            PreparedStatement s = conn.prepareStatement(query);
            s.setInt(1, 1); //CHANGE THIS FOR IT EMPLOYEES ???
            ResultSet rs = s.executeQuery();
            while(rs.next()){
                list.add(rs.getString(1));
                System.out.println(rs.getString(1));
            }
            for(String s1 : list){
                listEmployees.getItems().add(s1);
            }
            conn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        TextFields.bindAutoCompletion(type, it);

    }
}
