
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Role;
import logic.superProduct;

import java.io.FileNotFoundException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.ByteArrayInputStream;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import entityControllers.OrderController;

/**
 * 
 * The EktProductFormController class is a JavaFX controller class that handles
 * the UI and logic for a form that displays products and allows users to add
 * them to a cart. It contains several FXML-annotated variables for UI elements
 * such as text fields, labels, and buttons. The class also has several methods
 * that handle actions such as initializing the form, setting discounts, and
 * handling machine switches. There is also a static variable "itemsInCart" that
 * keeps track of the number of items in the cart and a HashMap
 * "productsInStockMap" that keeps track of the products in stock. The class
 * uses a client-server architecture, where it communicates with a server
 * through the ClientUI and ClientController classes to retrieve data and
 * perform actions.
 * 
 * @author Maxim, Rotem, Dima
 * @version 1.0
 *
 */
public class EktProductFormController {

	@FXML
	private Text txtCategoryName;

	@FXML
	private Pane topPane;

	@FXML
	private BorderPane borderPane;

	@FXML
	private Label lblProductName1;

	@FXML
	private Label lblProductCode1;

	@FXML
	private Label lblPrice1;

	@FXML
	private Button addItem1;

	@FXML
	private Label lblCount1;

	@FXML
	private Label lblProductName2;

	@FXML
	private Label lblProductCode2;

	@FXML
	private Label lblPrice2;

	@FXML
	private Button addItem2;

	@FXML
	private Label lblCount2;

	@FXML
	private Label lblProductName3;

	@FXML
	private Label lblProductCode3;

	@FXML
	private Label lblPrice3;

	@FXML
	private Button addItem3;

	@FXML
	private Label lblCount3;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnCart;

	@FXML
	private Label txtNumberOfItemsInCart;

	public static int itemsInCart = 0;

	///// Dima 30/12 17:15
	private int gridPaneRow = 0;
	private String nextItemLocation = "left";
	////////////////////

	private static boolean machineSwitchedFlag = false;

	// Rotem 1.13:
	public static HashMap<String, Integer> productsInStockMap = new HashMap<>();

	// Rotem ^^^

	private double salePromotionAmount = 1;

	/**
	 * The setDiscountAmount() method sets the discount amount for the products in
	 * the form, if an active promotion is found. It creates a new SCCP object and
	 * sets its request type to SELECT and messageSent to an object array containing
	 * the information to be retrieved from the server. It then calls the accept()
	 * method on the ClientUI.clientController object to send the request to the
	 * server. It retrieves the response from the server, which is an ArrayList
	 * containing the promotion status and promotion ID, and loops through it to
	 * find the first active promotion. If an active promotion is found, it sets the
	 * salePromotionAmount variable to the calculated discount amount.
	 */
	@SuppressWarnings("unchecked")
	private void setDiscountAmount() {
		
		// Set the discount amount if any is active
		SCCP getDiscountAmount = new SCCP();
		getDiscountAmount.setRequestType(ServerClientRequestTypes.SELECT);
		getDiscountAmount.setMessageSent(new Object[] { "promotions", true,
				"promotions.promotionStatus, promotions.discountPercentage", false, null, true,
				"JOIN locations ON promotions.locationID = locations.locationID \r\n"
						+ "JOIN machine ON promotions.locationID = machine.locationId" + " WHERE machine.machineId = "
						+ ClientController.getOLCurrentMachineID() + ";" });
		ClientUI.getClientController().accept(getDiscountAmount);

		ArrayList<?> promotionStatusAndPromotionID = (ArrayList<?>) ClientController.responseFromServer
				.getMessageSent();

		Boolean promotionStatus = false;
		double promotionDiscount = 0;
		for (ArrayList<Object> promotion : (ArrayList<ArrayList<Object>>) promotionStatusAndPromotionID) {
			promotionStatus = new Boolean((boolean) promotion.get(0));
			promotionDiscount = (float) promotion.get(1);
			// Get only the first active promotion
			if (promotionStatus == true)
				break;
		}
		if (promotionStatus == true) {
			salePromotionAmount = 1 - (promotionDiscount / 100);
		}
		if (EktSystemUserLoginController.firstOrderForSubscriber()) {
			salePromotionAmount = salePromotionAmount * 0.8;
		}

	}

