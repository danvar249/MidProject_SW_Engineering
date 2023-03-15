package controllers;

import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Role;
import logic.SystemUser;
/**
 * The EktServiceRepAddCustomerController class is a JavaFX controller class that is responsible for handling the events
 * and actions that occur in the EktServiceRepAddCustomer FXML file. It allows service representatives to add new customers
 * to the system by providing the necessary information and validating the input.
 * @author Maxim, Rotem, Dima
 * @version 1
 *
 */
public class EktServiceRepAddCustomerController {

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


	@FXML TextField txtRole;

	@FXML Button btnAdd;

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
    private Text lblStatus;

    @FXML
    private Label lblUsername;

	@FXML Button btnGoBack;
	
    @FXML
    private Label lblLocation;
	
	@FXML
	private ComboBox<String> cmbLocation;
	
	private String userLocation;
	/**
	 * This method initializes the ComboBox "cmbLocation" by adding all the possible locations to it.
	 * It also sets an action listener on the ComboBox, when an item is selected it sets the value of the userLocation variable to the selected location.
	 */
	public void initialize() {
		cmbLocation.getItems().addAll("North", "South", "UAE");
		cmbLocation.setOnAction(event ->{
			userLocation = cmbLocation.getValue();
		});
	}
	/**
	 * getAddUserToDB is an event handler method that is triggered when the "Add" button is clicked.
	 * It is responsible for adding a new user to the database by first validating input fields, checking if the
	 * entered username is already taken, and sending a message to the server to add the user to the database.
	 * It also updates the status label with a success or failure message and clears all input fields after adding the user.
	 * @param event the ActionEvent that triggers this method when the "Add" button is clicked.
	 */
	@FXML
    void getAddUserToDB(ActionEvent event) {
		clearAllLabels();
    	int id;
		lblStatus.setText("Checking input");
    	SCCP preparedMessage = new SCCP();
    	if(validFieldInput()) {
    		// check if username is taken
    		ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.SELECT, 
    				new Object[] {"systemuser", 
    								true, "username",
    								true, "username='"+txtUsername.getText()+"'",
    								false, null}));
    		@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
    		if(res.size() != 0) {
        		// show a pop up that lets the user know he has no open orders. return user to previous page!
        		Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setTitle("User name taken!");
                alert.setHeaderText("User name="+txtUsername.getText()+" is already taken, use another one.");
                alert.setContentText("Return to form.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                	System.out.println("Returning to form (add customer)");
                }
            	txtUsername.setText("");
            	return;
    		}
    		id = Integer.valueOf(txtID.getText());
    		// set message accordingly
    		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
    		// first field is table name - users here
    		Object[] fill = new Object[3];
    		fill[0] = "systemuser"; // add to table "systemusers" (hard code it elsewhere)
    		fill[1] = false; // add only 1
    		fill[2] = new Object[] {new SystemUser(id, txtFirstName.getText(), txtLastName.getText(), 
    				txtPhoneNumber.getText(), txtEmail.getText(), txtCreditCard.getText(), 
    				txtUsername.getText(), txtPassword.getText(), Role.UNAPPROVED_CUSTOMER)};
    		preparedMessage.setMessageSent(fill);
    		
    		// send to server
    		ClientUI.getClientController().accept(preparedMessage);
    		
    		// check comm for answer:
    		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
    			// add test that response.messageSent is the array we had in fill[2] (SAME OBJECT)
    			lblStatus.setText("Successfully added a " + txtRole.getText()+"!");
    			// clean all fields:
    			txtID.setText("");
    			txtFirstName.setText("");
    			txtLastName.setText("");
    			txtPhoneNumber.setText("");
    			txtEmail.setText("");
    			txtCreditCard.setText("");
    			txtUsername.setText("");
    			txtPassword.setText("");

    			SCCP addBalance = new SCCP();
    			addBalance.setRequestType(ServerClientRequestTypes.ADD);
	    		// first field is table name - users here
	    		Object[] fillBalance = new Object[3];
	    		fillBalance[0] = "customer_balance"; // add to table "customer_balance" (hard code it elsewhere)
	    		fillBalance[1] = false; // add only 1
	    		fillBalance[2] = new Object[] {"(" + id + ", " + 0 + ")"};
	    		addBalance.setMessageSent(fillBalance);
    			
