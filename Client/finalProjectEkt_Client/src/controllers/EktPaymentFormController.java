package controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import entityControllers.OrderController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.CustomerOrder;

/**
 * The EktPaymentFormController class handles the Payment Form window of the Ekt
 * application. It provides the user with different payment options such as
 * paying with the Ekt App, paying with the credit card, and paying with the
 * account balance. The class also displays the user's account balance and
 * credit card details. It also allows the user to choose the billing date when
 * paying with the Ekt App.
 * 
 * @author Rotem, Dima, Maxim
 * @version 1.0
 *
 */
public class EktPaymentFormController {

	@FXML
	private Button btnBack;

	@FXML
	private Button btnChargeMyCreditCard;

	@FXML
	private Button btnPayUsingTheEktApp;

	@FXML
	private Button btnPayWithBalance;

	@FXML
	private Text txtAccountBalance;

	@FXML
	private Text txtCreditCard;

	@FXML
	private Text txtProcessing;

	@FXML
	private ComboBox<String> comboBoxBillingDate;

	private Double accBalance;

	/**
	 * The initialize method is used to set up the Payment Form when it is opened.
	 * It retrieves the current system user's account balance and sets it to the
	 * text field txtAccountBalance. If the customer is a subscriber, it enables the
	 * button btnPayUsingTheEktApp and the comboBox comboBoxBillingDate, and sets
	 * the items in the comboBox to the current date and the next month's date.
	 */
	public void initialize() {
		SCCP getUserBalance = new SCCP();

		getUserBalance = new SCCP();
		getUserBalance.setRequestType(ServerClientRequestTypes.SELECT);

		getUserBalance.setMessageSent(new Object[] { "customer_balance", true, "balance", true,
				"id = " + ClientController.getCurrentSystemUser().getId(), false, null });
		ClientUI.getClientController().accept(getUserBalance);
		SCCP answer = ClientController.responseFromServer;

		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> preProcessedOutput = (ArrayList<ArrayList<Object>>) answer.getMessageSent();

		String temp = "";

		for (ArrayList<Object> lst : preProcessedOutput) {
			// we expect product to have 5 columns, and act accordingly
			Object[] arr = lst.toArray();
			System.out.println(Arrays.toString(arr));
			if(arr != null && arr[0] != null)
				temp = arr[0].toString();
			System.out.println(temp);
		}

		try {
		accBalance = Double.parseDouble(temp);
		}catch(NumberFormatException ex) {
			accBalance=0.;
		}
		if(accBalance <= 0.0 || accBalance < OrderController.getOrderTotalPrice())
			btnPayWithBalance.setDisable(true);
		txtAccountBalance.setText("ACCOUNT BALANCE: " + new DecimalFormat("##.##").format(accBalance) + "$");

		if (ClientController.getCustomerIsSubsriber() == true) {
			btnPayUsingTheEktApp.setDisable(false);
			comboBoxBillingDate.setDisable(false);
			ObservableList<String> comboPayment = FXCollections.observableArrayList();
			String[] nowDate = java.time.LocalDate.now().toString().split("-");
			comboPayment.add("Now: " + nowDate[2] + "-" + nowDate[1] + "-" + nowDate[0]);
			ArrayList<String> date = new ArrayList<>();
			String[] splitDate = java.time.LocalDate.now().toString().split("-");
			date.add(splitDate[0]);
			date.add(splitDate[1]);
			date.add(splitDate[2]);
			// Set day to 01
			date.set(2, "01");
			// If current month is "12" set month = 01 and year = currentYear+1
			if (date.get(1).equals("12")) {
				date.set(1, "01");
				date.set(0, (Integer.parseInt(date.get(0)) + 1) + "");
			} else {
				// Else do month = month+1
				int month = Integer.parseInt(date.get(1));

				if (month < 10) {
					date.set(1, "0" + (month + 1));
				} else {
					date.set(1, (month + 1) + "");
				}

			}

			comboPayment.add("Next month: " + date.get(2) + "-" + date.get(1) + "-" + date.get(0));

			comboBoxBillingDate.setItems(comboPayment);
		}
	}

	/**
	 * 
	 * @param orderType the type of order
	 * @return the id associated with the orderType
	 */
	private Integer getTypeId(String orderType) {
		switch (orderType) {
		case "Pickup":
			return 1;
		case "Delivery":
			return 2;
		case "Local":
			return 3;
		}
		return null;
	}

	/**
	 * 
	 * This method is triggered when the user clicks the "Charge My Credit Card"
	 * button. It sets the billing date to the current date, sets the processing
	 * text to "PROCESSING...", and calls the processOrder() method.
	 * 
	 * @param event The event that triggered this method, in this case, a button
	 *              click.
	 */
	@FXML
	void getBtnChargeMyCreditCard(ActionEvent event) {
		String[] date = java.time.LocalDate.now().toString().split("-");
		OrderController.setBillingDate(date[0] + "-" + date[1] + "-" + date[2]);
		txtProcessing.setText("PROCESSING...");
		processOrder(event);
	}

