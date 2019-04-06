package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import edu.wpi.cs3733.d19.teamM.Main;
import javafx.fxml.FXML;

public class ReligiousRequests {

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
}
