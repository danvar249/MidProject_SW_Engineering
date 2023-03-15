package controllers;

import java.io.IOException;
import client.ClientController;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class is the controller for the Sales Manager screen, 
 * which is used to handle the GUI events of the Sales Manager screen.
 * It controls the buttons and actions on the screen such as adding new promotions, 
 * activating promotions and logging out.
*/
public class SalesManagerController {

	@FXML
	private Button btnAddNewPromotion;

	@FXML
	private Button btnActivatePromotion;

	@FXML
	private Button btnLogout;

	@FXML
	private Text txtSalesManager;

	/**
	 * This method is called when the "Add new promotion" button is pressed.
	 * It opens the Promotion Editing window, and closes the current window.
	 * @param event the event of the button press
	 * @throws IOException 
	 */
	@FXML
	private void addNewPromotionHandler(ActionEvent event) throws IOException {
		// Get the current stage
		Stage stage = new Stage();
		WindowStarter.createWindow(stage, new Object(), "/gui/PromotionEditing.fxml", null, "Promotion Editing", false);
		stage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
	}

	/**
	 * This method is called when the "Activate Promotion" button is pressed.
	 * It opens the Edit Active Promotions window, and closes the current window.
	 * @param event the event of the button press
	 */
	@FXML
	private void getBtnActivatePromotion(ActionEvent event) {
		Stage stage = new Stage();

		WindowStarter.createWindow(stage, new Object(), "/gui/EditActivePromotions.fxml", null, "Promotion Editing",
				false);
		stage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
	}

	/**
	 * The SalesManagerController class is responsible for handling the actions of the Sales Manager in the system.
	 * It contains methods that handle the following actions:
	 */
	@FXML
	private void logoutHandler(ActionEvent event) throws Exception {
		// actually do the logout:
		ClientController.sendLogoutRequest();
		// log
		System.out.println("Sales manager has logged off");
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", false);
		primaryStage.show();
	}
	 /**
	 * The initialize method is called by the JavaFX framework when the FXML file is loaded.
	 * It is used to set the initial state of the elements in the scene.
	 * In this case, it sets the text of the txtSalesManager Text element to welcome the current user.
	 * And it sets the position of txtSalesManager to the center.
	 */
	@FXML
	public void initialize() {
		txtSalesManager.setText("Welcome back, " + ClientController.getCurrentSystemUser().getFirstName() + "!");
		txtSalesManager.setLayoutX(400 - (txtSalesManager.minWidth(0) / 2));
	}
}
