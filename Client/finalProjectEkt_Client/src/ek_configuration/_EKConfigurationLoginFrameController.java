package ek_configuration;

import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.SystemUser;
import javafx.scene.text.Text;

/**
 * 
 * _EKConfigurationLoginFrameController is responsible for handling the login
 * process for the system. The class implements a JavaFX controller for the
 * login frame of the system, it contains methods that handle the login process,
 * and also methods that control the fast-recognition feature of the system.
 * 
 * @author Nastya
 */
public class _EKConfigurationLoginFrameController {

	@FXML
	private Button btnLogin;

	@FXML
	private PasswordField pwdField;

	@FXML
	private Label statusLabel;

	@FXML
	private TextField txtUsername;

	@FXML
	Text lblFastRecognition;

	@FXML
	Text lblFastRecognition2;

	/**
	 * A method that triggers the login process when the user press the enter key.
	 * 
	 * @param ae the ActionEvent object.
	 */
	@FXML
	void onEnter(ActionEvent ae) {
		getBtnLoginEK(ae);
	}

	/**
	 * A method that initializes the login frame, this method is automatically
	 * called when the frame is loaded.
	 */
	@FXML
	private void initialize() {
		ClientController.resetVars();
		ClientController.setCustomerIsSubsriber(false);

		if (ClientController.isFastRecognitionToggle()) {
			// set fast recognition
			lblFastRecognition.setText("Using fast-recognition");
			lblFastRecognition2.setText("");
			txtUsername.setDisable(true);
			pwdField.setDisable(true);
		} else {
			lblFastRecognition.setText("USERNAME");
			lblFastRecognition2.setText("PASSWORD");
			txtUsername.setDisable(false);
			pwdField.setDisable(false);
		}
		statusLabel.setTextFill(Color.RED);
		statusLabel.setStyle("-fx-font-weight: bold;");
	}

	/**
	 * 
	 * This method represents the action listener for the login button. It retrieves
	 * the entered username and password, and sends a login request to the server.
	 * If the login is successful, it sets the current user and redirects to the
	 * appropriate home screen. If the login is unsuccessful, it sets the status
	 * label to display an error message.
	 * 
	 * @param event the ActionEvent that triggers this method, in this case the
	 *              login button being clicked
	 */
	@FXML
	void getBtnLoginEK(ActionEvent event) {
		statusLabel.setVisible(false);
		String userName, password, nextScreenPath = null, nextPathTitle = "Frame";
		userName = txtUsername.getText();
		password = pwdField.getText();

		userName = txtUsername.getText();
		password = pwdField.getText();
		if (ClientController.isFastRecognitionToggle()) {
			userName = ClientController.getFastRecognitionUserName();
			password = ClientController.getFastRecognitionPassword();
		}
		System.out.println(userName + " " + password);

		ClientController.setCustomerIsSubsriber(false);

		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.LOGIN);
		preparedMessage.setMessageSent(new String[] { userName, password });
		// request the login:
		// send to server
		System.out.println("Client: Sending login request to server as " + userName + ".");
		ClientController.setCustomerIsSubsriber(false);
		ClientUI.getClientController().accept(preparedMessage);
		// check client-side object for answer:
		// if login succeeded:
		if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			SystemUser su = (SystemUser) ClientController.responseFromServer.getMessageSent();
			System.out.println("Logged in successfuly as " + su);
			ClientController.setCurrentSystemUser(su);
			ClientController.setCurrentUserRole(su.getRole());
			switch (su.getRole()) {
			case SUBSCRIBER:
				ClientController.setCustomerIsSubsriber(true);
				nextScreenPath = "/gui/_EKConfigurationCustomerHomeArea.fxml";
				nextPathTitle = "Customer Home Frame";
				break;
			case CUSTOMER:
				if (ClientController.isFastRecognitionToggle()) {
					ClientController.setFastRecognitionToggle(false);
					ClientController.sendLogoutRequest();
					// show alert and reload window
					Stage prevStage = new Stage();
					WindowStarter.createWindow(prevStage, this, "/gui/_EKConfigurationLoginFrame.fxml", null,
							"Ekt Login", true);
					prevStage.show();
					((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Oops!");
					alert.setHeaderText("Your credentials are invalid!");
					alert.setContentText("Click OK and try again . . . ");
					alert.initStyle(StageStyle.UNDECORATED);
					alert.showAndWait();
					return;
				}
				nextScreenPath = "/gui/_EKConfigurationCustomerHomeArea.fxml";
				nextPathTitle = "Customer Home Frame";
				break;

			case INVENTORY_WORKER:
				nextScreenPath = "/gui/_EKConfigurationLogisticsEmployeeFrame.fxml";
				nextPathTitle = "Logistics Employee Frame";
				break;
			case UNAPPROVED_CUSTOMER:

				ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.LOGOUT,
						ClientController.getCurrentSystemUser().getUsername()));
				statusLabel.setText("Unapproved customer cannot log in.");
				statusLabel.setVisible(true);

				return;
			// break;
			case UNAPPROVED_SUBSCRIBER:
				ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.LOGOUT,
						ClientController.getCurrentSystemUser().getUsername()));
				statusLabel.setText("Unapproved customer cannot log in.");
				statusLabel.setVisible(true);

				return;
			// break;
			default:
				statusLabel.setText("EK Configuration only supports customers and machine maintenance employees.");
				statusLabel.setVisible(true);
				if (ClientController.getCurrentSystemUser() != null) {
					// log him out:
					ClientController.sendLogoutRequest();
				}
				return;
			}

		} else {
			if(ClientController.responseFromServer.
					getRequestType().
					equals(ServerClientRequestTypes.LOGIN_FAILED_ALREADY_LOGGED_IN))
				statusLabel.setText("User already logged in!");
			else
				statusLabel.setText("Invalid input in login!");
			statusLabel.setVisible(true);
			return;
		}
		System.out.println("Login EK -> " + nextScreenPath);
		// move user to next screen
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), nextScreenPath, null, nextPathTitle, true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}

	/**
	 * 
	 * This method is used to check if the currently logged in subscriber has any
	 * previous orders or not. It sends a query to the server to select the orderID
	 * from customer_orders table where customerId matches the id of the currently
	 * logged in subscriber. If the query returns an empty result set, it returns
	 * true, indicating that it is the first order for the subscriber. Else, it
	 * returns false.
	 * 
	 * @return boolean - true if it is the first order for the subscriber, else
	 *         false.
	 */
	static boolean firstOrderForSubscriber() {

		if (ClientController.getCustomerIsSubsriber() == null || !ClientController.getCustomerIsSubsriber()) {
			System.out.println("Invalid call to firstOrderForSubscriber() -> connected user is not a subsriber");
			return false;

		}
		ClientUI.getClientController()
				.accept(new SCCP(ServerClientRequestTypes.SELECT, new Object[] { "customer_orders", true, "orderID",
						true, "customerId = " + ClientController.getCurrentSystemUser().getId(), false, null }));
		if (!ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			System.out
					.println("Invalid database operation (checking subsriber orders history failed). (returnin false)");
			return false;
		}
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer
				.getMessageSent();
		// true if we have NO ORDERS else false
		return res.size() == 0;
	}

}
