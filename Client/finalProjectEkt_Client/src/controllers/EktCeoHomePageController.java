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
 * The EktCeoHomePageController class is responsible for handling the
 * functionality of the CEO home page GUI. It contains a button that allows the
 * CEO to log out of the application and a greeting text that displays the CEO's
 * first and last name.
 * 
 * @author danielvardimon
 *
 */
public class EktCeoHomePageController {

	@FXML
	private Button btnBack;

	@FXML
	private Text txtGreeting;

	/**
	 * 
	 * The initialize method is called when the class is first instantiated. It sets
	 * the text of the txtGreeting variable to include the CEO's first and last
	 * name.
	 */
	@FXML
	private void initialize() {
		// we set greeting to have both first and last name (honor & respect)
		txtGreeting.setText(
				txtGreeting.getText().replace(", ", ", " + ClientController.getCurrentSystemUser().getFirstName() + " "
						+ ClientController.getCurrentSystemUser().getLastName()));
	}

	/**
	 * 
	 * The getBackBtn method is used to log the CEO out of the application and
	 * redirect them to the login page.
	 * 
	 * @param event The event that triggered the method call.
	 */
	@FXML
	void getBackBtn(ActionEvent event) {
		System.out.println(
				"CEO with username=" + ClientController.getCurrentSystemUser().getUsername() + " is logging out.");
		ClientController.sendLogoutRequest();
		// move to new window
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", false);

		primaryStage.show();

	}

}
