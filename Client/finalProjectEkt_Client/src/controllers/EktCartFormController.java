package controllers;

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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Product;
import logic.superProduct;
/**

EktCartFormController is a class that manages the cart form that displays the products the user has added to their cart,
and allows the user to finalize their order.
@author Dima
*/
public class EktCartFormController {
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private Button btnCancelOrder;

    @FXML
    private Text txtTotalPrice;
    
    @FXML
    private ChoiceBox<String> choiceBox;
    
    @FXML
    private Pane btmPane;
	
	private GridPane gridpaneIntoVbox;
	
	private boolean emptyCart = true;
		
	private VBox vboxCart;
	
	private Double priceToAdd = 0.0;
	
	private Double totalPrice = 0.0;
	
	private String deliveryAddress = "";
	
	private TextField tf = new TextField();
	/**
	 * A method that calculates the price of a product being added to the cart.
	 * 
	 * @param costPerUnit the cost per unit of the product
	 * @param quantityNum the number of units of the product being added to the cart
	 * @param product the product being added to the cart
	 */
	private void calculatePriceToAdd(Double costPerUnit, Integer quantityNum, Product product) {
		
		quantityNum = OrderController.getCurrentUserCart().get(product.getProductID());
		costPerUnit = Double.valueOf(product.getCostPerUnit());
		priceToAdd = quantityNum * costPerUnit;

	}
	/**
	 * A method that calculates the total price of all products in the cart.
	 */
	private void calculateTotalPrice() {
		totalPrice = 0.0;
		for (Product product: OrderController.getCartPrice().keySet()) {
			System.out.println("Adding the price of " + product.getProductName()+ "in");
			totalPrice += OrderController.getCartPrice().get(product);
		}
		if(totalPrice == 0.0) {
			emptyCart = true;
			System.out.println("The cart is empty right now!");
		}
	}