	/**
	 * This method is used to handle the "Pay using the Ekt App" button. It gets the
	 * value of the selected date from the combo box and sets it as the billing
	 * date. It also sets the text of the "txtProcessing" text field to
	 * "PROCESSING..." and calls the "processOrder" method.
	 * 
	 * @param event The ActionEvent object that is triggered when the button is
	 *              clicked.
	 */
	@FXML
	void getBtnPayUsingTheEktApp(ActionEvent event) {
		String[] date = comboBoxBillingDate.getValue().split(" ");
		if (date.length == 3) {
			OrderController.setBillingDate(date[2]);
		} else {
			OrderController.setBillingDate(date[1]);
		}
		txtProcessing.setText("PROCESSING...");
		processOrder(event);
	}

	/**
	 * This method is used to handle the "Pay with Balance" button. It gets the
	 * current date and sets it as the billing date. It also sets the text of the
	 * "txtProcessing" text field to "PROCESSING...". It then calculates the new
	 * balance by subtracting the order total price from the current balance. It
	 * then updates the customer balance in the database with the new balance.
	 * Finally it calls the "processOrder" method.
	 * 
	 * @param event The ActionEvent object that is triggered when the button is
	 *              clicked.
	 */
	@FXML
	void getBtnPayWithBalance(ActionEvent event) {
		String[] date = java.time.LocalDate.now().toString().split("-");
		System.out.println(date);
		OrderController.setBillingDate(date[0] + "-" + date[1] + "-" + date[2]);
		txtProcessing.setText("PROCESSING...");

		Double newBalance = accBalance - OrderController.getOrderTotalPrice();

		SCCP updateStock = new SCCP();
		updateStock.setRequestType(ServerClientRequestTypes.UPDATE);
		updateStock.setMessageSent(
				new Object[] { "customer_balance", "balance = " + new DecimalFormat("##.##").format(newBalance),
						" id = " + ClientController.getCurrentSystemUser().getId() });

		ClientUI.getClientController().accept(updateStock);
		System.out.println("Balance was updated from " + new DecimalFormat("##.##").format(accBalance) + "to "
				+ new DecimalFormat("##.##").format(newBalance) + "after order!");

		processOrder(event);
	}

	/**
	 * The getBtnBack method is an event handler that is triggered when the user
	 * clicks on the 'back' button. It navigates the user to the previous page by
	 * calling the nextPage method and passing in the necessary parameters.
	 * 
	 * @param event - the ActionEvent object that is triggered when the button is
	 *              clicked
	 */
	@FXML
	public void getBtnBack(ActionEvent event) {
		nextPage(event, "/gui/EktOrderSummary.fxml", "EKT cart");
	}

