package client;

import java.io.IOException;

import common.SCCP;
import ocsf.client.AbstractClient;

/**
 * 
 * EKTClient class extends the AbstractClient class, and handles the
 * communication between the client and the server. This class opens a
 * connection to the server, sends and receives messages from the server, and
 * terminates the client. It also has a static customer object and a boolean
 * awaitResponse variable.
 * 
 * @author danielvardimon
 */
public class EKTClient extends AbstractClient {

	public static boolean awaitResponse = false;

	/**
	 * 
	 * Constructor that takes in a host and port and calls the superclass
	 * constructor to open a connection to the server.
	 * 
	 * @param host - The host IP address to connect to
	 * @param port - The port number to connect to
	 * @throws IOException
	 */
	public EKTClient(String host, int port) throws IOException {
		super(host, port); // Call the superclass constructor
		openConnection();

	}

	/**
	 * 
	 * Handles a message received from the server. It sets the awaitResponse
	 * variable to false and sets the static responseFromServer object in the
	 * ClientController class to the received message.
	 * 
	 * @param msg - The message received from the server
	 */
	public void handleMessageFromServer(Object msg) {
		System.out.println("--> EKT Client --> handleMessageFromServer");
		awaitResponse = false;

		if (msg instanceof SCCP) {
			SCCP tmp = (SCCP) msg;
			System.out.println("Got a message from server: " + tmp);
			// pass the message to the controller
			ClientController.responseFromServer.setRequestType(tmp.getRequestType());
			ClientController.responseFromServer.setMessageSent(tmp.getMessageSent());

		} else {
			// error! (invalid input to client)
			ClientController.responseFromServer.setRequestType(null);
			ClientController.responseFromServer.setMessageSent(null);
		}
	}

	/**
	 * 
	 * Handles a message sent from the client UI. It opens a connection to the
	 * server, sets the awaitResponse variable to true, sends the message to the
	 * server, and waits for a response.
	 * 
	 * @param message - The message sent from the client UI
	 */
	public void handleMessageFromClientUI(SCCP message) {
		try {

			openConnection();// in order to send more than one message

			awaitResponse = true;

			sendToServer(message);

			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
			System.err.println("Exception in EKTClient.quit: " + e.getMessage());
			e.printStackTrace();
		}
		System.exit(0);
	}
}