	/**
	 * A method that is automatically called by JavaFX when the cart form is initialized. It sets the initial state of the form 
	 * and sets the values of the ChoiceBox object.
	 */
	@FXML
	public void initialize() {
		OrderController.setOrderType("");
		OrderController.setPickupPlace("");
		
		vboxCart = new VBox();
		gridpaneIntoVbox  = new GridPane();
		
		txtTotalPrice.setText("CART TOTAL: 0$");
		txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
		
		
		final int numCols = 6;
		Double costPerUnit = 0.0;

		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800/6);
			gridpaneIntoVbox.getColumnConstraints().add(colConst);
		}	
		gridpaneIntoVbox.setMaxSize(800-20, Region.USE_COMPUTED_SIZE);
		gridpaneIntoVbox.setPrefSize(800 - 10, 550);
		gridpaneIntoVbox.setHgap(5);;
		gridpaneIntoVbox.setVgap(5);;
		
		choiceBox.getItems().addAll("Pickup","Delivery");
		ComboBox<String> cb = new ComboBox<>();
		cb.getStylesheets().add("/gui/comboboxCSS.css");
		Text t = new Text();
		choiceBox.setOnAction(event ->{
			if(choiceBox.getValue().equals("Pickup")) {
				System.out.println("Client Order Type is = " + choiceBox.getValue());
				OrderController.setOrderType(choiceBox.getValue());
				btmPane.getChildren().removeAll(tf,t);

				cb.getItems().setAll("Haifa, Downtown","Beer Sheva, Center","Beer Sheva, Downtown",
						"Kiryat Motzkin, Center", "Kiryat Shmona, Center", "Beer Sheva, Updog", "Abu Dabi, Center",
						"Abu Naji, Center");
				cb.setLayoutX(509);
				cb.setLayoutY(6);
				
				btmPane.getChildren().add(cb);
				System.out.println("pickup place = " + cb.getValue());
				
			}
			else if(choiceBox.getValue().equals("Delivery")) {
				OrderController.setOrderType(choiceBox.getValue());
				btmPane.getChildren().remove(cb);
				tf.setLayoutX(509);
				tf.setLayoutY(22);
				tf.getStylesheets().add("/gui/textfieldCSS.css");
				t.setLayoutX(509);
				t.setLayoutY(13);
				tf.prefWidth(300);
				tf.setPromptText("Please Insert Address");
				t.setText("Please Insert Address For Delivery");
				t.setFont(Font.font("berlin sans fb", FontWeight.NORMAL, 16));
				btmPane.getChildren().addAll(tf,t);
				System.out.println("Client Order Type is = " + choiceBox.getValue());
			}
		});
		
		cb.setOnAction(event ->{OrderController.setPickupPlace(cb.getValue());});
		
		
		System.out.println("deliveryAddress = " + deliveryAddress);
		int i = 0, j = 0;
		for (superProduct product: OrderController.getGetProductByID().values()) {
			String currentProductID = product.getProductID();
			calculatePriceToAdd(costPerUnit, OrderController.getCurrentUserCart().get(currentProductID), product);
			OrderController.getCartPrice().put(product,priceToAdd);
			calculateTotalPrice();
			txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
			txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
		
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
			
			productName.setStyle("-fx-font: 18 System; -fx-font-weight: bold;");
			productName.setFont(new Font(18));
			quantityLabel.setFont(new Font(18));
			
			Button removeButton = new Button();
			Button addButton = new Button();
			Button removeOneButton = new Button();
			removeButton.setFont(new Font(18));
			addButton.setFont(new Font(18));
			removeOneButton.setFont(new Font(18));
			removeButton.setPrefSize(50, 50);
			removeButton.setGraphic(removeItemIconImageView);
			removeButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; "
					+ "-fx-border-width: 2px; -fx-border-radius: 100");
			
			addButton.setPrefSize(50, 50);
			addButton.setGraphic(addOneToCartIconImageView);
			addButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; "
					+ "-fx-border-width: 2px; -fx-border-radius: 100");
			
			removeOneButton.setPrefSize(50, 50);
			removeOneButton.setGraphic(removeOneFromCartIconImageView);
			removeOneButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; "
					+ "-fx-border-width: 2px; -fx-border-radius: 100");
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
			GridPane.setHalignment(removeButton, HPos.LEFT);
			i++; j = 0;
			
			
			removeButton.setOnAction(action -> {
				EktProductFormController.productsInStockMap.put(currentProductID, 
						EktProductFormController.productsInStockMap.get(currentProductID) +
						OrderController.getCurrentUserCart().get(currentProductID));
				
				System.out.println("item" + product.getProductName() + " was removed");
				gridpaneIntoVbox.getChildren().remove(productName);
				gridpaneIntoVbox.getChildren().remove(quantityLabel);
				gridpaneIntoVbox.getChildren().remove(removeButton);
				gridpaneIntoVbox.getChildren().remove(addButton);
				gridpaneIntoVbox.getChildren().remove(removeOneButton);
				gridpaneIntoVbox.getChildren().remove(productImageView);

				//removeProduct = true;
				EktProductFormController.itemsInCart -= OrderController.getCurrentUserCart().get(currentProductID);
				OrderController.getCurrentUserCart().put(currentProductID, 0);
				calculatePriceToAdd(costPerUnit, OrderController.getCurrentUserCart().get(currentProductID), product);
				OrderController.getCartPrice().put(product, 0.0);
				calculateTotalPrice();
				txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
				
				if(EktProductFormController.itemsInCart == 0){
					OrderController.getCurrentUserCart().keySet().clear();
					OrderController.getGetProductByID().keySet().clear();
					OrderController.getCartPrice().keySet().clear();
				}
				


			});
			

			addButton.setOnAction(action -> {
				EktProductFormController.itemsInCart++;
				OrderController.getCurrentUserCart().put(currentProductID, OrderController.getCurrentUserCart().get(currentProductID) + 1);
				quantityLabel.setText("Quantity: " + (OrderController.getCurrentUserCart().get(currentProductID).toString()));
				calculatePriceToAdd(costPerUnit, OrderController.getCurrentUserCart().get(currentProductID), product);
				OrderController.getCartPrice().put(product, priceToAdd);
				calculateTotalPrice();
				txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
				
				// ROTEM ADDED URGENT 1.16:
				EktProductFormController.productsInStockMap.putIfAbsent(currentProductID, 0);
				EktProductFormController.productsInStockMap.put(currentProductID, 
						EktProductFormController.productsInStockMap.get(currentProductID) - 1);
			});
			

			removeOneButton.setOnAction(action -> {
				EktProductFormController.itemsInCart--;
				OrderController.getCurrentUserCart().put(currentProductID, OrderController.getCurrentUserCart().get(currentProductID) - 1);
				quantityLabel.setText("Quantity: " + (OrderController.getCurrentUserCart().get(currentProductID).toString()));
				calculatePriceToAdd(costPerUnit, OrderController.getCurrentUserCart().get(currentProductID), product);
				OrderController.getCartPrice().put(product, priceToAdd);
				calculateTotalPrice();
				txtTotalPrice.setText("Cart Total: " + (new DecimalFormat("##.##").format(totalPrice)).toString() + "$");
				txtTotalPrice.setLayoutX(400 - txtTotalPrice.minWidth(0)/2);
				if (OrderController.getCurrentUserCart().get(currentProductID) < 1) {
					System.out.println("item" + product.getProductName() + " was removed");
					gridpaneIntoVbox.getChildren().remove(productName);
					gridpaneIntoVbox.getChildren().remove(quantityLabel);
					gridpaneIntoVbox.getChildren().remove(removeButton);
					gridpaneIntoVbox.getChildren().remove(addButton);
					gridpaneIntoVbox.getChildren().remove(removeOneButton);
					gridpaneIntoVbox.getChildren().remove(productImageView);
				}
				if(EktProductFormController.itemsInCart == 0){
					OrderController.getCurrentUserCart().keySet().clear();
					OrderController.getGetProductByID().keySet().clear();
					OrderController.getCartPrice().keySet().clear();
				}
				
				EktProductFormController.productsInStockMap.putIfAbsent(currentProductID, 0);
				EktProductFormController.productsInStockMap.put(currentProductID, 
				EktProductFormController.productsInStockMap.get(currentProductID) + 1);

			});
			

			if(!OrderController.getCurrentUserCart().get(currentProductID).equals(0)) {
				OrderController.getArrayOfAddedProductsToGridpane().add(product);
				emptyCart = false;
			}
			
			if(OrderController.getCurrentUserCart().get(currentProductID).equals(0)) {
				OrderController.getCartPrice().put(product, 0.0);
				emptyCart = true;
				gridpaneIntoVbox.getChildren().remove(productName);
				gridpaneIntoVbox.getChildren().remove(quantityLabel);
				gridpaneIntoVbox.getChildren().remove(removeButton);
				gridpaneIntoVbox.getChildren().remove(addButton);
				gridpaneIntoVbox.getChildren().remove(removeOneButton);
				gridpaneIntoVbox.getChildren().remove(productImageView);
				
			}
			//Implement amount of items
		}

		OrderController.setOrderTotalPrice(totalPrice);
		System.out.println("total price = " + OrderController.getOrderTotalPrice());
		vboxCart.getChildren().add(gridpaneIntoVbox);
		ScrollPane scrollPane = new ScrollPane(vboxCart);
		
		scrollPane.prefHeight(600);
		scrollPane.prefWidth(800);
		scrollPane.getStylesheets().add("controllers/testCss.css");
		scrollPane.setStyle("-fx-background-color:  linear-gradient(from -200px 0px to 0px 1800px, #a837b4, transparent); -fx-border-color: transparent;");
		
		borderPane.setCenter(scrollPane);
	}
	/**
	 * A method that handles the "Back" button event. It closes the current window and opens the EktProductForm window.
	 * It also clears the items in the cart.
	 * 
	 * @param event the ActionEvent object that is triggered when the "Back" button is pressed
	 */
	@FXML
	public void getBtnBack(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		//category is located in a ArrayList
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktProductForm.fxml", null, OrderController.getCurrentProductCategory().get(0), true);
		vboxCart.getChildren().clear();
		primaryStage.show();
		
		
	}
	
	/**
	 * A method that handles the "Cancel Order" button event. It displays an Alert window that prompts the user to confirm 
	 * that they want to cancel their order. If the user confirms, it clears the items in the cart, closes the current window, 
	 * and opens the EktCatalogForm window. If the user cancels, it closes the Alert window and returns to the cart form.
	 * 
	 * @param event the ActionEvent object that is triggered when the "Cancel Order" button is pressed
	 */
	@FXML
	public void getBtnCancelOrder(ActionEvent event) {
		//Alert window
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Clear Cart");
		alert.setHeaderText("This action will remove all items from the cart");
		alert.setContentText("Are you sure you want to continue?");
		alert.initStyle(StageStyle.UNDECORATED);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			System.out.println("Cleaning cart...");
			EktProductFormController.itemsInCart = 0;
			
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			//category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktCatalogForm.fxml", null, 
					OrderController.getCurrentProductCategory().get(0), true);

			OrderController.getCurrentUserCart().keySet().clear();
			OrderController.getGetProductByID().keySet().clear();
			OrderController.getCartPrice().keySet().clear();
			primaryStage.show();
			//////////////////////
			((Stage) ((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
		}
		else if(result.get() == ButtonType.CANCEL) {
			System.out.println("Clear cart was canceled");
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
	        WindowStarter.createWindow(primaryStage, this, "/gui/EktCartForm.fxml", null, "Ekt Cart", true);
			primaryStage.show();
			//////////////////////
			((Stage) ((Node)event.getSource()).getScene().getWindow()).close(); //hiding primary window
		}
		
	}
	/**
	 * A method that handles the "Order" button event. 
	 * It retrieves the delivery address from the TextField and checks if the cart is empty or not.
	 * If the cart is empty, it shows an Alert window that the cart is empty and return to the EktProductForm.
	 * If the cart is not empty, the order is confirmed and the EktCatalogForm is displayed with the cart cleared.
	 * 
	 * @param event the ActionEvent object that is triggered when the "Order" button is pressed
	 */
	@FXML
	public void getBtnOrder(ActionEvent event){
		deliveryAddress = tf.getText();
		if(emptyCart) {
    		//Alert window
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Empty Cart");
    		alert.setHeaderText("Your cart is empty, you cannot persist to order!");
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		if (result.get() == ButtonType.OK) {
    			//Login window//
    			Stage primaryStage = new Stage();
    			//category is located in a ArrayList
    			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktProductForm.fxml", null, 
    					OrderController.getCurrentProductCategory().get(0), true);
    	
    			OrderController.getCurrentUserCart().keySet().clear();
    			
    			primaryStage.show();
    			//////////////////////
    			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window

    		}
    	}
		else if(OrderController.getOrderType().equals("") || 
				(OrderController.getOrderType().equals("Pickup") && OrderController.getPickupPlace().equals("") ||
						(OrderController.getOrderType().equals("Delivery") && deliveryAddress.equals("")))) {
			//Alert window
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Select Order Type");
    		alert.setHeaderText("You need to select Order Type And Pickup/Address before proceeding to order!");
    		alert.initStyle(StageStyle.UNDECORATED);
    		alert.showAndWait();
    		
		}
		else {
			
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			//category is located in a ArrayList
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktOrderSummary.fxml", null, "Order Summary", true);
			vboxCart.getChildren().clear();
			OrderController.setDeliveryAddress(deliveryAddress);
			primaryStage.show();
		}

	}
}