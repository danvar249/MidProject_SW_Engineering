package ek_configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Location;
import logic.Machine;
import logic.ProductInMachine;

/**
 * 
 * InventoryRestockWorkerPageController is a JavaFX controller class for the
 * Inventory Restock Worker page. It contains the logic for displaying and
 * updating inventory information for a specific machine.
 * 
 * @author Daniel Vardimon
 * @version 1.0
 */
public class _EKInventoryRestockWorkerController {
	/**
	 * Inner class representing a row in the TableView of inventory information. It
	 * has properties for the item name, item stock, and stock to add.
	 */
	public class InventoryTableData {
		/*
		 * Constructor for InventoryTableData
		 * 
		 * @param item ProductInMachine object representing the item
		 */
		public InventoryTableData(ProductInMachine item) {
			this.item = item;
			setItemName(item.getProduct().getProductName());
			setItemStock(item.getStock());
			setStockToAdd(0);
		}

		/**
		 * The ProductInMachine object representing the item
		 */
		private final ProductInMachine item;

		/**
		 * Getter for the ProductInMachine object
		 * 
		 * @return ProductInMachine object representing the item
		 */
		public final ProductInMachine getItem() {
			return item;
		}

		/**
		 * Property for the item name
		 */
		private final StringProperty itemName = new SimpleStringProperty(this, "itemName");

		/**
		 * Getter for the item name
		 * 
		 * @return String representing the item name
		 */
		public final String getItemName() {
			return itemName.get();
		}

		/**
		 * Setter for the item name
		 * 
		 * @param itemName the new item name
		 */
		public final void setItemName(String itemName) {
			this.itemName.set(itemName);
		}

		/**
		 * Property for the item name
		 * 
		 * @return StringProperty representing the item name
		 */
		public final StringProperty itemNameProperty() {
			return itemName;
		}

		/**
		 * Property for the item stock
		 */
		private final IntegerProperty itemStock = new SimpleIntegerProperty(this, "itemStock");

		/**
		 * Getter for the item stock
		 * 
		 * @return int representing the item stock
		 */
		public final int getItemStock() {
			return itemStock.get();
		}

		/**
		 * Setter for the item stock
		 * 
		 * @param itemStock the new item stock
		 */
		public final void setItemStock(int itemStock) {
			this.item.setStock(itemStock);
			this.itemStock.set(itemStock);
		}

		/**
		 * Property for the item stock
		 * 
		 * @return IntegerProperty representing the item stock
		 */
		public final IntegerProperty itemStockProperty() {
			return itemStock;
		}

		/**
		 * Property for the stock to add
		 */
		private final StringProperty itemStockToAdd = new SimpleStringProperty(this, "itemStockToAdd");

		/**
		 * Property for the stock to add
		 * 
		 * @return StringProperty representing the stock to add
		 */
		public final StringProperty itemStockToAddProperty() {
			return itemStockToAdd;
		}

		/**
		 * Getter for the stock to add
		 * 
		 * @return int representing the stock to add
		 */
		public final int getItemStockToAdd() {
			return Integer.parseInt(itemStockToAdd.get());
		}

		/**
		 * Setter for the stock to add
		 * 
		 * @param stockToAdd the new stock to add
		 */
		public final void setStockToAdd(int stockToAdd) {
			itemStockToAdd.set(String.valueOf(stockToAdd));
		}

	}

	/**
	 * HashMap that maps each machine to an ArrayList of its corresponding
	 * InventoryTableData objects
	 */
	private HashMap<Machine, ArrayList<InventoryTableData>> machineDataMap = new HashMap<>();
	/**
	 * Text field for displaying a welcome message for the user
	 */
	@FXML
	private Text WelcomeInventoryWorkerText;
	/**
	 * TableView for displaying the inventory information in a table format
	 */
	@FXML
	private TableView<InventoryTableData> tblInventory;
	/**
	 * TableColumn for displaying the item name in the table
	 */
	@FXML
	private TableColumn<InventoryTableData, String> colItem;
	/**
	 * TableColumn for displaying the item stock in the table
	 */
	@FXML
	private TableColumn<InventoryTableData, Number> colQuantity;
	/**
	 * TableColumn for displaying the stock to add in the table
	 */
	@FXML
	private TableColumn<InventoryTableData, String> colRestockAmount;
	/**
	 * ComboBox for selecting the machine to view the inventory information of
	 */
	/**
	 * Button for updating the inventory information
	 */
	@FXML
	private Button btnUpdate;
	/**
	 * Button for navigating back to the previous page
	 */
	@FXML
	private Button btnBack;

