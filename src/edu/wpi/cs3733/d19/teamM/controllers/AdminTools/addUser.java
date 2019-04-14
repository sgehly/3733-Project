package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;


//import java.awt.*;
import javafx.scene.control.CheckBox;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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


    private void savePhoto(){
        File f = new File("src/resources/tempPhoto.png");
        f.renameTo(new File("src/resources/People_Pictures/" + username.getText() + ".png"));
        Image image = new Image(f.toURI().toString());
    }

    private boolean areFieldsEmpty() {
        return username.getText() == null || username.getText().isEmpty() ||
                newPassword.getText() == null || newPassword.getText().isEmpty() ||
                newPassword.getText().compareTo(confirmPassword.getText()) != 0;
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

    private ArrayList<Integer> buildBuffer(){
        ArrayList<Integer> buffer =new ArrayList<Integer>();
        buffer.add(buttonVal(sanitationButton.isSelected()));
        buffer.add(buttonVal(languageButton.isSelected()));
        buffer.add(buttonVal(itButton.isSelected()));
        buffer.add(buttonVal(avButton.isSelected()));
        buffer.add(buttonVal(giftButton.isSelected()));
        buffer.add(buttonVal(flowersButton.isSelected()));
        buffer.add(buttonVal(internalButton.isSelected()));
        buffer.add(buttonVal(externalButton.isSelected()));
        buffer.add(buttonVal(securityButton.isSelected()));
        buffer.add(buttonVal(religousButton.isSelected()));
        buffer.add(buttonVal(perscriptionButton.isSelected()));
        buffer.add(buttonVal(labTestButton.isSelected()));
        buffer.add(buttonVal(adminButton.isSelected()));
        return buffer;
    }

    private int buttonVal(boolean state){
        if(state)
            return 1;
        return 0;
    }

    @FXML
    void addUser(ActionEvent event) throws SQLException {
        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setStyle("-fx-text-inner-color: red;");
                errorMessage.setText("Incorrect Fields/Photo");
                throw e;
            }
            String tempName = username.getText();
            String tempPass = Encrypt.getMd5(newPassword.getText());
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME = ?");
            stmt1.setString(1,tempName);
            ResultSet rs = stmt1.executeQuery();
            if(rs.next()){
                errorMessage.setText("User Already Exists");
                throw e;
            }
            String query = "INSERT INTO USERS (USERNAME, isSan, isInterp, isIT, isAV, isGift, isFlor, isExt, isInt, isRel, isSec, isPer, isLab, ACCOUNTINT, USERPASS, PATHTOPIC) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            savePhoto();
            stmt.execute();
            conn.close();
            errorMessage.setStyle("-fx-text-fill: green;");
            errorMessage.setText("User Added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void save(ActionEvent event) {
        try {
            Exception e = new Exception();
            if (areFieldsEmpty()) {
                errorMessage.setText("Missing Fields");
                throw e;
            }
            if(User.getUsername().compareTo(username.getText()) == 0){
                errorMessage.setText("Cannot edit current user");
                throw e;
            }
            String tempName = username.getText();
            Connection conn = new DatabaseUtils().getConnection();
            String query = "UPDATE USERS SET isSan = ?, isInterp= ?, isIT= ?, isAV= ?, isGift= ?, isFlor= ?, isExt= ?, isInt= ?, isRel= ?, isSec= ?, isPer= ?, isLab= ?, ACCOUNTINT= ?, USERPASS= ?, PATHTOPIC= ? WHERE USERNAME = ?";
            String tempPass = Encrypt.getMd5(newPassword.getText());
            PreparedStatement stmt = conn.prepareStatement(query);
            ArrayList<Integer> buffer = buildBuffer();
            stmt.setString(16, tempName);
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
            if(tempPhoto != null){
                savePhoto();
            }
            stmt.executeUpdate();
            conn.close();
            errorMessage.setStyle("-fx-text-fill: green");
            errorMessage.setText("User Updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout(MouseEvent event) {
        if (webcam != null)
            webcam.close();
        Main.logOut();

    }

    @FXML
    void navigateToHome(MouseEvent event) {
        if (webcam != null)
            webcam.close();
        Main.setScene("adminUI");
    }

    @FXML
    void removeUser(ActionEvent event) {
        try{
            Exception e = new Exception();
            if(User.getUsername().compareTo(username.getText()) == 0) {
                errorMessage.setText("Cannot Delete Current");
                throw e;
            }
            Connection conn = new DatabaseUtils().getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM USERS WHERE USERNAME = ?");
            stmt.setString(1, username.getText());
            stmt.execute();
            conn.close();
            errorMessage.setText("User Deleted");
        }
        catch (Exception e){
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
    void initialize() {
        System.out.println("123");

        String os = System.getProperty("os.name").toLowerCase();
        /*if(os.indexOf("win") >= 0){
            defaultImage.setFitHeight(0);
            defaultImage.setFitWidth(0);
            try{
                if (Webcam.getDefault() != null) {
                    System.out.println("Trying...");
                    webcam = Webcam.getDefault();
                    webcam.open();
                    webcamPanel = new WebcamPanel(webcam, false);
                    pane.getChildren().add(swingNode);
                    createSwingContent(swingNode);
                } else {
                    errorMessage.setText("No Webcam Found, Resart");
                    errorMessage.setStyle("-fx-text-inner-color: red;");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }else{
            tempPhoto.setFitHeight(0);
            tempPhoto.setFitWidth(0);
            tempPhoto.setVisible(false);

            image.setFitHeight(0);
            image.setFitHeight(0);
            image.setVisible(false);
        }*/


        new Clock(lblClock, lblDate);

        //userText.setText(User.getUsername());
        userText.setText("");

        assert userText != null : "fx:id=\"userText\" was not injected: check your FXML file 'addUser.fxml'.";
        assert lblDate != null : "fx:id=\"lblDate\" was not injected: check your FXML file 'addUser.fxml'.";
        assert lblClock != null : "fx:id=\"lblClock\" was not injected: check your FXML file 'addUser.fxml'.";
        assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'addUser.fxml'.";
        assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'addUser.fxml'.";
        //assert webcamPreview != null : "fx:id=\"webcamPreview\" was not injected: check your FXML file 'addUser.fxml'.";
    }
}