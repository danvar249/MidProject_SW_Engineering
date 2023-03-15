package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import client.ClientController;
import client.ClientUI;
import common.InventoryCalculations;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Role;

/**
 * 
 * This class is a JavaFX controller class for a report display page of a vending machine management system.
 * It uses various JavaFX components such as {@link BarChart}, {@link Text}, {@link TableView} to display the data of the report.
 * The class also uses classes such as {@link SCCP} and {@link InventoryCalculations} to gather data for the report and display it on the page.
 * 
 * The class has a method that creates a bar chart using the {@link CategoryAxis} and {@link NumberAxis} classes to set the x-axis and y-axis of the chart respectively.
 * It then sends a message to the server to fetch data on the products in the machine and their stock levels, using the {@link SCCP} class.
 * The data is then added to the chart by creating a new {@link XYChart.Series} object and adding {@link XYChart.Data} objects to it, with the product name and stock level as the values.
 * 
 * Additionally, the code also adds data to a {@link TableView} which is a JavaFX component used to display tabular data.
 * The data being added to the table is an object of the {@link productInTable} class, which contains information such as the product name, stock, minimum stock, threshold, and out of stock.
 * The table view and bar chart are also being styled with different font sizes and background colors.
 * @author Dima, Maxim, Rotem
 *
 */
public class EktReportDisplayPageController {
	
	private int machineID = 0; //Used for setting up the re-stock button, not used elsewhere

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnBack;

	@FXML
	public Text txtMachineName;

	@FXML
	public Text txtLocationName;

	@FXML
	public Text txtTypeOfReport;

	@FXML
	private Pane paneBottom;

	String nameOfLocation, nameOfMachine;
	private Button requestRestockBtn;
	/**
	 * This class defines the structure of a product that is to be displayed in a {@link TableView} on a report page of a vending machine management system.
	 * The class contains several {@link SimpleStringProperty} fields such as pName, pStock, pMinStock, pThreshold and pOutOfStock.
	 * These fields are used to store and display the product name, stock, minimum stock, threshold, and out of stock respectively.
	 * The class also has getter and setter methods for these fields.
	 * 
	 * The class has a constructor that accepts values for these fields, which are then used to initialize the corresponding properties.
	 * @author Dima, Maxim, Rotem
	 *
	 */
	// Used to insert rows to a table
	class productInTable {

