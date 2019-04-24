package edu.wpi.cs3733.d19.teamM.controllers.Home;


import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.User.User;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.java.com.goxr3plus.javafxwebbrowser.browser.WebBrowserController;

public class Web {
    @FXML
    private Button logout;

    @FXML
    private ImageView back;

    @FXML
    private VBox pane;

    @FXML
    private Text userText;

    @FXML
    Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private void navigateToHome() throws Exception{
        Main.setScene("home");
    }

    @FXML
    private void navigateToWelcome() throws Exception{
        Main.setScene("welcome");
    }

    public WebBrowserController webBrowser = new WebBrowserController();


    @FXML
    void initialize() {
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
        webBrowser.setMovingTitlesEnabled(false);
        webBrowser.setMinHeight(900);
        webBrowser.createAndAddNewTab("https://brighamandwomens.org");
        webBrowser.closeTabsToTheLeft(webBrowser.getTabPane().getTabs().get(1));
        pane.getChildren().add(webBrowser);
    }
}
