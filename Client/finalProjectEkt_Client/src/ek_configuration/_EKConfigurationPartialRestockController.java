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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Location;
import logic.Machine;
import logic.ProductInMachine;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;

/**
 * 
 * _EKConfigurationPartialRestockController is a class that controls the partial
 * restocking process of vending machines.
 * 
 * @author Nastay
 * @version (1.0)
 */
public class _EKConfigurationPartialRestockController {

	private HashMap<Machine, ArrayList<InventoryTableData>> machineDataMap = new HashMap<>();
	ObservableList<Machine> machines = FXCollections.observableArrayList(getMachines(new Location[] {}));

	/**
	 * Inner class InventoryTableData represents an item in a vending machine with
	 * attributes of its name, stock, new stock, and maximum stock.
	 */
	public class InventoryTableData {
		/**
		 * Constructor for InventoryTableData class
		 * 
		 * @param item - a ProductInMachine object that represents an item in a vending
		 *             machine
		 */
		public InventoryTableData(ProductInMachine item) {
			this.item = item;
			setItemName(item.getProduct().getProductName());
			setItemStock(item.getStock());
			setNewStock(item.getStock());
			setMaxStock(item.getMaxStock());
		}

		private final ProductInMachine item;

		/**
		 * 
		 * Getter for the item in the vending machine
		 * 
		 * @return the item in the vending machine
		 */
		public final ProductInMachine getItem() {
			return item;
		}

		private final StringProperty itemName = new SimpleStringProperty(this, "itemName");

		/**
		 * 
		 * Getter for the item's name
		 * 
		 * @return the item's name
		 */
		public final String getItemName() {
			return itemName.get();
		}

		/**
		 * Setter for the item's name
		 * 
		 * @param itemName - the new item's name
		 */
		public final void setItemName(String itemName) {
			this.itemName.set(itemName);
		}

		/**
		 * 
		 * Getter for the itemName property
		 * 
		 * @return the itemName property
		 */
		public final StringProperty itemNameProperty() {
			return itemName;
		}

		private final IntegerProperty itemStock = new SimpleIntegerProperty(this, "itemStock");

		/**
		 * 
		 * Getter for the item's stock
		 * 
		 * @return the item's stock
		 */
		public final int getItemStock() {
			return itemStock.get();
		}

		/**
		 * 
		 * Setter for the item's stock
		 * 
		 * @param itemStock - the new item's stock
		 */
		public final void setItemStock(int itemStock) {
			this.item.setStock(itemStock);
			this.itemStock.set(itemStock);
		}

		/**
		 * 
		 * Getter for the itemStock property
		 * 
		 * @return the itemStock property
		 */
		public final IntegerProperty itemStockProperty() {
			return itemStock;
		}

		private final StringProperty itemNewStock = new SimpleStringProperty(this, "itemNewStock");;

		/**
		 * 
		 * Gets the new stock property of the item
		 * 
		 * @return the new stock as an int
		 */
		public final StringProperty itemNewStockProperty() {
			return itemNewStock;
		}

		/**
		 * Gets the new stock value of the item
		 * 
		 * @return the new stock as an int
		 */
		public final int getItemNewStock() {
			return Integer.parseInt(itemNewStock.get());
		}

		/**
		 * Sets the new stock value of the item
		 * 
		 * @param newStock the new stock as an int
		 */
		public final void setNewStock(int newStock) {
			itemNewStock.set(String.valueOf(newStock));
		}

		private final IntegerProperty maxStock = new SimpleIntegerProperty(this, "maxStock");;

		/**
		 * 
		 * Gets the max stock property of the item
		 * 
		 * @return the max stock as an int
		 */
		public final IntegerProperty maxStockProperty() {
			return maxStock;
		}

		/**
		 * Gets the max stock value of the item
		 * 
		 * @return the max stock as an int
		 */
		public final int getItemMaxStock() {
			return maxStock.get();
		}

		/**
		 * Sets the max stock value of the item
		 * 
		 * @param stockToAdd the max stock as an int
		 */
		public final void setMaxStock(int stockToAdd) {
			maxStock.set(stockToAdd);
		}

		/**
		 * 
		 * Returns a string representation of the item, including its name, stock, new
		 * stock, and max stock.
		 * 
		 * @return a string of the item information
		 */
		public String toString() {
			return getItemName() + ", " + getItemStock() + ", " + getItemNewStock() + ", " + getItemMaxStock();
		}

	}// daniel code

	ArrayList<ProductInMachine> productsInMachineData = new ArrayList<>();

	@FXML
	private Button btnCancel;

	@FXML
	private Button btnFinishRestock;

	@FXML
	private Label statusLabel;

	@FXML
	private Text txtWelcomeText;

	@FXML
	TableView<InventoryTableData> tblProducts;

	@FXML
	TableColumn<InventoryTableData, String> colProdName;

	@FXML
	TableColumn<InventoryTableData, Number> colCurrentStock;

	@FXML
	TableColumn<InventoryTableData, String> colNewStock;

	@FXML
	TableColumn<InventoryTableData, Number> colMaxStock;

	@FXML
	Button change;
	@FXML
	TextField changeTextField;

	/**
	 * 
	 * Initializes the view by setting the table to be editable and setting the cell
	 * value factories for each column.
	 * 
	 * It also sets the cell factory for the "colNewStock" column to be a text field
	 * and adds an event listener for when the value is committed.
	 * 
	 * It also calls the "setUpStuff" method to initialize the table with data.
	 */

