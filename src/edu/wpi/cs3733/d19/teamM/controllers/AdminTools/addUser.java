package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;


//import java.awt.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.utilities.DatabaseUtils;
import edu.wpi.cs3733.d19.teamM.utilities.General.Encrypt;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import javax.swing.*;

public class addUser {

    private Webcam webcam;
    private WebcamPanel webcamPanel;

    final SwingNode swingNode = new SwingNode();
    @FXML
    private TextField confirmPassword;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Text errorMessage;
    @FXML
    private TextField username;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField newPassword;
    @FXML
    private Text userText;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblClock;
    @FXML
    private HBox imageView;
    @FXML
    private ImageView image;
    @FXML
    private StackPane pane;
    @FXML
    private ImageView tempPhoto;
    @FXML
    private ImageView defaultImage;



    @FXML
    private CheckBox religousButton2;
    @FXML
    private CheckBox sanitationButton2;
    @FXML
    private CheckBox languageButton2;
    @FXML
    private CheckBox externalButton2;
    @FXML
    private CheckBox securityButton2;
    @FXML
    private CheckBox flowersButton2;
    @FXML
    private CheckBox giftButton2;
    @FXML
    private CheckBox internalButton2;
    @FXML
    private CheckBox itButton2;
    @FXML
    private CheckBox perscriptionButton2;
    @FXML
    private CheckBox labTestButton2;
    @FXML
    private CheckBox avButton2;
    @FXML
    private CheckBox adminButton2;



    @FXML
    private CheckBox religousButton;
    @FXML
    private CheckBox sanitationButton;
    @FXML
    private CheckBox languageButton;
    @FXML
    private CheckBox externalButton;
    @FXML
    private CheckBox securityButton;
    @FXML
    private CheckBox flowersButton;
    @FXML
    private CheckBox giftButton;
    @FXML
    private CheckBox internalButton;
    @FXML
    private CheckBox itButton;
    @FXML
    private CheckBox perscriptionButton;
    @FXML
    private CheckBox labTestButton;
    @FXML
    private CheckBox avButton;
    @FXML
    private CheckBox adminButton;

    @FXML
    private Button addUserButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button modifyButton;



    @FXML
    private ListView list;
    @FXML
    private ListView modifyList;

    @FXML
    private VBox addUserBox;
    @FXML
    private VBox removeUserBox;
    @FXML
    private VBox modifyUserBox;

    @FXML
    private TextField username2;
    @FXML
    private PasswordField newPassword2;
    @FXML
    private PasswordField confirmPassword2;
    @FXML
    private TextField phoneField2;
    @FXML
    private Label user;



    private void savePhoto(){
        File f = new File("src/resources/tempPhoto.png");
        f.renameTo(new File("src/resources/People_Pictures/" + username.getText() + ".png"));
        Image image = new Image(f.toURI().toString());
    }

    private boolean areFieldsEmpty() {
        return  newPassword2.getText() == null || newPassword2.getText().isEmpty() ||
                newPassword2.getText().compareTo(confirmPassword2.getText()) != 0;
    }

    /**
     * 0-Religious
     * 1-Sanitation
     * 2-language
     * 3-External Transport
     * 4-Security
     * 5-Flowers
     * 6-Gift
     * 7-Internal Transport
     * 8-IT
     * 9-Lab Test
     * 10-Perscription
     * 11-AV
     *
     * 100-Admin
     *
     * @param type String representation of user type
     * @return corresponding integer
     */

    private int convertUserTypeInt(String type){
        if(type.compareTo("Religious") == 0){
            return 0;
        }
        else if(type.compareTo("Sanitation") == 0){
            return 1;
        }
        else if(type.compareTo("Language") == 0){
            return 2;
        }
        else if(type.compareTo("External Transport") == 0){
            return 3;
        }
        else if(type.compareTo("Security") == 0){
            return 4;
        }
        else if(type.compareTo("Flowers") == 0){
            return 5;
        }
        else if(type.compareTo("Gift") == 0){
            return 6;
        }
        else if(type.compareTo("Internal Transport") == 0){
            return 7;
        }
        else if(type.compareTo("IT") == 0){
            return 8;
        }
        else if(type.compareTo("Lab Test") == 0){
            return 9;
        }
        else if(type.compareTo("Prescription") == 0){
            return 10;
        }
        else if(type.compareTo("AV") == 0){
            return 11;
        }
        else
            return 100;
    }



