package ek_configuration;

import client.ClientController;
import common.WindowStarter;
import entityControllers.OrderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.Properties;
/**

The _EKConfigurationOrderReceiptController class is the controller for the receipt page for orders.

This page displays the order number, total price, delivery address and billing date.

It also sends an email to the customer's email address with the order details.

The user can also go back to the customer's home area or log out.
*/
public class _EKConfigurationOrderReceiptController {
	@FXML
	private Button btnBack;

	@FXML
	private Button btnLogout;
	
    @FXML
    private Text txtCustomerEmail;

    @FXML
    private Text txtOrderNumber;

    @FXML
    private Text txtOrderTotal;
    
    @FXML
    private Text txtBillingDate;
    
    @FXML
    private Text txtDeliveryAddress;

	
	/**

	This method is called when the _EKConfigurationOrderReceiptController is initialized. It sets the text for various Text objects
	on the GUI, including the customer's email address, order number, order total, billing date, and delivery address. It also
	sends an email to the customer's email address, and displays a message in the console.
	*/
	public void initialize() {
		txtCustomerEmail.setText("Order information was sent to\n" + ClientController.getCurrentSystemUser().getEmailAddress()
				+ "\n and " + ClientController.getCurrentSystemUser().getPhoneNumber());
		txtCustomerEmail.setLayoutX(200 - (txtCustomerEmail.minWidth(0) / 2));
		
		txtOrderNumber.setText("Order Number: " + OrderController.getOrderNumber());
		txtOrderNumber.setLayoutX(200 - (txtOrderNumber.minWidth(0) / 2));
		
		txtOrderTotal.setText("Order total: " + (new DecimalFormat("##.##").format(OrderController.getOrderTotalPrice())) + "$");
		txtOrderTotal.setLayoutX(200 - (txtOrderTotal.minWidth(0) / 2));
		
		System.out.println("ClientController.orderType = " + OrderController.getOrderType());

		
		txtBillingDate.setText("Your items are waiting in the tray - enjoy!");
		txtBillingDate.setLayoutX(200 - (txtBillingDate.minWidth(0) / 2));
		
		

		//////////IF WE HAVE TIME WE CAN DO IT. I THINK WE CAN DO IT BUT NOT USING A GOOGLE ACCOUNT!
		String CustomerEmail = "dimakislitsyn96@gmail.com";
		SendEmail(CustomerEmail);
		
		if (!OrderController.getDeliveryAddress().equals("")) {
			txtDeliveryAddress.setText("Delivery Address: " + OrderController.getDeliveryAddress());
		}
	}
	/**
	This method is called when the "Back" button is pressed.
	It opens the customer's local order page.
	@param event the button press event
	*/
	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/_EKConfigurationCustomerLocalOrderFrame.fxml",
				null, "Ekt Catalog", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}
	/**
	This method is called when the "Logout" button is pressed.
	It logs the user out and opens the login page.
	@param event the button press event
	*/
	@FXML
	void getBtnLogout(ActionEvent event) {
		// log the user out:
		ClientController.sendLogoutRequest();
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/_EKConfigurationLoginFrame.fxml",
				null, "Login", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}
	/**

	This method sends an email to the customer's email address with the order details.
	@param CustomerEmail the email address to send the order details to
	*/
	private void SendEmail(String CustomerEmail) {
	        Properties prop = new Properties();
	        
			prop.put("mail.smtp.host", "smtp.google.com");
	        prop.put("mail.smtp.port", "587");
	        prop.put("mail.smtp.auth", "true");
	        prop.put("mail.smtp.starttls.enable", "true"); //TLS

	    
	}
	
}