    			if(userLocation != null) {
    		    	SCCP addLocation = new SCCP();
    		    	addLocation.setRequestType(ServerClientRequestTypes.ADD);
    	    		// first field is table name - users here
    	    		Object[] fillLocation = new Object[3];
    	    		fillLocation[0] = "customer_location"; // add to table "customer_location" (hard code it elsewhere)
    	    		fillLocation[1] = false; // add only 1
    	    		fillLocation[2] = new Object[] {"(" + id + ", '" + userLocation + "')"};
    	    		addLocation.setMessageSent(fillLocation);
    	    		
    	    		// send to server
    	    		ClientUI.getClientController().accept(addLocation);
    			}
    		}
    		else {
    			lblStatus.setText("ERROR! Could not add user to database."); // add specifics
    		}
    		
    	}
    	else {
    		lblStatus.setText("Status: Invalid input");
    	}
    }
	/**
	 * This method is used to clear all the labels for the fields in the form.
	 */
	private void clearAllLabels() {
		// TODO Auto-generated method stub
		lblId.setText("");
		lblUsername.setText("");
		lblName.setText("");
		lblPhone.setText("");
		lblEmail.setText("");
		lblCreditCard.setText("");
		lblLocation.setText("");
	}

	// lblId, lblUsername, lblName, lblEmail, lblCreditCard, lblPhone
	/**
	 * 
	 * This method is used to validate the input fields for adding a user to the database.
	 * It checks that the ID is numeric and not empty, the username and password are non-empty,
	 * the first and last name are alphabetic, non-empty and that the email is in the format of x@y.z,
	 * the credit card is numeric and non-empty, the phone number is numeric and non-empty, and location is selected.
	 * If any of these conditions are not met, a corresponding error message is displayed on the form.
	 * @return boolean indicating whether the input fields are valid or not.
	 */
	private boolean validFieldInput() {
		boolean flag = true;
		// I forgot to check ID
		if(txtID.getText().length() < 1 || (!txtID.getText().matches("^[0-9]*$"))) {
			lblId.setText("ID must be numeric and not empty");
			flag = false;
		}
		// check username and password not empty:
		if(txtUsername.getText().length() < 1 || txtPassword.getText().length() < 1) {
			lblUsername.setText("User name and password must be non-empty");
			flag = false;
		}
		// check name is letters
		if(txtFirstName.getText().length() < 2 || !(txtFirstName.getText().matches("^[a-zA-Z]*$")) || 
				txtLastName.getText().length() < 2 || !(txtLastName.getText().matches("^[a-zA-Z]*$"))) {
			lblName.setText("First and last name must be alphabetic, non empty ");
			flag = false;
		}
		// check email is not empty
		if(!(txtEmail.getText().contains("@")) || !(txtEmail.getText().contains(".")) ||  txtEmail.getText().length() < 5) {
			lblEmail.setText("Email must be of the format x@y.z");
			flag = false;
		}
		// check credit-card is legit (why?! we need to remove credit card from user (user needs ONLY user,pass, id, role!))
		if (!(txtCreditCard.getText().matches("^[0-9.-]+$"))) {
			lblCreditCard.setText("Credit card must be numeric and non empty");
			flag = false;
		}
		// proper check (for valid companies)
		/*if(!(txtCreditCard.getText().matches("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])"
				+ "[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$")))
			return false;*/
		
		// check phone is a number (currently not letters)
		if(!(txtPhoneNumber.getText().matches("^[0-9.-]+$"))) {
			lblPhone.setText("Phone number must be numeric and non empty");
			flag = false;
		
		}
		
		if(cmbLocation.getValue() == null) {
			lblLocation.setText("ERROR! you need to select locatin");
			flag = false;
		}
		return flag;
	}
	/**
	 * Handles the action of the "Go Back" button. Hides the current window and opens the Service Representative Home Page.
	 * @param event The ActionEvent object that triggers this method.
	 */
	@FXML public void getBtnGoBack(ActionEvent event) {
		// load home area for service rep
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepresentativeHomePage.fxml", null, "Service Rep Home Page", true);
		primaryStage.show();
		
	}



}
