package controllers;

import java.sql.Date;
import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logic.Promotions;

 /**
 *The PromotionEditingController class provides a controller for the creation and editing of promotions.
 *
 *	@author DimaKyn
 *	@date 01/05/2023
 */
public class PromotionEditingController {

	@FXML
	private TextField txtPromotionName;

	private Promotions promotions;

	@FXML
	private TextField txtPromotionDescription;

	@FXML
	private ComboBox<String> cbLocation;

	@FXML
	private TextField txtDiscountPercentage;

	@FXML
	private DatePicker dpPromotionStartDate;

	@FXML
	private DatePicker dpPromotionEndDate;

	@FXML
	private Button btnCreatePromotion;

	@FXML
	private Button btnGoBack;
	
	@FXML
	private Text txtDiscountError;
	
	
	/**
	 * Initializes the location options in the location ComboBox.
	 * 
	 * @author Maxim
	 * @date 01/07/2023
	 */
	@FXML
	private void initialize() {
		String[] items = { "North", "South", "United Arab Emirates" };
		cbLocation.getItems().addAll(items);
	}
	
	/**
	* Handles the creation of a new promotion.
	* This method gets the values from the input fields, creates a new {@link Promotions} object and sends it to the server for adding it to the database.
	* If the server communication is successful, it displays a success message.
	* @author Rotem
	* @date 01/10/2023
	*/
	@FXML
	private void createPromotionHandler() {
		if (Integer.parseInt(txtDiscountPercentage.getText()) > 100 || Integer.parseInt(txtDiscountPercentage.getText()) < 0) {
			txtDiscountError.setVisible(true);
			return;
		}
		promotions = new Promotions();
		// Get the promotion details from the text fields and date pickers
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
		Object[] fillMessage = new Object[3];
		promotions.setDiscountPercentage(txtDiscountPercentage.getText());
		promotions.setPromotionName(txtPromotionName.getText());
		promotions.setPromotionDescription(txtPromotionDescription.getText());
		promotions.setstoreLocation(cbLocation.getSelectionModel().getSelectedItem());
		int locationNumber;
		switch (cbLocation.getSelectionModel().getSelectedItem()) {
		    case "North":
		        locationNumber =1;
		        break;
		    case "South":
		        locationNumber = 2;
		        break;
		    case "United Arab Emirates":
		        locationNumber = 3;
		        break;
		    default:
		        locationNumber = 0;
		        break;
		}
		promotions.setLocationID(locationNumber);
		promotions.setproductID(new Integer(0).toString());
		String startDateString = dpPromotionStartDate.getValue().toString();
		promotions.setStartDate(Date.valueOf(startDateString));
		String endDateString = dpPromotionEndDate.getValue().toString();
		promotions.setEndDate(Date.valueOf(endDateString));


		fillMessage[0] =  "promotions";
		fillMessage[1] = false;
		fillMessage[2] = new Object[] {promotions};
		
		preparedMessage.setMessageSent(fillMessage);

		// send to server
		System.out.println("Client: Sending new promotion request to the server.");
		ClientUI.getClientController().accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		} else {
			Alert successMessage = new Alert(AlertType.INFORMATION);
			successMessage.setTitle("Update Success");
			successMessage.setHeaderText("Update Success");
			successMessage.setContentText("Promotion created successfully!");
			successMessage.show();
		}
	}

	/** Closes the current window and opens the Sales Manager window. 
	 * @param event the ActionEvent trigger
	*/
	public void goBackHandler(ActionEvent event) {
		Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	    currentStage.close();
	    
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), "/gui/SalesManager.fxml", null, "Sales", true);
		primaryStage.show();
	}

}
