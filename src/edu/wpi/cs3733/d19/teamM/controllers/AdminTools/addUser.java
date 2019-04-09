package edu.wpi.cs3733.d19.teamM.controllers.AdminTools;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
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
    private TextField userType;
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
    void addUser(ActionEvent event) throws SQLException {
        try {
            //TODO change where the if statement is
            Exception e = new Exception();
            if (username.getText() == null || username.getText().isEmpty() ||
                    newPassword.getText() == null || newPassword.getText().isEmpty() ||
                    newPassword.getText().compareTo(confirmPassword.getText()) != 0 ||
                    userType.getText() == null || userType.getText().isEmpty()) {
                throw e;
            }
            String tempName = username.getText();
            String tempPass = Encrypt.getMd5(newPassword.getText());
            int tempType = Integer.parseInt(userType.getText());
            Connection conn = new DatabaseUtils().getConnection();
            String query = "INSERT INTO USERS (USERNAME, ACCOUNTINT, USERPASS, PATHTOPIC) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, tempName);
            stmt.setInt(2, tempType);
            stmt.setString(3, tempPass);
            stmt.setString(4, "src/resources/People_Pictures/" + tempName + ".png");
            File f = new File("src/resources/tempPhoto.png");
            f.renameTo(new File("src/resources/People_Pictures/" + tempName + ".png"));
            Image image = new Image(f.toURI().toString());
            System.out.println(tempType);
            stmt.execute();
            errorMessage.setStyle("-fx-text-fill: green;");
            errorMessage.setText("User Added");
        } catch (Exception e) {
            errorMessage.setStyle("-fx-text-inner-color: red;");
            errorMessage.setText("Incorrect Fields/Photo");
            e.printStackTrace();
        }

    }

    @FXML
    void logout(MouseEvent event) {
        if (webcam != null)
            webcam.close();
        Main.setScene("welcome");

    }

    @FXML
    void navigateToHome(MouseEvent event) {
        if (webcam != null)
            webcam.close();
        Main.setScene("home");
    }

    @FXML
    void removeUser(ActionEvent event) {
        //TODO delete user

    }

    @FXML
    void save(ActionEvent event) {
        String tempName = username.getText();
        String tempPass = Encrypt.getMd5(newPassword.getText());
        int tempType = Integer.parseInt(userType.getText());

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
        if (Webcam.getDefault() != null) {
            webcam = Webcam.getDefault();
            webcam.open();
            webcamPanel = new WebcamPanel(webcam, false);
            pane.getChildren().add(swingNode);
            createSwingContent(swingNode);
        } else {
            errorMessage.setText("No Webcam Found, Resart");
            errorMessage.setStyle("-fx-text-inner-color: red;");
        }
        new Clock(lblClock, lblDate);

        userText.setText(User.getUsername());

        assert userText != null : "fx:id=\"userText\" was not injected: check your FXML file 'addUser.fxml'.";
        assert lblDate != null : "fx:id=\"lblDate\" was not injected: check your FXML file 'addUser.fxml'.";
        assert lblClock != null : "fx:id=\"lblClock\" was not injected: check your FXML file 'addUser.fxml'.";
        assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'addUser.fxml'.";
        assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'addUser.fxml'.";
        //assert webcamPreview != null : "fx:id=\"webcamPreview\" was not injected: check your FXML file 'addUser.fxml'.";
    }
}