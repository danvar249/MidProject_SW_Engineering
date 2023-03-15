package client;

import java.io.IOException;

import controllers.ClientLoginController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 
 * The ClientUI class is the main class for the client side of the EKT project.
 * It extends the Application class from JavaFX and is responsible for launching
 * the client-to-server connection GUI window, where the user can enter the
 * server's IP address to connect to.
 * 
 * @author danielvardimon
 */
public class ClientUI extends Application {
	/**
	 * Only one instance of the ClientController class
	 */
	public static ClientController clientController;
	public static String serverIP;

	/**
	 * This is the main function for the client side of the EKT project. main calls
	 * the JavaFX function start, where the client-to-server connection GUI window
	 * is loaded.
	 * 
	 * @param args - these are the command-line arguments passed to the client
	 *             application on start-up. (currently, 1/4/23 no arguments)
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	/**
	 * Load the client login window (which has a misleading name, as it does not
	 * perform a login, but rather the handshake between the client and the server).
	 * The client login window allows the user to enter an IP address (or leave it
	 * blank for localhost) which the client will try to connect to, on port 5555.
	 * 
	 * @param primaryStage - the primary stage for this application
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientLoginController aFrame = new ClientLoginController(); // create the window
		aFrame.start(primaryStage);
	}

	/**
	 * Connects to the server by creating a new instance of the ClientController
	 * class with the serverIP and port 5555.
	 * 
	 * @throws IOException - if an input or output exception occurs
	 */
	public static void connectToServer() throws IOException {
		setClientController(new ClientController(serverIP, 5555));
	}

	public static ClientController getClientController() {
		return clientController;
	}

	public static void setClientController(ClientController clientController) {
		ClientUI.clientController = clientController;
	}

}
