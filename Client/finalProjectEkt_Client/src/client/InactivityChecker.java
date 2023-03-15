package client;

import java.util.concurrent.TimeUnit;

import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * 
 * Class InactivityChecker implements the Runnable interface and is responsible
 * for checking the user's inactivity time.
 * 
 * @author Rotem
 */
public class InactivityChecker implements Runnable {

	private long inactivityThreshold; // Threshold for inactivity in milliseconds
	private long lastActivityTime; // Timestamp of the user's last activity
	private boolean running; // Flag to indicate whether the inactivity checker is running
	private ActionEvent eventForStageClose;

	/**
	 * Constructor for InactivityChecker.
	 * 
	 * @param inactivityThreshold threshold for inactivity in milliseconds.
	 * @param event               ActionEvent for closing the current stage.
	 */
	public InactivityChecker(long inactivityThreshold, ActionEvent event) {
		this.inactivityThreshold = inactivityThreshold;
		this.lastActivityTime = System.currentTimeMillis();
		this.running = true;
		if (event == null) {
			throw new IllegalArgumentException("ActionEvent passed to InactivityChecker is NULL");
		}
		// Rotem added the event!
		this.eventForStageClose = event;
	}

	/**
	 * Method to update the timestamp of the user's last activity.
	 */
	public void updateActivityTime() {
		// Update the timestamp of the user's last activity
		this.lastActivityTime = System.currentTimeMillis();
	}

	/**
	 * Method to stop the inactivity checker.
	 */
	public void stop() {
		// Stop the inactivity checker
		this.running = false;
	}

	/**
	 * Method to run the inactivity checker. It checks if the user has been inactive
	 * for more than the specified threshold and logs them out if so.
	 */
	@Override
	public void run() {
		while (running) {
			long elapsedTime = System.currentTimeMillis() - lastActivityTime;
			if (elapsedTime > inactivityThreshold) {
				// User has been inactive for too long - log them out
				logoutUser();
				break;
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// Handle exception
			}
		}
	}

	/**
	 * Method to log out the user. It sends a logout request to the server, closes
	 * the current window, and opens the login window.
	 */
	private void logoutUser() {
		System.out.println("Inactive for 5 min. you have been logged out.");
		if (ClientController.getCurrentSystemUser() != null) {
			ClientUI.getClientController().accept(
					new SCCP(ServerClientRequestTypes.LOGOUT, ClientController.getCurrentSystemUser().getUsername()));
			System.out.println("Loading login page (OL)");
			// use the event pointer to shut down current window
			((Node) eventForStageClose.getSource()).getScene().getWindow().hide();
			// and load the login window
			Stage primaryStage = new Stage();
			WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);
			primaryStage.show();
		}
	}
}
