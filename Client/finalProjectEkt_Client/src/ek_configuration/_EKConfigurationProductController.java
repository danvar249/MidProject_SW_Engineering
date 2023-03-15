
package ek_configuration;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import entityControllers.OrderController;
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

/**
 * This class contains methods that handle the button actions for navigating to
 * the previous and cart pages. â€¢ It also contains a method for checking and
 * setting the machine switched flag. â€¢ â€¢ @author RAZ â€¢
 */
public class _EKConfigurationProductController {

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

	private double salePromotionAmount = 1;

	/**
	 * This method sets the discount amount if any is active by querying the server
	 * for active promotions, checking the promotion status and discount percentage,
	 * and applying the discount to the salePromotionAmount variable.
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
						+ ClientController.getEKCurrentMachineID() + ";" });
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
		System.out.println("1 = " + promotionStatus);
		if (promotionStatus == true) {
			salePromotionAmount = 1 - (promotionDiscount / 100);
			System.out.println("2 = " + salePromotionAmount);
		}
		if (_EKConfigurationLoginFrameController.firstOrderForSubscriber()) {
			salePromotionAmount = salePromotionAmount * 0.8;
			System.out.println("3 = " + salePromotionAmount);
		}

	}

	private int gridPaneRow = 0;
	private String nextItemLocation = "left";
	////////////////////

	private static boolean machineSwitchedFlag = false;

	static HashMap<String, Integer> productsInStockMap = new HashMap<>();

	/**
	 * 
	 * This method initializes the display of products on the client side by
	 * creating a grid pane for the products and setting its size, gap, and padding.
	 * It also checks if the machine has been switched and if so, clears the order
	 * and products in stock. If the current system user is a subscriber, it also
	 * sets the discount amount by calling the setDiscountAmount method.
	 * 
	 */

	@SuppressWarnings("rawtypes")
	public void initialize() throws FileNotFoundException {
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

		GridPane gridPaneProducts = new GridPane();
		ColumnConstraints columnLeft = new ColumnConstraints();
		columnLeft.setPercentWidth(50);
		ColumnConstraints columnRight = new ColumnConstraints();
		columnRight.setPercentWidth(50);
		gridPaneProducts.getColumnConstraints().addAll(columnLeft, columnRight); // each gets 50% of width

		gridPaneProducts.setMaxSize(800 - 20, Region.USE_COMPUTED_SIZE);
		gridPaneProducts.setPrefSize(800 - 10, 600 - 4);
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
		// txtCategoryName.setTextFill(Color.WHITE);
		txtCategoryName.setLayoutX(400 - (txtCategoryName.minWidth(gridPaneRow)) / 2);

		SCCP testmsg = new SCCP();
		testmsg.setRequestType(ServerClientRequestTypes.SELECT);
		testmsg.setMessageSent(new Object[] {
				"product JOIN files ON product.productID = files.file_name + '.png'"
						+ "JOIN products_in_machine ON product.productID = products_in_machine.productID",
				true, "product.*, files.file_name, files.file, products_in_machine.stock", true,
				"(category = " + "'" + productCategory + "'" + "OR product.subCategory =" + "'" + productCategory + "')"
						+ " AND products_in_machine.machineID = " + ClientController.getEKCurrentMachineID(),
				false, null });

		ClientUI.getClientController().accept(testmsg);
		if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			System.out.println("I got it good");
			// Might want to check this suppression
			ArrayList<?> arrayOfProducts = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			System.out.println(arrayOfProducts);
			for (Object product : arrayOfProducts) {
				System.out.println(product);
				// Main product hbox
				HBox productHBox = new HBox();

				// ProductName + ProductID + ProductPrice
				VBox productDetails = new VBox();
				Text txtProductName = new Text();
				txtProductName.setText(((ArrayList) product).get(1).toString());
				txtProductName.setFont(new Font(18));
				txtProductName.setFill(Color.WHITE);
				txtProductName.setStyle("-fx-font: 20 System; -fx-font-weight: bold;");

				Text txtProductID = new Text();
				txtProductID.setText("");
				txtProductID.setFont(new Font(18));
				txtProductID.setFill(Color.WHITE);
				Text txtProductCostPerUnit = new Text();
				txtProductCostPerUnit.setText(((ArrayList) product).get(2).toString() + "$");
				txtProductCostPerUnit.setFont(new Font(18));
				txtProductCostPerUnit.setFill(Color.WHITE);

				txtProductID.setFill(Color.WHITE);
				txtProductCostPerUnit.setFill(Color.WHITE);
				txtProductName.setFill(Color.WHITE);

				productsInStockMap.putIfAbsent(((ArrayList<?>) product).get(0).toString(),
						Integer.valueOf(((ArrayList<?>) product).get(7).toString()));

				Text txtProductStock = new Text();
				// txtProductStock.setText("Stock: " + ((ArrayList)product).get(7).toString());
				txtProductStock.setText("Stock: " + productsInStockMap.get(((ArrayList<?>) product).get(0).toString()));
				txtProductStock.setFont(new Font(18));
				txtProductStock.setFill(Color.WHITE);

				/////// Dima 30/12 18:05//////////////////////////////////////
				productDetails.getChildren().add(txtProductName);
				// productDetails.getChildren().add(txtProductID);
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

				Image img = new Image(new ByteArrayInputStream((byte[]) ((ArrayList) product).get(6)));
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
				////////////////////////////////////////////

				productAddToCartVBox.getChildren().add(addToCartButton);
//				productAddToCartVBox.getChildren().add(amountOfItems);
				//////////////////////////////////////////////////////
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

				// pane.getChildren().add(productHBox);
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
						System.out.println("we reached lvl 0");
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
			scrollPane.maxHeight(600);
			scrollPane.prefWidth(800);
			////////////////// Dima 31/12 10:50 changed styling into this
			Border border = new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
			borderPane.setCenter(scrollPane);
			scrollPane.getStylesheets().add("controllers/testCss.css");
			scrollPane.setStyle(
					"-fx-background-color: transparent; -fx-background-color:    linear-gradient(from 0px 0px to 0px 1400px, #a837b4, transparent);"
							+ "-fx-border-color: transparent;");
			scrollPane.setBorder(border);

			//////////////////////////////////////////////////

			// linear-gradient(from 0px 0px to 0px 1500px, black, crimson);
		}
	}

	/**
	 * This method is triggered when the "Back" button is pressed. It hides the
	 * current window and opens the _EKConfigurationCustomerLocalOrderFrame window.
	 * 
	 * @param event The ActionEvent object that is passed when the button is
	 *              pressed.
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationCustomerLocalOrderFrame.fxml", null,
				"Ekt Catalog", true);

		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * This method is triggered when the "Cart" button is pressed. It hides the
	 * current window and opens the _EKConfigurationCartForm window.
	 * 
	 * @param event The ActionEvent object that is passed when the button is
	 *              pressed.
	 */
	@FXML
	public void getBtnCart(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/_EKConfigurationCartForm.fxml", null, "Ekt Cart", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	/**
	 * This method returns the machine switched flag.
	 * 
	 * @return a boolean indicating if the machine has been switched.
	 */
	public static boolean isMachineSwitchedFlag() {
		return machineSwitchedFlag;
	}

	/**
	 * This method sets the machine switched flag.
	 * 
	 * @param machineSwitchedFlag a boolean indicating if the machine has been
	 *                            switched.
	 */
	public static void setMachineSwitchedFlag(boolean machineSwitchedFlag) {
		_EKConfigurationProductController.machineSwitchedFlag = machineSwitchedFlag;
	}

}