	@FXML
	private void initialize() {
		// initialize the table:
		tblProducts.setEditable(true);
		colProdName.setCellValueFactory(data -> data.getValue().itemNameProperty());
		colCurrentStock.setCellValueFactory(data -> data.getValue().itemStockProperty());
		colNewStock.setCellValueFactory(data -> data.getValue().itemNewStockProperty());
		colMaxStock.setCellValueFactory(data -> data.getValue().maxStockProperty());

		colNewStock.setCellFactory(TextFieldTableCell.forTableColumn());
		colNewStock.setOnEditCommit(event -> {
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
			event.getRowValue().setNewStock(newValue);
			tblProducts.refresh();
		});

		setUpStuff();
		tblProducts.refresh();

	}

	/**
	 * Sets up the table by initializing the data to be displayed. If no products
	 * list has been saved for the selected machine, it retrieves the products for
	 * that machine from the server and adds them to the table.
	 */
	private void setUpStuff() {
		Machine selectedMachine = new Machine(ClientController.getEKCurrentMachineID(),
				ClientController.getEKCurrentMachineName(), null, 0);
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
		tblProducts.setItems(FXCollections.observableArrayList(inventoryList));
		System.out.println("Inventory list=" + inventoryList);
	}

	/**
	 * 
	 * This method is used to fetch all the products in a given machine. It takes in
	 * a Machine object as a parameter and returns an ArrayList of ProductInMachine
	 * objects.
	 * 
	 * If the passed machine object is null, it returns null. The method creates an
	 * SCCP (Server-Client Communication Protocol) message of type
	 * FETCH_PRODUCTS_IN_MACHINE, sets it with the passed Machine object and sends
	 * it to the server.
	 * 
	 * Upon receiving the response from the server, if the request type is not of
	 * type FETCH_PRODUCTS_IN_MACHINE, it throws a runtime exception.
	 * 
	 * If the response is of expected type, it creates an ArrayList and adds the
	 * response items to it, casting them to ProductInMachine objects.
	 * 
	 * @param machine A Machine object for which the products need to be fetched.
	 * 
	 * @return An ArrayList of ProductInMachine objects if the products are
	 *         successfully fetched from the server, null otherwise.
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
	 * This method is used to fetch all the machines located at a given location. It
	 * takes in an array of Location objects and returns an ArrayList of Machine
	 * objects. If the passed location array is null, it returns null. The method
	 * creates an SCCP (Server-Client Communication Protocol) message of type
	 * FETCH_MACHINES_BY_LOCATION, sets it with the passed Location array and sends
	 * it to the server. Upon receiving the response from the server, if the request
	 * type is not of type FETCH_MACHINES_BY_LOCATION, it throws a runtime
	 * exception. If the response is of expected type, it creates an ArrayList and
	 * adds the response items to it, casting them to Machine objects.
	 * 
	 * @param locations The Location array for which to fetch the machines at.
	 * @return ArrayList<Machine> Returns an ArrayList of Machine objects
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
	 * Updates a list of products in a machine.
	 * 
	 * @param productsToUpdate An array of objects representing the products to
	 *                         update in the machine.
	 * @return true if the update is successful.
	 * @throws RuntimeException if there is an error with server communication and
	 *                          the response type is not as expected.
	 */
	private boolean updateProductsInMachine(Object[] productsToUpdate, ActionEvent event) {
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
			loadPreviousWindow(event);

			Alert successMessage = new Alert(AlertType.INFORMATION);
			successMessage.setTitle("Update Success");
			successMessage.setHeaderText("Update Success");
			successMessage.setContentText("Machines updated successfully!");
			successMessage.show();
		}
		return true;
	}

	/**
	 * 
	 * The getBtnCancel method is triggered when the "Cancel" button is pressed.
	 * This method calls the loadPreviousWindow method to navigate back to the
	 * previous window.
	 * 
	 * @param event The event that triggers the method call.
	 */
	@FXML
	void getBtnCancel(ActionEvent event) {
		// return to previous window
		loadPreviousWindow(event);
	}

	/**
	 * 
	 * The getBtnFinishRestock method is triggered when the "Finish Restock" button
	 * is pressed. This method updates the new stock data in the table and returns
	 * to the previous window. It creates a list of products to update and iterates
	 * over the machines that were accessed. It then sets the updated stock to the
	 * object and adds it to the list of products to update. If the list of products
	 * to update is not empty, it calls the updateProductsInMachine method.
	 * 
	 * @param event The event that triggers the method call.
	 */
	@FXML
	void getBtnFinishRestock(ActionEvent event) {
		// update date in table and return
		ArrayList<ProductInMachine> productsToUpdate = new ArrayList<>();
		// iterate over the machines that were accessed
		machineDataMap.forEach((machine, data) -> {
			for (InventoryTableData item : data) {
				int newStock = item.getItemNewStock();
				/*
				 * if stock to add is more than 0, we set the updated stock to the object and
				 * add it to the list.
				 */
				if (newStock != item.getItemStock()) {
					item.setItemStock(newStock);
					// item.setStockToAdd(0);
					productsToUpdate.add(item.getItem());
				}
			}
		});
		if (productsToUpdate.size() > 0) {
			updateProductsInMachine(productsToUpdate.toArray(), event);
			// tblProducts.refresh();
		}

	}

	/**
	 * 
	 * The loadPreviousWindow method is used to navigate back to the previous
	 * window. It hides the current window and opens the specified fxml file in a
	 * new stage.
	 * 
	 * @param event The event that triggers the method call.
	 */
	private void loadPreviousWindow(ActionEvent event) {
		String nextScreenPath = "/gui/_EKConfigurationLogisticsEmployeeFrame.fxml";
		// load table - insert all products in this machine
		// sammy D the current window
		((Node) event.getSource()).getScene().getWindow().hide();
		// prepare the new stage:
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, nextScreenPath, null, "Logistics Employee Frame", true);
		primaryStage.show();
	}

}
