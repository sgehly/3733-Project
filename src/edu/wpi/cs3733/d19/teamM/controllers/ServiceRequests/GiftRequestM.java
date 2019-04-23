package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Floor;
import edu.wpi.cs3733.d19.teamM.utilities.AStar.Node;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GiftRequestM {

    @FXML
    JFXTextField giftTypes;

    @FXML
    JFXComboBox<String> room;

    @FXML
    JFXTextArea requestText;

    @FXML
    Label lblClock;

    @FXML
    Label lblDate;

    @FXML
    private Text userText;

    @FXML
    private JFXCheckBox packaged;

    @FXML
    private Text errorMessage;

    @FXML
    private ListView listEmployees;

    @FXML
    private Button clrBtn;

    @FXML
    private Button balloons;
    @FXML
    private Button card;
    @FXML
    private Button chocolate;
    @FXML
    private Button pictureFrame;
    @FXML
    private Button teddyBear;

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
            giftTypes.setText("");
        }
    }

    @FXML
    public void handleGiftShortcuts(ActionEvent event) {
        if(event.getSource() == balloons) {
            giftTypes.setText(balloons.getText());
        } else if(event.getSource() == card) {
            giftTypes.setText(card.getText());
        } else if(event.getSource() == chocolate) {
            giftTypes.setText(chocolate.getText());
        } else if(event.getSource() == pictureFrame) {
            giftTypes.setText(pictureFrame.getText());
        } else if(event.getSource() == teddyBear) {
            giftTypes.setText(teddyBear.getText());
        }
    }

    /**
     *
     * @throws IOException
     */
    @FXML
    public void makeGiftRequest() throws IOException {

        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("gift", room.getSelectionModel().getSelectedItem(), giftTypes.getText(), requestText.getText(), packaged.isSelected());

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

    /**
     *
     * @return
     */
    private boolean areFieldsEmpty() {
        return room.getSelectionModel().getSelectedItem() == "NONE" || giftTypes.getText().isEmpty();
    }

    /**
     *
     * @throws Exception
     */
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
    void initialize()
    {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        new Clock(lblClock,lblDate); //initialize the clock
        userText.setText(User.getUsername());
        //Create the listener for the text with a few options

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isGift = ?";
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

        ArrayList<String> giftOptions = new ArrayList<String>();//ArrayList with options
        giftOptions.add("Gift Cards");
        giftOptions.add("Teddy Bears");
        giftOptions.add("Chocolates");
        giftOptions.add("Photo Frames");
        giftOptions.add("Balloons");

        giftTypes.textProperty().addListener((ov, oldValue, newValue) ->
        {
            TextFields.bindAutoCompletion(giftTypes,giftOptions);

        });
    }
}
