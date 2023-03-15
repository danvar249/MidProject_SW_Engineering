package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientController;
import client.ClientUI;
import client.Configuration;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Important: Read this - The configuration is OL - OL = when you buy from home or somewhere - the administrative part of the system.
 * 										   EK = when you physically assault the machine
 * So, here, when we log in, to use the system we made - we must choose OL - and this is the default configuration
 */
/**


The class ClientLoginController is responsible for the login screen of the client.
It allows the user to connect to the server and choose the configuration of the system (OL/EK).
It also allows the user to select a machine to connect to and to set fast recognition credentials for future usage.
@author Rotem
*/

public class ClientLoginController {
	private List<String> machines=null;

	@FXML
	private TextField txtIP;
	@FXML 
	private Button btnConnect;
	
	@FXML
	private Label hiddenLabel;
	
	@FXML ComboBox<String> cmbTezura;
	@FXML ComboBox<String> cmbMachines;
	@FXML Button btnFinish;

	@FXML CheckBox toggleEasyRecognition;

	@FXML TextField txtFastUsr;

	@FXML TextField txtFastPass;
	
	@FXML Text txtUsername;
	
	@FXML Text txtPassword;
	/**

	Initializes the login form. It sets the default configuration to OL and populates the combobox with the options of OL and EK.
	*/
	@FXML
	void initialize() {
		ClientController.setFastRecognitionToggle(false);
		ClientController.setFastRecognitionUserName(null);;
		ClientController.setFastRecognitionPassword(null);;
		toggleEasyRecognition.setVisible(false);
		toggleEasyRecognition.setDisable(true);

		cmbTezura.getItems().add("EK");
		cmbTezura.getItems().add("OL");
		cmbTezura.setValue("OL");
		ClientController.setLaunchConfig(Configuration.OL);
	}
	
	/**

	Starts the primary stage of the login form.
	@param primaryStage the primary stage of the application
	@throws Exception
	*/
		public void start(Stage primaryStage) throws Exception {
			WindowStarter.createWindow(primaryStage, this, "/gui/ClientLoginForm.fxml", "/gui/ClientLogin.css", "Login", true);
			primaryStage.show();	 	
		}
	
		/**
		 * This event grabs the enter key when we are on the password field.
		 * @param ae
		 */
		@FXML
		public void onEnter(ActionEvent ae){
			
			getConnectToServer(ae);
		}
		
