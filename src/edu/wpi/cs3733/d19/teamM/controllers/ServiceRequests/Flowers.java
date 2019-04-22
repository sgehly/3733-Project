package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import edu.wpi.cs3733.d19.teamM.Main;
import edu.wpi.cs3733.d19.teamM.utilities.Clock;
import edu.wpi.cs3733.d19.teamM.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * The controller class associated with selecting flowers
 */
public class Flowers {

    @FXML
    private Label lblClock;

    @FXML
    private Label lblDate;

    @FXML
    private Text userText;

    // Buttons for each flower
    @FXML
    private Button amaryllis;
    @FXML
    private Button azalea;
    @FXML
    private Button babysBreath;
    @FXML
    private Button birdOfParadise;
    @FXML
    private Button buttercup;
    @FXML
    private Button carnation;
    @FXML
    private Button chrysanthemum;
    @FXML
    private Button daffodil;
    @FXML
    private Button dahlia;
    @FXML
    private Button daisy;
    @FXML
    private Button delphinium;
    @FXML
    private Button freesia;
    @FXML
    private Button gardenia;
    @FXML
    private Button gladiolus;
    @FXML
    private Button hibiscus;
    @FXML
    private Button hyacinth;
    @FXML
    private Button hydrangea;
    @FXML
    private Button iris;
    @FXML
    private Button jasmine;
    @FXML
    private Button lavender;
    @FXML
    private Button lilac;
    @FXML
    private Button lily;
    @FXML
    private Button magnolia;
    @FXML
    private Button marigold;
    @FXML
    private Button orchid;
    @FXML
    private Button pansy;
    @FXML
    private Button peony;
    @FXML
    private Button petunia;
    @FXML
    private Button poinsettia;
    @FXML
    private Button roses;
    @FXML
    private Button snapdragon;
    @FXML
    private Button sunflower;
    @FXML
    private Button tulip;
    @FXML
    private Button violet;
    @FXML
    private Button waterlily;

    /**
     * This method is for the logout button which allows the user to go back to the welcome screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    public void logout() throws Exception {
        Main.logOut();
    }

    /**
     * This method is for the back button which allows the user to go back to the flowers request screen
     * @throws Exception: Any exception that is encountered
     */
    @FXML
    private void navigateToFlowerRequest() throws Exception {
        Main.setScene("serviceRequests/flowersRequest");
    }

    /**
     * This method takes the user's selection and puts it in the text box in the flowers request page
     * @throws IOException
     */
    @FXML
    public void changeToFlowerRequest(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("views/serviceRequests/flowersRequest.fxml"));
        Parent flowers = loader.load();

        FlowersRequest controller = loader.getController();

