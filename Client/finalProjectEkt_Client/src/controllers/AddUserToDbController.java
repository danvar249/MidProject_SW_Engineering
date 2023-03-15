package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Role;
import logic.SystemUser;
import javafx.scene.control.ComboBox;

/**
 *
 *
 * The AddUserToDbController class is responsible for controlling the behavior
 * and actions of the UI elements
 *
 * related to adding a user to the database.
 *
 * @author [maxim]
 *
 * @version [1.0]
 *
 * @since [12.12.22]
 */
public class AddUserToDbController {

	@FXML
	private Button btnConnect;

	@FXML
	private TextField txtCreditCard;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtFirstName;

	@FXML
	private TextField txtLastName;

	@FXML
	private TextField txtPassword;

	@FXML
	private TextField txtPhoneNumber;

	@FXML
	private TextField txtID;

	@FXML
	private TextField txtUsername;

	@FXML
	Text lblStatus;

	@FXML
	TextField txtRole;

	@FXML
	Button btnAdd;

	@FXML
	Button btnBack;

	@FXML
	private Label lblCreditCard;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblId;

	@FXML
	private Label lblName;

	@FXML
	private Label lblPhone;

	@FXML
	private Label lblUsername;

	@FXML
	private Label lblRole;

	@FXML
	ComboBox<Role> cmbRole;

	/**
	 * 
	 * This method is called when the FXML file is loaded. It initializes the values
	 * of the role ComboBox.
	 */
	@FXML
	private void initialize() {
		Role[] roles = new Role[] { Role.CUSTOMER, Role.DELIVERY_WORKER, Role.DIVISION_MANAGER,
				Role.INVENTORY_WORKER, Role.LOGISTICS_MANAGER, Role.REGIONAL_MANAGER,
				Role.SALES_MANAGER, Role.SALES_WORKER, Role.SERVICE_REPRESENTATIVE, Role.SUBSCRIBER,
				Role.UNAPPROVED_CUSTOMER, Role.UNAPPROVED_SUBSCRIBER };
		cmbRole.getItems().addAll(Arrays.asList(roles));
		cmbRole.setValue(Role.UNAPPROVED_CUSTOMER);

	}

