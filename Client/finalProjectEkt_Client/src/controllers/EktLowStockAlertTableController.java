package controllers;

import java.util.ArrayList;

import client.ClientController;
import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * The EktLowStockAlertTableController class is the controller for displaying
 * low stock alerts for a specific manager location in a table format. This
 * class is responsible for displaying the low stock alerts in a grid pane, with
 * each alert being represented by a row in the grid pane.
 *
 * @author Maxim, Rotem, Dima
 *
 * @version 1.0
 *
 */
public class EktLowStockAlertTableController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnBack;

	private GridPane gridPane;

	/**
	 * Initialize method is called by the FXMLLoader when the controller is created.
	 * This method is responsible for creating the low stock alert table by querying
	 * the server for low stock alerts and populating the grid pane with the alerts.
	 */
	public void initialize() {
		VBox lowStockVBox = new VBox();
		ScrollPane centerScrollBar = new ScrollPane(lowStockVBox);

		centerScrollBar.setPrefHeight(600);
		centerScrollBar.setPrefWidth(800);
		centerScrollBar.getStylesheets().add("controllers/testCss.css");
		centerScrollBar.setStyle(
				"-fx-background-color: transparent; -fx-background-color:   linear-gradient(from -200px 0px to 0px 1800px, #a837b4, transparent);");

		gridPane = new GridPane();

		final int numCols = 6;
		for (int i = 0; i < numCols; i++) {
			ColumnConstraints colConst = new ColumnConstraints();
			colConst.setPercentWidth(800 / 6);
			gridPane.getColumnConstraints().add(colConst);
		}

		gridPane.setMaxSize(770, Region.USE_COMPUTED_SIZE);
		gridPane.setPrefSize(800 - 2, 550);

		Integer managerLocationID = ClientController.getCurrentSystemUser().getId();

		SCCP getLowStockInfo = new SCCP();
		getLowStockInfo.setRequestType(ServerClientRequestTypes.SELECT);
		getLowStockInfo.setMessageSent(new Object[] {
				"machine JOIN products_in_machine ON machine.machineID = products_in_machine.machineID"
						+ " JOIN product ON products_in_machine.productID = product.productID"
						+ " JOIN manager_location ON machine.locationId = manager_location.locationId",
				true,
				"machine.machineID, machine.machineName, product.productID, product.productName, products_in_machine.stock, machine.locationId",
				true,
				"manager_location.idRegionalManager =" + managerLocationID + " AND products_in_machine.stock <=" + 10,
				false, null });

		ClientUI.getClientController().accept(getLowStockInfo);

		int i = 0, j = 0;
		if (ClientController.responseFromServer.getRequestType().equals(ServerClientRequestTypes.ACK)) {
			System.out.println("I got it good");
			// Might want to check this suppression
			ArrayList<?> arrayOfLowStock = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
			System.out.println(arrayOfLowStock);
			for (Object alert : arrayOfLowStock) {
				System.out.println(alert);
				// Main product hbox
				// HBox lowStockHBox = new HBox();

				// ProductName + ProductID + ProductPrice
				// VBox stockDetails = new VBox();

				Text txtMachineID = new Text();
				txtMachineID.setText("Machine id: " + ((ArrayList<?>) alert).get(0).toString());
				txtMachineID.setFill(Color.WHITE);
				txtMachineID.setStyle("-fx-font: 13 System; -fx-font-weight: bold;");

				Text txtMachineName = new Text();
				txtMachineName.setText(((ArrayList<?>) alert).get(1).toString());
				txtMachineName.setFill(Color.WHITE);
				txtMachineName.setStyle("-fx-font: 13 System; -fx-font-weight: bold;");

				Text txtProductID = new Text();
				txtProductID.setText("Product id: " + ((ArrayList<?>) alert).get(2).toString());
				txtProductID.setFill(Color.WHITE);
				txtProductID.setStyle("-fx-font: 13 System; -fx-font-weight: bold;");

				Text txtProductName = new Text();
				txtProductName.setText(((ArrayList<?>) alert).get(3).toString());
				txtProductName.setFill(Color.WHITE);
				txtProductName.setStyle("-fx-font: 13 System; -fx-font-weight: bold;");

				Text txtStockLvl = new Text();
				txtStockLvl.setText("Stock: " + ((ArrayList<?>) alert).get(4).toString());
				txtStockLvl.setFill(Color.WHITE);
				txtStockLvl.setStyle("-fx-font: 13 System; -fx-font-weight: bold;");

				Text txtLocationID = new Text();
				txtLocationID.setText("Location id: " + ((ArrayList<?>) alert).get(5).toString());
				txtLocationID.setFill(Color.WHITE);
				txtLocationID.setStyle("-fx-font: 13 System; -fx-font-weight: bold;");

				gridPane.add(txtMachineID, j, i);
				GridPane.setHalignment(txtMachineID, HPos.CENTER);

				j++;
				gridPane.add(txtMachineName, j, i);
				GridPane.setHalignment(txtMachineName, HPos.CENTER);

				j++;
				gridPane.add(txtProductID, j, i);
				GridPane.setHalignment(txtProductID, HPos.CENTER);

				j++;
				gridPane.add(txtProductName, j, i);
				GridPane.setHalignment(txtProductName, HPos.CENTER);

				j++;
				gridPane.add(txtStockLvl, j, i);
				GridPane.setHalignment(txtStockLvl, HPos.CENTER);

				j++;
				gridPane.add(txtLocationID, j, i);
				GridPane.setHalignment(txtLocationID, HPos.CENTER);

				i++;
				j = 0;

			}
		}

		lowStockVBox.getChildren().add(gridPane);
		borderPane.setCenter(centerScrollBar);

	}

	/**
	 * 
	 * The getBtnBack method is an event handler method that is called when the
	 * "Back" button is pressed. This method is responsible for creating a new stage
	 * for the Ekt Regional Manager Home Page, displaying it, and closing the
	 * current stage.
	 * 
	 * @param event The ActionEvent object that is passed when the "Back" button is
	 *              pressed.
	 * 
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktRegionalManagerHomePage.fxml", null, "Ekt Regional Manager Home Page", false);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}

}