    private String getIdFromTable() {
        int name = list.getFocusModel().getFocusedIndex();
        return (String) list.getItems().get(name);
    }

    private ArrayList<Integer> buildBuffer(){
        ArrayList<Integer> buffer =new ArrayList<Integer>();
        buffer.add(buttonVal(sanitationButton.isSelected()));
        System.out.println("sanitation: " + sanitationButton.isSelected());
        buffer.add(buttonVal(languageButton.isSelected()));
        System.out.println("language: " + languageButton.isSelected());
        buffer.add(buttonVal(itButton.isSelected()));
        System.out.println("it: " + itButton.isSelected());
        buffer.add(buttonVal(avButton.isSelected()));
        System.out.println("av: " + avButton.isSelected());
        buffer.add(buttonVal(giftButton.isSelected()));
        System.out.println("gift: " + giftButton.isSelected());
        buffer.add(buttonVal(flowersButton.isSelected()));
        System.out.println("flowers: " + flowersButton.isSelected());
        buffer.add(buttonVal(internalButton.isSelected()));
        System.out.println("internal: " + internalButton.isSelected());
        buffer.add(buttonVal(externalButton.isSelected()));
        System.out.println("external: " + externalButton.isSelected());
        buffer.add(buttonVal(securityButton.isSelected()));
        System.out.println("security: " + securityButton.isSelected());
        buffer.add(buttonVal(religousButton.isSelected()));
        System.out.println("religious: " + religousButton.isSelected());
        buffer.add(buttonVal(perscriptionButton.isSelected()));
        System.out.println("perscription: " + perscriptionButton.isSelected());
        buffer.add(buttonVal(labTestButton.isSelected()));
        System.out.println("lab test: " + labTestButton.isSelected());
        if(adminButton.isSelected()){
            buffer.add(100);
        }
        else{
            buffer.add(0);
        }
        System.out.println("admin: " + adminButton.isSelected());
        return buffer;
    }

    private ArrayList<Integer> buildBuffer2(){
        ArrayList<Integer> buffer =new ArrayList<Integer>();
        buffer.add(buttonVal(sanitationButton2.isSelected()));
        System.out.println("sanitation: " + sanitationButton2.isSelected());
        buffer.add(buttonVal(languageButton2.isSelected()));
        System.out.println("language: " + languageButton2.isSelected());
        buffer.add(buttonVal(itButton2.isSelected()));
        System.out.println("it: " + itButton2.isSelected());
        buffer.add(buttonVal(avButton2.isSelected()));
        System.out.println("av: " + avButton2.isSelected());
        buffer.add(buttonVal(giftButton2.isSelected()));
        System.out.println("gift: " + giftButton2.isSelected());
        buffer.add(buttonVal(flowersButton2.isSelected()));
        System.out.println("flowers: " + flowersButton2.isSelected());
        buffer.add(buttonVal(internalButton2.isSelected()));
        System.out.println("internal: " + internalButton2.isSelected());
        buffer.add(buttonVal(externalButton2.isSelected()));
        System.out.println("external: " + externalButton2.isSelected());
        buffer.add(buttonVal(securityButton2.isSelected()));
        System.out.println("security: " + securityButton2.isSelected());
        buffer.add(buttonVal(religousButton2.isSelected()));
        System.out.println("religious: " + religousButton2.isSelected());
        buffer.add(buttonVal(perscriptionButton2.isSelected()));
        System.out.println("perscription: " + perscriptionButton2.isSelected());
        buffer.add(buttonVal(labTestButton2.isSelected()));
        System.out.println("lab test: " + labTestButton2.isSelected());
        if(adminButton2.isSelected()){
            buffer.add(100);
        }
        else{
            buffer.add(0);
        }
        System.out.println("admin: " + adminButton2.isSelected());
        return buffer;
    }

    private int buttonVal(boolean state){
        if(state)
            return 1;
        return 0;
    }

    @FXML
    private void chooseAdd(){
        user.setVisible(false);
        addUserBox.setDisable(false);
        modifyUserBox.setDisable(true);
        removeUserBox.setDisable(true);
        modifyButton.setStyle("");
        removeButton.setStyle("");
        removeUserBox.setMouseTransparent(true);
        modifyUserBox.setMouseTransparent(true);
        addUserBox.setMouseTransparent(false);
        removeUserBox.setOpacity(0);
        modifyUserBox.setOpacity(0);
        addUserBox.setOpacity(1);
        addUserButton.setStyle("-fx-background-color: white; -fx-text-fill: black");
    }

