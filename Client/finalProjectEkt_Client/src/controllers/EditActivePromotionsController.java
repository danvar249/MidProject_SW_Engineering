package controllers;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * This class is a JavaFX controller for the active promotions editing form.
 * @author DimaKyn
 *
 */
public class EditActivePromotionsController implements Initializable {

	@FXML
	private TableView<promotionToTable> promotionTable;
	@FXML
	private TableColumn<promotionToTable, String> promotionIDColumn;
	@FXML
	private TableColumn<promotionToTable, String> promotionNameColumn;
	@FXML
	private TableColumn<promotionToTable, String> promotionDescriptionColumn;

	@FXML
	private TableColumn<promotionToTable, String> locationColumn;
	@FXML
	private TableColumn<promotionToTable, String> discountPercentageColumn;
	@FXML
	private TableColumn<promotionToTable, String> startDateColumn;
	@FXML
	private TableColumn<promotionToTable, String> promotionStatusColumn;
	
	private ObservableList<promotionToTable> listView = FXCollections.observableArrayList();

	/**
	 * Sends a request to the server to get the promotions to display in the table
	 */
	private void displayPromotionsTable() {
		// Connect to the database and retrieve the promotion names
		// Return the retrieved promotion names in an ArrayList
		SCCP preparedMessage = new SCCP();
		
		preparedMessage.setRequestType(ServerClientRequestTypes.GET_PROMOTIONS_BY_LOCATION);
		preparedMessage.setMessageSent(new Object[] {});
		
		ClientUI.getClientController().accept(preparedMessage);

	}


