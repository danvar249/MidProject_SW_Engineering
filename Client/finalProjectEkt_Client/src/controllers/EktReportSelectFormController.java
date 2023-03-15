package controllers;


import java.time.Year;
import java.util.ArrayList;


import client.ClientController;

import client.ClientUI;
import common.SCCP;
import common.ServerClientRequestTypes;
import common.WindowStarter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Role;

/**
 * EktReportSelectFormController is a JavaFX Controller class that handles the GUI interactions and logic for the "Ekt Report Select Form" window.
 * It allows the user to select different types of reports, such as order reports, inventory reports, and customer reports.
 * This class also contains methods to set up the page for different user roles, such as Regional Manager and Division Manager.
 * @author Rotem, Dima, Maxim
 *
 */
public class EktReportSelectFormController extends Application{
	private ArrayList<?> machinesList;
	/**
	 * It is responsible for handling the interactions with the elements on the EktReportSelectForm GUI.
	 */
	@FXML
	private Button btnViewOrderReportSingleRegion;
	
	@FXML
	private Button btnViewCustomerReportsSingleRegion;
	
    @FXML
    private Button btnBack;

    @FXML
    private Button buttonViewCustomerReports;

    @FXML
    private Button buttonViewInventoryReports;

    @FXML
    private Button buttonViewOrderReports;

    @FXML
    public ComboBox<String> comboBoxCustomerReports;

    @FXML
    public ComboBox<String> comboBoxInventoryReports;

    @FXML
    public ComboBox<String> comboBoxMonthCustomerReports;

    @FXML
    public ComboBox<String> comboBoxMonthOrderReports;

    @FXML
    public ComboBox<String> comboBoxOrderReports;

    @FXML
    public ComboBox<String> comboBoxYearCustomerReports;

    @FXML
    public ComboBox<String> comboBoxYearOrderReports;

    @FXML
    private Text customerErrorMessage;

    @FXML
    private Text inventoryErrorMessage;

    @FXML
    private Text orderErrorMessage;

    @FXML
    private Text txtRegion;

    @FXML
    private VBox vboxCEO;

	/**
	 * The initialize method is a JavaFX controller method that is called automatically when the associated FXML file is loaded.
	 * In this method, the program checks the role of the current system user and calls the appropriate set up method for the page.
	 * If the current system user is a Regional Manager, the setUpRegionalManagerPage() method is called.
	 * If the current system user is a Division Manager, the setUpDivisionManagerPage() method is called.
	 * @SuppressWarnings("static-access") is used to suppress warning caused by accessing static variable.
	 * @FXML annotation is used to mark the method as an event handler for the associated FXML element.
	 */
	@SuppressWarnings("static-access")
	@FXML
	public void initialize() {
		if (ClientUI.getClientController().getCurrentSystemUser().getRole().equals(Role.REGIONAL_MANAGER)) {
			setUpRegionalManagerPage();
		} else {
			setUpDivisionManagerPage();
		}
		
	}
	
