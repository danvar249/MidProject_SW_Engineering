package controllers;

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
import javafx.stage.Stage;

/**
 * 
 * The EktRegionalManagerAcceptNewCustomerController class handles the process
 * of accepting or declining new customer requests for a regional manager. It
 * retrieves all pending customer requests from the server, displays them in a
 * table, and allows the regional manager to either accept or decline the
 * request. Accepting the request will update the customer's status to a new
 * customer and decline will remove the customer's request.
 * 
 * @author Maxim, Dima, Rotem
 *
 */
public class EktRegionalManagerAcceptNewCustomerController {
	/**
	 * This class represents a customer that is waiting to be accepted by the
	 * Regional Manager. It contains properties such as name, userType, id,
	 * phoneNumber, and two buttons - accept and decline. The accept button, when
	 * clicked, will create a new customer and change the type of user to the
	 * specified user type. The decline button, when clicked, will remove the
	 * customer request. The class also has getters and setters for its properties.
	 * 
	 * @author Dima, Rotem, Maxim
	 *
	 */
	public class customerToAccept {
		private SimpleStringProperty name;
		private SimpleStringProperty userType;
		private SimpleStringProperty id;
		private SimpleStringProperty phoneNumber;
		Button accept = new Button();

		/**
		 * @return the accept button
		 */
		public Button getAccept() {
			return accept;
		}

		/**
		 * 
		 * @param set the accept button
		 */
		public void setAccept(Button accept) {
			this.accept = accept;
		}

		/**
		 * 
		 * @return the decline button
		 */
		public Button getDecline() {
			return decline;
		}

		/**
		 * 
		 * @param set the decline button
		 */
		public void setDecline(Button decline) {
			this.decline = decline;
		}

		Button decline = new Button();

