package controllers;

import java.sql.Date;
import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * A class that represents an order to be cancelled.
 * It contains variables for the ID, total price, and date of the order.
 *
 */
public class EktCancelOrderPageController {

	public class orderToCancel {
		private SimpleStringProperty id;
		private SimpleStringProperty totalPrice;
		private SimpleStringProperty date;
		/**
		 * Constructor for orderToCancel class.
		 * 
		 * @param id the ID of the order
		 * @param totalPrice the total price of the order
		 * @param date the date the order was received
		 */
		public orderToCancel(int id, double totalPrice, String date) {
			this.id = new SimpleStringProperty(new Integer(id).toString());
			this.totalPrice = new SimpleStringProperty(new Double(totalPrice).toString());
			this.date = new SimpleStringProperty(date);
		}
		/**
		 * Gets the total price of the order.
		 * 
		 * @return total price of the order
		 */
		public SimpleStringProperty getTotalPrice() {
			return totalPrice;
		}
		/**
		 * Sets the total price of the order.
		 * 
		 * @param totalPrice the new total price of the order
		 */

		public void setTotalPrice(SimpleStringProperty totalPrice) {
			this.totalPrice = totalPrice;
		}
		/**
		 * Gets the date the order was received.
		 * 
		 * @return date the order was received
		 */
		public SimpleStringProperty getDate() {
			return date;
		}
		/**

		Sets the date of the order.
		@param date the new date of the order
		*/
		public void setDate(SimpleStringProperty customerName) {
			this.date = customerName;
		}
		/**
		 * Gets the ID of the order.
		 * 
		 * @return ID of the order
		 */
		public SimpleStringProperty getId() {
			return id;
		}	/**
		 * Sets the ID of the order.
		 * 
		 * @param id the new ID of the order
		 */

		public void setId(SimpleStringProperty id) {
			this.id = id;
		}

	}
	
	@FXML
	private Text txtOrderCancelled;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnUpdate;

	@FXML
	private TableColumn<orderToCancel, String> columnOrderID;

	@FXML
	private TableColumn<orderToCancel, String> columnTotalPrice;

	@FXML
	private TableColumn<orderToCancel, String> columnDateReceived;

	@FXML
	private TableView<orderToCancel> tableUsers;
	
	@FXML
	/**
	 * A method that is called when the page is initialized.
	 * It sets up the table columns and populates the table with orders to be cancelled.
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
		final ObservableList<orderToCancel> data = FXCollections.observableArrayList();

		// Column 1
		columnOrderID.setCellValueFactory(cellData -> cellData.getValue().getId());
		columnOrderID.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400px,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");

		// Column 2
		columnTotalPrice.setCellValueFactory(cellData -> cellData.getValue().getTotalPrice());
		columnTotalPrice.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		
		// Column 3
		columnDateReceived.setCellValueFactory(cellData -> cellData.getValue().getDate());
		columnDateReceived.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		
		// Column 4
		TableColumn<orderToCancel, Button> columnAccept = new TableColumn<>("Cancel");
		columnAccept.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		columnAccept.setPrefWidth(99);
		columnAccept.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnAccept.setCellFactory(col -> {
			Button cancel = new Button("CANCEL");
			cancel.setTextFill(Color.WHITE);
			cancel.getStylesheets().add("/gui/buttonCSS.css");
			TableCell<orderToCancel, Button> cell = new TableCell<orderToCancel, Button>() {
				/**
				 * A method that is called when the btnUpdate button is clicked.
				 * It updates the list of orders to be cancelled in the table view.
				 * 
				 * @param event the event that triggered the method call
				 */
				@Override
				public void updateItem(Button item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(cancel);
					}
				}
			};
			cancel.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Cancel Order");
				alert.setHeaderText("This action will cancel the order!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						
						
						// Set order as cancelled
						orderToCancel order = cell.getTableView().getItems().get(cell.getIndex());
						tableUsers.getSelectionModel().clearSelection();
						SimpleStringProperty id = order.getId();
						data.remove(cell.getIndex());
						SCCP removeOrder = new SCCP();
						removeOrder.setRequestType(ServerClientRequestTypes.UPDATE);
						//Update order to cancelled
						removeOrder.setMessageSent(
								new Object[] { "orders", "statusId = 2", "orderID = " + id.getValue() });
						
						ClientUI.getClientController().accept(removeOrder);
						txtOrderCancelled.setText("ORDER #" + id.getValue() + " CANCELLED");
						
					} else {
						return;
					}
				});
			});
			return cell;
		});


		SCCP ordersForCancellation = new SCCP();
		ordersForCancellation.setRequestType(ServerClientRequestTypes.GET_ORDERS_FOR_CANCELLATION);
		ordersForCancellation.setMessageSent(ClientController.getCurrentUserRegion());

		ClientUI.getClientController().accept(ordersForCancellation);

		ArrayList<?> arrayOfCancelledOrders = ((ArrayList<?>) ClientController.responseFromServer.getMessageSent());

		for (ArrayList<Object> order : (ArrayList<ArrayList<Object>>) arrayOfCancelledOrders) {
			int orderID = (int) order.get(0);
			double totalPrice = (double) order.get(1);
			Date date = (Date) order.get(2);
			data.add(new orderToCancel(orderID, totalPrice, date.toString()));

		}

		tableUsers.setItems(data);
		tableUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableUsers.getColumns().add(columnAccept);

	}
	/**
	* A method that is called when the btnBack button is clicked.
	* It returns the user to the previous page.
	*
	* @param event the event that triggered the method call
	*/
	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktRegionalManagerHomePage.fxml", null, "Ekt Regional Manager Home Page",false);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
}
