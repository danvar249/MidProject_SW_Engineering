package controllers;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * AddSystemUserToDatabaseController is a class that handles the functionality
 * for adding a new user to the database. It contains several buttons that allow
 * the user to exit the application, update an existing customer, add a new
 * customer, and display customer information.
 * 
 * @author danielvardimon
 *
 */
public class AddSystemUserToDatabaseController {
	@FXML
	private Button btnExit;

	@FXML
	private Button btnUpdateCustomer;

	@FXML
	private Button btnAddCustomer;

	@FXML
	private Button btnDisplayCustomer;

	@FXML
	Button addUserBtn;

	/**
	 * The getExitBtn method is used to exit the application. It closes the
	 * connection to the server and exits the application.
	 * 
	 * @param event The event that triggered the method call.
	 * @throws Exception if an error occurs while closing the connection to the
	 *                   server.
	 */
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("Worker Has Exited The Academic Tool");
		ClientUI.getClientController().client.closeConnection();
		System.exit(0);
	}

	/**
	 * The addUserToDB method is used to add a new user to the database.
	 * 
	 * @param event The event that triggered the method call.
	 * @throws OperationNotSupportedException if the operation is not supported.
	 */
	@FXML
	public void addUserToDB(ActionEvent event) throws OperationNotSupportedException {
		FXMLLoader loader = new FXMLLoader();

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root;
		try {

			root = loader.load(getClass().getResource("/gui/AddUserToDbForm.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/gui/AddUserToDbForm.css").toExternalForm());
			primaryStage.setTitle("Worker Page");

			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
