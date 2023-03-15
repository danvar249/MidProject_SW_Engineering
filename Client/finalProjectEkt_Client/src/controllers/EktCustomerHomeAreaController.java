package controllers;


import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**

This class is the controller for the customer home page.
It contains methods for handling button events and updating the UI.
@author nastya
@version [1.0]
@since [1.1.23]
*/

public class EktCustomerHomeAreaController{

    @FXML
    private Button btnCreateOrder;

    @FXML
    private Button btnViewOrders;

    @FXML
    private Label statusLabel;

    @FXML
    private Text txtWelcomeText;
    /**
     * Initializes the UI by setting the welcome text to display the current user's first name
     */
	@FXML
	public void initialize() {
		this.txtWelcomeText.setText("Welcome back " + ClientController.getCurrentSystemUser().getFirstName());
	}

	/**
	 * Handles the event when the "View Orders" button is clicked.
	 * This method is currently not implemented.
	 * 
	 * @param event the action event that triggered the method call
	 */
	@FXML public void getBtnViewOrders(ActionEvent event) {
		
		assert 1 == 0;
	}
	/**
	 * Handles the event when the "Create Order" button is clicked.
	 * This method is currently not implemented.
	 * 
	 * @param event the action event that triggered the method call
	 */
	@FXML public void getBtnCreateOrder(ActionEvent event) {
		
		assert 1 == 0;
	}

}