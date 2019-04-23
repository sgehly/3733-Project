package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Node;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;
import com.jfoenix.controls.JFXComboBox;


import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.ResourceBundle;

public class AudioVisual implements Initializable {


    String[] audioVis = {"Headphones","Speakers","Radio","TV","Camera","Microphone","CD","DVD","DVD Player","Projector","Video Camera"};

    @FXML
    private ListView listEmployees;

    @FXML
    private Text errorMessage;

    //Tesxt Field for audio/visual type input
    @FXML
    private TextField audioVisType;

    @FXML
    private Text userText;

    //Text field for room location input
    @FXML
    private JFXComboBox<String> roomDropDown;

    @FXML
    private JFXCheckBox pickUp;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Button submitReuqest;

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private javafx.scene.control.Button clrBtn;

    @FXML
    private javafx.scene.control.Button camera;
    @FXML
    private javafx.scene.control.Button earbuds;
    @FXML
    private javafx.scene.control.Button projector;
    @FXML
    private javafx.scene.control.Button speakers;
    @FXML
    private javafx.scene.control.Button tv;


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
    private void navigateBack() throws Exception {
        Main.setScene("serviceRequests");
    }

    @FXML
    public void handleClear(ActionEvent event) {
        if(event.getSource() == clrBtn) {
            audioVisType.setText("");
        }
    }

    @FXML
    public void handleAVShortcuts(ActionEvent event) {
        if(event.getSource() == camera) {
            audioVisType.setText(camera.getText());
        } else if(event.getSource() == earbuds) {
            audioVisType.setText(earbuds.getText());
        } else if(event.getSource() == projector) {
            audioVisType.setText(projector.getText());
        } else if(event.getSource() == speakers) {
            audioVisType.setText(speakers.getText());
        } else if(event.getSource() == tv) {
            audioVisType.setText(tv.getText());
        }
    }

    /**
     * This method allows the user to create a flowers request using the button
     *
     * @param : The action that is associated with making the flowers request
     */
    @FXML
    public void makeAudioVisRequest() throws IOException {
        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("av", roomDropDown.getSelectionModel().getSelectedItem(), audioVisType.getText(), notes.getText(), pickUp.isSelected());

/*            confLabel.setTextFill(Color.GREEN);
            confLabel.setVisible(true);
            confLabel.setText("Request Submitted!");
            */

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
        return audioVisType.getText().isEmpty() || roomDropDown.getSelectionModel().getSelectedItem() == "NONE";
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
        roomDropDown.setItems(nodeList);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Clock clock = new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());

        //confLabel.setVisible(false);

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isAV = ?";
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

        TextFields.bindAutoCompletion(audioVisType, audioVis);

    }
}
