package ek_configuration;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Product;
import logic.superProduct;

/**
 * 
 * The _EKConfigurationCartController class is a JavaFX controller class for the
 * cart view of the EK Configuration application.
 * 
 * It is responsible for displaying the products in the user's cart, calculating
 * the total price of the items in the cart,
 * 
 * and handling user interactions with the cart, such as canceling an order.
 * 
 * @author [nastya]
 * 
 * @version [1]
 * 
 * @since [17.1.23]
 */
public class _EKConfigurationCartController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnCancelOrder;

	@FXML
	private Text txtTotalPrice;

	@FXML
	private Pane btmPane;

	private GridPane gridpaneIntoVbox;

	private boolean emptyCart = true;

	private VBox vboxCart;

	private Double priceToAdd = 0.0;

	private Double totalPrice = 0.0;

	private TextField tf = new TextField();

	/**
	 * 
	 * A private method that calculates the price of a single item in the user's
	 * cart based on its cost per unit and quantity.
	 * 
	 * @param costPerUnit the cost per unit of the product
	 * @param quantityNum the quantity of the product in the user's cart
	 * @param product     the Product object representing the item in the user's
	 *                    cart
	 */
	private void calculatePriceToAdd(Double costPerUnit, Integer quantityNum, Product product) {

		quantityNum = OrderController.getCurrentUserCart().get(product.getProductID());
		costPerUnit = Double.valueOf(product.getCostPerUnit());
		priceToAdd = quantityNum * costPerUnit;

	}

	/**
	 * 
	 * A private method that calculates the total price of all items in the user's
	 * cart.
	 */
	private void calculateTotalPrice() {
		totalPrice = 0.0;
		for (Product product : OrderController.getCartPrice().keySet()) {
			System.out.println("Adding the price of " + product.getProductName() + "in");
			totalPrice += OrderController.getCartPrice().get(product);
		}
		if (totalPrice == 0.0) {
			emptyCart = true;
			System.out.println("The cart is empty right now!");
		}
	}

	/**
	 * A method that is called automatically when the cart view is loaded. It is
	 * responsible for initializing the UI elements of the cart view, and setting up
	 * any necessary event handlers.
	 */
	@FXML
	public void initialize() {
		OrderController.setOrderType("");
		OrderController.setPickupPlace("");

		vboxCart = new VBox();
		gridpaneIntoVbox = new GridPane();

		txtTotalPrice.setText("CART TOTAL: 0$");
		txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0) / 2);

		final int numCols = 6;
		Double costPerUnit = 0.0;

		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800 / 6);
			gridpaneIntoVbox.getColumnConstraints().add(colConst);
		}
		gridpaneIntoVbox.setMaxSize(790, Region.USE_COMPUTED_SIZE);
		gridpaneIntoVbox.setPrefSize(800 - 10, 550);
		gridpaneIntoVbox.setHgap(5);
		;
		gridpaneIntoVbox.setVgap(5);
		;
		OrderController.setOrderType("Local");

		int i = 0, j = 0;
		for (superProduct product : OrderController.getGetProductByID().values()) {
			String currentProductID = product.getProductID();
			calculatePriceToAdd(costPerUnit, OrderController.getCurrentUserCart().get(currentProductID), product);
			OrderController.getCartPrice().put(product, priceToAdd);
			calculateTotalPrice();
			txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
			txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0) / 2);

			Image removeItemIcon = new Image("controllers/Images/removeItemFromCart.png");
			ImageView removeItemIconImageView = new ImageView(removeItemIcon);
			removeItemIconImageView.setFitHeight(30);
			removeItemIconImageView.setFitWidth(30);

			Image addOneToCartIcon = new Image("controllers/Images/addOneToCart.png");
			ImageView addOneToCartIconImageView = new ImageView(addOneToCartIcon);
			addOneToCartIconImageView.setFitHeight(30);
			addOneToCartIconImageView.setFitWidth(30);

			Image removeOneToCartIcon = new Image("controllers/Images/removeOneFromCart.png");
			ImageView removeOneFromCartIconImageView = new ImageView(removeOneToCartIcon);
			removeOneFromCartIconImageView.setFitHeight(30);
			removeOneFromCartIconImageView.setFitWidth(30);

			///////////////////////////////////////////////////////

			Image img = new Image(new ByteArrayInputStream(product.getFile()));

			ImageView productImageView = new ImageView(img);

			productImageView.setFitHeight(75);
			productImageView.setFitWidth(75);
			productImageView.setTranslateX(20);
			productImageView.setTranslateY(0);
			gridpaneIntoVbox.add(productImageView, j, i);

			///////////////////////////////////////////////////////

			Text productName = new Text(product.getProductName());
			Text quantityLabel = new Text("Quantity: " + OrderController.getCurrentUserCart().get(currentProductID));
			quantityLabel.setFill(Color.WHITE);
			productName.setFill(Color.WHITE);

			productName.setStyle("-fx-font: 18 System; -fx-font-weight: bold; -fx-text-fill:  #DADF76");
			productName.setFont(new Font(18));
			quantityLabel.setFont(new Font(18));
			quantityLabel.setStyle("-fx-text-fill: #DADF76");

			Button removeButton = new Button();
			Button addButton = new Button();
			Button removeOneButton = new Button();
			removeButton.setFont(new Font(18));
			addButton.setFont(new Font(18));
			removeOneButton.setFont(new Font(18));
			/////////////////////// Dima 31/12 10:18
			removeButton.setPrefSize(50, 50);
			removeButton.setGraphic(removeItemIconImageView);
			removeButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; "
					+ "-fx-border-width: 3px; -fx-border-radius: 100");

			addButton.setPrefSize(50, 50);
			addButton.setGraphic(addOneToCartIconImageView);
			addButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; "
					+ "-fx-border-width: 3px; -fx-border-radius: 100");

			removeOneButton.setPrefSize(50, 50);
			removeOneButton.setGraphic(removeOneFromCartIconImageView);
			removeOneButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; "
					+ "-fx-border-width: 3px; -fx-border-radius: 100");
			///////////////////////////////////////////////////////////////////////

			j++;

			gridpaneIntoVbox.add(productName, j, i);
			GridPane.setHalignment(productName, HPos.CENTER);
			j++;

			gridpaneIntoVbox.add(quantityLabel, j, i);
			GridPane.setHalignment(quantityLabel, HPos.CENTER);
			j++;

			gridpaneIntoVbox.add(addButton, j, i);
			GridPane.setHalignment(addButton, HPos.RIGHT);
			j++;

			gridpaneIntoVbox.add(removeOneButton, j, i);
			GridPane.setHalignment(removeOneButton, HPos.LEFT);
			j++;

			gridpaneIntoVbox.add(removeButton, j, i);
			GridPane.setHalignment(removeButton, HPos.RIGHT);
			i++;
			j = 0;

			removeButton.setOnAction(action -> {
				System.out.println(currentProductID);
				System.out.println(_EKConfigurationProductController.productsInStockMap.get(currentProductID));

				_EKConfigurationProductController.productsInStockMap.put(currentProductID,
						_EKConfigurationProductController.productsInStockMap.get(currentProductID)
								+ OrderController.getCurrentUserCart().get(currentProductID));

				System.out.println("item" + product.getProductName() + " was removed");
				gridpaneIntoVbox.getChildren().remove(productName);
				gridpaneIntoVbox.getChildren().remove(quantityLabel);
				gridpaneIntoVbox.getChildren().remove(removeButton);
				gridpaneIntoVbox.getChildren().remove(addButton);
				gridpaneIntoVbox.getChildren().remove(removeOneButton);
				gridpaneIntoVbox.getChildren().remove(productImageView);

				_EKConfigurationProductController.itemsInCart -= OrderController.getCurrentUserCart()
						.get(currentProductID);
				OrderController.getCurrentUserCart().put(currentProductID, 0);
				calculatePriceToAdd(costPerUnit, OrderController.getCurrentUserCart().get(currentProductID), product);
				OrderController.getCartPrice().put(product, 0.0);
				calculateTotalPrice();
				txtTotalPrice
						.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0) / 2);

				if (_EKConfigurationProductController.itemsInCart == 0) {
					OrderController.getCurrentUserCart().keySet().clear();
					OrderController.getGetProductByID().keySet().clear();
					OrderController.getCartPrice().keySet().clear();
				}

			});

			addButton.setOnAction(action -> {
				_EKConfigurationProductController.itemsInCart++;
				OrderController.getCurrentUserCart().put(currentProductID,
						OrderController.getCurrentUserCart().get(currentProductID) + 1);
				quantityLabel.setText(
						"Quantity: " + (OrderController.getCurrentUserCart().get(currentProductID).toString()));
				calculatePriceToAdd(costPerUnit, OrderController.getCurrentUserCart().get(currentProductID), product);
				OrderController.getCartPrice().put(product, priceToAdd);
				calculateTotalPrice();
				txtTotalPrice
						.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0) / 2);

				// ROTEM ADDED URGENT 1.16:
				_EKConfigurationProductController.productsInStockMap.putIfAbsent(currentProductID, 0);
				_EKConfigurationProductController.productsInStockMap.put(currentProductID,
						_EKConfigurationProductController.productsInStockMap.get(currentProductID) - 1);

			});

			removeOneButton.setOnAction(action -> {
				// ROTEM ADDED URGENT 1.16:
				_EKConfigurationProductController.productsInStockMap.putIfAbsent(currentProductID, 0);
				_EKConfigurationProductController.productsInStockMap.put(currentProductID,
						_EKConfigurationProductController.productsInStockMap.get(currentProductID) + 1);

				_EKConfigurationProductController.itemsInCart--;
				OrderController.getCurrentUserCart().put(currentProductID,
						OrderController.getCurrentUserCart().get(currentProductID) - 1);
				quantityLabel.setText(
						"Quantity: " + (OrderController.getCurrentUserCart().get(currentProductID).toString()));
				calculatePriceToAdd(costPerUnit, OrderController.getCurrentUserCart().get(currentProductID), product);
				OrderController.getCartPrice().put(product, priceToAdd);
				calculateTotalPrice();
				txtTotalPrice
						.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0) / 2);
				if (OrderController.getCurrentUserCart().get(currentProductID) < 1) {
					System.out.println("item" + product.getProductName() + " was removed");
					gridpaneIntoVbox.getChildren().remove(productName);
					gridpaneIntoVbox.getChildren().remove(quantityLabel);
					gridpaneIntoVbox.getChildren().remove(removeButton);
					gridpaneIntoVbox.getChildren().remove(addButton);
					gridpaneIntoVbox.getChildren().remove(removeOneButton);
					gridpaneIntoVbox.getChildren().remove(productImageView);
				}
				// Max 7/1
				if (_EKConfigurationProductController.itemsInCart == 0) {
					OrderController.getCurrentUserCart().keySet().clear();
					OrderController.getGetProductByID().keySet().clear();
					OrderController.getCartPrice().keySet().clear();
				}

			});

			if (!OrderController.getCurrentUserCart().get(currentProductID).equals(0)) {
				OrderController.getArrayOfAddedProductsToGridpane().add(product);
				emptyCart = false;
			}

			if (OrderController.getCurrentUserCart().get(currentProductID).equals(0)) {
				OrderController.getCartPrice().put(product, 0.0);
				emptyCart = true;
				gridpaneIntoVbox.getChildren().remove(productName);
				gridpaneIntoVbox.getChildren().remove(quantityLabel);
				gridpaneIntoVbox.getChildren().remove(removeButton);
				gridpaneIntoVbox.getChildren().remove(addButton);
				gridpaneIntoVbox.getChildren().remove(removeOneButton);
				gridpaneIntoVbox.getChildren().remove(productImageView);

			}
			// Implement amount of items
		}

		OrderController.setOrderTotalPrice(totalPrice);
		System.out.println("total price = " + OrderController.getOrderTotalPrice());
		vboxCart.getChildren().add(gridpaneIntoVbox);
		ScrollPane scrollPane = new ScrollPane(vboxCart);

		scrollPane.prefHeight(600);
		scrollPane.prefWidth(800);
		scrollPane.getStylesheets().add("controllers/testCss.css");
		scrollPane.setStyle("-fx-background-color: transparent; "
				+ "-fx-background-color:   linear-gradient(from 000px 0px to 0px 1800px, #a837b4, transparent); -fx-border-color: transparent;");
		borderPane.setCenter(scrollPane);
	}

	/**
	 * 
	 * A method that handles the event when the user clicks the "Back" button. It
	 * returns the user to the product view and clears the cart.
	 * 
	 * @param event the ActionEvent object that triggered the method call
	 */
	@FXML
	public void getBtnBack(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		// category is located in a ArrayList
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/_EKConfigurationProductForm.fxml", null, OrderController.getCurrentProductCategory().get(0),
				true);
		vboxCart.getChildren().clear();
		primaryStage.show();

	}

	/**
	 * 
	 * A method that handles the event when the user clicks the "Cancel Order"
	 * button. It prompts the user to confirm the cancellation and clears the cart.
	 * 
	 * @param event the ActionEvent object that triggered the method call
	 */
	@FXML
	public void getBtnCancelOrder(ActionEvent event) {
		// Alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Clear Cart");
		alert.setHeaderText("This action will remove all items from the cart");
		alert.setContentText("Are you sure you want to continue?");
		alert.initStyle(StageStyle.UNDECORATED);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			System.out.println("Cleaning cart...");
			_EKConfigurationProductController.itemsInCart = 0;

			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			// category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
					"/gui/_EKConfigurationCustomerLocalOrderFrame.fxml", null,
					OrderController.getCurrentProductCategory().get(0), true);

			OrderController.getCurrentUserCart().keySet().clear();
			OrderController.getGetProductByID().keySet().clear();
			OrderController.getCartPrice().keySet().clear();
			primaryStage.show();
			//////////////////////
			((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
		} else if (result.get() == ButtonType.CANCEL) {
			System.out.println("Clear cart was canceled");
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationCartForm.fxml", null, "Ekt Cart",
					true);
			primaryStage.show();
			//////////////////////
			((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // hiding primary window
		}

	}

	/**
	 * A method that handles the event when the user clicks the "Order" button. It
	 * checks if the cart is empty and if it is, it prompts the user to add items to
	 * the cart.
	 *
	 * @param event the ActionEvent object that triggered the method call
	 */
	@FXML
	public void getBtnOrder(ActionEvent event) {
		if (emptyCart) {
			// Alert window
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Empty Cart");
			alert.setHeaderText("Your cart is empty, you cannot persist to order!");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				// Login window//
				Stage primaryStage = new Stage();
				// category is located in a ArrayList
				WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
						"/gui/_EKConfigurationProductForm.fxml", null,
						OrderController.getCurrentProductCategory().get(0), true);

				OrderController.getCurrentUserCart().keySet().clear();

				primaryStage.show();
				//////////////////////
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window

			}
		} else {

			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			// category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
					"/gui/_EKConfigurationOrderSummary.fxml", null, "Order Summary", true);
			vboxCart.getChildren().clear();
			OrderController.setDeliveryAddress(tf.getText());
			primaryStage.show();
		}

	}
}