	/**
	 * The initialize() method is a built-in method in JavaFX controllers that is
	 * automatically called when the form is loaded. It first checks if the current
	 * user is a subscriber, and if so, calls the setDiscountAmount() method to set
	 * the discount for the products. It then checks if a machine switch has
	 * occurred, and if so, it clears the productsInStockMap, currentUserCart,
	 * getProductByID and sets itemsInCart to 0, and resets the machineSwitchedFlag.
	 * It then initializes a GridPane for displaying the products and sets its
	 * properties such as size, padding, and alignment. It sets the text of the
	 * txtNumberOfItemsInCart label to the current number of items in the cart and
	 * sets its style accordingly. It sets the text of the txtCategoryName label to
	 * the current product category and sets the layout of the label. It then
	 * creates a SCCP object and sets its request type and messageSent properties to
	 * retrieve information from the server about the products in the current
	 * category and machine. It calls the accept() method on the
	 * ClientUI.clientController object to send the request to the server, and
	 * retrieve the information to be displayed in the form.
	 * 
	 * @throws FileNotFoundException
	 */
	public void initialize()  {
		if (ClientController.getCurrentSystemUser().getRole().equals(Role.SUBSCRIBER)) {
			setDiscountAmount();
		}
			
		// if we switch machines - clear the order and so on [please test this! I only
		// Rotem-tested it]
		if (isMachineSwitchedFlag()) {
			productsInStockMap = new HashMap<>();
			OrderController.setCurrentUserCart(new HashMap<>());
			OrderController.setGetProductByID(new HashMap<>());
			itemsInCart = 0;
			// don't forget to release it
			setMachineSwitchedFlag(false);
		}
		///////////// Dima 31/12 10:00 ////////////
		GridPane gridPaneProducts = new GridPane();
		ColumnConstraints columnLeft = new ColumnConstraints();
		columnLeft.setPercentWidth(50);
		ColumnConstraints columnRight = new ColumnConstraints();
		columnRight.setPercentWidth(50);
		gridPaneProducts.getColumnConstraints().addAll(columnLeft, columnRight); // each gets 50% of width

		gridPaneProducts.setMaxSize(800 - 20, Region.USE_COMPUTED_SIZE);
		gridPaneProducts.setPrefSize(800 -3, 600 - 4);
		gridPaneProducts.setHgap(5);
		;
		gridPaneProducts.setVgap(5);
		;
		gridPaneProducts.setPadding(new Insets(5, 0, 0, 6));

		gridPaneProducts.setAlignment(Pos.TOP_CENTER);

		///////////////////////////////////////////

		String itemsInCartString = itemsInCart + "";
		txtNumberOfItemsInCart.setText(itemsInCartString);
		if (itemsInCart == 0)
			txtNumberOfItemsInCart
					.setStyle("-fx-background-color: #a7e8f2; -fx-opacity:  0.75; -fx-background-radius: 10;");

		else
			txtNumberOfItemsInCart
					.setStyle("-fx-background-color: #da8888; -fx-background-radius: 10; -fx-opacity: 0.75;");
		// Maxim new: added cart image
		ImageView cartImg = new ImageView(new Image("controllers/Images/cart.png"));
		cartImg.setFitHeight(50);
		cartImg.setFitWidth(50);
		cartImg.setPreserveRatio(true);
		btnCart.setGraphic(cartImg);

		String productCategory = OrderController.getCurrentProductCategory().get(0);
		txtCategoryName.setText(productCategory);
		txtCategoryName.setLayoutX(400 - (txtCategoryName.minWidth(gridPaneRow)) / 2);

		SCCP testmsg = new SCCP();
		testmsg.setRequestType(ServerClientRequestTypes.SELECT);
		testmsg.setMessageSent(new Object[] {
				"product JOIN files ON product.productID = files.file_name + '.png'"
						+ "JOIN products_in_machine ON product.productID = products_in_machine.productID",
				true, "product.*, files.file_name, files.file, products_in_machine.stock", true,
				"(category = " + "'" + productCategory + "'" + "OR product.subCategory =" + "'" + productCategory + "')"
						+ " AND products_in_machine.machineID = " + ClientController.getOLCurrentMachineID(),
				false, null });

		ClientUI.getClientController().accept(testmsg);

		if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			// Might want to check this suppression
			ArrayList<?> arrayOfProducts = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			for (Object product : arrayOfProducts) {
				// Main product hbox
				HBox productHBox = new HBox();

				// ProductName + ProductID + ProductPrice
				VBox productDetails = new VBox();
				Text txtProductName = new Text();
				txtProductName.setText(((ArrayList<?>) product).get(1).toString());
				txtProductName.setFont(new Font(18));
				txtProductName.setFill(Color.BLACK);
				txtProductName.setStyle("-fx-font: 20 System; -fx-font-weight: bold;");

				Text txtProductID = new Text();
				txtProductID.setText("");
				txtProductID.setFont(new Font(18));
				txtProductID.setFill(Color.BLACK);
				Text txtProductCostPerUnit = new Text();
				txtProductCostPerUnit.setText(((ArrayList<?>) product).get(2).toString() + "$");
				txtProductCostPerUnit.setFont(new Font(18));
				txtProductCostPerUnit.setFill(Color.BLACK);
				
				txtProductID.setFill(Color.WHITE);
				txtProductCostPerUnit.setFill(Color.WHITE);
				txtProductName.setFill(Color.WHITE);

				/*
				 * Rotem - insert stock to map (for each product in machine) Only inserts if
				 * nothing inside (like Shimon's mom)
				 */
				productsInStockMap.putIfAbsent(((ArrayList<?>) product).get(0).toString(),
						Integer.valueOf(((ArrayList<?>) product).get(7).toString()));

				/*
				 * Rotem ------
				 */

				Text txtProductStock = new Text();
				// txtProductStock.setText("Stock: " + ((ArrayList)product).get(7).toString());
				txtProductStock.setText("Stock: " + productsInStockMap.get(((ArrayList<?>) product).get(0).toString()));
				txtProductStock.setFont(new Font(18));
				txtProductStock.setFill(Color.WHITE);

				/////// Dima 30/12 18:05//////////////////////////////////////
				productDetails.getChildren().add(txtProductName);
				productDetails.getChildren().add(txtProductStock);
				// Implement item on sale
				// if(ClientController.getcurrentCustomer == Subscriber AND product == ON-SALE
				// -> display item on sale
				if (salePromotionAmount < 1) {
					// This is just an example
					txtProductCostPerUnit.setStrikethrough(true);
					Text txtSubscriberSale = new Text();
					Double priceAfterSale = new Double(((ArrayList<?>) product).get(2).toString());
					txtSubscriberSale.setText("ON SALE: "
							+ (new DecimalFormat("##.##").format((priceAfterSale * salePromotionAmount))).toString()
							+ "$");
					txtSubscriberSale.setFill(Color.CRIMSON);
					txtSubscriberSale.setFont(new Font(18));
					txtSubscriberSale.setStyle("-fx-font: 20 System; -fx-font-weight: bold;");

					productDetails.getChildren().add(txtProductCostPerUnit);

					productDetails.getChildren().add(txtSubscriberSale);
				} else {
					productDetails.getChildren().add(txtProductCostPerUnit);
				}
				// if(itemOnSale == true) - > add this text:
				//////////////////////////////////////////////////////////////

				productDetails.setAlignment(Pos.CENTER);

				Image img = new Image(new ByteArrayInputStream((byte[]) ((ArrayList<?>) product).get(6)));
				ImageView productImageView = new ImageView(img);
				productImageView.setFitHeight(150);
				productImageView.setFitWidth(150);
				productHBox.getChildren().add(productImageView);

				// AddToCart Button + amountTxt
				VBox productAddToCartVBox = new VBox();

				//////// Dima 30/12 17:29////////////////////
				Button addToCartButton = new Button();
				ImageView addToCartImageView = new ImageView(new Image("controllers/Images/addToCartIcon.png"));
				addToCartImageView.setFitHeight(50);
				addToCartImageView.setFitWidth(45);
				addToCartButton.setPrefSize(50, 50);
				addToCartButton.setGraphic(addToCartImageView);
				addToCartButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; "
						+ "-fx-border-width: 3px; -fx-border-radius: 100");

				productAddToCartVBox.getChildren().add(addToCartButton);
				productAddToCartVBox.setAlignment(Pos.CENTER_RIGHT);
				productHBox.setAlignment(Pos.CENTER);
				productHBox.setPrefSize(400, 150);
				productHBox.minHeight(150);
				productDetails.setPrefSize(150, 150);

				productHBox.getChildren().add(productDetails);
				productHBox.getChildren().add(productAddToCartVBox);

				BorderPane pane = new BorderPane();
				pane.minHeight(170);
				pane.setStyle("-fx-border-color: black; -fx-border-width: 3px; -fx-border-radius: 10;"
						+ " -fx-background-color:  linear-gradient(from 0px 0px to 0px 400px, #a837b4, transparent); -fx-background-radius: 12");

				pane.setCenter(productHBox);
				DropShadow paneShadow = new DropShadow();
				paneShadow.setColor(Color.YELLOW);
				paneShadow.setRadius(1);
				paneShadow.setSpread(0.001);
				pane.setEffect(paneShadow);

				addToCartButton.setOnAction(event -> {
					/*
					 * Rotem -> Added grab from hash map here:
					 */
					int newStock = productsInStockMap.get(((ArrayList<?>) product).get(0).toString()) - 1;
					txtProductStock.setText("Stock: " + newStock);
					productsInStockMap.put(((ArrayList<?>) product).get(0).toString(), newStock);
					if (newStock == 0) {
						addToCartButton.setDisable(true);
					}

					itemsInCart++;
					if (itemsInCart == 1) {
						String itemsInCartStr = itemsInCart + "";
						txtNumberOfItemsInCart.setText(itemsInCartStr);
						txtNumberOfItemsInCart.setStyle(
								"-fx-background-color: #da8888; -fx-background-radius: 10; -fx-opacity: 0.75;");
					} else {
						String itemsInCartStr = itemsInCart + "";
						txtNumberOfItemsInCart.setText(itemsInCartStr);
					}
					// Increment value of the product key in the hash map
					// If it does not exist, set value to "1"
					String currentProductID = ((ArrayList<?>) product).get(0).toString();

					if (!OrderController.getGetProductByID().containsKey(currentProductID)) {
						double pricePerProductBeforeDiscount = new Double(((ArrayList<?>) product).get(2).toString());
						Double priceAfterDiscount = new Double(pricePerProductBeforeDiscount * salePromotionAmount);
						superProduct p = new superProduct(((ArrayList<?>) product).get(0).toString(),
								((ArrayList<?>) product).get(1).toString(), priceAfterDiscount.toString(),
								((ArrayList<?>) product).get(3).toString(), (""),
								((ArrayList<?>) product).get(5).toString(), (byte[]) ((ArrayList<?>) product).get(6));
						OrderController.getGetProductByID().put(currentProductID, p);
					}
					OrderController.getCurrentUserCart().merge(currentProductID, 1, Integer::sum);
					// Animate add to cart
				});

				///////// Dima 30/12 17:24 ///////////////////////
				if (nextItemLocation.equals("left")) {
					gridPaneProducts.add(pane, 0, gridPaneRow);
					nextItemLocation = "right";
				} else {
					gridPaneProducts.add(pane, 1, gridPaneRow);
					gridPaneRow++;
					nextItemLocation = "left";
				}

				if (productsInStockMap.get(((ArrayList<?>) product).get(0).toString()) == 0) {
					addToCartButton.setDisable(true);////////////////////////////////////////////////////////////////////////
				}
			}