    @FXML
    private void chooseRemove() throws SQLException {
        user.setVisible(false);
        this.populateUserTable(list);
        this.populateUserTable(modifyList);
        addUserBox.setDisable(true);
        modifyUserBox.setDisable(true);
        removeUserBox.setDisable(false);
        modifyButton.setStyle("");
        addUserButton.setStyle("");
        removeUserBox.setMouseTransparent(false);
        modifyUserBox.setMouseTransparent(true);
        addUserBox.setMouseTransparent(true);
        removeUserBox.setOpacity(1);
        modifyUserBox.setOpacity(0);
        addUserBox.setOpacity(0);
        removeButton.setStyle("-fx-background-color: white; -fx-text-fill: black");
    }

    @FXML
    private void chooseModify() throws SQLException{
        user.setVisible(false);
        this.populateUserTable(list);
        this.populateUserTable(modifyList);
        addUserBox.setDisable(true);
        modifyUserBox.setDisable(false);
        removeUserBox.setDisable(true);
        addUserButton.setStyle("");
        removeButton.setStyle("");
        removeUserBox.setMouseTransparent(true);
        modifyUserBox.setMouseTransparent(false);
        addUserBox.setMouseTransparent(true);
        removeUserBox.setOpacity(0);
        modifyUserBox.setOpacity(1);
        addUserBox.setOpacity(0);
        modifyButton.setStyle("-fx-background-color: white; -fx-text-fill: black");
    }

    @FXML
    void addUser(ActionEvent event) throws SQLException {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        try {
            if (areFieldsEmpty2()) {
 //               errorMessage.setStyle("-fx-text-inner-color: red;");
 //               errorMessage.setText("Incorrect Fields/Photo");
            }
            String tempName = username.getText();
            String tempPhone = phoneField.getText();
            String tempPass = Encrypt.getMd5(newPassword.getText());
            Connection conn = DBUtils.getConnection();
            PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME = ?");
            stmt1.setString(1,tempName);
            ResultSet rs = stmt1.executeQuery();
            if(rs.next()){
//                errorMessage.setText("User Already Exists");

            }

            String query = "INSERT INTO USERS (USERNAME, isSan, isInterp, isIT, isAV, isGift, isFlor, isExt, isInt, isRel, isSec, isPer, isLab, ACCOUNTINT, USERPASS, PATHTOPIC, PHONEEMAIL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            //isSan int, isInterp int, isIT int, isAV int, isGift int, isFlor int, isInt int, isExt int, isRel int, isSec int, isPer int, isLab int, accountInt int, userPass varchar(100) not null,pathtopic varchar(100)
            PreparedStatement stmt = conn.prepareStatement(query);
            ArrayList<Integer> buffer = buildBuffer();
            stmt.setString(1, tempName);
            stmt.setInt(2, buffer.get(0));
            stmt.setInt(3, buffer.get(1));
            stmt.setInt(4, buffer.get(2));
            stmt.setInt(5, buffer.get(3));
            stmt.setInt(6, buffer.get(4));
            stmt.setInt(7, buffer.get(5));
            stmt.setInt(8, buffer.get(6));
            stmt.setInt(9, buffer.get(7));
            stmt.setInt(10, buffer.get(8));
            stmt.setInt(11, buffer.get(9));
            stmt.setInt(12, buffer.get(10));
            stmt.setInt(13, buffer.get(11));
            stmt.setInt(14, buffer.get(12));
            stmt.setString(15, tempPass);
            stmt.setString(16, "src/resources/People_Pictures/" + tempName + ".png");
            stmt.setString(17,tempPhone);
            savePhoto();
            stmt.execute();
            conn.close();
            user.setStyle("-fx-text-fill: green;");
            user.setText("User Added!");
            user.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            user.setStyle("-fx-text-fill: red;");
            user.setText("User not Added!");
            user.setVisible(true);
        }
    }

    private boolean areFieldsEmpty2() {
        return username.getText() == null || username.getText().isEmpty() ||
                newPassword.getText() == null || newPassword.getText().isEmpty() ||
                newPassword.getText().compareTo(confirmPassword.getText()) != 0;
    }

