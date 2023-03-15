package ek_configuration;

import java.util.ArrayList;
import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * This class is the controller for the customer collect order page in the EK
 * Configuration application.
 * 
 * It handles the logic for displaying a list of orders that the customer can
 * pick up,
 * 
 * and allows the customer to mark an order as collected.
 * 
 * @author Nastya
 */
public class _EKConfigurationCustomerCollectOrderController {

	ArrayList<String> existingOrdersContents = new ArrayList<>();

	@FXML
	private Button btnFinishCollectOrder;

	@FXML
	private ListView<String> ordersByID;

	@FXML
	private Label statusLabel;

	@FXML
	private Text txtWelcomeText;

	/**
	 * 
	 * Initializes the customer collect order page by sending a query to the server
	 * to retrieve all orders that can be picked up by the customer on the current
	 * machine, and populating the list view with the results. Also sets the welcome
	 * text to greet the customer by name.
	 */
	@FXML
	private void initialize() {

		// TODO: send query to get all orders from this customer that can be picked up
		// on this machine
		// (replace the argument in the following line with the result of a query)
		// select * from orders JOIN customer_orders on
		// orders.orderID=customer_orders.orderId WHERE statusId=1 AND typeId = 1;
		ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.SELECT,
				new Object[] { "orders JOIN customer_orders on orders.orderID=customer_orders.orderId", true,
						"orders.orderID", true,
						"customerId=" + ClientController.getCurrentSystemUser().getId()
								+ " AND orders.statusId=1 AND orders.typeId=1 AND orders.machineID="
								+ ClientController.getEKCurrentMachineID(),
						false, null }));
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer
				.getMessageSent();
		for (ArrayList<Object> row : res) {
			if (row.size() > 0) {
				existingOrdersContents.add(row.get(0).toString());
			}
		}
		ordersByID.getItems().addAll(existingOrdersContents);
		txtWelcomeText.setText("Hi " + ClientController.getCurrentSystemUser().getFirstName() + ", glad you are back");

	}

	/**
	 * Handles the logic for the customer to mark an order as collected.
	 * 
	 * Sends a query to the server to update the status of the selected order to
	 * "completed"
	 * 
	 * and displays a message to the customer confirming the order has been
	 * collected.
	 * 
	 * @param event The action event that triggered the method call
	 */
	@FXML
	void getBtnFinishCollectOrder(ActionEvent event) {
		String orderIdString = ordersByID.getSelectionModel().getSelectedItem();
		System.out.println("Selected: " + orderIdString);

		System.out.println("Processing . . .");
		// send this query:
		ClientUI.getClientController().accept(new SCCP(ServerClientRequestTypes.UPDATE,
				new Object[] { "orders", "statusId=3", "orderID=" + orderIdString }));
		System.out.println("Your order has been dispensed, waiting for you to collect your items.");

		// TODO:
		// insert a query to set the order status of the selected order to "completed".

		System.out.println("Enjoy!");
		// load previous page:
		((Node) event.getSource()).getScene().getWindow().hide();

		goBack(event);
	}

	/**
	 * Closes the current window and goes back to the previous page.
	 *
	 * @param event The action event that triggered the method call
	 */
	private void goBack(ActionEvent event) {
		// prepare the new stage:
		Stage primaryStage = new Stage();
		String nextScreenPath = "/gui/_EKConfigurationCustomerHomeArea.fxml";
		String nextPathTitle = "Customer Home Frame";
		WindowStarter.createWindow(primaryStage, this, nextScreenPath, null, nextPathTitle, true);
		primaryStage.show();
	}

}
