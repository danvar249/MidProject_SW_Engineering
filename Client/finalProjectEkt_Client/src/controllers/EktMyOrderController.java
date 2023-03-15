package controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The EktMyOrderController class is responsible for handling the interactions
 * and logic for the "My Orders" section of the application. This class uses
 * JavaFX and handles the display of orders in progress and completed orders in
 * tab panes.
 * 
 * @author Maxim, Dima, Rotem.
 * @version 1.0
 */
public class EktMyOrderController {
	// FXML annotation indicates that these elements are associated with FXML files
	// that define the layout of the UI
	@FXML
	private BorderPane borderPaneComplete; // variable for holding complete tasks
	@FXML
	private BorderPane borderPaneInProgress; // variable for holding tasks that are
	@FXML
	private Button btnBack; // variable for a "back" button
	@FXML
	private GridPane gridPane; // variable for organizing the layout of the UI
	@FXML
	private TabPane inProgressTabPane; // variable for holding tabs for tasks in
	@FXML
	private TabPane completedTabPane; // variable for holding tabs for completed

	/**
	 * The initialize method sets up the display for orders in progress and
	 * completed orders. It creates and configures a VBox, ScrollPane, and GridPane
	 * to display order information. It also creates and configures TabPanes to
	 * display tabs of orders in progress and completed orders. It retrieves orders
	 * data from the server through SCCP and ClientUI and displays the orders data
	 * in the tab pane.
	 *
	 */
	public void initialize() {
		// create a VBox to hold products
		VBox productsVbox = new VBox();
		// create a ScrollPane with the VBox as its content
		ScrollPane centerScrollBar = new ScrollPane(productsVbox);
		// set the width and height of the ScrollPane
		centerScrollBar.setPrefWidth(750);
		centerScrollBar.setPrefHeight(300);
		centerScrollBar.getStylesheets().add("controllers/testCss.css");
		// set the style of the ScrollPane
		centerScrollBar.setStyle(
				"-fx-background-color: transparent; -fx-background-color:  linear-gradient(from 0px 0px to 0px 1500px, pink, yellow);");
		// create a new GridPane
		gridPane = new GridPane();
		// set the maximum size of the GridPane
		gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

		//// In Progress////
		// create a new SCCP object
		SCCP preparedMessageForInProgress = new SCCP();
		// set the request type of the SCCP object
		preparedMessageForInProgress.setRequestType(ServerClientRequestTypes.SELECT);
		// create a variable for orderId
		Integer orderId = 0;
		// set the messageSent property of the SCCP object
		preparedMessageForInProgress.setMessageSent(new Object[] {
				"orders JOIN machine ON orders.machineID = machine.machineId"
						+ " JOIN order_contents ON orders.orderID = order_contents.orderID"
						+ " JOIN product ON order_contents.productID = product.productID" +
						" JOIN customer_orders on customer_orders.orderId = orders.orderID",
				true,
				"orders.orderID, machine.machineName,"
						+ "orders.date_received, product.productName, orders.total_quantity, orders.total_price, orders.statusId",
				true, "(orders.statusId = 1 OR orders.statusId = 5) AND customerId = " + ClientController.getCurrentSystemUser().getId(),
				true, "ORDER BY orders.orderID" });
		// Log message
		System.out.println("Client: Sending " + "order" + " to server.");
		Set<Integer> orderIDs = new HashSet<>();
		ClientUI.getClientController().accept(preparedMessageForInProgress);
		// check the response from the server
		if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			// create an ArrayList from the response
			ArrayList<?> arrayOfOrders = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			// iterate over the orders
			for (Object order : arrayOfOrders) {
				System.out.println("ORDER="+order+", current added orders=");
				// check the orderId
				// get the order id
				orderId = (Integer) ((ArrayList<?>) order).get(0);
				if (!orderIDs.contains(orderId)) {
					orderIDs.add(orderId);
					// create new UI elements
					Tab orderTab = new Tab();
					Pane orderPane = new Pane();
					Text location = new Text();
					
					Text date = new Text();

					Text quantity = new Text();
					quantity.setStyle("-fx-font-weight: bold");
					Text price = new Text();
					price.setStyle("-fx-font-weight: bold");
					Text txtStatus = new Text();
					txtStatus.setStyle("-fx-font-weight: bold");
					String status = ((ArrayList<?>) order).get(6).toString();
					Button received = new Button();
					received.setStyle("-fx-background-color: crimson; -fx-border-width: 3; -fx-border-color: crimson;"
							+ "-fx-text-weight: bold;");

					location.setText("Location: " + ((ArrayList<?>) order).get(1).toString());
					location.setStyle("-fx-font-weight: bold");
					date.setText("Date: " + ((ArrayList<?>) order).get(2).toString());
					date.setStyle("-fx-font-weight: bold");
					// checks if the status of the order is 5 (delivered)
					if (status.equals("5")) {
						// sets the status text to "delivered"
						txtStatus.setText("Order Status Delivered");
						// sets the text on the "received" button
						received.setText("Click if you received");
						received.setLayoutX(15); // sets the x-coordinate of the button
						received.setLayoutY(200); // sets the y-coordinate of the button
						received.setFont(new Font(15)); // sets the font of the button
						orderPane.getChildren().add(received); // adds the button to the orderPane
					}
					ArrayList<String> productList = new ArrayList<>(); // creates an array list for storing product
																		// names
					// iterates through the array of orders
					for (Object product : arrayOfOrders) {
						// checks if the order ID matches the current product's order ID
						if (orderId.equals(((ArrayList<?>) product).get(0))) {
							// if it does, add the product name to the productList array
							productList.add(((ArrayList<?>) product).get(3).toString());
						}
					}
					System.out.println("Products in order " + orderId + ": " + productList);
					int i = 45, j = 20;

					TitledPane tp = new TitledPane(); // creates a new titled pane
					tp.toFront();
					tp.getStylesheets().add("/gui/tiltedPaneCSS.css");
					tp.setText("Click Here To See Product List"); // sets the text on the titled pane
					tp.setFont(new Font(14)); // sets the font of the text
					tp.setLayoutX(275); // sets the x-coordinate of the titled pane
					tp.setLayoutY(15); // sets the y-coordinate of the titled pane
					tp.setPrefWidth(200); // sets the width of the titled pane
					tp.setExpanded(false); // sets the titled pane to be initially closed
					Pane intoTp = new Pane(); // creates a new pane to hold the products
					// iterates through the productList array
					for (String productName : productList) {
						Text Products = new Text(); // creates a new text object to hold the product name
						Products.setStyle("-fx-font-size: 10px;");
						Products.setText(productName); // sets the text of the product
						Products.setLayoutX(i); // sets the x-coordinate of the product text
						Products.setLayoutY(j); // sets the y-coordinate of the product text
						j += 35; // increments the y-coordinate for the next product
						Products.setFont(new Font(18)); // sets the font of
						intoTp.getChildren().add(Products);
					}
					tp.setContent(intoTp);// Set the content of TextPane "tp" to "intoTp"
					orderPane.getChildren().add(tp);// Add TextPane "tp" to the "orderPane" container
					// Set the text of "quantity" label to "Total Quantity: " + the 4th element of
					// the "order" ArrayList
					quantity.setText("Total Quantity: " + ((ArrayList<?>) order).get(4).toString());
					// Set the text of "price" label to "Total Price: " + the 5th element of the
					// "order" ArrayList
					price.setText("Total Price: " + new DecimalFormat("##.##").format(new Double(((ArrayList<?>) order).get(5).toString())));
					// Set the x-coordinate of "location" label to 15
					location.setLayoutX(15);
					// Set the y-coordinate of "location" label to 35
					location.setLayoutY(35);
					// Set the font size of "location" label to 18
					location.setFont(new Font(18));
					// Add "location" label to the "orderPane" container
					orderPane.getChildren().add(location);

					date.setLayoutX(15); // Set the x-coordinate of "date" label to 15
					date.setLayoutY(70); // Set the y-coordinate of "date" label to 70
					date.setFont(new Font(18)); // Set the font size of "date" label to 18
					orderPane.getChildren().add(date); // Add "date" label to the "orderPane" container

					quantity.setLayoutX(550); // Set the x-coordinate of "quantity" label to 550
					quantity.setLayoutY(35); // Set the y-coordinate of "quantity" label to 35
					quantity.setFont(new Font(18)); // Set the font size of "quantity" label to 18
					orderPane.getChildren().add(quantity); // Add "quantity" label to the "orderPane" container

					price.setLayoutX(550); // Set the x-coordinate of "price" label to 550
					price.setLayoutY(70); // Set the y-coordinate of "price" label to 70
					price.setFont(new Font(18)); // Set the font size of "price" label to 18
					orderPane.getChildren().add(price); // Add "price" label to the "orderPane" container

					txtStatus.setLayoutX(15); // Set the x-coordinate of "txtStatus" label to 15
					txtStatus.setLayoutY(100); // Set the y-coordinate of "txtStatus" label to 100
					txtStatus.setFont(new Font(18)); // Set the font size of "txtStatus" label to 18
					orderPane.getChildren().add(txtStatus); // Add "txtStatus" label to the "orderPane" container
					// Create a new button "reqToCancel"
					Button reqToCancel = new Button();
					reqToCancel.setStyle("-fx-background-color: crimson; -fx-border-width: 3; -fx-border-color: crimson;"
							+ "-fx-text-weight: bold;");
					reqToCancel.setText("Request to cancel order"); // Set the text of "reqToCancel" button to "Request
																	// to cancel order"
					reqToCancel.setLayoutX(535); // Set the x-coordinate of "reqToCancel" button to 535
					reqToCancel.setLayoutY(180); // Set the y-coordinate of "reqToCancel" button to 200
					reqToCancel.setFont(new Font(15)); // Set the font size of "reqToCancel" button to 15
					orderPane.getChildren().add(reqToCancel); // Add "reqToCancel" button to the "orderPane" container

					// Set an action listener for the "reqToCancel" button
					reqToCancel.setOnAction(event -> {
						// Create a new Alert dialog box with type CONFIRMATION
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.initStyle(StageStyle.UNDECORATED); // Remove the window decoration of the alert dialog
						alert.setTitle("Cancel Order"); // Set the title of the alert dialog to "Cancel Order"
						alert.setHeaderText("This action will send requset to cancel order!"); // Set the header text of
																								// the alert dialog to
																								// "This action will
																								// send requset to
																								// cancel order!"
						alert.setContentText("Are you sure you want to continue?"); // Set the content text of the alert
																					// dialog to "Are you sure you want
																					// to continue?"
						// Show the alert dialog and wait for user input
						Optional<ButtonType> result = alert.showAndWait();
						// If the user clicks the OK button
						if (result.get() == ButtonType.OK) {
							// Print a message to the console
							System.out.println("Sending requset to cancel Order...");
							// Create a new SCCP object
							SCCP preparedMessage = new SCCP();
							// Set the request type of the SCCP object to UPDATE
							preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE);
							// Create a new object array with 3 elements
							Object[] changeOrderStatus = new Object[3];

							changeOrderStatus[0] = "orders";
							changeOrderStatus[1] = "statusId = 4";
							changeOrderStatus[2] = "orderID = " + ((ArrayList<?>) order).get(0);

							preparedMessage.setMessageSent(changeOrderStatus);
							ClientUI.getClientController().accept(preparedMessage);

							((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
							Stage primaryStage = new Stage();

							WindowStarter.createWindow(primaryStage, this, "/gui/EktMyOrderFrom.fxml", null,
									"Ekt My Orders", true);
							primaryStage.show();
							((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

						}

						else if (result.get() == ButtonType.CANCEL) {
							System.out.println("Cancel Order was canceled");
						}
					});

					received.setOnAction(event -> {

						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.initStyle(StageStyle.UNDECORATED);
						alert.setTitle("Order Was Received");
						alert.setHeaderText("Are you sure you received the order?");
						alert.setContentText("Are you sure you want to continue?");
						Optional<ButtonType> result = alert.showAndWait();

						if (result.get() == ButtonType.OK) {
							System.out.println("Sending requset to update order status...");

							SCCP preparedMessage = new SCCP();

							preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE);
							// name of table, add many?, array of objects (to add),
							// ArrayList<Object> fillArrayToOrder = new ArrayList<>();

							Object[] changeOrderStatus = new Object[3];

							changeOrderStatus[0] = "orders";
							changeOrderStatus[1] = "statusId = 6";
							changeOrderStatus[2] = "orderID = " + ((ArrayList<?>) order).get(0);

							preparedMessage.setMessageSent(changeOrderStatus);
							ClientUI.getClientController().accept(preparedMessage);

							((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
							Stage primaryStage = new Stage();

							WindowStarter.createWindow(primaryStage, this, "/gui/EktMyOrderFrom.fxml", null,
									"Ekt My Orders", false);
							primaryStage.show();
							((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

						}

						else if (result.get() == ButtonType.CANCEL) {
							System.out.println("Order was not received");
						}
					});

					orderTab.setContent(orderPane);
					orderTab.setText(orderId.toString());

					inProgressTabPane.getTabs().add(orderTab);
				}
			}
		}
		borderPaneInProgress.setCenter(inProgressTabPane);
		//// End In Progress////

		//// Complete////
		SCCP preparedMessageForComplete = new SCCP();

		preparedMessageForComplete.setRequestType(ServerClientRequestTypes.SELECT);

		Integer orderIdForComplete = 0;

		preparedMessageForComplete.setMessageSent(new Object[] {
				"orders JOIN machine ON orders.machineID = machine.machineId"
						+ " JOIN order_contents ON orders.orderID = order_contents.orderID"
						+ " JOIN product ON order_contents.productID = product.productID"
						+ " JOIN customer_orders on customer_orders.orderId = orders.orderID",
				true,
				"orders.orderID, machine.machineName,"
						+ "orders.date_received, product.productName, orders.total_quantity, orders.total_price",
				true, "(orders.statusId = 3 OR orders.statusId = 6) AND customerId = " + ClientController.getCurrentSystemUser().getId(), true, "ORDER BY orders.orderID LIMIT 20" });
		// Log message
		System.out.println("Client: Sending " + "order" + " to server.");

		ClientUI.getClientController().accept(preparedMessageForComplete);
		if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			ArrayList<?> arrayOfOrders = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();

			for (Object order : arrayOfOrders) {
				if (orderIdForComplete != (Integer) ((ArrayList<?>) order).get(0)) {
					orderIdForComplete = (Integer) ((ArrayList<?>) order).get(0);
					Tab orderTab = new Tab();
					Pane orderPane = new Pane();
					Text location = new Text();
					Text date = new Text();

					Text quantity = new Text();
					Text price = new Text();

					location.setText("Location: " + ((ArrayList<?>) order).get(1).toString());
					date.setText("Date: " + ((ArrayList<?>) order).get(2).toString());

					ArrayList<String> productList = new ArrayList<>();

					for (Object product : arrayOfOrders)
						if (orderIdForComplete == (Integer) ((ArrayList<?>) product).get(0))
							productList.add(((ArrayList<?>) product).get(3).toString());

					int i = 45, j = 20;

					TitledPane tp = new TitledPane();
					tp.getStylesheets().add("/gui/tiltedPaneCSS.css");
					tp.setText("Click Here To See Product List");
					tp.setFont(new Font(14));
					tp.setLayoutX(275);
					tp.setLayoutY(15);
					tp.setPrefWidth(200);
					tp.setExpanded(false);
					Pane intoTp = new Pane();

					for (String productName : productList) {
						Text Products = new Text();
						Products.setText(productName);
						Products.setLayoutX(i);
						Products.setLayoutY(j);
						j += 35;
						Products.setFont(new Font(18));
						intoTp.getChildren().add(Products);
					}
					tp.setContent(intoTp);
					orderPane.getChildren().add(tp);

					quantity.setText("Total Quantity: " + ((ArrayList<?>) order).get(4).toString());
					price.setText("Total Price: " + ((ArrayList<?>) order).get(5).toString());

					location.setLayoutX(15);
					location.setLayoutY(35);
					location.setFont(new Font(18));
					orderPane.getChildren().add(location);

					date.setLayoutX(15);
					date.setLayoutY(70);
					date.setFont(new Font(18));
					orderPane.getChildren().add(date);

					quantity.setLayoutX(550);
					quantity.setLayoutY(35);
					quantity.setFont(new Font(18));
					orderPane.getChildren().add(quantity);

					price.setLayoutX(550);
					price.setLayoutY(70);
					price.setFont(new Font(18));
					orderPane.getChildren().add(price);

					orderTab.setContent(orderPane);
					orderTab.setText(orderIdForComplete.toString());

					completedTabPane.getTabs().add(orderTab);
				}
			}
		}
		borderPaneComplete.setCenter(completedTabPane);
		//// End Complete////
	}

	/**
	 * This method is used to handle the event of clicking the "back" button. It
	 * hides the current window and opens a new window with the Ekt Catalog Form.
	 * 
	 * @param event the ActionEvent object that triggers the method
	 * 
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog", true);

		primaryStage.show();
	}
}
