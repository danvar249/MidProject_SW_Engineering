package controllers;

import java.util.ArrayList;

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
 * The EktRegionalManagerHomePageController class handles the home page of the
 * Regional Manager in the Ekt system. It contains the following methods:
 * 
 * initialize: sets the location of the manager and the welcome text for the
 * manager. getBtnAcceptCustomers: opens the window for accepting new customers.
 * getBtnSetThreshold: opens the window for setting threshold.
 * getBtnReviewReports: opens the window for reviewing reports. getLogoutBtn:
 * sends a logout request to the server and opens the login window.
 * getBtnStockAlerts: opens the window for low stock alerts. getBtnCancelOrders:
 * opens the window for canceling orders. setRegionalManagerLocation: sets the
 * location of the manager.
 * 
 * @author Dima, Maxim, Rotem
 *
 */
public class EktRegionalManagerHomePageController {

	@FXML
	private Button btnLowStockAlerts;

	@FXML
	private Button btnCancelOrders;

	@FXML
	private Button btnAcceptCustomers;

	@FXML
	private Button btnLogout;

	@FXML
	private Button btnReviewReports;

	@FXML
	private Button btnSetThreshold;

	@FXML
	private Text txtManagerWelcome;

	/**
	 * The initialize method is used to initialize the elements of the
	 * EktRegionalManagerHomePageController class and is called automatically when
	 * the FXML file is loaded. It sets the location of the regional manager, sets
	 * the welcome message for the manager, and positions the welcome message in the
	 * center of the screen.
	 */
	public void initialize() {
		setRegionalManagerLocation();
		txtManagerWelcome
				.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back!");
		txtManagerWelcome.setLayoutX(400 - (txtManagerWelcome.minWidth(0)) / 2);
	}

	/**
	 * 
	 * The method is called when the "Accept New Customers" button is pressed. It
	 * creates a new window for the "Ekt Manage New Customers" page and closes the
	 * current window.
	 * 
	 * @param event The event of pressing the "Accept New Customers" button.
	 */
	@FXML
	void getBtnAcceptCustomers(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktRegionalManagerAcceptNewCustomer.fxml", null,
				"Ekt Manage New Customers", false);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * Handles the "Set Threshold" button press. Creates a new primary stage and
	 * opens the "EktRegionalManagerSetThreshold.fxml" window. Closes the current
	 * window.
	 * 
	 * @param event - the action event that triggered the method call
	 */
	@FXML
	void getBtnSetThreshold(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktRegionalManagerSetThreshold.fxml", null, "Reviews",
				false);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

	}

	/**
	 * 
	 * Handles the button press event for the "Review Reports" button. It creates a
	 * new primary stage and displays the "EktReportSelectForm" window. The current
	 * window is closed.
	 * 
	 * @param event the action event that triggered this method call.
	 */
	@FXML
	public void getBtnReviewReports(ActionEvent event) {

		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktReportSelectForm.fxml", null, "Reviews", false);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

	}

	/**
	 * This method handles the action of the logout button. It sends a logout
	 * request to the server and closes the current window, then opens a new login
	 * window.
	 * 
	 * @param event The event that triggers this method, in this case the logout
	 *              button being clicked.
	 */
	@FXML
	public void getLogoutBtn(ActionEvent event) {
		ClientController.sendLogoutRequest();
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", false);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * This method is the event handler for the "Low Stock Alerts" button. When the
	 * button is clicked, it will create a new stage and open the
	 * "EktLowStockAlertTable.fxml" scene. The current window will be closed.
	 * 
	 * @param event the action event that triggered this method call
	 */
	@FXML
	public void getBtnStockAlerts(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktLowStockAlertTable.fxml", null, "Low Stock Alert Table",
				false);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * 
	 * This method is called when the "Cancel Order" button is pressed. It creates a
	 * new window for canceling orders and closes the current window.
	 * 
	 * @param event The event that triggers the method.
	 */
	@FXML
	public void getBtnCancelOrders(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCancelOrdersPage.fxml", null, "Ekt Cancel Order",
				false);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * This method sets the current regional manager's location. It retrieves the
	 * location name from the database using a SELECT statement, sets it to the
	 * current user's region, and sends a request message to the server to get the
	 * location name of the current manager.
	 */
	private void setRegionalManagerLocation() {
		int currentManagerID = ClientController.getCurrentSystemUser().getId();
		SCCP getCurrentManagerLocationNameRequestMessage = new SCCP();
		getCurrentManagerLocationNameRequestMessage.setRequestType(ServerClientRequestTypes.SELECT);
		getCurrentManagerLocationNameRequestMessage.setMessageSent(new Object[] {
				"manager_location LEFT JOIN ektdb.locations on locations.locationID = manager_location.locationId",
				true, "locationName", true, "idRegionalManager = " + currentManagerID, false, null });

		ClientUI.getClientController().accept(getCurrentManagerLocationNameRequestMessage);

		ArrayList<?> currentManagerLocationName = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		@SuppressWarnings("unchecked")
		String locationName = ((ArrayList<Object>) currentManagerLocationName.get(0)).get(0).toString();
		ClientController.setCurrentUserRegion(locationName);
	}

}
