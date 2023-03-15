package controllers;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import client.ClientController;
import common.WindowStarter;
import entityControllers.OrderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.superProduct;

/**
 * This class represents the controller for the EktOrderSummary.fxml file, which
 * is used to display the summary of a customer's order. It imports various
 * JavaFX classes and the ClientController class, which is used to communicate
 * with the server. It also uses the LocalDateTime class to format the order and
 * delivery times. It contains various FXML elements such as a BorderPane,
 * Button, Text, and GridPane. The initialize() method is used to set up the
 * layout of the order summary page and display the details of the customer's
 * order. The btnApprove, btnBack, and btnClose buttons are used for the user to
 * approve, go back, and close the window respectively. The txtOrderTotal Text
 * element displays the total cost of the order. The gridPane GridPane is used
 * to display the details of the products in the customer's order. The
 * totalQuantity variable keeps track of the total quantity of products in the
 * order. The OrderInformation ArrayList stores information about the customer's
 * order.
 * 
 * @author Dima, Maxim, Rotem
 */

public class EktOrderSummaryController {
	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnApprove;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnClose;

	@FXML
	private Text txtOrderTotal;

	private GridPane gridPane;

	private Integer totalQuantity = 0;

	private ArrayList<String> OrderInformation = new ArrayList<>();

	/**
	 * The initialize() method is used to set up the layout of the order summary
	 * page and display the details of the customer's order. It creates a VBox and a
	 * ScrollPane, and sets their dimensions and background color. It also creates a
	 * GridPane, which is used to display the details of the products in the
	 * customer's order. It sets the number of columns for the GridPane and the size
	 * of each column. It also creates a DateTimeFormatter object and uses it to
	 * format the order and delivery times, which are then stored in the
	 * ClientController class. It then adds the order date, order number, and "Items
	 * in order:" to the OrderInformation ArrayList. It then iterates through the
	 * products in the customer's cart and displays their name, quantity, cost per
	 * unit, and image in the GridPane. It also calculates the total cost of the
	 * order and stores it in the totalPrice variable.
	 */
	public void initialize() {
		VBox productsVbox = new VBox();
		ScrollPane centerScrollBar = new ScrollPane(productsVbox);

		centerScrollBar.setPrefHeight(600);
		centerScrollBar.setPrefWidth(800);
		centerScrollBar.getStylesheets().add("controllers/testCss.css");
		centerScrollBar.setStyle(
				"-fx-background-color:  linear-gradient(from -200px 0px to 0px 1800px, #a837b4, transparent);");
		gridPane = new GridPane();

		Double totalPrice = 0.0;
		final int numCols = 4;
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800 / 4);
			gridPane.getColumnConstraints().add(colConst);
		}

		///////////////////////// Dima 31/12 10:30
		gridPane.setMaxSize(800-20, Region.USE_COMPUTED_SIZE);
		gridPane.setPrefSize(800 - 2, 550);
		gridPane.setVgap(5);
		//////////////////////////////////////////////////////
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime tomorrow = now.plusDays(1);
		tomorrow = tomorrow.plusHours(1);

		OrderController.setOrderDateReceived(dtf.format(now));
		OrderController.setOrderDeliveryTime(dtf.format(tomorrow));

		OrderInformation.add(OrderController.getOrderDateReceived());
		OrderInformation.add(OrderController.getOrderNumber().toString());
		OrderInformation.add("Items in order:");

		int i = 0, j = 0;
		for (superProduct product : OrderController.getGetProductByID().values()) {

			if (!(OrderController.getCartPrice().get(product) == 0.0)) {
				String currentProductID = product.getProductID();
				Text productName = new Text("" + product.getProductName());
				productName.setFill(Color.WHITE);
				productName.setStyle("-fx-font: 20 System; -fx-font-weight: bold;");

				Integer quantityNum = OrderController.getCurrentUserCart().get(currentProductID);
				totalQuantity += quantityNum;
				Text quantity = new Text("Quantity: " + (quantityNum).toString());
				quantity.setFill(Color.WHITE);
				Double costPerUnit = Double.valueOf(product.getCostPerUnit());
//				if (ClientController.getCustomerIsSubsriber() != null && ClientController.getCustomerIsSubsriber()) {
//					if (firstOrderForSubscriber())
//						costPerUnit *= COST_REDUCTION_PER_SUBSRIBER;
//				}
				Double totalSum = quantityNum * costPerUnit;
				Text sum = new Text("Cost: " + (new DecimalFormat("##.##").format(totalSum)).toString() + " $");
				sum.setFill(Color.WHITE);
				totalPrice += totalSum;
				Label emptySpace = new Label("");
				emptySpace.setMinHeight(75);
				productName.setFont(new Font(18));
				quantity.setFont(new Font(18));
				sum.setFont(new Font(18));

				Image img = new Image(new ByteArrayInputStream(product.getFile()));

				ImageView productImageView = new ImageView(img);
				productImageView.setFitHeight(75);
				productImageView.setFitWidth(75);
				productImageView.setTranslateX(20);
				productImageView.setTranslateY(0);

				gridPane.add(productImageView, j, i);
				GridPane.setHalignment(productImageView, HPos.CENTER);

				j++;
				gridPane.add(productName, j, i);
				GridPane.setHalignment(productName, HPos.CENTER);

				j++;
				gridPane.add(quantity, j, i);
				GridPane.setHalignment(quantity, HPos.CENTER);

				j++;
				gridPane.add(sum, j, i);
				GridPane.setHalignment(sum, HPos.CENTER);

				i++;
				j = 0;

				// Max 7/1: add product name in order to array
				OrderInformation.add(product.getProductID());
				OrderInformation.add(quantityNum.toString());
			}
		}
		txtOrderTotal.setText("ORDER TOTAL: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
		txtOrderTotal.setLayoutX(400 - txtOrderTotal.minWidth(0) / 2);
		OrderController.setOrderTotalPrice(totalPrice);
		OrderInformation
				.add("\nAt a total price of " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
		// Max 7/1: Add to user order hash map the items in the order!
		OrderController.getUserOrders().put(OrderController.getOrderNumber(), OrderInformation);
		OrderController.setOrderTotalQuantity(totalQuantity);

		productsVbox.getChildren().add(gridPane);
		borderPane.setCenter(centerScrollBar);

	}

	// Rip this code- JAVADOC'd for nothing
	/**
	 * The firstOrderForSubscriber() method is used to check if the current customer
	 * has made any previous orders. It sends a query to the server to select the
	 * orderID from the customer_orders table where the customerId is equal to the
	 * current user's ID. If the query returns no results, it returns true as it
	 * means that this is the customer's first order. If the query returns any
	 * results, it returns false as it means that the customer has made previous
	 * orders. If the connected user is not a subscriber, it will print an error
	 * message. It uses the SCCP class, ServerClientRequestTypes enum, and the
	 * ClientController class to communicate with the server.
	 * 
	 * @return boolean, true if the user is a subscriber and this is his first
	 *         order, false otherwise.
	 */
//	private boolean firstOrderForSubscriber() {
//		// send the following query:
//		// select orderID from customer_orders WHERE customerId=ConnectedClientID;
//		// if empty, return true, else false
//		if (ClientController.getCustomerIsSubsriber() == null || !ClientController.getCustomerIsSubsriber()) {
//			System.out.println("Invalid call to firstOrderForSubscriber() -> connected user is not a subsriber");
//		}
//		ClientUI.clientController
//				.accept(new SCCP(ServerClientRequestTypes.SELECT, new Object[] { "customer_orders", true, "orderID",
//						true, "customerId = " + ClientController.getCurrentSystemUser().getId(), false, null }));
//		if (!ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
////			throw new RuntimeException("Invalid database operation");
//			System.out.println(
//					"Invalid database operation (checking subsriber orders history failed). (returnin false [no existing orders])");
//		}
//		@SuppressWarnings("unchecked")
//		ArrayList<ArrayList<Object>> res = (ArrayList<ArrayList<Object>>) ClientController.responseFromServer
//				.getMessageSent();
//		// true if we have NO ORDERS else false
//		return res.size() == 0;
//	}

	/**
	 * The getBtnApprove() method is used to handle the event when the "Approve"
	 * button is pressed. It creates a new Stage and opens the EktPaymentForm.fxml
	 * window using the WindowStarter.createWindow method. It also closes the
	 * current window. It uses the Stage and Node classes from JavaFX to open and
	 * close the windows.
	 * 
	 * @param event An ActionEvent object that is passed in when the button is
	 *              pressed.
	 */
	@FXML
	void getBtnApprove(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktPaymentForm.fxml", null, "payment", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

	}

	/**
	 * The getBtnBack() method is used to handle the event when the "Back" button is
	 * pressed. It closes the current window and opens the EktCartForm.fxml window
	 * using the WindowStarter.createWindow method. It uses the Stage and Node
	 * classes from JavaFX to open and close the windows.
	 * 
	 * @param event An ActionEvent object that is passed in when the button is
	 *              pressed.
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCartForm.fxml", null, "Ekt Cart", true);

		primaryStage.show();
	}

	/**
	 * The getBtnClose() method is used to handle the event when the "Close" button
	 * is pressed. It creates a confirmation alert that asks the user if they want
	 * to cancel the order, if the user confirms it will cancel the order, it will
	 * clear the cart and close the current window, it will then open the
	 * EktCatalogForm.fxml window using the WindowStarter.createWindow method. If
	 * the user cancels the alert it will close the alert and will open the
	 * EktOrderSummary.fxml window. It use the Stage and Node classes from JavaFX to
	 * open and close the windows, Alert class to create the alert
	 * 
	 * @param event An ActionEvent object that is passed in when the button is
	 *              pressed.
	 */
	@FXML
	void getBtnClose(ActionEvent event) {
		// Alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.setTitle("Cancel Order");
		alert.setHeaderText("This action will remove all items from the order!");
		alert.setContentText("Are you sure you want to continue?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			System.out.println("Canceling Order...");
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();

			// category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
					"/gui/EktCatalogForm.fxml", null, OrderController.getCurrentProductCategory().get(0), true);

			EktProductFormController.itemsInCart = 0;
			OrderController.getGetProductByID().keySet().clear();
			OrderController.getCartPrice().keySet().clear();
			OrderController.getCurrentUserCart().keySet().clear();
			;
			primaryStage.show();
			//////////////////////
			((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
		}

		else if (result.get() == ButtonType.CANCEL) {
			System.out.println("Cancel Order was canceled");
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			// category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
					"/gui/EktOrderSummary.fxml", null, "Order Summary", true);

			primaryStage.show();

		}
	}
}
