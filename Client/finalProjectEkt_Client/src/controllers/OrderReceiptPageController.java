package controllers;

import client.ClientController;
import common.WindowStarter;
import entityControllers.OrderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.Properties;

public class OrderReceiptPageController {
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

	public void initialize() {
		txtCustomerEmail
				.setText("Order information was sent to\n" + ClientController.getCurrentSystemUser().getEmailAddress()
						+ "\n and " + ClientController.getCurrentSystemUser().getPhoneNumber());
		txtCustomerEmail.setLayoutX(200 - (txtCustomerEmail.minWidth(0) / 2));

		txtOrderNumber.setText("Order Number: " + OrderController.getOrderNumber());
		txtOrderNumber.setLayoutX(200 - (txtOrderNumber.minWidth(0) / 2));

		txtOrderTotal
				.setText("Order total: " + (new DecimalFormat("##.##").format(OrderController.getOrderTotalPrice())) + "$");
		txtOrderTotal.setLayoutX(200 - (txtOrderTotal.minWidth(0) / 2));

		System.out.println("ClientController.orderType = " + OrderController.getOrderType());
		if (OrderController.getOrderType().equals("Delivery")) {
			txtBillingDate.setText("Delivery Date: " + OrderController.getOrderDeliveryTime());
			txtBillingDate.setLayoutX(200 - (txtBillingDate.minWidth(0) / 2));
		}

		if (OrderController.getOrderType().equals("Pickup")) {
			txtBillingDate.setText("Pickup Place: " + OrderController.getPickupPlace());
			txtBillingDate.setLayoutX(200 - (txtBillingDate.minWidth(0) / 2));
		}

		String CustomerEmail = "";
		SendEmail(CustomerEmail);

		if (!OrderController.getDeliveryAddress().equals("")) {
			txtDeliveryAddress.setText("Delivery Address: " + OrderController.getDeliveryAddress());
			txtDeliveryAddress.setFill(Color.WHITE);
		}

	}

	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktCatalogForm.fxml",
				null, "Ekt Catalog", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}

	@FXML
	void getBtnLogout(ActionEvent event) {
		// log the user out:
		ClientController.sendLogoutRequest();

		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktSystemUserLoginForm.fxml", null, "Login", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}

	private void SendEmail(String CustomerEmail) {
		Properties prop = new Properties();

		prop.put("mail.smtp.host", "smtp.google.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS
	}

}
