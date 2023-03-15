
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
 * 
 *This class represents a controller for the Service Representative Add Subscriber page of the system.
 *It allows the Service Representative to add a new subscriber to the system by filling in a form and submitting it.
 *The class contains methods to initialize the page, go back to the previous page,
 *validate the input fields, check if the chosen username is already taken,
 *add the new subscriber to the database and display a message indicating the success of the operation.
 *@author Dima, Rotem, Maxim
 *
 */
public class EktServiceRepAddSubscriberController {

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
    
    @FXML
    private Label lblLocation;

	@FXML Button btnGoBack;
	
	@FXML
	private ComboBox<String> cmbLocation;
	
	private String userLocation;

	/**
	 * This method initializes the location combo box with the options "North", "South", and "UAE" and sets an event listener that
	 * assigns the selected location to the variable userLocation when a location is selected.
	 */
	public void initialize() {
		cmbLocation.getItems().addAll("North", "South", "UAE");
		cmbLocation.setOnAction(event ->{
			userLocation = cmbLocation.getValue();
		});
	}
	
	/**
	 * This method is the event handler for the "Add User to DB" button. It is responsible for validating the input fields,
	 * checking if the desired username is already taken, and sending the necessary information to the server to add a new user
	 * to the database.
	 * @param event The event that triggered this method, in this case the "Add User to DB" button being clicked.
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
    				txtUsername.getText(), txtPassword.getText(), Role.UNAPPROVED_SUBSCRIBER)};
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
	    		
	    		// send to server
	    		ClientUI.getClientController().accept(addBalance);
    			
    			
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
	 * This method is used to clear all the error labels on the form. It will set the text of all the error labels to an empty string.
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

	@FXML public void getBtnGoBack(ActionEvent event) {
		// load home area for service rep
		// sammy D the current window
		((Node)event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();

		WindowStarter.createWindow(primaryStage, this, "/gui/EktServiceRepresentativeHomePage.fxml", null, "Service Rep Home Page",false);
		primaryStage.show();
		
	}



}
