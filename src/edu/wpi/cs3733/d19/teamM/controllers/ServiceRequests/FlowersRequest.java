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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import org.controlsfx.control.textfield.TextFields;


import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.ResourceBundle;

public class FlowersRequest implements Initializable {

    @FXML
    private Text userText;

    @FXML
    private Text errorMessage;

    @FXML
    private ListView listEmployees;

    @FXML
    private javafx.scene.control.Label lblClock;

    @FXML
    private javafx.scene.control.Label lblDate;

    String[] flowers = {"Alstroemeria","Amaryllis","Asiatic Lily","Aster","Azalea","Baby’s Breath","Begonia","Bird of Paradise"
            ,"Calla Lily","Campanula","Carnation","Chrysanthemum","Cockscomb","Crocus","Cyclamen","Cymbidium Orchid","Daffodil","Daisy","Delphinium","Dendrobium Orchid",
            "Freesia","Gardenia","Geranium","Gladiolus","Heather","Hibiscus","Hyacinth","Hydrangea","Hypericum","Iris","Jasmine","Jonquil","Kalanchoe","Larkspur","Lavender",
            "Liatris","Lilac","Lily","Limonium","Lisianthus","Magnolia","Marigold","Mini-Carnation","Narcissus","Orchid","Oriental Lily","Pansy","Peace Lily","Petite Rose",
            "Phalaenopsis Orchid","Poinsettia","Pompon","Protea","Ranunculus","Snapdragon","Solidago","Spray Rose","Star of Bethlehem","Stargazer Lily","Statice","Stock",
            "Tea Rose","Trachelium","Tuberose","Violet","Waxflower","Zinnia","Dahlia","Tulip"};

    //Tesxt Field for flower type input
    @FXML
    private TextField flowerType;

    @FXML
    private javafx.scene.control.Button btn;


    //Text field for room location input
    @FXML
    private JFXComboBox<String> room;

    @FXML
    private JFXCheckBox replace;

    //Text field for additional specifications
    @FXML
    private javafx.scene.control.TextArea notes;

    @FXML
    private Button submitReuqest;

    @FXML
    private Button clrBtn;

    @FXML
    private Button colorfulBouquet;
    @FXML
    private Button earthyBouquet;
    @FXML
    private Button pinkBouquet;
    @FXML
    private Button roseBouquet;
    @FXML
    private Button sunflowerBouquet;

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
            flowerType.setText("");
        }
    }

    @FXML
    public void handleFlowerBouquet(ActionEvent event) {
        System.out.println("HANDLE FLOWER BUTTON");
        if(event.getSource() == colorfulBouquet) {
            flowerType.setText(colorfulBouquet.getText());
        } else if(event.getSource() == earthyBouquet) {
            flowerType.setText(earthyBouquet.getText());
        } else if(event.getSource() == pinkBouquet) {
            flowerType.setText(pinkBouquet.getText());
        } else if(event.getSource() == roseBouquet) {
            flowerType.setText(roseBouquet.getText());
        } else if(event.getSource() == sunflowerBouquet) {
            flowerType.setText(sunflowerBouquet.getText());
        }
    }

    /**
     * This method allows the user to create a flowers request using the button
     * @param : The action that is associated with making the flowers request
     */
    @FXML
    public void setFlower(String flower) {
        flowerType.setText(flower);
    }

    @FXML
    public void makeFlowersRequest() throws IOException {
        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("You didn't answer all the required fields.");
                throw e;
            }
            new ServiceRequests().makeRequest("flowers", room.getSelectionModel().getSelectedItem(), flowerType.getText(), notes.getText(), replace.isSelected());

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
        return room.getSelectionModel().getSelectedItem() == "NONE" || flowerType.getText().isEmpty();
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

        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        System.out.println("ROOM NUM: " + room.getText());

        ObservableList<String> list = FXCollections.observableArrayList();

        String query = "select * FROM users Where isFlor = ?";
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

//        System.out.println("HERE");
//        room = new TextField();
//        room.setText(room.getText());
//        ObservableList<String> input = FXCollections.observableArrayList();
//        input.add(room.getText());
//        input.add(flowerType.getText());

        TextFields.bindAutoCompletion(flowerType,flowers);
    }
}