		/**
		 * Constructor for customerToAccept class
		 * 
		 * @param name        the name of the customer
		 * @param userType    the type of user the customer is requesting to become
		 * @param id          the ID of the customer
		 * @param phoneNumber the phone number of the customer
		 */
		public customerToAccept(String name, String userType, String id, String phoneNumber) {
			this.name = new SimpleStringProperty(name);
			this.userType = new SimpleStringProperty(userType);
			this.id = new SimpleStringProperty(id);
			this.phoneNumber = new SimpleStringProperty(phoneNumber);
			// accept.setGraphic(new ImageView(new Image("controllers/Images/v.png")));
			accept.setOnAction(ae -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Accept Customer");
				alert.setHeaderText("This action will create a new customer!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.UPDATE);
						updateCustomerToNewCustomer.setMessageSent(
								new Object[] { "system_user", "typeOfUser = \"" + userType + "\"", "id = " + id });

					} else {
						return;
					}
				});
			});
			// decline.setGraphic(new ImageView(new Image("controllers/Images/x.png")));
			decline.setOnAction(ae -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Decline Customer");
				alert.setHeaderText("This action will remove the customer request!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.UPDATE);
						updateCustomerToNewCustomer.setMessageSent(
								new Object[] { "system_user", "typeOfUser = \"" + userType + "\"", "id = " + id });
					} else {
						return;
					}
				});
			});
		}

		/**
		 * 
		 * @return the userType of the customerToAccept object as a SimpleStringProperty
		 */
		public SimpleStringProperty getUserType() {
			return userType;
		}

		/**
		 * Sets the userType of the customerToAccept object
		 * 
		 * @param lastName the userType to be set as a SimpleStringProperty
		 */
		public void setuserType(SimpleStringProperty lastName) {
			this.userType = lastName;
		}

		/**
		 * @return the name of the customerToAccept object as a SimpleStringProperty
		 */
		public SimpleStringProperty getName() {
			return name;
		}

		/**
		 * 
		 * Sets the name of the customerToAccept object
		 * 
		 * @param firstName the name to be set as a SimpleStringProperty
		 */
		public void setName(SimpleStringProperty firstName) {
			this.name = firstName;
		}

		/**
		 * 
		 * @return the id of the customerToAccept object as a SimpleStringProperty
		 */
		public SimpleStringProperty getId() {
			return id;
		}

		/**
		 * Sets the id of the customerToAccept object
		 * 
		 * @param id the id to be set as a SimpleStringProperty
		 */
		public void setId(SimpleStringProperty id) {
			this.id = id;
		}

		/**
		 * 
		 * @return the phoneNumber of the customerToAccept object as a
		 *         SimpleStringProperty
		 */
		public SimpleStringProperty getPhoneNumber() {
			return phoneNumber;
		}

		/**
		 * 
		 * This method sets the phone number of the customer.
		 * 
		 * @param phoneNumber The phone number of the customer.
		 */
		public void setPhoneNumber(SimpleStringProperty phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

	}

	@FXML
	private Button btnBack;

	@FXML
	private Button btnUpdate;

	@FXML
	private TableColumn<customerToAccept, String> columnID;

	@FXML
	private TableColumn<customerToAccept, String> columnName;

	@FXML
	private TableColumn<customerToAccept, String> columnUserType;

	@FXML
	private TableColumn<customerToAccept, String> columnPhoneNumber;

	@FXML
	private TableView<customerToAccept> tableUsers;

	@FXML
	/**
	 * The initialize method is responsible for setting up the TableView for
	 * displaying customers that are waiting for acceptance. It sets the cell value
	 * factory for each column and sets the style of the columns. It also adds a
	 * "Accept" and "Decline" button to the table and sets their functionality. When
	 * the "Accept" button is clicked, a confirmation dialog is displayed and if
	 * confirmed, the customer is accepted and removed from the table. Similarly,
	 * when the "Decline" button is clicked, a confirmation dialog is displayed and
	 * if confirmed, the customer request is removed from the table.
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
		final ObservableList<customerToAccept> data = FXCollections.observableArrayList();

		// Column 1
		columnName.setCellValueFactory(cellData -> cellData.getValue().getName());
		columnName.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		// Column 2
		columnUserType.setCellValueFactory(cellData -> cellData.getValue().getUserType());
		columnUserType.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		// Column 3
		columnID.setCellValueFactory(cellData -> cellData.getValue().getId());
		columnID.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		// Column 4
		columnPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumber());
		columnPhoneNumber.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		// Column 5
		TableColumn<customerToAccept, Button> columnAccept = new TableColumn<>("Accept");
		columnAccept.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		columnAccept.setPrefWidth(74);
		columnAccept.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnAccept.setCellFactory(col -> {
			Button accept = new Button("ACCEPT");
			accept.setTextFill(Color.WHITE);
			accept.getStylesheets().add("/gui/buttonCSS.css");
			TableCell<customerToAccept, Button> cell = new TableCell<customerToAccept, Button>() {
				@Override
				public void updateItem(Button item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(accept);
					}
				}
			};
			accept.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Accept Customer");
				alert.setHeaderText("This action will create a new customer!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						customerToAccept customer = cell.getTableView().getItems().get(cell.getIndex());
						@SuppressWarnings("unused")
						SimpleStringProperty userType = customer.getUserType();
						tableUsers.getSelectionModel().clearSelection();
						SimpleStringProperty id = customer.getId();
						data.remove(cell.getIndex());
						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.UPDATE);
						if (customer.getUserType().getValue().equals("unapproved_customer")) {
							updateCustomerToNewCustomer.setMessageSent(new Object[] { "systemuser",
									"typeOfUser = \"customer\"", "id = " + id.getValue() });
						} else {
							updateCustomerToNewCustomer.setMessageSent(new Object[] { "systemuser",
									"typeOfUser = \"subscriber\"", "id = " + id.getValue() });
						}

						ClientUI.getClientController().accept(updateCustomerToNewCustomer);
						// send the updateCustomerToNewCustomer to the server
					} else {
						return;
					}
				});
			});
			return cell;
		});

		// Column 6
		TableColumn<customerToAccept, Button> columnDecline = new TableColumn<>("Decline");
		columnDecline.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		columnDecline.setPrefWidth(74);
		columnDecline.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnDecline.setCellFactory(col -> {
			Button decline = new Button("DECLINE");
			decline.setTextFill(Color.WHITE);
			decline.getStylesheets().add("/gui/buttonCSS.css");
			TableCell<customerToAccept, Button> cell = new TableCell<customerToAccept, Button>() {
				@Override
				public void updateItem(Button item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(decline);
					}
				}
			};
			decline.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Decline Customer");
				alert.setHeaderText("This action will delete this customer!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						// Accept customer
						customerToAccept customer = cell.getTableView().getItems().get(cell.getIndex());
						@SuppressWarnings("unused")
						SimpleStringProperty userType = customer.getUserType();
						tableUsers.getSelectionModel().clearSelection();
						SimpleStringProperty id = customer.getId();
						data.remove(cell.getIndex());
						SCCP updateCustomerToNewCustomer = new SCCP();
						updateCustomerToNewCustomer.setRequestType(ServerClientRequestTypes.REMOVE);
						updateCustomerToNewCustomer
								.setMessageSent(new Object[] { "systemuser", "", "id = " + id.getValue() });
						ClientUI.getClientController().accept(updateCustomerToNewCustomer);
						// send the updateCustomerToNewCustomer to the server
					} else {
						return;
					}
				});
			});
			return cell;
		});

		SCCP unapprovedCustomers = new SCCP();
		unapprovedCustomers.setRequestType(ServerClientRequestTypes.SELECT);
		unapprovedCustomers.setMessageSent(
				new Object[] { "systemuser JOIN customer_location ON systemuser.id = customer_location.id", true,
						"firstName, lastName, typeOfUser, systemuser.id, phoneNumber", true,
						"typeOfUser = \"unapproved_customer\" OR typeOfUser = \"unapproved_subscriber\" "
								+ "AND location = " + "'" + ClientController.getCurrentUserRegion() + "';",
						false, null });

		ClientUI.getClientController().accept(unapprovedCustomers);

		ArrayList<?> arrayOfUnapprovedCustomers = ((ArrayList<?>) ClientController.responseFromServer.getMessageSent());

		for (ArrayList<Object> customer : (ArrayList<ArrayList<Object>>) arrayOfUnapprovedCustomers) {
			String name = customer.get(0) + " " + customer.get(1);
			String userType = (String) customer.get(2);
			String id = customer.get(3) + "";
			String phoneNumber = (String) customer.get(4);
			data.add(new customerToAccept(name, userType, id, phoneNumber));

		}

		tableUsers.setItems(data);
		tableUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableUsers.getColumns().addAll(columnAccept, columnDecline);

	}

	/**
	 * This is the method for the "Back" button in the
	 * EktRegionalManagerAcceptNewCustomerController class. It is used to navigate
	 * to the Ekt Regional Manager Home Page. It takes an ActionEvent as a
	 * parameter. The method retrieves the current system user and calls the
	 * createWindow method in the WindowStarter class to display the Ekt Regional
	 * Manager Home Page. The current window is then closed.
	 * 
	 * @param event the event that triggers the button click
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktRegionalManagerHomePage.fxml", null, "Ekt Regional Manager Home Page", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

	@FXML
	void getBtnUpdate(ActionEvent event) {

	}

}