		SimpleStringProperty pName;
		SimpleStringProperty pStock;
		SimpleStringProperty pMinStock;
		SimpleStringProperty pThreshold;
		SimpleStringProperty pOutOfStock;
		/**
		 * Constructor for the {@link productInTable} class.
		 * Initializes the class fields with the given values for product name, stock, minimum stock, threshold and out of stock.
		 * @param pName String value of the product name
		 * @param pStock String value of the product stock
		 * @param pMinStock String value of the product minimum stock
		 * @param pThreshold String value of the product threshold
		 * @param pOutOfStock String value of the product out of stock
		 */
		public productInTable(String pName, String pStock, String pMinStock, String pThreshold, String pOutOfStock) {

			this.pName = new SimpleStringProperty(pName);
			this.pStock = new SimpleStringProperty(pStock);
			this.pMinStock = new SimpleStringProperty(pMinStock);
			;
			this.pThreshold = new SimpleStringProperty(pThreshold);
			this.pOutOfStock = new SimpleStringProperty(pOutOfStock);
		}
		/**
		 * Getter method for the pName field in the {@link productInTable} class.
		 * @return The value of the product name as a {@link SimpleStringProperty}
		 */
		public SimpleStringProperty getpName() {
			return pName;
		}
		/**
		 * Setter method for the pName field in the {@link productInTable} class.
		 * @param pName The new value of the product name as a {@link SimpleStringProperty}
		 */
		public void setpName(SimpleStringProperty pName) {
			this.pName = pName;
		}
		/**
		 * Getter method for the pStock field in the {@link productInTable} class.
		 * @return The value of the product stock as a {@link SimpleStringProperty}
		 */
		public SimpleStringProperty getpStock() {
			return pStock;
		}
		/**
		 * Setter method for the pStock field in the {@link productInTable} class.
		 * Sets the value of the product stock.
		 * @param pStock The new value of the product stock as a {@link SimpleStringProperty}
		 */
		public void setpStock(SimpleStringProperty pStock) {
			this.pStock = pStock;
		}
		/**
		 * Getter method for the pMinStock field in the {@link productInTable} class.
		 * @return The value of the product minimum stock as a {@link SimpleStringProperty}
		 */
		public SimpleStringProperty getpMinStock() {
			return pMinStock;
		}
		/**
		 * Setter method for the pMinStock field in the {@link productInTable} class.
		 * Sets the value of the product minimum stock.
		 * @param pMinStock The new value of the product minimum stock as a {@link 
		 */
		public void setpMinStock(SimpleStringProperty pMinStock) {
			this.pMinStock = pMinStock;
		}
		/**
		 * Getter method for the pThreshold field in the {@link productInTable} class.
		 * @return The value of the product threshold as a {@link SimpleStringProperty}
		 */
		public SimpleStringProperty getpThreshold() {
			return pThreshold;
		}
		/**
		 * Setter method for the pThreshold field in the {@link productInTable} class.
		 * Sets the value of the product threshold.
		 * @param pThreshold The new value of the product threshold as a {@link SimpleStringProperty}
		 */
		public void setpThreshold(SimpleStringProperty pThreshold) {
			this.pThreshold = pThreshold;
		}
		/**
		 * Getter method for the pOutOfStock field in the {@link productInTable} class.
		 * @return The value of the product out of stock as a {@link SimpleStringProperty}
		 */
		public SimpleStringProperty getpOutOfStock() {
			return pOutOfStock;
		}
		/**
		 * Setter method for the pOutOfStock field in the {@link productInTable} class.
		 * Sets the value of the product out of stock.
		 * @param pOutOfStock The new value of the product out of stock as a {@link SimpleStringProperty}
		 */
		public void setpOutOfStock(SimpleStringProperty pOutOfStock) {
			this.pOutOfStock = pOutOfStock;
		}

	}
	/**
	 * This method initializes the report based on the type of report selected by the user.
	 * The type of report, location name, and machine name are displayed in the UI.
	 * The method also creates the report (graph or chart) based on the type of report selected.
	 * The types of report that can be generated are Orders, Inventory, and Customer.
	 */
	@FXML
	public void initialize() {
		String typeOfReport = ClientController.getMachineID_TypeOfReport_Dates().get(0);
		txtTypeOfReport.setText(typeOfReport + " Distribution Report");

		nameOfLocation = ClientController.getMachineID_TypeOfReport_Dates().get(1);
		nameOfMachine = "machineName = \"" + ClientController.getMachineID_TypeOfReport_Dates().get(2) + "\"";
		
		String date = "";

		if (!ClientController.getMachineID_TypeOfReport_Dates().get(0).equals("Inventory")) {
			date = ", " + ClientController.getMachineID_TypeOfReport_Dates().get(3) + " " + ClientController.getMachineID_TypeOfReport_Dates().get(4);
		}

		if (ClientController.getMachineID_TypeOfReport_Dates().get(2).equals("ALL_MACHINES")) {
			if (ClientController.getCurrentSystemUser().getRole().equals(Role.DIVISION_MANAGER)) {
				txtLocationName.setText("All Regions" + date);
			} else {
				txtLocationName.setText(ClientController.getCurrentUserRegion() + date);
			}
			txtMachineName.setText("All Machines in this region");
		} else {
			txtLocationName.setText(ClientController.getCurrentUserRegion() + date);
			txtMachineName.setText("Machine: " + ClientController.getMachineID_TypeOfReport_Dates().get(2));
		}
		// Center the text headers
		txtTypeOfReport.setLayoutX(400 - (txtTypeOfReport.minWidth(0)) / 2);
		txtLocationName.setLayoutX(400 - (txtLocationName.minWidth(0)) / 2);
		txtMachineName.setLayoutX(400 - (txtMachineName.minWidth(0)) / 2);

		//// Create graph or chart
		switch (typeOfReport) {
		case "Orders":
			createOrderReport();
			break;

		case "Inventory":
			createInventoryReport();
			break;
		case "Customer":
			createCustomreReport();
			break;
		}

	}
	/**
	 * createCustomreReport is a method that creates a bar chart of the number of customers per number of orders.
	 * The data is fetched from the database, it uses the machine name and the date range to filter the data.
	 * It also creates the x and y axis for the bar chart and sets the label for the x and y axis.
	 * The bar chart is populated with data using the data series and the data is sorted by the number of orders.
	 */
	@SuppressWarnings("unchecked")
	private void createCustomreReport() {
		int month = getMonthNumberByString(ClientController.getMachineID_TypeOfReport_Dates().get(3));
		int year = Integer.parseInt(ClientController.getMachineID_TypeOfReport_Dates().get(4));
		int monthBeginning = month;
		int yearBeginning = year;
		// Fetch data from the database
		if (++month == 13) {
			year++;
			month = 1;
		}
		
		String machineName = ""; 
		if(ClientController.getMachineID_TypeOfReport_Dates().get(2).equals("ALL_MACHINES")) {
			machineName = "";
		} else {
			machineName = "machine.machineName = \"" + ClientController.getMachineID_TypeOfReport_Dates().get(2) + "\" AND ";
		}
		
		
		xAxis = new CategoryAxis();
		yAxis = new NumberAxis();
		xAxis.setLabel("Number Of Orders Per Customer");
		xAxis.setTickLabelFill(Color.BLACK);
		yAxis.setLabel("Number Of Customers");
		yAxis.setTickLabelFill(Color.BLACK);
		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
		XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>();
		dataSeries1.setName("Customers");
		
		System.out.println("MACHINE NAME: " + machineName + "");
		TreeMap<Long, Set<Integer>> customersMap = new TreeMap<>();
		
		SCCP fetchCustomersFromDatabase = new SCCP();
		fetchCustomersFromDatabase.setRequestType(ServerClientRequestTypes.SELECT);
		fetchCustomersFromDatabase.setMessageSent(new Object[] {"customer_orders", true, "customerId, count(*)", false, null, 
				true, "LEFT JOIN ektdb.orders on orders.orderID = customer_orders.orderId LEFT JOIN ektdb.machine on machine.machineId = customer_orders.machineId "
						+ " WHERE " + machineName + " date_received >= \"" + yearBeginning + "-" + monthBeginning + "-01\"" 
						+ " AND date_received < \"" + year + "-" + month + "-01\" " + "GROUP BY customerId"});
		ClientUI.getClientController().accept(fetchCustomersFromDatabase);
		
		//[(ID, numOrders), (ID, numOrders), (ID, numOrders), ...., (ID, numOrders)]
		ArrayList<?> customerIDAndNumOfOrders = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		
		for (ArrayList<Object> custIdNumOrders : (ArrayList<ArrayList<Object>>) customerIDAndNumOfOrders) {
			if (customersMap.get(custIdNumOrders.get(1)) == null) {
				Set<Integer> arrayOfIDsAsValue = new HashSet<>();
				customersMap.put((Long) custIdNumOrders.get(1), arrayOfIDsAsValue);
			}
			customersMap.get(custIdNumOrders.get(1)).add((Integer) custIdNumOrders.get(0));
		}
		
		ArrayList<Long> arrayOfNumberOfOrders = new ArrayList<>();
		
		for(Long integer: customersMap.keySet()) {
			arrayOfNumberOfOrders.add(integer);
		}
		
		System.out.println(arrayOfNumberOfOrders.toString());
		System.out.println(customersMap.keySet().toString());
		System.out.println(customersMap.values().toString());
		
		Long maxValue = (long) 1;
		if (!arrayOfNumberOfOrders.isEmpty()) {
			maxValue = arrayOfNumberOfOrders.get(arrayOfNumberOfOrders.size() - 1);
		}
		
		Long i;
		for(i = (long) 1; i <= maxValue; i++) {
			
			if (!(customersMap.get(i) == null)) {
				dataSeries1.getData().add(new Data<String, Number>(i + "", customersMap.get(i).size()));
			} else {
				dataSeries1.getData().add(new Data<String, Number>(i + "", 0));
			}
		}
		barChart.getData().add(dataSeries1);
		barChart.getStylesheets().add("controllers/testCss.css");
		barChart.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: linear-gradient(from 0px 0px to 0px 1500px, #CBC3E3, black); "
				+ "-fx-font-color: black; -fx-font: black; -fx-background-color: transparent;");
		borderPane.setCenter(barChart);
	}
	/**
	 * The method createInventoryReport() creates an inventory report for a specific machine. 
	 * It creates a TableView for displaying product stock information, 
	 * and sets up the columns for product name, in stock, threshold, 
	 * and estimated time till threshold and out-of-stock. 
	 * It also creates an x-axis and y-axis for a graph using the CategoryAxis and NumberAxis classes, 
	 * respectively. The method retrieves data from the database and populates the table and graph with the retrieved data.
	 */
	@SuppressWarnings("unchecked")
	private void createInventoryReport() {

		final ObservableList<productInTable> data = FXCollections.observableArrayList();

		TableView<productInTable> tableViewForProductStockInfo = new TableView<productInTable>();
		tableViewForProductStockInfo.getStylesheets().add("/gui/tableviewCSS.css");
		tableViewForProductStockInfo
				.setStyle("-fx-background: PURPLE; -fx-background-color: transparent; -fx-font-weight: bold;");
		tableViewForProductStockInfo.setMinWidth(800);

		// Column 1
		TableColumn<productInTable, String> productNameColumn = new TableColumn<>("Product Name");
		productNameColumn.setCellValueFactory(cellData -> cellData.getValue().getpName());
		productNameColumn.setMinWidth(172);
		productNameColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		// Column 2
		TableColumn<productInTable, String> inStockColumn = new TableColumn<>("In Stock");
		inStockColumn.setCellValueFactory(cellData -> cellData.getValue().getpStock());
		inStockColumn.setMinWidth(56);
		inStockColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		// Column 3
		TableColumn<productInTable, String> minStockColumn = new TableColumn<>("Threshold");
		minStockColumn.setCellValueFactory(cellData -> cellData.getValue().getpMinStock());
		minStockColumn.setMinWidth(56);
		minStockColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		// Column 4
		TableColumn<productInTable, String> estTimeTillThresholdColumn = new TableColumn<>(
				"Will reach threshold in approxi.");
		estTimeTillThresholdColumn.setCellValueFactory(cellData -> cellData.getValue().getpThreshold());
		estTimeTillThresholdColumn.setMinWidth(236);
		estTimeTillThresholdColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		// Column 5
		TableColumn<productInTable, String> estTimeTillEmptyColumn = new TableColumn<>("Out-of-stock in approx.");
		estTimeTillEmptyColumn.setCellValueFactory(cellData -> cellData.getValue().getpOutOfStock());
		estTimeTillEmptyColumn.setMinWidth(232);
		estTimeTillEmptyColumn.setStyle("-fx-alignment: CENTER; "
				+ "-fx-background-color:  linear-gradient(from 0px 0px to 0px 400,#e6e6fa , INDIGO); "
				+ "-fx-background: white;");
		tableViewForProductStockInfo.setMaxHeight(159);

		xAxis = new CategoryAxis();
		yAxis = new NumberAxis();
		yAxis.setLabel("Current Stock");
		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
		// Fetch data from the database
		SCCP fetchInventoryMessage = new SCCP();
		fetchInventoryMessage.setRequestType(ServerClientRequestTypes.SELECT);
		fetchInventoryMessage.setMessageSent(new Object[] { "products_in_machine", true,
				"productName, stock, min_stock, max_stock, restock_flag, category, products_in_machine.machineID", false, null, true,
				"LEFT JOIN ektdb.product on products_in_machine.productID = product.productID\r\n"
						+ "LEFT JOIN ektdb.machine on machine.machineId = products_in_machine.machineID\r\n" + "WHERE "
						+ nameOfMachine + " AND category IS NOT NULL;" });
		ClientUI.getClientController().accept(fetchInventoryMessage);

		XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<String, Number>();
		dataSeries1.setName("Products");

		ArrayList<?> inventoryInCurrentMachine = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();

		for (ArrayList<Object> productInMachine : (ArrayList<ArrayList<Object>>) inventoryInCurrentMachine) {
			String productName = (String) productInMachine.get(0);
			int stock = (int) productInMachine.get(1);
			int min_stock = (int) productInMachine.get(2);
			productInMachine.get(3);
			productInMachine.get(4);
			String category = (String) productInMachine.get(5);
			machineID = (int) productInMachine.get(6);

			Integer tmpStock = new Integer(stock);
			dataSeries1.getData().add(new XYChart.Data<String, Number>(productName + ": " + tmpStock, stock));

			category = category.replaceAll(" ", "_").toUpperCase();
			String threshold = null;
			String outOfStock = null;
			// This handles the case where the threshold (A.K.A min_stock) has still not
			// passed
			if (stock >= min_stock) {
				threshold = Math.round(InventoryCalculations.valueOf(category).getValue() * 24) + " Hours";
				outOfStock = Math.round(InventoryCalculations.valueOf(category).getValue() * 29) + " Hours";
		
			} 
			else if (stock == 0) {
				threshold = "Below the threshold! Needs A Restock!";
				outOfStock = "Out of stock!";
				
			}
			else {
				threshold = "Below the threshold! Needs A Restock!";
				outOfStock = Math.round(InventoryCalculations.valueOf(category).getValue() * 5) + " Hours";
			}

			data.add(new productInTable(productName, new Integer(stock).toString(), new Integer(min_stock).toString(),
					threshold, outOfStock));
		}

		barChart.getData().add(dataSeries1);
		barChart.getStylesheets().add("controllers/testCss.css");
		barChart.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-background-color: linear-gradient(from 0px 0px to 0px 1500px, #CBC3E3, black); "
				+ "-fx-font-color: black; -fx-font: black; -fx-background-color: transparent;");
		// Adding data to the table
		tableViewForProductStockInfo.setItems(data);
		tableViewForProductStockInfo.getStylesheets().add("controllers/testCss.css");
		tableViewForProductStockInfo.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: linear-gradient(from 0px 0px to 0px 1500px, #CBC3E3, black); "
				+ "-fx-font-color: black; -fx-font: black; -fx-background-color: transparent;");
		tableViewForProductStockInfo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableViewForProductStockInfo.getColumns().addAll(productNameColumn, inStockColumn, minStockColumn,
				estTimeTillThresholdColumn, estTimeTillEmptyColumn);
		
		// Create a button to request a restock only if the current user is a regional manager
		if (ClientController.getCurrentSystemUser().getRole().equals(Role.REGIONAL_MANAGER)) {
			requestRestockBtn = new Button("REQUEST RESTOCK");
			requestRestockBtn.setMinWidth(100);
			requestRestockBtn.setMinHeight(50);
			requestRestockBtn.setStyle("-fx-background-color: purple; -fx-border-color: white; -fx-border-width: 3px;"
					+ "-fx-border-radius: 3; -fx-background-radius: 10; -fx-font: Berlin Sans FB;"
					+ "-fx-font-size: 20; -fx-text-fill: white;");
			paneBottom.getChildren().add(requestRestockBtn);
			requestRestockBtn.setLayoutX(580);
			requestRestockBtn.setLayoutY(40);

			// Create the alert which asks the manager if they want to create a restock
			// request for this machine
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Request A Restock?");
			alert.setHeaderText("Are you sure you would like to request a restock for the "
					+ ClientController.getMachineID_TypeOfReport_Dates().get(2) + " machine?");
			alert.setContentText("By clicking \"Confirm\" a request will be sent to the local operational worker.");

			requestRestockBtn.setOnAction(action -> {
				Optional<ButtonType> option = alert.showAndWait();
				// If manager decided to create a restock order
				if (option.get() == ButtonType.OK) {
					SCCP raiseRestockFlagInDB = new SCCP();
					raiseRestockFlagInDB.setRequestType(ServerClientRequestTypes.UPDATE);
					raiseRestockFlagInDB.setMessageSent(new Object[] {"products_in_machine", "products_in_machine.restock_flag = 1",
							"products_in_machine.machineID = "  + machineID + " AND products_in_machine.stock < products_in_machine.min_stock"});
					ClientUI.getClientController().accept(raiseRestockFlagInDB);
				} else {
					return;
				}
			});
		}
		//Commit Test		
		borderPane.setBottom(tableViewForProductStockInfo);
		borderPane.setCenter(barChart);

	}
	/**
	 * This method createOrderReport is used to create an order report for a specific machine or all machines in a region. 
	 * The method starts by fetching order data from the database based on the specified month and year, and the machine id. 
	 * It then parses the data to calculate the profits, order quantity, amount of items, and counts the number of local, pickup, and delivery orders. 
	 * After that, it creates a pie chart to visualize the data and displays it on the screen.
	 */
	@SuppressWarnings("unchecked")
	private void createOrderReport() {
		int month = getMonthNumberByString(ClientController.getMachineID_TypeOfReport_Dates().get(3));
		int year = Integer.parseInt(ClientController.getMachineID_TypeOfReport_Dates().get(4));

		int monthBeginning = month;
		int yearBeginning = year;
		// Fetch data from the database
		SCCP fetchOrdersMessage = new SCCP();
		if (++month == 13) {
			year++;
			month = 1;
		}
		
		if (ClientController.getMachineID_TypeOfReport_Dates().get(2).equals("ALL_MACHINES")) {
			// Show all machines in region
			nameOfMachine = "";
		} else {
			// Show specific machine
			// nameOfMachine = "AND machineName = \"name of machine\""
			nameOfMachine = " AND " + nameOfMachine;
		}
		fetchOrdersMessage.setRequestType(ServerClientRequestTypes.SELECT);
		fetchOrdersMessage.setMessageSent(
				new Object[] { "machine", true, "orderID, total_price, total_quantity, typeId", false, null, true,
						"LEFT JOIN ektdb.orders on machine.machineId = orders.machineID"
								+ " LEFT JOIN ektdb.locations on locations.locationID = machine.locationId "
								+ "WHERE total_quantity IS NOT NULL AND date_received >= \"" + yearBeginning + "-"
								+ monthBeginning + "-01" + "\" AND date_received < \"" + year + "-" + month + "-01"
								+ "\"" + nameOfMachine + ";" });

		ClientUI.getClientController().accept(fetchOrdersMessage);

		ArrayList<?> ordersInCurrentMachine = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();

		double profits = 0;
		int orderQuantity = 0;
		int amountOfItems = 0;
		// Counts number of orders
		int localOrdersCounter = 0;
		int pickupOrdersCounter = 0;
		int deliveryOrdersCounter = 0;

		PieChart pChart;

		if (ordersInCurrentMachine.isEmpty()) {
			System.out.println("Empty list!");
		} else {

			for (ArrayList<Object> orders : (ArrayList<ArrayList<Object>>) ordersInCurrentMachine) {
				amountOfItems += (Integer) orders.get(2);
				profits += (double) orders.get(1);

				// Get the 4th column's (typeID) of our SQL query
				switch (((Integer) orders.get(3))) {
				// This row is a Pickup order
				case 1:
					pickupOrdersCounter++;
					orderQuantity++;
					break;
				// This row is a delivery order
				case 2:
					deliveryOrdersCounter++;
					orderQuantity++;
					break;
				// This row is a local order
				case 3:
					localOrdersCounter++;
					orderQuantity++;
					break;
				default:
					continue;
				}
			}
			// Create an observable list for the pie chart
			ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
					new PieChart.Data("Pickup Orders: " + pickupOrdersCounter, pickupOrdersCounter),
					new PieChart.Data("Delivery Orders: " + deliveryOrdersCounter, deliveryOrdersCounter),
					new PieChart.Data("  Local Orders: " + localOrdersCounter, localOrdersCounter));
			pChart = new PieChart(pieData);
			pChart.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
			borderPane.setCenter(pChart);
		}
		Text txtProfits = new Text("Profits: " + profits + "$");
		txtProfits.setFont(Font.font("Berlin Sans FB", 22));
		Text txtorderQuantity = new Text("Order quantity: " + orderQuantity);
		txtorderQuantity.setFont(Font.font("Berlin Sans FB", 22));
		Text txtamountOfItems = new Text("Products sold: " + amountOfItems);
		txtamountOfItems.setFont(Font.font("Berlin Sans FB", 22));

