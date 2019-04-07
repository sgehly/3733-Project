package edu.wpi.cs3733.d19.teamM.controllers.ServiceRequests;

import edu.wpi.cs3733.d19.teamM.Main;
import javafx.fxml.FXML;

public class ITRequests {

    @FXML
    public void logout() throws Exception {
        Main.setScene("welcome");
    }

    @FXML
    private void navigateBack() throws Exception {
        Main.setScene("serviceRequests");
    }
}