    @FXML
    void save(ActionEvent event) {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                user.setText("Missing Fields");
                throw e;
            }
            if(User.getUsername().compareTo(username.getText()) == 0){
                errorMessage.setText("Cannot edit current user");
                throw e;
            }
            String tempName = modifyList.getSelectionModel().selectedItemProperty().getValue().toString();;
            String tempPhone = phoneField2.getText();
            Connection conn = DBUtils.getConnection();
            String query = "UPDATE USERS SET isSan = ?, isInterp= ?, isIT= ?, isAV= ?, isGift= ?, isFlor= ?, isInt= ?, isExt= ?, ISSEC= ?, ISREL= ?, isPer= ?, isLab= ?, ACCOUNTINT= ?, USERPASS= ?, PATHTOPIC= ?, PHONEEMAIL= ? WHERE USERNAME = ?";
            String tempPass = Encrypt.getMd5(newPassword2.getText());
            PreparedStatement stmt = conn.prepareStatement(query);
            ArrayList<Integer> buffer = buildBuffer2();
            stmt.setInt(1, buffer.get(0));
            stmt.setInt(2, buffer.get(1));
            stmt.setInt(3, buffer.get(2));
            stmt.setInt(4, buffer.get(3));
            stmt.setInt(5, buffer.get(4));
            stmt.setInt(6, buffer.get(5));
            stmt.setInt(7, buffer.get(6));
            stmt.setInt(8, buffer.get(7));
            stmt.setInt(9, buffer.get(8));
            stmt.setInt(10, buffer.get(9));
            stmt.setInt(11, buffer.get(10));
            stmt.setInt(12, buffer.get(11));
            stmt.setInt(13, buffer.get(12));
            stmt.setString(14, tempPass);
            stmt.setString(15, "src/resources/People_Pictures/" + tempName + ".png");
            stmt.setString(16,tempPhone);
            stmt.setString(17, tempName);
            if(tempPhoto != null){
                savePhoto();
            }
            stmt.executeUpdate();
            conn.close();
            this.resetModifyFields();
            user.setStyle("-fx-text-fill: green;");
            user.setText("User modified!");
            user.setVisible(true);
//            errorMessage.setStyle("-fx-text-fill: green");
//            errorMessage.setText("User Updated");
        } catch (Exception e) {
            user.setStyle("-fx-text-fill: red;");
            user.setText("User not modified!");
            user.setVisible(true);
            e.printStackTrace();
        }
    }

    private void resetModifyFields() {
        newPassword2.clear();
        confirmPassword2.clear();
        phoneField2.clear();
        modifyList.getSelectionModel().clearSelection();
    }

    @FXML
    void logout(MouseEvent event) {
        if (webcam != null)
            webcam.close();
        Main.logOut();

    }

    @FXML
    void navigateToHome(MouseEvent event) {
        user.setVisible(false);
        if (webcam != null)
            webcam.close();
        Main.setScene("admin");
    }

    @FXML
    void removeUser(ActionEvent event) {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        try{
            Exception e = new Exception();
            if(User.getUsername().compareTo(username.getText()) == 0) {
                System.out.println("Cannot Delete Current");
                throw e;
            }
            Connection conn = DBUtils.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM USERS WHERE USERNAME = ?");
            stmt.setString(1, this.getIdFromTable());
            stmt.execute();
            conn.close();
            System.out.println("User Deleted");
            user.setStyle("-fx-text-fill: green;");
            user.setText("User removed!");
            user.setVisible(true);
            this.populateUserTable(list);
            this.populateUserTable(modifyList);
//            errorMessage.setText("User Deleted");
        }
        catch (Exception e){
            System.out.println("Error while trying to fetch all records");
            user.setStyle("-fx-text-fill: red;");
            user.setText("User not removed!");
            user.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    void takePhoto(ActionEvent event) throws IOException {
        ImageIO.write(webcam.getImage(), "PNG", new File("src/resources/tempPhoto.png"));
        File f = new File("src/resources/tempPhoto.png");
        Image image = new Image(f.toURI().toString());
        tempPhoto.setImage(image);
    }


    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(webcamPanel);
                webcamPanel.start();
            }
        });
    }

    @FXML
    void initialize() throws SQLException {
        user.setVisible(false);
        addUserBox.setDisable(false);
        modifyUserBox.setDisable(true);
        removeUserBox.setDisable(true);

        addUserButton.setStyle("-fx-background-color: white; -fx-text-fill: black");
        addUserBox.setOpacity(1);
        modifyUserBox.setOpacity(0);
        removeUserBox.setOpacity(0);
        this.populateUserTable(list);
        this.populateUserTable(modifyList);
        System.out.println("123");
        new Clock(lblClock, lblDate);

        userText.setText(User.getUsername());

        modifyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String user = modifyList.getSelectionModel().selectedItemProperty().getValue().toString();
                username2.setText(user);
                newPassword2.clear();
                confirmPassword2.clear();
                DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
                Connection conn = DBUtils.getConnection();
                try {
                    String query = "SELECT * FROM USERS WHERE USERNAME = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, user);
                    ResultSet rs = stmt.executeQuery();
                    ArrayList<Integer> checkboxList = new ArrayList<Integer>();
                    while (rs.next()) {
                        checkboxList.add(rs.getInt("isSan"));
                        checkboxList.add(rs.getInt("isInterp"));
                        checkboxList.add(rs.getInt("isIT"));
                        checkboxList.add(rs.getInt("isAV"));
                        checkboxList.add(rs.getInt("isGift"));
                        checkboxList.add(rs.getInt("isFlor"));
                        checkboxList.add(rs.getInt("isExt"));
                        checkboxList.add(rs.getInt("isInt"));
                        checkboxList.add(rs.getInt("isRel"));
                        checkboxList.add(rs.getInt("isSec"));
                        checkboxList.add(rs.getInt("isPer"));
                        checkboxList.add(rs.getInt("isLab"));
                        checkboxList.add(rs.getInt("ACCOUNTINT"));
                        phoneField2.setText(rs.getString("PHONEEMAIL"));
                    }
                    this.updateCheckboxes(checkboxList);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            private void updateCheckboxes(ArrayList<Integer> checkboxList) {
                for(int i = 0; i < checkboxList.size(); i++){
                    if(checkboxList.get(i) == 100){
                        adminButton2.setSelected(true);
                    }
                    else if(checkboxList.get(i) == 1){
                        switch(i){
                            case 1: sanitationButton2.setSelected(true);
                            case 2: languageButton2.setSelected(true);
                            case 3: itButton2.setSelected(true);
                            case 4: avButton2.setSelected(true);
                            case 5: giftButton2.setSelected(true);
                            case 6: flowersButton2.setSelected(true);
                            case 7: externalButton2.setSelected(true);
                            case 8: internalButton2.setSelected(true);
                            case 9: religousButton2.setSelected(true);
                            case 10: securityButton2.setSelected(true);
                            case 11: perscriptionButton2.setSelected(true);
                            case 12: labTestButton2.setSelected(true);
                        }
                    }
                    else{
                        switch (i) {
                            case 1: sanitationButton2.setSelected(false);
                            case 2: languageButton2.setSelected(false);
                            case 3: itButton2.setSelected(false);
                            case 4: avButton2.setSelected(false);
                            case 5: giftButton2.setSelected(false);
                            case 6: flowersButton2.setSelected(false);
                            case 7: externalButton2.setSelected(false);
                            case 8: internalButton2.setSelected(false);
                            case 9: religousButton2.setSelected(false);
                            case 10: securityButton2.setSelected(false);
                            case 11: perscriptionButton2.setSelected(false);
                            case 12: labTestButton2.setSelected(false);
                            case 13: adminButton2.setSelected(false);
                        }
                    }
                }
            }
        });


        assert userText !=null:"fx:id=\"userText\" was not injected: check your FXML file 'addUser.fxml'.";
        assert lblDate !=null:"fx:id=\"lblDate\" was not injected: check your FXML file 'addUser.fxml'.";
        assert lblClock !=null:"fx:id=\"lblClock\" was not injected: check your FXML file 'addUser.fxml'.";
        assert imageView !=null:"fx:id=\"imageView\" was not injected: check your FXML file 'addUser.fxml'.";
        assert image !=null:"fx:id=\"image\" was not injected: check your FXML file 'addUser.fxml'.";
            //assert webcamPreview != null : "fx:id=\"webcamPreview\" was not injected: check your FXML file 'addUser.fxml'."
    }


    private void populateUserTable(ListView list) throws SQLException {
        DatabaseUtils DBUtils = DatabaseUtils.getDBUtils();
        list.setItems(null);
        Connection conn = DBUtils.getConnection();
        String query = "SELECT USERNAME From USERS";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ObservableList<String>  items = FXCollections.observableArrayList();
        try{
            while(rs.next()){
                items.add(rs.getString(1));
            }
                list.setItems(items);
        }catch(SQLException e){
            System.out.println("error while trying to fetch all records");
            e.printStackTrace();
            throw e;
        }
        conn.close();
    }
}