	/**
	 * This method is called when the Add User button is clicked. It checks the
	 * input fields for validity and adds the user to the database.
	 *
	 * @param event The ActionEvent object representing the button click event.
	 */
	@FXML
	void getAddUserToDB(ActionEvent event) {
		Integer id;
		lblStatus.setText("Checking input");
		SCCP preparedMessage = new SCCP();
		if (validFieldInput()) {
			// Rotem added: check if username already exists!
			ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.SELECT, new Object[] { "systemuser",
					true, "username", true, "username='" + txtUsername.getText() + "'", false, null }));
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer
					.getMessageSent();
			if (res.size() != 0) {
				// show a pop up that lets the user know he has no open orders. return user to
				// previous page!
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.initStyle(StageStyle.UNDECORATED);
				alert.setTitle("User name taken!");
				alert.setHeaderText("User name=" + txtUsername.getText() + " is already taken, use another one.");
				alert.setContentText("Return to form.");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					System.out.println("Returning to form (add system user)");
				}
				txtUsername.setText("");
				return;
			}

			try {
				id = Integer.valueOf(txtID.getText());
			} catch (NumberFormatException ex) {
				lblStatus.setText("Invalid ID input (too long).");
				return;
			}
			// set message accordingly
			preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
			// first field is table name - users here
			Object[] fill = new Object[3];
			fill[0] = "systemuser"; // add to table "systemusers" (hard code it elsewhere)
			fill[1] = false; // add only 1
			fill[2] = new Object[] { new SystemUser(id, txtFirstName.getText(), txtLastName.getText(),
					txtPhoneNumber.getText(), txtEmail.getText(), txtCreditCard.getText(), txtUsername.getText(),
					txtPassword.getText(), cmbRole.getValue()) };
			preparedMessage.setMessageSent(fill);

			// send to server
			ClientUI.getClientController().accept(preparedMessage);

			// check comm for answer:
			if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
				// add test that response.messageSent is the array we had in fill[2] (SAME
				// OBJECT)
				lblStatus.setText("Successfully added a " + cmbRole.getValue().toString().toLowerCase() + "!");
				// clean all fields:
				txtID.setText("");
				txtFirstName.setText("");
				txtLastName.setText("");
				txtPhoneNumber.setText("");
				txtEmail.setText("");
				txtCreditCard.setText("");
				txtUsername.setText("");
				txtPassword.setText("");
				// txtRole.setText("");
				System.out.println("haveBalance = " + haveBalance());
				if (haveBalance()) {
					System.out.println("Adding balance to " + id);
					SCCP addBalance = new SCCP();
					addBalance.setRequestType(ServerClientRequestTypes.ADD);
					// first field is table name - users here
					Object[] fillBalance = new Object[3];
					fillBalance[0] = "customer_balance"; // add to table "customer_balance" (hard code it elsewhere)
					fillBalance[1] = false; // add only 1
					fillBalance[2] = new Object[] { "(" + id + ", " + 0 + ")" };
					addBalance.setMessageSent(fillBalance);
					// send to server
					ClientUI.getClientController().accept(addBalance);
				}

			} else {
				lblStatus.setText("ERROR!"); // add specifics
			}

		} else {
			lblStatus.setText("Status: Invalid input");
		}
	}

	/**
	 * 
	 * This method is responsible for validating the input fields of the user. It
	 * checks that the user has entered a valid ID, username, password, first name,
	 * last name, email, credit card, phone number and role. If any of the fields
	 * are not valid, it will display an error message and return false. If all
	 * fields are valid it will return true.
	 * 
	 * @return true if all fields are valid, false otherwise
	 */

	private boolean validFieldInput() {
		boolean flag = true;
		// I forgot to check ID
		if (txtID.getText().length() < 1 || (!txtID.getText().matches("^[0-9]*$"))) {
			lblId.setText("ID must be numeric and not empty");
			flag = false;
		}
		// check username and password not empty:
		if (txtUsername.getText().length() < 1 || txtPassword.getText().length() < 1) {
			lblUsername.setText("User name and password must be non-empty");
			flag = false;
		}
		// check name is letters
		if (txtFirstName.getText().length() < 2 || !(txtFirstName.getText().matches("^[a-zA-Z]*$"))
				|| txtLastName.getText().length() < 2 || !(txtLastName.getText().matches("^[a-zA-Z]*$"))) {
			lblName.setText("First and last name must be alphabetic, non empty ");
			flag = false;
		}
		// check email is not empty
		if (!(txtEmail.getText().contains("@")) || !(txtEmail.getText().contains("."))
				|| txtEmail.getText().length() < 5) {
			lblEmail.setText("Email must be of the format x@y.z");
			flag = false;
		}
		// check credit-card is legit (why?! we need to remove credit card from user
		// (user needs ONLY user,pass, id, role!))
		if (!(txtCreditCard.getText().matches("^[0-9.-]+$"))) {
			lblCreditCard.setText("Credit card must be numeric and non empty");
			flag = false;
		}

		// check phone is a number (currently not letters)
		if (!(txtPhoneNumber.getText().matches("^[0-9.-]+$"))) {
			lblPhone.setText("Phone number must be numeric and non empty");
			flag = false;
		}

		return flag;
	}

	/**
	 * This method is called when the Back button is clicked. It closes the current
	 * window and returns to the previous window.
	 *
	 * @param event The ActionEvent object representing the button click event.
	 */
	@FXML
	public void getBtnBack(ActionEvent event) {
		//
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktDivisionManagerHomePage.fxml", null,
				"Ekt Division Manager", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}

	/**
	 * Handles the event when the user selects a role from the role combobox.
	 * 
	 * @param event the action event that triggered the method call
	 */
	@FXML
	public void getCmbRole(ActionEvent event) {
	}

	/**
	 * This method checks if the selected user role has a balance or not. It returns
	 * true if the selected role is subscriber, unapproved subscriber, unapproved
	 * customer, or customer. Otherwise, it returns false.
	 *
	 * @return true if the user role has a balance, false otherwise
	 */
	private boolean haveBalance() {
		if (cmbRole.getValue() == Role.SUBSCRIBER || cmbRole.getValue() == Role.UNAPPROVED_SUBSCRIBER
				|| cmbRole.getValue() == Role.UNAPPROVED_CUSTOMER || cmbRole.getValue() == Role.CUSTOMER)
			return true;
		return false;

	}

}
