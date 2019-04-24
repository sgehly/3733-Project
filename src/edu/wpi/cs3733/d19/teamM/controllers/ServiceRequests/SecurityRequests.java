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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SecurityRequests {

    //Text field for room location input
    @FXML
    private Text userText;

    @FXML
    private JFXComboBox<String> room;

    @FXML
    private TextField type;

    @FXML
    private JFXCheckBox emergency;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Text errorMessage;

    @FXML
    private ListView listEmployees;

    String[] security = {"Intruder","Fight","Unruly Patient","Weapons Sighting","CODE ADAM","Lost Patient"};

    @FXML
    private javafx.scene.control.Button clrBtn;

    @FXML
    private javafx.scene.control.Button fight;
    @FXML
    private javafx.scene.control.Button intruder;
    @FXML
    private javafx.scene.control.Button lostPatient;
    @FXML
    private javafx.scene.control.Button unrulyPatient;
    @FXML
    private Button weapons;

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
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
    public void handleSecurityShortcuts(ActionEvent event) {
        if(event.getSource() == fight) {
            type.setText(fight.getText());
        } else if(event.getSource() == intruder) {
            type.setText(intruder.getText());
        } else if(event.getSource() == lostPatient) {
            type.setText(lostPatient.getText());
        } else if(event.getSource() == unrulyPatient) {
            type.setText(unrulyPatient.getText());
        } else if(event.getSource() == weapons) {
            type.setText(weapons.getText());
        }
    }

    public void makeSecurityRequest() throws IOException {

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("security", room.getSelectionModel().getSelectedItem(), type.getText(), notes.getText(), emergency.isSelected());

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

    @FXML
    void initialize(){

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isSec = ?";
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        Connection conn = DBUtils.getConnection();
        try{
            PreparedStatement s = conn.prepareStatement(query);
            s.setInt(1, 1);
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

        TextFields.bindAutoCompletion(type, security);

    }

}