	/**
	 * Navigates the user back to the sales manager page.
	 * @param event
	 */
	public void goBackHandler(ActionEvent event) {
		Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
		currentStage.close();

		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, new Object(), "/gui/SalesManager.fxml", null, "Sales", true);
		primaryStage.show();
	}
	
	
	/**
	 * Initializes the fields of the {@link EditActivePromotionsController} class.
	 * This method sets the cell value factory for each TableColumn object,
	 * sets the style for each column and adds the "Activate" button to the table.
	 * @param url URL of the location used to resolve relative paths for the root object, or null if the location is not known
	 * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
	 */
	@SuppressWarnings("unchecked")
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// Set the cell value factory for each TableColumn object
		promotionIDColumn.setCellValueFactory(cellData -> cellData.getValue().getPromoId());
		promotionIDColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		promotionNameColumn.setCellValueFactory(cellData -> cellData.getValue().getPromotionName());
		promotionNameColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		promotionDescriptionColumn
				.setCellValueFactory(cellData -> cellData.getValue().getPromotionDescription());
		promotionDescriptionColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		locationColumn.setCellValueFactory(cellData -> cellData.getValue().getLocationName());
		locationColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		discountPercentageColumn
				.setCellValueFactory(cellData -> cellData.getValue().getDiscountPercentage());
		discountPercentageColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		startDateColumn.setCellValueFactory(cellData -> cellData.getValue().getStartDate());
		startDateColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		promotionStatusColumn.setCellValueFactory(cellData -> cellData.getValue().getPromotionStatus());
		promotionStatusColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");

		TableColumn<promotionToTable, Button> columnButton = new TableColumn<>("Activate");
		columnButton.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white; -fx-font-weight: bold;");
		columnButton.setPrefWidth(134);
		columnButton.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		columnButton.setCellFactory(col -> {
			Button setPromoStatus = new Button("CHANGE STATUS");
			setPromoStatus.setTextFill(Paint.valueOf("WHITE"));
			setPromoStatus.getStylesheets().add("/gui/buttonCSS.css");
			TableCell<promotionToTable, Button> cell = new TableCell<promotionToTable, Button>() {
				@Override
				public void updateItem(Button item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						
					} else {
						setGraphic(setPromoStatus);
					}
				}
			};
			setPromoStatus.setOnAction((event) -> {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Activate Promotion");
				alert.setHeaderText("This action will activate a new promotion!");
				alert.setContentText("Continue?");
				alert.showAndWait().ifPresent(type -> {
					if (type == ButtonType.OK) {
						promotionToTable promotion = cell.getTableView().getItems().get(cell.getIndex());
						SimpleStringProperty status = promotion.getPromotionStatus();
						SimpleStringProperty promoId = promotion.getPromoId();
						// Accept customer
						SCCP updatePromotionStatus = new SCCP();
						String currentPromotionStatus = status.getValue();
						updatePromotionStatus.setRequestType(ServerClientRequestTypes.UPDATE);
						
						if (currentPromotionStatus.equals("Unactive")) {
							updatePromotionStatus.setMessageSent(new Object[] {
									"promotions", "promotionStatus = 1", "promotionId = " + promoId.getValue() });
							promotion.setPromotionStatus(new SimpleStringProperty("Active"));
							cell.getTableView().refresh();
						} else {
							updatePromotionStatus.setMessageSent(new Object[] {
									"promotions", "promotionStatus = 0", "promotionId = " + promoId.getValue() });
							promotion.setPromotionStatus(new SimpleStringProperty("Unactive"));
							cell.getTableView().refresh();
						}
						System.out.println("promoId = " + promoId.getValue());
						// send the updateCustomerToNewCustomer to the server
						ClientUI.getClientController().accept(updatePromotionStatus);
						
						if (setPromoStatus.getText().equals("ACTIVATE")) {
							setPromoStatus.setText("DEACTIVATE");
						} else {
							setPromoStatus.setText("ACTIVATE");
						}
						
					} else {
						return;
					}
				});
			});
			return cell;
		});
		// Connect to the database and retrieve the promotion names
		// Return the retrieved promotion names in an ArrayList
		displayPromotionsTable();
		
		ArrayList<?> promotionNames = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		// System.out.println(promotionNames.toString());
		// Add the promotion names to the combo box
		for (ArrayList<Object> promotion: (ArrayList<ArrayList<Object>>) promotionNames) {
			String promoId = new Integer((int) promotion.get(0)).toString();
			String promotionName = (String) promotion.get(1);
			String promotionDescription = (String) promotion.get(2);
			String locationName = (String) promotion.get(3);
			String discountPercentage = new Float((float) promotion.get(4)).toString();
			String startDate = (String) ((Date) promotion.get(5)).toString(); 
			String promotionStatus = new Boolean((boolean) promotion.get(6)).toString();
			System.out.println((new Boolean((boolean) promotion.get(6)).toString()));
			if (promotionStatus.equals("true")) {
				promotionStatus = "Active";
			} else {
				promotionStatus = "Unactive";
			}
			
			listView.add(new promotionToTable(promoId, promotionName, promotionDescription, locationName, 
					discountPercentage, startDate, promotionStatus));
		}
		promotionTable.setItems(listView);
		promotionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		promotionTable.getColumns().add(columnButton);
	}
	
	/**
	 * This class is used to set the promotion in the table.
	 * It contains the following properties: promoId, promotionName, promotionDescription, locationName, 
	 * discountPercentage, startDate, promotionStatus
	 * @author DimaKyn
	 *
	 */
	public class promotionToTable {
		
		/**
		 * Getter for the promotion name property
		 * @return the promotion name property
		 */
		public SimpleStringProperty getPromotionName() {
			return promotionName;
		}
		
		/**
		 * Setter for the promotion name property
		 * @param promotionName the promotion name property
		 */
		public void setPromotionName(SimpleStringProperty promotionName) {
			this.promotionName = promotionName;
		}

		/**
		 * Getter for the promotion description property
		 * @return the promotion description property
		 */
		public SimpleStringProperty getPromotionDescription() {
			return promotionDescription;
		}

		/**
		 * Setter for the promotion description property
		 * @param promotionDescription the promotion description property
		 */
		public void setPromotionDescription(SimpleStringProperty promotionDescription) {
			this.promotionDescription = promotionDescription;
		}
		/**
		 * Getter for the location name property
		 * @return the location name property
		 */
		public SimpleStringProperty getLocationName() {
			return locationName;
		}
		/**
		 * Setter for the location name property
		 * @param locationName the location name property
		 * 
		 */
		public void setLocationName(SimpleStringProperty locationName) {
			this.locationName = locationName;
		}

		/**
		 * Getter for the discount percentage property
		 * @return the discount percentage property
		 * 
		 */
		public SimpleStringProperty getDiscountPercentage() {
			return discountPercentage;
		}

		/**
		 * Setter for the discount percentage property
		 * @param discountPercentage the discount percentage property
		 */
		public void setDiscountPercentage(SimpleStringProperty discountPercentage) {
			this.discountPercentage = discountPercentage;
		}

		/**
		 * Getter for the start date property
		 * @return the start date property
		*/
		public SimpleStringProperty getStartDate() {
			return startDate;
		}
		/**
		 * Setter for the start date property
		 * @param startDate the start date property
		 * 
		 */
		public void setStartDate(SimpleStringProperty startDate) {
			this.startDate = startDate;
		}

		SimpleStringProperty promoId;
		SimpleStringProperty promotionName;
		SimpleStringProperty promotionDescription;
		SimpleStringProperty locationName;
		SimpleStringProperty discountPercentage;
		SimpleStringProperty startDate;
		SimpleStringProperty promotionStatus;
		
		/**
		 * @param promoId - the id of the promotion
		 * @param promotionName - the name of the promotion
		 * @param promotionDescription - the description of the promotion
		 * @param locationName - the location name of the promotion
		 * @param discountPercentage - the discount percentage of the promotion
		 * @param startDate - the start date of the promotion
		 * @param promotionStatus - the status of the promotion
		 */
		public promotionToTable(String promoId, String promotionName, String promotionDescription, String locationName,
				String discountPercentage, String startDate,String promotionStatus) {
			this.promoId = new SimpleStringProperty(promoId);
			this.promotionName = new SimpleStringProperty(promotionName);
			this.promotionDescription = new SimpleStringProperty(promotionDescription);
			this.locationName = new SimpleStringProperty(locationName);
			this.discountPercentage = new SimpleStringProperty(discountPercentage);
			this.startDate = new SimpleStringProperty(startDate);
			this.promotionStatus = new SimpleStringProperty(promotionStatus);
		}
		/**
		 * Setter for the promotion status property
		 * @return the promotion status property
		 * 
		 */
		public SimpleStringProperty getPromotionStatus() {
			return promotionStatus;
		}	
		/**
		 * Setter for the promotion status property
		 * @param promotion status property
		 * 
		 */
		public void setPromotionStatus(SimpleStringProperty promotionStatus) {
			this.promotionStatus = promotionStatus;
		}
		/**
		 * Getter for the promotion ID property
		 * @param startDate the start date property
		 * 
		 */
		public SimpleStringProperty getPromoId() {
			return promoId;
		}
		/**
		 * Setter for the promotion ID property
		 * @param startDate the start date property
		 * 
		 */
		public void setPromoId(SimpleStringProperty promoId) {
			this.promoId = promoId;
		}
	}

}
