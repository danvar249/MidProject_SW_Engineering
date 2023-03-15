package gui;

import java.io.IOException;


import Server.ServerUI;
import common.WindowStarter;
import database.DatabaseController;
import database.DatabaseOperation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ocsf.server.ConnectionToClient;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
/**
 * The class ServerPortController is the controller for the server port GUI.
 * It provides functionality for starting and stopping the server, connecting to the database and adding/removing users from the database.
 * Additionally, it shows a list of all the currently connected clients to the server.
 * @author Rotem
 *
 */
public class ServerPortController  {
	/**
	 * The clientsViewer class is a thread that is used to display a list of connected clients to the server at any given time.
	 * It uses the oldClientList and newClientList arrays to compare the current connected clients to the previous connected clients.
	 * If there is a change in the connected clients, the list of clients is updated on the GUI.
	 * The thread runs as long as the server is listening for connections.
	 * When the server stops listening for connections, the thread is closed.
	 * @author Rotem
	 *
	 */
	private final class clientsViewer extends Thread {
		private Thread[] oldClientList = new Thread[1];
		private Thread[] newClientList;

		/*
		 * Rotem: Added a custom listener that lists the active clients at any time
		 */
		@Override
		public void run() {
			System.out.println("Helper server thread for showing connected clients has been created (and started).");
			while(ServerUI.getEktServerObject()!=null && ServerUI.getEktServerObject().isListening()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!oldClientList.equals(ServerUI.getEktServerObject().getClientConnections())) {
					oldClientList=ServerUI.getEktServerObject().getClientConnections();
					newClientList = ServerUI.getEktServerObject().getClientConnections();
					txtClients.setText("");
					StringBuilder sb = new StringBuilder();
					long i =1;
					for(Thread t : newClientList) {
						ConnectionToClient client = (ConnectionToClient)t; // please work
						sb.append("Client " + (i++)+": " + client.getInetAddress().getHostAddress() +"\n");
					}
					txtClients.setText(sb.toString());
					
				}
			}
			System.out.println("Helper server thread for showing connected clients has been closed.");
		}
	}

	String temp="";
	
	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnConnect = null;
	@FXML
	private Label lbllist;
	
	@FXML
	private TextField portxt;
	ObservableList<String> list;

	// additions:
	
	@FXML TextField databaseUsernameTxt;

	@FXML TableView<String> clientsTable;

	@FXML PasswordField databasePasswdTxt;

	@FXML Button addUserToDB;

	@FXML Button btnDisconnect;

	private Thread threadForListeningToClients;

	@FXML TableColumn<Object, String> colClients;

	@FXML TextArea txtClients;
	/**
	 * Method that initializes the GUI elements when the FXML file is loaded.
	 * Specifically, it sets the text of the "addUserToDB" button to "Import Simulation (one time use)".
	 */
	@FXML
	private void initialize() {
		//colClients.setCellValueFactory(null);
		addUserToDB.setText("Import Simulation (one time use)");
	}
	/**
	 * This method start the stage of the server window by loading the fxml and css files, setting the title and showing the window.
	 * Also, it sets the behavior of the X button to be handled in ServerUI's main method.
	 * @param primaryStage the main stage of the application
	 * @throws Exception if an error occurs while loading the fxml or css files
	 */
	public void start(Stage primaryStage) throws Exception {	
		// load server window
		WindowStarter.createWindow(primaryStage, this, "/gui/ServerPort.fxml", "/gui/ServerPort.css", "Server");

		// here, we don't catch the X button, since we already did it in ServerUI's main
		primaryStage.show();		
	}
	/**
	 * Retrieves the text entered in the port text field.
	 * @return the text in the port text field as a String.
	 */
	private String getport() {
		return portxt.getText();			
	}

	/**
	 * Returns the value of the database username text field
	 * @return A string representing the value of the database username text field
	 */
	private String getDbUser() {
		return databaseUsernameTxt.getText();			
	}
	/**
	 * Method to retrieve the text entered in the database password text field.
	 * @return the text entered in the database password text field.
	 */
	private String getDbPass() {
		return databasePasswdTxt.getText();			
	}
	/**
	 * This method is the event handler for the 'Enter' key press when the focus is on the port number text field.
	 * If the server is already listening, the method will return without doing anything.
	 * Otherwise, it will call the {@link #clickConnectBtn(ActionEvent)} method to start the server.
	 * @param ae ActionEvent representing the 'Enter' key press
	 * @throws Exception if there is an error starting the server
	 */
	@FXML
	public void onEnter(ActionEvent ae) throws Exception{
		if(ServerUI.getEktServerObject() != null && ServerUI.getEktServerObject().isListening()) {
			return;
		}
		else {
		clickConnectBtn(ae);
		}
	}
	/**
	 * This method is an event handler for the connect button press. It is responsible for setting the server's port number, SQL username, and SQL password.
	 * It then checks the validity of the entered SQL credentials, and if they are valid, it starts the server and loads the connected clients list.
	 * It also enables the 'add user to database' button, and shows the disconnect button.
	 * @param event the event triggered by the connect button press.
	 * @throws Exception if server port number is not entered, or if SQL credentials are invalid
	 */
	@FXML
	public void clickConnectBtn(ActionEvent event) throws Exception {		
		// try asking database controller to log in using the text fields
		String p = getport();
		DatabaseController.setDatabaseUserName(getDbUser());
		DatabaseController.setDatabasePassword(getDbPass());
		
		
		if(p.trim().isEmpty()) {
			System.out.println("You must enter a port number");
					
		}
		else if(!DatabaseController.checkLoginCredentials()) {
			System.out.println("Database username or password is incorrect, disconnecting server!");
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Oops!");
			alert.setHeaderText("Your SQL credentials are invalid!");
			alert.setContentText("Click OK and try again . . . ");
			alert.initStyle(StageStyle.UNDECORATED);
			alert.showAndWait();
		}
		else
		{
			// start server (this starts connection to database too)
			ServerUI.runServer(p);
			
			// load clients
			threadForListeningToClients = new clientsViewer();
			threadForListeningToClients.start();
			
			// allow the user of the server to insert users to the database:
			addUserToDB.setDisable(false);
			
			// toggle disconnect button
			btnDisconnect.setVisible(true);
			btnDisconnect.setDisable(false);
			
			// toggle connect button
			btnConnect.setVisible(false);
			btnConnect.setDisable(true);
		}
	}
	/**
	 * This method handles the Exit button click event. It will close the server and exit the application.
	 * It first calls the {@link ServerUI#serverForcedShutdown()} method to properly shutdown the server,
	 * and then exits the application by calling {@link System#exit(int)} with a status of 0.
	 * @param event the event that triggered the call of this method.
	 */
	public void getExitBtn(ActionEvent event)  {
		System.out.println("exit Academic Tool");
		ServerUI.serverForcedShutdown();
		// TODO:
		// find a better way to do this
		System.exit(0);			
	}
	/**
	 * This method is called when the "Import Simulation" button is pressed. It reads the user-management table (one big table), an external table with all user related info and places every user in the appropriate tables. It insert into systemuser, manager_location, worker(maybe worker is not needed?).
	 * @param event the event triggered by the button press
	 * @throws IOException
	 */
	@FXML public void getAddUserToDbBtn(ActionEvent event) throws IOException {
		// what happens here:
		// we read the user-management table (one big table), an external table with all user related info
		// we read a table of the format: everything_in_systemuser + location (for managers)
		// origin schema.table=ektdb.external_users
		// we place every user in the appropriate tables
		// insert into: systemuser, manager_location, worker (maybe worker is not needed?)
		// For now, I only insert to systemuser and to manager_location (in case typeOfUser=regional_manager)
		Object res = DatabaseController.handleQuery(DatabaseOperation.IMPORT_SIMULATION, new Object[] {});
		if(res instanceof Boolean) {
			String popupTitle, pHeader;
			popupTitle = "Import Simulation";
			pHeader = "Import simulation succeeded!";
			Boolean bRes = (Boolean)res;
			if(bRes) {
				// alert success
				System.out.println("Succcessfully imported data from external table \"external_users\" in schema ektdb.");
			}
			else {
				// alert failure
				System.out.println("Failure: importing data from external table "
						+ "\"external_users\" in schema ektdb has failed, please check the table's state and entries.");
				pHeader = "Failure importing data!";
			}
    		Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle(popupTitle);
            alert.setHeaderText(pHeader);
            alert.setContentText("thank you for understanding!");
            alert.showAndWait();
            addUserToDB.setDisable(true);
		}
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("You dont have the table external_users");
            alert.setHeaderText("you need to add it yourself");
            alert.setContentText("thank you for understanding!");
            alert.showAndWait();
            addUserToDB.setDisable(true);
		}
	}
	/**
	 * This method is triggered when the user clicks on the "Disconnect" button in the GUI.
	 * It stops the connection to the database and shuts down the server.
	 * The program does not close, allowing for re-connection. The "Connect" button is re-enabled,
	 * the "Disconnect" button is disabled, and the "Add user to DB" button is also disabled.
	 * @param event
	 */
	@FXML public void clickDisconnectBtn(ActionEvent event) {
		// similar to exit, we stop the connection to the database, and shut down the server:
		ServerUI.serverForcedShutdown();
		// but we don't close the program, allowing re-connection
		// NOT System.exit(0)
		
		// now, re-allow connect button and disable disconnect button
		
		// toggle disconnect button
		btnConnect.setVisible(true);
		btnConnect.setDisable(false);
		
		// toggle connect button
		btnDisconnect.setVisible(false);
		btnDisconnect.setDisable(true);
		
		// and disable "add user to db" button
		addUserToDB.setDisable(true);

	}

}