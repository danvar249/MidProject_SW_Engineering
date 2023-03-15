package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Order;
import logic.Order.*;

/**
 * 
 * Delivery Manager page controller for the client application. Handles the UI
 * and logic for the delivery manager page.
 */
public class DeliveryManagerPageController {
	/*
	 * A list of orders to be displayed and updated in the delivery manager page.
	 */
	private ArrayList<Order> orders;
	/*
	 * A set of orders that have been changed and need to be updated on the server.
	 */
	private Set<Order> ordersChanged = new HashSet<>();

	@FXML
	private Text welcomeText;

	@FXML
	private TableView<Order> deliveryTable;

	@FXML
	private TableColumn<Order, String> tblOrderNumberColumn;

	@FXML
	private TableColumn<Order, LocalDate> tblDateReceivedColumn;

	@FXML
	private TableColumn<Order, LocalDateTime> tblTimeColumn;

	@FXML
	private TableColumn<Order, Order.Status> tblStatusColumn;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnUpdate;

	/**
	 * Logs the user out and navigate back to login screen.
	 * 
	 * @param event
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		ClientController.sendLogoutRequest();
		// move to new window
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", true);

		primaryStage.show();
	}

	/**
	 * Update the order status on the server
	 * 
	 * @param event
	 * @throws RuntimeException if there's an error with server communication
	 */
	@FXML
	void getBtnUpdate(ActionEvent event) {
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE_ONLINE_ORDERS);
		preparedMessage.setMessageSent(new Object[] { orders.toArray() });

		// send to server
		System.out.println("Client: Sending online orders update request to server.");
		ClientUI.getClientController().accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		} else {
			Alert successMessage = new Alert(AlertType.INFORMATION);
			successMessage.setTitle("Update Success");
			successMessage.setHeaderText("Update Success");
			successMessage.setContentText("Orders updated successfully!");
			successMessage.show();
			// remove orders that moved to complete, requested cancel or delivered.
			for (Order order : ordersChanged) {
				Status newStatus = order.getStatus();
				switch (newStatus) {
				case Complete:
				case RequestedCancellation:
				case Delivered:
					deliveryTable.getItems().remove(order);
					break;
				default:
					// do nothing
					break;
				}
			}
		}
	}

	/**
	 * Initialize the delivery manager page by populating the table with orders and
	 * setting up the table columns.
	 */
	@FXML
	public void initialize() {
		/**
		 * Retrieve the orders from the database
		 */
		orders = getOrders();
		if (orders == null)
			return;
		/**
		 * Calculate the delivery time for orders with InProgress status and null
		 * delivery time set the delivery time to the date received plus 7 days and
		 * 12:00 PM.
		 */
		for (Order o : orders) {
			if (o.getStatus() == Status.InProgress && o.getDeliveryTime() == null) {
				o.setDeliveryTime(LocalDateTime.of(o.getDateReceived().plusDays(7), LocalTime.NOON));
				ordersChanged.add(o);
			}
		}
		/**
		 * Define the order number column
		 */
		tblOrderNumberColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("orderID"));
		tblOrderNumberColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		/**
		 * Define the date received column
		 */
		tblDateReceivedColumn.setCellValueFactory(new PropertyValueFactory<Order, LocalDate>("dateReceived"));
		tblDateReceivedColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		/**
		 * Define the time column
		 */
		tblTimeColumn.setCellValueFactory(new PropertyValueFactory<Order, LocalDateTime>("deliveryTime"));
		tblTimeColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		/**
		 * Define the status column
		 */
		tblStatusColumn.setCellValueFactory(
				cellData -> new SimpleObjectProperty<Order.Status>(cellData.getValue().getStatus()));
		tblStatusColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		/**
		 * Set the status column as a ComboBox
		 */
		tblStatusColumn.setCellFactory(col -> {
			ComboBox<Order.Status> combo = new ComboBox<>();
			combo.getStylesheets().add("gui/comboboxCSS.css");

			TableCell<Order, Order.Status> cell = new TableCell<Order, Order.Status>() {
				@Override
				protected void updateItem(Order.Status status, boolean empty) {
					super.updateItem(status, empty);
					if (empty) {
						setGraphic(null);
					} else {
						/**
						 * Populate the status combobox based on the current status of the order.
						 * If the order is in progress, allow it to be changed to request cancellation or delivered.
						 * If the order is received allow to to be changed only to complete.
						 * */
						switch (status) {
						case InProgress:
							combo.getItems().setAll(
									new Status[] { Status.InProgress, Status.Delivered, Status.RequestedCancellation });
							break;
						case Received:
							combo.getItems().setAll(new Status[] { Status.Received, Status.Complete });
							break;
						default:
							// do nothing
							break;
						}
						combo.setValue(status);
						setGraphic(combo);
					}
				}
			};
			/**
			 * Handle the changing of the order status by keeping track of the orders that
			 * were changed.
			 */
			combo.setOnAction(e -> {
				/**
				 * Retrieve the Order object that corresponds to the current row of the table
				 */
				Order o = deliveryTable.getItems().get(cell.getIndex());
				/**
				 * Retrieve the new status selected by the user in the ComboBox
				 */
				Status newStatus = combo.getValue();
				if (o.getStatus() != newStatus) {
					/**
					 * Remove the order from the set in case it was already there, update the
					 * status, and add it back to the set
					 */
					ordersChanged.remove(o);
					o.setStatus(newStatus);
					ordersChanged.add(o);
				}
			});

			return cell;
		});
		tblStatusColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");

		deliveryTable.setItems(FXCollections.observableArrayList(orders));
		deliveryTable.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");

	}

	/**
	 * Gets the orders with status "InProgress" or "Received" from the database.
	 * @return a list of the orders.
	 */
	private ArrayList<Order> getOrders() {
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.FETCH_ORDERS);
		preparedMessage.setMessageSent(
				new Object[] { new Status[] { Status.InProgress, Status.Received }, new Type[] { Type.Delivery } });

		// send to server
		System.out.println("Client: Sending online orders fetch request to server.");
		ClientUI.getClientController().accept(preparedMessage);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.FETCH_ORDERS))) {
			throw new RuntimeException("Error with server communication: Non expected request type");
		}
		// otherwise we create an Order arrayList and add the items from response
		// to it.
		Object response = ClientController.responseFromServer.getMessageSent();
		ArrayList<?> responseArr = (ArrayList<?>) response;
		if (responseArr.size() == 0)
			return null;
		// return new arrayList with the items from response casted to Order.
		return orders = responseArr.stream().map(x -> (Order) x).collect(Collectors.toCollection(ArrayList::new));
	}

}