        // puts corresponding flower in the text box of the flower request
        if(event.getSource() == amaryllis) {
            controller.setFlower(amaryllis.getText());
            amaryllis.getScene().setRoot(flowers);
        } else if(event.getSource() == azalea) {
            controller.setFlower(azalea.getText());
            azalea.getScene().setRoot(flowers);
        } else if(event.getSource() == babysBreath) {
            controller.setFlower(babysBreath.getText());
            babysBreath.getScene().setRoot(flowers);
        } else if(event.getSource() == birdOfParadise) {
            controller.setFlower(birdOfParadise.getText());
            birdOfParadise.getScene().setRoot(flowers);
        } else if(event.getSource() == buttercup) {
            controller.setFlower(buttercup.getText());
            buttercup.getScene().setRoot(flowers);
        } else if(event.getSource() == carnation) {
            controller.setFlower(carnation.getText());
            carnation.getScene().setRoot(flowers);
        } else if(event.getSource() == chrysanthemum) {
            controller.setFlower(chrysanthemum.getText());
            chrysanthemum.getScene().setRoot(flowers);
        } else if(event.getSource() == daffodil) {
            controller.setFlower(daffodil.getText());
            daffodil.getScene().setRoot(flowers);
        } else if(event.getSource() == dahlia) {
            controller.setFlower(dahlia.getText());
            dahlia.getScene().setRoot(flowers);
        } else if(event.getSource() == daisy) {
            controller.setFlower(daisy.getText());
            daisy.getScene().setRoot(flowers);
        } else if(event.getSource() == delphinium) {
            controller.setFlower(delphinium.getText());
            delphinium.getScene().setRoot(flowers);
        } else if(event.getSource() == freesia) {
            controller.setFlower(freesia.getText());
            freesia.getScene().setRoot(flowers);
        } else if(event.getSource() == gardenia) {
            controller.setFlower(gardenia.getText());
            gardenia.getScene().setRoot(flowers);
        } else if(event.getSource() == gladiolus) {
            controller.setFlower(gladiolus.getText());
            gladiolus.getScene().setRoot(flowers);
        } else if(event.getSource() == hibiscus) {
            controller.setFlower(hibiscus.getText());
            hibiscus.getScene().setRoot(flowers);
        } else if(event.getSource() == hyacinth) {
            controller.setFlower(hyacinth.getText());
            hyacinth.getScene().setRoot(flowers);
        } else if(event.getSource() == hydrangea) {
            controller.setFlower(hydrangea.getText());
            hydrangea.getScene().setRoot(flowers);
        } else if(event.getSource() == iris) {
            controller.setFlower(iris.getText());
            iris.getScene().setRoot(flowers);
        } else if(event.getSource() == jasmine) {
            controller.setFlower(jasmine.getText());
            jasmine.getScene().setRoot(flowers);
        } else if(event.getSource() == lavender) {
            controller.setFlower(lavender.getText());
            lavender.getScene().setRoot(flowers);
        } else if(event.getSource() == lilac) {
            controller.setFlower(lilac.getText());
            lilac.getScene().setRoot(flowers);
        } else if(event.getSource() == lily) {
            controller.setFlower(lily.getText());
            lily.getScene().setRoot(flowers);
        } else if(event.getSource() == magnolia) {
            controller.setFlower(magnolia.getText());
            magnolia.getScene().setRoot(flowers);
        } else if(event.getSource() == marigold) {
            controller.setFlower(marigold.getText());
            marigold.getScene().setRoot(flowers);
        } else if(event.getSource() == orchid) {
            controller.setFlower(orchid.getText());
            orchid.getScene().setRoot(flowers);
        } else if(event.getSource() == pansy) {
            controller.setFlower(pansy.getText());
            pansy.getScene().setRoot(flowers);
        } else if(event.getSource() == peony) {
            controller.setFlower(peony.getText());
            peony.getScene().setRoot(flowers);
        } else if(event.getSource() == petunia) {
            controller.setFlower(petunia.getText());
            petunia.getScene().setRoot(flowers);
        } else if(event.getSource() == poinsettia) {
            controller.setFlower(poinsettia.getText());
            poinsettia.getScene().setRoot(flowers);
        } else if(event.getSource() == roses) {
            controller.setFlower(roses.getText());
            roses.getScene().setRoot(flowers);
        } else if(event.getSource() == snapdragon) {
            controller.setFlower(snapdragon.getText());
            snapdragon.getScene().setRoot(flowers);
        } else if(event.getSource() == sunflower) {
            controller.setFlower(sunflower.getText());
            sunflower.getScene().setRoot(flowers);
        } else if(event.getSource() == tulip) {
            controller.setFlower(tulip.getText());
            tulip.getScene().setRoot(flowers);
        } else if(event.getSource() == violet) {
            controller.setFlower(violet.getText());
            violet.getScene().setRoot(flowers);
        } else if(event.getSource() == waterlily){
            controller.setFlower(waterlily.getText());
            waterlily.getScene().setRoot(flowers);
        }
    }

    /**
     * This method initializes the Flowers Controller class
     */
    @FXML
    void initialize(){
        new Clock(lblClock, lblDate);
        userText.setText(User.getUsername());
    }
}

