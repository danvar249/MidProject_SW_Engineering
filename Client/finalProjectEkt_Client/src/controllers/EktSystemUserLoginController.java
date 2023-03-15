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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Role;
import logic.SystemUser;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 *
 */
public class EktSystemUserLoginController {
	
	public class LoginHelper{
		private SCCP responseFromServer;
		public ServerClientRequestTypes responseType;
		
		
		public String nextPage, title;
		public SystemUser loggedUser = null;
		String statusLabelString = null;
		Role role = null;
		boolean newFastRecState;
		boolean useServerResponseObject;
		LoginHelper(boolean useServer){
			this.useServerResponseObject = useServer;
		}
		
		
		/**
		 * Refactored login method -> used to avoid JavaFX dependency.
		 * Server dependency managed with a boolean parameter, with which 
		 * the method assesses whether to access the genuine server SCCP object
		 * (the response from server) or a stub object passed to it.
		 * @param userName login user name
		 * @param password login password
		 */
		public void login(String userName, String password) {
			if(userName == null || password == null) {
				throw new NullPointerException();
			}
			
	    	// ask to connect
			SCCP actualResponse;
	    	SCCP preparedMessage = new SCCP();
	    	preparedMessage.setRequestType(ServerClientRequestTypes.LOGIN);
	    	preparedMessage.setMessageSent(new String[] {userName, password});
	    	// request the login:
			// send to server
	    	System.out.println("Client: Sending login request to server as " + userName+".");
			

			// check client-side object for answer:
			// if login succeeded:
			if (useServerResponseObject) {
				ClientUI.clientController.accept(preparedMessage);
				setResponseFromServer(ClientController.responseFromServer);
			}
			
			
			actualResponse = getResponseFromServer();
			// responseFrom = {ServerClientRequestTypes, Object}
			responseType = actualResponse.getRequestType();	

			
			if(responseType.equals(ServerClientRequestTypes.ACK)) {
				// add test that response.messageSent is the array we had in fill[2] (SAME OBJECT)
				loggedUser =  (SystemUser)actualResponse.getMessageSent();
				statusLabelString = "Successfully connected as: " + loggedUser.getUsername() +".";
				role = loggedUser.getRole();

				// switch statement for the current user's role
				switch(loggedUser.getRole()) {
				case SUBSCRIBER:
					// set subscriber boolean value (true)
					nextPage = "/gui/EktCatalogForm.fxml";
					title = "Ekt Catalog";
					break;			
				case CUSTOMER:
					nextPage = "/gui/EktCatalogForm.fxml";
					title = "Ekt Catalog";
					break;
					
				case REGIONAL_MANAGER:
					// TODO: same as customer
					nextPage = "/gui/EktRegionalManagerHomePage.fxml";
					title = "Regional Manager Home Page";
					break;
					
				case SERVICE_REPRESENTATIVE:
					nextPage = "/gui/EktServiceRepresentativeHomePage.fxml";
					title = "Service Rep Home Page";
					break;
					
				case DIVISION_MANAGER:
					nextPage = "/gui/EktDivisionManagerHomePage.fxml";
					title = "Female Division Manager Home Page";
					break;
				
				case SALES_MANAGER:
					nextPage = "/gui/SalesManager.fxml";
					title = "Sales Manager";
					break;
					
				case SALES_WORKER:
					nextPage = "/gui/SalesDepartmentWorker.fxml";
					title = "Ekt Sales Department Worker";
					break;
				case DELIVERY_WORKER:
					nextPage = "/gui/DeliveryManagerPage.fxml";
					title = "Ekt Delivery Department Worker";
					break;
				case UNAPPROVED_CUSTOMER:
					statusLabelString = "Customer not yet approved!";
					return;
				case UNAPPROVED_SUBSCRIBER:
					statusLabelString = "Subscriber not yet approved!";
					return;
				case INVENTORY_WORKER:
					nextPage =  "/gui/InventoryRestockWorkerPage.fxml";
					title = "Ekt Inventory Worker";
					break;
				default:
					throw new UnsupportedOperationException("No valid landing page for system user with role=" + role);
				}			

			}
			
			// login failed
			// Rotem 1.12.23 added granularity
			else {
				if(responseType.equals(ServerClientRequestTypes.LOGIN_FAILED_ILLEGAL_INPUT)) {
					statusLabelString = ("Invalid input for login");
				}
				else if(responseType.equals(ServerClientRequestTypes.LOGIN_FAILED_ALREADY_LOGGED_IN)) {
					statusLabelString =("Sorry, user is already logged in");
				}
				else {
					statusLabelString =("ERROR (unspecified login error)!"); // add specifics
				}
			}
		}


		public SCCP getResponseFromServer() {
			return responseFromServer;
		}


