package controllers;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**

This class is the controller for the Division Manager home page.
It contains methods for handling button events and updating the UI.
@author [Raz]
@version [1.0]
@since [12.1.23]
*/
public class EktDivisionManagerHomePageController {
    @FXML
    private Button btnLogout;

    @FXML
    private Button btnViewReports;

    @FXML
    private Text txtGreeting;
    
    @FXML
    private Button btnAddUserToDB;
    /**
     * Initializes the UI by setting the welcome text to display the current user's first name
     * and centering the text
     */
    @FXML
    public void initialize() {
    	txtGreeting.setText("WELCOME BACK, " + ClientController.getCurrentSystemUser().getFirstName());
    	txtGreeting.setLayoutX(400 - (txtGreeting.minWidth(0))/ 2);
    }
    /**
     * Handles the event when the "View Reports" button is clicked.
     * Opens a new window for the report selection form.
     * The primary window is closed after the new window is opened.
     * 
     * @param event the action event that triggered the method call
     */
    @FXML
    public void getBtnViewReports(ActionEvent event) {
    	Stage primaryStage = new Stage();
    	WindowStarter.createWindow(primaryStage, this, "/gui/EktReportSelectForm.fxml", null, "Ekt Report Select", true);
    	primaryStage.show();
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }

/**
 * Handles the event when the "Logout" button is clicked.
 * Sends a logout request to the server and opens a new window for the login form.
 * The primary window is closed after the new window is opened.
 * 
 * @param event the action event that triggered the method call
 */
    @FXML
    void getBtnLogout(ActionEvent event) {
    	Stage primaryStage = new Stage();
    	ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
    	ClientController.sendLogoutRequest();
    	WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);
    	primaryStage.show();
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    	
    }
    /**
     * Handles the event when the "Add User To Database" button is clicked.
     * Opens a new window for the "Add User To Database" form.
     * The primary window is closed after the new window is opened.
     @param event the action event that triggered the method call
*/
    @FXML
    public void getBtnAddUserToDB(ActionEvent event) {
    	Stage primaryStage = new Stage();
    	WindowStarter.createWindow(primaryStage, this, "/gui/AddUserToDbForm.fxml", null, "Ekt Add User Form", true);
    	primaryStage.show();
    	((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

    }

}