			ScrollPane scrollPane = new ScrollPane(gridPaneProducts);
			scrollPane.maxHeight(570);
			scrollPane.prefWidth(800);
			////////////////// Dima 31/12 10:50 changed styling into this
			Border border = new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
			borderPane.setCenter(scrollPane);
			scrollPane.getStylesheets().add("controllers/testCss.css");
			scrollPane.setStyle(
					"-fx-background-color: transparent; -fx-background-color: linear-gradient(from 0px 0px to 0px 1400px, #a837b4, transparent);"
							+ "-fx-border-color: transparent;");
			scrollPane.setBorder(border);
		}
	}

	/**
	 * 
	 * The getBtnBack() method is an event handler for the "btnBack" button, that is
	 * activated when the button is clicked. It hides the current window, creates a
	 * new stage and opens the EktCatalogForm.fxml window on it.
	 * 
	 * @param event an ActionEvent representing the button click.
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCatalogForm.fxml", null, "Ekt Catalog", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * 
	 * The getBtnCart() method is an event handler for the "btnCart" button, that is
	 * activated when the button is clicked. It hides the current window, creates a
	 * new stage and opens the EktCartForm.fxml window on it.
	 * 
	 * @param event an ActionEvent representing the button click.
	 */
	@FXML
	public void getBtnCart(ActionEvent event) {
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktCartForm.fxml", null, "Ekt Cart", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * 
	 * The isMachineSwitchedFlag() method is a getter method that returns the
	 * current value of the static boolean variable 'machineSwitchedFlag'.
	 * 
	 * @return boolean value of machineSwitchedFlag
	 */
	public static boolean isMachineSwitchedFlag() {
		return machineSwitchedFlag;
	}

	/**
	 * 
	 * The setMachineSwitchedFlag() method is a setter method that sets the value of
	 * the static boolean variable 'machineSwitchedFlag' to the value passed as a
	 * parameter.
	 * 
	 * @param machineSwitchedFlag a boolean value that the static variable
	 *                            'machineSwitchedFlag' will be set to.
	 */
	public static void setMachineSwitchedFlag(boolean machineSwitchedFlag) {
		EktProductFormController.machineSwitchedFlag = machineSwitchedFlag;
	}

}