		public void setResponseFromServer(SCCP responseFromServer) {
			this.responseFromServer = responseFromServer;
		}
		
	}
	
	public SystemUser currentUser = null;
	
    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;
    
	@FXML 
	Label statusLabel;

	@FXML Text lblFastRecognition1;

	@FXML Text lblFastRecognition2;
	
	
	/**
	 * This event grabs the enter key when we are on the password field.
	 * @param ae
	 */
	@FXML
	public void onEnter(ActionEvent ae){
	   getBtnLogin(ae);
	}

	/*
	 * This method does nothing
	 */
	@FXML
	private void initialize() {
		// don't worry, this DOES NOT reset the fast-recognition variables
		ClientController.resetVars();
		if(ClientController.isFastRecognitionToggle()) {
			// set fast recognition
			lblFastRecognition1.setText("Using fast-recognition");
			lblFastRecognition2.setText("");
			txtUsername.setDisable(true);
			txtPassword.setDisable(true);
		}
		else{
			lblFastRecognition1.setText("USERNAME");
			lblFastRecognition2.setText("PASSWORD");
			txtUsername.setDisable(false);
			txtPassword.setDisable(false);
		}
		statusLabel.setTextFill(Color.RED);
		statusLabel.setStyle("-fx-font-weight: bold;");
	}
	
	/**
	 * This is the login method for a system user
	 * TODO: write tests for it
	 * @param event: not used
	 */
	@FXML
    public void getBtnLogin(ActionEvent event) {
		WindowStarter.mySoundPlayer();
		// Rotem 1.12.23
		// hide the status label until we finish login attempt
		statusLabel.setVisible(false);
		
    	String userName, password;
    	userName = txtUsername.getText();
    	password = txtPassword.getText();
    	if(ClientController.isFastRecognitionToggle()) {
    		userName=ClientController.getFastRecognitionUserName();
    		password=ClientController.getFastRecognitionPassword();
    	}
    	System.out.println(userName + " " + password);
    	LoginHelper lh = new LoginHelper(true);
    	lh.login(userName, password);
    	if(lh.responseType.equals(ServerClientRequestTypes.ACK)) {
    		ClientController.setCurrentSystemUser(lh.loggedUser);
    		ClientController.setCurrentUserRole(lh.loggedUser.getRole());
			statusLabel.setText(lh.statusLabelString);
			statusLabel.setVisible(true);
			System.out.println("SLEEPING FOR A SECOND TO SHOW LABEL!");
			ClientController.setCustomerIsSubsriber(false);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(ClientController.getCurrentUserRole().equals(Role.SUBSCRIBER)) {
				ClientController.setCustomerIsSubsriber(true);
			}
			
			if(ClientController.isFastRecognitionToggle()) {
				// show alert and reload window
				ClientController.setFastRecognitionToggle(lh.newFastRecState);
			}
			
			if(lh.role.equals(Role.UNAPPROVED_CUSTOMER) || lh.role.equals(Role.UNAPPROVED_SUBSCRIBER)) {
		    	ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
				statusLabel.setText("User not yet registered!");
				statusLabel.setVisible(true);
		    	return;
			}
			
			Stage primaryStage = new Stage();
			WindowStarter.createWindow(primaryStage, this, lh.nextPage, null, lh.title, false);
			primaryStage.show();
			((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

    	}
    	else {
    		statusLabel.setVisible(true);
			if(lh.responseType.equals(ServerClientRequestTypes.LOGIN_FAILED_ILLEGAL_INPUT)) {
				statusLabel.setText(lh.statusLabelString);
			}
			else if(lh.responseType.equals(ServerClientRequestTypes.LOGIN_FAILED_ALREADY_LOGGED_IN)) {
				statusLabel.setText(lh.statusLabelString);
			}
			else {
			statusLabel.setText(lh.statusLabelString); // add specifics
			}
    	}
		//primaryStage.show();
		//((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
		

    }
	
	static boolean firstOrderForSubscriber() {
		// send the following query:
		// select orderID from customer_orders WHERE customerId=ConnectedClientID;
		// if empty, return true, else false
		if(ClientController.getCustomerIsSubsriber()== null || !ClientController.getCustomerIsSubsriber()) {
			System.out.println("Invalid call to firstOrderForSubscriber() -> connected user is not a subsriber");
			return false;
		}
		ClientUI.clientController.accept(new SCCP(ServerClientRequestTypes.SELECT, 
				new Object[]
						{"customer_orders", 
								true, "orderID",
								true, "customerId = " + ClientController.getCurrentSystemUser().getId(),
								false, null}));
		if(!ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			System.out.println("Invalid database operation (checking subsriber orders history failed). (returnin false)");
			return false; // Rotem forgot to add this back then
		}
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
		// true if we have NO ORDERS else false
		return res.size() == 0;
	}
}
