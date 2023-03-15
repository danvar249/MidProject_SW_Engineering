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
 * This class is a JavaFX controller for the EktServiceRepresentativeHomePage
 * page. It allows a service representative user to navigate to the register
 * user and register subscriber pages.
 *
 */
public class EktServiceRepresentativeHomePageController {

	@FXML
	private Button btnLogout;

	@FXML
	private Button btnRegisterSubscriber;

	@FXML
	private Text txtGreeting;

	@FXML
	Button btnRegisterCustomer;

	/**
	 * Initializes the page, prints the welcome message to the text field.
	 */
	@FXML
	private void initialize() {
		txtGreeting.setText("Welcome back, " + ClientController.getCurrentSystemUser().getFirstName());
		txtGreeting.setLayoutX(400 - txtGreeting.minWidth(0) / 2);
	}

	/**
	 * Handles clicking the logout button. Logs the current user out and returns him
	 * to the login form page.
	 * 
	 * @param event
	 */
	@FXML
	void getBackLogout(ActionEvent event) {
		// actually log him out
		ClientUI.getClientController().accept(
				new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
		// inform log
		System.out.println(
				"Service representative " + ClientController.getCurrentSystemUser().getUsername() + " logged out!");
		// load home area for service rep
		// sammy D the current window
		((Node) event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();
		ClientController.sendLogoutRequest();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", false);
		primaryStage.show();
	}

	/**
	 * Handles clicking the register customer button. Navigates the user to the Add
	 * Customer form page.
	 * 
	 * @param event
	 */
	@FXML
	public void getRegisterCustomer(ActionEvent event) {
		// load home area for service rep
		// sammy D the current window
		((Node) event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepAddCustomerForm.fxml", null,
				"Customer Registration Page", false);
		primaryStage.show();

	}

	/**
	 * Handles clicking the register subscriber button. Navigates the user to the
	 * Add Subscriber form page.
	 * 
	 * @param event
	 */
	@FXML
	public void getBtnRegisterSubscriber(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepAddSubscriberForm.fxml", null,
				"Subscriber Registration Page", false);

		primaryStage.show();
	}

}