	/**
	 * Closes the current window and opens the previous window.
	 * 
	 * @param event ActionEvent for the button click
	 */
	@FXML
	void getBtnLogout(ActionEvent event) {
		Stage primaryStage = new Stage();
		ClientController.sendLogoutRequest();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null,
				"Ekt Login", true);
		// this was done so that we can use this button
		primaryStage.setOnCloseRequest(we -> extracted());

		primaryStage.show();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	}

	private void extracted() {
		System.out.println("Pressed the X button.");
		System.exit(0);
	}

	/**
	 * Updates the inventory information for the selected machine by sending a
	 * request to the server and updating the TableView with the new information.
	 * 
	 * @param event ActionEvent for the button click
	 */
	@FXML
	void getBtnUpdate(ActionEvent event) {
		ArrayList<ProductInMachine> productsToUpdate = new ArrayList<>();
		// iterate over the machines that were accessed
		machineDataMap.forEach((machine, data) -> {
			for (InventoryTableData item : data) {
				int stockToAdd = item.getItemStockToAdd();
				/*
				 * if stock to add is more than 0, we set the updated stock to the object and
				 * add it to the list.
				 */
				if (stockToAdd > 0) {
					item.setItemStock(item.getItemStock() + stockToAdd);
					item.setStockToAdd(0);
					ProductInMachine product = item.getItem();
					item.getItem().setRestockFlag(product.getStock() < product.getMinStock());
					productsToUpdate.add(item.getItem());
				}
			}
		});
		if (productsToUpdate.size() > 0) {
			updateProductsInMachine(productsToUpdate.toArray());
			// clear the hash map and reset the combo selection and table so we get the up
			// to date data from database
			machineDataMap.clear();
			tblInventory.setItems(null);
			tblInventory.refresh();
		}
	}

	/**
	 * Updates the TableView with the inventory information for the selected
	 * machine.
	 * 
	 * @param event ActionEvent for the ComboBox value change
	 */
	@FXML
	void getComboMachine(ActionEvent event) {
		Machine selectedMachine = new Machine(ClientController.getEKCurrentMachineID(), ClientController.getEKCurrentMachineName(), null, 0);
		ArrayList<InventoryTableData> inventoryList = machineDataMap.get(selectedMachine);
		// no products list saved for this machine
		if (inventoryList == null) {
			inventoryList = new ArrayList<>();
			ArrayList<ProductInMachine> products = getProductsForMachine(selectedMachine);
			if (products != null) {
				selectedMachine.setProducts(products);
				for (ProductInMachine p : products) {
					inventoryList.add(new InventoryTableData(p));
				}
			}
			machineDataMap.put(selectedMachine, inventoryList);
		}
		tblInventory.setItems(FXCollections.observableArrayList(inventoryList));
	}

	/**
	 * Initializes the controller and sets up the TableView, ComboBox, and event
	 * handlers for the buttons.
	 */
	@FXML
	void initialize() {
		ObservableList<Machine> machines = FXCollections
				.observableArrayList(getMachines(new Location[] { Location.North, Location.South, Location.UAE }));
		if (machines == null)
			return;
		tblInventory.setEditable(true);
		colItem.setCellValueFactory(data -> data.getValue().itemNameProperty());
		colItem.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		colQuantity.setCellValueFactory(data -> data.getValue().itemStockProperty());
		colQuantity.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		colRestockAmount.setCellValueFactory(data -> data.getValue().itemStockToAddProperty());
		colRestockAmount.setCellFactory(TextFieldTableCell.forTableColumn());
		colRestockAmount.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");

		colRestockAmount.setOnEditCommit(event -> {
			String value = event.getNewValue();
			// the new value to set, initialized as the old value.
			int newValue = Integer.parseInt(event.getOldValue());
			try {
				/*
				 * try parsing the new value, if we don't get exception and the new value is
				 * non-negative set the value to set as the new value. Otherwise we set back the
				 * old value.
				 */
				int valueInt = Integer.parseInt(value);
				if (valueInt >= 0)
					newValue = valueInt;
			} catch (NumberFormatException exc) {
			}
			event.getRowValue().setStockToAdd(newValue);
			tblInventory.refresh();
		});
	}

	/**
	 * 
	 * Retrieves a list of machines based on the given locations.
	 * 
	 * @param locations An array of Location objects representing the locations to
	 *                  retrieve machines from.
	 * @return An ArrayList of Machine objects representing the machines at the
	 *         given locations.Returns null if there are no machines at the given
	 *         locations.
	 * @throws RuntimeException if there is an error with server communication and
	 *                          the response type is not as expected.
	 */
	private ArrayList<Machine> getMachines(Location[] locations) {
		// if locations passed is null instantiate it to an empty array.
		if (locations == null)
			locations = new Location[] {};
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_MACHINES_BY_LOCATION);
		preparedMessage.setMessageSent(locations);

		// send to server
		System.out.println("Client: Sending machines fetch request to server.");
		ClientUI.getClientController().accept(preparedMessage);

		/*
		 * if the response is not the type we expect, something went wrong with server
		 * communication and we throw an exception.
		 */
		if (!(ClientController.responseFromServer.getRequestType()
				.equals(ServerClientRequestTypes.FETCH_MACHINES_BY_LOCATION))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		}
		/*
		 * otherwise we create a Machine arrayList and add the items from response to
		 * it.
		 */
		Object response = ClientController.responseFromServer.getMessageSent();
		ArrayList<?> responseArr = (ArrayList<?>) response;
		if (responseArr.size() == 0)
			return null;
		// return new arrayList with the items from response casted to Machine.

		return responseArr.stream().map(x -> (Machine) x).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * 
	 * Retrieves a list of products for a given machine.
	 * 
	 * @param machine The machine to retrieve products for.
	 * @return An ArrayList of ProductInMachine objects representing the products in
	 *         the given machine. Returns null if the machine passed is null or if
	 *         there are no products in the machine.
	 * @throws RuntimeException if there is an error with server communication and
	 *                          the response type is not as expected.
	 */
	private ArrayList<ProductInMachine> getProductsForMachine(Machine machine) {
		// if machine wasn't passed we return null.
		if (machine == null)
			return null;
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_PRODUCTS_IN_MACHINE);
		preparedMessage.setMessageSent(machine);

		// send to server
		System.out.println("Client: Sending machines fetch request to server.");
		ClientUI.getClientController().accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType()
				.equals(ServerClientRequestTypes.FETCH_PRODUCTS_IN_MACHINE))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		}
		/*
		 * otherwise we create a Machine arrayList and add the items from response to
		 * it.
		 */
		Object response = ClientController.responseFromServer.getMessageSent();
		ArrayList<?> responseArr = (ArrayList<?>) response;
		if (responseArr.size() == 0)
			return null;
		// return new arrayList with the items from response casted to Machine.

		return responseArr.stream().map(x -> (ProductInMachine) x).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * 
	 * Updates a list of products in a machine.
	 * 
	 * @param productsToUpdate An array of objects representing the products to
	 *                         update in the machine.
	 * @return true if the update is successful.
	 * @throws RuntimeException if there is an error with server communication and
	 *                          the response type is not as expected.
	 */
	private boolean updateProductsInMachine(Object[] productsToUpdate) {
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE_PRODUCTS_IN_MACHINE);
		preparedMessage.setMessageSent(new Object[] { productsToUpdate });

		// send to server
		System.out.println("Client: Sending products in machine update request to server.");
		ClientUI.getClientController().accept(preparedMessage);

		/*
		 * if the response is not the type we expect, something went wrong with server
		 * communication and we throw an exception.
		 */
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		} else {
			Alert successMessage = new Alert(AlertType.INFORMATION);
			successMessage.setTitle("Update Success");
			successMessage.setHeaderText("Update Success");
			successMessage.setContentText("Machines updated successfully!");
			successMessage.show();
		}
		return true;
	}

}