	/**
	 * 
	 * The nextPage method is used to navigate the user to the next page by creating
	 * a new window with the given fxml address and window label. It also closes the
	 * current window.
	 * 
	 * @param event       - the ActionEvent object that triggers the method
	 * @param fxmlAddress - the address of the fxml file of the next page
	 * @param windowLabel - the label of the next window
	 */
	private void nextPage(ActionEvent event, String fxmlAddress, String windowLabel) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), fxmlAddress, null,
				windowLabel, true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
	}

	/**
	 * 
	 * The processOrder method is used to process the order by inserting it into the
	 * database table 'orders' and associating it with the current customer in the
	 * 'customer_orders' table. It retrieves the max order id from the database and
	 * sets it to the ClientController's orderNumber variable. This method also
	 * handles the delivery time and typeId of the order and sets them accordingly.
	 * 
	 * @param event - the ActionEvent object that triggers the method
	 */
	private void processOrder(ActionEvent event) {
		// insert to database, table: orders
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
		// name of table, add many?, array of objects (to add),
		// ArrayList<Object> fillArrayToOrder = new ArrayList<>();

		Object[] fillOrder = new Object[3];

		if (getTypeId(OrderController.getOrderType()) == 2) {
			fillOrder[0] = "orders (total_price, total_quantity, machineID, date_received, deliveryTime, typeId, statusId)";
			fillOrder[1] = false;
			fillOrder[2] = new Object[] { "(" + new DecimalFormat("##.##").format(OrderController.getOrderTotalPrice()) + ","
					+ OrderController.getOrderTotalQuantity() + "," + ClientController.getOLCurrentMachineID() + ",\""
					+ OrderController.getOrderDateReceived() + "\"" + ",\"" + OrderController.getOrderDeliveryTime() + "\""
					+ "," + getTypeId(OrderController.getOrderType()) + "," + 1 + ")" };
		} else {
			fillOrder[0] = "orders (total_price, total_quantity, machineID, date_received, typeId, statusId)";
			fillOrder[1] = false;
			fillOrder[2] = new Object[] {
					"(" + new DecimalFormat("##.##").format(OrderController.getOrderTotalPrice()) + "," + 
							OrderController.getOrderTotalQuantity() + ","
							+ ClientController.getOLCurrentMachineID() + ",\"" + OrderController.getOrderDateReceived() + "\""
							+ "," + getTypeId(OrderController.getOrderType()) + "," + 1 + ")" };
		}

		preparedMessage.setMessageSent(fillOrder);
		ClientUI.getClientController().accept(preparedMessage);

		// select from database for MAX orderID
		preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.SELECT);

		preparedMessage.setMessageSent(new Object[] { "orders", true, "MAX(orderID)", false, null, false, null });
		ClientUI.getClientController().accept(preparedMessage);

		SCCP answer = ClientController.responseFromServer;

		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> preProcessedOutput = (ArrayList<ArrayList<Object>>) answer.getMessageSent();

		String temp = "";

		for (ArrayList<Object> lst : preProcessedOutput) {
			// we expect product to have 5 columns, and act accordingly
			Object[] arr = lst.toArray();
			System.out.println(Arrays.toString(arr));
			temp = arr[0].toString();
			System.out.println(temp);
		}

		Integer maxOrderId = Integer.parseInt(temp);
		OrderController.setOrderNumber(maxOrderId);

		////////////////
		/*
		 * Rotem: 1.12.23 -> adding an insert to customer_orders (associate a customer
		 * with an order in DB)
		 */
		CustomerOrder toInsert = new CustomerOrder(ClientController.getCurrentSystemUser().getId(), maxOrderId, ClientController.getOLCurrentMachineID(), 
				OrderController.getBillingDate());
		ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.ADD,
				new Object[] { "customer_orders", false, new Object[] { toInsert } }));

		SCCP rotemRes = ClientController.responseFromServer;

		if (rotemRes.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			System.out.println("Updated customer_orders successfully!");
		} else {
			System.out.println("Failed in updating customer_orders!");
		}

		/*
		 * End Rotem -> added insert to custoemr_orders
		 */
		////////////////

		// insert to database, table: order_contents
		preparedMessage = new SCCP();

		preparedMessage.setRequestType(ServerClientRequestTypes.ADD);
		// name of table, add many?, array of objects (to add),
		ArrayList<Object> fillArrayToOrderContents = new ArrayList<>();

		Object[] fillOrderContents = new Object[3];

		fillOrderContents[0] = "order_contents";
		fillOrderContents[1] = true;

		int i = 3, j = 4;
		for (ArrayList<?> order : OrderController.getUserOrders().values()) {
			while (j < order.size() - 1) {
				fillArrayToOrderContents
						.add("(" + maxOrderId + ",\"" + order.get(i) + "\"" + ",\"" + order.get(j) + "\")");
				i += 2;
				j += 2;
			}
		}

		fillOrderContents[2] = fillArrayToOrderContents.toArray();

		preparedMessage.setMessageSent(fillOrderContents);
		ClientUI.getClientController().accept(preparedMessage);
		OrderController.setOrderNumber(OrderController.getOrderNumber() + 1);

		for (Map.Entry<String, Integer> set : EktProductFormController.productsInStockMap.entrySet()) {
			if(OrderController.getCurrentUserCart().get(set.getKey())!=null && OrderController.getCurrentUserCart().get(set.getKey()) > 0) {
				SCCP updateStock = new SCCP();
				updateStock.setRequestType(ServerClientRequestTypes.UPDATE);
				updateStock.setMessageSent(new Object[] { "products_in_machine", "stock = " + set.getValue(),
						" machineID = " + ClientController.getOLCurrentMachineID() + " AND productID = " + set.getKey() });
	
				ClientUI.getClientController().accept(updateStock);
				System.out.println("For Product " + set.getKey() + "the Stock was updated!");
			}
		}

		EktProductFormController.itemsInCart = 0;
		OrderController.getCurrentUserCart().keySet().clear();
		OrderController.getGetProductByID().keySet().clear();
		OrderController.getCartPrice().keySet().clear();
		OrderController.getUserOrders().keySet().clear();
		nextPage(event, "/gui/OrderReceiptPage.fxml", "EKrut Order Receipt");
		if (EktSystemUserLoginController.firstOrderForSubscriber()) {
			SCCP updateSubscriber = new SCCP();
			updateSubscriber.setRequestType(ServerClientRequestTypes.UPDATE);
			updateSubscriber.setMessageSent(new Object[] { "systemuser", 
					"typeOfUser = \"subscriber\"", "id = " + ClientController.getCurrentSystemUser().getId()});
			ClientUI.getClientController().accept(updateSubscriber);
		}
			
	}

}
