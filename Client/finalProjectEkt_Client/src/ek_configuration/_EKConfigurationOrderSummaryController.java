package ek_configuration;

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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.superProduct;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * 
 * The _EKConfigurationOrderSummaryController class handles the functionality of
 * the order summary screen in the EK Configuration application.
 * 
 * It allows the user to view the details of the order such as the order number,
 * order total and delivery address.
 * 
 * It also allows the user to approve the order, go back to the previous screen
 * or close the application.
 * 
 * @author Dima Kislitsyn
 * 
 * @version 1.0
 */
public class _EKConfigurationOrderSummaryController {
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
	 * This class initializes the view for displaying the order details of a
	 * customer's purchase. It creates a VBox, ScrollPane and GridPane to layout the
	 * products, along with the product image, name, quantity, and cost. The class
	 * also sets the delivery date and time for the order and calculates the total
	 * price for the order. It also sets the style, size and layout of the view
	 * using CSS stylesheets.
	 */
	public void initialize() {
		VBox productsVbox = new VBox();
		ScrollPane centerScrollBar = new ScrollPane(productsVbox);

		centerScrollBar.setPrefHeight(600);
		centerScrollBar.setPrefWidth(800);
		centerScrollBar.getStylesheets().add("controllers/testCss.css");
		centerScrollBar.setStyle(
				"-fx-background-color: transparent; -fx-background-color:   linear-gradient(from -200px 0px to 0px 1800px, #a837b4, transparent);");
		gridPane = new GridPane();

		Double totalPrice = 0.0;
		final int numCols = 4;
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800 / 4);
			gridPane.getColumnConstraints().add(colConst);
		}

		///////////////////////// Dima 31/12 10:30
		gridPane.setMaxSize(800 - 5, Region.USE_COMPUTED_SIZE);
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
				productName.setStyle("-fx-font: 20 System; -fx-font-weight: bold; -fx-text-fill: white;");

				Integer quantityNum = OrderController.getCurrentUserCart().get(currentProductID);
				totalQuantity += quantityNum;
				Text quantity = new Text("Quantity: " + (quantityNum).toString());
				quantity.setFill(Color.WHITE);
				Double costPerUnit = Double.valueOf(product.getCostPerUnit());
//				if(ClientController.getCustomerIsSubsriber()!=null && ClientController.getCustomerIsSubsriber()) {
//					if(firstOrderForSubscriber())
//					costPerUnit *= COST_REDUCTION_PER_SUBSRIBER;
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

	/**
	 * 
	 * The method getBtnApprove handles the functionality of the approve button on
	 * the Order Summary form.
	 * 
	 * When clicked, it opens the payment form and closes the current form.
	 * 
	 * @param event the ActionEvent that triggers the method, in this case the
	 *              approve button being clicked
	 */
	@FXML
	void getBtnApprove(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationPaymentForm.fxml", null, "payment", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window

	}

	/**
	 * The getBtnBack() method is used to handle the event when the "Back" button is
	 * pressed. It closes the current window and opens the
	 * EKConfigurationCartForm.fxml window using the WindowStarter.createWindow
	 * method. It uses the Stage and Node classes from JavaFX to open and close the
	 * windows.
	 * 
	 * @param event An ActionEvent object that is passed in when the button is
	 *              pressed.
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationCartForm.fxml", null, "Ekt Cart", true);

		primaryStage.show();
	}

	/**
	 * The getBtnClose() method is used to handle the event when the "Close" button
	 * is pressed. It creates a confirmation alert that asks the user if they want
	 * to cancel the order, if the user confirms it will cancel the order, it will
	 * clear the cart and close the current window, it will then open the
	 * EktCatalogForm.fxml window using the WindowStarter.createWindow method. If
	 * the user cancels the alert it will close the alert and will open the
	 * EKConfigurationCustomerLocalOrderFrame.fxml window. It use the Stage and Node
	 * classes from JavaFX to open and close the windows, Alert class to create the
	 * alert
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
					"/gui/_EKConfigurationCustomerLocalOrderFrame.fxml", null,
					OrderController.getCurrentProductCategory().get(0), true);

			_EKConfigurationProductController.itemsInCart = 0;
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
					"/gui/_EKConfigurationOrderSummary.fxml", null, "Order Summary", true);

			primaryStage.show();

		}
	}
}