		/**

		Attempts to connect to the server using the IP address specified in the text field.
		It also sets the configuration and the machine to connect to.
		If fast recognition is enabled, it also stores the user credentials for future use.
		@param event
		*/
	public void getConnectToServer(ActionEvent event) {


		if(!ClientController.getLaunchConfig().equals(Configuration.EK)) {
			ClientController.setFastRecognitionToggle(false);
		}
		if(ClientController.isFastRecognitionToggle()) {
			// store user credentials for fast-recognition
			ClientController.setFastRecognitionUserName(txtFastUsr.getText());
			ClientController.setFastRecognitionPassword(txtFastPass.getText());
		}
		
		hiddenLabel.setVisible(false);
		
		try{
			ClientController.setLaunchConfig(Configuration.valueOf(cmbTezura.getValue()));
		}catch(IllegalArgumentException ex) {
			System.err.println("Invalid configuration!");
			return;
		}
		
		System.out.println("Client is connecting to server");
		String tmp = txtIP.getText();
		if(tmp.equals(""))
			tmp = "localhost";
		ClientUI.serverIP = tmp;
		try {
			ClientUI.connectToServer();
			
			if(cmbTezura.getValue().equals("EK")) {
				// expose the option to select a machine!
				cmbMachines.setVisible(true);
				btnFinish.setVisible(true);
				btnConnect.setVisible(false);
				// get all machines:
				try {
					machines = getExistingMachinesFromServer();
					// insert them to the box:
					for(String machine: machines) {
						cmbMachines.getItems().add(machine);
					}
					hiddenLabel.setText("Please choose a machine and click finish");
					hiddenLabel.setVisible(true);
					
					System.out.println("Client message: Please choose a machine and click Finish");
					return;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					hiddenLabel.setText("Error fetching existing machines from database");
					hiddenLabel.setVisible(true);
					return;
				}

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
			hiddenLabel.setText("Could not connect to " + tmp + " over port " + 5555);
			hiddenLabel.setVisible(true);
			return;
		}



		
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);

		
		System.out.println("Client is now connected to server");
		primaryStage.show();	 	


	}
	/**
	 * This method sets the tezura configuration of the application.
	 * It takes an ActionEvent as a parameter and sets the launch configuration
	 * of the ClientController to the value selected in the tezura combo box.
	 * It also makes the toggle button for easy recognition visible and enabled 
	 * if the configuration is set to EK, and invisible and disabled otherwise.
	 * 
	 * @param event the action event that triggers the method call
	 */
	@FXML public void setTezura(ActionEvent event) {
		System.out.println("Switched to " + cmbTezura.getValue());
		ClientController.setLaunchConfig(Configuration.valueOf(cmbTezura.getValue()));

		if(ClientController.getLaunchConfig().equals(Configuration.EK)) {
			toggleEasyRecognition.setVisible(true);
			toggleEasyRecognition.setDisable(false);
		}
		else {
			toggleEasyRecognition.setVisible(false);
			toggleEasyRecognition.setDisable(true);
		}
	}
	/**
	 * This method retrieves a list of existing machines from the server.
	 * It opens a connection to the server, sends a request for all existing machines,
	 * and closes the connection. It then checks the response from the server
	 * and returns a list of strings representing the names of the machines if the response is an ACK.
	 * Otherwise, it prints "FAILURE" and returns null.
	 * 
	 * @return a list of strings representing the names of existing machines, or null if the request fails
	 * @throws IOException if there is an error with the connection to the server
	 */
	private List<String> getExistingMachinesFromServer() throws IOException {
		ClientUI.getClientController().client.openConnection();
		ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.REQUEST_ALL_MACHINES, ""));
		ClientUI.getClientController().client.closeConnection();

		// we want to check the response from server
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)){
			// we want the List<String> where each string is the name of a machine
			@SuppressWarnings("unchecked")
			List<String> tmp = (List<String>)ClientController.responseFromServer.getMessageSent();
			return tmp;
		}
		else{
			System.out.println("FAILURE");
		}
		return null;
	}
	/**
	 * This method sets the chosen machine for easy recognition.
	 * It takes an ActionEvent as a parameter and sets the _EkCurrentMachineName
	 * of the ClientController to the value selected in the machines combo box.
	 * 
	 * @param event the action event that triggers the method call
	 */
	@FXML public void chooseMachine(ActionEvent event) {
		System.out.println("Chose machine " + cmbMachines.getValue());
		
		ClientController.setEKCurrentMachineName(cmbMachines.getValue());
	}
	/**
	 * This method gets the finish configuration for easy recognition.
	 * It checks that the chosen machine is valid, and if it is, sends a query to the server to get the machine ID.
	 * If the response from the server is an ACK, it sets the _EkCurrentMachineID of the ClientController to the retrieved ID
	 * and opens a new window for login.
	 * If the response is not an ACK, it throws a runtime exception.
	 * 
	 * @param event the action event that triggers the method call
	 */
	@FXML public void getFinishEkConfig(ActionEvent event) {
		// check that the chosen machine is valid:
		if(ClientController.getEKCurrentMachineName() == null || !machines.contains(ClientController.getEKCurrentMachineName())) {
			System.out.println("Invalid machine choice - please select an existing machine!");
			return;
		}
		// send query to get the machine ID (yeah yeah):
		SCCP msg = new SCCP(ServerClientRequestTypes.SELECT, 
				new Object[]{"machine", true, "machineId", true, "machineName = '" +ClientController.getEKCurrentMachineName()+ "'", false, null});
		ClientUI.getClientController().accept(msg);
		if(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Object>> tmp= (ArrayList<ArrayList<Object>>) ClientController.responseFromServer.getMessageSent();
			System.out.println(tmp);
			ClientController.setEKCurrentMachineID((Integer.valueOf(tmp.get(0).get(0).toString())));
			System.out.println("Machine ID set to " + ClientController.getEKCurrentMachineID());
		}
		else {
			throw new RuntimeException("Error getting machine ID from database");
		}
		
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationLoginFrame.fxml", null, "Login", true);
		primaryStage.show();
	}
	/**
	 * This method sets the easy recognition toggle.
	 * It takes an ActionEvent as a parameter and sets the fast recognition toggle
	 * of the ClientController to true if it is currently false, and vice versa.
	 * It also makes the text fields for the fast recognition username and password
	 * visible and enabled if the toggle is set to true, and invisible and disabled otherwise.
	 * 
	 * @param event the action event that triggers the method call
	 */
	@FXML public void easyRecognitionSetter(ActionEvent event) {
		if(!ClientController.isFastRecognitionToggle()) {
			System.out.println("Fast recognition simulation on");
			ClientController.setFastRecognitionToggle(true);
			txtFastUsr.setVisible(true);
			txtFastUsr.setDisable(false);
			txtFastPass.setVisible(true);
			txtFastPass.setDisable(false);
			txtUsername.setVisible(true);
			txtPassword.setVisible(true);
			
		}
		else {
			System.out.println("Fast recognition simulation off");
			ClientController.setFastRecognitionToggle(false);
			ClientController.setFastRecognitionUserName(null);
			ClientController.setFastRecognitionPassword(null);
			txtFastUsr.setVisible(false);
			txtFastUsr.setDisable(true);
			txtFastPass.setVisible(false);
			txtFastPass.setDisable(true);
			txtUsername.setVisible(false);
			txtPassword.setVisible(false);  
		}
	}
}