	/**
	 * This method sets up the Division Manager page by initializing the combo boxes for Order Reports, Inventory Reports, and Customer Reports.
	 * It also sets the months and years for the combo boxes, fetches the list of machines from the server, and populates the combo boxes with the names of the machines.
	 * @SuppressWarnings("unchecked") is used to suppress the unchecked warnings from the type casting of the ArrayList of ArrayLists of objects to ArrayList of ArrayLists of specific object types.
	 */
	@SuppressWarnings("unchecked")
	private void setUpDivisionManagerPage() {
		//Setup orderReports combo box
		ObservableList<String> comboOrders = FXCollections.observableArrayList();
		
		//Setup InventoryReports combo box
		ObservableList<String> comboInventory = FXCollections.observableArrayList();
		
		//Setup CustomerReports combo box
		ObservableList<String> comboCustomers = FXCollections.observableArrayList();
		
		ObservableList<String> comboMonths = FXCollections.observableArrayList(
			"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		
		ObservableList<String> comboYears = FXCollections.observableArrayList();
		
		int year = Year.now().getValue();
		for (int i = 0; i < 10; i++) {
			comboYears.add("" + year--);
		}
		//Set months and years for the combo boxes
		comboBoxMonthOrderReports.setItems(comboMonths);
		comboBoxYearOrderReports.setItems(comboYears);
		comboBoxMonthCustomerReports.setItems(comboMonths);
		comboBoxYearCustomerReports.setItems(comboYears);
		
		SCCP fetchMachines = new SCCP();
		fetchMachines.setRequestType(ServerClientRequestTypes.SELECT);
		fetchMachines.setMessageSent(new Object[] {"machine", false, null, false, null, true, "LEFT JOIN locations on machine.locationId = locations.locationID"});
		
		ClientUI.getClientController().accept(fetchMachines);
		
		//ClientController.getMessageSent() -> returns ArrayList of ArrayListst of objects
		machinesList = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		
		for (ArrayList<Object> machine : (ArrayList<ArrayList<Object>>)machinesList) {
			comboOrders.add((String) machine.get(5) + "-" + (String) machine.get(2));
			comboInventory.add((String) machine.get(5)+ "-" + (String) machine.get(2));
			comboCustomers.add((String) machine.get(5) + "-" + (String) machine.get(2));
		}
		
		//Set combo boxes of 
		comboBoxOrderReports.setItems(comboOrders);
		comboBoxInventoryReports.setItems(comboInventory);
		comboBoxCustomerReports.setItems(comboCustomers);
		
		txtRegion.setText("All regions");

	}

	/**
	 * This method is used to set up the page for the regional manager.
	 * It sets up the combo boxes for order, inventory, and customer reports, and also sets the current region for the user.
	 */
	//Setup CEOpage before launch
	@SuppressWarnings("unchecked")
	public void setUpRegionalManagerPage() {
		//Setup orderReports combo box
		ObservableList<String> comboOrders = FXCollections.observableArrayList();
		
		//Setup InventoryReports combo box
		ObservableList<String> comboInventory = FXCollections.observableArrayList();
		
		//Setup CustomerReports combo box
		ObservableList<String> comboCustomers = FXCollections.observableArrayList();
		
		ObservableList<String> comboMonths = FXCollections.observableArrayList(
			"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		
		ObservableList<String> comboYears = FXCollections.observableArrayList();
		
		int year = Year.now().getValue();
		for (int i = 0; i < 10; i++) {
			comboYears.add("" + year--);
		}
		//Set months and years for the combo boxes
		comboBoxMonthOrderReports.setItems(comboMonths);
		comboBoxYearOrderReports.setItems(comboYears);
		comboBoxMonthCustomerReports.setItems(comboMonths);
		comboBoxYearCustomerReports.setItems(comboYears);
		
		txtRegion.setText(ClientController.getCurrentUserRegion() + " Region");
		txtRegion.setLayoutX(400 - (txtRegion.minWidth(0))/2);
		
		SCCP fetchMachines = new SCCP();
		fetchMachines.setRequestType(ServerClientRequestTypes.SELECT);
		fetchMachines.setMessageSent(new Object[] {"machine", false, null, false, null, true, "LEFT JOIN locations on machine.locationId = locations.locationID WHERE locations.locationName = \""
				+ ClientController.getCurrentUserRegion() + "\" ORDER BY locationName;"});
		ClientUI.getClientController().accept(fetchMachines);
		
		//ClientController.getMessageSent() -> returns ArrayList of ArrayListst of objects
		machinesList = (ArrayList<?>) ClientController.responseFromServer.getMessageSent();
		
		for (ArrayList<Object> machine : (ArrayList<ArrayList<Object>>)machinesList) {
			comboOrders.add((String) machine.get(5) + "-" + (String) machine.get(2));
			comboInventory.add((String) machine.get(5)+ "-" + (String) machine.get(2));
			comboCustomers.add((String) machine.get(5) + "-" + (String) machine.get(2));
		}
		
		//Set combo boxes of 
		comboBoxOrderReports.setItems(comboOrders);
		comboBoxInventoryReports.setItems(comboInventory);
		comboBoxCustomerReports.setItems(comboCustomers);
	}
	/**
	 * 
	 * The getBtnOrderReports method is an event handler method that is triggered when the "View Order Report" button is clicked.
	 * It retrieves the selected month, year and location-machine name from the corresponding combo boxes, and adds them to an array list.
	 * It then opens a new window, passing the array list as a parameter and closes the current window.
	 * @param event The ActionEvent object that is triggered when the button is clicked.
	 * @throws Exception if the new window cannot be opened.
	 */
	public void getBtnOrderReports(ActionEvent event) throws Exception {
		
		comboBoxMonthOrderReports.getSelectionModel().getSelectedItem();
		comboBoxYearOrderReports.getSelectionModel().getSelectedItem();
		
		if (comboBoxMonthOrderReports.getSelectionModel().isEmpty() || 
				comboBoxYearOrderReports.getSelectionModel().isEmpty() || comboBoxOrderReports.getSelectionModel().isEmpty() ){
			orderErrorMessage.setText("Please fill in all required fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxOrderReports.getValue().split("-");
		
		ClientController.getMachineID_TypeOfReport_Dates().add("Orders"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[1]); //Add chosen machineName to the array
		//Add the chosen date to view reports
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxMonthOrderReports.getValue());
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxYearOrderReports.getValue());
		
		for (String s : ClientController.getMachineID_TypeOfReport_Dates()) {
			System.out.println(s);
		}


		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
	}
	/**
	 * 
	 * This method is used to get inventory reports by the user
	 * This method first checks if the inventory report combo box is empty. If it is, it displays an error message.
	 * If not, it extracts the location and machine name from the combo box value.
	 * It then adds the type of report, location and machine name to the array list.
	 * A new stage is created and the EktReportDisplayPage is loaded on it. The primary stage is closed and the new stage is shown.
	 * @param event the action event that triggers this method
	 * @throws Exception when there is an error in creating a new window
	 */
	public void getBtnInventoryReports(ActionEvent event) throws Exception {
		
		if (comboBoxInventoryReports.getSelectionModel().isEmpty() == true) {
			inventoryErrorMessage.setText("Please fill in all required fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxInventoryReports.getValue().split("-");
		
		ClientController.getMachineID_TypeOfReport_Dates().add("Inventory"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[1]); //Add chosen machineName to the array
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
		//Implement Inventory reports
	}
	/**
	 * 
	 * This method is an event handler for the "Customer Reports" button. It is responsible for retrieving the user's selected month, year, and location-machine name from the appropriate combo boxes, and passing this information to the next scene (EktReportDisplayPage.fxml).
	 * If any of the combo boxes are empty, it will display an error message.
	 * @param event the event that triggered the method call
	 * @throws Exception if the next scene (EktReportDisplayPage.fxml) cannot be loaded
	 */
	public void getBtnCustomerReports(ActionEvent event) throws Exception {
		String month = comboBoxMonthCustomerReports.getValue();
		String year = comboBoxYearCustomerReports.getValue();
		
		if (month == null || year == null || comboBoxCustomerReports.getSelectionModel().isEmpty() == true) {
			customerErrorMessage.setText("Please fill in all required fields");
			return;
		}
		
		String[] locationAndMachineName = comboBoxCustomerReports.getValue().split("-");
		
		ClientController.getMachineID_TypeOfReport_Dates().add("Customer"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[0]); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(locationAndMachineName[1]); //Add chosen machineName to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxMonthCustomerReports.getValue());
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxYearCustomerReports.getValue());
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
		//Implement Customer reports
	}
	/**
	 * The start method is the main entry point of the EktReportSelectForm application. It loads the FXML file
	 * "EktReportSelectForm.fxml" which represents the layout and controls of the form. It then sets the scene
	 */
	public void start(Stage primaryStage) throws Exception {
		// Rotem - changed this line - interesting that no one noticed I broke this previously
		Parent root = FXMLLoader.load(getClass().getResource("/gui/EktReportSelectForm.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * 
	 * The getBtnLogout method handles the logout button action event. It creates a new stage and depending on the role of the current user,
	 * it opens either the Regional Manager Home Page or Division Manager Home Page.
	 * @param event the action event of clicking the logout button
	 * @throws Exception if the specified fxml file is not found
	 */
	public void getBtnLogout(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		if (ClientController.getCurrentSystemUser().getRole().equals(Role.REGIONAL_MANAGER)) {
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktRegionalManagerHomePage.fxml", null, "Regional Manager Home Page", true);
		} else {
			WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(), "/gui/EktDivisionManagerHomePage.fxml", null, "Female Division Manager Home Page", true);

		}
		primaryStage.show();  
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
		
	}

	
	//Manager pressed on all reports button
	/**
	 * 
	 * This method is used to handle the event when the "View Customer Reports for Single Region" button is clicked.
	 * It checks if all the required fields, month and year, are filled. If not, it displays an error message.
	 * Otherwise, it adds the required parameters to the list of machineID_TypeOfReport_Dates and opens the EktReportDisplayPage.
	 * @param event the ActionEvent that triggered the method call
	 */
    @FXML
    void getBtnViewCustomerReportsSingleRegion(ActionEvent event) {
    	if (comboBoxMonthCustomerReports.getSelectionModel().isEmpty() || comboBoxYearCustomerReports.getSelectionModel().isEmpty()) {
    		customerErrorMessage.setText("Please fill in all required fields");
			return;
    	}
    	ClientController.getMachineID_TypeOfReport_Dates().add("Customer"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(ClientController.getCurrentUserRegion()); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add("ALL_MACHINES"); //Add chosen machineName to the array
		//Add the chosen date to view reports
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxMonthCustomerReports.getValue());
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxYearCustomerReports.getValue());
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }
    /**
     * 
     * This method is used to handle the event when the button "View Order Report Single Region" is clicked.
     * It checks if the month and year fields are empty. If so, it sets an error message.
     * If not, it adds the type of report, the location, machine name, month and year to the 'ClientController.getMachineID_TypeOfReport_Dates()' array.
     * Then it opens the 'EktReportDisplayPage.fxml' with the 'Ekt Report Display Form' title and closes the current window.
     * @param event the event of clicking the button "View Order Report Single Region"
     */
    @FXML
    void getBtnViewOrderReportSingleRegion(ActionEvent event) {
    	if (comboBoxMonthOrderReports.getSelectionModel().isEmpty() || comboBoxYearOrderReports.getSelectionModel().isEmpty()) {
    		orderErrorMessage.setText("Please fill in all required fields");
    		return;
    	}	
		ClientController.getMachineID_TypeOfReport_Dates().add("Orders"); //Add type of report to view to the array
		ClientController.getMachineID_TypeOfReport_Dates().add(ClientController.getCurrentUserRegion()); //Add chosen location to the array
		ClientController.getMachineID_TypeOfReport_Dates().add("ALL_MACHINES"); //Add chosen machineName to the array
		//Add the chosen date to view reports
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxMonthOrderReports.getValue());
		ClientController.getMachineID_TypeOfReport_Dates().add(comboBoxYearOrderReports.getValue());
		
		Stage primaryStage = new Stage();
		WindowStarter.createWindow(primaryStage, ClientController.getCurrentSystemUser(),
				"/gui/EktReportDisplayPage.fxml", null, "Ekt Report Display Form", true);
		primaryStage.show();
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // closing primary window
    }
}
