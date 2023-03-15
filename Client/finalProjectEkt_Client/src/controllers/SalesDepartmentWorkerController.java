package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Promotions;

/**
 * This class is responsible for handling the functionality of the Sales
 * Department Worker in the application, such as displaying active promotions
 * and logging out of the application.
 */
public class SalesDepartmentWorkerController implements Initializable {

	@FXML
	private Button btnActivePromotion;
	@FXML
	private Button btnLogout;
	@FXML
	private TableView<Promotions> promotionTable;
	@FXML
	private TableColumn<Promotions, Integer> promotionIDColumn;
	@FXML
	private TableColumn<Promotions, String> promotionNameColumn;
	@FXML
	private TableColumn<Promotions, String> promotionDescriptionColumn;
	@FXML
	private TableColumn<Promotions, String> productIDColumn;
	@FXML
	private TableColumn<Promotions, Integer> locationColumn;
	@FXML
	private TableColumn<Promotions, Integer> discountPercentageColumn;
	@FXML
	private TableColumn<Promotions, LocalDate> startDateColumn;
	@FXML
	private TableColumn<Promotions, LocalDate> endDateColumn;
	@FXML
	private TableColumn<Promotions, Boolean> promotionStatusColumn;
	private ObservableList<Promotions> listView = FXCollections.observableArrayList();
	//private Promotions selectedPromotion;

	/**
	 * 
	 * Initializes the promotion table and sets the cell value factories for each
	 * column, also connect to the database and retrieve the promotion names and
	 * sets it to the promotion table.
	 * 
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// promotionId, promotionName, promotionDescription, locationId, productID,
		// discountPercentage, startDate, endDate, promotionStatus
		// Set the cell value factory for each TableColumn object
		promotionIDColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("promotionId"));
		promotionNameColumn.setCellValueFactory(new PropertyValueFactory<Promotions, String>("promotionName"));
		promotionDescriptionColumn
				.setCellValueFactory(new PropertyValueFactory<Promotions, String>("promotionDescription"));
		// locationColumn.setCellValueFactory(new PropertyValueFactory<Promotions,
		// Integer>("locationID"));
		productIDColumn.setCellValueFactory(new PropertyValueFactory<Promotions, String>("productID"));
		discountPercentageColumn
				.setCellValueFactory(new PropertyValueFactory<Promotions, Integer>("discountPercentage"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<Promotions, LocalDate>("startDate"));
		endDateColumn.setCellValueFactory(new PropertyValueFactory<Promotions, LocalDate>("endDate"));
		promotionStatusColumn.setCellValueFactory(new PropertyValueFactory<Promotions, Boolean>("promotionStatus"));
		promotionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// Update the selectedOffer variable with the new selected Offer object
			//selectedPromotion = newValue;
		});

		// Connect to the database and retrieve the promotion names
		// Return the retrieved promotion names in an ArrayList
		//promotionNames = new ArrayList<>();
		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.DISPLAY_PROMOTIONS_TO_ACTIVE);

		Integer idUser = ClientController.getCurrentSystemUser().getId();
		preparedMessage.setMessageSent(idUser);

		// send to servers
		System.out.println("Client: Sending excisiting promotion request to the server.");
		ClientUI.getClientController().accept(preparedMessage);

		@SuppressWarnings("unchecked")
		ArrayList<Promotions> arrayFromDatabase = (ArrayList<Promotions>) ClientController.responseFromServer
				.getMessageSent();
		listView.clear();
		for (Promotions promotion : arrayFromDatabase) {
			listView.add(promotion);
		}
		listView.forEach(promotion -> System.out.println(promotion));
		promotionTable.setEditable(true);
		// promotionTable.setItems(listView);
		promotionTable.getItems().setAll(arrayFromDatabase);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.DISPLAY))) {
			throw new RuntimeException("Error with server communication: Non expected request type");

		}

	}

	/**
	 * 
	 * Handles the event of activating a promotion from the table view by sending a
	 * request to the server to update the status of the selected promotion, and
	 * then updates the table view to reflect the change.
	 * 
	 * @param event
	 */
	@FXML
	private void ActivePromotionHandler(ActionEvent event) {

		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.UPDATE_PROMOTION_STATUS);
		String selectedPid = promotionTable.getSelectionModel().getSelectedItem().getPromotionId();
		preparedMessage.setMessageSent(selectedPid);

		// send to servers
		System.out.println("Client: Sending excisiting promotion request to the server.");
		ClientUI.getClientController().accept(preparedMessage);

		if (!(ClientController.responseFromServer.getRequestType()
				.equals(ServerClientRequestTypes.UPDATE_PROMOTION_STATUS)))
			throw new RuntimeException("Error with server communication: Non expected request type");

		updateTablePro();
	}

	/**
	 * 
	 * This method updates the promotions table with the updated promotions by
	 * sending a request to the server to retrieve the updated promotions and
	 * updating the table with the received promotions.
	 */
	public void updateTablePro() {

		SCCP preparedMessage = new SCCP();
		preparedMessage.setRequestType(ServerClientRequestTypes.DISPLAY_PROMOTIONS_TO_ACTIVE);
		Integer idUser = ClientController.getCurrentSystemUser().getId();
		preparedMessage.setMessageSent(idUser);

		// send to servers
		System.out.println("Client: Sending excisiting promotion request to the server.");
		ClientUI.getClientController().accept(preparedMessage);

		@SuppressWarnings("unchecked")
		ArrayList<Promotions> arrayFromDatabase = (ArrayList<Promotions>) ClientController.responseFromServer
				.getMessageSent();
		listView.clear();
		for (Promotions promotion : arrayFromDatabase) {
			listView.add(promotion);
		}
		listView.forEach(promotion -> System.out.println(promotion));
		promotionTable.setEditable(true);
		// promotionTable.setItems(listView);
		promotionTable.getItems().setAll(arrayFromDatabase);

		// if the response is not the type we expect, something went wrong with server
		// communication and we throw an exception.
		if (!(ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.DISPLAY)))
			throw new RuntimeException("Error with server communication: Non expected request type");
	}

	/**
	 * Handles clicking the logout button, sends request to the server to log out
	 * the current user and navigates the user back to the login page.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	private void logoutHandler(ActionEvent event) throws Exception {
		// actually do the logout:
		ClientController.sendLogoutRequest();

		// log
		System.out.println("Sales Worker has logged off");
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, this, "/gui/EktSystemUserLoginForm.fxml", null, "Login", false);
		primaryStage.show();
	}

}
