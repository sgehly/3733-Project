package edu.wpi.cs3733.d19.teamM.controllers;

import edu.wpi.cs3733.d19.teamM.Main;
import javafx.fxml.FXML;

/**
 * The controller for the Home screen
 */
public class Home {

    /**
     * This method
     * @throws Exception
     */
    @FXML
    public void navigateToPathfinding(){
        Main.setScene("pathfinding");
    }

    @FXML
    public void navigateToServiceRequests(){
        Main.setScene("serviceRequest");
    }

    @FXML
    public void logout(){
        Main.setScene("welcome");
    }
    @FXML
    public void navigateToScheduling(){
        Main.setScene("scheduling");
    }

    @FXML
    public void navigateToAdmin(){
        Main.setScene("admin");
    }
}