		VBox vboxInfo = new VBox();
		vboxInfo.setMinWidth(800);
		vboxInfo.setAlignment(Pos.CENTER);
		
		vboxInfo.getChildren().add(txtProfits);
		//This is just to create spacing between lines
		Text tmpTxt1 = new Text();
		vboxInfo.getChildren().add(tmpTxt1);
		
		vboxInfo.getChildren().add(txtorderQuantity);

		//This is just to create spacing between lines
		Text tmpTxt2 = new Text();
		vboxInfo.getChildren().add(tmpTxt2);

		vboxInfo.getChildren().add(txtamountOfItems);
		borderPane.setBottom(vboxInfo);

	}
	/**
	 * 
	 * A method that is triggered when the "Back" button is pressed. 
	 * It clears the array used for the current report, opens the "Ekt Report Select Form" window, and closes the current window.
	 * @param event the ActionEvent that triggers this method
	 */
	@FXML
	void getBtnBack(ActionEvent event) {
		// Clear the array for the next report viewing
		ClientController.getMachineID_TypeOfReport_Dates().clear();
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportSelectForm.fxml", null, "Ekt Report Select Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
	/**
	 * 
	 * This method will take a string as input and return the corresponding number of the month.
	 * @param string - A string representing the month name
	 * @return int - the corresponding number of the month. If input does not match any month name, it will return 0
	 */
	private int getMonthNumberByString(String string) {
		switch (string) {
		case "January":
			return 1;
		case "February":
			return 2;
		case "March":
			return 3;
		case "April":
			return 4;
		case "May":
			return 5;
		case "June":
			return 6;
		case "July":
			return 7;
		case "August":
			return 8;
		case "September":
			return 9;
		case "October":
			return 10;
		case "November":
			return 11;
		case "December":
			return 12;
		default:
			return 0;
		}

	}

}
