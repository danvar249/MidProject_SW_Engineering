package controllers;

import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**

This class is the controller for the home page of the logistics manager in the Ekt Logistics Management system.

It handles the functionality of the logout button and the restock button, as well as displaying a greeting to the user.

@author Raz,Nastya

@version 1.0
*/
public class EktLogisticsManagerHomePageController {

    @FXML
    private Button btnLogout;

    @FXML
    private Text txtGreeting;
    
    @FXML
    private Button btnRestock;
    /**

    Initializes the controller and sets the text of the greeting to include the user's first name.
    */
    @FXML
    private void initialize() {
    	txtGreeting.setText(txtGreeting.getText().concat(" ".concat(ClientController.getCurrentSystemUser().getFirstName())));
    }
    /**

    Handles the functionality of the logout button, including sending a logout request to the server and opening the login window.

    @param event the action event for the logout button
    */
    @FXML
    void getBackLogout(ActionEvent event) {
    	// actually log the user out
    			ClientController.sendLogoutRequest();

    			// move to new window
    			Stage primaryStage = new Stage();
    			WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);

    			primaryStage.show();
    			((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }
    /**

    Handles the functionality of the restock button, including opening the inventory restock window.
    @param event the action event for the restock button
    */
    @FXML
    void getBtnRestock(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/InventoryRestockWorkerPage.fxml", null, "Inventory Restock", true);
		primaryStage.show();
    }
}
